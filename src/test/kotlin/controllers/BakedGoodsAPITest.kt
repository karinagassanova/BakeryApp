package controllers
import models.BakedGoods
import models.Ingredient
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import persistence.JSONSerializer
import utils.CategoryUtility
import java.io.File
import java.util.*

class BakedGoodsAPITest {

    private var blueberryMuffin: BakedGoods? = null
    private var sourdoughBread: BakedGoods? = null
    private var carrotCake: BakedGoods? = null
    private var cinnamonBuns: BakedGoods? = null
    private var lemonCake: BakedGoods? = null
    private var populatedBakedGoods: BakedGoodsAPI? = BakedGoodsAPI(JSONSerializer(File("bakedgoods.json")))
    private var emptyBakedGoods: BakedGoodsAPI? = BakedGoodsAPI(JSONSerializer(File("bakedgoods.json")))

    @BeforeEach
    fun setup() {

        blueberryMuffin = BakedGoods(1, "Blueberry Muffin", "Fluffy muffin with blueberry filling", 3.50, "Bun", false)
        blueberryMuffin!!.addIngredient(Ingredient(1, "Blueberry Muffin Ingredients", 8.0, "Flour,Sugar,Baking Powder,Salt,Milk,Butter,Eggs,Blueberries", setOf("Gluten","Dairy")))
        sourdoughBread = BakedGoods(2, "Sourdough Bread", "Artisanal sourdough bread", 5.99, "Bread", false)
        sourdoughBread!!.addIngredient(Ingredient(2,"Sourdough Bread Ingredients", 4.0,"Flour,Water,Starter,Salt,Olive oil",setOf("Gluten")))
        carrotCake = BakedGoods(3, "Carrot Cake", "Moist carrot cake with cream cheese frosting", 14.99, "Cake", true)
        carrotCake!!.addIngredient(Ingredient(3,"Carrot Cake Ingredients",12.0,("Flour, Sugar,Baking Powder,Baking Soda,Salr,Cinnamon,Nutmeg,Oil,Eggs,Carrots, Walnuts or Pecans, Cream-cheese frosting"), setOf("Gluten, Nuts","Dairy")))
        cinnamonBuns = BakedGoods(4, "Cinnamon Buns", "Sweet and gooey cinnamon buns", 8.99, "Bun", true)
        cinnamonBuns!!.addIngredient(Ingredient(4,"Cinnamon Bun Ingredients",10.0,"Flour,Sugar,Baking Powder,Baking Soda,Salt,Milk,Butter,Cinnamon,Brown Sugar,Cream-cheese", setOf("Gluten","Dairy")))
        lemonCake = BakedGoods(5, "Lemon Cake", "Zesty lemon-flavored cake", 12.99, "Cake", false)
        lemonCake!!.addIngredient(Ingredient(5,"Lemon Cake Ingredients",10.0,"Flour,Sugar,Baking Powder,Salt,Butter,Eggs,Lemon zest, Lemon juice, Milk,Powdered Sugar", setOf("Gluten","Dairy")))

        populatedBakedGoods!!.add(blueberryMuffin!!)
        populatedBakedGoods!!.add(sourdoughBread!!)
        populatedBakedGoods!!.add(carrotCake!!)
        populatedBakedGoods!!.add(cinnamonBuns!!)
        populatedBakedGoods!!.add(lemonCake!!)
    }

    @AfterEach
    fun tearDown() {
        blueberryMuffin = null
        sourdoughBread = null
        carrotCake = null
        cinnamonBuns = null
        lemonCake = null
    }

    @Nested
    inner class PersistenceTests {
        @Test
        fun `saving and loading an loaded collection in JSON doesn't loose data`() {
            // Storing 3 notes to the notes.json file.
            val storingBakedGoods = BakedGoodsAPI(JSONSerializer(File("bakedgoods.json")))
            storingBakedGoods.add(carrotCake!!)
            storingBakedGoods.add(blueberryMuffin!!)
            storingBakedGoods.add(sourdoughBread!!)
            storingBakedGoods.store()

            val loadedBakedGoods = BakedGoodsAPI(JSONSerializer(File("bakedgoods.json")))
            loadedBakedGoods.load()

            assertEquals(3, storingBakedGoods.numberOfBakedGoods())
            assertEquals(3, loadedBakedGoods.numberOfBakedGoods())
            assertEquals(storingBakedGoods.numberOfBakedGoods(), loadedBakedGoods.numberOfBakedGoods())
            assertEquals(storingBakedGoods.findBakedGoods(0), loadedBakedGoods.findBakedGoods(0))
            assertEquals(storingBakedGoods.findBakedGoods(1), loadedBakedGoods.findBakedGoods(1))
            assertEquals(storingBakedGoods.findBakedGoods(2), loadedBakedGoods.findBakedGoods(2))
        }
    }


    @Nested
    inner class AddBakedGoods {

        @Test
        // Test that adding a baked good to a populated list increases the count and the new baked good is added.
        fun `adding a BakedGood to a populated list adds to ArrayList`() {
            val newBakedGood = BakedGoods(1, "Chocolate Cake", "Delicious chocolate cake", 10.99, "Dessert", true)
            assertEquals(5, populatedBakedGoods!!.numberOfBakedGoods())
            assertTrue(populatedBakedGoods!!.add(newBakedGood))
            assertEquals(6, populatedBakedGoods!!.numberOfBakedGoods())
            assertEquals(
                newBakedGood,
                populatedBakedGoods!!.findBakedGoods(populatedBakedGoods!!.numberOfBakedGoods() - 1)
            )
        }

        @Test
        // Test that adding a baked good to an empty list increases the count and the new baked good is added.
        fun `adding a BakedGood to an empty list adds to ArrayList`() {
            val newBakedGood = BakedGoods(1, "Chocolate Cake", "Delicious chocolate cake", 10.99, "Dessert", true)
            assertEquals(0, emptyBakedGoods!!.numberOfBakedGoods())
            assertTrue(emptyBakedGoods!!.add(newBakedGood))
            assertEquals(1, emptyBakedGoods!!.numberOfBakedGoods())
            assertEquals(newBakedGood, emptyBakedGoods!!.findBakedGoods(emptyBakedGoods!!.numberOfBakedGoods() - 1))


        }
    }
@Nested
inner class SearchMethods {
    @Test
    fun `searchByIngredientName returns baked goods with matching ingredient name`() {
        val searchString = "Vanilla"
        val result = populatedBakedGoods!!.searchByIngredientName(searchString)
        assertFalse(result.contains("no ingredients found"))
    }

    @Test
    fun `searchBakedGoodsByAllergen returns baked goods with matching allergen`() {
        val allergen = "Nuts"
        val result = populatedBakedGoods!!.searchBakedGoodsByAllergen(allergen)
        assertFalse(result.contains("no baked goods found with allergen"))
    }

}
    @Nested
    inner class ListBakedGoods {

        @Test
        fun `listRefrigeratedBakedGoods returns refrigerated baked goods when ArrayList has them`() {
            val refrigeratedBakedGoodsString = populatedBakedGoods!!.listRefrigeratedBakedGoods().lowercase()
            assertFalse(refrigeratedBakedGoodsString.contains("no refrigerated baked goods"))
        }

        @Test
        fun `listNonRefrigeratedBakedGoods returns non-refrigerated baked goods when ArrayList has them`() {
            val nonRefrigeratedBakedGoodsString = populatedBakedGoods!!.listNonRefrigeratedBakedGoods().lowercase()
            assertFalse(nonRefrigeratedBakedGoodsString.contains("no non-refrigerated baked goods"))
        }

        @Test
        fun `listBakedGoodsByPriceRange returns baked goods in the specified price range`() {
            val minPrice = 5.0
            val maxPrice = 15.0
            val result = populatedBakedGoods!!.listBakedGoodsByPriceRange(minPrice, maxPrice)
            assertFalse(result.contains("no baked goods found"))
        }

        @Test
        fun `listBakedGoodsByCategory returns baked goods with the specified category`() {
            val category = "Cakes"
            val result = populatedBakedGoods!!.listBakedGoodsByCategory(category)
            assertFalse(result.contains("no products with category"))
        }

        @Test
        fun `listAllBakedGoods returns all baked goods when ArrayList is not empty`() {
            val result = populatedBakedGoods!!.listAllBakedGoods()
            assertFalse(result.contains("no baked goods stored"))
        }

        @Test
        fun `numberOfBakedGoods returns the correct count of baked goods`() {
            assertEquals(5, populatedBakedGoods!!.numberOfBakedGoods())
        }

        @Test
        fun `searchByProductName returns baked goods with matching product name`() {
            val searchString = "Chocolate"
            val result = populatedBakedGoods!!.searchByProductName(searchString)
            assertFalse(result.contains("no products found"))
        }

        @Test
        fun isValidCategoryTrueWhenCategoryExists() {
            Assertions.assertTrue(CategoryUtility.isValidCategory("Home"))
            Assertions.assertTrue(CategoryUtility.isValidCategory("home"))
            Assertions.assertTrue(CategoryUtility.isValidCategory("COLLEGE"))
        }

        @Test
        fun isValidCategoryFalseWhenCategoryDoesNotExist() {
            Assertions.assertFalse(CategoryUtility.isValidCategory("Hom"))
            Assertions.assertFalse(CategoryUtility.isValidCategory("colllege"))
            Assertions.assertFalse(CategoryUtility.isValidCategory(""))
        }

        @Test
        fun `listBakedGoodsByAllergen returns baked goods with matching allergen`() {
            val allergen = "Dairy"
            val result = populatedBakedGoods!!.listBakedGoodsByAllergen(allergen)
            assertFalse(result.contains("no baked goods found with allergen"))
        }

        @Test
        fun `listBakedGoodsByPrice returns baked goods in the specified price range`() {
            val minPrice = 5.0
            val maxPrice = 15.0
            val result = populatedBakedGoods!!.listBakedGoodsByPriceRange(minPrice, maxPrice)
            assertFalse(result.contains("no baked goods found"))
        }
    }
        @Test
        fun `listRefrigeratedBakedGoods returns refrigerated baked goods when ArrayList has them`() {
            val refrigeratedBakedGoodsString = populatedBakedGoods!!.listRefrigeratedBakedGoods().lowercase()
            assertFalse(refrigeratedBakedGoodsString.contains("no refrigerated baked goods"))
        }
         @Test
         fun `listNonRefrigeratedBakedGoods returns non-refrigerated baked goods when ArrayList has them`() {
            val nonRefrigeratedBakedGoodsString = populatedBakedGoods!!.listNonRefrigeratedBakedGoods().lowercase()
             assertFalse(nonRefrigeratedBakedGoodsString.contains("no non-refrigerated baked goods"))
    }
    }
