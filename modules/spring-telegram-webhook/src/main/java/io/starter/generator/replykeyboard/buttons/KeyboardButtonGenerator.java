package io.starter.generator.replykeyboard.buttons;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

@UtilityClass
public class KeyboardButtonGenerator {

  public static KeyboardButton generate(String text) {
    return KeyboardButton.builder().text(text).build();
  }
}
