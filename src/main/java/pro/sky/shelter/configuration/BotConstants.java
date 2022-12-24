package pro.sky.shelter.configuration;

public class BotConstants {
    public static final String INITIAL_MSG =            "/start";
    public static final String CATS_MSG =               "/cat";
    public static final String DOGS_MSG =               "/dog";
    public static final String PROBLEM_OCCURS_MSG =     "Я Вас не понял.";
    public static final String GREETING_MSG =           "Приветствую!\n" +
                                                        "Я могу Вас проинформировать о нашем приюте для животных!\n" +
                                                        "Кто Вас интересует?\nНаберите /dog если собака, либо /cat если кошка.";
    public static final String CATS_INFO_MSG =          "В нашем приюте вы можете выбрать черных, белых, вообще любых цветов кошек :)";
    public static final String DOGS_INFO_MSG =          "В нашем приюте есть и пародистые собаки и дворняги ;)";
    public static final String ERROR_MSG =              "Что-то пошло не так..." +
                                                        "Измените пожалуйста дату/время на будущее!";
    public static final String CRON_TASK_PERIOD =       "*/1 * * * * *";
    public static final String TEXT_PATTERN =           "([0-9\\.\\:\\s]{16})\\s([\\w|\\W+]+)";
}
