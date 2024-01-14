package uz.nazir.musicbot.thread;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import uz.nazir.musicbot.telegram.TelegramBot;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class AdminConsoleThread extends Thread {

    private static final Logger slf4jLogger = LoggerFactory.getLogger(AdminConsoleThread.class);
    private final TelegramBot bot;
    private boolean exitThread = false;
    Map<Integer, Command> commands = new HashMap<>();
    Scanner reader = new Scanner(System.in);

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_BLUE = "\u001B[34m";

    @Override
    public void run() {
        commands.put(-1, () -> System.exit(0));
        commands.put(0, () -> exitThread = true);
        commands.put(1, () -> bot.sendAd(getUserInputAsString("Input chatId:", reader), getUserInputAsString("Input messageText:", reader)));
        commands.put(2, () -> bot.sendAdToAllUsers(getUserInputAsString("Input messageText:", reader)));
        commands.put(3, () -> bot.forwardAd(getUserInputAsString("Input fromChatId:", reader), getUserInputAsString("Input chatId:", reader), Integer.valueOf(getUserInputAsString("Input messageId:", reader))));
        commands.put(4, () -> bot.forwardAdToAllUsers(getUserInputAsString("Input fromChatId:", reader), Integer.valueOf(getUserInputAsString("Input messageId:", reader))));
        commands.put(5, () -> bot.readMusicFromLocalFolder(getUserInputAsString("Input path:", reader)));
        commands.put(6, () -> bot.sendTrackById(getUserInputAsString("Input chatId", reader), Long.valueOf(getUserInputAsString("Input trackId:", reader))));

        slf4jLogger.info(ANSI_GREEN + "Admin console STARTED" + ANSI_RESET);
        slf4jLogger.info(ANSI_GREEN + "Type " + ANSI_RED + "_ABORT" + ANSI_GREEN + " to abort task" + ANSI_RESET);

        while (!exitThread) {
            printMenu();
            int inputKey = Integer.parseInt(getUserInputAsString("", reader));
            catchCommand(commands.get(inputKey));
            if (exitThread) reader.close();
        }
    }

    private static void printMenu() {
        slf4jLogger.info(ANSI_GREEN + "Supported commands" + ANSI_RESET);
        slf4jLogger.info(ANSI_BLUE + "-1 - " + ANSI_RED + "Exit application" + ANSI_RESET);
        slf4jLogger.info(ANSI_BLUE + "0 - " + ANSI_RED + "Exit thread" + ANSI_RESET);
        slf4jLogger.info(ANSI_BLUE + "1 - Send text to user by chatId" + ANSI_RESET);
        slf4jLogger.info(ANSI_BLUE + "2 - Send text to all users" + ANSI_RESET);
        slf4jLogger.info(ANSI_BLUE + "3 - Forward message to user (fromChatId, chatId, messageId)" + ANSI_RESET);
        slf4jLogger.info(ANSI_BLUE + "4 - Forward message to all users (fromChatId, messageId)" + ANSI_RESET);
        slf4jLogger.info(ANSI_BLUE + "5 - Scan local folder for music files (String path) example E:\\Music" + ANSI_RESET);
        slf4jLogger.info(ANSI_BLUE + "6 - Send music by id from DB (chatId, trackId)" + ANSI_RESET);
    }

    private interface Command {
        void run();
    }

    private static void catchCommand(Command command) {
        try {
            command.run();
        } catch (RuntimeException e) {
            slf4jLogger.warn(e.getMessage());
        }
    }

    private static String getUserInputAsString(String message, Scanner reader) {
        slf4jLogger.info(message);
        try {
            String line = reader.nextLine();
            if (line.contains("_ABORT")) {
                throw new RuntimeException("TASK ABORTED");
            }
            return line;
        } catch (NoSuchElementException e) {
            slf4jLogger.warn(e.getMessage());
        }
        return "0";
    }
}
