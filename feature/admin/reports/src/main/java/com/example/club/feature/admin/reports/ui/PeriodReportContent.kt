package com.example.club.feature.admin.reports.ui

import android.annotation.SuppressLint
import android.icu.text.NumberFormat
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.club.feature.admin.reports.R
import com.example.club.shared.report.domain.entity.ReportEvent
import com.example.club.shared.report.domain.entity.ReportPeriod
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisGuidelineComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import ir.mahozad.android.PieChart


@Composable
fun PeriodReportContent(
    reportPeriod: ReportPeriod,
    reportEvent: List<ReportEvent>
) {

    val profits = reportEvent.map { it.report.profit.toFloat() }
    val totalIncome = profits.sum()
    val eventIds = reportEvent.map { it.eventId }

    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
    ) {

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(5.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(2.dp),) {
                    Text(
                        text = stringResource(R.string.tabRow_0),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    ReportRow(reportPeriod)
                }
            }
        }
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(5.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = stringResource(R.string.title_reportPie),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    PieChart(
                        expenses = reportPeriod.report.expenses.toFloat(),
                        netProfit = reportPeriod.report.netProfit.toFloat(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .padding(16.dp)
                          //  .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f))

                    )
                }
            }
        }
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp),
                shape = RoundedCornerShape(5.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {

                BarChart(profits, eventIds, totalIncome)

            }

        }

    }

}

@SuppressLint("ResourceAsColor")
@Composable
fun ReportRow(period: ReportPeriod) {
    Column(modifier = Modifier.padding(2.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            // .horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            val reportValues = listOf(
                Triple(
                    stringResource(R.string.table_profit),
                    period.report.profit,
                    Icons.Filled.MonetizationOn
                ),
                Triple(
                    stringResource(R.string.table_ticketSold),
                    period.report.ticketSold,
                    Icons.Filled.ConfirmationNumber
                ),
                Triple(
                    stringResource(R.string.table_expenses),
                    period.report.expenses,
                    Icons.Filled.MoneyOff
                ),
                Triple(
                    stringResource(R.string.table_netProfit),
                    period.report.netProfit,
                    Icons.Filled.Savings
                )
            )

            reportValues.forEach { (label, value, icon) ->
                val backgroundColor = when (label) {
                    stringResource(R.string.table_profit) -> Color.Blue
                    stringResource(R.string.table_ticketSold) -> Color(0xFFFFA500)
                    stringResource(R.string.table_expenses) -> Color.Red
                    stringResource(R.string.table_netProfit) -> Color.Green
                    else -> Color.Gray
                }

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                        .background(backgroundColor.copy(alpha = 0.3f)),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(backgroundColor.copy(alpha = 0.3f))
                            .padding(2.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = label,
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.outline
                            )
                            Text(
                                text = label,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                        Text(
                            text = value.toString(),
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            // textAlign = TextAlign.Right
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
    var isLegendVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            columnSeries() {
                series(*profits.toTypedArray())

            }
        }
    }
    Column(modifier = Modifier.padding(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.title_reportE),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
            IconButton(
                onClick = { isLegendVisible = !isLegendVisible },
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    imageVector = if (isLegendVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    contentDescription = null
                )
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
                    rememberColumnCartesianLayer(
                    ),
                    startAxis = VerticalAxis.rememberStart(guideline = rememberAxisGuidelineComponent(fill = fill(MaterialTheme.colorScheme.outline))),
                    bottomAxis = HorizontalAxis.rememberBottom(guideline = rememberAxisGuidelineComponent(fill = fill(MaterialTheme.colorScheme.outline))),
                ),
                modelProducer = modelProducer,
                modifier = Modifier
                    .fillMaxWidth()
                //  .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f))
            )

            if (isLegendVisible) {
                Legend(eventIds, totalIncome)
            }
        }

    }


}


@Composable
fun Legend(eventIds: List<String>, totalIncome: Float) {
    Card(
        modifier = Modifier
            .padding(2.dp)
            .padding(top = 8.dp)
            .height(300.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(3.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.2f)
        )
    ) {
        Text(
            text = stringResource(R.string.totalIncome, totalIncome),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .padding(top = 8.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
            //  .verticalScroll(rememberScrollState())
        )
        eventIds.forEachIndexed { index, eventId ->
            LegendItem(eventId, index)
        }
    }
}

@Composable
fun LegendItem(eventId: String, index: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)


    ) {
        Text(text = "$index - $eventId")
    }
}

@Composable
fun PieChart(
    expenses: Float,
    netProfit: Float,
    modifier: Modifier = Modifier
) {
    val total = expenses + netProfit
    if (total == 0f) return


    val expenseFraction = expenses / total
    val profitFraction = netProfit / total

    val strExpense = stringResource(id = R.string.table_expenses)
    val strProfit = stringResource(id = R.string.table_netProfit)

    AndroidView(
        modifier = modifier,
        factory = { context ->
            PieChart(context).apply {
                slices = listOf(
                    PieChart.Slice(
                        fraction = expenseFraction,
                        legend = strExpense,
                        color = Color.Red.copy(alpha = 0.5f).toArgb(),
                        label = NumberFormat.getPercentInstance().format(expenseFraction)
                    ),
                    PieChart.Slice(
                        fraction = profitFraction,
                        legend = strProfit,
                        color = Color.Green.copy(alpha = 0.5f).toArgb(),
                        label = NumberFormat.getPercentInstance().format(profitFraction)
                    )
                )
                isLegendEnabled = true

            }
        }
    )
}