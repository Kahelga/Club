package com.example.club.feature.purchase.ui

import androidx.compose.foundation.layout.Box
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
import com.example.club.feature.booking.presentation.BookedTicketState
import com.example.club.feature.booking.presentation.BookedTicketViewModel
import com.example.club.feature.eventdetails.presentation.EventDetailsState
import com.example.club.feature.eventdetails.presentation.EventDetailsViewModel
import com.example.club.feature.purchase.R
import com.example.club.feature.purchase.presentation.PurchaseViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchaseScreen(
    //viewModelEvent: EventDetailsViewModel,
    bookedTicketViewModel: BookedTicketViewModel,
   // seats: List<String>,
   // totalPrice: Int,
    onBackPressed: () -> Unit,
    onData: () -> Unit
) {

   // val detailsState by viewModelEvent.state.collectAsState()
    val bookedTicketState by bookedTicketViewModel.state.collectAsState()


    val progress = 1
    val totalSteps = 3

    LaunchedEffect(Unit) {
        //viewModelEvent.loadEvent()
        bookedTicketViewModel.loadBookedTicket()
    }


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
                text = stringResource(R.string.progress, progress, totalSteps),
                style = MaterialTheme.typography.bodyMedium
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                LinearProgressIndicator(
                    progress = progress / totalSteps.toFloat(),
                    modifier = Modifier
                        .fillMaxWidth(),
                    // .clip(RoundedCornerShape(10.dp)),
                    trackColor = MaterialTheme.colorScheme.outline
                )

            }

        }
        when (val state = bookedTicketState) {
            is BookedTicketState.Initial,
            is BookedTicketState.Loading -> {}
            is BookedTicketState.Failure ->{
                Error(
                    message = state.message ?: stringResource(id = R.string.error_unknown_error),
                    onRetry = { bookedTicketViewModel.loadBookedTicket() /*viewModelEvent.loadEvent() */}
                )
            }
            is BookedTicketState.Content->ContentPurchase(
                state.bookedTicket.event,
                state.bookedTicket.seats,
                state.bookedTicket.totalPrice,
                toBuySelected = onData
            )

        }
       /* when (val state = detailsState) {
            is EventDetailsState.Initial,
            is EventDetailsState.Loading -> {
            }

            is EventDetailsState.Failure -> {
                Error(
                    message = state.message ?: stringResource(id = R.string.error_unknown_error),
                    onRetry = { viewModelEvent.loadEvent() }
                )
            }

            is EventDetailsState.Content -> ContentPurchase(
                state.event,
                seats,
                totalPrice,
                toBuySelected = onData
            )

        }*/

    }
}

