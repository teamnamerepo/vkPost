package ru.vk.bot.repost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vk.bot.repost.entities.Competition;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Long> {

    List<Competition> findAllByCreatorId(Long creator_id);

    List<Competition> findAllByFinishDate(LocalDateTime date);
}
