package pro.sky.shelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
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
import static pro.sky.shelter.configuration.BotConstants.*;

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
    private final ReportRepository reportRepository;
    private final ContentSaverService contentSaverService;
    private final Logger logger = LoggerFactory.getLogger(BotService.class);

    /**
     * RegEx для определения формата и парсинга контактных данных пользователя
     */
    private final String parsePhone = "(?<phone>[+]7-\\d{3}-\\d{3}-\\d{2}-\\d{2})(\\s)(?<name>[\\W+]+)";
    /**
     * RegEx для определения формата и парсинга информации для отчета о животном
     */
    private final String parseReport = "1[)](?<diet>[\\W+]+)2[)](?<health>[\\W+]+)3[)](?<behavior>[\\W+]+)";
    /**
     * RegEx для парсинга строки ответа на сообщение пользователю через чат бота
     */
    private final String parseResponse = "(([\\d+]+)\\s([\\W+]+))";
    /**
     * Хранение отсылаемых пользователем на отчет данных
     */
    private ReportHolder reportHolder = new ReportHolder();
    /**
     * Map поддерживаемых диалогов
     */
    private final Map<String, DialogInterface> supportedDialogs;

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
                if (incomeMessage.text().matches(parsePhone)) {     //Начато получение телефона и имени
                    logger.info("ChatId={}; Получаем номер телефона из сообщения", incomeMessage.chat().id());
                    parsePhone(incomeMessage.text(), incomeMessage.chat().id());
                    sendResponse(incomeMessage.chat().id(), "Спасибо, контакты сохранены.", SHELTER_KEYBOARD);
                    return;
                }
                if (incomeMessage.text().matches(parseReport)) {    //Начато получение отчета
                    UserEntity user = userRepository.getUserEntitiesByChatId(incomeMessage.chat().id());
                    if (user.getAnimalEntity() != null) {
                        logger.info("ChatId={}; Получаем данные отчета", incomeMessage.chat().id());
                        if (user.getDate() == null) {
                            user.setDate(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
                            userRepository.save(user);
                        }
                        reportHolder.setUserEntity(user);
                        reportHolder.setAnimalName(user.getAnimalEntity().getAnimalName());
                        parseReport(incomeMessage.text());
                        sendResponse(incomeMessage.chat().id(), "Спасибо, теперь отправьте фото животного.", WELCOME_KEYBOARD);
                    } else {
                        logger.info("ChatId={}; К пользователю не привязано животное, не принимаем отчет.", incomeMessage.chat().id());
                        sendResponse(incomeMessage.chat().id(), "Извините, вы еще не брали питомца из нашего приюта.", WELCOME_KEYBOARD);
                    }
                    return;
                } else if (!Objects.equals(incomeMessage.text(), VOLUNTEER_CHAT_CLOSE)) {  //проверка на чат с волонтером
                    UserEntity user = userRepository.getUserEntitiesByChatId(incomeMessage.chat().id());
                    if (user != null && user.getVolunteerChatId() != null) {
                        logger.info("ChatId={}; Пользователь отправляет сообщение волонтеру: {}", incomeMessage.chat().id(), incomeMessage.text());
                        sendResponse(user.getVolunteerChatId(), incomeMessage.chat().id() + ": " + incomeMessage.text(), WELCOME_KEYBOARD);
                        sendResponse(incomeMessage.chat().id(), "Ваше сообщение отправлено волонтеру, ожидайте ответа.", CHAT_KEYBOARD);
                        return;
                    }
                    if (user != null && user.getIsVolunteer() && incomeMessage.text().matches(parseResponse)) {
                        logger.info("ChatId={}; Волонтер отправляет сообщение пользователю: {}", incomeMessage.chat().id(), incomeMessage.text());
                        messageToTheUser(incomeMessage.text());
                        return;
                    }
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
     * @param chatId   - чат пользователя
     * @param message  - сообщение для пользователя
     * @param keyboard - готовый ReplyKeyboardMarkup для применения
     */
    public void sendResponse(Long chatId, String message, ReplyKeyboardMarkup keyboard) {
        if (message.equals(SHELTER_CONTACTS_MSG)) {
            sendResponse(chatId, contentSaverService.getPhoto(1).getSecond(), null);
        }
        SendMessage preparedMessage = new SendMessage(chatId, message);
        if (keyboard != null) preparedMessage.replyMarkup(keyboard);
        telegramBot.execute(preparedMessage);
    }

    /**
     * Отправка фотографии пользователю
     *
     * @param chatId   - чат пользователя
     * @param photo    - фото которое нужно отправить в byte[]
     * @param keyboard - готовый ReplyKeyboardMarkup для применения
     */
    public void sendResponse(Long chatId, byte[] photo, ReplyKeyboardMarkup keyboard) {
        SendPhoto preparedPhoto = new SendPhoto(chatId, photo);
        if (keyboard != null) preparedPhoto.replyMarkup(keyboard);
        telegramBot.execute(preparedPhoto);
    }

    /**
     * Если сообщение в формате запрошенных контактов - тогда берем оттуда информацию.
     * <p>
     * Формат верен? Обновляем Телефон и Имя пользователя в БД.
     *
     * @param text   - текстовая строка для парсинга телефона и имени
     * @param chatId - ID чата пользователя
     */
    private void parsePhone(String text, Long chatId) {
        Pattern pattern = Pattern.compile(parsePhone);
        Matcher matcher = pattern.matcher(text);
        if (matcher.matches()) {
            UserEntity userEntity = userRepository.getUserEntitiesByChatId(chatId);
            if (userEntity == null) {
                userEntity = new UserEntity(chatId);
                userEntity.setIsVolunteer(false);
            }
            userEntity.setUserName(matcher.group("name"));
            userEntity.setPhone(matcher.group("phone"));
            userRepository.save(userEntity);
            logger.info("Контакты пользователя сохранены.");
        }
    }

    /**
     * Метод получения отчета о животном
     *
     * @param text - текстовая строка для парсинга отчета
     */
    private void parseReport(String text) {
        Pattern pattern = Pattern.compile(parseReport);
        Matcher matcher = pattern.matcher(text);
        if (matcher.matches()) {
            logger.info("Заполнение данных отчета");
            reportHolder.setDiet(trim(matcher.group("diet")));
            reportHolder.setHealth(trim(matcher.group("health")));
            reportHolder.setBehavior(trim(matcher.group("behavior")));
            reportHolder.setDate(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        }
    }

    /**
     * Метод сохранения отчета
     *
     * @param chatId - id чата пользователя
     */
    private void saveReport(Long chatId) {
        if (Objects.equals(reportHolder.getUserEntity().getChatId(), chatId)) {
            ReportEntity report = new ReportEntity(reportHolder.getAnimalName(),
                    reportHolder.getDiet(), reportHolder.getHealth(),
                    reportHolder.getBehavior(), reportHolder.getUserEntity(),
                    reportHolder.getDate(), reportHolder.getAnimalPhotoEntity());
            reportRepository.save(report);
            logger.info("Сохранили отчет.");
        }
        reportHolder = new ReportHolder();
    }

    /**
     * Метод отправки сообщения пользователю
     *
     * @param text ответ волонтера
     */
    public void messageToTheUser(String text) {
        Pattern pattern = Pattern.compile(parseResponse);
        Matcher matcher = pattern.matcher(text);
        if (matcher.matches()) {
            long id = Long.parseLong(matcher.group(2));
            String answer = matcher.group(3);
            sendResponse(id, "Ответ волонтера: " + answer, CHAT_KEYBOARD);
        }
    }

    /**
     * Метод отправляющий уведомления
     */
    @Scheduled(cron = "0 0/1 * * * *")
    @Transactional(readOnly = true)
    public void findByDate() {
        logger.debug("Обработка необходимости отправки уведомлений пользователям");

        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

        reportRepository.findAll().stream()
                .filter(Objects::nonNull)
                .filter(reportEntity -> ChronoUnit.DAYS.between(reportEntity.getDate(), now) == 1)
                .map(ReportEntity::getUserEntity)
                .forEach(userEntity -> telegramBot.
                        execute(new SendMessage(userEntity.getChatId(), "Вы забыли отправить ежедневный отчет о питомце!")));
    }
}
