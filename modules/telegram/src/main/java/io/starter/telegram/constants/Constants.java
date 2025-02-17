package io.starter.telegram.constants;

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

  public static class Start {

    public static final String SKILLS = "Skills";
    public static final String BLESSING = "Blessing Items";
  }

  public static class Settings {

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

    public static final String SETTINGS_UPDATED = "Your settings has been updated";
  }
}
