package ru.vk.bot.repost.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Lev_S
 */

@Entity
@Table(name = "vk_post")
@NoArgsConstructor
@Data
@EqualsAndHashCode
public class VkPost implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Temporal(value = TemporalType.TIMESTAMP)
    Date date;

    String text;

    Integer vkId;

    Boolean isSent;

    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "post",
            cascade = CascadeType.ALL
    )
    List<VkAttachment> attachments;


}
