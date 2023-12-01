package persistence

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver
import models.BakedGoods
import models.Ingredient
import java.io.File
import java.io.FileReader
import java.io.FileWriter

/**
 * JSONSerializer class implements the Serializer interface for reading and writing objects to JSON files.
 *
 * @param file The File object representing the JSON file for serialization.
 */
class JSONSerializer(private val file: File) : Serializer {

    /**
     * Reads and deserializes the contents of the JSON file.
     *
     * @return The deserialized object read from the JSON file.
     * @throws Exception if there is an error during the reading or deserialization process.
     */
    @Throws(Exception::class)
    override fun read(): Any {
        val xStream = XStream(JettisonMappedXmlDriver())
        xStream.allowTypes(arrayOf(BakedGoods::class.java, Ingredient::class.java))
        val inputStream = xStream.createObjectInputStream(FileReader(file))
        val obj = inputStream.readObject() as Any
        inputStream.close()
        return obj
    }

    /**
     * Writes and serializes the given object to the JSON file.
     *
     * @param obj The object to be serialized and written to the JSON file.
     * @throws Exception if there is an error during the writing or serialization process.
     */
    @Throws(Exception::class)
    override fun write(obj: Any?) {
        val xStream = XStream(JettisonMappedXmlDriver())
        val outputStream = xStream.createObjectOutputStream(FileWriter(file))
        outputStream.writeObject(obj)
        outputStream.close()
    }
}
