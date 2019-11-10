package ru.vk.bot.repost.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.vk.bot.repost.service.VkApiRequestService;

/**
 * @author Lev_S
 */
@Component
public class ApiRequestScheduler {

    private static final int delay = 60000;

    @Autowired
    VkApiRequestService requestService;

    @Scheduled(fixedDelay = delay)
    public void doRequest() {
        requestService.createPost();
    }
}
