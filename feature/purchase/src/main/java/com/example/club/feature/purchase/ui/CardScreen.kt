package com.example.club.feature.purchase.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.club.feature.purchase.R
import com.example.club.feature.purchase.presentation.PurchaseState
import com.example.club.feature.purchase.presentation.PurchaseViewModel
import com.example.club.shared.tickets.domain.entity.PurchaseRequest
import com.example.club.util.validation.validateCVV
import com.example.club.util.validation.validateCardNumber
import com.example.club.util.validation.validateExpirationDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardScreen(
    eventId: String,
    seats: List<String>,
    viewModel: PurchaseViewModel,
    onBackPressed: () -> Unit,
) {
    val purchaseState by viewModel.state.collectAsState()
    val purchaseRequest = PurchaseRequest(eventId, seats)
    var showDialog by remember { mutableStateOf(false) }

    var cardNumber by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var expirationDate by remember { mutableStateOf("") }
    var validationMessage by remember { mutableStateOf("") }

    val progress = 3
    val totalSteps = 3
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { onBackPressed() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        stringResource(id = R.string.card_title),
                        style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                    )
                }
            }
        )
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.progress, progress, totalSteps),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = progress / totalSteps.toFloat(),
                modifier = Modifier.fillMaxWidth()
            )

        }
        CardInputFields(
            cardNumber,
            cvv,
            expirationDate,
            onCardNumberChange = { cardNumber = it },
            onExpirationDateChange = { expirationDate = it },
            onCvvChange = { cvv = it }
        )
        Button(
            onClick = {

                val parts = expirationDate.split("/")
                val month = if (parts.isNotEmpty()) parts[0] else ""
                val year = if (parts.size > 1) parts[1] else ""


                val cardValidation = validateCardNumber(cardNumber.replace(" ", ""))
                val cvvValidation = validateCVV(cvv)
                val expirationValidation = validateExpirationDate(month, year)

                validationMessage = cardValidation.ifEmpty {
                    cvvValidation.ifEmpty {
                        expirationValidation
                    }
                }

                if (validationMessage.isEmpty()) {
                    viewModel.buyTicket(purchaseRequest)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            Text(stringResource(id = R.string.button_buy))
        }

        if (validationMessage.isNotEmpty()) {
            Text(
                text = validationMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        when (val state = purchaseState) {
            is PurchaseState.Initial -> {
            }

            is PurchaseState.Loading -> {
                Loading()
            }

            is PurchaseState.Success -> {
                if (!showDialog) {
                    showDialog = true
                }
            }

            is PurchaseState.Failure -> {
                Error(
                    message = state.message ?: stringResource(id = R.string.error_unknown_error),
                    onRetry = { viewModel.buyTicket(purchaseRequest) }
                )
            }
        }


        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                text = {
                    Text(
                        stringResource(id = R.string.purchase_dialog),
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showDialog = false
                            // onPosterScreen()
                        }
                    ) {
                        Text(stringResource(id = R.string.purchase_dialog_ok))
                    }
                }
            )
        }
    }


}


@Composable
fun CardInputFields(
    cardNumber: String,
    cvv: String,
    expirationDate: String,
    onCardNumberChange: (String) -> Unit,
    onCvvChange: (String) -> Unit,
    onExpirationDateChange: (String) -> Unit,
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(25.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),


    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            var textFieldValueState by remember {
                mutableStateOf(
                    TextFieldValue(
                        text = cardNumber,
                        selection = TextRange(cardNumber.length)
                    )
                )
            }
            TextField(
                value = textFieldValueState,
                onValueChange = { newValue ->

                    val digitsOnly = newValue.text.filter { it.isDigit() }
                    val formattedNumber = digitsOnly.chunked(4)
                        .joinToString(" ")

                    if (formattedNumber.length <= 19) {
                        val cursorPosition = formattedNumber.length

                        textFieldValueState = TextFieldValue(
                            text = formattedNumber,
                            selection = TextRange(cursorPosition)
                        )
                        onCardNumberChange(formattedNumber)
                    } else {
                        val limitedFormattedNumber = formattedNumber.substring(0, 19)
                        val cursorPosition = limitedFormattedNumber.length
                        textFieldValueState = TextFieldValue(
                            text = limitedFormattedNumber,
                            selection = TextRange(cursorPosition)
                        )
                        onCardNumberChange(limitedFormattedNumber)
                    }
                },
                label = { Text(stringResource(R.string.label_cardNumber)) },
                placeholder = { Text(stringResource(R.string.example_cardNumber)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top=10.dp, end = 20.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        BorderStroke(
                            width = 1.dp,
                            color = Color.Gray,
                        ), shape = RoundedCornerShape(8.dp)

                    ),

            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val slash = "/"
                var textFieldDateValueState by remember {
                    mutableStateOf(
                        TextFieldValue(
                            text = expirationDate,
                            selection = TextRange(expirationDate.length)
                        )
                    )
                }
                TextField(

                    value =textFieldDateValueState,
                    onValueChange = { newValue ->

                        val digitsOnly = newValue.text.filter { it.isDigit() }
                        var formattedValue = if (digitsOnly.length > 2) {
                            "${digitsOnly.substring(0, 2)}$slash${digitsOnly.substring(2, minOf(digitsOnly.length, 4))}"
                        } else {
                            digitsOnly
                        }

                        if (formattedValue.length <= 5) {
                            val cursorPosition = formattedValue.length

                            textFieldDateValueState = TextFieldValue(
                                text = formattedValue,
                                selection = TextRange(cursorPosition)
                            )
                            onExpirationDateChange(formattedValue)

                        } else {
                            formattedValue = formattedValue.substring(0, 5)
                            val cursorPosition = formattedValue.length
                            textFieldDateValueState = TextFieldValue(
                                text = formattedValue,
                                selection = TextRange(cursorPosition)
                            )
                            onExpirationDateChange(formattedValue)
                        }
                    },
                    label = { Text(stringResource(R.string.label_date)) },
                    placeholder = { Text(stringResource(R.string.example_date)) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 20.dp)
                        .border(
                            BorderStroke(
                                width = 1.dp,
                                color = Color.Gray,

                            ),
                            shape = RoundedCornerShape(8.dp),
                        )

                        .clip(RoundedCornerShape(8.dp))


                )
                TextField(
                    value = cvv,
                    onValueChange = { onCvvChange(it) },
                    label = { Text(stringResource(R.string.label_cvv)) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            BorderStroke(
                                width = 1.dp,
                                color = Color.Gray,
                            ), shape = RoundedCornerShape(8.dp)

                        )

                )
            }


        }
    }
}
