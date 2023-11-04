package com.edselmustapa.mywallet.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.edselmustapa.mywallet.lib.DiscussionViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CreateDiscussionScreen(
    navController: NavController,
    viewModel: DiscussionViewModel
) {

    var topic by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    val sendLoading by viewModel.sendLoading.collectAsState()

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Discussion") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        Firebase.auth.currentUser?.email?.let {
                            viewModel.createDiscussion(
                                topic,
                                content,
                                it
                            ) {
                                navController.popBackStack()
                            }
                        }
                    }) {
                        if (sendLoading) CircularProgressIndicator(
                            modifier = Modifier
                                .padding(10.dp)
                                .then(Modifier.size(28.dp))
                        ) else
                            Icon(imageVector = Icons.Default.Send, contentDescription = "")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding),
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Topic",
                    style = MaterialTheme.typography.bodyLarge
                        .copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
                Spacer(modifier = Modifier.width(20.dp))
                BasicTextField(
                    value = topic,
                    onValueChange = { topic = it },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    textStyle = MaterialTheme.typography.bodyLarge
                        .copy(color = MaterialTheme.colorScheme.onBackground),
                    modifier = Modifier.fillMaxWidth(),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground)
                )
            }
            Divider()
            Box(modifier = Modifier.padding(20.dp)) {
                BasicTextField(
                    value = content,
                    onValueChange = { content = it },
                    singleLine = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    textStyle = MaterialTheme.typography.bodyLarge
                        .copy(color = MaterialTheme.colorScheme.onBackground),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
                    decorationBox = {
                        if (content.isEmpty()) Text(
                            "Write content",
                            style = MaterialTheme.typography.bodyLarge
                                .copy(color = MaterialTheme.colorScheme.outline)
                        )
                        it()
                    }
                )
            }
        }
    }
}