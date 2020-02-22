package ru.vk.bot.repost.processor;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.vk.bot.repost.entities.ChatManager;
import ru.vk.bot.repost.enums.Action;
import ru.vk.bot.repost.interfaces.Sender;
import ru.vk.bot.repost.interfaces.UpdateHandler;
import ru.vk.bot.repost.repository.ChatManagerRepository;
import ru.vk.bot.repost.util.KeyboardUtil;

@Component
@AllArgsConstructor
public class CancelProcessor implements UpdateHandler<Message> {

    private final ChatManagerRepository chatManagerRepository;

    @Override
    public Action getStatus() {
        return Action.CANCEL;
    }

    @Override
    public void handleUpdate(Message update, Sender sender, ChatManager chatManager) {

        chatManager.setCurrentCompetition(null);

        chatManagerRepository.save(chatManager);

        sender.send(
                new SendMessage(
                        update.getFrom()
                                .getId()
                                .longValue(),
                        "canceled")
                        .setReplyMarkup(KeyboardUtil.getCommonKeyboard())
        );
    }
}
