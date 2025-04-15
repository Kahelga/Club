package com.example.club.feature.purchase.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import com.example.club.feature.eventdetails.presentation.EventDetailsState
import com.example.club.feature.eventdetails.presentation.EventDetailsViewModel
import com.example.club.feature.purchase.R
import com.example.club.feature.purchase.presentation.PurchaseViewModel



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchaseScreen(
   // viewModel: PurchaseViewModel,
    viewModelEvent: EventDetailsViewModel,
    seats: List<String>,
    totalPrice: Int,
    onBackPressed: () -> Unit,
    onData: () -> Unit
) {
  //  val purchaseState by viewModel.state.collectAsState()
    val detailsState by viewModelEvent.state.collectAsState()
    //val purchaseRequest = PurchaseRequest(eventId, seats)
    var showDialog by remember { mutableStateOf(false) }
    val progress = 1
    val totalSteps = 3
    LaunchedEffect(Unit) {
        viewModelEvent.loadEvent()
    }
  /*  when (val state = purchaseState) {
        is PurchaseState.Initial -> {
        }

        is PurchaseState.Loading -> {
            Loading()
        }

        is PurchaseState.Success -> {
            if (!showDialog) {
                showDialog = true
            }
        }

        is PurchaseState.Failure -> {
            Error(
                message = state.message ?: stringResource(id = R.string.error_unknown_error),
                onRetry = { viewModel.buyTicket(purchaseRequest) }
            )
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            text = {
                Text(
                    stringResource(id = R.string.purchase_dialog),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        onPosterScreen()
                    }
                ) {
                    Text(stringResource(id = R.string.purchase_dialog_ok))
                }
            }
        )
    }*/

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        TopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { onBackPressed() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        stringResource(id = R.string.all_title),
                        style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                    )
                }
            }
        )
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.progress, progress,totalSteps),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = progress / totalSteps.toFloat(),
                modifier = Modifier.fillMaxWidth()
            )

        }
        when (val state = detailsState) {
            is EventDetailsState.Initial,
            is EventDetailsState.Loading -> {}

            is EventDetailsState.Failure -> {  Error(
                message = state.message ?: stringResource(id = R.string.error_unknown_error),
                onRetry = { viewModelEvent.loadEvent() }
            )}

            is EventDetailsState.Content -> ContentPurchase( state.event,seats,totalPrice, toBuySelected= onData )

        }

    }
}

