package com.example.club.feature.purchase.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.club.feature.eventdetails.ui.EventTitle
import com.example.club.feature.purchase.R
import com.example.club.shared.event.domain.entity.EventDetails
import com.example.club.util.formatting.formatDateSelected
import com.example.club.util.formatting.formatTimeSelected


@Composable
fun ContentPurchase(
    event: EventDetails,
    seats: List<String>,
    totalPrice: Int,
    toBuySelected: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            EventTitle(event)

            Text(
                text = stringResource(R.string.dataTime, formatDateSelected(event.date), formatTimeSelected(event.date)),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 6.dp, start = 8.dp, end = 8.dp)
            )

            Text(
                text = stringResource(id = R.string.purchase_seats),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(top = 6.dp, start = 8.dp, end = 8.dp)
            )

            for (seat in seats) {
                Text(
                    text = seat,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 2.dp, start = 8.dp, end = 8.dp)
                )
            }

            Text(
                text = stringResource(id = R.string.purchase_price, totalPrice),
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(top = 16.dp, start = 8.dp, end = 8.dp)
            )
        }
    }

    Spacer(modifier = Modifier.height(20.dp))
    BuyTicketButton(onClick = {
        toBuySelected()
        //  viewModel.buyTicket(purchaseRequest)
    })
}
@Composable
private fun BuyTicketButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(top = 20.dp, bottom = 8.dp, start = 10.dp, end = 10.dp)
    ) {
        Text(
            text = stringResource(id = R.string.button_buy_ticket2),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
    }
}