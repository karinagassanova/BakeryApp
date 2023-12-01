import controllers.BakedGoodsAPI
import models.BakedGoods
import models.Ingredient
import mu.KotlinLogging
import persistence.JSONSerializer
import utils.ScannerInput.readNextChar
import utils.ScannerInput.readNextDouble
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.io.File
import kotlin.system.exitProcess

private val logger = KotlinLogging.logger {}
private val bakedGoodsAPI = BakedGoodsAPI(JSONSerializer(File("bakedgoods.json")))

/**
 * The main entry point for the Bakery Application. Initializes the application and runs the menu.
 */
fun main() {
    logger.info { "Starting the Bakery Application" }
    runMenu()
    logger.info { "Exiting the Bakery Application" }
}

/**
 * Runs the main menu of the Bakery Application in a loop until the user chooses to exit.
 */
fun runMenu() {
    do {
        when (val option = mainMenu()) {
            1 -> addBakedGood()
            2 -> deleteBakedGood()
            3 -> updateBakedGood()
            4 -> listBakedGoods()
            5 -> addIngredientToBakedGood()
            6 -> updateIngredientQuantityInBakedGood()
            7 -> deleteIngredientFromBakedGood()
            8 -> markIngredientAllergens()
            9 -> searchBakedGoods()
            10 -> searchIngredients()
            11 -> searchBakedGoodsByAllergen()
            12 -> load()
            13 -> save()
            0 -> exitApp()
            else -> println("Invalid option entered: $option")
        }
    } while (true)
}

/**
 * Displays the main menu options and returns the user's selected option.
 *
 * @return The selected option.
 */
fun mainMenu(): Int {
    // ANSI escape codes for text colors
    val titleColor = "\u001B[34m" // blue
    val optionColor = "\u001B[35m" // purple
    val exitColor = "\u001B[31m" // red
    return readNextInt(
        """  
          ${colorText("\uD83E\uDDC1 Bakery App \uD83C\uDF70",titleColor)}
         
        | 1) ${colorText("\u2795Add a Baked Good",optionColor)}
        | 2) ${colorText("\u274EDelete a Baked Good",optionColor)}
        | 3) ${colorText("Update a Baked Good",optionColor)}
        | 4) ${colorText("\uD83D\uDCDCList Baked Goods",optionColor)}
        | 5) ${colorText("\u2705Add Ingredient to a Baked Good",optionColor)}
        | 6) ${colorText("Update Ingredient Quantity in a Baked Good",optionColor)}
        | 7) ${colorText("\uD83D\uDEABDelete an Ingredient",optionColor)}
        | 8) ${colorText("Mark Ingredient Allergens",optionColor)}
        | 9) ${colorText("\uD83D\uDD0DSearch Baked Goods",optionColor)}
        | 10) ${colorText("\uD83D\uDD0DSearch Ingredients",optionColor)}
        | 11) ${colorText("\uD83D\uDD0DSearch Baked Goods by Allergens",optionColor)}
        | 12) ${colorText("\uD83D\uDCE5Load Baked Goods",optionColor)}
        | 13) ${colorText("\uD83D\uDCBESave Baked Goods",optionColor)}
        | 0) ${colorText("\uD83D\uDEAAExit",exitColor)}
        
         > ==>> """.trimMargin(">")
    )
}

/**
 * Applies color to a given text using ANSI escape codes
 * The text parameter is described as the text to be colorized.
 * The reset color ensures that the color change is limited to the specified text
 * The color parameter is described as the ANSI escape code for setting the text color
 * The return value is defined as a colorized text string
 */
fun colorText(text: String, color: String): String {
    val resetColor = "\u001B[0m"
    return "$color$text$resetColor"
}

/**
 * Displays a list of functions to list baked goods.
 */
fun listBakedGoods() {
    val titleColor = "\u001B[34m" // blue
    val optionColor = "\u001B[35m" // purple
    if (bakedGoodsAPI.numberOfBakedGoods() > 0) {
        val option = readNextInt(
            """
                >        ${colorText("\uD83C\uDF70LIST OF BAKED GOODS\uD83C\uDF81",titleColor)}
        
                  > |   1) ${colorText("List ALL baked goods\uD83D\uDCCB",optionColor)}        |
                  > |   2) ${colorText("List Baked Goods by Category",optionColor)}            |
                  > |   3) ${colorText("List Baked Goods by Price\uD83D\uDCB0",optionColor)}   |
                  > |   4) ${colorText("List Refrigerated Baked Goods",optionColor)}           |
                  > |   5) ${colorText("List Baked Goods by Allergen",optionColor)}            |

 
         > ==>> """.trimMargin(">")
        )

        when (option) {
            1 -> listAllBakedGoods()
            2 -> listBakedGoodsByCategory()
            3 -> listBakedGoodsByPrice()
            4 -> listRefrigeratedBakedGoods()
            5 -> listBakedGoodsByAllergen()
            else -> println("Invalid option entered: $option")
        }
    } else {
        println("Option Invalid - No baked goods stored")
    }
}

/**
 * Displays a list of baked goods based on the specified category.
 */
fun listBakedGoodsByCategory() {
    val category = readNextLine("Enter the category to filter by: ")
    val result = bakedGoodsAPI.listBakedGoodsByCategory(category)

    if (result.isNotEmpty()) {
        println(result)
    } else {
        logger.info { "No baked goods found in category: $category" }
    }
}

/**
 * Displays a list of baked goods based on the specified allergen.
 */
fun listBakedGoodsByAllergen() {
    val allergen = readNextLine("Enter the allergen to filter by: ")
    val result = bakedGoodsAPI.listBakedGoodsByAllergen(allergen)

    if (result.isNotEmpty()) {
        println(result)
    } else {
        logger.info { "No baked goods found with allergen: $allergen" }
    }
}

/**
 * Displays a list of refrigerated baked goods.
 */
fun listRefrigeratedBakedGoods() {
    println(bakedGoodsAPI.listRefrigeratedBakedGoods())
}

/**
 * Displays a list of baked goods within the specified price range.
 */
fun listBakedGoodsByPrice() {
    val minPrice = readNextDouble("Enter the minimum price: ")
    val maxPrice = readNextDouble("Enter the maximum price: ")

    val result = bakedGoodsAPI.listBakedGoodsByPriceRange(minPrice, maxPrice)

    if (result.isNotEmpty()) {
        println(result)
    } else {
        logger.info { "No baked goods found in the price range: $minPrice - $maxPrice" }
    }
}

/**
 * Adds a new baked good to the system.
 */
fun addBakedGood() {
    val productId = readNextInt("Enter the product ID: ")
    val productName = readNextLine("Enter the name of the product: ")
    val productDesc = readNextLine("Enter the description of the product: ")
    val productPrice = readNextInt("Enter the price of the product: ")
    val category = readNextLine("Enter the category of the product: ")
    val refrigeratedOrNot = readNextLine("Enter if the baked good is refrigerated or not (yes, no): ")

    val isAdded = bakedGoodsAPI.add(
        BakedGoods(
            productId,
            productName,
            productDesc,
            productPrice.toDouble(),
            category,
            refrigeratedOrNot.equals("yes", ignoreCase = true)
        )
    )

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}

/**
 * Searches for baked goods with a specific allergen.
 */
fun searchBakedGoodsByAllergen() {
    val allergen = readNextLine("Enter the allergen to search by: ")
    val result = bakedGoodsAPI.searchBakedGoodsByAllergen(allergen)

    if (result.isNotEmpty()) {
        println(result)
    } else {
        logger.info { "No baked goods found with allergen: $allergen" }
    }
}

/**
 * Searches for baked goods with a specific name.
 */
fun searchBakedGoods() {
    val searchByName = readNextLine("Enter the title to search by: ")
    val searchResults = bakedGoodsAPI.searchByProductName(searchByName)
    if (searchResults.isEmpty()) {
        logger.info { "No products found" }
    } else {
        logger.info { searchResults }
    }
}

/**
 * Searches for ingredients with a specific name.
 */
fun searchIngredients() {
    val searchByName = readNextLine("Enter the name to search by: ")
    val searchResults = bakedGoodsAPI.searchByIngredientName(searchByName)

    if (searchResults.isEmpty()) {
        logger.info { "No ingredients found" }
    } else {
        logger.info { searchResults }
    }
}

fun listAllBakedGoods() {
    println(bakedGoodsAPI.listAllBakedGoods())
}

/**
 * Updates the details of an existing baked good.
 */
fun updateBakedGood() {
    listAllBakedGoods()
    if (bakedGoodsAPI.numberOfBakedGoods() > 0) {
        val indexToUpdate = readNextInt("Enter the number of the baked good to update: ")
        if (bakedGoodsAPI.isValidIndex(indexToUpdate)) {
            val productId = readNextInt("Enter the product ID: ")
            val productName = readNextLine("Enter the name of the product: ")
            val productDesc = readNextLine("Enter the description of the product: ")
            val productPrice = readNextInt("Enter the price of the product: ")
            val category = readNextLine("Enter the category of the product (cake, bun, bread) : ")
            val refrigeratedOrNot = readNextLine("Enter if the baked good is refrigerated or not (yes, no): ")

            val updatedBakedGoods = BakedGoods(
                productId,
                productName,
                productDesc,
                productPrice.toDouble(),
                category,
                refrigeratedOrNot.equals("yes", ignoreCase = true)
            )

            if (bakedGoodsAPI.updateBakedGood(indexToUpdate, updatedBakedGoods)) {
                logger.info { "Update Successful" }
            } else {
                logger.info { "Update Failed" }
            }
        } else {
            logger.info { "There are no baked goods for this index number" }
        }
    }
}

/**
 * Deletes an existing baked good.
 */
fun deleteBakedGood() {
    logger.info { "deleteBakedGood() function invoked" }
    listAllBakedGoods()
    if (bakedGoodsAPI.numberOfBakedGoods() > 0) {
        val indexToDelete = readNextInt("Enter the index of the baked good to delete: ")
        val bakedGoodToDelete = bakedGoodsAPI.deleteBakedGood(indexToDelete)
        if (bakedGoodToDelete != null) {
            logger.info { "Delete Successful! Deleted baked good: ${bakedGoodToDelete.productName}" }
        } else {
            logger.warn { "Delete NOT Successful" }
        }
    }
}

/**
 * Adds a new ingredient to an existing baked good.
 */

private fun addIngredientToBakedGood() {
    val bakedGood: BakedGoods? = askUserToChooseBakedGood()
    if (bakedGood != null) {
        val ingredientId = readNextInt("\t Enter the ingredient ID: ")
        val ingredientName = readNextLine("\t Enter the ingredient name: ")
        val ingredientQuantity = readNextDouble("\t Enter the quantity: ")
        val ingredientDescription = readNextLine("\t Enter the ingredient description: ")
        val allergens = readNextLine("\t Enter the allergens (comma-separated): ").split(",").map { it.trim() }.toSet()

        val ingredient = Ingredient(
            ingredientId = ingredientId,
            ingredientName = ingredientName,
            ingredientQuantity = ingredientQuantity,
            ingredientDescription = ingredientDescription,
            allergens = allergens
        )

        if (bakedGood.addIngredient(ingredient)) {
            println("Add Successful!")
        } else {
            println("Add NOT Successful")
        }
    }
}

/**
 * Marks allergens for an ingredient in an existing baked good.
 */
fun markIngredientAllergens() {
    val bakedGood: BakedGoods? = askUserToChooseBakedGood()
// If the user chooses a baked good, the user is asked to choose an ingredient from the selected baked good
    if (bakedGood != null) {
        val ingredient: Ingredient? = askUserToChooseIngredient(bakedGood)

        if (ingredient != null) {
            var changeAllergenStatus = 'X'
            // Checks if the chosen ingredient has allergens
            if (ingredient.allergens.isNotEmpty()) {
                changeAllergenStatus =
                    readNextChar("The ingredient currently has allergens. Do you want to mark it as allergen-free? (Y/N)")
                // If the user agrees, set allergens to an empty set
                if ((changeAllergenStatus == 'Y') || (changeAllergenStatus == 'y')) {
                    ingredient.allergens = emptySet()
                }
            } else {
                // If the ingredient has no allergens, prompt the user to mark it with allergens
                changeAllergenStatus =
                    readNextChar("The ingredient currently has no allergens. Do you want to mark it with allergens? (Y/N)")
                // If the user agrees, set allergens to a set containing example allergens
                if ((changeAllergenStatus == 'Y') || (changeAllergenStatus == 'y')) {
                    ingredient.allergens = setOf("Gluten", "Dairy")
                    println("Allergens set for ${ingredient.ingredientName}: ${ingredient.allergens}.")
                } else {
                    println("No changes made to allergens.")
                }
            }
        }
    }
}

/**
 * Updates the quantity of an ingredient in an existing baked good.
 */
private fun updateIngredientQuantityInBakedGood() {
    val bakedGood = askUserToChooseBakedGood()
    val ingredient = bakedGood?.let { askUserToChooseIngredient(it) }

    if (bakedGood != null && ingredient != null) {
        val newQuantity = readNextDouble("Enter new quantity: ")

        if (bakedGood.updateIngredient(
                ingredient.ingredientId,
                Ingredient(
                        ingredient.ingredientId,
                        ingredient.ingredientName,
                        newQuantity,
                        ingredient.ingredientDescription,
                        ingredient.allergens
                    )
            )
        ) {
            println("Ingredient quantity updated successfully")
        } else {
            println("Failed to update ingredient quantity")
        }
    } else {
        println("Invalid Baked Good or Ingredient")
    }
}

/**
 * Deletes an ingredient from an existing baked good.
 */
fun deleteIngredientFromBakedGood() {
    val bakedGood: BakedGoods? = askUserToChooseBakedGood()
    if (bakedGood != null) {
        val ingredient: Ingredient? = askUserToChooseIngredient(bakedGood)
        if (ingredient != null) {
            val isDeleted = bakedGood.deleteIngredient(ingredient.ingredientId)
            if (isDeleted) {
                println("Delete Successful!")
            } else {
                println("Delete NOT Successful")
            }
        }
    }
}

/**
 * Asks the user to choose a baked good from the list.
 *
 * @return The selected baked good or null if not found.
 */
private fun askUserToChooseBakedGood(): BakedGoods? {
    listAllBakedGoods()
    // Checks if there are any baked goods available
    if (bakedGoodsAPI.numberOfBakedGoods() > 0) {
        val productId = readNextInt("\nEnter the product ID of the baked good: ")
        val bakedGood = bakedGoodsAPI.findBakedGoods(productId)

        if (bakedGood != null) {
            return bakedGood
        } else {
            println("Baked good with ID $productId not found.")
        }
    } else {
        println("No baked goods available.")
    }

    return null
}

/**
 * Asks the user to choose an ingredient from the list of ingredients in a baked good.
 *
 * @param bakedGood The selected baked good.
 * @return The selected ingredient or null if not found.
 */
private fun askUserToChooseIngredient(bakedGood: BakedGoods): Ingredient? {
    // Checks if the baked good has any ingredients
    if (bakedGood.numberOfIngredients() > 0) {
        println(bakedGood.listIngredients())
        val ingredientId = readNextInt("\nEnter the ID of the ingredient: ")
        val ingredient = bakedGood.findIngredient(ingredientId)

        if (ingredient != null) {
            return ingredient
        } else {
            println("Ingredient with ID $ingredientId not found.")
        }
    } else {
        println("No ingredients available for the chosen baked good.")
    }

    return null
}

/**
 * Exits the Bakery Application.
 */
fun exitApp() {
    println("exitApp() function invoked")
    exitProcess(0)
}

/**
 * Saves the current state of baked goods to a file.
 */
fun save() {
    try {
        bakedGoodsAPI.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

/**
 * Loads baked goods from a file.
 */
fun load() {
    try {
        bakedGoodsAPI.load()
        println("Products loaded successfully.")
    } catch (e: Exception) {
        println("Error reading from file: $e")
    }
}
