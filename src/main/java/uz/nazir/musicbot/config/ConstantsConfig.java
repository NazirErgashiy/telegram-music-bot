package uz.nazir.musicbot.config;

import org.springframework.stereotype.Component;

@Component
public class ConstantsConfig {

    public static final String TELEGRAM_BOT_TOKEN = "";//Get it from https://t.me/BotFather
    public static final String TELEGRAM_BOT_USER_NAME = "";//Example PikoMusicBot
    public static final String PATH_TO_SAVE_MUSIC_FROM_USERS = "";//Example D:\\music-bot\\
    public static final Long[] ADMIN_ID = new Long[]{};//Admin id from DataBase (needs if you want to know your chatId,messageId)
}
