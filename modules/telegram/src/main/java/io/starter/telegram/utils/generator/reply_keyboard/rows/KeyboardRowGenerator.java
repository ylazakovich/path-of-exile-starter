package io.starter.telegram.utils.generator.reply_keyboard.rows;

import java.util.List;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

@UtilityClass
public class KeyboardRowGenerator {

  public static KeyboardRow generate(List<KeyboardButton> buttons) {
    KeyboardRow keyboardRow = new KeyboardRow();
    keyboardRow.addAll(buttons);
    return keyboardRow;
  }

  public static KeyboardRow generate(KeyboardButton button) {
    KeyboardRow keyboardRow = new KeyboardRow();
    keyboardRow.add(button);
    return keyboardRow;
  }
}
