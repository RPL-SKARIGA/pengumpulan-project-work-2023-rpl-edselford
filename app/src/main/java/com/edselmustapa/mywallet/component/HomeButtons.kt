package com.edselmustapa.mywallet.component

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.edselmustapa.mywallet.R
import com.edselmustapa.mywallet.config.lightColor
import com.edselmustapa.mywallet.graph.Route

@Composable
fun HomeButtons(loading: Boolean, navController: NavController) {
    val buttons: List<NavigationButton> = listOf(
        NavigationButton(
            text = "Top Up",
            containerColor = lightColor().first,
            contentColor = lightColor().second,
            icon = { Icon(imageVector = Icons.Filled.Add, contentDescription = "")},
            onClick = {navController.navigate(Route.TopUp.route)}
        ),
        NavigationButton(
            text = "Transfer",
            containerColor = lightColor("secondary").first,
            contentColor = lightColor("secondary").second,
            icon = { Icon(imageVector = Icons.Filled.Send, contentDescription = "")},
            onClick = {navController.navigate(Route.Transfer.route)}
        ),
        NavigationButton(
            text = "Shop",
            containerColor = lightColor("tertiary").first,
            contentColor = lightColor("tertiary").second,
            icon = { Icon(imageVector = Icons.Filled.ShoppingCart, contentDescription = "")},
            onClick = {navController.navigate(Route.Shop.route)}
        ),
        NavigationButton(
            text = "Activity",
            containerColor = lightColor("error").first,
            contentColor = lightColor("error").second,
            icon = { Icon(
                painter = painterResource(id = R.drawable.baseline_short_text_24),
                contentDescription = ""
            )},
            onClick = {navController.navigate(Route.Activity.route)}
        )
    )



    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(10.dp)
            .horizontalScroll(rememberScrollState())
    ) {
        buttons.forEach {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ShimmerBox(loading = loading) {
                FilledIconButton(
                    enabled = !loading,
                    onClick = it.onClick,
                    shape = FloatingActionButtonDefaults.shape,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = it.containerColor,
                        contentColor = it.contentColor
                    ),
                    modifier = Modifier.size(70.dp)
                ) {
                    it.icon()
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(text = it.text)
        }

        }




    }
}


private data class NavigationButton(
    val text: String,
    val containerColor: Color,
    val contentColor: Color,
    val icon: @Composable () -> Unit,
    val onClick: () -> Unit,
)