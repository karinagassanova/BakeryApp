package models

/**
 * Data class representing an ingredient used in baked goods.
 *
 * @property ingredientId The unique identifier for the ingredient.
 * @property ingredientName The name of the ingredient.
 * @property ingredientQuantity The quantity of the ingredient.
 * @property ingredientDescription A description or additional information about the ingredient.
 * @property allergens A set of allergens associated with the ingredient.
 */
data class Ingredient(
    var ingredientId: Int = 0,
    var ingredientName: String = "",
    var ingredientQuantity: Double = 0.0,
    var ingredientDescription: String = "",
    var allergens: Set<String> = emptySet()
) {

    /**
     * Overrides the default toString method to provide a formatted string representation of the Ingredient.
     *
     * @return Formatted string representation of the Ingredient.
     */
    override fun toString(): String {
        return """
            Ingredient ID: $ingredientId
            Ingredient Name: $ingredientName
            Ingredient Quantity: $ingredientQuantity
            Ingredient Description: $ingredientDescription
            Allergens: ${allergens.joinToString(", ")}
        """.trimIndent()
    }
}
