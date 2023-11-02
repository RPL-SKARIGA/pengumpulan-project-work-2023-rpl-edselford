package com.edselmustapa.mywallet.view

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem as Lists
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.edselmustapa.mywallet.lib.DiscussionViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dev.jeziellago.compose.markdowntext.MarkdownText
import java.lang.Exception
import java.text.SimpleDateFormat

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavController,
    viewModel: DiscussionViewModel,
    discussionId: String
) {
    val discussions by viewModel.discussion.collectAsState()
    val discuss = discussions.find { it._id == discussionId }
    val answer by viewModel.answers.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val sendLoading by viewModel.sendLoading.collectAsState()

    val scrollState = rememberScrollState()
    val user = Firebase.auth.currentUser

    LaunchedEffect(Unit) {
        viewModel.getAnswer(discussionId)

        if (user == null) {
            throw Exception("user not found")
        }
    }

    if (discuss == null) {
        Surface {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Text("Not Found")
                Button(onClick = { navController.popBackStack() }) {
                    Text("go back")
                }
            }
        }
        return
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        topBar = {
            LargeTopAppBar(
                title = { Text(discuss.topic) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                    }
                }

            )
        },
        bottomBar = {
            var answerInput by remember { mutableStateOf("") }
            BasicTextField(
                modifier = Modifier.imePadding(),
                value = answerInput,
                onValueChange = { answerInput = it },
                cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground)
            ) {
                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.surfaceColorAtElevation(
                                20.dp
                            ), shape = RoundedCornerShape(100)
                        )
                        .padding(start = 20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val animateEmpty by
                        animateFloatAsState(
                            targetValue = if (answerInput.isEmpty()) 0.0f else 1.0f,
                            label = ""
                        )
                        Box {
                            it()
                            if (answerInput.isEmpty()) Text(
                                "Ketik pesan",
                                style = MaterialTheme.typography.bodyLarge
                                    .copy(color = MaterialTheme.colorScheme.outline)
                            )
                        }
                        if (sendLoading) CircularProgressIndicator(
                            modifier = Modifier
                                .padding(10.dp)
                                .then(Modifier.size(28.dp))
                        )
                        else IconButton(
                            enabled = answerInput.isNotEmpty(),
                            onClick = {
                                viewModel.sendAnswer(
                                    discussionId,
                                    user?.displayName ?: "",
                                    answerInput,
                                    user?.email ?: "",
                                    callback = {
                                        scrollState.scrollTo(scrollState.maxValue)
                                        answerInput = ""
                                        println("callback running")
                                    }
                                )

                            },
                            modifier = Modifier.alpha(animateEmpty)
                        ) {
                            Icon(imageVector = Icons.Default.Send, contentDescription = "")
                        }
                    }
                }
            }
        }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier
                .padding(it)
                .verticalScroll(scrollState)
        ) {
            Text(
                discuss.asked,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            MarkdownText(
                markdown = discuss.content,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Divider(modifier = Modifier.padding(vertical = 20.dp))

            if (loading)

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(20.dp)
                ) {
                    CircularProgressIndicator(modifier = Modifier.then(Modifier.size(32.dp)))
                    Text("loading ...")
                }
            else
                answer.forEach {
                    Lists(
                        headlineContent = { Text(it.owner) },
                        supportingContent = {
                            MarkdownText(
                                markdown = it.content,
                                style = MaterialTheme.typography.bodyMedium
                                    .copy(color = MaterialTheme.colorScheme.onBackground),
                                modifier = Modifier.padding(top = 10.dp)
                            )
                        },
                        trailingContent = { Text(SimpleDateFormat("hh:mm").format(it.dateAnswered)) }
                    )
                }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}