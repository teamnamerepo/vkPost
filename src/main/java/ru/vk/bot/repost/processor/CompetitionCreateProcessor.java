package ru.vk.bot.repost.processor;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.vk.bot.repost.entities.ChatManager;
import ru.vk.bot.repost.entities.Competition;
import ru.vk.bot.repost.enums.Action;
import ru.vk.bot.repost.interfaces.Sender;
import ru.vk.bot.repost.interfaces.UpdateHandler;
import ru.vk.bot.repost.repository.ChatManagerRepository;
import ru.vk.bot.repost.repository.CompetitionRepository;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class CompetitionCreateProcessor implements UpdateHandler<Message> {

    private final ChatManagerRepository managerRepository;
    private final CompetitionRepository competitionRepository;

    @Override
    public Action getStatus() {
        return Action.CREATED;
    }

    @Override
    public void handleUpdate(Message update,
                             Sender sender,
                             ChatManager chatManager) {

        if (chatManager.getCurrentManagedChat() != null) {
            Competition competitionForRemoving = chatManager.getCurrentCompetition();

            Competition competition = new Competition();

            competition.setCreatedDate(
                    LocalDateTime
                            .now()
                            .withSecond(0)
                            .withNano(0)
            );
            competition.setAction(Action.SET_TEXT);
            competition.setFinished(false);
            competition.setAmountOfWinners(1);
            chatManager.setCurrentCompetition(competition);

            managerRepository.save(chatManager);

            if (competitionForRemoving != null) {
                competitionRepository.delete(competitionForRemoving);
            }
            sender.send(new SendMessage(
                    update.getFrom()
                            .getId()
                            .longValue()
                    ,
                    "Введите текст для розыгрыша")
            );
        } else {
            sender.send(new SendMessage(
                    update.getFrom()
                            .getId()
                            .longValue()
                    ,
                    "Выберете чат для розыгрыша"));
        }
    }
}
