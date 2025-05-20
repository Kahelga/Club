package com.example.club.feature.admin.events.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.club.feature.admin.events.R
import com.example.club.feature.hall.presentation.HallState
import com.example.club.feature.hall.presentation.HallViewModel
import com.example.club.shared.event.domain.entity.AgeRatings
import com.example.club.shared.event.domain.entity.EventDetails
import com.example.club.shared.event.domain.entity.EventStatus
import com.example.club.util.formatting.formatDateSelected
import com.example.club.util.formatting.formatTimeSelected

@Composable
fun Content(
    events: List<EventDetails>,
    hallViewModel: HallViewModel,
) {

    var showDetailsDialog by remember { mutableStateOf(false) }
    var selectedEventItem by remember { mutableStateOf<EventDetails?>(null) }

    var showHallDialog by remember { mutableStateOf(false) }


    var selectedStatus by remember { mutableStateOf<EventStatus?>(null) }
    val allStatuses = EventStatus.entries


    var expandedStatusMenu by remember { mutableStateOf(false) }
    var eventsPreview by remember { mutableStateOf(events) }

    val filteredEventsPreview = if (selectedStatus != null) {
        eventsPreview.filter { it.status == selectedStatus }
    } else {
        eventsPreview
    }

    val onShowDetails: (EventDetails) -> Unit = { event ->
        selectedEventItem = events.find { it.id == event.id }
        showDetailsDialog = true
    }
    val onShowHall: (EventDetails) -> Unit = { event ->
        selectedEventItem = events.find { it.id == event.id }
        showHallDialog = true


    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(1.dp)
    ) {
        Row(modifier = Modifier.padding(1.dp)) {
            Box {
                TextButton(onClick = { expandedStatusMenu = !expandedStatusMenu }) {
                    val statusName = when (selectedStatus?.name) {
                        EventStatus.ACTIVE.name -> "Активные"
                        EventStatus.EDITING.name -> "Редактированные"
                        EventStatus.ARCHIVED.name-> "Архивные"
                        EventStatus.CANCELED.name-> "Отмененные"
                        EventStatus.SCHEDULED.name -> "Запланированные"
                        else -> {stringResource(R.string.dropdownMenu_title)}
                    }
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(8.dp)
                            ),

                        text = statusName,//selectedStatus?.name ?: stringResource(R.string.dropdownMenu_title)
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                DropdownMenu(
                    expanded = expandedStatusMenu,
                    onDismissRequest = { expandedStatusMenu = false },
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        )
                        //.border(1.dp,MaterialTheme.colorScheme.onSurfaceVariant)
                ) {
                    DropdownMenuItem(
                        onClick = {
                            selectedStatus = null
                            expandedStatusMenu = false
                        },
                        text = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()

                            ) {
                                Text(
                                    text = stringResource(R.string.dropdownMenu_select),

                                )
                            }
                        },

                        )

                    allStatuses.forEach { status ->
                        val statusName = when (status.name) {
                            EventStatus.ACTIVE.name -> "Активные"
                            EventStatus.EDITING.name -> "Редактированные"
                            EventStatus.ARCHIVED.name-> "Архивные"
                            EventStatus.CANCELED.name-> "Отмененные"
                            EventStatus.SCHEDULED.name -> "Запланированные"
                            else -> {""}
                        }

                        Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface)
                        DropdownMenuItem(onClick = {
                            selectedStatus = status
                            expandedStatusMenu = false
                        },
                            text = {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()

                                ) {
                                   Text(text = statusName)
                                }
                            }
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(2.dp))

        if (filteredEventsPreview.isEmpty()) {
            Text(stringResource(R.string.event_warning))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                items(filteredEventsPreview, key = { it.id }) { event ->
                    EventRow(event = event, onShowDetails = onShowDetails, onShowHall = onShowHall)
                }
            }
        }


        if (showDetailsDialog && selectedEventItem != null) {
            EventDetailsDialog(
                event = selectedEventItem!!,
                onDismiss = { showDetailsDialog = false }
            )
        }

        if (showHallDialog && selectedEventItem != null) {
            HallContent(selectedEventItem!!.id, hallViewModel) {
                showHallDialog = false
            }
        }

    }
}


@Composable
fun EventRow(
    event: EventDetails,
    onShowDetails: (EventDetails) -> Unit,
    onShowHall: (EventDetails) -> Unit
) {
    val statusColor = when (event.status) {
        EventStatus.ACTIVE -> Color.Green
        EventStatus.ARCHIVED -> Color.LightGray
        EventStatus.EDITING -> Color.Yellow
        EventStatus.SCHEDULED -> Color.Blue
        EventStatus.CANCELED -> Color.Red
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(statusColor.copy(alpha = 0.15f), shape = RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val ageRatingText = when (event.ageRating) {
            AgeRatings.G -> "Для всех"
            AgeRatings.PG -> "С родительским контролем"
            AgeRatings.PG13 -> "13+"
            AgeRatings.R -> "16+"
            AgeRatings.NC17 -> "18+"
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = event.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = stringResource(
                    R.string.event_dataTime,
                    formatDateSelected(event.date),
                    formatTimeSelected(event.date)
                ),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(

                text = stringResource(R.string.event_ageRating, ageRatingText),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = stringResource(R.string.event_genre, event.genre.joinToString(", ")),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = stringResource(R.string.event_status, event.status.name),
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { onShowDetails(event) }) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = stringResource(R.string.button_details),
                )
            }
            IconButton(onClick = { onShowHall(event) }) {
                Icon(
                    imageVector = Icons.Default.Map,
                    contentDescription = stringResource(R.string.diolog_hall),
                )
            }
        }


    }
}

@Composable
fun HallContent(
    eventId: String,
    viewModel: HallViewModel,
    onDismiss: () -> Unit

) {
    viewModel.setEventId(eventId)
    val hallState by viewModel.state.collectAsState()
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val error = stringResource(id = com.example.club.feature.hall.R.string.error_unknown_error)


    LaunchedEffect(Unit) {
        viewModel.loadHall()
    }

    LaunchedEffect(hallState) {
        when (hallState) {
            is HallState.Failure -> {
                showErrorDialog = true
                errorMessage = (hallState as HallState.Failure).message ?: error
            }

            is HallState.Content -> {
                if (showErrorDialog) {
                    showErrorDialog = false
                }
            }

            else -> {}
        }
    }
    when (val state = hallState) {
        is HallState.Initial,
        is HallState.Loading -> Loading()

        is HallState.Failure -> {}
        is HallState.Content -> HallPlan(
            hall = state.hall,
            onDismiss = onDismiss,
            update = {
                viewModel.loadHall()}
        )
    }
    if (showErrorDialog) {
        Error(
            message = errorMessage,
            onRetry = {
                viewModel.loadHall()
                showErrorDialog = false
            },
            onCancel = {
                showErrorDialog = false
               // viewModel.setHallStateToInitial()

            }
        )
    }

}