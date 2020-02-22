package ru.vk.bot.repost.entities;

import ru.vk.bot.repost.enums.Action;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "competition")
public class Competition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "created_date",
            columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    LocalDateTime createdDate;

    @Column(name = "finish_date",
            columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    LocalDateTime finishDate;

    String text;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    ManagedChat chat;

    Integer messageId;

    Boolean isFinished;

    Integer amountOfWinners;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    ChatManager creator;

    @Column(name = "status")
    @Enumerated(value = EnumType.ORDINAL)
    Action action;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "competition")
    List<Winner> winners;


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "competition_participant",
            joinColumns = {@JoinColumn(name = "competition_id")},
            inverseJoinColumns = {@JoinColumn(name = "participant_id")})
    Set<Participant> participants = new HashSet<>();

    @Override
    public String toString() {
        return "Competition{" +
                "id=" + id +
                ", createdDate=" + createdDate +
                ", finishDate=" + finishDate +
                ", text='" + text + '\'' +
                ", chat=" + chat +
                ", messageId=" + messageId +
                ", isFinished=" + isFinished +
                ", amountOfWinners=" + amountOfWinners +
                ", creatorId=" + creator +
                '}';
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDateTime finishDate) {
        this.finishDate = finishDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public Boolean getFinished() {
        return isFinished;
    }

    public void setFinished(Boolean finished) {
        isFinished = finished;
    }

    public Integer getAmountOfWinners() {
        return amountOfWinners;
    }

    public void setAmountOfWinners(Integer amountOfWinners) {
        this.amountOfWinners = amountOfWinners;
    }

    public Set<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<Participant> participants) {
        this.participants = participants;
    }

    public ManagedChat getChat() {
        return chat;
    }

    public void setChat(ManagedChat chat) {
        this.chat = chat;
    }

    public ChatManager getCreator() {
        return creator;
    }

    public void setCreator(ChatManager creator) {
        this.creator = creator;
    }

    public List<Winner> getWinners() {
        return winners;
    }

    public void setWinners(List<Winner> winners) {
        this.winners = winners;
    }
}
