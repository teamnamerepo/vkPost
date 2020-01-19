package ru.vk.bot.repost.scheduler;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.vk.bot.repost.service.VkApiRequestService;

/**
 * @author Lev_S
 */
@Data
@Component
@Profile("!test")
public class ApiRequestScheduler {

    private static final int DELAY = 60000;

    @Autowired
    VkApiRequestService requestService;

    @Scheduled(fixedDelay = DELAY)
    public void doRequest() {
        requestService.createPost();
    }

    @Scheduled(fixedDelay = DELAY + 20000)
    public void checkForUpdatedPosts() {
        try {
            requestService.tryToUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
