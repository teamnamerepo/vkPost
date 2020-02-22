package ru.vk.bot.repost.processor;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.vk.bot.repost.entities.ChatManager;
import ru.vk.bot.repost.enums.Action;
import ru.vk.bot.repost.interfaces.Sender;
import ru.vk.bot.repost.interfaces.UpdateHandler;
import ru.vk.bot.repost.repository.ChatManagerRepository;
import ru.vk.bot.repost.repository.ManagedChatRepository;

@Component
@AllArgsConstructor
public class DefineCurrentChatProcessor implements UpdateHandler<CallbackQuery> {

    private final ChatManagerRepository chatManagerRepository;
    private final ManagedChatRepository managedChatRepository;

    @Override
    public Action getStatus() {
        return Action.DEFINE_CURRENT_CHAT;
    }

    @Override
    public void handleUpdate(CallbackQuery update,
                             Sender sender,
                             ChatManager chatManager) {

        String[] splited = update.getData().split(" ");

        managedChatRepository
                .findByChatId(Long.valueOf(splited[1]))
                .ifPresent(chat -> {
                    chatManager.setCurrentManagedChat(chat);
                    chatManagerRepository.save(chatManager);

                    sender.send(
                            new SendMessage(
                                    update.getFrom().getId().longValue(),
                                    "Текущий чат: " + chat.getChatName()));
                });
    }
}
