package io.starter.telegram.service;

import java.util.List;

import io.starter.telegram.cash.state.CallbackState;
import io.starter.telegram.cash.state.MessageState;
import io.starter.telegram.config.Emoji;

import io.starter.telegram.utils.generator.reply_keyboard.InlineKeyboardGenerator;
import io.starter.telegram.utils.generator.reply_keyboard.ReplyKeyboardGenerator;
import io.starter.telegram.utils.generator.messages.EditMessageGenerator;
import io.starter.telegram.utils.generator.messages.SendMessageGenerator;
import io.starter.telegram.utils.generator.reply_keyboard.buttons.InlineKeyboardButtonGenerator;
import io.starter.telegram.utils.generator.reply_keyboard.rows.InlineKeyboardRowGenerator;
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
public class MenuService {

  public SendMessage messageOnFirstStart(Message message) {
    List<String> line1 = List.of(MessageState.START.value, MessageState.SETTINGS.value);
    List<String> line2 = List.of(MessageState.FEEDBACK.value);
    String inlineMessage = """
        %s
        Greetings, Exile **%s**!
        I will tell you the most profitable ways to earn your first Divine.
        """.formatted(Emoji.WAVING_HAND, message.getFrom().getFirstName());
    ReplyKeyboardMarkup keyboard = ReplyKeyboardGenerator.replyMenu(line1, line2);
    return SendMessageGenerator.generate(inlineMessage, message.getChatId(), keyboard);
  }

  public SendMessage messageOnClickStart(Message message) {
    String inlineMessage = "What options do you want to choose ?";
    InlineKeyboardMarkup inlineKeyboard = keyboardOnClickStart();
    return SendMessageGenerator.generate(inlineMessage, message.getChatId(), inlineKeyboard);
  }

  private InlineKeyboardMarkup keyboardOnClickStart() {
    InlineKeyboardButton button1 = InlineKeyboardButtonGenerator.generate("Skills", CallbackState.SKILLS.value);
    InlineKeyboardButton button2 =
        InlineKeyboardButtonGenerator.generate("Blessing Items", CallbackState.ITEMS_AFTER_BLESSING.value);
    List<InlineKeyboardButton> buttons = List.of(button1, button2);
    List<InlineKeyboardRow> keyboard = InlineKeyboardRowGenerator.generate(buttons);
    return InlineKeyboardGenerator.withRows(keyboard);
  }

  private InlineKeyboardMarkup keyboardOnClickSkills() {
    InlineKeyboardButton button =
        InlineKeyboardButtonGenerator.generate("Analyze All Skills", CallbackState.ALL_SKILLS.value);
    List<InlineKeyboardButton> buttons = List.of(button);
    return InlineKeyboardGenerator.withButtons(buttons);
  }

  public EditMessageText messageOnClickSkills(MaybeInaccessibleMessage message) {
    String inlineMessage = """
        GUIDE

        1. Looking for Skill gem      = 20 lvl / no quality
        2. #1 + Gemcutter's Prism =   1  lvl / 20% quality

        Example:
        Faster Attack Support 10
        1. Faster Attack Support - Skill Gem which you can craft and trade on market
        2. 10 - Your expected profit value in Chaos
        """;
    InlineKeyboardMarkup keyboard = keyboardOnClickSkills();
    return EditMessageGenerator.generate(message, inlineMessage, keyboard);
  }
}
