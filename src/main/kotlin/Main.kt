import controllers.BakedGoodsAPI
import models.BakedGoods
import mu.KotlinLogging
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.lang.System.exit

private val logger = KotlinLogging.logger {}
private val bakedGoodsAPI = BakedGoodsAPI()  // Declare at a higher level

fun main(args: Array<String>) {
    runMenu()
}

fun runMenu() {
    do {
        val option = mainMenu()
        when (option) {
            1 -> addBakedGood()
            2 -> deleteBakedGood()
            3 -> updateBakedGood()
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
        | 0) ("Exit")
        
         > ==>> """.trimMargin(">")
    )
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
            val category = readNextLine("Enter the category of the product: ")
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
        // only ask the user to choose the baked good to delete if baked goods exist
        val indexToDelete = readNextInt("Enter the index of the baked good to delete: ")
        // pass the index of the baked good to BakedGoodsAPI for deleting and check for success.
        val bakedGoodToDelete = bakedGoodsAPI.deleteBakedGood(indexToDelete)
        if (bakedGoodToDelete != null) {
            logger.info { "Delete Successful! Deleted baked good: ${bakedGoodToDelete.productName}" }
        } else {
            logger.warn { "Delete NOT Successful" }
        }
    }
}

fun exitApp(){
    println("exitApp() function invoked")
    exit(0)
}
