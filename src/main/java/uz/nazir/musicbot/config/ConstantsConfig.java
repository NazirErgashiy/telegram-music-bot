package uz.nazir.musicbot.config;

import org.springframework.stereotype.Component;

@Component
public class ConstantsConfig {

    public static final String TELEGRAM_BOT_TOKEN = "6955080751:AAHfsagYpV83RLa-bf4FogzpMTHPE6P9gG4";//Get it from https://t.me/BotFather
    public static final String TELEGRAM_BOT_USER_NAME = "PikoMusicBot";//Example PikoMusicBot
    public static final String PATH_TO_SAVE_MUSIC_FROM_USERS = "E:\\music-api\\";//Example D:\\music-bot\\
    public static final Long[] ADMIN_ID = new Long[]{1L};//Admin id from DataBase (needs if you want to know your chatId,messageId)
}
