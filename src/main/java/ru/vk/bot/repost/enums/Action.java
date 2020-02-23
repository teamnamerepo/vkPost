package ru.vk.bot.repost.enums;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum Action {
    NONE("none"),
    SET_DATE("установить дату"),
    WRITE_DATE("write_date"),
    CANCEL("отмена"),
    ADD_NEW_CHAT("add_chat"),
    DEFINE_CURRENT_CHAT("define_current_chat"),
    PUBLISH("опубликовать сейчас"),
    SET_TEXT("set_text"),
    CHANGE_TEXT("change_text"),
    AMOUNT_OF_SUBSCRIBERS("change_amount"),
    SET_NUMBER_OF_WINNERS("set_number_of_winners"),
    ADD_CHAT("Добавить чат"),
    ADD_PARTICIPANT("new_participant"),
    START("start"),
    DEFAULT("default"),
    PROVIDE_WINNERS("Установить победителей"),
    SET_WINNERS("set_winners"),
    EDIT_MESSAGE("изменить текст"),
    SHOW_CHAT("Выбрать чат"),
    CREATED("Создать розыгрыш"),
    PUBLISHED("published");

    static final Map<String, Action> statusMap;

    public static Map<String, Action> getStatusMap() {
        return statusMap;
    }

    public String getValue() {
        return value;
    }

    String value;

    Action(String value) {
        this.value = value;
    }

    static {
        Map<String, Action> statuses = new HashMap<>();

        for (Action action : Action.values()) {
            statuses.put(action.value, action);
        }
        statusMap = Collections.unmodifiableMap(statuses);
    }
}
