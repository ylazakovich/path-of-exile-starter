package io.starter.model.aggregator;

public record VendorRecipeItemDiagnostic(
    String name,
    Integer quantity,
    String source,
    Integer matches,
    Double selectedChaosEquivalent,
    String status
) {
}
