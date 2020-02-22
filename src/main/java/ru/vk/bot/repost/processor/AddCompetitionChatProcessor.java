package ru.vk.bot.repost.processor;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.ChatMember;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.vk.bot.repost.entities.ChatManager;
import ru.vk.bot.repost.entities.ManagedChat;
import ru.vk.bot.repost.enums.Action;
import ru.vk.bot.repost.interfaces.Sender;
import ru.vk.bot.repost.interfaces.UpdateHandler;
import ru.vk.bot.repost.repository.ChatManagerRepository;

@Component
public class AddCompetitionChatProcessor implements UpdateHandler<Message> {

    private final ChatManagerRepository chatManagerRepository;
    private final TelegramLongPollingBot bot;

    public AddCompetitionChatProcessor(ChatManagerRepository chatManagerRepository,
                                       @Lazy TelegramLongPollingBot bot) {
        this.chatManagerRepository = chatManagerRepository;
        this.bot = bot;
    }

    @Override
    public Action getStatus() {
        return Action.ADD_NEW_CHAT;
    }

    @Override
    public void handleUpdate(Message update,
                             Sender sender,
                             ChatManager chatManager) {

        Long id = update.getForwardFromChat().getId();
        GetChatMember chatMember = new GetChatMember();

        chatMember.setChatId(id);
        chatMember.setUserId(update.getFrom().getId());

        if (chatManager
                .getManagedChats()
                .stream()
                .map(ManagedChat::getChatId)
                .noneMatch(identifier -> identifier.equals(id))
        ) {
            try {
                ChatMember member = bot.execute(chatMember);

                if ("creator".equals(member.getStatus()) ||
                        "administrator".equals(member.getStatus())) {

                    ManagedChat chat = new ManagedChat();
                    chat.setChatManager(chatManager);
                    chat.setChatId(id);
                    chat.setChatName(update.getForwardFromChat().getTitle());

                    chatManager.setCurrentManagedChat(chat);
                    chatManagerRepository.save(chatManager);

                    sender.send(new SendMessage(
                            update.getFrom()
                                    .getId()
                                    .longValue(),
                            "Нажмите 'выбор чата', чтобы выбрать этот чат"));
                } else {
                    sender.send(new SendMessage(
                            update.getFrom()
                                    .getId()
                                    .longValue(),
                            "Вы должны быть администратором или создателем этого чата"
                    ));
                }
            } catch (TelegramApiException e) {
                sender.send(new SendMessage(
                        update.getFrom()
                                .getId()
                                .longValue(),
                        "Бот не является администратором этого чата"));
            }
        } else {
            sender.send(new SendMessage(
                    update.getFrom()
                            .getId()
                            .longValue(),
                    "Нажмите 'выбор чата', чтобы выбрать этот чат"));
        }
    }
}
