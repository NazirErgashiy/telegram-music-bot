package uz.nazir.musicbot.telegram.keyboard;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import uz.nazir.musicbot.service.dto.response.TrackResponseDto;

import java.util.ArrayList;
import java.util.List;

@Component
public class SelectMusicKeyboard {

    public SendMessage sendKeyboard(long chatId, List<TrackResponseDto> tracks, int page, int fullSize) {
        SendMessage resultMessage = new SendMessage();
        resultMessage.setChatId(chatId);
        resultMessage.setText(getInfoFromTracks(tracks, page, fullSize));

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = createButtons(tracks);
        List<InlineKeyboardButton> row3 = new ArrayList<>();

        InlineKeyboardButton buttonLeft = new InlineKeyboardButton();
        buttonLeft.setText("⬅️");
        buttonLeft.setCallbackData("_LEFT");

        InlineKeyboardButton buttonDelete = new InlineKeyboardButton();
        buttonDelete.setText("♻️");
        buttonDelete.setCallbackData("_DELETE");

        InlineKeyboardButton buttonRight = new InlineKeyboardButton();
        buttonRight.setText("➡️");
        buttonRight.setCallbackData("_RIGHT");

        row3.add(buttonLeft);
        row3.add(buttonDelete);
        row3.add(buttonRight);
        rowsInline.add(row3);

        markupInline.setKeyboard(rowsInline);
        resultMessage.setReplyMarkup(markupInline);
        return resultMessage;
    }

    private List<List<InlineKeyboardButton>> createButtons(List<TrackResponseDto> tracks) {
        List<List<InlineKeyboardButton>> result = new ArrayList<>();
        List<InlineKeyboardButton> buttonsStage1 = new ArrayList<>();
        List<InlineKeyboardButton> buttonsStage2 = new ArrayList<>();

        for (int i = 0; i < tracks.size(); i++) {
            InlineKeyboardButton currentButton = new InlineKeyboardButton();
            currentButton.setText(String.valueOf(i + 1));
            currentButton.setCallbackData(tracks.get(i).getId().toString());
            if (i < 5) {
                buttonsStage1.add(currentButton);
            } else if (i < 10) {
                buttonsStage2.add(currentButton);
            }
            if (i > 10) break;
        }

        result.add(buttonsStage1);
        result.add(buttonsStage2);
        return result;
    }

    private String getInfoFromTracks(List<TrackResponseDto> tracks, int page, int fullSize) {
        StringBuilder builder = new StringBuilder();

        builder.append("Total matches: ").append(fullSize).append("\n");
        builder.append("Page: ").append(page + 1).append(" of ").append(fullSize / 10 + 1).append("\n");
        builder.append("\n");
        for (int i = 0; i < tracks.size(); i++) {
            if (i < 10) {
                builder.append(i + 1).append(". ").append(tracks.get(i).getName()).append("\n");
            } else {
                break;
            }
        }
        return builder.toString();
    }
}
