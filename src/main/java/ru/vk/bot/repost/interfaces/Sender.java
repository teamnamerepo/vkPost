package ru.vk.bot.repost.interfaces;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@FunctionalInterface
public interface Sender {

    Message send(SendMessage message);
}
