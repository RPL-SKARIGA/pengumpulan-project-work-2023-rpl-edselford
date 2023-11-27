package com.edselmustapa.mywallet.component

import android.app.Activity
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.edselmustapa.mywallet.R
import com.edselmustapa.mywallet.config.OPERATOR
import com.edselmustapa.mywallet.config.lightColor
import com.edselmustapa.mywallet.config.rupiah
import com.edselmustapa.mywallet.lib.HomeViewModel
import com.edselmustapa.mywallet.lib.ShopViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlin.math.floor

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ShopPLN(
    navController: NavController,
    viewModel: ShopViewModel,
    homeViewModel: HomeViewModel
) {
    var meterNumber by remember { mutableStateOf("") }
    var nominal by remember { mutableStateOf<Int?>(null) }
    val wallet by homeViewModel.wallet.collectAsState()
    val focusManager = LocalFocusManager.current


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pulsa") },
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
                .background(MaterialTheme.colorScheme.surfaceColorAtElevation((2).dp))
                .verticalScroll(rememberScrollState())
        ) {

            Row(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(
                            Color.White
                        )
                        .size(30.dp)
                ) {
                    Image(
                        modifier = Modifier.align(Alignment.Center),
                        painter = painterResource(
                            id = R.drawable.pln
                        ), contentDescription = ""
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    "Token Listrik",
                    style = MaterialTheme.typography.bodyLarge
                        .copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold
                        )
                )
            }
            Row(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Meter Number",
                    style = MaterialTheme.typography.bodyLarge
                        .copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
                Spacer(modifier = Modifier.width(20.dp))
                BasicTextField(
                    value = meterNumber,
                    onValueChange = {
                        meterNumber = it.filter { char -> char.isDigit() }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    textStyle = MaterialTheme.typography.bodyLarge
                        .copy(color = MaterialTheme.colorScheme.onBackground),
                    modifier = Modifier.fillMaxWidth(),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground)
                ) {

                    Box {
                        if (meterNumber.isBlank()) Text(
                            "Ex: 123456789",
                            style = TextStyle(color = MaterialTheme.colorScheme.outline),
                            modifier = Modifier.alpha(.5f)
                        )
                        it()
                    }

                }
            }
            BoxWithConstraints(
                modifier = Modifier.fillMaxWidth()
            ) {

            FlowRow(
                modifier = Modifier
                    .height(700.dp)
                    .fillMaxWidth()
                    .padding(5.dp),
                maxItemsInEachRow = if (maxWidth <= 500.dp) 2 else 3
            ) {
                (if (meterNumber != "") listOf(
                    20_000,
                    50_000,
                    100_000,
                    200_000,
                    500_000,
                    1_000_000,
                    2_000_000,
                    5_000_000,
                    10_000_000
                ) else emptyList()).forEachIndexed { index, price ->
                    val colors = lightColor("$index")
                    Box(
                        modifier = Modifier
                            .weight(.5f)
                            .padding(vertical = 5.dp, horizontal = 5.dp)
                            .clip(
                                RoundedCornerShape(
                                    animateIntAsState(
                                        targetValue = if (nominal == price) 30 else 100,
                                        label = ""
                                    ).value
                                )
                            )
                            .background(
                                if (nominal == price)
                                    colors.first
                                else if (price >= wallet.wallet)
                                    MaterialTheme.colorScheme.surfaceColorAtElevation(7.dp)
                                else
                                    MaterialTheme.colorScheme.background,
                            )
                            .then(if (price <= wallet.wallet) Modifier.clickable {
                                nominal = price
                            } else Modifier)
                            .padding(vertical = 30.dp)
                    ) {
                        Column(modifier = Modifier.align(Alignment.Center)) {

                            Text(
                                text = rupiah(price),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    color = if (nominal == price) colors.second
                                    else MaterialTheme.colorScheme.onSurface
                                )
                            )
                            if (price >= wallet.wallet) Text(
                                "Not enough money",
                                style = TextStyle(color = MaterialTheme.colorScheme.onSurface),
                                modifier = Modifier.alpha(.5f)
                            )
                        }
                    }
                }
            }
            }
        }
    }


    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    val view = LocalView.current

    LaunchedEffect(nominal) {
        val window = (view.context as Activity).window

        if (nominal != null) {
            window.statusBarColor = Color.Black.copy(alpha = .3f).toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
        } else {
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
        }
    }

    if (nominal != null) {
        ModalBottomSheet(onDismissRequest = {
            scope.launch {
                sheetState.hide()
            }.invokeOnCompletion { nominal = null }
        }) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp)
                ) {
                    Text(
                        text = "Payment Detail",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.align(Alignment.Center)
                        ) {
                            Icon(
                                modifier = Modifier.size(20.dp),
                                painter = painterResource(id = R.drawable.baseline_credit_card_24),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = rupiah(wallet.wallet),
                                style = TextStyle(color = MaterialTheme.colorScheme.onPrimaryContainer)
                            )

                        }
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp)
                ) {
                    Text(text = "Total Transaction")
                    Text(text = rupiah(nominal!!))
                }
                val amount_double = try {
                    nominal!!.toDouble()
                } catch (e: Exception) {
                    0.0
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp)
                ) {
                    Text(text = "Fee")
                    Text(text = rupiah(floor(amount_double / 100)))
                }
                Divider()
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp)
                ) {
                    Text(text = "Subtotal")
                    Text(text = rupiah(floor(amount_double + amount_double / 100)))
                }


                Button(
                    onClick = {
                        Firebase.auth.currentUser?.email?.let {
                            viewModel.pay(
                                it,
                                nominal!!
                            ) {
                                homeViewModel.refresh()
                                navController.popBackStack()
                                navController.popBackStack()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Konfirmasi")
                }
            }
        }
    }
}