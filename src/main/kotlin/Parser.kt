import java.io.File
import java.lang.IllegalArgumentException
import kotlin.random.Random
import kotlin.reflect.full.memberProperties

object RinexParser {
    fun parse(filePath: String): Satellite{
        val rinexFile = File(filePath)
        if(!rinexFile.exists()) throw IllegalArgumentException("File with this name does not exist")
        val fileLines = rinexFile.readLines().skipHeader()
        val numberOfSatellite = fileLines.size / 8
        val randomSatelliteNumber = Random.nextInt(numberOfSatellite)
        val randomSatelliteBody = fileLines.readSatelliteBody(randomSatelliteNumber)
        return Satellite.fromStringRepresentation(randomSatelliteBody)


    }
    private fun List<String>.skipHeader(): List<String>{
        return this
            .dropWhile { !it.contains("END OF HEADER") }
            .drop(1)
            .filter { it.isNotBlank() && it.isNotEmpty() }
    }

    private fun List<String>.readSatelliteBody(number: Int):List<String>{
        return this.subList(number*8, number*8+8)
    }
}