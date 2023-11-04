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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import java.text.DecimalFormat

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
    val transaction by viewModel.transaction.collectAsState()
    val setting by preferences.setting.collectAsState()
    val cardLoading by preferences.loading.collectAsState()
    val view = LocalView.current

    val shimmerInstance = rememberShimmer(shimmerBounds = ShimmerBounds.Window)

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

    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var openBottomSheet by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    val cardImages = listOf(
        "card_1" to R.drawable.card_1,
        "card_2" to R.drawable.card_2,
        "card_3" to R.drawable.card_3
    )

    val lightColor: Pair<Color, Color> =
        (if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer) to
                if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer


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
            dismissRequest = { openBottomSheet = false },
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

    ) {
        Box(modifier = Modifier.padding(it)) {
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp)
                    .verticalScroll(scrollState)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
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


                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(10.dp)
                        .horizontalScroll(rememberScrollState())
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        FilledIconButton(
                            onClick = { /*TODO*/ },
                            shape = FloatingActionButtonDefaults.shape,
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = lightColor.first,
                                contentColor = lightColor.second
                            ),
                            modifier = Modifier.size(70.dp)
                        ) {
                            Icon(imageVector = Icons.Filled.Add, contentDescription = "")
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "Top Up")
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        FilledIconButton(
                            onClick = { navController.navigate(Route.Transfer.route) },
                            shape = FloatingActionButtonDefaults.shape,
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = lightColor.first,
                                contentColor = lightColor.second
                            ),
                            modifier = Modifier.size(70.dp)
                        ) {
                            Icon(imageVector = Icons.Filled.Send, contentDescription = "")
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "Transfer")
                    }

                    for (i in 0..1) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            FilledIconButton(
                                onClick = { },
                                shape = FloatingActionButtonDefaults.shape,
                                colors = IconButtonDefaults.filledIconButtonColors(
                                    containerColor = lightColor.first,
                                    contentColor = lightColor.second
                                ),
                                modifier = Modifier.size(70.dp)
                            ) {
//                                Icon(imageVector = Icons.Filled.Send, contentDescription = "")
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = "idk")
                        }
                    }
                }


                Box(
                    modifier = Modifier
                        .height(150.dp)
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(20.dp)
                ) {
                    Button(
                        modifier = Modifier.align(Alignment.TopStart),
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = lightColor.first,
                            contentColor = lightColor.second
                        )
                    ) {
                        Text(text = "This week")
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = ""
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        listOf(
                            "Income" to (transaction.fold(0L) { a, b ->
                                if (!b.isSender) a + b.amount else a
                            } to R.drawable.round_trending_up_24),
                            "Expense" to (transaction.fold(0L) { a, b ->
                                if (b.isSender) a + b.amount else a
                            } to R.drawable.round_trending_down_24)
                        ).forEach { (key, value) ->
                            Row(
                                modifier = Modifier.weight(.5f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = value.second),
                                    contentDescription = "",
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(key)
                                    if (loading)
                                        Box(
                                            modifier = Modifier
                                                .shimmer(shimmerInstance)
                                                .background(
                                                    color = Color.White.copy(alpha = 1f),
                                                    shape = RoundedCornerShape(50)
                                                )
                                        ) {
                                            Text(
                                                "Rp." + DecimalFormat("#,###")
                                                    .format(1_000_000),
                                                modifier = Modifier.alpha(0f)
                                            )
                                        }
                                    else
                                        Text(
                                            "Rp." + DecimalFormat("#,###")
                                                .format(value.first)
                                        )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2000.dp))

//                HomeCard(
//                    containerColor = MaterialTheme.colorScheme.primaryContainer,
//                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
//                    title = "Your Wallet",
//                    value = "Rp." + DecimalFormat("#,###")
//                        .format(wallet.wallet),
//                    description = "+20.1% from last month",
//                    icon = {
//                        Icon(
//                            painter = painterResource(id = R.drawable.baseline_attach_money_24),
//                            contentDescription = ""
//                        )
//                    },
//                    loading = loading,
//                    shimmerInstance = shimmerInstance
//                )
//                HomeCard(
//                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
//                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
//                    title = "Transaction",
//                    value = "${transaction.size} Times",
//                    description = "+180.1% from last month",
//                    icon = {
//                        Icon(
//                            painter = painterResource(id = R.drawable.baseline_supervisor_account_24),
//                            contentDescription = ""
//                        )
//                    },
//                    loading = loading,
//                    shimmerInstance = shimmerInstance
//                )
//                HomeCard(
//                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
//                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
//                    title = "Income",
//                    value = "Rp." + DecimalFormat("#,###")
//                        .format(transaction.fold(0) {a,b ->
//                            if (!b.isSender) a + b.amount else a
//                        }),
//                    description = "+19% from last month",
//                    icon = {
//                        Icon(
//                            painter = painterResource(id = R.drawable.baseline_credit_card_24),
//                            contentDescription = ""
//                        )
//                    },
//                    loading = loading,
//                    shimmerInstance = shimmerInstance
//                )
//                HomeCard(
//                    containerColor = MaterialTheme.colorScheme.errorContainer,
//                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
//                    title = "Expenses",
//                    value = "Rp." + DecimalFormat("#,###")
//                        .format(transaction.fold(0) {a,b ->
//                            if (b.isSender) a + b.amount else a
//                        }),
//                    description = "+201 since last hour",
//                    icon = {
//                        Icon(
//                            painter = painterResource(id = R.drawable.baseline_short_text_24),
//                            contentDescription = ""
//                        )
//                    },
//                    loading = loading,
//                    shimmerInstance = shimmerInstance
//                )

                Spacer(modifier = Modifier.height(100.dp))
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

