package com.example.club.authorization.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.club.authorization.presentation.AuthViewModel

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    onPosterSelected: () -> Unit,
    // onLogout: () -> Unit
    // onUpdateData: () -> Unit
) {
    val user by authViewModel.userData.collectAsState()

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

            if (user != null) {
                // Поля профиля
                ProfileField(label = "Телефон*", value = user!!.phone)
                ProfileField(label = "Имя*", value = user!!.firstname)
                ProfileField(label = "Фамилия*", value = user!!.lastname)
                ProfileField(label = "Email", value = user!!.email)
                ProfileField(label = "Город", value = user!!.city)

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { /* Обновить данные */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Обновить данные")
                }
            } else {
                Text(text = "Данные о пользователе недоступны")
            }
        }
    }
}

@Composable
fun ProfileField(label: String, value: String) {
    Text(text = label, style = MaterialTheme.typography.bodyMedium)
    TextField(
        value = value,
        onValueChange = {},
        readOnly = true,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
    )
    Spacer(modifier = Modifier.height(8.dp))
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