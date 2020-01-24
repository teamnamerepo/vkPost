package ru.vk.bot.repost.scheduler;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.vk.bot.repost.service.VkApiRequestService;

/**
 * @author Lev_S
 */
@Data
@Component
@Profile("!test")
@RequiredArgsConstructor
public class ApiRequestScheduler {

    private static final int DELAY = 60000;

    private final VkApiRequestService requestService;

    @Transactional
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
