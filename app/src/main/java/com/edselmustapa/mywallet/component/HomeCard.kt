package com.edselmustapa.mywallet.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun HomeCard(
    containerColor: Color,
    contentColor: Color,
    title: String,
    value: String,
    description: String,
    icon: @Composable () -> Unit,
    loading: Boolean,
    shimmerInstance: Shimmer
) {
    Card(
        elevation = CardDefaults.cardElevation(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .fillMaxWidth()
//            .shadow(5.dp, shape = RoundedCornerShape(15.dp))
//            .padding(bottom = 20.dp)
    ) {
        Column(modifier = Modifier.padding(all = 20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, style = MaterialTheme.typography.bodyLarge)

                icon()
            }
            if (!loading) {
                Text(
                    value, style = MaterialTheme.typography.headlineLarge
                        .copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 2.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .shimmer(shimmerInstance)
                        .width(200.dp)
                        .background(color = contentColor, shape = RoundedCornerShape(15.dp))
                ) {
                    Text(
                        "Rp.0", style = MaterialTheme.typography.headlineLarge
                            .copy(fontWeight = FontWeight.Bold)
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .shimmer(shimmerInstance)
                        .width(100.dp)
                        .background(color = contentColor, shape = RoundedCornerShape(15.dp))
                ) {
                    Text("loading", style = MaterialTheme.typography.bodyMedium)

                }
            }


        }
    }
}