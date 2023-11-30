package models

data class Ingredient(
    var ingredientId: Int = 0,
    var ingredientName: String = "",
    var ingredientQuantity: Double = 0.0,
    var ingredientDescription: String = "",
    var allergens: Set<String> = emptySet()
) {

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
