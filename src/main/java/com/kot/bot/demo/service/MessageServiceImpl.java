package com.kot.bot.demo.service;

import com.kot.bot.demo.client.MessageClient;
import com.kot.bot.demo.repository.AnswerRepository;
import com.kot.bot.demo.repository.KotPhotoRepository;
import com.kot.bot.demo.service.interfaces.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageClient messageClient;
    @Autowired
    private KotPhotoRepository kotPhotoRepository;
    @Autowired
    private AnswerRepository answerRepository;

    //TODO:: add scheduled method for processing failed messages
    private List<Message> failedMessages = new LinkedList<>();

    @Override
    public void processUpdate(Message message) throws RuntimeException {
        if (message.getText().toLowerCase().contains("кот")) {
            sendPhoto(message);
        } else {
            sendAnswer(message);
        }

    }

    private void sendAnswer(Message message) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(message.getChatId().toString())
                .text(
                        answerRepository.getRandomAnswer()
                                .orElseThrow(() -> {
                                    failedMessages.add(message);
                                    return new RuntimeException("Exception in getting answer");
                                }).getAnswer()
                )
                .build();
        messageClient.sendTextMessage(sendMessage);
    }

    private void sendPhoto(Message message) {
        messageClient.sendPhoto(message.getChatId(), new File(kotPhotoRepository.getRandomPhoto()
                .orElseThrow(() -> {
                    failedMessages.add(message);
                    return new RuntimeException("Exception in getting photo");
                })
                .getPath()));
    }


}
