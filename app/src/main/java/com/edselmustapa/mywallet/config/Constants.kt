package com.edselmustapa.mywallet.config

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.edselmustapa.mywallet.lib.model.Payment
import com.edselmustapa.mywallet.R
import java.text.DecimalFormat
import kotlin.random.Random


val PAYMENT_METHOD = listOf(
    Payment(99, "Instant", true, "used for testing"),
    Payment(0, "BCA Oneklik", false),
    Payment(1, "Alfamart", false),
    Payment(2, "Indomaret", false),
    Payment(3, "Debit Visa / Mastercard", false),
    Payment(4, "ATM", false),
    Payment(5, "Mobile Banking", false),
    Payment(6, "Tokopedia", false)
)

//val ProductSans = FontFamily(
//    Font()
//)

@Composable
fun lightColor(color: String = "primary"): Pair<Color, Color> {
    val isDark = isSystemInDarkTheme()
    val colors = listOf("primary", "secondary", "tertiary", "error")
    val number = try {
        color.toInt()
    } catch (exception: Exception) {
        null
    }
    val type = if (color == "rand") {
        colors[Random.nextInt(colors.size)]
    } else if (number != null) {
        colors[number % colors.size]
    } else color
    return when (type) {
        "secondary" -> (if (isDark) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.secondaryContainer) to
                if (isDark) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSecondaryContainer

        "tertiary" -> (if (isDark) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.tertiaryContainer) to
                if (isDark) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onTertiaryContainer

        "error" -> (if (isDark) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.errorContainer) to
                if (isDark) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onErrorContainer

        else -> (if (isDark) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer) to
                if (isDark) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer
    }
}

fun rupiah(num: Number) = "Rp." + DecimalFormat("#,###").format(num)
fun rupiah(num: String) = "Rp." + DecimalFormat("#,###").format(
    try {
        num.toLong()
    } catch (e: Exception) {
        0
    }
)

val isProbablyRunningOnEmulator: Boolean =
    ((Build.MANUFACTURER == "Google" && Build.BRAND == "google" &&
            ((Build.FINGERPRINT.startsWith("google/sdk_gphone_")
                    && Build.FINGERPRINT.endsWith(":user/release-keys")
                    && Build.PRODUCT.startsWith("sdk_gphone_")
                    && Build.MODEL.startsWith("sdk_gphone_"))
                    //alternative
                    || (Build.FINGERPRINT.startsWith("google/sdk_gphone64_")
                    && (Build.FINGERPRINT.endsWith(":userdebug/dev-keys") || Build.FINGERPRINT.endsWith(
                ":user/release-keys"
            ))
                    && Build.PRODUCT.startsWith("sdk_gphone64_")
                    && Build.MODEL.startsWith("sdk_gphone64_")))))


//const val URL = "http://192.168.43.167:3000/api/v1/"
//const val URL = "http://10.0.2.2:3000/api/v1/"
//const val URL = "https://my-wallet-api.vercel.app/api/v1/"
val URL =
    if (isProbablyRunningOnEmulator) "http://10.0.2.2:3000/api/v1/" else "http://127.0.0.1:3000/api/v1/"