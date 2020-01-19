package ru.vk.bot.repost.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class VkPost implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

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

    @Override
    public String toString() {
        return "VkPost{" +
                "id=" + id +
                ", date=" + date +
                ", text='" + text + '\'' +
                ", vkId=" + vkId +
                ", isSent=" + isSent +
                ", attachments=" + attachments +
                '}';
    }
}
