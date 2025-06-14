package io.starter.generator.replykeyboard;

import java.util.ArrayList;
import java.util.List;

import io.starter.generator.replykeyboard.buttons.KeyboardButtonGenerator;
import io.starter.generator.replykeyboard.rows.KeyboardRowGenerator;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

@UtilityClass
public class ReplyKeyboardGenerator {

  public static ReplyKeyboardMarkup replyMenu(List<String> line1,
                                              List<String> line2) {
    KeyboardRow row1 = KeyboardRowGenerator.generate(line1.stream().map(KeyboardButtonGenerator::generate).toList());
    KeyboardRow row2 = KeyboardRowGenerator.generate(line2.stream().map(KeyboardButtonGenerator::generate).toList());
    List<KeyboardRow> keyboard = new ArrayList<>(List.of(row1, row2));
    ReplyKeyboardMarkup replyKeyboardMarkup = buildReplyKeyboard();
    replyKeyboardMarkup.setKeyboard(keyboard);
    replyKeyboardMarkup.setResizeKeyboard(true);
    return replyKeyboardMarkup;
  }

  private ReplyKeyboardMarkup buildReplyKeyboard() {
    return ReplyKeyboardMarkup.builder()
        .selective(true)
        .resizeKeyboard(true)
        .oneTimeKeyboard(false)
        .build();
  }
}
