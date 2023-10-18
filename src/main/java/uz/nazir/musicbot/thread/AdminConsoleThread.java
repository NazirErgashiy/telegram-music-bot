package uz.nazir.musicbot.thread;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uz.nazir.musicbot.telegram.TelegramBot;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class AdminConsoleThread extends Thread {

    private final TelegramBot bot;
    private boolean exitThread = false;
    Map<Integer, Command> commands = new HashMap<>();
    Scanner reader = new Scanner(System.in);

    @Override
    public void run() {
        commands.put(0, () -> exitThread = true);
        commands.put(1, () -> bot.sendAd(getUserInputAsString("Input chatId:", reader), getUserInputAsString("Input messageText:", reader)));
        commands.put(2, () -> bot.sendAdToAllUsers(getUserInputAsString("Input messageText:", reader)));
        commands.put(3, () -> bot.forwardAd(getUserInputAsString("Input fromChatId:", reader), getUserInputAsString("Input chatId:", reader), Integer.valueOf(getUserInputAsString("Input messageId:", reader))));
        commands.put(4, () -> bot.forwardAdToAllUsers(getUserInputAsString("Input fromChatId:", reader), Integer.valueOf(getUserInputAsString("Input messageId:", reader))));
        commands.put(5, () -> bot.readMusicFromLocalFolder(getUserInputAsString("Input path:", reader)));
        commands.put(6, () -> bot.sendTrackById(getUserInputAsString("Input chatId", reader), Long.valueOf(getUserInputAsString("Input trackId:", reader))));

        System.out.println("Admin console STARTED");

        while (!exitThread) {
            System.out.println("Session - OPEN");
            printMenu();
            int inputKey = Integer.parseInt(getUserInputAsString("", reader));
            catchThis(commands.get(inputKey));
            if (exitThread) reader.close();
            System.out.println("Session - CLOSED");
        }
    }

    private static void printMenu() {
        System.out.println("Supported commands");
        System.out.println("0 - Exit");
        System.out.println("1 - Send text to user by chatId");
        System.out.println("2 - Send text to all users");
        System.out.println("3 - Forward message to user (fromChatId,chatId,messageId)");
        System.out.println("4 - Forward message to all users (fromChatId,messageId)");
        System.out.println("5 - Scan local folder for music files (String path) example E:\\Music");
        System.out.println("6 - Send music by id from DB (chatId,trackId)");
    }

    public interface Command {
        void run();
    }

    private static void catchThis(Command command) {
        try {
            command.run();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    private static String getUserInputAsString(String message, Scanner reader) {
        System.out.println(message);
        try {
            String line = reader.nextLine();
            if (line.contains("_ABORT")) {
                throw new RuntimeException("TASK ABORTED");
            }
            return line;
        } catch (InputMismatchException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }
}
