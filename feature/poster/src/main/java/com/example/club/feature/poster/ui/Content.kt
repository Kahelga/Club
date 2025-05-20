package com.example.club.feature.poster.ui

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.club.feature.poster.R
import com.example.club.shared.event.domain.entity.AgeRatings
import com.example.club.shared.event.domain.entity.Event
import com.example.club.shared.event.domain.entity.EventStatus
import com.example.club.util.formatting.MonthData
import com.example.club.util.formatting.formatDateSelected
import com.example.club.util.formatting.formatDateString
import com.example.club.util.formatting.getCalendarMonthData
import com.example.club.util.formatting.getMonthNameInNominative
import com.example.club.util.formatting.localDateToDate
import com.google.android.material.chip.Chip
import com.kizitonwose.calendar.core.OutDateStyle
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.time.YearMonth
import java.time.format.DateTimeParseException
import java.time.format.TextStyle


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Content(
    events: List<Event>,
    onItemClicked: (eventId: String) -> Unit,
) {
    CalendarWithEvents(events, onItemClicked)

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarWithEvents(events: List<Event>, onItemClicked: (eventId: String) -> Unit) {
    var isCalendarVisible by remember { mutableStateOf(false) }
    val selectedStartDateState = remember { mutableStateOf("") }
    val selectedEndDateState = remember { mutableStateOf("") }
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val focusRequester = remember { FocusRequester() }

    var searchQuery by remember { mutableStateOf("") }

    var selectedGenres by remember { mutableStateOf(setOf<String>()) }
    val genres = listOf("Рок", "Поп", "Джаз", "Электро", "Классика", "Фолк")

    var filteredEvents by remember { mutableStateOf(events) }


    val monthName = getMonthNameInNominative(currentMonth.monthValue)

    LaunchedEffect(searchQuery, selectedGenres) {
        filteredEvents = filterEvents(
            events,
            selectedStartDateState.value,
            selectedEndDateState.value,
            searchQuery,
            selectedGenres.toList()
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, _ ->
                    focusRequester.requestFocus()
                }
                detectVerticalDragGestures { _, _ ->
                    focusRequester.requestFocus()
                }
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SearchBar(query = searchQuery, onQueryChange = { newText ->
                searchQuery = newText
            }, focusRequester = focusRequester)
            Icon(
                Icons.Default.DateRange,
                contentDescription = "",
                modifier = Modifier
                    .size(35.dp)
                    .clickable {
                        focusRequester.requestFocus()
                        isCalendarVisible = !isCalendarVisible
                    }
                    .padding(end = 5.dp)
            )

            GenreDropdown(
                genres = genres,
                //  selectedGenres = selectedGenres,
                onGenreSelected = { genre ->
                    if (selectedGenres.contains(genre)) {
                        selectedGenres = selectedGenres - genre
                    } else {
                        selectedGenres = selectedGenres + genre
                    }
                },
                focusRequester = focusRequester
            )

        }

        if (selectedGenres.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                selectedGenres.forEach { genre ->
                    Row(
                        modifier = Modifier
                            .padding(5.dp)
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(5.dp)
                            )
                            .padding(2.dp)
                            .height(30.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = genre, style = MaterialTheme.typography.bodyMedium)
                        IconButton(onClick = {
                            selectedGenres = selectedGenres - genre
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "",
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }
                }
            }
        }
        if (isCalendarVisible) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(
                        MaterialTheme.colorScheme.outline,
                        shape = MaterialTheme.shapes.medium
                    )
                    .border(2.dp, Color.Gray, shape = MaterialTheme.shapes.medium)
                    .padding(8.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = ""
                            )
                        }
                        Text(
                            text = "$monthName ${currentMonth.year}",//currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.align(Alignment.CenterVertically),
                            color=MaterialTheme.colorScheme.surface
                        )
                        IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = ""
                            )
                        }
                    }


                    Row(modifier = Modifier.fillMaxWidth()) {
                        DayOfWeek.entries.forEach { day ->
                            Text(
                                text = day.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                color=MaterialTheme.colorScheme.surface
                            )
                        }
                    }


                    val monthData = getCalendarMonthData(
                        startMonth = currentMonth,
                        offset = 0,
                        firstDayOfWeek = DayOfWeek.MONDAY,
                        outDateStyle = OutDateStyle.EndOfRow
                    )

                    CalendarView(
                        monthData = monthData,
                        selectedStartDate = selectedStartDateState.value,
                        selectedEndDate = selectedEndDateState.value
                    ) { newStartDate, newEndDate ->
                        selectedStartDateState.value = newStartDate
                        selectedEndDateState.value = newEndDate
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = {
                            filteredEvents = filterEvents(
                                events,
                                selectedStartDateState.value,
                                selectedEndDateState.value,
                                searchQuery,
                                selectedGenres.toList()
                            )
                            isCalendarVisible = false
                        }) {
                            Text(stringResource(R.string.button_calendar_done))
                        }
                        Button(onClick = {
                            selectedStartDateState.value = ""
                            selectedEndDateState.value = ""
                            searchQuery = ""
                            selectedGenres = emptySet()
                            filteredEvents = filterEvents(events, "", "", "", listOf())
                        }) {
                            Text(stringResource(R.string.button_calendar_reset))
                        }
                    }
                }
            }
        }else{
            currentMonth=YearMonth.now()
        }

        EventList(filteredEvents, onItemClicked, focusRequester)
    }
}

@Composable
fun EventList(
    events: List<Event>,
    onItemClicked: (eventId: String) -> Unit,
    focusRequester: FocusRequester
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, _ ->
                    focusRequester.requestFocus()
                }
                detectVerticalDragGestures { _, _ ->
                    focusRequester.requestFocus()
                }
            }
    ) {
        items(events) { event ->
            if (event.status == EventStatus.ACTIVE) {
                EventItem(
                    item = event,
                    onItemClicked = { onItemClicked(event.id) }
                )
            }

        }
    }
}

@Composable
fun GenreDropdown(
    genres: List<String>,
    //  selectedGenres: Set<String>,
    onGenreSelected: (String) -> Unit,
    focusRequester: FocusRequester
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top=17.dp, end = 10.dp, start = 10.dp)
            .clip(RoundedCornerShape(10.dp))
    ) {
        Column( modifier = Modifier.fillMaxWidth()){
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = "Filter",
                modifier = Modifier
                    .size(35.dp)
                    .clickable {
                        expanded = true
                        focusRequester.requestFocus()
                    }


            )
            Spacer(modifier = Modifier.height(20.dp))
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .align(Alignment.End)
                    .background(MaterialTheme.colorScheme.outline)


            ) {
                genres.forEach { genre ->
                    Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface)
                    DropdownMenuItem(onClick = {
                        onGenreSelected(genre)
                        expanded = false
                    },
                        text = {
                            Text(
                                text = genre,
                                color = MaterialTheme.colorScheme.surface
                            )
                        }
                    )
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
private fun filterEvents(
    events: List<Event>,
    selectedStartDate: String,
    selectedEndDate: String,
    searchQuery: String,
    selectedGenres: List<String>
): List<Event> {
    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale("ru"))

    return events.filter { event ->

        val eventDate: LocalDate
        try {
            eventDate = LocalDate.parse(formatDateSelected(event.date), dateFormatter)
        } catch (e: DateTimeParseException) {
            return@filter false
        }


        val isDateMatch = if (selectedStartDate.isNotEmpty() && selectedEndDate.isNotEmpty()) {
            val startDate = LocalDate.parse(selectedStartDate, dateFormatter)
            val endDate = LocalDate.parse(selectedEndDate, dateFormatter)
            !eventDate.isBefore(startDate) && !eventDate.isAfter(endDate)
        } else if (selectedStartDate.isNotEmpty()) {
            eventDate == LocalDate.parse(selectedStartDate, dateFormatter)
        } else {
            true
        }

        val isTitleMatch = event.title.contains(searchQuery, ignoreCase = true)

        val isGenreMatch =
            selectedGenres.isEmpty() || event.genre.any { genre -> selectedGenres.contains(genre) }

        isDateMatch && (searchQuery.isEmpty() || isTitleMatch) && isGenreMatch
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarView(
    monthData: MonthData,
    selectedStartDate: String,
    selectedEndDate: String,
    onDateSelected: (String, String) -> Unit
) {
    val currentDate = LocalDate.now()

    val startDate = if (selectedStartDate.isNotEmpty()) LocalDate.parse(
        selectedStartDate,
        DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale("ru"))
    ) else null
    val endDate = if (selectedEndDate.isNotEmpty()) LocalDate.parse(
        selectedEndDate,
        DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale("ru"))
    ) else null

    monthData.calendarMonth.weekDays.forEach { week ->
        Row(modifier = Modifier.fillMaxWidth()) {
            week.forEach { day ->
                val formattedDate = formatDateString(localDateToDate(day.date))
                val isDateSelectable =
                    day.date.isEqual(currentDate) || day.date.isAfter(currentDate)
                val isToday = day.date.isEqual(currentDate)


                val isInDateRange =
                    startDate != null && endDate != null && day.date.isAfter(startDate.minusDays(1)) && day.date.isBefore(
                        endDate.plusDays(1)
                    )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                        .clickable(enabled = isDateSelectable) {
                            if (selectedStartDate.isEmpty()) {
                                onDateSelected(formattedDate, "")
                            } else if (selectedEndDate.isEmpty()) {
                                onDateSelected(selectedStartDate, formattedDate)
                            } else {
                                onDateSelected(formattedDate, "")
                            }
                        }
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            when {
                                formattedDate == selectedStartDate -> MaterialTheme.colorScheme.primary
                                formattedDate == selectedEndDate -> MaterialTheme.colorScheme.secondary
                                isInDateRange -> MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                                isToday -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                                else -> Color.Transparent
                            }
                        )
                        .padding(8.dp)
                ) {
                    Text(
                        text = day.date.dayOfMonth.toString(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = when {
                            formattedDate == selectedStartDate || formattedDate == selectedEndDate -> MaterialTheme.colorScheme.onPrimary
                            isInDateRange -> MaterialTheme.colorScheme.onPrimary
                            isToday -> MaterialTheme.colorScheme.surface
                            isDateSelectable -> MaterialTheme.colorScheme.surface
                            else -> MaterialTheme.colorScheme.onSurface
                        },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}


@SuppressLint("SuspiciousIndentation")
@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit, focusRequester: FocusRequester) {
    val keyboardController = LocalSoftwareKeyboardController.current

        TextField(
            value = query,
            onValueChange = { newText ->
                onQueryChange(newText)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            },
            textStyle=MaterialTheme.typography.bodyMedium,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            singleLine = false,
            maxLines = 1,
            modifier = Modifier
                .width(300.dp)
                .padding(end = 10.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .focusRequester(focusRequester)
                .focusable()
                .onFocusChanged { focusState ->
                    if (!focusState.hasFocus && query.isEmpty()) {
                        keyboardController?.hide()
                    }
                },
            )
}

@Composable
private fun EventItem(
    item: Event,
    onItemClicked: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 6.dp),
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
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
    val imagePath = item.imgPreview
    println(imagePath)
    //val fullUrl = "$baseUrl$imagePath"

    Box(
        modifier = Modifier
            .height(600.dp)
            .fillMaxWidth()
            .focusable()
            .padding(top = 8.dp, start = 5.dp, end = 5.dp)
    ) {
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


        val genres = item.genre.joinToString(", ")
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
    val ageRatingText = when (item.ageRating) {
        AgeRatings.G -> "Для всех"
        AgeRatings.PG -> "С родительским контролем"
        AgeRatings.PG13 -> "13+"
        AgeRatings.R -> "16+"
        AgeRatings.NC17 -> "18+"
    }
    Text(
        text = buildAnnotatedString {
            append(item.title)
            append(" (${ageRatingText})")
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
            text = formatDateSelected(item.date),
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(start = 4.dp)
        )
    }
    print(item.date)
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




