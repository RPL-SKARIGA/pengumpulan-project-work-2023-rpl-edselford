package com.edselmustapa.mywallet.component

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.edselmustapa.mywallet.config.GAME_VOUCHER
import com.edselmustapa.mywallet.config.rupiah
import com.edselmustapa.mywallet.graph.Route
import com.edselmustapa.mywallet.lib.HomeViewModel
import com.edselmustapa.mywallet.lib.ShopViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ShopVoucher(
    navController: NavController,
    viewModel: ShopViewModel,
    homeViewModel: HomeViewModel
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Voucher Game") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                    }
                }
            )
        }
    ) {
        FlowRow(
            modifier = Modifier
                .padding(it)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,

            ) {
            val backgroundColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
            GAME_VOUCHER.forEach {
                Box(
                    modifier = Modifier
                        .padding(5.dp)
                        .clip(RoundedCornerShape(10))
                        .clickable { navController.navigate(Route.Game.route + "/" + it.id) }
                        .size(175.dp)
                        .background(MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp))
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxSize(),
                        painter = painterResource(id = it.icon),
                        contentDescription = ""
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .height(30.dp)
                            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp))
                    ) {
                        Text(
                            text = it.name,
                            modifier = Modifier
                                .align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}