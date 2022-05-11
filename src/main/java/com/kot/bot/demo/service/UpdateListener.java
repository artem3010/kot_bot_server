package com.kot.bot.demo.service;

import com.kot.bot.demo.service.interfaces.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class UpdateListener {

    @Autowired
    MessageService messageService;


    @KafkaListener(containerFactory = "kafkaListenerContainerFactory", groupId = "updateListeners", topics = {"updates"})
    public void consume(Update update) {
            messageService.processUpdate(update.getMessage());
    }

}
