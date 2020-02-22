package ru.vk.bot.repost.processor;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.vk.bot.repost.entities.ChatManager;
import ru.vk.bot.repost.enums.Action;
import ru.vk.bot.repost.interfaces.Sender;
import ru.vk.bot.repost.interfaces.UpdateHandler;
import ru.vk.bot.repost.repository.CompetitionRepository;

@Component
@AllArgsConstructor
public class EditAmountOfWinnersProcessor implements UpdateHandler<CallbackQuery> {

    private final CompetitionRepository competitionRepository;

    @Override
    public Action getStatus() {
        return Action.AMOUNT_OF_SUBSCRIBERS;
    }

    @Override
    public void handleUpdate(CallbackQuery update, Sender sender, ChatManager chatManager) {
        String[] split = update.getData().split(" ");

        competitionRepository
                .findById(Long.valueOf(split[1]))
                .ifPresent(competition -> {

                    if ("+".equals(split[2])) {
                        competition.setAmountOfWinners(competition.getAmountOfWinners() + 1);

                    } else if (competition.getAmountOfWinners() > 1) {
                        competition.setAmountOfWinners(competition.getAmountOfWinners() - 1);
                    }

                    sender.send(
                            new SendMessage(
                                    update
                                            .getFrom()
                                            .getId()
                                            .longValue()
                                    ,
                                    "количество победителей: " +
                                            competition.getAmountOfWinners()
                            )
                    );
                    competitionRepository.save(competition);
                });
    }
}
