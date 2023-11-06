package uz.nazir.musicbot.thread;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Starts threads after spring application starts
 */
@Component
public class ThreadStarter implements ApplicationListener<ApplicationReadyEvent> {
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        AdminConsoleThread consoleThread = event.getApplicationContext().getBean(AdminConsoleThread.class);
        consoleThread.setName("AdminConsole");
        consoleThread.start();
    }
}
