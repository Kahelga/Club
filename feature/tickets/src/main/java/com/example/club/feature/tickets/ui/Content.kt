package com.example.club.feature.tickets.ui

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.club.feature.tickets.R
import com.example.club.shared.event.domain.entity.EventStatus

import com.example.club.shared.tickets.domain.entity.PurchaseStatus
import com.example.club.shared.tickets.domain.entity.Order
import com.example.club.util.formatting.formatDateSelected
import com.example.club.util.formatting.formatTimeSelected


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
                    item = order,
                    showReturnButton = selectedTab == 0
                )
            }
        }
    }
}

@Composable
private fun OrderItem(item: Order, showReturnButton: Boolean) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 30.dp, horizontal = 12.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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
                modifier = Modifier.padding(vertical = 24.dp),
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

            if (showReturnButton) {
                ReturnButton(onClick = { showDialog = true })
            }
        }
    }

    if (showDialog) {
        ConfirmationDialog(
            onConfirm = {
                // Логика возврата билета

                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
}

@Composable
fun ConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularIcon {
                    Icon(
                        Icons.Default.QuestionMark,
                        contentDescription = null
                    )
                }
            }
        },
        text = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(id = R.string.confirmation_message),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        },
        confirmButton = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    onClick = onConfirm
                ) {
                    Text(
                        text = stringResource(id = R.string.button_return_d),
                        style = MaterialTheme.typography.bodyLarge

                    )
                }
            }
        },
        dismissButton = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.inverseSurface)
                ) {
                    Text(
                        text = stringResource(id = R.string.button_cancel),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    )
}

@Composable
fun CircularIcon(icon: @Composable () -> Unit) {
    Surface(
        modifier = Modifier.size(55.dp),
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.primary,
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
        text = stringResource(id = R.string.order_seats, seats.joinToString(", ")),
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
        style = MaterialTheme.typography.bodyLarge
    )
}


@Composable
private fun StatusText(status: PurchaseStatus, modifier: Modifier = Modifier) {
    val statusColor = when (status) {
        PurchaseStatus.PAID -> Color.Green.copy(0.3f)
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
        text = stringResource(id = R.string.order_idTicket, ticketId),
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier
    )
}

@Composable
fun ReturnButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
    ) {
        Text(stringResource(id = R.string.button_return))
    }
}