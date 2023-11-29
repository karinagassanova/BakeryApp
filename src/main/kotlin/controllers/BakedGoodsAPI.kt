package controllers

import models.BakedGoods
import models.Ingredient
import persistence.Serializer
import utils.Utilities.formatListString
import utils.Utilities.isValidListIndex

class BakedGoodsAPI(serializerType: Serializer) {

    private var serializer: Serializer = serializerType
private var ingredientList = ArrayList<Ingredient>()
    private var bakedGoodsList = ArrayList<BakedGoods>()


    @Throws(Exception::class)
    fun load() {
        bakedGoodsList = serializer.read() as ArrayList<BakedGoods>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(bakedGoodsList)
    }

    fun add(bakedGoods: BakedGoods): Boolean {
        return bakedGoodsList.add(bakedGoods)
    }

    fun deleteBakedGood(indexToDelete: Int): BakedGoods? {
        return if (isValidListIndex(indexToDelete, bakedGoodsList)) {
            bakedGoodsList.removeAt(indexToDelete)
        } else null
    }

    fun findBakedGoods(index: Int): BakedGoods? {
        return if (isValidIndex(index)) {
            bakedGoodsList[index]
        } else null
    }

    fun isValidIndex(index: Int): Boolean {
        return isValidListIndex(index, bakedGoodsList)
    }

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
    // ------------------------
// SEARCHING METHODS
// ------------------------
    fun searchByProductName(searchString: String): String =
        formatListString(
            bakedGoodsList.filter { bakedGoods -> bakedGoods.productName.contains(searchString, ignoreCase = true) }
        )
    fun searchByIngredientName(searchString: String): String =
        formatListString(
            ingredientList.filter { ingredient -> ingredient.ingredientName.contains(searchString, ignoreCase = true) }
        )

    fun searchBakedGoodsByAllergen(allergen: String): String =
        if (bakedGoodsList.isEmpty()) "No baked goods stored"
        else {
            val filteredBakedGoods = bakedGoodsList.filter { bakedGood ->
                bakedGood.ingredients.any { it.allergens.contains(allergen) }
            }

            if (filteredBakedGoods.isNotEmpty()) {
                formatListString(filteredBakedGoods)
            } else {
                "No baked goods found with allergen: $allergen"
            }
        }

    // ---------------------
    // LISTING METHODS FOR BAKEDGOODS ArrayList
    // ---------------------

    fun listBakedGoodsByAllergen(allergen: String): String =
        if (bakedGoodsList.isEmpty()) "No baked goods stored"
        else {
            val filteredBakedGoods = bakedGoodsList.filter { bakedGood ->
                bakedGood.ingredients.any { it.allergens.contains(allergen) }
            }

            if (filteredBakedGoods.isNotEmpty()) {
                formatListString(filteredBakedGoods)
            } else {
                "No baked goods found with allergen: $allergen"
            }
        }

    fun listRefrigeratedBakedGoods(): String {
        val refrigeratedBakedGoods = bakedGoodsList.filter { it.refrigeratedOrNot }
        return if (refrigeratedBakedGoods.isNotEmpty()) {
            formatListString(refrigeratedBakedGoods)
        } else {
            "No refrigerated baked goods found."
        }
    }
    fun listNonRefrigeratedBakedGoods(): String {
        val nonRefrigeratedBakedGoods = bakedGoodsList.filter { !it.refrigeratedOrNot }
        return if (nonRefrigeratedBakedGoods.isNotEmpty()) {
            formatListString(nonRefrigeratedBakedGoods)
        } else {
            "No non-refrigerated baked goods found."
        }
    }

    fun listBakedGoodsByPriceRange(minPrice: Double, maxPrice: Double): String {
        val filteredBakedGoods = bakedGoodsList.filter { it.productPrice in minPrice..maxPrice }
        return if (filteredBakedGoods.isNotEmpty()) {
            formatListString(filteredBakedGoods)
        } else {
            "No baked goods found in the price range: $minPrice - $maxPrice"
        }
    }

    fun listBakedGoodsByCategory(category: String): String =
        if (bakedGoodsList.isEmpty()) "No products stored"
        else {
            val filteredBakedGoods =
                formatListString(bakedGoodsList.filter { it.productCategory.equals(category, ignoreCase = true) })
            if (filteredBakedGoods.isBlank()) "No products with category: $category"
            else "${numberOfBakedGoodsByCategory(category)} baked goods with category $category: $filteredBakedGoods"
        }

    fun listAllBakedGoods(): String =
        if (bakedGoodsList.isEmpty()) "No baked goods stored"
        else formatListString(bakedGoodsList)

    // -------------------------
    // COUNTING METHODS FOR BAKEDGOODS ArrayList
    // --------------------------
    fun numberOfBakedGoods(): Int {
        return bakedGoodsList.size
    }
    fun numberOfBakedGoodsByCategory(category: String): Int =
        bakedGoodsList.count { it.productCategory.equals(category, ignoreCase = true) }
}


fun numberOfRefrigeratedBakedGoods(bakedGoodsList: List<BakedGoods>): Int {
        return bakedGoodsList.count { it.refrigeratedOrNot }
    }
    fun numberOfNonRefrigeratedBakedGoods(bakedGoodsList: List<BakedGoods>): Int {
        return bakedGoodsList.count { !it.refrigeratedOrNot }
    }


    private fun formatListString(bakedGoodsToFormat: List<BakedGoods>): String =
        bakedGoodsToFormat
            .joinToString(separator = "\n") { bakedGoods ->
                "${bakedGoodsToFormat.indexOf(bakedGoods)}: ${bakedGoods.toString()}"
            }



