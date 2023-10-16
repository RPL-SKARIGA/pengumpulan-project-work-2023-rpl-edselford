package com.edselmustapa.mywallet.graph

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.internal.composableLambda
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.edselmustapa.mywallet.auth.UserData
import com.edselmustapa.mywallet.lib.DiscussionViewModel
import com.edselmustapa.mywallet.lib.HomeViewModel
import com.edselmustapa.mywallet.view.ChatScreen
import com.edselmustapa.mywallet.view.CreateDiscussionScreen
import com.edselmustapa.mywallet.view.DiscussionScreen
import com.edselmustapa.mywallet.view.HomeScreen
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeNavigationGraph(
    rootNavController: NavHostController,
    userData: UserData?,
    onLogoutClick: () -> Unit
) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val homeViewModel = HomeViewModel(context)
    val discussionViewModel = DiscussionViewModel()


    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {

            val views = listOf(
                Route.Dashboard,
                Route.Discussion
            )

            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            val barDestination = views.any { it.route == currentDestination?.route }

            if (barDestination) {
                Box(
                    modifier = Modifier.background(
                        MaterialTheme.colorScheme.surfaceColorAtElevation(
                            3.dp
                        )
                    )
                ) {
                    NavigationBar(
                        modifier = Modifier
                            .navigationBarsPadding()
                            .height(45.dp)
                    ) {
                        views.forEach {
                            AddItem(
                                screen = it,
                                currentDestination = currentDestination,
                                navController = navController
                            )
                        }

                    }
                }

            }
        }
    ) {


        NavHost(
            navController = navController,
            startDestination = Route.Dashboard.route,
            enterTransition = {
                slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left)
            },
            popEnterTransition = {
                slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Right)
            },
            exitTransition = {
                slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left)
            },
            popExitTransition = {
                slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.Right)
            }

        ) {
            composable(Route.Dashboard.route) {
                HomeScreen(navController, onLogoutClick, userData, homeViewModel)

            }
            composable(Route.Discussion.route) {
                DiscussionScreen(navController, discussionViewModel)
            }
            composable(
                "${Route.Chat.route}/{id}",
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) {
                ChatScreen(navController, discussionViewModel, it.arguments?.getString("id") ?: "")
            }
            composable(Route.CreateDiscussion.route) {
                CreateDiscussionScreen(navController, discussionViewModel)
            }
        }

    }
}


@Composable
fun RowScope.AddItem(
    screen: Route,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    NavigationBarItem(
        alwaysShowLabel = false,
        icon = screen.icon,
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        colors = NavigationBarItemDefaults.colors(
            unselectedIconColor = LocalContentColor.current.copy(
                alpha = .5f
            )
        ),
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    )
}
