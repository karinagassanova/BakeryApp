import controllers.BakedGoodsAPI
import models.BakedGoods
import mu.KotlinLogging
import persistence.JSONSerializer
import utils.ScannerInput.readNextDouble
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import utils.ScannerInput.readNextChar
import java.lang.System.exit
import java.io.File
import models.Ingredient

private val logger = KotlinLogging.logger {}
private val bakedGoodsAPI = BakedGoodsAPI(JSONSerializer(File("bakedgoods.json")))

fun main(args: Array<String>) {
    logger.info { "Starting the Bakery Application" }
    runMenu()
    logger.info { "Exiting the Bakery Application" }
}

fun runMenu() {
    do {
        val option = mainMenu()
        when (option) {
            1 -> addBakedGood()
            2 -> deleteBakedGood()
            3 -> updateBakedGood()
            4 -> listBakedGoods()
            5 -> load()
            6 -> save()
            0 -> exitApp()
            else -> System.out.println("Invalid option entered: $option")
        }
    } while (true)
}

fun mainMenu(): Int {
    return readNextInt(
        """  
          ("Bakery App")
        
              > -----------------------------------------------------------
         ("Bakery MENU")
         
        | 1) ("Add a Baked Good")
        > 2) ("Delete a Baked Good")
        > 3) ("Update a Baked Good")
        > 4) ("List Baked Goods")
        > 5) ("Load Baked Goods")
        > 6) ("Save Baked Goods")
        | 0) ("Exit")
        
         > ==>> """.trimMargin(">")
    )
}

fun listBakedGoods() {
    if (bakedGoodsAPI.numberOfBakedGoods() > 0) {
        val option = readNextInt(
            """
                >        ( "LIST OF BAKED GOODS")
        
                  > |   1) ("List ALL baked goods")           |
                  > |   2) ("List Baked Goods by Category")   |
                  > |   3) ("List Baked Goods by Price")      |
                  > |   4) ("List Refrigerated Baked Goods")  |

 
         > ==>> """.trimMargin(">"))

        when (option) {
            1 -> listAllBakedGoods()
            2 -> listBakedGoodsByCategory()
            3 -> listBakedGoodsByPrice()
            4 -> listRefrigeratedBakedGoods()
            else -> println("Invalid option entered: $option")
        }
    } else {
        println("Option Invalid - No baked goods stored")
    }
}

fun listBakedGoodsByCategory() {
    val category = readNextLine("Enter the category to filter by: ")
    val result = bakedGoodsAPI.listBakedGoodsByCategory(category)

    if (result.isNotEmpty()) {
        println(result)
    } else {
        logger.info { "No baked goods found in category: $category" }
    }
}

fun listRefrigeratedBakedGoods() {
    println(bakedGoodsAPI.listRefrigeratedBakedGoods())
}
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
fun addBakedGood() {
    val productId = readNextInt("Enter the product ID: ")
    val productName = readNextLine("Enter the name of the product: ")
    val productDesc = readNextLine("Enter the description of the product: ")
    val productPrice = readNextInt("Enter the price of the product: ")
    val category = readNextLine("Enter the category of the product: ")
    val refrigeratedOrNot = readNextLine("Enter if the baked good is refrigerated or not (yes, no): ")

    val isAdded = bakedGoodsAPI.add(BakedGoods(
        productId,
        productName,
        productDesc,
        productPrice.toDouble(),
        category,
        refrigeratedOrNot.equals("yes", ignoreCase = true)
    ))

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}

fun searchBakedGoods() {
    val searchByName = readNextLine("Enter the title to search by: ")
    val searchResults = bakedGoodsAPI.searchByProductName(searchByName)
    if (searchResults.isEmpty()) {
        logger.info { "No products found" }
    } else {
        logger.info { searchResults }
    }
}
fun listAllBakedGoods() {
    println(bakedGoodsAPI.listAllBakedGoods())
}

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

/*
INGREDIENTS
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
fun markIngredientAllergens() {
    val bakedGood: BakedGoods? = askUserToChooseBakedGood()
    if (bakedGood != null) {
        val ingredient: Ingredient? = askUserToChooseIngredient(bakedGood)
        if (ingredient != null) {
            var changeAllergenStatus = 'X'
            if (ingredient.allergens.isNotEmpty()) {
                changeAllergenStatus = readNextChar("This ingredient contains allergens...do you want to remove them?")
                if (changeAllergenStatus.equals('Y', ignoreCase = true)) {
                    ingredient.allergens = emptySet()
                    println("Allergens removed.")
                }
            } else {
                changeAllergenStatus = readNextChar("This ingredient is currently allergen-free...do you want to add allergens?")
                if (changeAllergenStatus.equals('Y', ignoreCase = true)) {
                    val newAllergens = readNextLine("Enter allergens (comma-separated): ")
                        .split(",").map { it.trim() }.toSet()
                    ingredient.allergens = newAllergens
                    println("Allergens added.")
                }
            }
        }
    }
}

private fun updateIngredientQuantityInBakedGood() {
    val bakedGood = askUserToChooseBakedGood()
    val ingredient = bakedGood?.let { askUserToChooseIngredient(it) }

    if (bakedGood != null && ingredient != null) {
        val newQuantity = readNextDouble("Enter new quantity: ")

        if (bakedGood.updateIngredient(ingredient.ingredientId,
                Ingredient(ingredient.ingredientId, ingredient.ingredientName, newQuantity,
                    ingredient.ingredientDescription, ingredient.allergens))) {
            println("Ingredient quantity updated successfully")
        } else {
            println("Failed to update ingredient quantity")
        }
    } else {
        println("Invalid Baked Good or Ingredient")
    }
}

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

fun exitApp(){
    println("exitApp() function invoked")
    exit(0)
}
fun save() {
    try {
        bakedGoodsAPI.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun load() {
    try {
        bakedGoodsAPI.load()
        println("Products loaded successfully.")
    } catch (e: Exception) {
        println("Error reading from file: $e")
    }
}
 /*
 HELPER FUNCTIONS
  */
 private fun askUserToChooseBakedGood(): BakedGoods? {
     listAllBakedGoods()

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

private fun askUserToChooseIngredient(bakedGood: BakedGoods): Ingredient? {
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

// ---------------------
//INGREDIENT REPORTS MENU
//----------------------
