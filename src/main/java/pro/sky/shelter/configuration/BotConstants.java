package pro.sky.shelter.configuration;

import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;

/**
 * Class of constants
 * Allows easy-change possibility
 *
 * @autor Shikunov Andrey
 */
public class BotConstants {
    public static final String INITIAL_CMD = "/start";
    public static final String CATS_CMD = "Взять кошку";
    public static final String DOGS_CMD = "Взять собаку";
    public static final String SAFETY_CMD = "Безопасность посещения";
    public static final String USER_CONTACTS_CMD = "Оставить контакты";
    public static final String SHELTER_INFO_CMD = "Контакты приюта";
    public static final String VOLUNTEER_CMD = "Позвать волонтера";
    public static final String MEET_DOG_RULES_CMD = "Правила знакомства с собакой";
    public static final String DOCUMENT_LIST_CMD = "Необходимые документы";
    public static final String TRANSPORTATION_CMD = "Как перевозить";
    public static final String HOME_PREPARATION_CMD = "Подготовка дома";
    public static final String FROM_TRAINER_CMD = "Советы кинолога";
    public static final String DECLINE_REASONS_CMD = "Почему могут отказать";
    public static final String GO_BACK_CMD = "Назад";

    public static final String MEET_DOG_RULES_MSG = "Дайте себя обнюхать, сразу руки не суйте.";
    public static final String DOCUMENT_LIST_MSG = "Нужен паспорт";
    public static final String TRANSPORTATION_MSG = "Обязательно используйте ошейник и поводок.";
    public static final String HOME_PREPARATION_MSG = "Лучше вальер на улице.";
    public static final String FROM_TRAINER_MSG = "Кормить, мыть, не обижать!";
    public static final String DECLINE_REASONS_MSG = "Вы показались нам подозрительным!";

    public static final String PROBLEM_OCCURS_MSG = "Я Вас не понял.";
    public static final String GREETING_MSG = """
            Приветствуем Вас!
            Мы-негосударственный приют для кошек и собак в Астане.
            Приют был открыт в 2020 году командой единомышленников.
            И существует за счет добровольных пожертвований неравнодушных к нашему делу людей.
            Мы арендуем небольшое помещение в частном секторе.
            Если у Вас есть возможность помочь приюту, то мы будем очень признательны за любой вклад.
                        
            Можете просмотреть контактную информацию, либо выбрать кто вас интересует, кошки или собаки?""";
    public static final String CATS_INFO_MSG = """
            Выбрать кошку можно приехав в приют лично.
            1. Предварительно оставьте заявку и назначить встречу.
            2. Кошки отдаются будущему владельцу только после подписания договора о передаче животного.
            3. Мы оставляем за собой право проверять условия содержания животных и возвращаем их в приют в случае, если новые владельцы не соблюдают условия договора.
                        
            Чтобы взять собаку, Вам потребуется:
            • паспорт,
            • ошейник с поводком,
            • транспорт.""";
    public static final String DOGS_INFO_MSG = """
            Выбрать собаку может только потенциальный хозяин, приехав в приют лично.
            1. Предварительно оставьте заявку и назначить встречу.
            2. Собаки отдаются будущему владельцу только после подписания договора о передаче животного.
            3. Мы оставляем за собой право проверять условия содержания животных и возвращаем их в приют в случае, если новые владельцы не соблюдают условия договора.
                        
            Чтобы взять собаку, Вам потребуется:
            • паспорт,
            • переноску для кошек,
            • транспорт.""";
    public static final String SHELTER_INFO_MSG = """
            Адрес нашего приюта:
            г. Астана, район Сарыарка, улица Аккорган, 5в.
            Время работы: Ежедневно с 11:00 до 18:00
            Телефон: +7-702‒111‒22‒33""";
    public static final String SAFETY_MSG = """
            Посетителям запрещается:
            - перемещаться по территории приюта без сопровождающего
            - подходить к клеткам, вольерам и к собакам на привязи без разрешения работника приюта
            - предпринимать попытки потрогать животное через решетку
            - открывать вольеры и закрытые двери без разрешения работника приюта
            - трогать миски животных
            - залезать в будки""";
    public static final String USER_CONTACTS_MSG = "Введите Ваши телефон и имя в формате " +
            "\"+7-999-123-45-67 Имя\" и мы обязательно с вами свяжемся.";
    public static final String VOLUNTEER_MSG = "Ожидайте, с Вами свяжется первый освободившийся волонтер";
    public static final String WARNING_MSG = """
            Дорогой усыновитель, мы заметили, что ты заполняешь отчет не так подробно, как необходимо. 
            Пожалуйста, подойди ответственнее к этому занятию. 
            В противном случае волонтеры приюта будут обязаны самолично проверять условия содержания собаки""";

    public static final ReplyKeyboardMarkup WELCOME_KEYBOARD = new ReplyKeyboardMarkup(
            new String[]{SHELTER_INFO_CMD, SAFETY_CMD},
            new String[]{USER_CONTACTS_CMD, DOGS_CMD},
            new String[]{VOLUNTEER_CMD});

    public static final ReplyKeyboardMarkup CONSULTING_KEYBOARD = new ReplyKeyboardMarkup(
            new String[]{MEET_DOG_RULES_CMD, DOCUMENT_LIST_CMD},
            new String[]{TRANSPORTATION_CMD, HOME_PREPARATION_CMD},
            new String[]{FROM_TRAINER_CMD, DECLINE_REASONS_CMD},
            new String[]{USER_CONTACTS_CMD, VOLUNTEER_CMD},
            new String[]{GO_BACK_CMD});

}
