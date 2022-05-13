package com.kot.bot.demo.client;

import com.kot.bot.demo.exceptions.FileIdNotReturnedException;
import com.kot.bot.demo.model.domain.Photo;
import com.kot.bot.demo.model.dto.MessageResponseDto;
import com.kot.bot.demo.model.dto.PhotoRequestDto;
import com.kot.bot.demo.repository.KotPhotoRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class MessageClient {

    private RestTemplate restTemplate;

    private final KotPhotoRepository kotPhotoRepository;

    private Logger logger = Logger.getLogger(MessageClient.class.getName());

    @Value("${telegram.url}")
    private String url;


    public void sendTextMessage(SendMessage sendMessage) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SendMessage> entity = new HttpEntity<>(sendMessage, headers);
        String messageUrl = url + "/sendMessage";
        String result = restTemplate.postForEntity(messageUrl, entity, String.class).toString();
        logger.log(Level.INFO, "Success message sent: {0}", result);
    }

    public void sendPhoto(String chatId, Photo photo) {
        String photoUrl = url + "/sendPhoto";
        ResponseEntity<MessageResponseDto> result;
        if (Strings.isEmpty(photo.getFileId())) {
            result = restTemplate.exchange(photoUrl, HttpMethod.POST, getFileHttpEntity(chatId, photo), MessageResponseDto.class);
            photo.setFileId(Optional.of(result.getBody().getResult().getPhoto()[0]).orElseThrow(() -> {
                throw new FileIdNotReturnedException("Didn't return exception");
            }).getFileId());
            kotPhotoRepository.save(photo);
            logger.log(Level.INFO, "Photo's file_id was save: {0}", result);
        } else {
            result = restTemplate.exchange(photoUrl, HttpMethod.POST, getFileIdHttpEntity(chatId, photo), MessageResponseDto.class);
        }
        logger.log(Level.INFO, "Photo has been sent: {0}", result);
    }

    private HttpEntity<PhotoRequestDto> getFileIdHttpEntity(String chatId, Photo photo) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(PhotoRequestDto.builder()
                .chatId(chatId)
                .photo(photo.getFileId())
                .build(),
                headers);
    }

    private HttpEntity<MultiValueMap<String, Object>> getFileHttpEntity(String chatId, Photo photo) {
        File photoFile = photo.getFile();
        MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
        ContentDisposition contentDisposition = ContentDisposition
                .builder("form-data")
                .name("photo")
                .filename(photoFile.getName())
                .build();

        fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
        HttpEntity<byte[]> fileEntity = null;
        try {
            fileEntity = new HttpEntity<>(Files.readAllBytes(photoFile.toPath()), fileMap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("chat_id", chatId);
        body.add("file", fileEntity);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        return new HttpEntity<>(body, headers);


    }

    @PostConstruct
    public void init() {
        restTemplate = new RestTemplate();
    }

}
