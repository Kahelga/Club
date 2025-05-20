package com.example.club.feature.tickets.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalActivity
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
import com.example.club.feature.auth.presentation.AuthViewModel
import com.example.club.feature.tickets.R
import com.example.club.feature.tickets.presentation.CancelOrderViewModel


import com.example.club.feature.tickets.presentation.OrderState
import com.example.club.feature.tickets.presentation.OrderViewModel

@Composable
fun OrderScreen(
    orderViewModel: OrderViewModel,
    authViewModel: AuthViewModel,
    cancelOrderViewModel: CancelOrderViewModel,
    onProfileSelected: (login: String) -> Unit,
    onPosterSelected: () -> Unit,
    onBuy:(bookedId:String)-> Unit,
    onOrderScreen: () -> Unit
) {
    val orderState by orderViewModel.state.collectAsState()
    val loginUser by authViewModel.login.collectAsState()
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val error =stringResource(id = R.string.error_unknown_error)

    LaunchedEffect(Unit) {
        orderViewModel.loadOrder()
    }
    LaunchedEffect(orderState) {
        when (orderState) {
            is OrderState.Failure -> {
                showErrorDialog = true
                errorMessage = (orderState as OrderState.Failure).message ?: error
            }
            is OrderState.Content -> {
                if (showErrorDialog) {
                    showErrorDialog = false
                }
            }
           // else -> {}
        }
    }

    Scaffold(
        bottomBar = {
            BottomBar({ onProfileSelected(loginUser) }, { onPosterSelected() })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            OrderTitle()

            when (val state = orderState) {
                is OrderState.Initial,
                is OrderState.Loading -> Loading()

                is OrderState.Failure ->{}

                is OrderState.Content -> Content(
                    cancelOrderViewModel,
                    orders = state.orders,
                    onBuy,
                    onPosterSelected,
                    onOrderScreen
                )
            }
            if (showErrorDialog) {
               Error(
                    message = errorMessage,
                    onRetry = {
                        orderViewModel.loadOrder()
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
fun BottomBar(onProfileSelected: () -> Unit, onPosterSelected: () -> Unit) {
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
                .padding(start = 15.dp)
                .padding(end = 15.dp)
        ) {
            BottomBarItem(
                icon = Icons.Filled.Menu,
                label = stringResource(id = R.string.bar_poster),
                onClick = onPosterSelected,
                iconTint = Color.Gray
            )
            BottomBarItem(
                icon = Icons.Filled.LocalActivity,
                label = stringResource(id = R.string.order_title),
                onClick = { }

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
fun OrderTitle() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp)
            .padding(vertical = 12.dp, horizontal = 8.dp),
        text = stringResource(id = R.string.order_title),
        style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
    )
}
