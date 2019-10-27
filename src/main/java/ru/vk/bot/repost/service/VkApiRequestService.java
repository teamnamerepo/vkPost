package ru.vk.bot.repost.service;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.wall.WallpostAttachment;
import com.vk.api.sdk.objects.wall.WallpostFull;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.vk.bot.repost.entities.VkAttachment;
import ru.vk.bot.repost.entities.VkPost;
import ru.vk.bot.repost.enums.PhotoSizeEnum;
import ru.vk.bot.repost.repository.VkPostRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Lev_S
 */
@Service
public class VkApiRequestService {

    @Autowired
    ServiceActor actor;

    @Autowired
    VkApiClient client;

    @Autowired
    VkPostRepository postRepository;

    private final static String regExp = "^.*(https://m\\.youtube|https://www\\.twitch\\.tv/|https://youtu\\.be/).*$";
    private final static Pattern pattern = Pattern.compile(regExp);

    private static final int targetGroupId = -79268570;

    @Transactional
    public void createPost() {

        List<VkPost> postsForBd = new ArrayList<>();
        try {
            GetResponse response = client
                    .wall()
                    .get(actor)
                    .count(5)
                    .ownerId(targetGroupId)
                    .execute();
            List<WallpostFull> postListFromResponse = response.getItems();
            System.out.println(postListFromResponse);

            List<VkPost> existingListFromDb = postRepository.findByVkIdIn(postListFromResponse.stream()
                    .map(WallpostFull::getId)
                    .collect(Collectors.toList())
            );

            postListFromResponse.stream()
                    .filter(post -> pattern.matcher(post.getText()).matches() || (
                            !post.getText().contains("http://") && !post.getText().contains("https://"))
                    )
                    .filter(post -> {
                                if (post.getAttachments().stream().map(wallPost -> wallPost.getType().getValue())
                                        .collect(Collectors.toList()).contains("video")) {
                                    return pattern.matcher(post.getText()).matches();
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
                    postForBd.setDate(new Timestamp(post.getDate() * 1000L));

                    List<WallpostAttachment> attachments = post.getAttachments();
                    if (!CollectionUtils.isEmpty(attachments)) {

                        for (WallpostAttachment attachmentFromResponse : attachments) {
                            VkAttachment attachmentForBd = new VkAttachment();

                            if ("photo".equals(attachmentFromResponse.getType().getValue())) {
                                Photo photo = attachmentFromResponse.getPhoto();

                                for (PhotoSizeEnum size : PhotoSizeEnum.values()) {

                                    if (attachmentForBd.getUrl() == null) {
                                        photo.getSizes().stream()
                                                .filter(photoSizes -> size.getValue().equals(photoSizes.getType().getValue())
                                                )
                                                .findAny()
                                                .ifPresent(photoSizes -> attachmentForBd.setUrl(photoSizes.getUrl().getPath())
                                                );
                                    } else {
                                        break;
                                    }
                                }
                            }
                            attachmentForBd.setPost(postForBd);
                            attachmentsForBd.add(attachmentForBd);
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
    }
}
