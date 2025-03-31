package com.example.club.profile.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.draw.clip
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
    profileViewModel: ProfileViewModel,
    onPosterSelected: () -> Unit,
    onLogout: () -> Unit,
     onOrderSelected:()->Unit,
     onUpdateData: () -> Unit
) {
    val profileState by profileViewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        profileViewModel.loadUser()
    }

    Scaffold(
        bottomBar = {
            BottomBar({onPosterSelected()},{onOrderSelected()})
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
                    text = stringResource(id = R.string.profile_title),
                    style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                   // modifier = Modifier.padding(bottom = 28.dp)
                )
                IconButton(
                    onClick = onLogout,
                    modifier = Modifier.size(35.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ExitToApp,
                        contentDescription = null,
                        Modifier.size(35.dp)
                    )
                }
            }


            Spacer(modifier = Modifier.height(25.dp))
            when (val state = profileState) {
                is ProfileState.Initial,
                is ProfileState.Loading -> {
                } /*-> Loading()*/
                is ProfileState.Failure -> Error(
                    message = state.message ?: stringResource(id = R.string.error_unknown_error),
                    onRetry = { profileViewModel.loadUser() }
                )

                is ProfileState.Content -> Content(
                    user = state.user,
                    onUpdateData=onUpdateData
                )
            }

        }
    }
}


@Composable
fun BottomBar(onPosterSelected: () -> Unit, onOrderSelected:()->Unit) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .height(60.dp)
            .border(BorderStroke(1.dp, Color.LightGray), RoundedCornerShape(15.dp))
            .clip(RoundedCornerShape(15.dp))
    ) {Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start=15.dp)
            .padding(end=15.dp)
    ){
        BottomBarItem(
            icon = Icons.Filled.Menu,
            label = stringResource(id = R.string.bar_poster),
            onClick = onPosterSelected,
            iconTint = Color.Gray
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
            onClick = { }

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