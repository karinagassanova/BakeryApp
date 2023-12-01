package models

import utils.Utilities

/**
 * Data class representing a baked goods product.
 *
 * @property productId The unique identifier for the baked goods product.
 * @property productName The name of the baked goods product.
 * @property productDesc The description of the baked goods product.
 * @property productPrice The price of the baked goods product.
 * @property productCategory The category of the baked goods product.
 * @property refrigeratedOrNot A flag indicating whether the baked goods product requires refrigeration.
 * @property ingredients The list of ingredients used in the baked goods product.
 */
data class BakedGoods(
    var productId: Int = 0,
    var productName: String,
    var productDesc: String,
    var productPrice: Double,
    var productCategory: String,
    var refrigeratedOrNot: Boolean = false,
    var ingredients: MutableList<Ingredient> = mutableListOf()
) {

    /**
     * Generates a unique identifier for ingredients.
     *
     * @return A unique identifier for an ingredient.
     */
    private var lastIngredientId = 0
    private fun getIngredientId() = lastIngredientId++

    /**
     * Adds an ingredient to the list of ingredients for the baked goods product.
     *
     * @param ingredient The ingredient to be added.
     * @return True if the addition is successful, false otherwise.
     */
    fun addIngredient(ingredient: Ingredient): Boolean {
        ingredient.ingredientId = getIngredientId()
        return ingredients.add(ingredient)
    }

    /**
     * Returns the number of ingredients in the baked goods product.
     *
     * @return The number of ingredients.
     */
    fun numberOfIngredients() = ingredients.size

    /**
     * Finds an ingredient by its unique identifier.
     *
     * @param id The unique identifier of the ingredient.
     * @return The found ingredient or null if not found.
     */
    fun findIngredient(id: Int): Ingredient? {
        return ingredients.find { ingredient -> ingredient.ingredientId == id }
    }

    /**
     * Deletes an ingredient by its unique identifier.
     *
     * @param id The unique identifier of the ingredient to be deleted.
     * @return True if the deletion is successful, false otherwise.
     */
    fun deleteIngredient(id: Int): Boolean {
        return ingredients.removeIf { ingredient -> ingredient.ingredientId == id }
    }

    /**
     * Updates an ingredient by its unique identifier with new information.
     *
     * @param id The unique identifier of the ingredient to be updated.
     * @param newIngredient The new information for the ingredient.
     * @return True if the update is successful, false otherwise.
     */
    fun updateIngredient(id: Int, newIngredient: Ingredient): Boolean {
        val foundIngredient = findIngredient(id)

        if (foundIngredient != null) {
            foundIngredient.ingredientName = newIngredient.ingredientName
            foundIngredient.ingredientQuantity = newIngredient.ingredientQuantity
            foundIngredient.ingredientDescription = newIngredient.ingredientDescription
            foundIngredient.allergens = newIngredient.allergens
            return true
        }

        return false
    }

    /**
     * Returns a formatted string representation of the ingredients.
     *
     * @return Formatted string of ingredients or a message if no ingredients are added.
     */
    fun listIngredients(): String =
        if (ingredients.isEmpty()) {
            "\tNO INGREDIENTS ADDED"
        } else {
            Utilities.formatSetString(ingredients)
        }

    /**
     * Overrides the default toString method to provide a formatted string representation of the baked goods product.
     *
     * @return Formatted string representation of the baked goods product.
     */
    override fun toString(): String {
        val refrigerated = if (refrigeratedOrNot) 'Y' else 'N'
        return "Product ID: $productId\n" +
            "Product Name: $productName\n" +
            "Product Description: $productDesc\n" +
            "Product Price: $productPrice\n" +
            "Product Category: $productCategory\n" +
            "Refrigerated: $refrigerated\n" +
            "Ingredients:\n" +
            listIngredients()
    }
}
