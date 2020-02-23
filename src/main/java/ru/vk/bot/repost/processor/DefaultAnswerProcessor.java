package ru.vk.bot.repost.processor;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.vk.bot.repost.entities.ChatManager;
import ru.vk.bot.repost.enums.Action;
import ru.vk.bot.repost.interfaces.Sender;
import ru.vk.bot.repost.interfaces.UpdateHandler;

@Component
public class DefaultAnswerProcessor implements UpdateHandler<Message> {

    @Override
    public Action getStatus() {
        return Action.DEFAULT;
    }

    @Override
    public void handleUpdate(Message update, Sender sender, ChatManager chatManager) {
        sender.send(
                new SendMessage(
                    update.getFrom().getId().longValue(),
                    "Выберите команду для продолжения"
                )
        );
    }
}
