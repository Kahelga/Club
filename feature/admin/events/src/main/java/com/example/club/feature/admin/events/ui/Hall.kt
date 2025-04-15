package com.example.club.feature.admin.events.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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


@Composable
fun HallPlan(
    hall: Hall,
    onDismiss: () -> Unit
) {
    Popup(alignment = Alignment.Center) {
        Column(
            modifier = Modifier
               // .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.medium
                )
                .verticalScroll(rememberScrollState())
            ,
        ) {
            val gridSize = 40.dp
            Text(
                text = hall.name,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(modifier = Modifier.padding(bottom = 16.dp)) {
                ColorBox(Color.Gray, stringResource(id = R.string.ticket_free))
                Spacer(modifier = Modifier.width(8.dp))
                ColorBox(Color.Red, stringResource(id = R.string.ticket_occupied))

            }
            Card(
                modifier = Modifier.height(400.dp),
                elevation = CardDefaults.cardElevation(10.dp)
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