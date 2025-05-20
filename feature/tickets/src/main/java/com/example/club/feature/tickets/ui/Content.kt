package com.example.club.feature.tickets.ui

import android.graphics.Bitmap
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
import androidx.compose.ui.graphics.asImageBitmap
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
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import android.graphics.Color.BLACK
import android.graphics.Color.WHITE
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.example.club.feature.tickets.presentation.CancelOrderState
import com.example.club.feature.tickets.presentation.CancelOrderViewModel
import com.example.club.feature.tickets.presentation.OrderState
import com.example.club.shared.tickets.domain.entity.CancelOrderRequest
import com.example.club.feature.tickets.ui.Error

@Composable
fun Content(
    cancelOrderViewModel: CancelOrderViewModel,
    orders: List<Order>,
    onBuy: (bookedId: String) -> Unit,
    onPosterScreen: () -> Unit,
    onOrderScreen: () -> Unit
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
                    cancelOrderViewModel = cancelOrderViewModel,
                    showReturnButton = order.status.name == "PAID",//selectedTab == 0,
                    onBuy = { onBuy(order.bookingId) },
                    onPosterScreen,
                    onOrderScreen
                )
            }
        }
    }
}

@Composable
private fun OrderItem(
    item: Order,
    cancelOrderViewModel: CancelOrderViewModel,
    showReturnButton: Boolean,
    onBuy: () -> Unit,
    onPosterScreen: () -> Unit,
    onOrderScreen: () -> Unit
) {

    var showDialog by remember { mutableStateOf(false) }
    var showCancel by remember { mutableStateOf(false) }
    var showQRCodeDialog by remember { mutableStateOf(false) }
    val cancelOrderState by cancelOrderViewModel.state.collectAsState()
    val cancelOrderRequest = CancelOrderRequest(item.ticketId)
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val error = stringResource(id = R.string.error_unknown_error)

    val qrCodeBitmap = generateQRCode(item.ticketCode)

    LaunchedEffect(cancelOrderState) {
        when (cancelOrderState) {
            is CancelOrderState.Failure -> {
                showErrorDialog = true
                errorMessage = (cancelOrderState as CancelOrderState.Failure).message ?: error
            }

            is CancelOrderState.Success -> {
                if (showErrorDialog) {
                    showErrorDialog = false
                }
            }

            else -> {}
        }
    }
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatDateSelected(item.issueDate),
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = formatTimeSelected(item.issueDate),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

            Column(
                modifier = Modifier.padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                EventTitle(eventName = item.event.title)
                SeatList(seats = item.seats)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatusText(status = item.status)
                TicketIdText(ticketId = item.ticketId)
            }

            if (showReturnButton) {
                ReturnButton { showDialog = true }
                QRButton { showQRCodeDialog = true }
            } else {
                if(item.status.name == "PENDING"){
                    BuyButton { onBuy() }
                }

            }
        }
    }

    if (showDialog) {
        ConfirmationDialog(
            onConfirm = {
                cancelOrderViewModel.cancelTicket(cancelOrderRequest)
                showDialog = false
                showCancel=true
            },
            onDismiss = { showDialog = false }
        )
    }
    if (showQRCodeDialog) {
        QRCodeDialog(
            qrCodeBitmap = qrCodeBitmap,
          //  ticketCode=item.ticketCode,
            onDismiss = { showQRCodeDialog = false }
        )
    }
    if(showCancel){
        when (val state = cancelOrderState) {
            is CancelOrderState.Initial,
            is CancelOrderState.Loading,
            is CancelOrderState.Failure->{}
            is CancelOrderState.Success -> {
                CancelScreen(state.response,onPosterScreen, onOrderScreen)
            }
        }
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
private fun CircularIcon(icon: @Composable () -> Unit) {
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
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
private fun StatusText(status: PurchaseStatus) {
    val statusColor = when (status) {
        PurchaseStatus.PAID -> Color.Green.copy(0.3f)
        PurchaseStatus.CANCELLED -> Color.Red.copy(0.3f)
        else -> Color.Gray
    }
    val statusText = when (status) {
        PurchaseStatus.PENDING -> stringResource(R.string.status_pending)
        //   PurchaseStatus.CONFIRMED -> stringResource(R.string.status_confirmed)
        PurchaseStatus.PAID -> stringResource(R.string.status_paid)
        PurchaseStatus.CANCELLED -> stringResource(R.string.status_cancelled)
        else->{""}
    }

    Box(
        modifier = Modifier
            .background(color = statusColor, shape = RoundedCornerShape(50))
            .padding(horizontal = 8.dp, vertical = 4.dp),

        ) {
        Text(
            text = statusText,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun TicketIdText(ticketId: String) {
    Text(
        text = stringResource(id = R.string.order_idTicket, ticketId),
        style = MaterialTheme.typography.bodyMedium
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

@Composable
fun QRButton(onClick: () -> Unit) {

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp),
    ) {
        Text(stringResource(id = R.string.button_show_qr))
    }

}

@Composable
fun BuyButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp),
    ) {
        Text(stringResource(id = R.string.button_paid))
    }

}

fun generateQRCode(data: String): Bitmap {
    val bitMatrix: BitMatrix = MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, 1300, 1300)
    val width = bitMatrix.width
    val height = bitMatrix.height
    val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

    for (x in 0 until width) {
        for (y in 0 until height) {
            bmp.setPixel(x, y, if (bitMatrix[x, y]) BLACK else WHITE)
        }
    }
    return bmp
}

@Composable
fun QRCodeDialog(/*ticketCode:String*/qrCodeBitmap: Bitmap, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(id = R.string.qr_code_title)) },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              //  val qrCodeBitmap = generateQRCode(ticketCode)
                Image(
                    bitmap = qrCodeBitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(300.dp),
                    alignment = Alignment.Center
                )

            }
        },
        confirmButton = {

            Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(id = R.string.error_close))
            }

        }
    )
}