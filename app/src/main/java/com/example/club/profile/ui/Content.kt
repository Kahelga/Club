package com.example.club.profile.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.club.R
import com.example.club.profile.domain.entity.User

@Composable
fun Content(
    user: User,
    // onUpdateData: () -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxHeight()) {
        item {
            UserItem(
                user,
                /*onUpdateData = { onItemClicked() }*/
            )
        }
    }

}

@Composable
private fun UserItem(
    user: User,
    // onUpdateData: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ProfileField(label = stringResource(R.string.user_name), value = user.firstname)
        ProfileField(label = stringResource(R.string.user_middlename), value = user.middlename)
        ProfileField(label = stringResource(R.string.user_lastname), value = user.lastname)
        ProfileField(label = stringResource(R.string.user_phone), value = user.phone)
        ProfileField(label = stringResource(R.string.user_email), value = user.email)
        ProfileField(label = stringResource(R.string.user_city), value = user.city)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* Обновить данные */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.button_update))
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