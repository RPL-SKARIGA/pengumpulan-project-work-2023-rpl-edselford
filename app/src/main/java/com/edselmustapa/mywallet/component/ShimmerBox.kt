package com.edselmustapa.mywallet.component

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun ShimmerBox(
    loading: Boolean,
    shimmer: Shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.Window),
    shimmerContent: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit
) {
    if (loading) {
        Box(modifier = Modifier.shimmer(shimmer)) {
            if (shimmerContent == null)
                content()
            else
                shimmerContent()
        }
    } else {
        content()
    }
}