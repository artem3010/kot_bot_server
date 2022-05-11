package com.kot.bot.demo.client;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class MessageClient {

    private RestTemplate restTemplate;

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

    //TODO::add try/catch
    @SneakyThrows
    public void sendPhoto(Long chatId, File photo) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
        ContentDisposition contentDisposition = ContentDisposition
                .builder("form-data")
                .name("photo")
                .filename(photo.getName())
                .build();

        fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
        HttpEntity<byte[]> fileEntity = new HttpEntity<>(Files.readAllBytes(photo.toPath()), fileMap);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("chat_id", chatId);
        body.add("file", fileEntity);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        String photoUrl = url + "/sendPhoto";

        String result = restTemplate.exchange(photoUrl, HttpMethod.POST, requestEntity, String.class).toString();
        logger.log(Level.INFO, "Photo had sent: {0}", result);
    }

    @PostConstruct
    public void init() {
        restTemplate = new RestTemplate();
    }

}
