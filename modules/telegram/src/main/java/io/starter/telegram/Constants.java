package io.starter.telegram;

public class Constants {

  public static class General {

    public static final String SEPARATER = " : ";
    public static final String QUESTION = "What options do you want to choose ?";
    public static final String WELCOME = """
      %s
      Greetings, Exile **%s**!
      I will tell you the most profitable ways to earn your first Divine.
      """;
  }

  public static class StepWithAnalyze {

    public static final String SKILLS_GUIDE = """
        GUIDE

        1. Looking for Skill gem      = 20 lvl / no quality
        2. #1 + Gemcutter's Prism =   1  lvl / 20% quality

        Example:
        Faster Attack Support 10
        1. Faster Attack Support - Skill Gem which you can craft and trade on market
        2. 10 - Your expected profit value in Chaos""";
    public static final String ALL_SKILLS = "Analyze All Skill`s";
    public static final String SKILLS = "Skills";
    public static final String BLESSING = "Blessing Items";
  }

  public static class StepWithSettings {

    // TODO: Later need to make a feature for bot to show current user league dynamically
    public static final String ANSWER = """
        If you want change league
        please select one of available options
        
        ⭐ - Your Current League
        
        ➡️ Standard
        ➡️ League ⭐
        ➡️ Hardcore
        ➡️ League Hardcore""";
    public static final String STANDARD = "Standard";
    public static final String HARDCORE = "Hardcore";
    public static final String LEAGUE = "League";
    public static final String LEAGUE_HARDCORE = "League Hardcore";
  }
}
