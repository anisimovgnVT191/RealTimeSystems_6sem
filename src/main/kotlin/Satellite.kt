import java.lang.IllegalArgumentException

class Satellite private constructor(
    private val sourceString: List<String>
){
    val satNum: String
    val epoch: Epoch
    val clockShift: Double
    val clockSpeed: Double
    val clockAcceleration: Double
    val IODE: Double by lazy { doubleFromStringByNum(1) }
    val Crs: Double by lazy { doubleFromStringByNum(2) }
    val Delta_n: Double by lazy { doubleFromStringByNum(3) }
    val M0: Double by lazy { doubleFromStringByNum(4) }
    val Cuc: Double by lazy { doubleFromStringByNum(5) }
    val E_orbit: Double by lazy { doubleFromStringByNum(6) }
    val Cus: Double by lazy { doubleFromStringByNum(7) }
    val Sqrt_A: Double by lazy { doubleFromStringByNum(8) }
    val Toe: Double by lazy { doubleFromStringByNum(9) }
    val Cic: Double by lazy { doubleFromStringByNum(10) }
    val _OMEGA: Double by lazy { doubleFromStringByNum(11) }
    val CIS: Double by lazy { doubleFromStringByNum(12) }
    val i0: Double by lazy { doubleFromStringByNum(13) }
    val Crc: Double by lazy { doubleFromStringByNum(14) }
    val omega: Double by lazy { doubleFromStringByNum(15) }
    val OMEGA_DOT: Double by lazy { doubleFromStringByNum(16) }
    val IDOT: Double by lazy { doubleFromStringByNum(17) }
    val L2_codes: Double by lazy { doubleFromStringByNum(18) }
    val Week_num: Double by lazy { doubleFromStringByNum(19) }
    val L2_P: Double by lazy { doubleFromStringByNum(20) }
    val exactness: Double by lazy { doubleFromStringByNum(21) }
    val Condition_sat: Double by lazy { doubleFromStringByNum(22) }
    val TGD: Double by lazy { doubleFromStringByNum(23) }
    val IODC: Double by lazy { doubleFromStringByNum(24) }
    val time_of_passing: Double by lazy { doubleFromStringByNum(25) }
    val approx: Double by lazy { doubleFromStringByNum(26) }
    val reserved_1: Double by lazy { doubleFromStringByNum(27) }
    val reserved_2: Double by lazy { doubleFromStringByNum(28) }

    init {
        val PRN_EPOCH_SV_CLK = sourceString[0].split(IGNORE_SPACING.toRegex())
            .filter { it.isNotEmpty() && it.isNotBlank() }
        satNum = PRN_EPOCH_SV_CLK[0]
        epoch = Epoch(
            year = PRN_EPOCH_SV_CLK[1].toShort(),
            month = PRN_EPOCH_SV_CLK[2].toShort(),
            day = PRN_EPOCH_SV_CLK[3].toShort(),
            hour = PRN_EPOCH_SV_CLK[4].toShort(),
            minute = PRN_EPOCH_SV_CLK[5].toShort(),
            seconds = PRN_EPOCH_SV_CLK[6].toDouble()
        )
        val SV_CLK = PRN_EPOCH_SV_CLK.drop(7)
        clockShift = SV_CLK[0].formatToDouble()
        clockSpeed = SV_CLK[1].formatToDouble()
        clockAcceleration = SV_CLK[2].formatToDouble()

    }
    private fun doubleFromStringByNum(index: Int): Double{
        val indexPair = when(index){
            in 1..4 -> Pair(1, index-1)
            in 5..8 -> Pair(2, index-5)
            in 9..12 -> Pair(3, index-9)
            in 13..16 -> Pair(4, index-13)
            in 17..20 -> Pair(5, index-17)
            in 21..24 -> Pair(6, index-21)
            in 25..28 -> Pair(7, index-25)
            else ->throw IllegalArgumentException("Index $index out of bounds for size = ${sourceString.size}")
        }
        return try{
            sourceString[indexPair.first]
                .split(IGNORE_SPACING.toRegex()).drop(1)[indexPair.second].formatToDouble()
        }catch (e: Exception){
            println(e.message?:"")
            return 0.0
        }

    }
    private fun String.formatToDouble() =
        when(first()){
            '-' -> "-0.${this.drop(2).replace("D", "E")}".toDoubleOrNull()?:0.0
            else -> "0.${this.drop(1).replace("D", "E")}".toDoubleOrNull()?:0.0
        }

    companion object{
        fun fromStringRepresentation(listStr: List<String>) = Satellite(listStr)
        private const val IGNORE_SPACING = """  *"""
    }

    override fun toString() = sourceString.joinToString(separator = "\n")
}

data class Epoch(
    val year: Short,
    val month: Short,
    val day: Short,
    val hour: Short,
    val minute: Short,
    val seconds: Double
)