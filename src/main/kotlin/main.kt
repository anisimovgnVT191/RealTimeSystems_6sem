import kotlin.reflect.full.memberProperties

fun main(args: Array<String>) {
    val sat = RinexParser.parse("C:\\Users\\tewat\\Desktop\\godz2460.07n")
    println(sat)
    for(prop in Satellite::class.memberProperties){
        println("${prop.name} = ${prop.get(sat)}")
    }
}
