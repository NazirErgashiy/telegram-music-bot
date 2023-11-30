package uz.nazir.musicbot.telegram;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.facilities.filedownloader.TelegramFileDownloader;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Audio;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.nazir.musicbot.config.ConstantsConfig;
import uz.nazir.musicbot.service.TrackService;
import uz.nazir.musicbot.service.UserService;
import uz.nazir.musicbot.service.dto.request.TrackRequestDto;
import uz.nazir.musicbot.service.dto.response.TrackResponseDto;
import uz.nazir.musicbot.service.dto.response.UserResponseDto;
import uz.nazir.musicbot.telegram.keyboard.SelectMusicKeyboard;
import uz.nazir.musicbot.telegram.request.TelegramHttpRequestManager;
import uz.nazir.musicbot.telegram.request.dto.PostRequestFather;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final TrackService trackService;
    private final UserService userService;
    private final TelegramHttpRequestManager telegramHttpRequestManager;
    private final SelectMusicKeyboard selectMusicKeyboard;
    Supplier<String> b = this::getBotToken;
    private final TelegramFileDownloader telegramFileDownloader = new TelegramFileDownloader(b);
    private static final Logger slf4jLogger = LoggerFactory.getLogger(TelegramBot.class);

    @Override
    public void onUpdateReceived(Update update) {
        //Saving user to DB
        userService.saveOrUpdateUser(update);

        //Download music from user
        if (update.hasMessage() && update.getMessage().hasAudio()) {
            long chatId = update.getMessage().getChatId();
            Audio audio = update.getMessage().getAudio();

            try {
                PostRequestFather postRequestFather = telegramHttpRequestManager.downloadMusicFromChat("https://api.telegram.org/bot" + getBotToken() + "/getFile?file_id=" + audio.getFileId());
                File music = telegramFileDownloader.downloadFile(postRequestFather.getResult().getFile_path());

                TrackRequestDto track = new TrackRequestDto();
                track.setName(audio.getFileName());

                trackService.saveTrack(track, music, update);

                sendMessage(chatId, "Success");
            } catch (TelegramApiException | IOException e) {
                sendMessage(chatId, "Failure");
                throw new RuntimeException(e);
            }
        }

        //Send list of music name
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getUserName());
                    break;
                default:
                    List<TrackResponseDto> tracks = trackService.getTrackByName(messageText, 0, 10);
                    int size = trackService.getTrackByName(messageText).size();
                    userService.saveUserPagination(String.valueOf(chatId), 0);
                    userService.saveUserSearch(String.valueOf(chatId), messageText);
                    if (tracks.size() == 0) {
                        sendMessage(chatId, "No results");
                        return;
                    }
                    sendMessage(selectMusicKeyboard.sendKeyboard(chatId, tracks, 0, size));
                    break;
            }
        }

        //Compute buttons
        if (update.hasCallbackQuery()) {
            String callBack = update.getCallbackQuery().getData();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
            UserResponseDto user = userService.getUserByChatId(String.valueOf(chatId));
            int size = trackService.getTrackByName(user.getSearch()).size();
            switch (callBack) {
                case "_LEFT":
                    if (user.getPage() > 0) {
                        deleteMessage(chatId, messageId);
                        List<TrackResponseDto> tracks = trackService.getTrackByName(user.getSearch(), user.getPage() - 1, 10);
                        sendMessage(selectMusicKeyboard.sendKeyboard(chatId, tracks, user.getPage() - 1, size));
                        userService.saveUserPagination(String.valueOf(chatId), user.getPage() - 1);
                    }
                    break;
                case "_DELETE":
                    deleteMessage(chatId, messageId);
                    break;
                case "_RIGHT":
                    deleteMessage(chatId, messageId);
                    List<TrackResponseDto> tracks = trackService.getTrackByName(user.getSearch(), user.getPage() + 1, 10);
                    sendMessage(selectMusicKeyboard.sendKeyboard(chatId, tracks, user.getPage() + 1, size));
                    userService.saveUserPagination(String.valueOf(chatId), user.getPage() + 1);
                    break;
            }
            try {
                //Send music to user (byMusicId)
                sendTrackById(String.valueOf(chatId), Long.parseLong(callBack));
            } catch (NumberFormatException ignored) {
            }
        }
    }

    @Override
    public String getBotUsername() {
        return ConstantsConfig.TELEGRAM_BOT_USER_NAME;
    }

    @Override
    public String getBotToken() {
        // Return bot token from BotFather
        return ConstantsConfig.TELEGRAM_BOT_TOKEN;
    }

    private void startCommandReceived(Long chatId, String name) {
        String answer = "Hi, " + name + ", nice to meet you!" + "\n" +
                "Type the name of song" + "\n";
        sendMessage(chatId, answer);
    }

    public void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException ignore) {
        }
    }

    public void sendMessage(Long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException ignore) {
        }
    }

    public void sendTrack(Long chatId, File track) {
        InputFile inputFile = new InputFile();
        inputFile.setMedia(track);

        SendAudio sendAudio = new SendAudio();
        sendAudio.setChatId(String.valueOf(chatId));
        sendAudio.setAudio(inputFile);
        try {
            execute(sendAudio);
        } catch (TelegramApiException ignore) {
        }
    }

    public void deleteMessage(Long chatId, Integer messageId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId);
        deleteMessage.setMessageId(messageId);
        try {
            execute(deleteMessage);
        } catch (TelegramApiException ignore) {
        }
    }

    public void readMusicFromLocalFolder(String path) {
        trackService.readTracksFromLocalDirectory(path);
    }

    public void forwardAd(String fromChatId, String chatId, Integer messageId) {
        ForwardMessage message = new ForwardMessage();
        message.setChatId(chatId);
        message.setFromChatId(fromChatId);
        message.setMessageId(messageId);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            slf4jLogger.warn("FAILED TO DELIVER AD");
            slf4jLogger.warn(Arrays.toString(e.getStackTrace()));
        }
    }

    public void forwardAdToAllUsers(String fromChatId, Integer messageId) {
        List<UserResponseDto> allUsers = userService.getAllUsers();

        for (UserResponseDto user : allUsers) {
            forwardAd(fromChatId, user.getCode(), messageId);
        }
    }

    public void sendAd(String chatId, String messageText) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(messageText);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            slf4jLogger.warn("FAILED TO DELIVER AD");
            slf4jLogger.warn(Arrays.toString(e.getStackTrace()));
        }
    }

    public void sendAdToAllUsers(String messageText) {
        List<UserResponseDto> allUsers = userService.getAllUsers();

        for (UserResponseDto user : allUsers) {
            sendAd(user.getCode(), messageText);
        }
    }

    public void sendTrackById(String chatId, Long trackId) {
        sendTrack(Long.valueOf(chatId), trackService.getTrack(trackId));
    }
}
