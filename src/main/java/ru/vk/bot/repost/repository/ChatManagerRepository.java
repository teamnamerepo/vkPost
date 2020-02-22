package ru.vk.bot.repost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vk.bot.repost.entities.ChatManager;

import java.util.Optional;

@Repository
public interface ChatManagerRepository extends JpaRepository<ChatManager, Long> {

    Optional<ChatManager> findByTelegramId(Integer telegramId);
}
