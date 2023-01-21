package pro.sky.shelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import pro.sky.shelter.core.dialog.DialogInterface;
import pro.sky.shelter.core.dto.DialogDto;
import pro.sky.shelter.core.entity.ReportEntity;
import pro.sky.shelter.core.entity.UserEntity;
import pro.sky.shelter.core.model.ReportHolder;
import pro.sky.shelter.core.repository.ReportRepository;
import pro.sky.shelter.core.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.trim;
import static pro.sky.shelter.configuration.BotConstants.SHELTER_KEYBOARD;
import static pro.sky.shelter.configuration.BotConstants.WELCOME_KEYBOARD;

/**
 * Main service for TelegramBot
 *
 * @autor Shikunov Andrey
 */
@Service
public class BotService {

    /**
     * TelegramBot instance that this BotService is responsible for
     */
    private final TelegramBot telegramBot;
    private final UserRepository userRepository;
    private final ContentSaverService contentSaverService;
    private final Logger logger = LoggerFactory.getLogger(BotService.class);
    /**
     * RegEx для определения формата и парсинга контактных данных пользователя
     */
    private final String parsePhone = "(?<phone>[+][7]-\\d{3}-\\d{3}-\\d{2}-\\d{2})(\\s)(?<name>[\\W+]+)";
    /**
     * RegEx для определения формата и парсинга информации для отчета о животном
     */
    private final String parseReport = "1[)](?<diet>[\\W+]+)2[)](?<health>[\\W+]+)3[)](?<behavior>[\\W+]+)";
    private ReportHolder reportHolder = new ReportHolder();
    /**
     * Map of supported dialogs
     */
    private final Map<String, DialogInterface> supportedDialogs;
    private final ReportRepository reportRepository;


    public BotService(TelegramBot bot, UserRepository userRepository, Map<String, DialogInterface> supportedDialogs, ContentSaverService contentSaverService, ReportRepository reportRepository) {
        this.telegramBot = bot;
        this.userRepository = userRepository;
        this.supportedDialogs = supportedDialogs;
        this.contentSaverService = contentSaverService;
        this.reportRepository = reportRepository;
    }

    /**
     * Gets the Bot update request
     * <p>
     * Checks is there a photo in message, if true - proceed with photo
     * Checks that message is <b>not null</b> and has <b>text data</b> - proceed with text.
     * Chooses needed dialog and sending response to user.
     */
    public void process(Update update) {
        if (update == null || update.message().document() != null) {
            logger.debug("Получен document или null update");
            return;
        }
        for (DialogInterface dialog : supportedDialogs.values()) {
            Message incomeMessage = update.message();
            if (update.message() == null) {
                logger.debug("ChatId={}; получено null сообщение", incomeMessage.chat().id());
                return;
            }
            if (incomeMessage.photo() != null) {
                if (update.message().photo()[update.message().photo().length - 1].fileId() != null
                        && Objects.equals(reportHolder.getUserEntity().getChatId(), incomeMessage.chat().id())) {
                    reportHolder.setAnimalPhotoEntity(contentSaverService.uploadPhoto(update));
                    saveReport(incomeMessage.chat().id());
                    sendResponse(incomeMessage.chat().id(), "Спасибо, ваш отчет принят!", WELCOME_KEYBOARD);
                    return;
                } else {
                    logger.debug("ChatId={}; Похоже получено null фото", incomeMessage.chat().id());
                }
            } else if (incomeMessage.text() != null) {
                if (incomeMessage.text().matches(parsePhone)) {
                    logger.info("ChatId={}; Получаем номер телефона из сообщения", incomeMessage.chat().id());
                    parsePhone(incomeMessage.text(), incomeMessage.chat().id());
                    sendResponse(incomeMessage.chat().id(), "Спасибо, контакты сохранены.", SHELTER_KEYBOARD);
                    return;
                }
                if (incomeMessage.text().matches(parseReport)) {
                    //Начато получение отчета
                    logger.info("ChatId={}; Получаем данные отчета", incomeMessage.chat().id());
                    reportHolder.setUserEntity(userRepository.getUserEntitiesByChatId(incomeMessage.chat().id()));
                    parseReport(incomeMessage.text());
                    sendResponse(incomeMessage.chat().id(), "Спасибо, теперь отправьте фото животного.", WELCOME_KEYBOARD);
                    return;
                }
                DialogDto dto = new DialogDto(incomeMessage.chat().id(), update.message().from().firstName(), incomeMessage.text());
                if (dialog.isSupport(dto) && dialog.process(dto)) {
                    sendResponse(dto.chatId(), dialog.getMessage(dto.chatId()), dialog.getKeyboard());
                    return;
                } else {
                    logger.debug("ChatId={}; Получен пустой текст", incomeMessage.chat().id());
                }
            }
        }
    }

    /**
     * Отправка сообщения пользователю
     *
     * @param chatId - чат пользователя
     * @param message - сообщение для пользователя
     * @param keyboard - готовый ReplyKeyboardMarkup для применения
     */
    public void sendResponse(Long chatId, String message, ReplyKeyboardMarkup keyboard) {
        SendMessage preparedMessage = new SendMessage(chatId, message);
        if (keyboard != null) preparedMessage.replyMarkup(keyboard);
        telegramBot.execute(preparedMessage);
    }

    /**
     * Если сообщение в формате запрошенных контактов - тогда берем оттуда информацию.
     * <p>
     * Формат верен? Обновляем Телефон и Имя пользователя в БД.
     */
    private void parsePhone(String text, Long chatId) {
        Pattern pattern = Pattern.compile(parsePhone);
        Matcher matcher = pattern.matcher(text);
        if (matcher.matches()) {
            String phone = matcher.group("phone");
            String name = matcher.group("name");
            UserEntity userEntity = userRepository.getUserEntitiesByChatId(chatId);
            if (userEntity == null) {
                userEntity = new UserEntity(chatId, name, phone);
            } else {
                userEntity.setUserName(name);
                userEntity.setPhone(phone);
            }
            userRepository.save(userEntity);
            logger.info("Контакты сохранены.");
        }
    }

    /**
     * Метод заполнения отчета
     * @param text текст отчета
     */
    private void parseReport(String text) {
        logger.info("Заполнение отчета");
        Pattern pattern = Pattern.compile(parseReport);
        Matcher matcher = pattern.matcher(text);
        if (matcher.matches()) {
            reportHolder.setDiet(trim(matcher.group("diet")));
            reportHolder.setHealth(trim(matcher.group("health")));
            reportHolder.setBehavior(trim(matcher.group("behavior")));
            reportHolder.setDate(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        }
    }

    /**
     * Метод сохранения отчета
     * @param chatId - id чата пользователя
     */
    private void saveReport(Long chatId) {
        if (Objects.equals(reportHolder.getUserEntity().getChatId(), chatId)) {
            ReportEntity report = new ReportEntity(reportHolder.getDiet(), reportHolder.getHealth(),
                    reportHolder.getBehavior(), reportHolder.getUserEntity(),
                    reportHolder.getDate(), reportHolder.getAnimalPhotoEntity());
            reportRepository.save(report);
            logger.info("Сохранили отчет.");
        }
        reportHolder = new ReportHolder();
    }
}
