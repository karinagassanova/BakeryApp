package persistence

import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.lang.Exception


class YamlSerializer(private val file: File) : Serializer {

    @Throws(Exception::class)
    override fun read(): Any {
        val yaml = Yaml()
        val reader = FileReader(file)
        return try {
            yaml.loadAs(reader, Any::class.java)
        } finally {
            reader.close()
        }
    }

    @Throws(Exception::class)
    override fun write(obj: Any?) {
        val yaml = Yaml(DumperOptions())
        val writer = FileWriter(file)
        try {
            yaml.dump(obj, writer)
        } finally {
            writer.close()
        }
    }
}
