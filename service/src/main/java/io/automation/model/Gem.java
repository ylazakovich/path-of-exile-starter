package io.automation.model;

public record Gem(
    String name,
    String variant,
    boolean corrupted,
    int gemLevel,
    int gemQuality,
    double chaosValue
) {

}
