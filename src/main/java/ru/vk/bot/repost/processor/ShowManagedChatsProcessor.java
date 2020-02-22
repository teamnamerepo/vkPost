package ru.vk.bot.repost.processor;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.vk.bot.repost.entities.ChatManager;
import ru.vk.bot.repost.entities.ManagedChat;
import ru.vk.bot.repost.enums.Action;
import ru.vk.bot.repost.interfaces.Sender;
import ru.vk.bot.repost.interfaces.UpdateHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class ShowManagedChatsProcessor implements UpdateHandler<Message> {
    @Override
    public Action getStatus() {
        return Action.SHOW_CHAT;
    }

    @Override
    public void handleUpdate(Message update, Sender sender, ChatManager chatManager) {
        List<ManagedChat> managedChats = chatManager.getManagedChats();

        if (!managedChats.isEmpty()) {
            InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

            managedChats.forEach(
                    chat -> buttons.add(
                            Collections.singletonList(
                                    new InlineKeyboardButton()
                                            .setCallbackData(
                                                    Action.DEFINE_CURRENT_CHAT.getValue() +
                                                            " " +
                                                            chat.getChatId()
                                                                    .toString()
                                            )
                                            .setText(chat.getChatName())
                            ))
            );
            keyboard.setKeyboard(buttons);

            sender.send(
                    new SendMessage()
                            .setChatId(
                                    update.getFrom()
                                            .getId()
                                            .longValue()
                            )
                            .setReplyMarkup(keyboard)
                            .setText(
                                    "Текущий чат: " +
                                            chatManager.getCurrentManagedChat().getChatId()
                            )
            );
        } else {
            sender.send(
                    new SendMessage(
                            update.getFrom()
                                    .getId()
                                    .longValue(),
                            "У вас нет управляемых чатов"));
        }
    }
}
