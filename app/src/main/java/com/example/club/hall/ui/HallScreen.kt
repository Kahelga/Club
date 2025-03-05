package com.example.club.hall.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.club.R
import com.example.club.hall.domain.entity.Ticket
import com.example.club.hall.presentation.HallState
import com.example.club.hall.presentation.HallViewModel
import com.example.club.hall.ui.Error

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HallScreen(
    viewModel: HallViewModel,
    onBackPressed: () -> Unit,
    toBuySelected: (List<String>,Int) -> Unit,
) {
    val hallState by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadHall()
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
            when (val state = hallState) {
                is HallState.Initial,
                is HallState.Loading -> Loading()
                is HallState.Failure->Error(
                    message = state.message ?: stringResource(id = R.string.error_unknown_error),
                    onRetry = {viewModel.loadHall()},
                    onDismiss = {onBackPressed}
                )
                is HallState.Content -> Content(
                    hall = state.hall,
                    toBuySelected = { selectedTickets ->
                        val seatNames = selectedTickets.map { it.seat }
                        val totalPrice = selectedTickets.sumOf { it.price }
                        toBuySelected(seatNames,totalPrice)
                    }
                )
            }

    }

}