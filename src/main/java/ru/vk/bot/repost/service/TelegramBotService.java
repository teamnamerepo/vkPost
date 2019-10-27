package ru.vk.bot.repost.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.vk.bot.repost.entities.VkAttachment;
import ru.vk.bot.repost.entities.VkPost;
import ru.vk.bot.repost.repository.VkPostRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Lev_S
 */

@Service
public class TelegramBotService extends TelegramLongPollingBot {

    @Autowired
    VkPostRepository repository;

    @Value("${tg.bot.token}")
    private String token;

    @Value("${tg.bot.name}")
    private String name;

    private static final long chatId = -1001247006240L;

    @Autowired
    public TelegramBotService(DefaultBotOptions options) {
        super(options);
    }

    @Override
    public void onUpdateReceived(Update update) {
        update.getChannelPost().getChat().getId();
    }

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Transactional
    public void repost() throws TelegramApiException {
        List<VkPost> postsFromDb = repository.findAllByIsSentFalse();

        if (!postsFromDb.isEmpty()) {

            postsFromDb.sort(Comparator.comparing(VkPost::getDate));
            for (VkPost post : postsFromDb) {

                if (!post.getAttachments().isEmpty()) {
                    List<VkAttachment> attachments = post.getAttachments();

                    if (attachments.size() > 1 || StringUtils.isEmpty(post.getText())) {
                        SendMediaGroup mediaGroup = new SendMediaGroup();
                        List<InputMedia> photoList = new ArrayList<>();

                        for (VkAttachment photo : attachments) {
                            InputMedia media = new InputMediaPhoto();
                            media.setMedia("https://pp.vk.me" + photo.getUrl());

                            photoList.add(media);
                        }
                        photoList.get(0).setCaption(post.getText());
                        mediaGroup.setChatId(chatId);
                        mediaGroup.setMedia(photoList);

                        execute(mediaGroup);
                        post.setIsSent(true);
                    } else {
                        if (attachments.size() == 1) {
                            SendMessage message = new SendMessage();

                            message.setParseMode("html");
                            message.setChatId(chatId);
                            message.setText(post.getText() + "<a href = \""
                                    + "https://pp.vk.me"
                                    + attachments.get(0).getUrl()
                                    + "\">&#8205;</a>");
                            execute(message);
                            post.setIsSent(true);
                        }
                    }
                } else {
                    SendMessage message = new SendMessage();

                    message.setChatId(chatId);
                    message.setText(post.getText());

                    execute(message);
                    post.setIsSent(true);
                }
            }
            repository.saveAll(postsFromDb);
        }
    }
}
