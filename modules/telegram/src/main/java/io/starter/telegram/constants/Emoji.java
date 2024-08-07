package io.starter.telegram.constants;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Emoji {

  PERFORMING_ARTS("🎭"),
  YELLOW_RIBBON("🎗"),
  WAVING_HAND("👋"),
  WARNING("⚠️"),
  GAME_DIE("🎲"),
  GEAR("⚙️"),
  REPEAT("🔄");

  public final String value;

  @Override
  public String toString() {
    return value;
  }
}
