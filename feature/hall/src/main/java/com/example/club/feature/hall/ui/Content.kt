package com.example.club.feature.hall.ui


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.example.club.feature.hall.R
import com.example.club.shared.event.domain.entity.Hall
import com.example.club.shared.event.domain.entity.SeatPlan
import com.example.club.shared.event.domain.entity.SeatStatus
import com.example.club.shared.event.domain.entity.SeatType
import com.example.club.shared.event.domain.entity.Ticket


@Composable
fun Content(
    hall: Hall,
    toBuySelected: (List<Ticket>) -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxHeight()) {
        item {
            HallItem(
                hall,
                toBuySelected = { selectedTickets -> toBuySelected(selectedTickets) }
            )
        }
    }
}

@Composable
private fun HallItem(
    hall: Hall,
    toBuySelected: (List<Ticket>) -> Unit,
) {
    val selectedTickets = remember { mutableStateListOf<Ticket>() }
    val gridSize = 40.dp
    var isDialogVisible by remember { mutableStateOf(false) }
    var ticketToBuy by remember { mutableStateOf<Ticket?>(null) }
    val currentTicketCount = remember { mutableStateOf(0) }
    val originalTicketCount = remember { mutableStateOf(0) }
    var showErrorDialog by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxSize())
    {
        Text(
            text = hall.name,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(modifier = Modifier.padding(bottom = 16.dp)) {
            ColorBox(Color.Gray, stringResource(id = R.string.ticket_free))
            Spacer(modifier = Modifier.width(8.dp))
            ColorBox(Color.Red, stringResource(id = R.string.ticket_occupied))
            Spacer(modifier = Modifier.width(8.dp))
            ColorBox(Color.Green, stringResource(id = R.string.ticket_selected))
        }

        Card(
            modifier = Modifier.height(400.dp),
               // .background(MaterialTheme.colorScheme.secondaryContainer),
            elevation = CardDefaults.cardElevation(10.dp)
        ) {
            SeatPlanView(
                seatPlan = hall.seatingPlan,
                gridSize = gridSize,
                selectedTickets = selectedTickets,
                onSeatClick = { ticket ->
                    if (ticket.status == SeatStatus.FREE) {
                        if (ticket.type == SeatType.DANCEFLOOR) {
                            if (selectedTickets.contains(ticket)) {
                                ticketToBuy = ticket
                                originalTicketCount.value = selectedTickets.count { it == ticket }
                                currentTicketCount.value = originalTicketCount.value
                                isDialogVisible = true
                            } else {
                                selectedTickets.add(ticket)
                                ticketToBuy = ticket
                                originalTicketCount.value = 1
                                currentTicketCount.value = 1
                                isDialogVisible = true
                            }
                        } else {
                            if (selectedTickets.contains(ticket)) {
                                selectedTickets.remove(ticket)
                            } else {
                                selectedTickets.add(ticket)
                            }
                        }

                    }
                }
            )
        }

        SelectedTicketInfo(tickets = selectedTickets)
        BuyTicketButton {
            if (selectedTickets.isEmpty()) {
                showErrorDialog = true
            } else {
                toBuySelected(selectedTickets)
            }

        }
        if (showErrorDialog) {
            AlertDialog(
                onDismissRequest = { showErrorDialog = false },
                title = { Text(text =stringResource(id = R.string.error_title)) },
                text = { Text(text = stringResource(id = R.string.order_error_massage)) },
                confirmButton = {
                    Button(onClick = { showErrorDialog = false }) {
                        Text(stringResource(id = R.string.purchase_dialog_ok))
                    }
                }
            )
        }
        if (isDialogVisible && ticketToBuy != null) {
            TicketCountDialog(
                ticket = ticketToBuy!!,
                ticketCount = currentTicketCount.value,
                onCountChange = { count -> currentTicketCount.value = count },
                onConfirm = {
                    val difference = currentTicketCount.value - originalTicketCount.value
                    if (difference > 0) {
                        repeat(difference) {
                            selectedTickets.add(ticketToBuy!!.copy())
                        }
                    } else if (difference < 0) {
                        val ticketsToRemoveCount = -difference
                        repeat(ticketsToRemoveCount.coerceAtMost(selectedTickets.count { it == ticketToBuy })) {
                            val ticketToRemoveIndex = selectedTickets.indexOf(ticketToBuy)
                            if (ticketToRemoveIndex != -1) {
                                selectedTickets.removeAt(ticketToRemoveIndex)
                            }
                        }
                    }
                    isDialogVisible = false
                },
                onDismiss = {
                    currentTicketCount.value = originalTicketCount.value
                    isDialogVisible = false
                }
            )
        }
    }
}

@Composable
fun TicketCountDialog(
    ticket: Ticket,
    ticketCount: Int,
    onCountChange: (Int) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(id = R.string.ticket_dialog_label)) },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(stringResource(id = R.string.ticket_dialog_count))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(onClick = { onCountChange((ticketCount - 1).coerceAtLeast(0)) }) {
                        Text(stringResource(id = R.string.ticket_dialog_remove))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = ticketCount.toString(),
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = { onCountChange(ticketCount + 1) }) {
                        Text(stringResource(id = R.string.ticket_dialog_add))
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(stringResource(id = R.string.ticket_dialog_ok))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(id = R.string.ticket_dialog_close))
            }
        }
    )
}

@Composable
private fun SeatPlanView(
    seatPlan: SeatPlan,
    gridSize: Dp,
    selectedTickets: List<Ticket>,
    onSeatClick: (Ticket) -> Unit
) {
    val rows = seatPlan.row
    val columns = seatPlan.column
    //val sceneHeight = 100.dp
    val boxSize = Offset(
        x = with(LocalDensity.current) { (columns * gridSize.toPx()) },
        y = with(LocalDensity.current) { (rows * gridSize.toPx()) }
    )
    val boxWidth = columns * gridSize
    val boxHeight = rows * gridSize

    // Размеры и позиция сцены
    val sceneHeight = boxHeight * 0.1f
    val sceneWidth = boxWidth * 0.6f
    val sceneX = (boxWidth - sceneWidth) / 2
    val sceneY = 0f

    var scale by remember { mutableStateOf(1f) }
    //var offsetY by remember { mutableStateOf(0f) }
    val color=MaterialTheme.colorScheme.surfaceVariant

    Box(
        modifier = Modifier

            .pointerInput(Unit) {
                detectTransformGestures { _, _, zoom, _ ->
                    scale = (scale * zoom).coerceIn(0.5f, 3f)
                }
            }
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,

            )
            .drawBehind {
                drawRect(
                    color =color ,
                    size = this.size.copy(width = boxWidth.toPx(), height = boxHeight.toPx())
                )
            }
            //.size(boxWidth,boxHeight)
           // .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        DrawGrid(boxSize, gridSize)
        Box(
            modifier = Modifier
                .width(sceneWidth)
                .height(sceneHeight)
                .offset(sceneX, sceneY.dp)
                .background(Color.Red.copy(alpha = 0.3f), shape = RoundedCornerShape(10.dp))
        ) {
            Text(
                stringResource(id = R.string.hall_scene),
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Box(
            modifier = Modifier
                .wrapContentHeight()
               // .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            seatPlan.tickets.forEach { ticket ->
                SeatView(
                    ticket,
                    selectedTickets.contains(ticket),
                    onSeatClick,
                    gridSize,
                    boxWidth,
                    boxHeight,
                    boxSize
                )
            }
        }
    }
}

@Composable
private fun SeatView(
    ticket: Ticket,
    isSelected: Boolean,
    onSeatClick: (Ticket) -> Unit,
    gridSize: Dp,
    boxWidth: Dp,
    boxHeight: Dp,
    boxSize: Offset
) {
    val shape = when (ticket.type) {
        SeatType.DANCEFLOOR -> RectangleShape
        SeatType.BAR -> CircleShape
        SeatType.TABLE -> RoundedCornerShape(8.dp)
        SeatType.VIPTABLE -> RoundedCornerShape(8.dp)
        else -> RectangleShape
    }

    val backgroundColor = when {
        isSelected -> Color.Green
        ticket.status == SeatStatus.OCCUPIED -> Color.Red
        ticket.status == SeatStatus.RESERVED -> Color.Yellow
        ticket.type == SeatType.DANCEFLOOR -> Color.Gray.copy(alpha = 0.3f)
        else -> Color.Gray
    }
    val seatSize = when (ticket.type) {
        SeatType.DANCEFLOOR -> Size(
            boxWidth.value * 0.5f,
            boxHeight.value * 0.6f/*boxSize.x* 0.2f, boxSize.y * 0.2f*/
        )//boxWidth*0,5f,boxHeight*0,6f
        else -> Size(gridSize.value, gridSize.value)
    }
    Box(
        modifier = Modifier
            .size(seatSize.width.dp, seatSize.height.dp)
            .offset(
                x = (ticket.x * gridSize.value).dp,
                y = (ticket.y * gridSize.value).dp
            )

            .background(backgroundColor, shape = shape)
            .clickable { onSeatClick(ticket) },

        contentAlignment = Alignment.Center
    ) {
        Text(
            text = ticket.seat,
            color = Color.White,
            style = MaterialTheme.typography.bodySmall
        )
    }
}


@Composable
private fun SelectedTicketInfo(tickets: List<Ticket>) {
    if (tickets.isNotEmpty()) {
        val groupedTickets = tickets.chunked(3)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            groupedTickets.forEach { group ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    group.forEach { selectedTicket ->
                        Card(
                            modifier = Modifier
                                .padding(4.dp)
                                .heightIn(max = 100.dp)
                                .weight(1f),
                            elevation = CardDefaults.cardElevation(8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = stringResource(R.string.seat_name, selectedTicket.seat),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = stringResource(
                                        R.string.seat_price,
                                        selectedTicket.price
                                    ),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ColorBox(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(end = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(color, shape = RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
        ) {

        }
        Text(
            text = label,
            fontSize = 10.sp,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

@Composable
 fun DrawGrid(size: Offset, gridSize: Dp) {
    val gridSizePx = with(LocalDensity.current) { gridSize.toPx() }
    val color = MaterialTheme.colorScheme.outline
    Canvas(
        modifier = Modifier
            .width(size.x.dp)
            .height(size.y.dp)

    ) {

        for (x in 0 until (size.x.toInt() / gridSizePx).coerceAtLeast(1F).toInt()) {
            drawLine(
                color = color,
                Offset(x * gridSizePx, 0f),
                Offset(x * gridSizePx, size.y),
                strokeWidth = 1f
            )
        }
        for (y in 0 until (size.y.toInt() / gridSizePx).coerceAtLeast(1F).toInt()) {
            drawLine(
                color = color,
                Offset(0f, y * gridSizePx),
                Offset(size.x, y * gridSizePx),
                strokeWidth = 1f
            )
        }
        drawLine(color = color, Offset(size.x, 0f), Offset(size.x, size.y), strokeWidth = 1f)
        drawLine(color = color, Offset(0f, size.y), Offset(size.x, size.y), strokeWidth = 1f)

    }
}

@Composable
private fun BuyTicketButton(toBuySelected: () -> Unit) {
    Button(
        onClick = toBuySelected,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(top = 20.dp, bottom = 8.dp, start = 10.dp, end = 10.dp)
    ) {
        Text(
            text = stringResource(id = R.string.button_continue),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
    }
}
