package com.example.club.feature.admin.reports.ui

import android.annotation.SuppressLint
import android.icu.text.NumberFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.club.feature.admin.reports.R
import com.example.club.shared.report.domain.entity.ReportEvent
import com.example.club.shared.report.domain.entity.ReportPeriod
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.common.copyColor
import ir.mahozad.android.PieChart


@Composable
fun PeriodReportContent(
    reportPeriod: ReportPeriod,
    reportEvent: List<ReportEvent>
) {
    // Извлечение прибыли и идентификаторов мероприятий из полученных данных
    val profits = reportEvent.map { it.report.profit.toFloat() }
    val totalIncome= profits.sum()
    val eventIds = reportEvent.map { it.eventId }

    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
    ) {

        item {
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                shape = RoundedCornerShape(5.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(2.dp)) {
                    Text(text = stringResource(R.string.tabRow_0), style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
                    ReportRow(reportPeriod)
                }
            }
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                shape = RoundedCornerShape(5.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = stringResource(R.string.title_reportPie),
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    PieChartCompose(
                        expenses = reportPeriod.report.expenses.toFloat(),
                        netProfit = reportPeriod.report.netProfit.toFloat(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .padding(16.dp)
                    )
                }
            }
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 30.dp),
                shape = RoundedCornerShape(5.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(text = stringResource(R.string.title_reportE), style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
                    BarChart(profits, eventIds, totalIncome)
                }
            }

        }

    }

}

@Composable
fun ReportRow(period: ReportPeriod) {
    Column(modifier = Modifier.padding(2.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            // Создаем карточки для каждого параметра
            val reportValues = listOf(
                Pair(stringResource(R.string.table_profit), period.report.profit),
                Pair(stringResource(R.string.table_ticketSold), period.report.ticketSold),
                Pair(stringResource(R.string.table_expenses), period.report.expenses),
                Pair(stringResource(R.string.table_netProfit), period.report.netProfit)
            )

            reportValues.forEach { (label, value) ->
                val backgroundColor = when {
                    label == stringResource(R.string.table_profit)  -> Color.Blue
                    label == stringResource(R.string.table_ticketSold) -> Color(0xFFFFA500) // Оранжевый
                    label == stringResource(R.string.table_expenses) -> Color.Red
                    label == stringResource(R.string.table_netProfit)  -> Color.Green
                    else -> Color.Gray
                }

                Card(
                    modifier = Modifier
                        .weight(4f)
                        .padding(4.dp)
                        //.height(100.dp)
                        .background(backgroundColor.copy(alpha = 0.3f))

                ) {
                    Box(modifier = Modifier.fillMaxSize().background(backgroundColor.copy(alpha = 0.3f)), contentAlignment = Alignment.Center) {
                        Text(
                            text = "$label: $value",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(8.dp),
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun BarChart(profits: List<Float>, eventIds: List<String>, totalIncome: Float) {
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            columnSeries {
                series(*profits.toTypedArray())
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .padding(top = 20.dp)
    ) {
        CartesianChartHost(
            chart = rememberCartesianChart(
                rememberColumnCartesianLayer(),
                startAxis = VerticalAxis.rememberStart(),
                bottomAxis = HorizontalAxis.rememberBottom(),
            ),
            modelProducer = modelProducer,
        )
        Legend(eventIds,totalIncome)
    }
}


@Composable
fun Legend(eventIds: List<String>, totalIncome: Float) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            text = stringResource(R.string.totalIncome,totalIncome),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 8.dp)
        )
        eventIds.forEachIndexed { index, eventId ->
            LegendItem(eventId, index)
        }
    }
}

@Composable
fun LegendItem(eventId: String, index: Int) {
    Box(modifier = Modifier.padding(4.dp)) {
        Text(text = "$index - $eventId")
    }
}
@Composable
fun PieChartCompose(
    expenses: Float,
    netProfit: Float,
    modifier: Modifier = Modifier
) {
    val total = expenses + netProfit
    if (total == 0f) return

    // Проценты для каждой части
    val expenseFraction = expenses / total
    val profitFraction = netProfit / total

    val strExpense=stringResource(id = R.string.table_expenses)
    val strProfit=stringResource(id = R.string.table_netProfit)

    AndroidView(
        modifier = modifier,
        factory = { context ->
            PieChart(context).apply {
                // Задаем срезы: расходы и чистая прибыль
                slices = listOf(
                    PieChart.Slice(
                        fraction = expenseFraction,
                        legend = strExpense,
                        color = Color.Red.copy(alpha = 0.5f).toArgb(),
                        label = NumberFormat.getPercentInstance().format(expenseFraction) // Отображение процентов
                    ),
                    PieChart.Slice(
                        fraction = profitFraction,
                        legend = strProfit,
                        color = Color.Green.copy(alpha = 0.5f).toArgb(),
                        label = NumberFormat.getPercentInstance().format(profitFraction) // Отображение процентов
                    )
                )
                isLegendEnabled = true

            }
        }
    )
}