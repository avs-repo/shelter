package pro.sky.shelter.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final Logger logger = LoggerFactory.getLogger(BotListener.class);
    @Autowired
    private TelegramBot telegramBot;
    @Autowired
    private BotService botService;

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
                logger.info("Update: {}", "name:" + update.message().from().firstName() + " id:" + update.message().from().id() + " msg:" + update.message().from().toString());
                botService.process(update);
            });
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

}
