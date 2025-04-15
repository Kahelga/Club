package com.example.club.feature.admin.reports.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight

import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

import androidx.compose.material3.Divider

import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.club.feature.admin.reports.R
import com.example.club.shared.report.domain.entity.ReportEvent
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries

@Composable
fun EventReportContent(
    reportEvent: List<ReportEvent>
) {
    // Извлечение прибыли и идентификаторов мероприятий из полученных данных
    val tickets = reportEvent.map { it.report.ticketSold.toFloat() }
    val total= tickets.sum().toInt()
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
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(text = stringResource(R.string.title_reportTicket), style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
                    BarChartTicket(tickets, eventIds, total)
                }
            }

        }
        // Отображение таблицы с параметрами
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(5.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(2.dp)) {
                    Text(text =stringResource(R.string.report), style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
                    ReportTable(reportEvent)
                }
            }
        }
    }

}

@Composable
fun BarChartTicket(tickets: List<Float>, eventIds: List<String>, total: Int) {
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            columnSeries {
                series(*tickets.toTypedArray())
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
        LegendTicket(eventIds,total)
    }
}


@Composable
fun LegendTicket(eventIds: List<String>, total: Int) {
   Column(modifier = Modifier.padding(8.dp)) {
       Text(
           text = stringResource(R.string.totalTicket,total),
           style = MaterialTheme.typography.bodyLarge,
           modifier = Modifier.padding(top = 8.dp)
       )
        eventIds.forEachIndexed { index, eventId ->
            LegendTicketItem(eventId, index)
        }
    }
}

@Composable
fun LegendTicketItem(eventId: String, index: Int) {
    Box(modifier = Modifier.padding(4.dp)) {
        Text(text = "$index - $eventId")
    }
}
@Composable
fun ReportTable(reportEvents: List<ReportEvent>) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Text(
                text = stringResource(R.string.table_eventId),
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text =stringResource(R.string.table_profit),
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = stringResource(R.string.table_ticketSold),
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = stringResource(R.string.table_expenses),
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text =stringResource(R.string.table_netProfit),
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Линия разделения после заголовков
        Divider(thickness = 2.dp, color = MaterialTheme.colorScheme.onSurface)

        // Данные таблицы
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
    ) {
        Text(text = reportEvent.eventId, modifier = Modifier
            .weight(1f)
            .padding(bottom = 5.dp))

        Text(
            text = reportEvent.report.profit.toString(),
            modifier = Modifier
                .weight(1f)
                .padding(5.dp)
        )

        Text(
            text = reportEvent.report.ticketSold.toString(),
            modifier = Modifier
                .weight(1f)
                .padding(5.dp)
        )

        Text(
            text = reportEvent.report.expenses.toString(),
            modifier = Modifier
                .weight(1f)
                .padding(5.dp)
        )

        Text(
            text = reportEvent.report.netProfit.toString(),
            modifier = Modifier
                .weight(1f)
                .padding(5.dp)
        )
    }

    // Линия разделения после каждой строки
    Divider(thickness = 2.dp, color = MaterialTheme.colorScheme.onSurface)
}