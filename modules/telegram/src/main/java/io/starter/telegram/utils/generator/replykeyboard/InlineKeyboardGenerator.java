package io.starter.telegram.utils.generator.replykeyboard;

import java.util.ArrayList;
import java.util.List;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@UtilityClass
public class InlineKeyboardGenerator {

  public static InlineKeyboardMarkup withRows(List<InlineKeyboardRow> keyboard) {
    return InlineKeyboardMarkup.builder().keyboard(keyboard).build();
  }

  public static InlineKeyboardMarkup withButtons(List<InlineKeyboardButton> buttons) {
    List<InlineKeyboardRow> keyboard = new ArrayList<>(buttons.stream().map(InlineKeyboardRow::new).toList());
    return InlineKeyboardMarkup.builder().keyboard(keyboard).build();
  }
}
