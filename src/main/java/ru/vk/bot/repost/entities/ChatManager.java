package ru.vk.bot.repost.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chat_managers")
public class ChatManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "telegram_id", unique = true)
    Integer telegramId;

    @OneToMany(
            mappedBy = "creator",
            cascade = CascadeType.ALL)
    List<Competition> createdCompetitions = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "current_chat_id", referencedColumnName = "id")
    ManagedChat currentManagedChat;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "current_competition", referencedColumnName = "id")
    Competition currentCompetition;

    @OneToMany(
            mappedBy = "chatManager",
            cascade = CascadeType.ALL)
    List<ManagedChat> managedChats = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(Integer telegramId) {
        this.telegramId = telegramId;
    }

    public List<Competition> getCreatedCompetitions() {
        return createdCompetitions;
    }

    public void setCreatedCompetitions(List<Competition> createdCompetitions) {
        this.createdCompetitions = createdCompetitions;
    }

    public ManagedChat getCurrentManagedChat() {
        return currentManagedChat;
    }

    public void setCurrentManagedChat(ManagedChat currentManagedChat) {
        this.currentManagedChat = currentManagedChat;
    }

    public List<ManagedChat> getManagedChats() {
        return managedChats;
    }

    public void setManagedChats(List<ManagedChat> managedChats) {
        this.managedChats = managedChats;
    }
    public Competition getCurrentCompetition() {
        return currentCompetition;
    }

    public void setCurrentCompetition(Competition currentCompetition) {
        this.currentCompetition = currentCompetition;
    }
}
