package com.example.club.feature.admin.events.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.Popup
import com.example.club.feature.admin.events.R
import com.example.club.feature.hall.ui.DrawGrid
import com.example.club.shared.event.domain.entity.Hall
import com.example.club.shared.event.domain.entity.SeatPlan
import com.example.club.shared.event.domain.entity.SeatStatus
import com.example.club.shared.event.domain.entity.SeatType
import com.example.club.shared.event.domain.entity.Ticket
import okhttp3.internal.wait


@Composable
fun HallPlan(
    hall: Hall,
    onDismiss: () -> Unit,
    update: () -> Unit
) {
    Popup(alignment = Alignment.Center) {
        Column(
            modifier = Modifier
               // .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.medium
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = MaterialTheme.shapes.small
                )
                .verticalScroll(rememberScrollState()),
        ) {
            val gridSize = 40.dp
            Row(
                modifier = Modifier.fillMaxWidth(),
                //horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = hall.name,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .weight(1f)
                )
                IconButton(
                    onClick = update,
                    modifier = Modifier.size(35.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Update,
                        contentDescription = null,
                        modifier = Modifier.size(35.dp)

                    )
                }

            }

            Card(
                modifier = Modifier.padding(bottom = 10.dp),
                elevation = CardDefaults.cardElevation(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ) {
                    val hasDanceFloor =
                        hall.seatingPlan.tickets.any { it.type == SeatType.DANCEFLOOR }
                    val hasTable =
                        hall.seatingPlan.tickets.any { it.type == SeatType.TABLE || it.type == SeatType.VIPTABLE }
                    val hasBar = hall.seatingPlan.tickets.any { it.type == SeatType.BAR }

                    if (hasDanceFloor) {
                        val dancefloorTickets =
                            hall.seatingPlan.tickets.filter { it.type == SeatType.DANCEFLOOR }
                        val countDanceFloor =
                            if (dancefloorTickets.isNotEmpty()) dancefloorTickets[0].capacity else 0

                        Text(
                            text = stringResource(R.string.count_danceFloor, countDanceFloor),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 3.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant

                        )
                    }

                    if (hasTable) {
                        val countTable =
                            hall.seatingPlan.tickets.filter { it.type == SeatType.TABLE || it.type == SeatType.VIPTABLE && it.status == SeatStatus.FREE }
                                .count()

                        Text(
                            text = stringResource(R.string.count_table, countTable),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 3.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (hasBar) {
                        val countBar =
                            hall.seatingPlan.tickets.filter { it.type == SeatType.BAR && it.status == SeatStatus.FREE }
                                .count()

                        Text(
                            text = stringResource(R.string.count_bar, countBar),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 3.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            Card(
                modifier = Modifier.padding(bottom = 10.dp),
                elevation = CardDefaults.cardElevation(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
            ) {
                Row(modifier = Modifier.padding(bottom = 16.dp)) {
                    ColorBox(Color.Green, stringResource(id = R.string.ticket_free))
                    // Spacer(modifier = Modifier.width(8.dp))
                    ColorBox(Color.Red, stringResource(id = R.string.ticket_occupied))
                    //Spacer(modifier = Modifier.width(8.dp))
                    ColorBox(Color.Yellow, stringResource(id = R.string.ticket_reserved))

                }
            }

            Card(
                modifier = Modifier.height(400.dp),
                elevation = CardDefaults.cardElevation(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),

                ) {
                SeatPlanView(
                    seatPlan = hall.seatingPlan,
                    gridSize = gridSize,
                )

            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = onDismiss) {
                Text(stringResource(id = R.string.error_close))
            }
        }
    }
}

@Composable
private fun SeatPlanView(
    seatPlan: SeatPlan,
    gridSize: Dp,
) {
    val rows = seatPlan.row
    val columns = seatPlan.column

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

    val color = MaterialTheme.colorScheme.surfaceVariant

    Box(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.medium
            )
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
                    color = color,
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
        ticket.status == SeatStatus.OCCUPIED -> Color.Red
        ticket.status == SeatStatus.RESERVED -> Color.Yellow
        ticket.type == SeatType.DANCEFLOOR -> if (ticket.capacity != 0) Color.Green.copy(alpha = 0.3f) else Color.Red
        else -> Color.Green
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

            .background(backgroundColor, shape = shape),

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