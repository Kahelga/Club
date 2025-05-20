package com.example.club.feature.profileupdate.ui

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import com.example.club.feature.profileupdate.presentation.ProfileUpdateViewModel

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


import com.example.club.shared.user.profile.domain.entity.User
import com.example.club.feature.profile.presentation.ProfileViewModel
import com.example.club.feature.profileupdate.R
import com.example.club.feature.profileupdate.presentation.ProfileUpdateState
import com.example.club.util.formatting.formatDateSelected
import com.example.club.util.formatting.formatDateToDDMMYYYY
import com.example.club.util.validation.validateAge
import com.example.club.util.validation.validateCity
import com.example.club.util.validation.validateEmail
import com.example.club.util.validation.validateMiddlename
import com.example.club.util.validation.validateName
import com.example.club.util.validation.validatePhone
import com.example.club.util.validation.validateSurname
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


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
    var birthDate  by remember { mutableStateOf(formatDateToDDMMYYYY(user.birthDate)) }
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
                .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Profile Icon",
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
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
                    birthDate=birthDate,
                    onFirstnameChange = { firstname = it },
                    onLastnameChange = { lastname = it },
                    onMiddlenameChange = { middlename = it },
                    onEmailChange = { email = it },
                    onPhoneChange = { phone = it },
                    onCityChange = { city = it },
                    onBirthDate={birthDate=formatDateToDDMMYYYY(it)},
                    onUpdateData = {
                        val updatedUser  = User(
                            id=id,
                            firstname = firstname,
                            lastname = lastname,
                            middlename = middlename,
                            email = email,
                            phone = phone,
                            city = city,
                            birthDate = birthDate,
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
    birthDate:String,
    onFirstnameChange: (String) -> Unit,
    onLastnameChange: (String) -> Unit,
    onMiddlenameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onCityChange: (String) -> Unit,
    onBirthDate:(String)->Unit,
    onUpdateData: () -> Unit
) {
    var firstnameError by remember { mutableStateOf("") }
    var lastnameError by remember { mutableStateOf("") }
    var middlenameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var phoneError by remember { mutableStateOf("") }
    var cityError by remember { mutableStateOf("") }
    var birthDateError by remember { mutableStateOf("") }
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

        ProfileFieldWithDatePicker(
            label = stringResource(R.string.user_birth_date),
            value = birthDate,
            onValueChange = {
                onBirthDate(it)
                birthDateError = validateAge(it)
            },
            errorMessage = birthDateError
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

/*
        if (birthDateError.isNotEmpty()) {
            Text(
                text = birthDateError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
        }*/
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                focusManager.clearFocus()
                if (firstnameError.isEmpty() && lastnameError.isEmpty() && middlenameError.isEmpty() &&
                    emailError.isEmpty() && phoneError.isEmpty() && cityError.isEmpty() && birthDateError.isEmpty()) {
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
fun ProfileFieldWithDatePicker(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    errorMessage: String = ""
) {
    var showDialog by remember { mutableStateOf(false) }
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val context = LocalContext.current

    Column {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        TextField(
            value = value,
            onValueChange =onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clickable { showDialog = true },
            enabled = false,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Event,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
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

    if (showDialog) {
        DatePickerDialog(
            context,
            R.style.CustomDatePickerDialog,
            { _, selectedYear, selectedMonth, selectedDay ->
                calendar.set(Calendar.YEAR, selectedYear)
                calendar.set(Calendar.MONTH, selectedMonth)
                calendar.set(Calendar.DAY_OF_MONTH, selectedDay)

                val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
                onValueChange(formattedDate)
                showDialog = false
            },
            year, month, day
        ).show()
    }
}
@Composable
fun TopBar(onBackPressed: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 28.dp)
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

