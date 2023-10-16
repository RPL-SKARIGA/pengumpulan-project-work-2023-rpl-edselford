package com.edselmustapa.mywallet.view

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pullrefresh.PullRefreshIndicator
import androidx.compose.material3.pullrefresh.pullRefresh
import androidx.compose.material3.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.edselmustapa.mywallet.graph.Route
import com.edselmustapa.mywallet.lib.DiscussionViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscussionScreen(
    navController: NavController,
    viewModel: DiscussionViewModel
) {
    val discussion by viewModel.discussion.collectAsState()
    val loading by viewModel.loading.collectAsState()

    val pullRefreshState =
        rememberPullRefreshState(refreshing = loading, onRefresh = { viewModel.refresh() })

    Scaffold(
        modifier = Modifier
            .pullRefresh(pullRefreshState),

        topBar = {
            TopAppBar(
                title = { Text("Discussion") },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Route.CreateDiscussion.route) },
                modifier = Modifier.padding(bottom = 50.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "")
            }
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                discussion.forEach {
                    androidx.compose.material3.ListItem(
                        headlineText = { Text(it.topic) },
                        supportingText = { Text("Asked by ${it.owner}") },
                        trailingContent = { Text("Posted 3h Ago") },
                        modifier = Modifier.clickable {
                            navController.navigate("${Route.Chat.route}/${it._id}")
                        }
                    )
                    Divider(modifier = Modifier.padding(vertical = 10.dp))
                }
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