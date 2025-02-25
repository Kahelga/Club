package com.example.club.poster.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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



@Composable
fun PosterScreen(
    posterViewModel: PosterViewModel,
    authViewModel: AuthViewModel,
    onItemSelected: (eventId: String) -> Unit,
    onProfileSelected: (login:String) -> Unit
) {
    val posterState by posterViewModel.state.collectAsState()
    val loginUser by authViewModel.login.collectAsState()

    LaunchedEffect(Unit) {
        posterViewModel.loadEvents()
    }

    Scaffold(
        bottomBar = {
            BottomBar { onProfileSelected(loginUser) }
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
                is PosterState.Failure -> Error(
                    message = state.message ?: stringResource(id = R.string.error_unknown_error),
                    onRetry = { posterViewModel.loadEvents() },
                )
                is PosterState.Content -> Content(
                    events = state.events,
                    onItemClicked = onItemSelected,
                )
            }
        }
    }
}

@Composable
fun BottomBar(onProfileSelected: () -> Unit) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .height(95.dp)
            .border(BorderStroke(2.dp, Color.LightGray))
    ) {
        BottomBarItem(
            icon = Icons.Filled.Menu,
            label = "Афиша",
            onClick = { /* Обработка клика для Афиши */ }
        )
        BottomBarItem(
            icon = Icons.Filled.Star,
            label = "Билеты",
            onClick = { /* Обработка клика для Билетов */ },
            iconTint = Color.Gray
        )
        BottomBarItem(
            icon = Icons.Filled.Person,
            label = "Профиль",
            onClick = onProfileSelected,
            iconTint = Color.Gray
        )
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
        modifier = Modifier.padding(horizontal = 35.dp)
    ) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(28.dp)
            )
        }
        Text(label, color = iconTint, fontSize = 15.sp)
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