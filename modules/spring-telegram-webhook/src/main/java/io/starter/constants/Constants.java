package io.starter.constants;

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
    public static final String SKILLS_GUIDE_LINK =
        "https://github.com/ylazakovich/path-of-exile-starter/blob/main/docs/guides/how_to_interact_with_skills.md";
    public static final String SKILLS = "Skills";
    public static final String VENDOR_RECIPES = "Vendor Recipes";
  }

  public static class Recipes {
    public static final String ANIMA_STONE = "The Anima Stone";
    public static final String PRIMORDIAL_MIGHT = "Primordial Might";
    public static final String PRIMORDIAL_HARMONY = "Primordial Harmony";
    public static final String PRIMORDIAL_EMINENCE = "Primordial Eminence";
  }

  public static class Settings {

    // TODO: Later need to make a feature for bot to show current user league dynamically
    public static final String ANSWER_FORMAT = """
        If you want change league
        please select one of available options
        
        ⭐ - Your Current League
        
        ➡️ Standard %s
        ➡️ League %s
        ➡️ Hardcore %s
        ➡️ League Hardcore %s""";
    public static final String STANDARD = "Standard";
    public static final String HARDCORE = "Hardcore";
    public static final String LEAGUE = "League";
    public static final String LEAGUE_HARDCORE = "League Hardcore";

    public static final String SETTINGS_UPDATED = "Your settings has been updated";
  }
}
