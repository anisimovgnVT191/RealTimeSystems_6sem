import kotlin.math.*

data class MainAlgorithm(
    val satellite: Satellite,
    val T_pc: Epoch,
) {

    fun evaluate():SatelliteSpeeds = with(satellite){
        //3
        val t = with(T_pc) {
            (Toe.toInt() / 86400) * 86400 + hour.toInt() * 3600 + minute.toInt() * 60 + seconds }

        //4
        var t_K = t - Toe
        if(t_K > 302400) { t_K -= 604800} else if (t_K < -302400) { t_K += 604800}

        //5
        val n = sqrt(m / Sqrt_A.pow(6)) + Delta_n

        //6
        val M_k = M0 + n * t_K

        //7
        var E_kn = M_k
        var E_k: Double
        while (true){
            E_k = E_kn
            E_kn += (M_k - E_kn + E_orbit * sin(E_kn)) / (1 - E_orbit * cos(E_kn))
            if(abs(E_kn - E_kn) < eps)
                break
        }
        E_k = E_kn
        //8
        val der_E_k = n / (1 - E_orbit * cos(E_k))

        //9
        val theta_k = atan2((sqrt(1 - E_orbit.pow(2))) * sin(E_k) , (cos(E_k) - E_orbit) )

        //10
        val phi_k = theta_k + omega

        //11
        val der_phi_k = ( sqrt(1 - E_orbit.pow(2)) * der_E_k) / (1 - E_orbit * cos(E_k))

        //12
        val delta_U_k = Cuc * cos(2 * phi_k) + Cus * sin(2 * phi_k)
        val U_k = phi_k + delta_U_k

        //13
        val der_U_k = der_phi_k * (1 + 2 * (Cuc * cos(2 * phi_k) - Cus * sin(2 * phi_k)))

        //14
        val delta_r_K = Crc * cos(2 * phi_k) + Crs * sin(2 * phi_k)
        val r_k = Sqrt_A.pow(2) * (1 - E_orbit * cos(E_k)) + delta_r_K

        //15
        val der_r_k = Sqrt_A.pow(2) * E_orbit * der_E_k * sin(E_k) +
            2 * der_phi_k * (Crc * cos(2 * phi_k) + Crs * sin(2 * phi_k))

        //16
        val delta_I_k = Cic * cos(2 * phi_k) + CIS * sin(2 * phi_k)
        val I_k = i0 + delta_I_k + IDOT * t_K

        //17
        val der_I_k = IDOT + 2 * der_phi_k * (CIS * cos(2 * phi_k) + Cic * sin(2 * phi_k))

        //18
        val X_phi_k = r_k * cos(U_k)
        val Y_phi_k = r_k * sin(U_k)

        //19
        val der_X_phi_k = der_r_k * cos(U_k) - Y_phi_k * der_U_k
        val der_Y_phi_k = der_r_k * sin(U_k) - X_phi_k * der_U_k

        //20
        val OMEGA_K = _OMEGA + (OMEGA_DOT - earthRotationSpeed) * t_K - earthRotationSpeed * Toe

        //21
        val der_OMEGA_k = OMEGA_DOT - earthRotationSpeed

        //22
        val X_SVK = X_phi_k * cos(OMEGA_K) - Y_phi_k * cos(I_k) * sin(OMEGA_K)
        val Y_SVK = X_phi_k * sin(OMEGA_K) + Y_phi_k * cos(I_k) * cos(OMEGA_K)
        val Z_SVK = Y_phi_k * sin(I_k)


        return@with SatelliteSpeeds(
            der_X_svk = -der_OMEGA_k * Y_SVK + der_X_phi_k * cos(OMEGA_K) - (der_Y_phi_k * cos(I_k) - Y_phi_k * der_I_k * sin(I_k)) * sin(OMEGA_K),
            der_Y_svk = der_OMEGA_k * X_SVK +der_X_phi_k * sin(OMEGA_K) + (der_Y_phi_k * cos(I_k) - Y_phi_k * der_I_k * sin(I_k)) * cos(OMEGA_K),
            der_Z_svk = Y_phi_k * der_I_k * sin(I_k) + der_Y_phi_k * sin(I_k),
            X_svk = X_SVK,
            Y_svk = Y_SVK,
            Z_svk = Z_SVK
        )
    }






    companion object{
        private const val m = 3.986005E14
        private const val earthRotationSpeed = 7.2921151467E-5
        private const val eps = 10E-6
    }
}

data class SatelliteSpeeds(
    val X_svk: Double,
    val Y_svk: Double,
    val Z_svk: Double,
    val der_X_svk: Double,
    val der_Y_svk: Double,
    val der_Z_svk: Double,
)