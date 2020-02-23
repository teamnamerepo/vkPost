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
public class StartProcessor implements UpdateHandler<Message> {

    private final ChatManagerRepository chatManagerRepository;
    @Override
    public Action getStatus() {
        return Action.START;
    }

    @Override
    public void handleUpdate(Message update, Sender sender, ChatManager chatManager) {

        ChatManager cm = new ChatManager();
        cm.setTelegramId(update.getFrom().getId());

        chatManagerRepository.save(cm);

        sender.send(
                new SendMessage()
                        .setReplyMarkup(KeyboardUtil.getCommonKeyboard())
                        .setText("great")
                        .setChatId(update.getFrom()
                                .getId()
                                .longValue()
                        )
        );
    }
}