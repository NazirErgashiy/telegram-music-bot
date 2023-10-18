package uz.nazir.musicbot.telegram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import uz.nazir.musicbot.config.ConstantsConfig;
import uz.nazir.musicbot.thread.AdminConsoleThread;

@Component
public class TelegramBotRegistration {

    @Autowired
    public TelegramBotRegistration(TelegramBot bot) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(bot);
        } catch (
                TelegramApiException e) {
            System.out.println(e.getStackTrace());
        }

        AdminConsoleThread console = new AdminConsoleThread(bot);
        console.start();
    }
}
