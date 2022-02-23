import kotlin.reflect.full.memberProperties

fun main(args: Array<String>) {
    val sat = RinexParser.parse(
        filePath = "C:\\Users\\tewat\\Desktop\\godz2460.07n",
        satInfo = " 4 07  9  3  2  0  0.0")
    try{
        println("Идентификатор спутника: ${sat.satNum}")
        println("Данные со спутника:")
        println(sat)
        for (prop in Satellite::class.memberProperties) {
            println("${prop.name} = ${prop.get(sat)}")
        }
    }catch (e: Exception){
        println("Время на которое вычисляются параметры движения навигационного спутника: ${sat.epoch}")
    }
    finally {
        val alg = MainAlgorithm(sat, sat.epoch).evaluate()
        println("Резулльтат работы программы:")
        println(alg)
    }
}
