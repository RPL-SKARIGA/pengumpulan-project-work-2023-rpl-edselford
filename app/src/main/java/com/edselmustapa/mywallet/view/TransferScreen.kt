package com.edselmustapa.mywallet.view


import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.sharp.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavController
import com.edselmustapa.mywallet.config.rupiah
import com.edselmustapa.mywallet.lib.HomeViewModel
import com.edselmustapa.mywallet.lib.TransferViewModel
import com.edselmustapa.mywallet.service.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.floor
import androidx.compose.material3.ListItem as Lists

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint(
    "CoroutineCreationDuringComposition", "UnusedMaterial3ScaffoldPaddingParameter",
    "SuspiciousIndentation"
)
@Composable
fun TransferScreen(
    navController: NavController,
    viewModel: TransferViewModel,
    homeViewModel: HomeViewModel
) {
    var amount by remember { mutableStateOf("0") }
    var search by rememberSaveable { mutableStateOf("") }
    val listUsers by viewModel.userSearch.collectAsState()
    var selected by remember { mutableStateOf<User?>(null) }
    var message by remember { mutableStateOf("") }
    var openDialog by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }
    val loading by viewModel.loading.collectAsState()
    val user = Firebase.auth.currentUser
    val wallet by homeViewModel.wallet.collectAsState()

    val searchLoading by viewModel.searchLoading.collectAsState()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transfer") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val amount_double = try { amount.toDouble() } catch(e: Exception) {0.0}
                        if (floor(amount_double + amount_double / 100) > wallet.wallet) {
                            Toast.makeText(
                                context,
                                "Not enough money",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@IconButton
                        }
                        if (selected == null) {
                            Toast.makeText(
                                context,
                                "Reciever cannot be empty",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@IconButton
                        }
                        if (amount.toLong() == 0L) {
                            Toast.makeText(
                                context,
                                "Amount can't be zero",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@IconButton
                        }
                        openDialog = true
                    }) {
                        Icon(
                            imageVector = Icons.Sharp.Send,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        modifier = Modifier.clickable(
            indication = null,
            interactionSource = remember {
                MutableInteractionSource()
            }
        ) {
            focusManager.clearFocus()
        }
    ) {
        Column(
            modifier = Modifier
                .padding(top = 100.dp)
                .fillMaxSize()

        ) {
            Row(
                modifier = Modifier.padding(vertical = 20.dp, horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "Amount",
                    style = MaterialTheme.typography.bodyLarge
                        .copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text("Rp.")
                BasicTextField(
                    value = amount,
                    onValueChange = {
                        if (it.length <= Long.MAX_VALUE.toString().length - 2 && it.isDigitsOnly()) {
                            amount = it
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    textStyle = MaterialTheme.typography.bodyLarge
                        .copy(color = MaterialTheme.colorScheme.onBackground),
                    modifier = Modifier.fillMaxWidth(),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
                    visualTransformation = NumberCommaTransformation()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        it()
                        Text(
                            "Wallet: Rp." + DecimalFormat("#,###")
                                .format(wallet.wallet),
                            style = TextStyle(color = MaterialTheme.colorScheme.outline)
                        )
                    }
                }
            }
            Divider()
            Row(
                modifier = Modifier.padding(vertical = 20.dp, horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "To", style = MaterialTheme.typography.bodyLarge
                        .copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
                Spacer(modifier = Modifier.width(20.dp))
                BasicTextField(
                    value = selected?.name ?: search,
                    onValueChange = {
                        if (selected != null) {
                            if (it.contains(selected!!.name)) return@BasicTextField
                            selected = null
                        }
                        viewModel.search(it)
                        search = it
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    textStyle = MaterialTheme.typography.bodyLarge
                        .copy(color = MaterialTheme.colorScheme.onBackground),
                    modifier = Modifier.fillMaxWidth(),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        it()
                        if (selected != null) Text(
                            selected!!.email,
                            style = TextStyle(color = MaterialTheme.colorScheme.outline)
                        )
                    }
                }
            }
            if (search.isNotEmpty() && listUsers.isNotEmpty() && selected == null) Column(
                modifier = Modifier.fillMaxSize()
            ) {
                listUsers.forEach {
                    if (user != null) {
                        if (it.email == user.email) return@forEach
                    }
                    Lists(
                        modifier = Modifier.clickable {
                            selected = it
                            viewModel.search("")
                            focusManager.clearFocus()
                        },
                        headlineContent = { Text(it.name) },
                        supportingContent = { Text(it.email) }
                    )
                }
            }

            if (searchLoading) {
                Lists(
                    headlineContent = { Text("Loading") },
                    supportingContent = { Text("Please wait..") },
                    leadingContent = {
                        CircularProgressIndicator()
                    }
                )
            }
            Divider()
            Column(modifier = Modifier.padding(20.dp)) {
                Text(text = "Payment Detail", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp)
                ) {
                    Text(text = "Total Transaction")
                    Text(text = rupiah(amount))
                }
                val amount_double = try { amount.toDouble() } catch(e: Exception) {0.0}
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp)
                ) {
                    Text(text = "Fee")
                    Text(text = rupiah(floor(amount_double / 100)))
                }
                Divider()
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp)
                ) {
                    Text(text = "Subtotal")
                    Text(text = rupiah(floor(amount_double + amount_double / 100)))
                }

            }
        }


    }
    val shimmerInstance = rememberShimmer(shimmerBounds = ShimmerBounds.Window)

    if (openDialog) {
        AlertDialog(
            onDismissRequest = { /*TODO*/ },
            confirmButton = {
                TextButton(
                    modifier = if (loading) Modifier.shimmer(shimmerInstance) else Modifier,
                    colors = if (loading)
                        ButtonDefaults.textButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    else ButtonDefaults.textButtonColors(),
                    enabled = !loading,
                    onClick = {
                        selected?.let {
                            if (user != null) {
                                user.email?.let { email ->
                                    viewModel.transfer(
                                        email = email,
                                        receipentEmail = it.email,
                                        total = amount.toLong(),
                                        callback = {
                                            homeViewModel.refresh()
                                            navController.popBackStack()
                                        }
                                    )
                                }
                            }
                        }
                    }) {

                    Text("Transfer")

                }
            },
            dismissButton = {
                TextButton(onClick = { openDialog = false }, enabled = !loading) {
                    Text("Cancel")
                }
            },
            title = { Text("Confirm Transfer") },
            text = {
                Text("Are you sure to transfer?")
            }
        )
    }
}

class NumberCommaTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            text = AnnotatedString(text.text.toLongOrNull().formatWithComma()),
            offsetMapping = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    return text.text.toLongOrNull().formatWithComma().length
                }

                override fun transformedToOriginal(offset: Int): Int {
                    return text.length
                }
            }
        )
    }
}

fun Long?.formatWithComma(): String =
    NumberFormat.getNumberInstance(Locale.US).format(this ?: 0)