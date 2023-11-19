package controllers

import models.BakedGoods
import persistence.Serializer
import utils.Utilities.isValidListIndex

class BakedGoodsAPI(serializerType: Serializer) {
    private var serializer: Serializer = serializerType

    private var BakedGoods = ArrayList<BakedGoods>()

    fun add(bakedGoods: BakedGoods): Boolean {
        return BakedGoods.add(bakedGoods)
    }

    fun deleteBakedGood(indexToDelete: Int): BakedGoods? {
        return if (isValidListIndex(indexToDelete, BakedGoods)) {
            BakedGoods.removeAt(indexToDelete)
        } else null
    }

    fun findBakedGoods(index: Int): BakedGoods? {
        return if (isValidIndex(index)) {
            BakedGoods[index]
        } else null
    }

    fun isValidIndex(index: Int): Boolean {
        return isValidListIndex(index, BakedGoods)
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

    fun listBakedGoodsByCategory(category: String): String =
        if (BakedGoods.isEmpty()) "No products stored"
        else {
            val filteredBakedGoods =
                formatListString(BakedGoods.filter { it.productCategory.equals(category, ignoreCase = true) })
            if (filteredBakedGoods.isBlank()) "No products with category: $category"
            else "${numberOfBakedGoodsByCategory(category)} baked goods with category $category: $filteredBakedGoods"
        }

    fun listAllBakedGoods(): String =
        if (BakedGoods.isEmpty()) "No baked goods stored"
        else formatListString(BakedGoods)

    fun numberOfBakedGoods(): Int {
        return BakedGoods.size
    }


    private fun formatListString(bakedGoodsToFormat: List<BakedGoods>): String =
        bakedGoodsToFormat
            .joinToString(separator = "\n") { bakedGoods ->
                "${bakedGoodsToFormat.indexOf(bakedGoods)}: ${bakedGoods.toString()}"
            }
    @Throws(Exception::class)
    fun load() {
        BakedGoods = serializer.read() as ArrayList<BakedGoods>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(BakedGoods)
    }


    fun searchByProductName(searchString: String): String =
        formatListString(
            BakedGoods.filter { BakedGoods -> BakedGoods.productName.contains(searchString, ignoreCase = true)
            }
        )

    fun numberOfBakedGoodsByCategory(category: String): Int =
        BakedGoods.count { it.productCategory.equals(category, ignoreCase = true) }
}