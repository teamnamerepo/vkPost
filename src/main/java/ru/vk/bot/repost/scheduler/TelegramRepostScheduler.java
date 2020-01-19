package ru.vk.bot.repost.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.vk.bot.repost.service.TelegramBotService;

/**
 * @author Lev_S
 */

@Component
@Profile("!test")
public class TelegramRepostScheduler {

    @Autowired
    TelegramBotService service;

    private static final int DELAY = 60000;

    @Scheduled(fixedDelay = DELAY)
    public void doRepost() {
        service.repost();
    }
}
