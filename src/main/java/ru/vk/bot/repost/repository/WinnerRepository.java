package ru.vk.bot.repost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vk.bot.repost.entities.Winner;

@Repository
public interface WinnerRepository extends JpaRepository<Winner, Long> {
}
