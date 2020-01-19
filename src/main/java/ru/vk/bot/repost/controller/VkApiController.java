package ru.vk.bot.repost.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.vk.bot.repost.service.TelegramBotService;

/**
 * @author Lev_S
 */
@RestController
@RequestMapping("/management")
public class VkApiController {

    private final Logger LOGGER = LoggerFactory.getLogger(VkApiController.class);

    @Autowired
    TelegramBotService service;

    @GetMapping("/proceed")
    public void proceedInvocation(@RequestParam(value = "amount", required = false) Integer amount) {

        if (TelegramBotService.isStoped()) {
            if (amount == null) {
                TelegramBotService.setStoped(false);
                LOGGER.info("Requesting has been proceeded");
            } else {
                service.flushDefinedAmountOfPosts(amount);
                LOGGER.info(amount + " posts have been sent");
            }
        }
    }

    @GetMapping("/stop")
    public void stopInvocation() {
        if (!TelegramBotService.isStoped()) {
            TelegramBotService.setStoped(true);
            LOGGER.info("Requesting has been stoped");
        }
    }
}
