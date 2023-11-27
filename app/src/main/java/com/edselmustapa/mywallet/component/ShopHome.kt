package com.edselmustapa.mywallet.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.edselmustapa.mywallet.R
import com.edselmustapa.mywallet.graph.Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopHome(
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Shop") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
        ) {
            ListItem(
                modifier = Modifier
                    .clickable { navController.navigate(Route.Shop.route + "/pulsa") }
                    .padding(10.dp),
                headlineContent = { Text(text = "Pulsa") },
                leadingContent = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_phone_iphone_24),
                        contentDescription = ""
                    )
                },
                trailingContent = {
                    Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "")
                }
            )
            Divider()
            ListItem(
                modifier = Modifier
                    .clickable { navController.navigate(Route.Shop.route + "/pln") }
                    .padding(10.dp),
                headlineContent = { Text(text = "PLN") },
                leadingContent = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_electric_bolt_24),
                        contentDescription = ""
                    )
                },
                trailingContent = {
                    Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "")
                }
            )
            Divider()
            ListItem(
                modifier = Modifier
                    .clickable { navController.navigate(Route.Shop.route + "/game") }
                    .padding(10.dp),
                headlineContent = { Text(text = "Voucher Game") },
                leadingContent = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_videogame_asset_24),
                        contentDescription = ""
                    )
                },
                trailingContent = {
                    Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "")
                }
            )
        }
    }

}