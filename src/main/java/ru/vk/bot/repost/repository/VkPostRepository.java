package ru.vk.bot.repost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vk.bot.repost.entities.VkPost;

import java.util.List;

/**
 * @author Lev_S
 */
@Repository
public interface VkPostRepository extends JpaRepository<VkPost, Long> {

    List<VkPost> findByIdIn(List<Integer> idList);
    List<VkPost> findByVkIdIn(List<Integer> idList);
    List<VkPost> findAllByIsSentFalse();

}
