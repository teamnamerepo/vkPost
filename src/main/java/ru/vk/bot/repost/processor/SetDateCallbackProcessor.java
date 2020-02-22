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
import ru.vk.bot.repost.repository.CompetitionRepository;

@Component
@AllArgsConstructor
public class SetDateCallbackProcessor implements UpdateHandler<Message> {

    private final CompetitionRepository competitionRepository;

    @Override
    public Action getStatus() {
        return Action.SET_DATE;
    }

    @Override
    public void handleUpdate(Message update, Sender sender, ChatManager chatManager) {
        Competition currentCompetition = chatManager.getCurrentCompetition();
        currentCompetition.setAction(Action.WRITE_DATE);

        competitionRepository.save(currentCompetition);

        sender.send(
                new SendMessage(
                        update.getFrom()
                                .getId()
                                .longValue(),
                        "Введите дату окончания викторины в формате \"yyyy MM dd HH:mm\""));
    }
}
