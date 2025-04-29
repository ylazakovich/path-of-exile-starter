package io.starter.generator.replykeyboard.rows;

import java.util.ArrayList;
import java.util.List;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@UtilityClass
public class InlineKeyboardRowGenerator {

  public static List<InlineKeyboardRow> generate(List<InlineKeyboardButton> rows) {
    List<InlineKeyboardRow> keyboard = new ArrayList<>();
    keyboard.add(new InlineKeyboardRow(rows));
    return keyboard;
  }

  @SafeVarargs
  public static List<InlineKeyboardRow> generate(List<InlineKeyboardButton>... rows) {
    List<InlineKeyboardRow> keyboard = new ArrayList<>();
    for (List<InlineKeyboardButton> row : rows) {
      keyboard.add(new InlineKeyboardRow(row));
    }
    return keyboard;
  }
}
