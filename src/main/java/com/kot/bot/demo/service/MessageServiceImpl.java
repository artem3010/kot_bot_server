package com.kot.bot.demo.service;

import com.kot.bot.demo.client.MessageClient;
import com.kot.bot.demo.exceptions.RandomAnswerGetException;
import com.kot.bot.demo.exceptions.RandomPhotoGetException;
import com.kot.bot.demo.repository.AnswerRepository;
import com.kot.bot.demo.repository.KotPhotoRepository;
import com.kot.bot.demo.service.interfaces.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Iterator;
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
                                    return new RandomAnswerGetException("Exception in getting answer");
                                }).getTextMessage()
                )
                .build();
        messageClient.sendTextMessage(sendMessage);
    }

    private void sendPhoto(Message message) {
        messageClient.sendPhoto(message.getChatId().toString(), kotPhotoRepository.getRandomPhoto()
                .orElseThrow(() -> {
                    throw new RandomPhotoGetException("Exception in getting photo");
                }));
    }

    @Scheduled(fixedRate = 10000)
    private void processFailedMessages() {
        if (!failedMessages.isEmpty()) {
            Iterator<Message> iterator = failedMessages.iterator();
            while (iterator.hasNext()){
                processUpdate(iterator.next());
                iterator.remove();
            }
        }
    }


}
