package com.example.club.feature.admin.reports.ui

import androidx.compose.foundation.background
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.club.feature.admin.reports.R
import com.example.club.shared.report.domain.entity.ReportUser
@Composable
fun UserReportContent(
    reportUser: List<ReportUser>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(5.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(2.dp)) {
                    Text(text =stringResource(R.string.report), style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
                    ReportUserTable(reportUser)
                }
            }
        }
    }

}


@Composable
fun ReportUserTable(reportUsers: List<ReportUser>) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Text(
                text = stringResource(R.string.table_userId),
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
                style = MaterialTheme.typography.bodyMedium
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

        // Линия разделения после заголовков
        Divider(thickness = 2.dp, color = MaterialTheme.colorScheme.onSurface)

        // Данные таблицы
        reportUsers.forEach { reportUser ->
            ReportUserRow(reportUser)
        }
    }
}

@Composable
fun ReportUserRow(reportUser: ReportUser) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(text = reportUser.userId, modifier = Modifier
            .weight(1f)
            .padding(bottom = 5.dp))

        Text(
            text = reportUser.report.profit.toString(),
            modifier = Modifier
                .weight(1f)
                .padding(5.dp)
        )

        Text(
            text = reportUser.report.ticketSold.toString(),
            modifier = Modifier
                .weight(1f)
                .padding(5.dp)
        )

        Text(
            text = reportUser.report.expenses.toString(),
            modifier = Modifier
                .weight(1f)
                .padding(5.dp)
        )

        Text(
            text = reportUser.report.netProfit.toString(),
            modifier = Modifier
                .weight(1f)
                .padding(5.dp)
        )
    }

    // Линия разделения после каждой строки
    Divider(thickness = 2.dp, color = MaterialTheme.colorScheme.onSurface)
}