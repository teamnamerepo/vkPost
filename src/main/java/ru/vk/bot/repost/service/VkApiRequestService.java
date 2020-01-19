package ru.vk.bot.repost.service;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.wall.WallpostAttachment;
import com.vk.api.sdk.objects.wall.WallpostFull;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.vk.bot.repost.entities.VkAttachment;
import ru.vk.bot.repost.entities.VkPost;
import ru.vk.bot.repost.enums.PhotoSizeEnum;
import ru.vk.bot.repost.repository.VkPostRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Lev_S
 */
@Service
@Data
public class VkApiRequestService {

    Logger LOGGER = LoggerFactory.getLogger(VkApiRequestService.class);

    @Autowired
    private ServiceActor actor;

    @Autowired
    private VkApiClient client;

    @Autowired
    private VkPostRepository postRepository;

    @Autowired
    private TelegramBotService botService;

    private final static String REG_EXP = "^.*(https://m\\.youtube|https://www\\.twitch\\.tv/|https://youtu\\.be/).*$";
    private final static Pattern PATTERN = Pattern.compile(REG_EXP);

    private static final Integer TARGET_GROUP_ID = -139228227; //-79268570;

    @Transactional
    public void createPost() {

        List<VkPost> postsForBd = new ArrayList<>();
        try {
            GetResponse response = client
                    .wall()
                    .get(actor)
                    .count(5)
                    .ownerId(TARGET_GROUP_ID)
                    .execute();
            List<WallpostFull> postListFromResponse = response.getItems();

            List<VkPost> existingListFromDb = postRepository.findByVkIdIn(postListFromResponse.stream()
                    .map(WallpostFull::getId)
                    .collect(Collectors.toList())
            );

            postListFromResponse.stream()
                    .filter(post -> (PATTERN.matcher(post.getText()).matches() || (
                            !post.getText().contains("http://") && !post.getText().contains("https://")) &&
                            Arrays.stream(post.getText().split("."))
                                    .allMatch(str -> !str.startsWith("ru") && !str.startsWith("com")) && !post.isMarkedAsAds()
                            && CollectionUtils.isEmpty(post.getCopyHistory()))
                    )
                    .filter(post -> {
                                if (!CollectionUtils.isEmpty(post.getAttachments())) {
                                    if (post.getAttachments().stream().map(wallPost -> wallPost.getType()
                                            .getValue()).collect(Collectors.toList()).contains("video")) {
                                        return PATTERN.matcher(post.getText()).matches();
                                    } else {
                                        return true;
                                    }
                                } else {
                                    return true;
                                }
                            }
                    ).forEach(post -> {
                if (!existingListFromDb.stream().map(VkPost::getVkId).collect(Collectors.toList()).contains(post.getId())) {

                    VkPost postForBd = new VkPost();
                    List<VkAttachment> attachmentsForBd = new ArrayList<>();

                    postForBd.setVkId(post.getId());
                    postForBd.setText(post.getText());
                    postForBd.setIsSent(false);
                    postForBd.setDate(new Timestamp(new Date().getTime()));

                    List<WallpostAttachment> attachments = post.getAttachments();
                    if (!CollectionUtils.isEmpty(attachments)) {

                        for (WallpostAttachment attachmentFromResponse : attachments) {
                            VkAttachment attachmentForBd = new VkAttachment();

                            if ("photo".equals(attachmentFromResponse.getType().getValue())) {
                                Photo photo = attachmentFromResponse.getPhoto();

                                for (PhotoSizeEnum size : PhotoSizeEnum.values()) {

                                    if (attachmentForBd.getUrl() != null) {
                                        break;
                                    }
                                    photo.getSizes().stream()
                                            .filter(photoSize -> size.getValue().equals(photoSize.getType().getValue())
                                            )
                                            .findAny()
                                            .ifPresent(photoSize -> attachmentForBd.setUrl(photoSize.getUrl().getPath())
                                            );
                                }
                            }
                            if ("doc".equals(attachmentFromResponse.getType().getValue())) {
                                attachmentForBd.setUrl(
                                        attachmentFromResponse
                                                .getDoc()
                                                .getUrl()
                                                .getPath()
                                );
                            }
                            attachmentForBd.setPost(postForBd);
                            if (!StringUtils.isEmpty(attachmentForBd.getUrl())) {
                                attachmentsForBd.add(attachmentForBd);
                            }
                        }

                        String[] splittedTextForReference = post.getText().split("https");
                        String referenceUrl = "";
                        VkAttachment referenceAttachment = new VkAttachment();

                        if (splittedTextForReference.length == 2) {
                            if (splittedTextForReference[1].indexOf(' ') != -1) {
                                referenceUrl = "https" + splittedTextForReference[1]
                                        .substring(
                                                0, splittedTextForReference[1].indexOf(' ')
                                        );
                            } else {
                                referenceUrl = "https" + splittedTextForReference[1];
                            }
                        }
                        if (!StringUtils.isEmpty(referenceUrl)) {

                            referenceAttachment.setPost(postForBd);
                            referenceAttachment.setUrl(referenceUrl);
                            attachmentsForBd.add(referenceAttachment);
                        }
                    }
                    postForBd.setAttachments(attachmentsForBd);
                    postsForBd.add(postForBd);
                }
            });

        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
        postRepository.saveAll(postsForBd);

        LOGGER.info("Posts with id's were created: " + postsForBd.stream()
                .map(VkPost::getVkId)
                .collect(Collectors.toList())
        );
    }

    @Transactional
    public void tryToUpdate() throws Exception {

        List<VkPost> allPostsWithoutAttachments = postRepository.findAllWithoutAttachments(
                Instant.now().getEpochSecond()
                , 30L);

        LOGGER.info("Posts without attachments size is " + allPostsWithoutAttachments.size());

        if (!allPostsWithoutAttachments.isEmpty()) {

            List<WallpostFull> wallpostFulls =
                    client.wall()
                            .getById(actor,
                                    allPostsWithoutAttachments
                                            .stream()
                                            .map(VkPost::getVkId)
                                            .map(id -> TARGET_GROUP_ID.toString() + "_" + id.toString())
                                            .collect(Collectors.toList())
                            ).execute();

            wallpostFulls.forEach(post -> {
                if (post != null) {
                    if (!CollectionUtils.isEmpty(post.getAttachments())) {

                        List<String> collectionOfAttachmentsUrls = post.getAttachments()
                                .stream()
                                .filter(wallpostAttachment -> "photo".equals(wallpostAttachment.getType()
                                        .getValue()))
                                .map(att -> att.getPhoto()
                                        .getSizes()
                                        .stream()
                                        .filter(photoSize -> Arrays.stream(PhotoSizeEnum.values())
                                                .map(PhotoSizeEnum::getValue)
                                                .anyMatch(value -> value.equals(photoSize.getType().getValue())
                                                )
                                        )
                                        .min(Comparator.comparingInt(a -> Arrays.stream(PhotoSizeEnum.values())
                                                .map(PhotoSizeEnum::getValue)
                                                .collect(Collectors.toList())
                                                .indexOf(a.getType().getValue()))
                                        )
                                        .orElseThrow(RuntimeException::new)
                                        .getUrl()
                                        .getPath())
                                .collect(Collectors.toList());

                        if (collectionOfAttachmentsUrls.size() == 1) {
                            LOGGER.info(post.getText() + "\n" +
                                    collectionOfAttachmentsUrls.get(0));

                            VkAttachment attachment = new VkAttachment();
                            attachment.setUrl(collectionOfAttachmentsUrls.get(0));

                            allPostsWithoutAttachments.stream()
                                    .filter(p -> p.getVkId().equals(post.getId()))
                                    .findFirst()
                                    .ifPresent(p -> {
                                        p.setAttachments(new ArrayList<>(
                                                Collections.singletonList(attachment))
                                        );
                                        attachment.setPost(p);

                                    });
                        } else {
                            LOGGER.info(post.getText() + "\n" +
                                    collectionOfAttachmentsUrls);

                            allPostsWithoutAttachments.stream()
                                    .filter(p -> p.getVkId().equals(post.getId()))
                                    .findFirst()
                                    .ifPresent(p -> p.setAttachments(
                                            collectionOfAttachmentsUrls
                                                    .stream()
                                                    .map(VkAttachment::new)
                                                    .peek(att -> att.setPost(p))
                                                    .collect(Collectors.toList())
                                    ));
                        }
                    }
                }
            });
            postRepository.saveAll(allPostsWithoutAttachments);
        }
    }
}