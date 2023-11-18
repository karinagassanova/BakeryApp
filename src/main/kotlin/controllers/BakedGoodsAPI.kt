package controllers

import models.bakedGoods
import utils.Utilities
import utils.Utilities.isValidListIndex

class BakedGoodsAPI {
    private var bakedGoodsList = ArrayList<bakedGoods>()

    fun add(bakedGoods: bakedGoods): Boolean {
        return bakedGoodsList.add(bakedGoods)
    }

    fun deleteBakedGood(indexToDelete: Int): bakedGoods? {
        return if (isValidListIndex(indexToDelete, bakedGoodsList)) {
            bakedGoodsList.removeAt(indexToDelete)
        } else null
    }

    fun findBakedGoods(index: Int): bakedGoods? {
        return if (isValidIndex(index)) {
            bakedGoodsList[index]
        } else null
    }

    fun isValidIndex(index: Int): Boolean {
        return isValidListIndex(index, bakedGoodsList)
    }

    fun updateBakedGood(indexToUpdate: Int, updatedBakedGoods: bakedGoods?): Boolean {
        val foundBakedGoods = findBakedGoods(indexToUpdate)

        if ((foundBakedGoods != null) && (updatedBakedGoods != null)) {
            foundBakedGoods.productId = updatedBakedGoods.productId
            foundBakedGoods.productName = updatedBakedGoods.productName
            foundBakedGoods.productDesc = updatedBakedGoods.productDesc
            foundBakedGoods.productPrice = updatedBakedGoods.productPrice
            foundBakedGoods.category = updatedBakedGoods.category
            foundBakedGoods.refrigeratedOrNot = updatedBakedGoods.refrigeratedOrNot
            return true
        }

        return false
    }

    private fun formatListString(bakedGoodsToFormat: List<bakedGoods>): String =
        bakedGoodsToFormat
            .joinToString(separator = "\n") { bakedGoods ->
                bakedGoodsList.indexOf(bakedGoods).toString() + ": " + bakedGoods.toString()
            }


}
