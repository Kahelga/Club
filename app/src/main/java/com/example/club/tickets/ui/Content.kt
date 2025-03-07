package com.example.club.tickets.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.club.R
import com.example.club.eventDetails.ui.formatTimeSelected
import com.example.club.poster.domain.entity.EventStatus
import com.example.club.poster.ui.formatDateSelected
import com.example.club.purchase.domain.entity.PurchaseStatus
import com.example.club.tickets.domain.entity.Order

@Composable
fun Content(
    orders: List<Order>
) {
    var selectedTab by remember { mutableStateOf(0) }

    Column {
        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text(stringResource(id = R.string.order_active)) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text(stringResource(id = R.string.order_archive)) }
            )
        }

        val filteredOrders = if (selectedTab == 0) {
            orders.filter { it.event.status == EventStatus.ACTIVE }
        } else {
            orders.filter { it.event.status == EventStatus.ARCHIVED }
        }

        LazyColumn(modifier = Modifier.fillMaxHeight()) {
            items(filteredOrders) { order ->
                OrderItem(
                    item = order
                )
            }
        }
    }
}

@Composable
private fun OrderItem(item: Order) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 30.dp, horizontal = 12.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = formatDateSelected(item.issueDate),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.TopStart)
                )
                Text(
                    text = formatTimeSelected(item.issueDate),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.TopEnd)
                )
            }

            Column(
                modifier = Modifier
                    .padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                EventTitle(eventName = item.event.title)
                Spacer(modifier = Modifier.height(8.dp))
                SeatList(seats = item.seats)
            }

            Box(modifier = Modifier.fillMaxWidth()) {
                StatusText(status = item.status, modifier = Modifier.align(Alignment.BottomStart))
                TicketIdText(
                    ticketId = item.ticketId,
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }
        }
    }
}


@Composable
private fun EventTitle(eventName: String) {
    Text(
        text = eventName,
        style = MaterialTheme.typography.titleSmall.copy(fontSize = 20.sp),
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun SeatList(seats: List<String>) {
    Text(
        text =stringResource(id = R.string.order_seats,seats.joinToString(", ")),
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
        style = MaterialTheme.typography.bodyLarge
    )
}


@Composable
private fun StatusText(status: PurchaseStatus, modifier: Modifier = Modifier) {
    val statusColor = when (status) {
        PurchaseStatus.PAID -> Color.Green.copy( 0.3f)
        PurchaseStatus.CANCELLED -> Color.Red.copy(0.3f)
        else -> Color.Gray
    }

    Box(
        modifier = modifier
            .background(color = statusColor, shape = RoundedCornerShape(50))
            .padding(horizontal = 8.dp, vertical = 4.dp),

    ) {
        Text(
            text = status.name,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun TicketIdText(ticketId: String, modifier: Modifier = Modifier) {
   Text(
        text= stringResource(id = R.string.order_idTicket,ticketId),
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier
    )
}