package pro.sky.shelter.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.shelter.service.BotService;

import java.util.List;

/**
 * Class-service for TelegramBot listener
 *
 * @autor Shikunov Andrey
 */
@Service
public class BotListener implements UpdatesListener {
    private final TelegramBot telegramBot;

    private final BotService botService;

    private final Logger logger = LoggerFactory.getLogger(BotListener.class);

    public BotListener(TelegramBot telegramBot, BotService botService) {
        this.telegramBot = telegramBot;
        this.botService = botService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    /**
     * Метод обработки сообщений
     *
     * @param updates - обновления полученные ботом
     * @return возвращает номер обработанного обновления
     */
    @Override
    public int process(List<Update> updates) {
        try {
            updates.forEach(update -> {
                logger.info("Processing update: {}", update);
                botService.process(update);
            });
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}