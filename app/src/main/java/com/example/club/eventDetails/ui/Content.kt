package com.example.club.eventDetails.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.club.R
import com.example.club.eventDetails.domain.entity.EventDetails
import com.example.club.poster.ui.formatDate
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


@Composable
fun Content(
    event: EventDetails,
    toBuySelected: (eventId: String) -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxHeight()) {
        item {
            DetailItem(event,
                toBuySelected = {  toBuySelected(event.id) }
                )

        }
    }
}


@Composable
private fun DetailItem(
    event: EventDetails,
    toBuySelected: () -> Unit
) {
    var isDescriptionExpanded by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxSize()) {
        EventImage(event)
        EventInfoCard(event)
        EventDescription(event, isDescriptionExpanded) { isDescriptionExpanded = it }
        BuyTicketButton( toBuySelected)

    }
}

@Composable
fun EventImage(event: EventDetails) {
    //val baseUrl = "http://localhost:8090/"
    val imagePath = event.imgPreview
    //val fullUrl = "$baseUrl$imagePath"

    Box(
        modifier = Modifier
            .height(600.dp)
            .fillMaxWidth()
            .padding(top = 8.dp, start = 5.dp, end = 5.dp)
    ) {
        // загрузка изображения
        val context = LocalContext.current
        val imageRequest = ImageRequest.Builder(context)
            .data(imagePath)
            .crossfade(true)
            .build()
        val painter = rememberAsyncImagePainter(imageRequest)

        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(5.dp)),
            contentScale = ContentScale.Crop
        )

        // отображение жанров, мин цены
        val genres = event.genre.joinToString(", ")
        val price = stringResource(R.string.min_price_event, event.minPrice)

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(2.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(5.dp))
                .padding(2.dp)
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(genres)
                    }
                    append("\n")
                    append(price)
                },
                style = MaterialTheme.typography.headlineSmall.copy(color = Color.Black)
            )
        }
    }
}

@Composable
fun EventTitle(event: EventDetails) {
    Text(
        text = buildAnnotatedString {
            append(event.title)
            append(" (${event.ageRating})")
        },
        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier.padding(top = 6.dp, start = 8.dp, end = 8.dp)
    )
}

@Composable
fun EventInfoCard(event: EventDetails) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            EventTitle(event)
            //EventVenue(event)
            EventDateTime(event)
            EventDuration(event)
            EventArtists(event)
        }
    }
}

@Composable
fun EventDateTime(event: EventDetails) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 4.dp, start = 8.dp, end = 8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.DateRange,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(30.dp)
        )
        Text(
            text = stringResource(R.string.dataTime_event, formatDate(event.date), formatTimeSelected(event.date)),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EventArtists(event: EventDetails) {
    Text(
        text = stringResource(R.string.details_artists),
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(2.dp)
    )
    FlowRow(
        modifier = Modifier.padding(2.dp),
        horizontalArrangement = Arrangement.spacedBy(1.dp),
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        event.artists.forEachIndexed { index, artist ->
            Text(text = artist.name)
            if (index < event.artists.size - 1) {
                Text(text = ", ")
            }
        }
    }
}

@Composable
private fun EventDescription(
    event: EventDetails,
    isDescriptionExpanded: Boolean,
    onDescriptionToggle: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) { // Добавляем внутренние отступы
            Text(
                text = stringResource(R.string.details_description),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp) // Увеличиваем отступ
            )
            if (isDescriptionExpanded) {
                Text(text = event.description)
            } else {
                Text(
                    text = event.description.take(100) + "...",
                    modifier = Modifier.clickable { onDescriptionToggle(true) },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                text = if (isDescriptionExpanded)
                    stringResource(R.string.details_description_less)
                else stringResource(R.string.details_description_more),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(top = 8.dp) // Увеличиваем отступ
                    .clickable { onDescriptionToggle(!isDescriptionExpanded) }
            )
        }
    }
}

@Composable
private fun EventDuration(event: EventDetails) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 4.dp, start = 8.dp, end = 8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(30.dp)
        )
        Text(
            text = stringResource(R.string.duration_event, event.duration),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

/*@Composable
fun EventVenue(event: EventDetails) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            text = stringResource(R.string.venue_event, event.venue.name),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        //может что-то еще
    }
}*/

@Composable
private fun BuyTicketButton( toBuySelected: () -> Unit) {
    Button(
        onClick =  toBuySelected,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(top = 20.dp, bottom = 8.dp, start = 10.dp, end = 10.dp)
    ) {
        Text(
            text = "Купить билет",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
    }
}

fun formatTimeSelected(date: String): String {
    // Указываем формат входящей даты
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC") // Устанавливаем временную зону UTC
    }
    // Указываем формат выходной времени
    val outputFormat = SimpleDateFormat("HH:mm", Locale("ru"))

    return try {
        val parsedDate = inputFormat.parse(date)
        outputFormat.format(parsedDate ?: Date())
    } catch (e: Exception) {
        date
    }
}