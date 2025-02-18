package io.starter.telegram.generator.replykeyboard;

import java.util.List;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@UtilityClass
public class InlineKeyboardGenerator {

  public static InlineKeyboardMarkup withRows(List<InlineKeyboardRow> keyboard) {
    return InlineKeyboardMarkup.builder().keyboard(keyboard).build();
  }
}
