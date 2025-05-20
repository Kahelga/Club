package com.example.club.feature.auth.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
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
import com.example.club.feature.auth.R


import com.example.club.feature.auth.presentation.AuthState
import com.example.club.feature.auth.presentation.AuthViewModel


@Composable
fun AuthScreen(
    authViewModel: AuthViewModel,
    onLoginSuccess: (login: String) -> Unit,
    onRegisterPressed:()->Unit,
    onBackPressed: () -> Unit,

    ) {
    val authState by authViewModel.state.collectAsState()
    val userLogin by authViewModel.login.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        TopBar(onBackPressed = onBackPressed)

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(id = R.string.auth_title),
            style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(32.dp))

        LoginField(value = email, onValueChange = { email = it })
        Spacer(modifier = Modifier.height(8.dp))

        PasswordField(
            value = password,
            onValueChange = {
                isError=false
                password = it
            },
            isError = isError
        )

        Spacer(modifier = Modifier.height(16.dp))

        AuthButton(onClick = {
            isError = false
            if (email.isNotEmpty() && password.isNotEmpty()) {
                authViewModel.authorize(email, password)

            } else {
                password=""
                isError = true
            }
        })


        Spacer(modifier = Modifier.height(8.dp))

        RegisterButton(onClick = { onRegisterPressed() })

        when (val state = authState) {
            is AuthState.Initial -> {
               // isError = false
            }

            is AuthState.Failure -> {
                isError = true
            }

            is AuthState.Success -> {
                LaunchedEffect(Unit) {
                    onLoginSuccess(userLogin)
                }
            }
            is AuthState.LoggedOut->{}

        }

    }
}

@Composable
fun PasswordField(value: String, onValueChange: (String) -> Unit, isError: Boolean) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    TextField(
        value = value,
        onValueChange ={ newValue->
            val trimmedValue = newValue.replace(" ", "")
            onValueChange(trimmedValue)
        } ,
        label = { Text(stringResource(id = R.string.auth_pass)) },
        //isError = isError,

        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
            )
        },

        trailingIcon = {
            val icon = if (isPasswordVisible) {
                Icons.Default.Visibility
            } else {
                Icons.Default.VisibilityOff
            }
            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                Icon(imageVector = icon, contentDescription = null)
            }

        },
        singleLine = false,
        maxLines = 1,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(
                BorderStroke(
                    width = 1.dp,
                    color = if (isError) Color.Red  else Color.Gray,
                ),
                shape = RoundedCornerShape(8.dp)
            ),

        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
    )
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
private  fun LoginField(value: String, onValueChange: (String) -> Unit) {
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
                    imageVector = Icons.Default.Person,
                    contentDescription = null
                )
            },
            singleLine = false,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
        )

    }
}


@Composable
fun AuthButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(stringResource(id = R.string.button_auth))
    }
}

@Composable
fun RegisterButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(stringResource(id = R.string.button_registration))
    }
}

private fun validateEmail(email: String): String {
    if (email.isBlank()) {
        return ""
    }
    val emailRegex = Regex(
        "^(?!\\.)([a-zA-Z0-9!#$%&'*+/=?^_`{|}~.-]+)@[a-zA-Z0-9.-]+\\.[a-zA-Z]{1,}$"
    )

    return if (emailRegex.matches(email)) {
        ""
    } else {
        "Некорректный формат"
    }
}