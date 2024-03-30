package io.starter.telegram.config;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Emoji {
  PERFORMING_ARTS("🎭"),
  YELLOW_RIBBON("🎗"),
  WAVING_HAND("👋"),
  WARNING(	"⚠️"),
  GAME_DIE("🎲"),
  GEAR("⚙️");

  public final String value;

  @Override
  public String toString() {
    return value;
  }
}
