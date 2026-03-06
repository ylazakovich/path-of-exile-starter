package io.starter.model.aggregator;

import java.util.List;

public record VendorRecipeDiagnostic(
    String recipeId,
    String recipeName,
    VendorRecipeItemDiagnostic result,
    List<VendorRecipeItemDiagnostic> ingredients,
    boolean hasMissing,
    boolean hasAmbiguous
) {
}
