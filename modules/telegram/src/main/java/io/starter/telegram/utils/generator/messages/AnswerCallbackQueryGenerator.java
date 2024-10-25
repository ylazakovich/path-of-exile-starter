package io.starter.telegram.utils.generator.messages;

import io.starter.telegram.constants.Constants;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;

@UtilityClass
public class AnswerCallbackQueryGenerator {

  public static AnswerCallbackQuery generateAnswerCallbackQuery(String callbackQueryId) {
    AnswerCallbackQuery answer = new AnswerCallbackQuery(callbackQueryId);
    answer.setText(Constants.StepWithSettings.SETTINGS_UPDATED);
    return answer;
  }
}
