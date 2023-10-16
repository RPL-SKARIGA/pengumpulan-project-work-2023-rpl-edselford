package com.edselmustapa.mywallet.graph

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.edselmustapa.mywallet.R

sealed class Route(
    val route: String,
    val title: String,
    val icon: @Composable () -> Unit
) {

    object Dashboard : Route(
        route = "dashboard",
        title = "Dashboard",
        icon = {
            Icon(imageVector = Icons.Rounded.Home, contentDescription = "")
        }
    )

    object Transfer : Route(
        route = "transfer",
        title = "Transfer",
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.outline_local_atm_24),
                contentDescription = ""
            )
        }
    )

    object Discussion : Route(
        route = "discussion",
        title = "Discussion",
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_chat_24),
                contentDescription = ""
            )
        }
    )

    object Chat : Route(
        route = "chat",
        title = "Chat",
        icon = {}
    )

    object CreateDiscussion : Route(
        route = "create-discussion",
        title = "Create Descussion",
        icon = {}
    )
}