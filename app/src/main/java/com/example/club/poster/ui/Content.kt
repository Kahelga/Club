package com.example.club.poster.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

import coil.request.ImageRequest
import com.example.club.R
import com.example.club.poster.domain.EventResponse
import com.example.club.poster.domain.entity.Event
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun Content(
    events: EventResponse,
    onItemClicked: (eventId: String) -> Unit,
) {
    CalendarWithEvents(events, onItemClicked)

}

@Composable
fun CalendarWithEvents(events: EventResponse, onItemClicked: (eventId: String) -> Unit) {
    val calendar = Calendar.getInstance()
    var currentDate by remember { mutableStateOf(calendar.time) }
    var selectedDate by remember { mutableStateOf(formatDateString(Date())) } // Устанавливаем сегодняшнюю дату

    val availableDates = remember(currentDate) { generateFutureDates(currentDate) }

    Column {
        Row(
            modifier = Modifier
                .padding(1.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(modifier = Modifier.size(20.dp),
                onClick = {
                    val cal = Calendar.getInstance()
                    cal.time = currentDate
                    cal.add(Calendar.DAY_OF_MONTH, -4)
                    if (formatDateString(cal.time) >= formatDateString(Date())) {
                        currentDate = cal.time
                    }
                }) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
            }

            LazyRow(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
                    .background(
                        MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .border(
                        2.dp,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                items(availableDates) { date ->
                    DateItem(date = date, isSelected = date == selectedDate) {
                        selectedDate = date
                    }

                    if (date != availableDates.last()) {
                        Divider(
                            modifier = Modifier
                                .height(50.dp)
                                .width(2.dp)
                                .background(MaterialTheme.colorScheme.outline),
                        )
                    }
                }
            }

            IconButton(modifier = Modifier.size(20.dp),
                onClick = {
                    val cal = Calendar.getInstance()
                    cal.time = currentDate
                    cal.add(Calendar.DAY_OF_MONTH, 4)
                    currentDate = cal.time
                }) {
                Icon(Icons.Default.ArrowForward, contentDescription = null)
            }
        }

        EventList(
            events.events.filter { formatDateSelected(it.date) == selectedDate },
            onItemClicked
        )
    }
}

fun generateFutureDates(startDate: Date): List<String> {
    val dates = mutableListOf<String>()
    val calendar = Calendar.getInstance()
    calendar.time = startDate
    repeat(365) {
        dates.add(formatDateString(calendar.time))
        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }
    return dates
}


@Composable
fun DateItem(date: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .height(50.dp)
            //.padding(vertical = 2.dp)
            .clickable(onClick = onClick)
            .padding(2.dp)
            .background(
                if (isSelected) MaterialTheme.colorScheme.onPrimary else Color.Transparent,
                //shape = RoundedCornerShape(11.dp)
            )

    ) {
        Text(
            text = date,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.align(Alignment.Center),
            color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}


@Composable
fun EventList(events: List<Event>, onItemClicked: (eventId: String) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxHeight()) {
        items(events) { event ->
            EventItem(
                item = event,
                onItemClicked = { onItemClicked(event.id) }
            )
        }
    }
}


@Composable
private fun EventItem(
    item: Event,
    onItemClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 6.dp),
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            EventImage(item)
            EventTitle(item)
            EventDate(item)
            EventButton(onItemClicked)
        }
    }
}

@Composable
fun EventImage(item: Event) {
    //val baseUrl = "http://localhost:8090/"
    val imagePath = item.img
    println(imagePath)
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
        val genres = item.genres.joinToString(", ")
        val price = stringResource(R.string.min_price_event, item.minPrice)

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
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black)
            )
        }
    }
}

@Composable
fun EventTitle(item: Event) {
    Text(
        text = buildAnnotatedString {
            append(item.name)
            append(" (${item.ageRating})")
        },
        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier.padding(top = 6.dp, start = 8.dp, end = 8.dp)
    )
}

@Composable
fun EventDate(item: Event) {
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
            text = formatDate(item.date),
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}


@Composable
private fun EventButton(onItemClicked: () -> Unit) {
    Button(
        onClick = onItemClicked,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(top = 20.dp, bottom = 8.dp, start = 10.dp, end = 10.dp)
    ) {
        Text(
            text = stringResource(id = R.string.button_details),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
    }
}

fun formatDate(date: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    return try {
        val parsedDate = inputFormat.parse(date)
        outputFormat.format(parsedDate ?: Date())
    } catch (e: Exception) {
        date
    }
}

fun formatDateSelected(date: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("EEE, d MMM", Locale("ru"))

    return try {
        val parsedDate = inputFormat.parse(date)
        outputFormat.format(parsedDate ?: Date())
    } catch (e: Exception) {
        date
    }
}

private fun formatDateString(date: Date): String {
    val outputFormat = SimpleDateFormat("EEE, d MMM", Locale("ru"))
    return outputFormat.format(date)
}
