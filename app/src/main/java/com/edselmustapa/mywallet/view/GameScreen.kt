package com.edselmustapa.mywallet.view

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.edselmustapa.mywallet.R
import com.edselmustapa.mywallet.config.GAME_VOUCHER
import com.edselmustapa.mywallet.config.PAYMENT_METHOD
import com.edselmustapa.mywallet.config.rupiah
import com.edselmustapa.mywallet.lib.HomeViewModel
import com.edselmustapa.mywallet.lib.ShopViewModel
import com.edselmustapa.mywallet.lib.model.Voucher
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.patrykandpatrick.vico.core.extension.getFieldValue
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun GameScreen(
    navController: NavController,
    viewModel: ShopViewModel,
    homeViewModel: HomeViewModel,
    id: Int?
) {
    val game = GAME_VOUCHER.find { it.id == id } ?: Voucher(-1, "Error", -1)
    val wallet by homeViewModel.wallet.collectAsState()
    val loading by viewModel.loading.collectAsState()

    var tabIndex by remember { mutableIntStateOf(0) }
    var selectedVoucher by remember { mutableStateOf<Int?>(null) }
    var openNominal by remember { mutableStateOf(false) }
    var inputCompletion by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    fun refresh() {
        selectedVoucher = null
        openNominal = false
        inputCompletion = false
    }

    Scaffold(
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(onTap = { focusManager.clearFocus() })
        },
        topBar = {
            TopAppBar(
                title = { Text(text = game.name) },
                navigationIcon = {
                    IconButton(onClick = { if (!loading) navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
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
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                        Text(text = "Total Purchases")

                        Text(
                            text = if (selectedVoucher == null) "-" else rupiah(game.type[tabIndex].nominal[selectedVoucher!!].second),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                },
                floatingActionButton = {
                    Button(
                        modifier = Modifier.size(60.dp),
                        contentPadding = PaddingValues(0.dp),
                        enabled = inputCompletion && !loading,
                        shape = FloatingActionButtonDefaults.shape,
                        onClick = {
                            val nominal = game.type[tabIndex].nominal[selectedVoucher!!].second
                            if (wallet.wallet < nominal) {
                                Toast.makeText(context, "Not enough money", Toast.LENGTH_SHORT)
                                    .show()
                                return@Button
                            }
                            Firebase.auth.currentUser?.email?.let {
                                viewModel.pay(
                                    it,
                                    game.type[tabIndex].nominal[selectedVoucher!!].second
                                ) {
                                    homeViewModel.refresh()
                                    navController.popBackStack()
                                    navController.popBackStack()
                                    navController.popBackStack()
                                }
                            }
                        }
                    ) {
                        if (loading)
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .then(Modifier.size(30.dp))
                            )
                        else
                            Icon(imageVector = Icons.Default.Send, contentDescription = "")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            TabRow(selectedTabIndex = tabIndex) {
                game.type.forEachIndexed { index, content ->
                    Tab(
                        modifier = Modifier.height(50.dp),
                        selected = tabIndex == index,
                        onClick = {
                            refresh()
                            tabIndex = index
                        },
                        unselectedContentColor = MaterialTheme.colorScheme.outline
                    ) {
                        Text(text = content.name)
                    }
                }
            }


            Text(
                text = "Select Nominal",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .padding(top = 40.dp, bottom = 10.dp)
            )
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .clip(RoundedCornerShape(15))
                    .clickable { openNominal = true }
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp))
                    .padding(10.dp)
            ) {
                Text(
                    text = if (selectedVoucher == null) "Select Nominal" else game.type[tabIndex].nominal[selectedVoucher!!].first,
                    modifier = Modifier.align(
                        Alignment.CenterStart
                    )
                )

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "",
                    modifier = Modifier.align(
                        Alignment.CenterEnd
                    )
                )
            }

            if (selectedVoucher != null) {
                game.input() {
                    inputCompletion = it
                }
            }
        }
    }


    val sheetState = rememberModalBottomSheetState()
    val view = LocalView.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(openNominal) {
        val window = (view.context as Activity).window

        if (openNominal) {
            window.statusBarColor = Color.Black.copy(alpha = .3f).toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
        } else {
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
        }
    }

    if (openNominal) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                scope.launch { sheetState.hide() }.invokeOnCompletion { openNominal = false }
            },
            modifier = Modifier.fillMaxSize()
        ) {
            game.type[tabIndex].nominal.forEachIndexed { i, it ->
                ListItem(
                    modifier = Modifier
                        .clickable {
                            selectedVoucher = i
                            scope
                                .launch { sheetState.hide() }
                                .invokeOnCompletion { openNominal = false }
                        }
                        .fillMaxWidth(),
                    headlineContent = { Text(it.first) },
                    trailingContent = {
                        Text(
                            rupiah(it.second),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                )
                if (i != game.type[tabIndex].nominal.lastIndex) Divider(
                    modifier = Modifier.padding(
                        horizontal = 15.dp
                    )
                )
            }
        }
    }


}