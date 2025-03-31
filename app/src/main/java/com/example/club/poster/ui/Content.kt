package com.example.club.poster.ui

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration

import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

import coil.request.ImageRequest
import com.example.club.R

import com.example.club.poster.domain.entity.Event
import com.kizitonwose.calendar.core.CalendarDay


import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.kizitonwose.calendar.core.yearMonth
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.time.YearMonth
import java.time.format.DateTimeParseException
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit

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
                .padding(5.dp)
            ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SearchBar(query = searchQuery, onQueryChange = { newText ->
                searchQuery = newText
            }, focusRequester = focusRequester)
            Icon(
                Icons.Default.DateRange,
                contentDescription = "Открыть календарь",
                modifier = Modifier
                    .size(35.dp)
                    .clickable {
                        focusRequester.requestFocus()
                        isCalendarVisible = !isCalendarVisible }
            )

        }

        GenreDropdown(genres = genres,
            selectedGenres = selectedGenres,
            onGenreSelected = { genre, isSelected ->
                if (isSelected) {
                    selectedGenres = selectedGenres + genre
                } else {
                    selectedGenres = selectedGenres - genre
                }

            },
            focusRequester = focusRequester
        )

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
                                contentDescription = "Previous Month"
                            )
                        }
                        Text(
                            text = currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Next Month"
                            )
                        }
                    }


                    Row(modifier = Modifier.fillMaxWidth()) {
                        DayOfWeek.entries.forEach { day ->
                            Text(
                                text = day.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
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
                            Text("Готово")
                        }
                        Button(onClick = {
                            selectedStartDateState.value = ""
                            selectedEndDateState.value = ""
                            searchQuery = ""
                            selectedGenres = emptySet()
                            filteredEvents = filterEvents(events, "", "", "", listOf())
                        }) {
                            Text("Сбросить")
                        }
                    }
                }
            }
        }

        EventList(filteredEvents, onItemClicked,focusRequester)
    }
}
@Composable
fun EventList(events: List<Event>, onItemClicked: (eventId: String) -> Unit,focusRequester: FocusRequester) {
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
            EventItem(
                item = event,
                onItemClicked = { onItemClicked(event.id)}
            )
        }
    }
}

@Composable
fun GenreDropdown(
    genres: List<String>,
    selectedGenres: Set<String>,
    onGenreSelected: (String, Boolean) -> Unit,
    focusRequester: FocusRequester
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clip(RoundedCornerShape(10.dp))


    ) {
        Column {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { expanded = true
                        focusRequester.requestFocus()
                    }
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(5.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Выбрать жанр"
                )
                Text(
                    text = if (selectedGenres.isEmpty()) "Жанры" else selectedGenres.joinToString(", "),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )

            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .align(Alignment.End)
                    .background(
                        MaterialTheme.colorScheme.outline,
                        shape = MaterialTheme.shapes.medium
                    )

            ) {
                genres.forEach { genre ->
                    DropdownMenuItem(onClick = {
                        val isSelected = !selectedGenres.contains(genre)
                        onGenreSelected(genre, isSelected)
                        expanded = false
                    },
                        text = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Checkbox(
                                    checked = selectedGenres.contains(genre),
                                    onCheckedChange = null
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = genre,
                                    color = MaterialTheme.colorScheme.surface
                                )
                            }
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


@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit, focusRequester: FocusRequester) {
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        value = query,
        onValueChange = { newText ->
            onQueryChange(newText)
        },
        label = { Text("Поиск событий") },
        modifier = Modifier
            .padding(5.dp)
            .focusRequester(focusRequester)
            .focusable()
            .onFocusChanged { focusState ->
                if (!focusState.hasFocus && query.isEmpty()) {
                    keyboardController?.hide()
                }
            }
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
    Text(
        text = buildAnnotatedString {
            append(item.title)
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
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())/*.apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }*/
    val outputFormat = SimpleDateFormat("dd.MM.yyyy", Locale("ru"))

    return try {
        val parsedDate = inputFormat.parse(date)
        outputFormat.format(parsedDate ?: Date())
    } catch (e: Exception) {
        date
    }
}

private fun formatDateString(date: Date): String {
    val outputFormat = SimpleDateFormat("dd.MM.yyyy", Locale("ru"))
    return outputFormat.format(date)
}

@RequiresApi(Build.VERSION_CODES.O)
private fun localDateToDate(localDate: LocalDate): Date {
    return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
}


@RequiresApi(Build.VERSION_CODES.O)
public data class MonthData internal constructor(
    private val month: YearMonth,
    private val inDays: Int,
    private val outDays: Int,
) {

    private val totalDays = inDays + month.lengthOfMonth() + outDays


    private val firstDay = month.atStartOfMonth().minusDays(inDays.toLong())

    private val rows = (0 until totalDays).chunked(7)

    private val previousMonth = month.previousMonth

    private val nextMonth = month.nextMonth

    val calendarMonth: CalendarMonth =
        CalendarMonth(month, rows.map { week -> week.map { dayOffset -> getDay(dayOffset) } })

    private fun getDay(dayOffset: Int): CalendarDay {
        val date = firstDay.plusDays(dayOffset.toLong())
        val position = when (date.yearMonth) {
            month -> DayPosition.MonthDate
            previousMonth -> DayPosition.InDate
            nextMonth -> DayPosition.OutDate
            else -> throw IllegalArgumentException("Invalid date: $date in month: $month")
        }
        return CalendarDay(date, position)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
public fun getCalendarMonthData(
    startMonth: YearMonth,
    offset: Int,
    firstDayOfWeek: DayOfWeek,
    outDateStyle: OutDateStyle,
): MonthData {
    val month = startMonth.plusMonths(offset.toLong())
    val firstDay = month.atStartOfMonth()
    val firstDayOfWeekValue = firstDayOfWeek.value

    val inDays = (firstDay.dayOfWeek.value - firstDayOfWeekValue + 7) % 7

    val daysInMonth = month.lengthOfMonth()

    val totalDays = inDays + daysInMonth

    val endOfRowDays = if (totalDays % 7 != 0) 7 - (totalDays % 7) else 0
    val endOfGridDays = if (outDateStyle == OutDateStyle.EndOfRow) {
        0
    } else {
        val weeksInMonth = (totalDays + endOfRowDays) / 7
        (6 - weeksInMonth) * 7
    }

    val outDays = endOfRowDays + endOfGridDays

    return MonthData(month, inDays, outDays)
}

@RequiresApi(Build.VERSION_CODES.O)
public fun getMonthIndex(startMonth: YearMonth, targetMonth: YearMonth): Int {
    return ChronoUnit.MONTHS.between(startMonth, targetMonth).toInt()
}
