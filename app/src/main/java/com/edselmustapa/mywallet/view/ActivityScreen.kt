package com.edselmustapa.mywallet.view

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.edselmustapa.mywallet.config.rupiah
import com.edselmustapa.mywallet.lib.ActivityViewModel
import com.edselmustapa.mywallet.lib.HomeViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.extension.round
import java.text.SimpleDateFormat
import java.time.LocalDate
import kotlin.math.round

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ActivityScreen(
    viewModel: ActivityViewModel,
    homeViewModel: HomeViewModel,
    navController: NavController
) {
    val activities by viewModel.activities.collectAsState()


    BottomSheetScaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Activity") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                    }
                })
        },
        scaffoldState = rememberBottomSheetScaffoldState(),
        sheetPeekHeight = 550.dp,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                activities.forEachIndexed { index, it ->
                    ListItem(
                        modifier = Modifier
                            .padding(horizontal = 5.dp, vertical = 1.dp)
                            .clip(
                                when (index) {
                                    0 -> RoundedCornerShape(
                                        topStartPercent = 20,
                                        topEndPercent = 20
                                    )

                                    activities.lastIndex -> RoundedCornerShape(
                                        bottomStartPercent = 20,
                                        bottomEndPercent = 20
                                    )

                                    else -> RoundedCornerShape(5)
                                }
                            ),
                        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp) ),
                        headlineContent = { Text(SimpleDateFormat("dd MMM yyyy").format(it.date)) },
                        supportingContent = { Text(text = it.activity) },
                        trailingContent = { Text(SimpleDateFormat("HH:mm").format(it.date)) }

                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            val graph = homeViewModel.expenseGraph()
            Chart(
                chart = lineChart(),
                chartModelProducer = graph.model,
                startAxis = rememberStartAxis(
                    valueFormatter = graph.axisY
                ),
                bottomAxis = rememberBottomAxis(
                    valueFormatter = graph.axisX
                )
            )

        }
    }

}