package com.edselmustapa.mywallet.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.edselmustapa.mywallet.config.Setting
import com.edselmustapa.mywallet.service.Wallet
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.shimmer
import java.text.DecimalFormat

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun CardWallet(
    cardChange: () -> Unit,
    shimmerInstance: Shimmer,
    cardImages: List<Pair<String, Int>>,
    setting: Setting,
    loading: Boolean,
    wallet: Wallet
) {
    Box(
        modifier = Modifier.combinedClickable(
            onClick = {},
            onLongClick = cardChange
        )
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
                    shape = RoundedCornerShape(20.dp),
                )
                .shimmer(shimmerInstance)
                .fillMaxWidth()
                .height(235.dp)
        )
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(cardImages.find { (card, svg) -> card == setting.cardColor }?.second)
                .decoderFactory(SvgDecoder.Factory())
                .build(),
            contentDescription = "",
//                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
        )
        Text(
            "**** **** **** ${
                Firebase.auth.currentUser?.uid?.filter { it.isDigit() }
                    ?.substring(0 until 4)
            }",
            style = MaterialTheme.typography.displaySmall
                .copy(color = Color.White),
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 30.dp)
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(30.dp)
        ) {
            Text(
                "Balance",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Normal
                )
            )
            if (loading) {
                Box(
                    modifier = Modifier
                        .shimmer(shimmerInstance)
                        .background(
                            color = Color.White.copy(alpha = 1f),
                            shape = RoundedCornerShape(50)
                        )
                ) {
                    Text(
                        "Rp." + DecimalFormat("#,###")
                            .format(100000),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.alpha(0f)
                    )
                }
            } else
                Text(
                    "Rp." + DecimalFormat("#,###")
                        .format(wallet.wallet),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
        }

    }
}