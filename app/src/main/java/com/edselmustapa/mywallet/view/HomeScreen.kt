package com.edselmustapa.mywallet.view

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import com.edselmustapa.mywallet.lib.pullrefresh.PullRefreshIndicator
import com.edselmustapa.mywallet.lib.pullrefresh.pullRefresh
import com.edselmustapa.mywallet.lib.pullrefresh.rememberPullRefreshState
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.edselmustapa.mywallet.R
import com.edselmustapa.mywallet.auth.UserData
import com.edselmustapa.mywallet.graph.Route
import com.edselmustapa.mywallet.lib.HomeViewModel
import com.edselmustapa.mywallet.lib.PreferencesViewModel
import com.edselmustapa.mywallet.component.CardWallet
import com.edselmustapa.mywallet.component.ChangeCardBottomSheet
import com.edselmustapa.mywallet.component.HomeButtons
import com.edselmustapa.mywallet.component.ShimmerBox
import com.edselmustapa.mywallet.component.TransactionData
import com.edselmustapa.mywallet.config.lightColor
import com.edselmustapa.mywallet.config.rupiah
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.util.Calendar
import java.util.Date

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun HomeScreen(
    navController: NavController,
    onLogoutClick: () -> Unit,
    userData: UserData?,
    viewModel: HomeViewModel,
    preferences: PreferencesViewModel
) {
    val loading by viewModel.loading.collectAsState()
    val wallet by viewModel.wallet.collectAsState()
    val expense by viewModel.expense.collectAsState()
    val income by viewModel.income.collectAsState()
    val setting by preferences.setting.collectAsState()
    val view = LocalView.current
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var openBottomSheet by remember { mutableStateOf(false) }
    val shimmerInstance = rememberShimmer(shimmerBounds = ShimmerBounds.Window)
    val scope = rememberCoroutineScope()
    var displayTransaction by remember { mutableStateOf("week") }


    val pullRefreshState =
        rememberPullRefreshState(refreshing = loading, onRefresh = { viewModel.refresh() })
    val scrollState = rememberScrollState()

    val titleVisible by animateFloatAsState(
        if (scrollState.value > 130) 1f else 0f,
        label = ""
    )

    val colorState by animateColorAsState(
        if (scrollState.value <= 130)
            MaterialTheme.colorScheme.background
        else
            MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
        label = ""
    )

    val cardImages = listOf(
        "card_1" to R.drawable.card_1,
        "card_2" to R.drawable.card_2,
        "card_3" to R.drawable.card_3,
        "card_4" to R.drawable.card_4,
        "card_5" to R.drawable.card_5,
        "card_6" to R.drawable.card_6
    )


    LaunchedEffect(openBottomSheet) {
        val window = (view.context as Activity).window

        if (openBottomSheet) {
            window.statusBarColor = Color.Black.copy(alpha = .3f).toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
        } else {
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
        }
    }

    if (openBottomSheet) {
        ChangeCardBottomSheet(
            dismissRequest = {
                scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                    if (!bottomSheetState.isVisible) {
                        openBottomSheet = false
                    }
                }
            },
            bottomSheetState,
            cardImages,
            preferences,
            setting
        )
    }

    Scaffold(
        modifier = Modifier
            .pullRefresh(pullRefreshState),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorState
                ),
                title = {
                    Color.Black
                    Text(
                        "Dashboard",
                        modifier = Modifier.alpha(titleVisible)
                    )

                },
                actions = {

                    var dropdown by remember { mutableStateOf(false) }
                    IconButton(
                        onClick = { dropdown = true },
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ) {
                        AsyncImage(
                            model = Firebase.auth.currentUser?.photoUrl,
                            contentDescription = "",
                            modifier = Modifier.clip(CircleShape)
                        )
                    }

                    DropdownMenu(
                        modifier = Modifier
                            .width(200.dp),
                        expanded = dropdown,
                        onDismissRequest = { dropdown = false }) {
                        Box(
                            modifier = Modifier.padding(
                                start = 15.dp,
                                bottom = 15.dp,
                                top = 5.dp
                            )
                        ) {
                            if (userData != null) {
                                Text(
                                    userData.email,
                                    style = TextStyle(fontWeight = FontWeight.Bold)
                                )
                            }

                        }
                        Divider()
                        DropdownMenuItem(
                            text = { Text("Log out") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.ExitToApp,
                                    contentDescription = ""
                                )
                            },
                            onClick = {
                                onLogoutClick()
                                dropdown = false
                            },
                            modifier = Modifier.height(40.dp)
                        )
                    }
                }
            )
        }

    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp)
                    .verticalScroll(scrollState)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 10.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    if (userData != null) {
                        Text(userData.username, style = TextStyle(fontSize = 20.sp))
                    }
                    Text(
                        "MyWallet", style = MaterialTheme.typography.headlineLarge
                            .copy(fontWeight = FontWeight.Bold)
                    )
                }


                CardWallet(
                    cardChange = { openBottomSheet = true },
                    shimmerInstance = shimmerInstance,
                    cardImages = cardImages,
                    setting = setting,
                    loading = loading,
                    wallet = wallet
                )


                HomeButtons(loading, navController)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, start = 5.dp)
                ) {
                    var dropdown by remember { mutableStateOf(false) }
                    Button(
                        onClick = { dropdown = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        contentPadding = PaddingValues(20.dp, 5.dp, 10.dp, 5.dp),
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Text(text = if (displayTransaction == "anytime") "Any Time" else "This $displayTransaction")
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = ""
                        )
                    }
                    DropdownMenu(
                        modifier = Modifier
                            .width(200.dp),
                        expanded = dropdown,
                        onDismissRequest = { dropdown = false }) {
                        DropdownMenuItem(
                            text = { Text("anytime") },
                            onClick = {
                                displayTransaction = "anytime"
                                dropdown = false
                                viewModel.transactionInfo("anytime")
                            },
                            modifier = Modifier.height(40.dp)
                        )
                        DropdownMenuItem(
                            text = { Text("month") },
                            onClick = {
                                displayTransaction = "month"
                                dropdown = false
                                viewModel.transactionInfo("month")
                            },
                            modifier = Modifier.height(40.dp)
                        )
                        DropdownMenuItem(
                            text = { Text("week") },
                            onClick = {
                                displayTransaction = "week"
                                dropdown = false
                                viewModel.transactionInfo("week")

                            },
                            modifier = Modifier.height(40.dp)
                        )
                    }
                }

                Row {
                    TransactionData(
                        amount = expense,
                        text = "Excpense",
                        modifier = Modifier.weight(.5f),
                        shimmerInstance = shimmerInstance,
                        loading = loading
                    )
                    TransactionData(
                        amount = income,
                        text = "Income",
                        modifier = Modifier.weight(.5f),
                        shimmerInstance = shimmerInstance,
                        loading = loading
                    )
                }


                Spacer(modifier = Modifier.height(2000.dp))

            }
            PullRefreshIndicator(
                refreshing = loading,
                state = pullRefreshState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
            )
        }
    }
}



