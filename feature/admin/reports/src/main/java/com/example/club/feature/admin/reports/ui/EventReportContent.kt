package com.example.club.feature.admin.reports.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight

import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

import androidx.compose.material3.Divider
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.club.feature.admin.reports.R
import com.example.club.shared.report.domain.entity.ReportEvent
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

@Composable
fun EventReportContent(
    reportEvent: List<ReportEvent>
) {

    val tickets = reportEvent.map { it.report.ticketSold.toFloat() }
    val total = tickets.sum().toInt()
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

                    BarChartTicket(tickets, eventIds, total)

            }

        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(5.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = stringResource(R.string.report),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    ReportTable(reportEvent)
                }
            }
        }
    }

}

@Composable
fun BarChartTicket(tickets: List<Float>, eventIds: List<String>, total: Int) {
    val modelProducer = remember { CartesianChartModelProducer() }
    var isLegendVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            columnSeries {
                series(*tickets.toTypedArray())
            }
        }
    }
    Column(
        modifier = Modifier
            .padding(10.dp)
    ) {
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
                    contentDescription = if (isLegendVisible) "Скрыть легенду" else "Показать легенду"
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
                    rememberColumnCartesianLayer(),
                    startAxis = VerticalAxis.rememberStart(guideline = rememberAxisGuidelineComponent(fill = fill(MaterialTheme.colorScheme.outline))),
                    bottomAxis = HorizontalAxis.rememberBottom(guideline = rememberAxisGuidelineComponent(fill = fill(MaterialTheme.colorScheme.outline))),
                ),
                modelProducer = modelProducer,
                modifier = Modifier
                    .fillMaxWidth()
                    //.background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f))
            )
            if (isLegendVisible) {
                LegendTicket(eventIds, total)
            }

        }

    }


}


@Composable
fun LegendTicket(eventIds: List<String>, total: Int) {
    Card( modifier = Modifier
        .padding(2.dp)
        .padding(top=8.dp)
        .fillMaxWidth(),
        shape = RoundedCornerShape(3.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.2f)
        )
    ) {
        Text(
            text = stringResource(R.string.totalTicket, total),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .padding(top = 8.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
        eventIds.forEachIndexed { index, eventId ->
            LegendTicketItem(eventId, index)
        }
    }
}

@Composable
fun LegendTicketItem(eventId: String, index: Int) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(4.dp)
        .background(MaterialTheme.colorScheme.surfaceVariant)) {
        Text(text = "$index - $eventId")
    }
}

@Composable
fun ReportTable(reportEvents: List<ReportEvent>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .padding(top=16.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.5f))
        ) {
            Text(
                text = stringResource(R.string.table_eventId),
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = stringResource(R.string.table_profit),
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = stringResource(R.string.table_ticketSold),
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = stringResource(R.string.table_expenses),
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = stringResource(R.string.table_netProfit),
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium
            )
        }


        Divider(thickness = 2.dp, color = MaterialTheme.colorScheme.onSurface)


        reportEvents.forEach { reportEvent ->
            ReportRow(reportEvent)
        }
    }
}

@Composable
fun ReportRow(reportEvent: ReportEvent) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.4f))
    ) {
        Text(
            text = reportEvent.eventId, modifier = Modifier
                .weight(1f)
                .padding(bottom = 5.dp)
        )

        Text(
            text = reportEvent.report.profit.toString(),
            modifier = Modifier
                .weight(1f)
                .padding(2.dp)
        )

        Text(
            text = reportEvent.report.ticketSold.toString(),
            modifier = Modifier
                .weight(1f)
                .padding(2.dp)
        )

        Text(
            text = reportEvent.report.expenses.toString(),
            modifier = Modifier
                .weight(1f)
                .padding(2.dp)
        )

        Text(
            text = reportEvent.report.netProfit.toString(),
            modifier = Modifier
                .weight(1f)
                .padding(2.dp)
        )
    }


    Divider(thickness = 2.dp, color = MaterialTheme.colorScheme.onSurface)
}