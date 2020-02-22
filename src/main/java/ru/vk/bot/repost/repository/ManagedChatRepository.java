package ru.vk.bot.repost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vk.bot.repost.entities.ManagedChat;

import java.util.Optional;

@Repository
public interface ManagedChatRepository extends JpaRepository<ManagedChat, Long> {

    Optional<ManagedChat> findByChatId(Long aLong);
}
