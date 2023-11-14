package com.edselmustapa.mywallet.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.edselmustapa.mywallet.config.rupiah
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun TransactionData(
    modifier: Modifier = Modifier,
    amount: Long,
    text: String,
    shimmerInstance: Shimmer,
    loading: Boolean
) {
    Column(
        modifier = modifier.then(
            Modifier
                .padding(5.dp)
                .background(
                    MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
                    RoundedCornerShape(20.dp)
                )
                .padding(20.dp)
        )
    ) {
        Text(text)

        if (loading) {
            Box(
                modifier = Modifier
                    .shimmer(shimmerInstance)
                    .background(
                        color = MaterialTheme.colorScheme.inverseSurface,
                        shape = RoundedCornerShape(50)
                    )
            ) {
                Text(
                    text = rupiah(100000),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.alpha(0f)
                )
            }
        } else
            Text(
                rupiah(amount),
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
            )
    }
}