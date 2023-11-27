package com.edselmustapa.mywallet.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import com.edselmustapa.mywallet.component.ShopHome
import com.edselmustapa.mywallet.component.ShopPLN
import com.edselmustapa.mywallet.component.ShopPulsa
import com.edselmustapa.mywallet.component.ShopVoucher
import com.edselmustapa.mywallet.lib.HomeViewModel
import com.edselmustapa.mywallet.lib.ShopViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopScreen(
    navController: NavController,
    id: String? = null,
    viewModel: ShopViewModel,
    homeViewModel: HomeViewModel,
) {
    when (id) {
        "pulsa" -> ShopPulsa(navController, viewModel, homeViewModel)
        "pln" -> ShopPLN(navController, viewModel, homeViewModel)
        "game" -> ShopVoucher(navController, viewModel, homeViewModel)
        else -> ShopHome(navController)
    }

}