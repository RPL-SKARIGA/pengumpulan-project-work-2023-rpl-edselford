package com.edselmustapa.mywallet.view

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.edselmustapa.mywallet.config.PAYMENT_METHOD
import com.edselmustapa.mywallet.config.lightColor
import com.edselmustapa.mywallet.config.rupiah
import com.edselmustapa.mywallet.lib.HomeViewModel
import com.edselmustapa.mywallet.lib.TopupViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.text.DecimalFormat@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TopupScreen(
    navController: NavController,
    viewModel: TopupViewModel,
    homeViewModel: HomeViewModel
) {
    var nominal by remember { mutableStateOf<Int?>(null) }
    var paymentMethod by remember { mutableStateOf<Int?>(null) }
    var openPaymentMethod by remember { mutableStateOf(false) }
    val loading by viewModel.loading.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Choose topup nominal") },
                navigationIcon = {
                    IconButton(
                        enabled = !loading,
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            BoxWithConstraints(
                modifier = Modifier.fillMaxWidth()
            ) {
                FlowRow(
                    modifier = Modifier
                        .padding(padding)
                        .padding(5.dp),
                    maxItemsInEachRow = if (maxWidth <= 500.dp) 2 else 3
                ) {
                    listOf(
                        20_000,
                        50_000,
                        100_000,
                        200_000,
                        300_000,
                        500_000
                    ).forEachIndexed { index, price ->
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
                                    else
                                        MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
                                )
                                .clickable { nominal = price }
                                .padding(vertical = 30.dp)
                        ) {
                            Text(
                                modifier = Modifier.align(Alignment.Center),
                                text = rupiah(price),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    color = if (nominal == price) colors.second else MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                    }
                }
            }

            val density = LocalDensity.current
            AnimatedVisibility(
                visible = nominal != null,
                modifier = Modifier.align(Alignment.BottomCenter),
                enter = slideInVertically {
                    with(density) {
                        100.dp.roundToPx()
                    }
                }
            ) {
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
                            RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp)
                        )
                        .padding(10.dp),
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 90.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(30.dp)
                                .height(5.dp)
                                .background(
                                    MaterialTheme.colorScheme.outline,
                                    RoundedCornerShape(50)
                                )
                        )

                        Box(
                            modifier = Modifier
                                .padding(top = 20.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(20.dp))
                                .background(MaterialTheme.colorScheme.background)
                                .clickable { openPaymentMethod = true }
                                .padding(20.dp)
                        ) {
                            Text(
                                text = if (paymentMethod == null)
                                    "Choose payment method"
                                else
                                    PAYMENT_METHOD.find { it.id == paymentMethod }?.name ?: "",
                                modifier = Modifier.align(Alignment.CenterStart)
                            )
                            Box(
                                modifier = Modifier.align(Alignment.CenterEnd)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = ""
                                )
                            }
                        }

                    }

                    Button(
                        enabled = !(nominal == null || paymentMethod == null || loading),
                        onClick = {
                            Firebase.auth.currentUser?.email?.let { email ->
                                nominal?.let { amount ->
                                    viewModel.topup(
                                        amount = amount,
                                        email = email,
                                        callback = {
                                            navController.popBackStack()
                                            homeViewModel.refresh()
                                        }
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Top up")
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = rupiah(nominal!!))
                                Spacer(modifier = Modifier.width(10.dp))
                                if (!loading) Icon(
                                    imageVector = Icons.Default.Send,
                                    contentDescription = ""
                                )
                                else CircularProgressIndicator(
                                    modifier = Modifier
                                        .then(Modifier.size(20.dp))
                                )

                            }
                        }
                    }
                }
            }
        }
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val view = LocalView.current
    val scope = rememberCoroutineScope()
    val (available, notAvailable) = PAYMENT_METHOD.partition { it.enable }

    LaunchedEffect(openPaymentMethod) {
        val window = (view.context as Activity).window

        if (openPaymentMethod) {
            window.statusBarColor = Color.Black.copy(alpha = .3f).toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
        } else {
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
        }
    }

    if (openPaymentMethod)
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                openPaymentMethod = false
            },
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(text = "Select payment method", style = MaterialTheme.typography.titleLarge)
                Text(
                    text = "Available method",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(top = 40.dp, bottom = 15.dp)
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.background),
                ) {

                    available.forEachIndexed { index, method ->
                        ListItem(
                            modifier = Modifier.clickable {
                                paymentMethod = method.id
                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        openPaymentMethod = false
                                    }
                                }
                            },
                            headlineContent = { Text(text = method.name) },
                            supportingContent = { Text(text = method.description) },
                            leadingContent = {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(100))
                                        .background(
                                            lightColor().first
                                        )
                                ) {
                                    Icon(
                                        modifier = Modifier.align(Alignment.Center),
                                        imageVector = Icons.Filled.Person,
                                        contentDescription = "",
                                        tint = lightColor().second
                                    )
                                }
                            },
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                        )
                        if (index < available.lastIndex)
                            Divider(color = MaterialTheme.colorScheme.outlineVariant)
                    }

                }





                Text(
                    text = "Not Avaliable method",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(top = 40.dp, bottom = 15.dp)
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.background),
                ) {

                    notAvailable.forEachIndexed { index, method ->
                        ListItem(
                            modifier = Modifier.clickable {},
                            headlineContent = { Text(text = method.name) },
                            leadingContent = {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(100))
                                        .background(
                                            lightColor().first
                                        )
                                ) {
                                    Icon(
                                        modifier = Modifier.align(Alignment.Center),
                                        imageVector = Icons.Filled.Person,
                                        contentDescription = "",
                                        tint = lightColor().second
                                    )
                                }
                            },
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                        )
                        if (index < notAvailable.lastIndex)
                            Divider(color = MaterialTheme.colorScheme.outlineVariant)
                    }

                }

            }

        }

}

