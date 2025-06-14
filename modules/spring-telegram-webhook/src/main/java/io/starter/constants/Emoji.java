package io.starter.constants;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Emoji {

  LEFT("⬅️"),
  RIGHT("➡️"),
  PERFORMING_ARTS("🎭"),
  YELLOW_RIBBON("💎"),
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
