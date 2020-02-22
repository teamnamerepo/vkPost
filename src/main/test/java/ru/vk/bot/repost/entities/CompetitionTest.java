package ru.vk.bot.repost.entities;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.vk.bot.repost.repository.ChatManagerRepository;
import ru.vk.bot.repost.repository.CompetitionRepository;
import ru.vk.bot.repost.repository.ParticipantRepository;

import java.util.Optional;

@SpringBootTest
class CompetitionTest {

    @Autowired
    CompetitionRepository repository;

    @Autowired
    ParticipantRepository participantRepository;

    @Autowired
    ChatManagerRepository chatManagerRepository;

    @Autowired
    CompetitionRepository competitionRepository;

    @Test
    void create() {
//        Competition competition = new Competition();
//        Participant participant = new Participant();
//        participant.setFirstName("HUY");
//        participant.setUserName("@RRR");
//        participant.setLastName("QQ");
//
//
//        competition.setChatId(124124L);
//        competition.setMessageId(41);
//        competition.setAmountOfWinners(4);
//        competition.setCreatorId(5);
//        competition.setCreatedDate(LocalDateTime.now());
//        competition.setFinishDate(LocalDateTime.of(2020, Month.AUGUST,12,20,30));
//        competition.setFinishDate(LocalDateTime.now());
//        competition.setText("FFFFFFFFFFFFFFFFFFFFF");
//
//        competition.setParticipants(Stream.of(participant).collect(Collectors.toSet()));
//
//        Competition save = repository.save(competition);
//        System.out.println(save);
//
//        Participant participant1 = new Participant();
//        participant1.setFirstName("VVVV");
//        participant1.setUserName("@RSSSS");
//        participant1.setLastName("QQ");
//        Competition competition1 = repository.findById(6L).get();

       // participant1.getCompetitions().add(competition1);
//
//        ChatManager manager = new ChatManager();
//        Competition competition = new Competition();
//        competition.setText("aaf");
//        competition.setCreatedDate(LocalDateTime.now().withSecond(0).withNano(0));
//        competition.setMessageId(345);
//        competition.setFinished(false);
//        competition.setStatus(Status.NONE);
//        manager.setTelegramId(124214);
//
//        manager.setCurrentCompetition(competition);
//
//        chatManagerRepository.save(manager);
       // participantRepository.save(participant1);
//        participantRepository.deleteById(29L);

        Optional<ChatManager> byId1 = chatManagerRepository.findById(2L);
        ChatManager manager = byId1.get();
        manager.setCurrentCompetition(null);

        chatManagerRepository.save(manager);

        Optional<Competition> byId = competitionRepository.findById(13L);
        competitionRepository.delete(byId.get());
    }

}