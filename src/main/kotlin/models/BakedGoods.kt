package models

import utils.Utilities

data class BakedGoods(
    var productId: Int = 0,
    var productName: String,
    var productDesc: String,
    var productPrice: Double,
    var productCategory: String,
    var refrigeratedOrNot: Boolean = false,
    var ingredients: MutableList<Ingredient> = mutableListOf()
) {

    private var lastIngredientId = 0
    private fun getIngredientId() = lastIngredientId++

    fun addIngredient(ingredient: Ingredient): Boolean {
        ingredient.ingredientId = getIngredientId()
        return ingredients.add(ingredient)
    }

    fun numberOfIngredients() = ingredients.size

    fun findIngredient(id: Int): Ingredient? {
        return ingredients.find { ingredient -> ingredient.ingredientId == id }
    }

    fun deleteIngredient(id: Int): Boolean {
        return ingredients.removeIf { ingredient -> ingredient.ingredientId == id }
    }

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

    fun listIngredients(): String =
        if (ingredients.isEmpty()) {
            "\tNO INGREDIENTS ADDED"
        } else {
            Utilities.formatSetString(ingredients)
        }

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
