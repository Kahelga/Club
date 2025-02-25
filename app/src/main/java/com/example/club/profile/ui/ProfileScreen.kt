package com.example.club.profile.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
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
import com.example.club.profile.presentation.ProfileState
import com.example.club.profile.presentation.ProfileViewModel
import com.example.club.profile.ui.Error

@Composable
fun ProfileScreen(
    profileViewModel:ProfileViewModel,
    onPosterSelected: () -> Unit,
    // onLogout: () -> Unit
    // onUpdateData: () -> Unit
) {
    val profileState by profileViewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        profileViewModel.loadUser()
    }

    Scaffold(
        bottomBar = {
            BottomBar(onPosterSelected)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Профиль",
                    style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 28.dp)
                )
                IconButton(onClick = { /* выход из профиля */ }) {
                    Icon(
                        imageVector = Icons.Filled.ExitToApp,
                        contentDescription = null,
                    )
                }
            }
            when (val state = profileState) {
                is ProfileState.Initial,
                is ProfileState.Loading ->{ } /*-> Loading()*/
                is ProfileState.Failure -> Error(
                    message = state.message ?: stringResource(id = R.string.error_unknown_error),
                    onRetry ={profileViewModel.loadUser()}
                )

                    is ProfileState.Content -> Content(
                    user = state.user
                )
            }

        }
    }
}


@Composable
fun BottomBar(onPosterSelected: () -> Unit) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .height(95.dp)
            .border(BorderStroke(2.dp, Color.LightGray))
    ) {
        BottomBarItem(
            icon = Icons.Filled.Menu,
            label = "Афиша",
            onClick =  onPosterSelected,
            iconTint = Color.Gray
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
            onClick = {  }

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