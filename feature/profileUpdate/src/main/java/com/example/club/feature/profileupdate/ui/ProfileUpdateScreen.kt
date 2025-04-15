package com.example.club.feature.profileupdate.ui

import com.example.club.feature.profileupdate.presentation.ProfileUpdateViewModel

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


import com.example.club.shared.user.profile.domain.entity.User
import com.example.club.feature.profile.presentation.ProfileViewModel
import com.example.club.feature.profileupdate.R
import com.example.club.feature.profileupdate.presentation.ProfileUpdateState
import com.example.club.util.validation.validateCity
import com.example.club.util.validation.validateEmail
import com.example.club.util.validation.validateMiddlename
import com.example.club.util.validation.validateName
import com.example.club.util.validation.validatePhone
import com.example.club.util.validation.validateSurname


@Composable
fun ProfileUpdateScreen(
    profileViewModel: ProfileViewModel,
    profileUpdateViewModel: ProfileUpdateViewModel,
    onBackPressed: () -> Unit,
) {
    val profileUpdateState by profileUpdateViewModel.state.collectAsState()
    val user by profileViewModel.user.collectAsState()


    val id by remember { mutableStateOf(user.id ) }
    var firstname by remember { mutableStateOf(user.firstname ) }
    var lastname by remember { mutableStateOf(user.lastname) }
    var middlename by remember { mutableStateOf(user.middlename ) }
    var email by remember { mutableStateOf(user.email ) }
    var phone by remember { mutableStateOf(user.phone ) }
    var city by remember { mutableStateOf(user.city ) }
    val role by remember { mutableStateOf(user.role ) }


    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopBar(onBackPressed = onBackPressed)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = stringResource(id = R.string.profile_title),
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
            )
        }
        LazyColumn(modifier = Modifier.fillMaxHeight()) {
            item {
                UserItem(
                    firstname = firstname,
                    lastname = lastname,
                    middlename = middlename,
                    email = email,
                    phone = phone,
                    city = city,
                    onFirstnameChange = { firstname = it },
                    onLastnameChange = { lastname = it },
                    onMiddlenameChange = { middlename = it },
                    onEmailChange = { email = it },
                    onPhoneChange = { phone = it },
                    onCityChange = { city = it },
                    onUpdateData = {
                        val updatedUser  = User(
                            id=id,
                            firstname = firstname,
                            lastname = lastname,
                            middlename = middlename,
                            email = email,
                            phone = phone,
                            city = city,
                            role= role
                        )
                        profileUpdateViewModel.updateUser (updatedUser)
                    }
                )
            }
        }

        when (val state = profileUpdateState) {
            is ProfileUpdateState.Initial,
            is ProfileUpdateState.Loading -> Loading()
            is ProfileUpdateState.Failure -> {
                showDialog = true
                dialogMessage = state.message ?: stringResource(id = R.string.error_unknown_error)
            }
            is ProfileUpdateState.Success -> {
                showDialog = true
                dialogMessage =stringResource(id = R.string.profile_update)
                profileUpdateViewModel.resetState()
            }
        }

        if (showDialog) {
            ErrorDialog(
                onDismiss = { showDialog = false },
                title = stringResource(id = R.string.error_title),
                message = dialogMessage
            )
        }
    }
}
@Composable
private fun UserItem(
    firstname: String,
    lastname: String,
    middlename: String,
    email: String,
    phone: String,
    city: String,
    onFirstnameChange: (String) -> Unit,
    onLastnameChange: (String) -> Unit,
    onMiddlenameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onCityChange: (String) -> Unit,
    onUpdateData: () -> Unit
) {
    var firstnameError by remember { mutableStateOf("") }
    var lastnameError by remember { mutableStateOf("") }
    var middlenameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var phoneError by remember { mutableStateOf("") }
    var cityError by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ProfileField(
            label = stringResource(R.string.user_lastname),
            value = lastname,
            onValueChange = {
                onLastnameChange(it)
                lastnameError = validateSurname(it)
            },
            errorMessage = lastnameError
        )

        ProfileField(
            label = stringResource(R.string.user_name),
            value = firstname,
            onValueChange = {
                onFirstnameChange(it)
                firstnameError = validateName(it)
            },
            errorMessage = firstnameError
        )

        ProfileField(
            label = stringResource(R.string.user_middlename),
            value = middlename,
            onValueChange = {
                onMiddlenameChange(it)
                middlenameError = validateMiddlename(it)
            },
            errorMessage = middlenameError
        )

        ProfileField(
            label = stringResource(R.string.user_phone),
            value = phone,
            onValueChange = {
                onPhoneChange(it)
                phoneError = validatePhone(it)
            },
            errorMessage = phoneError
        )

        ProfileField(
            label = stringResource(R.string.user_email),
            value = email,
            onValueChange = {
                onEmailChange(it)
                emailError = validateEmail(it)
            },
            errorMessage = emailError
        )
        ProfileField(
            label = stringResource(R.string.user_city),
            value = city,
            onValueChange = {
                onCityChange(it)
                cityError = validateCity(it)
            },
            errorMessage = cityError
        )


        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                focusManager.clearFocus()
                if (firstnameError.isEmpty() && lastnameError.isEmpty() && middlenameError.isEmpty() &&
                    emailError.isEmpty() && phoneError.isEmpty() && cityError.isEmpty()) {
                    onUpdateData()
                } else {
                    showDialog = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.button_update))
        }

        if (showDialog) {
            ErrorDialog(
                onDismiss = { showDialog = false },
                title = stringResource(id = R.string.error_title),
                message = stringResource(id = R.string.error_register)
            )
        }
    }
}

@Composable
fun ProfileField(label: String, value: String, onValueChange: (String) -> Unit, errorMessage: String = "") {
    Text(text = label, style = MaterialTheme.typography.bodyMedium)
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
    )
    if (errorMessage.isNotEmpty()) {
        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}
@Composable
fun TopBar(onBackPressed: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 35.dp)
    ) {
        IconButton(onClick = { onBackPressed() }) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}
@Composable
fun ErrorDialog(onDismiss: () -> Unit, title: String, message: String) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
        },
        text = {
            Text(text = message)
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(id = R.string.purchase_dialog_ok))
            }
        },
        modifier = Modifier.padding(16.dp)
    )
}


