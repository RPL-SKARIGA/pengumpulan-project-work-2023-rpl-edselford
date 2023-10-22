package com.edselmustapa.mywallet

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.edselmustapa.mywallet.auth.GithubAuth
import com.edselmustapa.mywallet.auth.GoogleAuthUiClient
import com.edselmustapa.mywallet.auth.SignInViewModel
import com.edselmustapa.mywallet.graph.HomeNavigationGraph
import com.edselmustapa.mywallet.service.UserService
import com.edselmustapa.mywallet.ui.theme.MyWalletTheme
import com.edselmustapa.mywallet.view.SignInScreen
import com.example.compose.AppTheme
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    private val githubAuth = GithubAuth()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current

            var loading by remember { mutableStateOf(false) }
            var isOnline by remember { mutableStateOf(isInternetAvailable(context)) }

            ColorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    if (!isOnline) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                "MyWallet",
                                style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(bottom = 50.dp)
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(20.dp)
                            ) {

                                Text("No Internet Connection")
                                FilledIconButton(onClick = {
                                    isOnline = isInternetAvailable(context)
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = ""
                                    )
                                }
                            }

                        }
                    } else {
                        val navController = rememberNavController()

                        fun signInRegister() {
                            lifecycleScope.launch {
                                loading = true

                                val user = googleAuthUiClient.getSignedInUser()
                                if (user != null) {
                                    UserService().register(
                                        user.username.filterNot { it.isWhitespace() }
                                            .lowercase(),
                                        user.email,
                                        user.username
                                    )

                                }

                                loading = false
                                navController.navigate("home") {
                                    popUpTo("sign_in") {
                                        inclusive = true
                                    }
                                }

                            }
                        }

                        Box {

                            NavHost(navController = navController, startDestination = "home") {
                                composable("home") {
                                    LaunchedEffect(key1 = Unit) {
                                        if (googleAuthUiClient.getSignedInUser() == null) {
                                            navController.navigate("sign_in") {
                                                popUpTo("home") {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                    }
                                    HomeNavigationGraph(
                                        rootNavController = navController,
                                        userData = googleAuthUiClient.getSignedInUser(),
                                        onLogoutClick = {
                                            lifecycleScope.launch {
                                                googleAuthUiClient.signOut()
                                                Toast.makeText(
                                                    applicationContext,
                                                    "Signed Out",
                                                    Toast.LENGTH_SHORT
                                                ).show()

                                                navController.navigate("sign_in")
                                            }
                                        }
                                    )
                                }
                                composable("sign_in") {
                                    val viewModel = viewModel<SignInViewModel>()
                                    val state by viewModel.state.collectAsState()


                                    val launcher = rememberLauncherForActivityResult(
                                        contract = ActivityResultContracts.StartIntentSenderForResult(),
                                        onResult = { result ->
                                            if (result.resultCode == RESULT_OK) {
                                                lifecycleScope.launch {
                                                    val signInResult =
                                                        googleAuthUiClient.signInWithIntent(
                                                            intent = result.data ?: return@launch
                                                        )
                                                    viewModel.onSignInResult(signInResult)
                                                }
                                            }
                                        }
                                    )

                                    LaunchedEffect(key1 = state.isSignInSuccessful) {
                                        if (state.isSignInSuccessful) {
                                            Toast.makeText(
                                                applicationContext,
                                                "Sign in successful",
                                                Toast.LENGTH_LONG
                                            ).show()

                                            signInRegister()

                                            viewModel.resetState()

                                        }
                                    }

                                    SignInScreen(
                                        state = state,
                                        loading = loading,
                                        onGoogleSignInClick = {
                                            lifecycleScope.launch {
                                                val signInIntentSender = googleAuthUiClient.signIn()
                                                launcher.launch(
                                                    IntentSenderRequest.Builder(
                                                        signInIntentSender ?: return@launch
                                                    )
                                                        .build()
                                                )
                                            }
                                        },
                                        onGithubSignInClick = {
                                            githubAuth.signIn(
                                                context = context,
                                                onSuccess = {
                                                    signInRegister()
                                                    navController.navigate("home") {
                                                        popUpTo("sign_in") {
                                                            inclusive = true
                                                        }
                                                    }
                                                }
                                            )
                                        }
                                    )
                                }
                            }
                            if (loading) {
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                color = MaterialTheme.colorScheme.surfaceColorAtElevation(
                                                    2.dp
                                                ),
                                                shape = RoundedCornerShape(20.dp)
                                            )
                                            .padding(all = 20.dp)
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }
                            }

                        }
                    }


                }

            }

        }
    }


    @Composable
    private fun ColorTheme(content: @Composable () -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            MyWalletTheme { content() }
        else {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            AppTheme { content() }
        }
    }

    @SuppressLint("ServiceCast")
    private fun isInternetAvailable(context: Context): Boolean {
        val result: Boolean
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
        return result
    }


}
