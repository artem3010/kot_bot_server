package com.kot.bot.demo.service.interfaces;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface MessageService {

    void processUpdate(Message message) throws RuntimeException;
}
