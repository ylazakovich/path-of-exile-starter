package io.starter.service;

import java.util.List;

import io.starter.cache.state.CallbackState;
import io.starter.cache.state.MessageState;
import io.starter.constants.Constants;
import io.starter.constants.Emoji;
import io.starter.generator.messages.SendMessageGenerator;
import io.starter.generator.replykeyboard.InlineKeyboardGenerator;
import io.starter.generator.replykeyboard.ReplyKeyboardGenerator;
import io.starter.generator.replykeyboard.buttons.InlineKeyboardButtonGenerator;
import io.starter.generator.replykeyboard.rows.InlineKeyboardRowGenerator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@Service
@AllArgsConstructor
public class MessageAnswerService {

  private final SettingsService settingsService;

  public SendMessage onFirstStart(Message message) {
    List<String> line1 = List.of(MessageState.START.value, MessageState.SETTINGS.value);
    List<String> line2 = List.of(MessageState.FEEDBACK.value);
    String firstName = message.getFrom().getFirstName();
    String inlineMessage = Constants.General.WELCOME.formatted(Emoji.WAVING_HAND, firstName);
    ReplyKeyboardMarkup keyboard = ReplyKeyboardGenerator.replyMenu(line1, line2);
    return SendMessageGenerator.generate(inlineMessage, message.getChatId(), keyboard);
  }

  public SendMessage onClickStart(Message message) {
    String inlineMessage = Constants.General.QUESTION;
    InlineKeyboardMarkup inlineKeyboard = keyboardOnClickStart();
    return SendMessageGenerator.generate(inlineMessage, message.getChatId(), inlineKeyboard);
  }

  private InlineKeyboardMarkup keyboardOnClickStart() {
    InlineKeyboardButton button1 = InlineKeyboardButtonGenerator
        .generate(Constants.Start.SKILLS, CallbackState.SKILLS.value);
    InlineKeyboardButton button2 = InlineKeyboardButtonGenerator
        .generate(Constants.Start.VENDOR_RECIPES, CallbackState.VENDOR_RECIPES.value);
    List<InlineKeyboardButton> row1 = List.of(button1, button2);
    List<InlineKeyboardRow> keyboard = InlineKeyboardRowGenerator.generate(row1);
    return InlineKeyboardGenerator.withRows(keyboard);
  }

  public SendMessage onClickSettings(Message message) {
    String inlineMessage = settingsService.generateInlineMessage(message.getFrom());
    InlineKeyboardMarkup inlineKeyboard = settingsService.generateKeyboard();
    return SendMessageGenerator.generate(inlineMessage, message.getChatId(), inlineKeyboard);
  }
}
