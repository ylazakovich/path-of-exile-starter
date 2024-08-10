package io.starter.telegram.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.starter.telegram.Constants;
import io.starter.telegram.cash.state.CallbackState;
import io.starter.telegram.cash.state.MessageState;
import io.starter.telegram.constants.Emoji;
import io.starter.telegram.utils.generator.messages.EditMessageGenerator;
import io.starter.telegram.utils.generator.messages.SendMessageGenerator;
import io.starter.telegram.utils.generator.replykeyboard.InlineKeyboardGenerator;
import io.starter.telegram.utils.generator.replykeyboard.ReplyKeyboardGenerator;
import io.starter.telegram.utils.generator.replykeyboard.buttons.InlineKeyboardButtonGenerator;
import io.starter.telegram.utils.generator.replykeyboard.rows.InlineKeyboardRowGenerator;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.message.MaybeInaccessibleMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@Service
public class OnMessageAnswerService {

  public OnMessageAnswerService() {
  }

  public SendMessage onFirstStart(Message message) {
    List<String> line1 = List.of(MessageState.START.value, MessageState.SETTINGS.value);
    List<String> line2 = List.of(MessageState.FEEDBACK.value);
    String firstName = message.getFrom().getFirstName();
    String inlineMessage = Constants.WELCOME.formatted(Emoji.WAVING_HAND, firstName);
    ReplyKeyboardMarkup keyboard = ReplyKeyboardGenerator.replyMenu(line1, line2);
    return SendMessageGenerator.generate(inlineMessage, message.getChatId(), keyboard);
  }

  public SendMessage onClickStart(Message message) {
    String inlineMessage = Constants.QUESTION;
    InlineKeyboardMarkup inlineKeyboard = keyboardOnClickStart();
    return SendMessageGenerator.generate(inlineMessage, message.getChatId(), inlineKeyboard);
  }

  private InlineKeyboardMarkup keyboardOnClickStart() {
    InlineKeyboardButton button1 = InlineKeyboardButtonGenerator.generate(Constants.SKILLS, CallbackState.SKILLS.value);
    InlineKeyboardButton button2 = InlineKeyboardButtonGenerator
        .generate(Constants.BLESSING, CallbackState.ITEMS_AFTER_BLESSING.value);
    List<InlineKeyboardButton> buttons = List.of(button1, button2);
    List<InlineKeyboardRow> keyboard = InlineKeyboardRowGenerator.generate(buttons);
    return InlineKeyboardGenerator.withRows(keyboard);
  }

  public EditMessageText onClickSkills(MaybeInaccessibleMessage message) {
    String inlineMessage = Constants.SKILLS_GUIDE;
    InlineKeyboardMarkup keyboard = keyboardOnClickSkills();
    return EditMessageGenerator.generate(message, inlineMessage, keyboard);
  }

  private InlineKeyboardMarkup keyboardOnClickSkills() {
    InlineKeyboardButton button = InlineKeyboardButtonGenerator
        .generate(Constants.ALL_SKILLS, CallbackState.ALL_SKILLS.value);
    List<InlineKeyboardButton> buttons = new ArrayList<>(Collections.singleton(button));
    return InlineKeyboardGenerator.withButtons(buttons);
  }
}
