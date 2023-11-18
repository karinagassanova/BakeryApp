package controllers
import models.BakedGoods
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import persistence.YamlSerializer
import java.io.File
import java.util.*

class BakedGoodsAPITest {

    private var blueberryMuffin: BakedGoods? = null
    private var sourdoughBread: BakedGoods? = null
    private var carrotCake: BakedGoods? = null
    private var cinnamonBuns: BakedGoods? = null
    private var lemonCake: BakedGoods? = null
    private var populatedNotes: BakedGoodsAPI? = BakedGoodsAPI(YamlSerializer(File("bakedgoods.yaml")))
    private var emptyNotes: BakedGoodsAPI? = BakedGoodsAPI(YamlSerializer(File("bakedgoods.yaml")))

    @BeforeEach
    fun setup() {

        blueberryMuffin = BakedGoods(1, "Blueberry Muffin", "Fluffy muffin with blueberry filling", 3.50, "Bun", false)
        sourdoughBread = BakedGoods(2, "Sourdough Bread", "Artisanal sourdough bread", 5.99, "Bread", false)
        carrotCake = BakedGoods(3, "Carrot Cake", "Moist carrot cake with cream cheese frosting", 14.99, "Cake", true)
        cinnamonBuns = BakedGoods(4, "Cinnamon Buns", "Sweet and gooey cinnamon buns", 8.99, "Bun", true)
        lemonCake = BakedGoods(5, "Lemon Cake", "Zesty lemon-flavored cake", 12.99, "Cake", false)

        populatedNotes!!.add(blueberryMuffin!!)
        populatedNotes!!.add(sourdoughBread!!)
        populatedNotes!!.add(carrotCake!!)
        populatedNotes!!.add(cinnamonBuns!!)
        populatedNotes!!.add(lemonCake!!)
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
        fun `saving and loading an loaded collection in YAML doesn't loose data`() {
            // Storing 3 notes to the notes.YAML file.
            val storingBakedGoods = BakedGoodsAPI(YamlSerializer(File("bakedgoods.yaml")))
            storingBakedGoods.add(blueberryMuffin!!)
            storingBakedGoods.add(carrotCake!!)
            storingBakedGoods.add(sourdoughBread!!)
            storingBakedGoods.store()

            val loadedBakedGoods = BakedGoodsAPI(YamlSerializer(File("bakedgoods.yaml")))
            loadedBakedGoods.load()

            assertEquals(3, storingBakedGoods.numberOfBakedGoods())
            assertEquals(3, loadedBakedGoods.numberOfBakedGoods())
            assertEquals(storingBakedGoods.numberOfBakedGoods(), loadedBakedGoods.numberOfBakedGoods())
            assertEquals(storingBakedGoods.findBakedGoods(0), loadedBakedGoods.findBakedGoods(0))
            assertEquals(storingBakedGoods.findBakedGoods(1), loadedBakedGoods.findBakedGoods(1))
            assertEquals(storingBakedGoods.findBakedGoods(2), loadedBakedGoods.findBakedGoods(2))
        }

    }
}