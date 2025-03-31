package com.example.club.eventDetails.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.club.R
import com.example.club.eventDetails.presentation.EventDetailsState
import com.example.club.eventDetails.presentation.EventDetailsViewModel
import com.example.club.poster.presentation.PosterState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailsScreen(
    viewModel: EventDetailsViewModel,
    onBackPressed: () -> Unit,
    toBuySelected: (eventId: String) -> Unit,
) {
    val detailsState by viewModel.state.collectAsState()
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val error =stringResource(id = R.string.error_unknown_error)

    LaunchedEffect(Unit) {
        viewModel.loadEvent()
    }

    LaunchedEffect(detailsState) {
        when (detailsState) {
            is EventDetailsState.Failure -> {
                showErrorDialog = true
                errorMessage = (detailsState as EventDetailsState.Failure).message ?: error
            }
            is EventDetailsState.Content -> {
                if (showErrorDialog) {
                    showErrorDialog = false
                }
            }
            else -> {}
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { onBackPressed() }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        stringResource(id = R.string.details_title),
                        style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                    )
                }
            }

        )
        when (val state = detailsState) {
            is EventDetailsState.Initial,
            is EventDetailsState.Loading -> Loading()

            is EventDetailsState.Failure -> {}

            is EventDetailsState.Content -> Content(
                event = state.event,
                toBuySelected= toBuySelected
            )
        }
        if (showErrorDialog) {
            com.example.club.poster.ui.Error(
                message = errorMessage,
                onRetry = {
                    viewModel.loadEvent()
                    showErrorDialog = false
                },
                onCancel = {
                    showErrorDialog = false
                }
            )
        }
    }
}
