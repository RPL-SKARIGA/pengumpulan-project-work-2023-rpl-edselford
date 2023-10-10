package com.edselmustapa.mywallet.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.edselmustapa.mywallet.auth.GoogleAuthUiClient
import com.edselmustapa.mywallet.auth.UserData

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    onLogoutClick: () -> Unit,
    userData: UserData?
) {
    Scaffold {
        Column {
            Text("Home, sweet home!")
            Button(onClick = onLogoutClick) {
                Text("logout")
            }
        }
    }
}