package ru.vk.bot.repost.processor;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.vk.bot.repost.entities.ChatManager;
import ru.vk.bot.repost.entities.Competition;
import ru.vk.bot.repost.enums.Action;
import ru.vk.bot.repost.interfaces.Sender;
import ru.vk.bot.repost.interfaces.UpdateHandler;
import ru.vk.bot.repost.repository.ChatManagerRepository;
import ru.vk.bot.repost.repository.CompetitionRepository;

@Component
@AllArgsConstructor
public class ProvideWinnersProcessor implements UpdateHandler<Message> {

    private final CompetitionRepository repository;

    @Override
    public Action getStatus() {
        return Action.PROVIDE_WINNERS;
    }

    @Override
    public void handleUpdate(Message update, Sender sender, ChatManager chatManager) {
        Competition currentCompetition = chatManager.getCurrentCompetition();

        currentCompetition.setAction(Action.SET_WINNERS);
        repository.save(currentCompetition);

        sender.send(
                new SendMessage(
                        update.getFrom().getId().longValue(),
                        "Напишите через пробел юзернейм, имя, фамилию(если имеется)" +
                                "каждого победителя разделяйте знаком ';'. " +
                                "Пример: UltraDoter2006 Никита Мусихин; EBASHU_NA_LONGE_AWP!! Андрей")
        );
    }
}
