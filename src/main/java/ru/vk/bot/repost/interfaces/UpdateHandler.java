package ru.vk.bot.repost.interfaces;

import ru.vk.bot.repost.entities.ChatManager;
import ru.vk.bot.repost.enums.Action;

@FunctionalInterface
public interface UpdateHandler<T> {

    default Action getStatus() {
        return Action.NONE;
    }

    void handleUpdate(T update, Sender sender, ChatManager chatManager);
}
