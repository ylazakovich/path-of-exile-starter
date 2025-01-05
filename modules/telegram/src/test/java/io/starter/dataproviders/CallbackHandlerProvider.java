package io.starter.dataproviders;

import io.starter.telegram.cash.state.CallbackState;

import org.testng.annotations.DataProvider;

public class CallbackHandlerProvider {

  @DataProvider()
  public Object[][] whenUserInSkillsMenu() {
    return new Object[][] {
        {CallbackState.ALL_SKILLS},
        {CallbackState.SKILLS_PREVIOUS},
        {CallbackState.SKILLS_NEXT},
        {CallbackState.REFRESH_SKILLS}
    };
  }

  @DataProvider()
  public Object[][] whenUserInSettingsMenu() {
    return new Object[][] {
        {CallbackState.SETTING_STANDARD},
        {CallbackState.SETTING_LEAGUE},
        {CallbackState.SETTING_HARDCORE},
        {CallbackState.SETTING_LEAGUE_HARDCORE}
    };
  }
}
