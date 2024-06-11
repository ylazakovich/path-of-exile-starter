package io.starter.telegram.utils.generator.replykeyboard.buttons;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@UtilityClass
public class InlineKeyboardButtonGenerator {

  public static InlineKeyboardButton generate(String text, String callbackData) {
    return InlineKeyboardButton.builder().text(text).callbackData(callbackData).build();
  }
}
