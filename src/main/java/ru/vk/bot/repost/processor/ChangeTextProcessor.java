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
import ru.vk.bot.repost.repository.CompetitionRepository;

@Component
@AllArgsConstructor
public class ChangeTextProcessor implements UpdateHandler<Message> {

    private final CompetitionRepository competitionRepository;

    @Override
    public Action getStatus() {
        return Action.CHANGE_TEXT;
    }

    @Override
    public void handleUpdate(Message update, Sender sender, ChatManager chatManager) {

        Competition currentCompetition = chatManager.getCurrentCompetition();
        currentCompetition.setText(update.getText());
        currentCompetition.setAction(Action.NONE);

        competitionRepository.save(currentCompetition);

        sender.send(
                new SendMessage(
                        update.getFrom()
                                .getId()
                                .longValue(),
                        "текст изменен"));
    }
}
