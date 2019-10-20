package ru.vk.bot.repost.enums;

import lombok.Getter;

/**
 * @author Lev_S
 */
@Getter
public enum PhotoSizeEnum {

    MAX_SIZE_QUALITY("z"),
    MEDIUM_SIZE_QUALITY("x"),
    MIN_SIZE_QUALITY("s");

    String value;

    PhotoSizeEnum(String value) {
        this.value = value;
    }

}
