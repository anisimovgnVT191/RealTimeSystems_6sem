import java.io.File
import java.lang.IllegalArgumentException
import kotlin.random.Random
import kotlin.reflect.full.memberProperties

object RinexParser {
    fun parse(
        filePath: String,
        satInfo: String): Satellite{
        val rinexFile = File(filePath)
        if(!rinexFile.exists()) throw IllegalArgumentException("File with $filePath name does not exist")
        val fileLines = rinexFile.readLines().skipHeader()
        val satelliteBody = fileLines.dropWhile { !it.contains(satInfo) }.apply {
            if(isEmpty()) throw IllegalArgumentException("No satellite found")
        } readSatelliteBody(0)
        return Satellite.fromStringRepresentation(satelliteBody)
    }
    private fun List<String>.skipHeader(): List<String>{
        return this
            .dropWhile { !it.contains("END OF HEADER") }
            .drop(1)
            .filter { it.isNotBlank() && it.isNotEmpty() }
    }

    private infix fun List<String>.readSatelliteBody(number: Int):List<String>{
        return this.subList(number*8, number*8+8)
    }
}