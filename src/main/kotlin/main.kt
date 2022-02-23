import kotlin.reflect.full.memberProperties

fun main(args: Array<String>) {
    print("Введите номер спутника и время для вычисления: ")
    val satInfo = readLine()?:throw IllegalStateException("")
    val sat = RinexParser.parse(
        filePath = "C:\\Users\\tewat\\Desktop\\godz2460.07n",
        satInfo = satInfo)
    try{
        println("Идентификатор спутника: ${sat.satNum}")
        println("Данные со спутника:")
        for (prop in Satellite::class.memberProperties) {
            println("${prop.name} = ${prop.get(sat)}")
        }
    }catch (e: Exception){
        println("Время на которое вычисляются параметры движения навигационного спутника: ${sat.epoch}")
    }
    finally {
        val alg = MainAlgorithm(sat, sat.epoch).evaluate()
        println("Резулльтат работы программы:")
        with(alg){
            println("""Координаты
                |X = ${X_svk / 1000}
                |Y = ${Y_svk / 1000}
                |Z = ${Z_svk / 1000}
                |Скорости
                |X = $der_X_svk
                |Y = $der_Y_svk
                |Z = $der_Z_svk
            """.trimMargin())
        }
    }
}
