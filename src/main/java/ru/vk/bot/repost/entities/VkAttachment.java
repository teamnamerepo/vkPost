package ru.vk.bot.repost.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Lev_S
 */

@Entity
@Table(name = "vk_attachment")
@NoArgsConstructor
@Data
@EqualsAndHashCode
public class VkAttachment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String url;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    VkPost post;
}
