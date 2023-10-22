package com.edselmustapa.mywallet.view

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pullrefresh.PullRefreshIndicator
import androidx.compose.material3.pullrefresh.pullRefresh
import androidx.compose.material3.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.edselmustapa.mywallet.R
import com.edselmustapa.mywallet.auth.UserData
import com.edselmustapa.mywallet.component.HomeCard
import com.edselmustapa.mywallet.graph.Route
import com.edselmustapa.mywallet.lib.HomeViewModel
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import java.text.DecimalFormat

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    onLogoutClick: () -> Unit,
    userData: UserData?,
    viewModel: HomeViewModel
) {
    val loading by viewModel.loading.collectAsState()
    val wallet by viewModel.wallet.collectAsState()

    val pullRefreshState =
        rememberPullRefreshState(refreshing = loading, onRefresh = { viewModel.refresh() })
    val scrollState = rememberScrollState()

    val titleVisible by animateFloatAsState(
        if (scrollState.value > 130) 1f else 0f,
        label = ""
    )

    Scaffold(
        modifier = Modifier
            .pullRefresh(pullRefreshState),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Dashboard",
//                            style = MaterialTheme.typography.headlineLarge
//                                .copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.alpha(titleVisible)
                    )

                },
                actions = {

                    var dropdown by remember { mutableStateOf(false) }
                    IconButton(onClick = { dropdown = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = ""
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Route.Transfer.route) },
                modifier = Modifier.padding(bottom = 50.dp)
            ) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "")
            }
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
                        "Dashboard", style = MaterialTheme.typography.headlineLarge
                            .copy(fontWeight = FontWeight.Bold)
                    )
                }

                val shimmerInstance = rememberShimmer(shimmerBounds = ShimmerBounds.Window)

//                HomeCard(
//                    containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
//                    contentColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer,
//                    title = "Your Wallet $loading",
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
//                    containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.secondaryContainer,
//                    contentColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSecondaryContainer,
//                    title = "Transaction",
//                    value = "3 Times",
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
//                    containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.tertiaryContainer,
//                    contentColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onTertiaryContainer,
//                    title = "Income",
//                    value = "Rp100.000",
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
//                    containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.errorContainer,
//                    contentColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onErrorContainer,
//                    title = "Expenses",
//                    value = "Rp20.000",
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


                HomeCard(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    title = "Your Wallet ${scrollState.value}",
                    value = "Rp." + DecimalFormat("#,###")
                        .format(wallet.wallet),
                    description = "+20.1% from last month",
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_attach_money_24),
                            contentDescription = ""
                        )
                    },
                    loading = loading,
                    shimmerInstance = shimmerInstance
                )
                HomeCard(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    title = "Transaction",
                    value = "3 Times",
                    description = "+180.1% from last month",
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_supervisor_account_24),
                            contentDescription = ""
                        )
                    },
                    loading = loading,
                    shimmerInstance = shimmerInstance
                )
                HomeCard(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    title = "Income",
                    value = "Rp100.000",
                    description = "+19% from last month",
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_credit_card_24),
                            contentDescription = ""
                        )
                    },
                    loading = loading,
                    shimmerInstance = shimmerInstance
                )
                HomeCard(
                    containerColor =MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    title = "Expenses",
                    value = "Rp20.000",
                    description = "+201 since last hour",
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_short_text_24),
                            contentDescription = ""
                        )
                    },
                    loading = loading,
                    shimmerInstance = shimmerInstance
                )

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