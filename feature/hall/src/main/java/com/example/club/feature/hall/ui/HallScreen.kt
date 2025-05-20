package com.example.club.feature.hall.ui

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.club.feature.auth.presentation.AuthViewModel
import com.example.club.feature.booking.presentation.BookingState
import com.example.club.feature.booking.presentation.BookingViewModel
import com.example.club.feature.hall.R
import com.example.club.feature.hall.presentation.HallState
import com.example.club.feature.hall.presentation.HallViewModel
import com.example.club.shared.tickets.domain.entity.BookingRequest


@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HallScreen(
    viewModel: HallViewModel,
    bookingViewModel: BookingViewModel,
  //  profileViewModel: ProfileViewModel,
    authViewModel: AuthViewModel,
    eventId:String,
    onBackPressed: () -> Unit,
    toBuySelected: (bookedId:String/*List<String>, Int,String*/) -> Unit,
) {
    val hallState by viewModel.state.collectAsState()
    val bookingState by  bookingViewModel.state.collectAsState()
    val login=authViewModel.login.value
    //val profileState by profileViewModel.state.collectAsState()
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val error = stringResource(id = R.string.error_unknown_error)
    val progress = 1
    val totalSteps = 3

    LaunchedEffect(Unit) {
        viewModel.loadHall()
       // profileViewModel.loadUser()

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

    Column(modifier = Modifier.fillMaxSize()) {
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
                        stringResource(id = R.string.hall_title),
                        style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                    )
                }
            }

        )
        Column(
            modifier = Modifier.padding(10.dp)
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
        when (val state = hallState) {
            is HallState.Initial,
            is HallState.Loading -> Loading()
            is HallState.Failure -> {}
            is HallState.Content -> Content(
                hall = state.hall,
                toBuySelected = { selectedTickets ->
                    val seatNames = selectedTickets.map { it.seat }
                    val totalPrice = selectedTickets.sumOf { it.price }
                    val bookingRequest= BookingRequest(eventId,seatNames,totalPrice,login)
                    bookingViewModel.bookedTicket(bookingRequest)
                }
            )
        }
        LaunchedEffect(bookingState) {
            when (val state = bookingState) {
                is BookingState.Success -> {
                    toBuySelected(state.response.bookedId)
                }
                is BookingState.Failure -> {
                    showErrorDialog = true
                    errorMessage = state.message ?: error
                }

            }
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
                }
            )
        }

    }

}