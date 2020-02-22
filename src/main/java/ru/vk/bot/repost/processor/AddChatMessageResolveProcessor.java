package ru.vk.bot.repost.processor;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.vk.bot.repost.entities.ChatManager;
import ru.vk.bot.repost.enums.Action;
import ru.vk.bot.repost.interfaces.Sender;
import ru.vk.bot.repost.interfaces.UpdateHandler;

@Component
public class AddChatMessageResolveProcessor implements UpdateHandler<Message> {
    @Override
    public Action getStatus() {
        return Action.ADD_CHAT;
    }

    @Override
    public void handleUpdate(Message update, Sender sender, ChatManager chatManager) {
        sender.send(
                new SendMessage(
                        update.getFrom()
                                .getId()
                                .longValue(),
                        "Перешлите сообщение из группы, " +
                                "в которой вы являетесь администратором" +
                                " или создателем. Бот также должен быть администратором этой группы"));
    }
}
