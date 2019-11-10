package ru.vk.bot.repost.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.vk.bot.repost.service.TelegramBotService;

/**
 * @author Lev_S
 */
@Component
public class TelegramRepostScheduler {

    @Autowired
    TelegramBotService service;
    private static final int delay =  10000;//60000;

    @Scheduled(fixedDelay = delay)
    public void doRepost() {
            service.repost();
    }
}
