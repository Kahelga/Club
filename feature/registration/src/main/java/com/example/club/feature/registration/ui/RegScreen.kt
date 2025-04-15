package com.example.club.feature.registration.ui

import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.club.feature.registration.R


import com.example.club.feature.registration.presentation.RegState
import com.example.club.feature.registration.presentation.RegViewModel
import com.example.club.util.validation.validateEmail
import com.example.club.util.validation.validateMiddlename
import com.example.club.util.validation.validateName
import com.example.club.util.validation.validatePassword
import com.example.club.util.validation.validatePasswordRepeat
import com.example.club.util.validation.validateSurname

@Composable
fun RegScreen(
    regViewModel: RegViewModel,
    onRegSuccess: () -> Unit,
    onBackPressed: () -> Unit
) {
    val regState by regViewModel.state.collectAsState()

    var firstname by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var middlename by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordRepeat by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var isPasswordMismatch by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        TopBar(onBackPressed = onBackPressed)

       // Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = stringResource(id = R.string.reg_title),
            style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))

        SurnameField(value = lastname, onValueChange = { lastname = it })

        Spacer(modifier = Modifier.height(8.dp))

        NameField(value = firstname, onValueChange = { firstname = it })

        Spacer(modifier = Modifier.height(8.dp))

        MiddlenameField(value = middlename, onValueChange = { middlename = it })

        Spacer(modifier = Modifier.height(8.dp))

        LoginField(value = email, onValueChange = { email = it })

        Spacer(modifier = Modifier.height(8.dp))

        PasswordField(value = password, onValueChange = { password = it })

        Spacer(modifier = Modifier.height(8.dp))

        PasswordRepeatField(value = passwordRepeat, onValueChange = { passwordRepeat = it }, password = password)

        Spacer(modifier = Modifier.height(32.dp))

        RegButton(onClick = {
            isError = false
            isPasswordMismatch = false

            val surnameErrorMessage = validateSurname(lastname)
            val nameErrorMessage = validateName(firstname)
            val middlenameErrorMessage =
                if (middlename.isNotEmpty()) validateMiddlename(middlename) else ""

            val emailErrorMessage = validateEmail(email)
            val passwordErrorMessage = validatePasswordRepeat(password, passwordRepeat)

            isError = surnameErrorMessage.isNotEmpty() || nameErrorMessage.isNotEmpty() ||
                    emailErrorMessage.isNotEmpty() || password.isEmpty() || passwordRepeat.isEmpty() || passwordErrorMessage.isNotEmpty()

            if (isError) {
                showDialog = true
            } else {
                val fullName = listOf(lastname, firstname, middlename).filter { it.isNotEmpty() }
                    .joinToString(" ")
                regViewModel.register(fullName, email, passwordRepeat)
            }
        })


        when (val state = regState) {
            is RegState.Initial -> {
            }

            is RegState.Failure -> {
                showDialog = true
            }

            is RegState.Success -> {
                LaunchedEffect(Unit) {
                    onRegSuccess()
                }
            }

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
fun RegButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(stringResource(id = R.string.button_registration))
    }
}

@Composable
fun SurnameField(value: String, onValueChange: (String) -> Unit) {
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(value) {
        errorMessage = validateSurname(value)
    }

    Column {
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        TextField(
            value = value,
            onValueChange = { newValue ->
                onValueChange(newValue)
                errorMessage = validateSurname(newValue)
            },
            label = { Text(stringResource(id = R.string.user_lastname)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
        )
    }
}

@Composable
fun NameField(value: String, onValueChange: (String) -> Unit) {
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(value) {
        errorMessage = validateName(value)
    }

    Column {
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        TextField(
            value = value,
            onValueChange = { newValue ->
                onValueChange(newValue)
                errorMessage = validateName(newValue)
            },
            label = { Text(stringResource(id = R.string.user_name)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
        )
    }
}

@Composable
fun MiddlenameField(value: String, onValueChange: (String) -> Unit) {
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(value) {
        errorMessage = validateMiddlename(value)
    }

    Column {
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        TextField(
            value = value,
            onValueChange = { newValue ->
                onValueChange(newValue)
                errorMessage = validateMiddlename(newValue)
            },
            label = { Text(stringResource(id = R.string.user_middlename)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
        )
    }
}
@Composable
fun LoginField(value: String, onValueChange: (String) -> Unit) {
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(value) {
        errorMessage = validateEmail(value)
    }

    Column {
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        TextField(
            value = value,
            onValueChange = { newValue ->
                onValueChange(newValue)
                errorMessage = validateEmail(newValue)
            },
            label = { Text(stringResource(id = R.string.auth_login)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
        )

    }
}

@Composable
fun PasswordField(value: String, onValueChange: (String) -> Unit) {
    var errorMessage by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(value) {
        errorMessage = validatePassword(value)
    }

    Column {
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        TextField(
            value = value,
            onValueChange = { newValue ->
                onValueChange(newValue)
                errorMessage = validatePassword(newValue)
            },
            label = { Text(stringResource(id = R.string.auth_pass)) },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Password,
                    contentDescription = null
                )
            },
            trailingIcon = {
                val image = if (isPasswordVisible) {
                    Icons.Default.Visibility
                } else {
                    Icons.Default.VisibilityOff
                }
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(imageVector = image, contentDescription = null)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
        )
    }
}
@Composable
fun PasswordRepeatField(value: String, onValueChange: (String) -> Unit, password: String) {
    var errorMessage by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(value) {
        errorMessage = validatePasswordRepeat(password, value)
    }

    Column {
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        TextField(
            value = value,
            onValueChange = { newValue ->
                onValueChange(newValue)
                errorMessage = validatePasswordRepeat(password, newValue)
            },
            label = { Text(stringResource(id = R.string.repeat_password)) },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Password,
                    contentDescription = null
                )
            },
            trailingIcon = {
                val image = if (isPasswordVisible) {
                    Icons.Default.Visibility
                } else {
                    Icons.Default.VisibilityOff
                }
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(imageVector = image, contentDescription = null)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
        )
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

