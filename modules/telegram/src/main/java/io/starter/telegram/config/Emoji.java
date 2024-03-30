package io.starter.telegram.config;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Emoji {
  WARNING(	"⚠️"),
  YELLOW_RIBBON("🎗"),
  GEAR("⚙️"),
  GAME_DIE("🎲"),
  PERFORMING_ARTS("🎭");

  public final String value;

  @Override
  public String toString() {
    return value;
  }
}
