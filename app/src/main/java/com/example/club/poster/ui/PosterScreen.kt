package com.example.club.poster.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
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
import com.example.club.R
import com.example.club.authorization.presentation.AuthViewModel
import com.example.club.poster.presentation.PosterState
import com.example.club.poster.presentation.PosterViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PosterScreen(
    posterViewModel: PosterViewModel,
    authViewModel: AuthViewModel,
    onItemSelected: (eventId: String) -> Unit,
    onProfileSelected: (login: String) -> Unit,
    onOrderSelected: (login: String) -> Unit
) {
    val posterState by posterViewModel.state.collectAsState()
    val loginUser by authViewModel.login.collectAsState()
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val error =stringResource(id = R.string.error_unknown_error)

    LaunchedEffect(Unit) {
        posterViewModel.loadEvents()
    }
    LaunchedEffect(posterState) {
        when (posterState) {
            is PosterState.Failure -> {
                showErrorDialog = true
                errorMessage = (posterState as PosterState.Failure).message ?: error
            }
            is PosterState.Content -> {
                if (showErrorDialog) {
                    showErrorDialog = false
                }
            }
            else -> {}
        }
    }
    Scaffold(
        bottomBar = {
            BottomBar({ onProfileSelected(loginUser) }, { onOrderSelected(loginUser) })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            PosterTitle()
            when (val state = posterState) {
                is PosterState.Initial,
                is PosterState.Loading -> Loading()

                is PosterState.Failure ->{}
                is PosterState.Content -> Content(
                    events = state.events,
                    onItemClicked = onItemSelected,
                )

            }
            if (showErrorDialog) {
                Error(
                    message = errorMessage,
                    onRetry = {
                        posterViewModel.loadEvents()
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
fun BottomBar(onProfileSelected: () -> Unit, onOrderSelected: () -> Unit) {

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
                .padding(start=15.dp)
                .padding(end=15.dp)
        ) {
            BottomBarItem(
                icon = Icons.Filled.Menu,
                label = stringResource(id = R.string.bar_poster),
                onClick = { }
            )
            BottomBarItem(
                icon = Icons.Filled.Star,
                label = stringResource(id = R.string.order_title),
                onClick = onOrderSelected,
                iconTint = Color.Gray
            )
            BottomBarItem(
                icon = Icons.Filled.Person,
                label = stringResource(id = R.string.profile_title),
                onClick = onProfileSelected,
                iconTint = Color.Gray
            )
        }
    }


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

@Composable
fun PosterTitle() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp)
            .padding(vertical = 12.dp, horizontal = 8.dp),
        text = stringResource(id = R.string.poster_title),
        style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
    )
}