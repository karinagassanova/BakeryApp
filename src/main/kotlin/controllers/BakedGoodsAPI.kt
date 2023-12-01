package controllers

import models.BakedGoods
import models.Ingredient
import persistence.Serializer
import utils.Utilities.formatListString
import utils.Utilities.isValidListIndex

/**
 * API for managing baked goods and ingredients.
 *
 * @property serializer The serializer used to read and write data.
 * @property ingredientList The list of ingredients.
 * @property bakedGoodsList The list of baked goods.
 */
class BakedGoodsAPI(serializerType: Serializer) {

    private var serializer: Serializer = serializerType
    private var ingredientList = ArrayList<Ingredient>()
    private var bakedGoodsList = ArrayList<BakedGoods>()

    /**
     * Loads baked goods from the serializer.
     *
     * @throws Exception if there is an error during the loading process.
     */
    @Throws(Exception::class)
    fun load() {
        bakedGoodsList = serializer.read() as ArrayList<BakedGoods>
    }

    /**
     * Stores the current state of baked goods using the serializer.
     *
     * @throws Exception if there is an error during the storing process.
     */
    @Throws(Exception::class)
    fun store() {
        serializer.write(bakedGoodsList)
    }

    /**
     * Adds a baked good to the list.
     *
     * @param bakedGoods The baked good to add.
     * @return `true` if the addition is successful, `false` otherwise.
     */
    fun add(bakedGoods: BakedGoods): Boolean {
        return bakedGoodsList.add(bakedGoods)
    }

    /**
     * Deletes a baked good from the list by index.
     *
     * @param indexToDelete The index of the baked good to delete.
     * @return The deleted baked good, or `null` if the index is invalid.
     */
    fun deleteBakedGood(indexToDelete: Int): BakedGoods? {
        return if (isValidListIndex(indexToDelete, bakedGoodsList)) {
            bakedGoodsList.removeAt(indexToDelete)
        } else {
            null
        }
    }

    /**
     * Finds a baked good in the list by index.
     *
     * @param index The index of the baked good to find.
     * @return The found baked good, or `null` if the index is invalid.
     */
    fun findBakedGoods(index: Int): BakedGoods? {
        return if (isValidIndex(index)) {
            bakedGoodsList[index]
        } else {
            null
        }
    }

    /**
     * Checks if the given index is valid for the list of baked goods.
     *
     * @param index The index to check.
     * @return `true` if the index is valid, `false` otherwise.
     */
    fun isValidIndex(index: Int): Boolean {
        return isValidListIndex(index, bakedGoodsList)
    }

    /**
     * Updates the details of an existing baked good.
     *
     * @param indexToUpdate The index of the baked good to update.
     * @param updatedBakedGoods The updated details for the baked good.
     * @return `true` if the update is successful, `false` otherwise.
     */

    fun updateBakedGood(indexToUpdate: Int, updatedBakedGoods: BakedGoods?): Boolean {
        val foundBakedGoods = findBakedGoods(indexToUpdate)

        if ((foundBakedGoods != null) && (updatedBakedGoods != null)) {
            foundBakedGoods.productId = updatedBakedGoods.productId
            foundBakedGoods.productName = updatedBakedGoods.productName
            foundBakedGoods.productDesc = updatedBakedGoods.productDesc
            foundBakedGoods.productPrice = updatedBakedGoods.productPrice
            foundBakedGoods.productCategory = updatedBakedGoods.productCategory
            foundBakedGoods.refrigeratedOrNot = updatedBakedGoods.refrigeratedOrNot
            return true
        }

        return false
    }

    /**
     * Searches for baked goods by product name.
     *
     * @param searchString The string to search for in the product names.
     * @return The formatted list of matching baked goods.
     */
    fun searchByProductName(searchString: String): String =
        formatListString(
            bakedGoodsList.filter { bakedGoods -> bakedGoods.productName.contains(searchString, ignoreCase = true) }
        )

    /**
     * Searches for ingredients by name.
     *
     * @param searchString The string to search for in the ingredient names.
     * @return The formatted list of matching ingredients.
     */
    fun searchByIngredientName(searchString: String): String =
        formatListString(
            ingredientList.filter { ingredient -> ingredient.ingredientName.contains(searchString, ignoreCase = true) }
        )

    /**
     * Searches for baked goods containing a specific allergen.
     *
     * @param allergen The allergen to search for.
     * @return The formatted list of matching baked goods.
     */
    fun searchBakedGoodsByAllergen(allergen: String): String =
        if (bakedGoodsList.isEmpty()) {
            "No baked goods stored"
        } else {
            val filteredBakedGoods = bakedGoodsList.filter { bakedGood ->
                bakedGood.ingredients.any { it.allergens.contains(allergen) }
            }

            if (filteredBakedGoods.isNotEmpty()) {
                formatListString(filteredBakedGoods)
            } else {
                "No baked goods found with allergen: $allergen"
            }
        }

    /**
     * Lists baked goods by a specific allergen.
     *
     * @param allergen The allergen to filter by.
     * @return The formatted list of matching baked goods.
     */
    fun listBakedGoodsByAllergen(allergen: String): String =
        if (bakedGoodsList.isEmpty()) {
            "No baked goods stored"
        } else {
            formatListString(bakedGoodsList.filter { bakedGood -> bakedGood.ingredients.any { it.allergens.contains(allergen) } })
        }

    /**
     * Lists refrigerated baked goods.
     *
     * @return The formatted list of refrigerated baked goods or a message if none are found.
     */
    fun listRefrigeratedBakedGoods(): String =
        if (bakedGoodsList.isEmpty()) {
            "No refrigerated baked goods found."
        } else {
            formatListString(bakedGoodsList.filter { it.refrigeratedOrNot })
        }

    /**
     * Lists non-refrigerated baked goods.
     *
     * @return The formatted list of non-refrigerated baked goods or a message if none are found.
     */
    fun listNonRefrigeratedBakedGoods(): String =
        if (bakedGoodsList.isEmpty()) {
            "No non-refrigerated baked goods found."
        } else {
            formatListString(bakedGoodsList.filter { it.refrigeratedOrNot })
        }

    /**
     * Lists baked goods within a specified price range.
     *
     * @param minPrice The minimum price of the range.
     * @param maxPrice The maximum price of the range.
     * @return The formatted list of baked goods within the specified price range or a message if none are found.
     */
    fun listBakedGoodsByPriceRange(minPrice: Double, maxPrice: Double): String =
        if (bakedGoodsList.isEmpty()) {
            "No baked goods found in the price range: $minPrice - $maxPrice"
        } else {
            formatListString(bakedGoodsList.filter { it.productPrice in minPrice..maxPrice })
        }

    /**
     * Lists baked goods of a specific category.
     *
     * @param category The category to filter by.
     * @return The formatted list of baked goods in the specified category or a message if none are found.
     */
    fun listBakedGoodsByCategory(category: String): String =
        if (bakedGoodsList.isEmpty()) {
            "No products stored"
        } else {
            val filteredBakedGoods = formatListString(bakedGoodsList.filter { it.productCategory.equals(category, ignoreCase = true) })
            if (filteredBakedGoods.isBlank()) {
                "No products with category: $category"
            } else {
                "${numberOfBakedGoodsByCategory(category)} baked goods with category $category: $filteredBakedGoods"
            }
        }

    /**
     * Lists all baked goods.
     *
     * @return The formatted list of all baked goods or a message if none are found.
     */
    fun listAllBakedGoods(): String =
        if (bakedGoodsList.isEmpty()) {
            "No baked goods stored"
        } else {
            formatListString(bakedGoodsList)
        }

    /**
     * Gets the total number of baked goods.
     *
     * @return The total number of baked goods.
     */
    fun numberOfBakedGoods(): Int {
        return bakedGoodsList.size
    }

    /**
     * Gets the number of baked goods in a specific category.
     *
     * @param category The category to count baked goods for.
     * @return The number of baked goods in the specified category.
     */
    fun numberOfBakedGoodsByCategory(category: String): Int =
        bakedGoodsList.count { it.productCategory.equals(category, ignoreCase = true) }
}

/**
 * Gets the number of refrigerated baked goods in a list.
 *
 * @param bakedGoodsList The list of baked goods to count.
 * @return The number of refrigerated baked goods in the list.
 */
fun numberOfRefrigeratedBakedGoods(bakedGoodsList: List<BakedGoods>): Int {
    return bakedGoodsList.count { it.refrigeratedOrNot }
}

/**
 * Gets the number of non-refrigerated baked goods in a list.
 *
 * @param bakedGoodsList The list of baked goods to count.
 * @return The number of non-refrigerated baked goods in the list.
 */
fun numberOfNonRefrigeratedBakedGoods(bakedGoodsList: List<BakedGoods>): Int {
    return bakedGoodsList.count { !it.refrigeratedOrNot }
}

/**
 * Formats a list of baked goods into a string.
 *
 * @param bakedGoodsToFormat The list of baked goods to format.
 * @return The formatted string representation of the list.
 */
private fun formatListString(bakedGoodsToFormat: List<BakedGoods>): String =
    bakedGoodsToFormat
        .joinToString(separator = "\n") { bakedGoods ->
            "${bakedGoodsToFormat.indexOf(bakedGoods)}: $bakedGoods"
        }
