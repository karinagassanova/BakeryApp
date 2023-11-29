/**
 * A utility class providing common functions for validation and list indexing.
 */
package utils
import models.Ingredient

object Utilities {

    /**
     * Checks if a number is within a specified range.
     *
     * @param numberToCheck The number to check.
     * @param min The minimum value of the range (inclusive).
     * @param max The maximum value of the range (inclusive).
     * @return true if the number is within the range, false otherwise.
     */
    @JvmStatic
    fun validRange(numberToCheck: Int, min: Int, max: Int): Boolean {
        return numberToCheck in min..max
    }

    /**
     * Checks if an index is within the bounds of a list.
     *
     * @param index The index to check.
     * @param list The list to validate the index against.
     * @return true if the index is within the bounds of the list, false otherwise.
     */
    @JvmStatic
    fun isValidListIndex(index: Int, list: List<Any>): Boolean {
        return (index >= 0 && index < list.size)
    }

    @JvmStatic
    fun formatListString(notesToFormat: List<Ingredient>): String =
        notesToFormat
            .joinToString(separator = "\n") { bakedGoods -> "$bakedGoods" }

    @JvmStatic
    fun formatSetString(itemsToFormat: MutableList<Ingredient>): String =
        itemsToFormat
            .joinToString(separator = "\n") { ingredients -> "\t$ingredients" }
}
