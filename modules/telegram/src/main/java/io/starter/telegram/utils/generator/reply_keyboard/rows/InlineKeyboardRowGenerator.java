package io.starter.telegram.utils.generator.reply_keyboard.rows;

import java.util.ArrayList;
import java.util.List;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@UtilityClass
public class InlineKeyboardRowGenerator {

  public static List<InlineKeyboardRow> generate(List<InlineKeyboardButton> buttons) {
    List<InlineKeyboardRow> keyboard = new ArrayList<>();
    keyboard.add(new InlineKeyboardRow(buttons));
    return keyboard;
  }
}
