package com.edselmustapa.mywallet.config

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.edselmustapa.mywallet.R
import com.edselmustapa.mywallet.lib.model.Payment
import com.edselmustapa.mywallet.lib.model.Voucher
import com.edselmustapa.mywallet.lib.model.VoucherType
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import kotlin.random.Random


@Composable
fun GameTitle() {
    Text(
        text = "Additional Information",
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .padding(top = 40.dp, bottom = 10.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
val GAME_VOUCHER = listOf(
    Voucher(
        id = 1,
        name = "Genshin Impact",
        icon = R.drawable.genshin,
        type = listOf(
            VoucherType("Bundle", listOf("Blessing of the Welkin Moon" to 60_000)),
            VoucherType(
                "Genesis Crystal", listOf(
                    "60 Genesis Crystal" to 11_000,
                    "300+30 Genesis Crystal" to 58_000,
                    "980+110 Genesis Crystal" to 174_000,
                    "1980+260 Genesis Crystal" to 378_000,
                    "3280+600 Genesis Crystal" to 582_000,
                    "6480+1600 Genesis Crystal" to 1_165_000,
                )
            )
        ),
        input = {
            val SERVER_LIST = listOf("America", "Europe", "Asia", "TW, HK, MO")

            var uid by remember { mutableStateOf("") }
            var server by remember { mutableStateOf<String?>(null) }

            var openServer by remember { mutableStateOf(false) }

            LaunchedEffect(uid, server) {
                if (uid.isEmpty() || server == null) {
                    it(false)
                } else {
                    it(true)
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                GameTitle()
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                            1.dp
                        ),
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
                    ),
                    shape = RoundedCornerShape(topStartPercent = 15, topEndPercent = 15),
                    value = uid,
                    onValueChange = { uid = it },
                    label = { Text("UID") },
                    placeholder = { Text("Enter Your UID") }
                )

                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .clip(RoundedCornerShape(15))
                        .clickable { openServer = true }
                        .height(60.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp))
                        .padding(10.dp)
                ) {
                    Text(
                        text = server ?: "Choose Server",
                        modifier = Modifier.align(
                            Alignment.CenterStart
                        )
                    )

                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "",
                        modifier = Modifier.align(
                            Alignment.CenterEnd
                        )
                    )
                }
            }

            val sheetState = rememberModalBottomSheetState()
            val view = LocalView.current
            val scope = rememberCoroutineScope()

            LaunchedEffect(openServer) {
                val window = (view.context as Activity).window

                if (openServer) {
                    window.statusBarColor = Color.Black.copy(alpha = .3f).toArgb()
                    window.navigationBarColor = Color.Transparent.toArgb()
                } else {
                    window.statusBarColor = Color.Transparent.toArgb()
                    window.navigationBarColor = Color.Transparent.toArgb()
                }
            }

            if (openServer) {
                ModalBottomSheet(
                    sheetState = sheetState,
                    onDismissRequest = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion { openServer = false }
                    },
                    modifier = Modifier.fillMaxSize()
                ) {
                    SERVER_LIST.forEachIndexed { i, it ->
                        ListItem(
                            modifier = Modifier
                                .clickable {
                                    server = it
                                    scope
                                        .launch { sheetState.hide() }
                                        .invokeOnCompletion { openServer = false }
                                }
                                .fillMaxWidth(),
                            headlineContent = { Text(it) },
                        )
                        if (i != SERVER_LIST.lastIndex) Divider(
                            modifier = Modifier.padding(
                                horizontal = 15.dp
                            )
                        )
                    }
                }
            }
        }
    ),

    Voucher(
        id = 2,
        name = "Valorant",
        icon = R.drawable.valorant,
        type = listOf(
            VoucherType(
                "Point", listOf(
                    "125 Points" to 14_250,
                    "420 Points" to 50_000,
                    "700 Points" to 80_000,
                    "1375 Points" to 150_000,
                    "2400 Points" to 250_000,
                    "4000 Points" to 400_000,
                    "8150 Points" to 800_000
                )
            )
        ),
        input = {
            var riotId by remember { mutableStateOf("") }

            LaunchedEffect(riotId) {
                it(riotId.isNotEmpty())
            }

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                GameTitle()
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                            1.dp
                        ),
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
                    ),
                    shape = RoundedCornerShape(topStartPercent = 15, topEndPercent = 15),
                    value = riotId,
                    onValueChange = { riotId = it },
                    label = { Text("Riot ID") },
                    placeholder = { Text("Enter Your Riot ID") }
                )
            }
        }
    ),

    Voucher(
        id = 3,
        name = "PUBG Mobile",
        icon = R.drawable.pubgm,
        type = listOf(
            VoucherType(
                "UC PUBG Mobile", listOf(
                    "60 UC" to 15_500,
                    "325 UC" to 79_500,
                    "660 UC" to 159_000,
                    "1800 UC" to 397_500,
                    "3850 UC" to 795_000,
                    "8100 UC" to 1_590_000
                )
            ),
            VoucherType(
                "Royale Pass", listOf(
                    "Royale Pass" to 155_000,
                    "Elite Pass Plus" to 416_000
                )
            )
        ),
        input = {it(true)}
    ),

    Voucher(
        id = 4,
        name = "Steam",
        icon = R.drawable.steam,
        type = listOf(
            VoucherType(
                "Steam Wallet IDR", listOf(
                    "IDR 12,000" to 12_830,
                    "IDR 45,000" to 48_114,
                    "IDR 60,000" to 64_152,
                    "IDR 90,000" to 96_228,
                    "IDR 120,000" to 128_304,
                    "IDR 250,000" to 267_300,
                    "IDR 400,000" to 425_600,
                    "IDR 600,000" to 633_900
                )
            )
        ),
        input = {it(true)}
    ),
)


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


val OPERATOR = listOf(
    "0852" to "Telkomsel",
    "0853" to "Telkomsel",
    "0813" to "Telkomsel",
    "0821" to "Telkomsel",
    "0822" to "Telkomsel",
    "0851" to "Telkomsel",
    "0812" to "Telkomsel",
    "0811" to "Telkomsel",

    "0857" to "Indosat",
    "0856" to "Indosat",

    "0817" to "XL Axiata",
    "0818" to "XL Axiata",
    "0819" to "XL Axiata",
    "0859" to "XL Axiata",
    "0877" to "XL Axiata",
    "0878" to "XL Axiata",

    "0813" to "AXIS",
    "0832" to "AXIS",
    "0833" to "AXIS",
    "0838" to "AXIS",

    "0881" to "Smartfren",
    "0882" to "Smartfren",
    "0883" to "Smartfren",
    "0884" to "Smartfren",
    "0885" to "Smartfren",
    "0886" to "Smartfren",
    "0887" to "Smartfren",
    "0888" to "Smartfren",
    "0889" to "Smartfren"
)

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