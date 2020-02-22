package ru.vk.bot.repost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.vk.bot.repost.entities.VkPost;

import java.util.List;

/**
 * @author Lev_S
 */
@Repository
public interface VkPostRepository extends JpaRepository<VkPost, Long> {

    List<VkPost> findByVkIdIn(List<Integer> idList);

    List<VkPost> findAllByIsSentFalse();

    @Query(nativeQuery = true,
            value = "select p.id, p.date, p.text, p.is_sent, p.vk_id, va.post_id" +
                    "            from vk_post p left join vk_attachment va on p.id = va.post_id where va.post_id" +
                    "                is null and p.date in (select p2.date from vk_post p2" +
                    "                where (:now - " +
                    "                              (select extract(epoch from(select vk_post.date from vk_post where vk_post.id = p2.id))) > :howLong)" +
                    "and p2.is_sent = false)"
    )
    List<VkPost> findAllWithoutAttachments(@Param("now") Long now, @Param("howLong") Long time);

    List<VkPost> findAllByIsSentFalseAndPreparedToPostTrue();

    @Query(nativeQuery = true, value = "select * from vk_post p where p.id not in (select att.post_id from vk_attachment att where att.post_id = p.id) " +
            "and p.preparedtopost = false")
    List<VkPost> findAllWithoutAttachmentsAndPreparedFalse();
}
