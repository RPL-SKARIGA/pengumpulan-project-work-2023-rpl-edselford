package com.edselmustapa.mywallet.lib

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edselmustapa.mywallet.config.rupiah
import com.edselmustapa.mywallet.service.Transaction
import com.edselmustapa.mywallet.service.TransferService
import com.edselmustapa.mywallet.service.Wallet
import com.edselmustapa.mywallet.service.WalletService
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entryOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import kotlin.math.round

class HomeViewModel : ViewModel() {
    private val walletService = WalletService()
    private val transferService = TransferService()
    private val user = Firebase.auth.currentUser

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _wallet = MutableStateFlow(Wallet(null, "", 0))
    val wallet = _wallet.asStateFlow()

    private val _transaction = MutableStateFlow(emptyList<Transaction>())
    val transaction = _transaction.asStateFlow()

    private val _income = MutableStateFlow(0L)
    val income = _income.asStateFlow()

    private val _expense = MutableStateFlow(0L)
    val expense = _expense.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        if (user != null) {
            viewModelScope.launch {
                _loading.value = true
                _wallet.value = try {
                    walletService.getWallet(user.email ?: "")
                } catch (e: Exception) {
                    e.printStackTrace()
                    Wallet("", "", 0)
                }
                _transaction.value = try {
                    transferService.transaction(user.email ?: "")
                } catch (e: Exception) {
                    e.printStackTrace()
                    emptyList()
                }

                transactionInfo("week")
                print(_transaction.value)
                delay(1000)
                _loading.value = false
            }
        }
    }

    fun transactionInfo(option: String) {
        val (expense, income) = _transaction.value.filter {
            when (option) {
                "month" -> Calendar.getInstance()
                    .apply { time = it.date }
                    .get(Calendar.YEAR) == Calendar.getInstance()
                    .get(Calendar.YEAR) &&
                        Calendar.getInstance().apply { time = it.date }
                            .get(Calendar.MONTH) == Calendar.getInstance()
                    .get(Calendar.MONTH)

                "week" -> Calendar.getInstance().apply { time = it.date }
                    .get(Calendar.WEEK_OF_YEAR) == Calendar.getInstance()
                    .get(Calendar.WEEK_OF_YEAR)

                else -> true
            }
        }.partition { it.is_sender }

        _expense.value = expense.fold(0) { initial, operation -> initial + operation.subtotal }
        _income.value = income.fold(0) { initial, operation -> initial + operation.subtotal }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")
    fun expenseGraph(): ChartModel {
        val (expense) = _transaction.value.partition { it.is_sender }

        val chartEntryModel = expense.mapIndexed { index, data -> entryOf(index, data.subtotal) }

        val dateFormat = SimpleDateFormat("d MMM")
        val timeFormat = SimpleDateFormat("HH:mm")
        val horizontalAxisValueFormatter =
            AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, _ ->
                try {
                    if (expense[value.toInt()].date.toInstant().atZone(ZoneId.systemDefault())
                            .toLocalDate().compareTo(LocalDate.now()) == 0
                    ) {
                        timeFormat.format(expense[value.toInt()].date)
                    } else
                        dateFormat.format(expense[value.toInt()].date)
                } catch(e: Exception) {
                    "-"
                }
            }

        val verticalAxisValueFormatter =
            AxisValueFormatter<AxisPosition.Vertical.Start> { value, _ ->
                rupiah(round(value / 100) * 100)
            }

        return ChartModel(
            model = ChartEntryModelProducer(chartEntryModel),
            axisX = horizontalAxisValueFormatter,
            axisY = verticalAxisValueFormatter
        )
    }
}

data class ChartModel(
    val model: ChartEntryModelProducer,
    val axisX: AxisValueFormatter<AxisPosition.Horizontal.Bottom>,
    val axisY: AxisValueFormatter<AxisPosition.Vertical.Start>
)