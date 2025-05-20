package com.example.club.feature.admin.events.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.club.feature.admin.events.R
import com.example.club.feature.admin.events.presentation.EventsState
import com.example.club.feature.admin.events.presentation.EventsViewModel
import com.example.club.feature.hall.presentation.HallViewModel

@Composable
fun EventsScreen(
    eventsViewModel: EventsViewModel,
    hallViewModel: HallViewModel,
    onReportsSelected: () -> Unit,
    logOut:() ->Unit
) {
    val eventsState by eventsViewModel.state.collectAsState()
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val error = stringResource(id = R.string.error_unknown_error)

    LaunchedEffect(Unit) {
        eventsViewModel.loadEvents()
    }
    LaunchedEffect(eventsState) {
        when (eventsState) {
            is EventsState.Failure -> {
                showErrorDialog = true
                errorMessage = (eventsState as EventsState.Failure).message ?: error
            }

            is EventsState.Content -> {
                if (showErrorDialog) {
                    showErrorDialog = false
                }
            }

            else -> {}
        }
    }
    Scaffold(
        bottomBar = {
            BottomBar(onReportsSelected, logOut)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            EventsTitle()
            when (val state = eventsState) {
                is EventsState.Initial,
                is EventsState.Loading -> Loading()

                is EventsState.Failure -> {}
                is EventsState.Content -> Content(
                    events = state.events,
                    hallViewModel = hallViewModel
                )

            }
            if (showErrorDialog) {
                Error(
                    message = errorMessage,
                    onRetry = {
                        eventsViewModel.loadEvents()
                        showErrorDialog = false
                    },
                    onCancel = {
                        showErrorDialog = false
                    }
                )
            }
        }
    }
}

@Composable
fun BottomBar(onReportsSelected: () -> Unit, logOut: () -> Unit) {

    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .height(60.dp)
            .border(BorderStroke(1.dp, Color.LightGray), RoundedCornerShape(15.dp))
            .clip(RoundedCornerShape(15.dp))
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp)
                .padding(end = 15.dp)
        ) {
            BottomBarItem(
                icon = Icons.Filled.Assessment,
                label = stringResource(id = R.string.bar_report),
                onClick = onReportsSelected,
                iconTint = Color.Gray
            )
            BottomBarItem(
                icon = Icons.Filled.Event,
                label = stringResource(id = R.string.event_title),
                onClick = {},

                )
            BottomBarItem(
                icon = Icons.Filled.ExitToApp,
                label = stringResource(id = R.string.exit_title),
                onClick = logOut ,
                iconTint = Color.Gray
            )
        }
    }


}

@Composable
fun EventsTitle() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp)
            .padding(vertical = 12.dp, horizontal = 8.dp),
        text = stringResource(id = R.string.event_title),
        style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
    )
}

@Composable
fun BottomBarItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    iconTint: Color = MaterialTheme.colorScheme.onPrimaryContainer
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxHeight()
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(25.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
        }
        Text(
            label,
            color = iconTint,
            fontSize = 11.sp,
        )
    }
}