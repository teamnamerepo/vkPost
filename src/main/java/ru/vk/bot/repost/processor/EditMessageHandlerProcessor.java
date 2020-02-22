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

@Component
@AllArgsConstructor
public class EditMessageHandlerProcessor implements UpdateHandler<Message> {

    private final ChatManagerRepository chatManagerRepository;

    @Override
    public Action getStatus() {
        return Action.EDIT_MESSAGE;
    }

    @Override
    public void handleUpdate(Message update, Sender sender, ChatManager chatManager) {

        chatManager.getCurrentCompetition().setAction(Action.CHANGE_TEXT);
        chatManagerRepository.save(chatManager);

        sender.send(
                new SendMessage(
                        update.getFrom()
                                .getId()
                                .longValue(),
                        "Введите текст"));
    }
}
