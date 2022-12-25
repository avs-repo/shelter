package pro.sky.shelter.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.DeleteMyCommands;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration of TelegramBot
 *
 * @autor Shikunov Andrey
 */
@Configuration
public class BotConfiguration {
    /**
     * TelegramBot token
     */
    @Value("${telegram.bot.token}")
    private String token;

    /**
     * Creation of the TelegramBot Bean with given token
     */
    @Bean
    public TelegramBot telegramBot() {
        TelegramBot bot = new TelegramBot(token);
        bot.execute(new DeleteMyCommands());
        return bot;
    }

}