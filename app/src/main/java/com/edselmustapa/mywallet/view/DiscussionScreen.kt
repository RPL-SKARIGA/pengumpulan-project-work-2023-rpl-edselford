package com.edselmustapa.mywallet.view

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ListItem as Lists
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import com.edselmustapa.mywallet.lib.pullrefresh.PullRefreshIndicator
import com.edselmustapa.mywallet.lib.pullrefresh.pullRefresh
import com.edselmustapa.mywallet.lib.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

    var searchState by remember { mutableStateOf(false) }
    var searchKeyword by remember { mutableStateOf("") }

    val pullRefreshState =
        rememberPullRefreshState(refreshing = loading, onRefresh = { viewModel.refresh() })

    Scaffold(
        modifier = Modifier
            .pullRefresh(pullRefreshState),

        topBar = {
            Box(modifier = Modifier.fillMaxWidth()) {
                SearchBar(
                    modifier = Modifier.align(Alignment.TopCenter),
                    query = searchKeyword,
                    onQueryChange = { searchKeyword = it },
                    onSearch = {},
                    active = searchState,
                    onActiveChange = { searchState = it },
                    placeholder = { Text("Search discussion") },
                    leadingIcon = {
                        if (searchState)
                            IconButton(onClick = { searchState = false }) {
                                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                            }
                        else
                            Icon(Icons.Default.Search, contentDescription = null)

                    },
                ) {
                    discussion.filter { it.topic.contains(searchKeyword, ignoreCase = true) }
                        .forEach {
                            Lists(
                                headlineContent = { Text(it.topic) },
                                supportingContent = { Text("Asked by ${it.owner}") },
                                trailingContent = { Text("Posted 3h Ago") },
                                modifier = Modifier.clickable {
                                    navController.navigate("${Route.Chat.route}/${it._id}")
                                }.padding(vertical = 10.dp)
                            )
                            Divider(
                                color = MaterialTheme.colorScheme.outline.copy(alpha = .2f)
                            )
                        }
                }
            }

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
                .padding(top = 20.dp)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                discussion.forEach {
                    Lists(
                        headlineContent = { Text(it.topic) },
                        supportingContent = { Text("Asked by ${it.owner}") },
                        trailingContent = { Text("Posted 3h Ago") },
                        modifier = Modifier.clickable {
                            navController.navigate("${Route.Chat.route}/${it._id}")
                        }.padding(vertical = 10.dp)
                    )
                    Divider(
                        color = MaterialTheme.colorScheme.outline.copy(alpha = .2f)
                    )
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