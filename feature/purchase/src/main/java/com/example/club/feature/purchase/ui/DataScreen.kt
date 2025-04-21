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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import com.example.club.feature.profile.presentation.ProfileState
import com.example.club.feature.profile.presentation.ProfileViewModel

import com.example.club.feature.purchase.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataScreen(
    profileViewModel: ProfileViewModel,
    onBackPressed: () -> Unit,
    toBuySelected: (/*List<String>,eventId: String,userId:String*/) -> Unit
) {
    val profileState by profileViewModel.state.collectAsState()
    val progress = 2
    val totalSteps = 3

    LaunchedEffect(Unit) {
        profileViewModel.loadUser()
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
                        stringResource(id = R.string.data_title),
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
        when (val state = profileState) {
            is ProfileState.Initial,
            is ProfileState.Loading -> {}
            is ProfileState.Failure -> Error(
                message = state.message
                    ?: stringResource(id = R.string.error_unknown_error),
                onRetry = { profileViewModel.loadUser() }
            )

            is ProfileState.Content -> {ContentData(state.user,toBuySelected)}
        }

    }


}