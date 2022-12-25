package pro.sky.shelter.configuration;

/**
 * Class of String constants
 * Allows easy-change possibility
 *
 * @autor Shikunov Andrey
 */
public class BotConstants {
    public static final String INITIAL_MSG = "/start";
    public static final String CATS_MSG = "/cat";
    public static final String DOGS_MSG = "/dog";
    public static final String PROBLEM_OCCURS_MSG = "Я Вас не понял.";
    public static final String GREETING_MSG = """
            Приветствую!
            Я могу Вас проинформировать о нашем приюте для животных!
            Кто Вас интересует?
            Наберите /dog если собака, либо /cat если кошка.""";
    public static final String CATS_INFO_MSG = "В нашем приюте вы можете выбрать черных, белых, вообще любых цветов кошек :)";
    public static final String DOGS_INFO_MSG = "В нашем приюте есть и пародистые собаки и дворняги ;)";
    public static final String ERROR_MSG = "Что-то пошло не так...";
}
