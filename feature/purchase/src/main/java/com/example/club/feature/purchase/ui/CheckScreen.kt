package com.example.club.feature.purchase.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.example.club.feature.purchase.R
import com.example.club.shared.event.domain.entity.AgeRatings
import com.example.club.shared.event.domain.entity.EventDetails
import com.example.club.shared.tickets.domain.entity.Order
import com.example.club.util.formatting.formatDateSelected
import com.example.club.util.formatting.formatTimeSelected

@Composable
fun CheckScreen(
    order: Order,
    onPosterScreen: () -> Unit,
    onOrderScreen: () -> Unit
) {
    Popup(alignment = Alignment.Center) {
        Box(
            modifier = Modifier
                // .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.medium
                )
                .border(BorderStroke(2.dp,MaterialTheme.colorScheme.outline))
                .verticalScroll(rememberScrollState()),

        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularIcon {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = stringResource(id = R.string.purchase_dialog),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                }
                EventTitle(order)
                EventDateTime(order)
                SeatList(order)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    contentAlignment = Alignment.Center
                ){
                    Button(onClick = onPosterScreen, modifier = Modifier.padding(bottom = 10.dp)) {
                        Text(stringResource(R.string.button_poster))
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    contentAlignment = Alignment.Center
                ){
                    Button(onClick = onOrderScreen, modifier = Modifier.padding(bottom = 10.dp)) {
                        Text(stringResource(R.string.button_order))
                    }
                }

            }

        }
    }
}
@Composable
private fun EventTitle(order: Order) {
    val ageRatingText = when (order.event.ageRating) {
        AgeRatings.G -> "Для всех"
        AgeRatings.PG -> "С родительским контролем"
        AgeRatings.PG13 -> "13+"
        AgeRatings.R -> "16+"
        AgeRatings.NC17 -> "18+"
    }
    Column(
        modifier = Modifier
            .padding(10.dp)
    ) {
        Text(
            text = stringResource(R.string.title_event),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(top = 4.dp, start = 4.dp)
        )
        Text(
            text = buildAnnotatedString {
                append(order.event.title)
                append(" (${ageRatingText})")
            },
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 4.dp, start = 4.dp)
        )
    }

}
@Composable
private fun EventDateTime(order: Order) {
    Column(
        modifier = Modifier
            .padding(10.dp)
    ) {
        Text(
            text = stringResource(R.string.event_dataTime),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(start = 4.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 4.dp)
        ) {

            Text(
                text = formatDateSelected(order.event.date),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 4.dp)
            )
            Text(
                text = formatTimeSelected(order.event.date),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }

}
@Composable
private fun SeatList(order: Order) {
    Column(
        modifier = Modifier
            .padding(10.dp)
    ) {
        Text(
            text = stringResource(R.string.seats),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(start = 4.dp)
        )
        Text(
            text = order.seats.joinToString(", "),
            modifier = Modifier.padding(start = 4.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }

}
@Composable
private fun CircularIcon(icon: @Composable () -> Unit) {
    Surface(
        modifier = Modifier.size(55.dp),
        shape = MaterialTheme.shapes.extraLarge,
        color = Color.Green.copy(0.3f),
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        Box(
            modifier = Modifier.size(48.dp),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }
    }
}