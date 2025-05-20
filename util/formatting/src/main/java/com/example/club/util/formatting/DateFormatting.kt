package com.example.club.util.formatting

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.util.Date
import java.util.Locale
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.kizitonwose.calendar.core.yearMonth


@RequiresApi(Build.VERSION_CODES.O)
data class MonthData internal constructor(
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
fun getCalendarMonthData(
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
fun formatTimeSelected(date: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    val outputFormat = SimpleDateFormat("HH:mm", Locale("ru"))

    return try {
        val parsedDate = inputFormat.parse(date)
        outputFormat.format(parsedDate ?: Date())
    } catch (e: Exception) {
        date
    }
}
fun formatDateString(date: Date): String {
    val outputFormat = SimpleDateFormat("dd.MM.yyyy", Locale("ru"))
    return outputFormat.format(date)
}
fun formatDateToDDMMYYYY(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        if (date != null) outputFormat.format(date) else ""
    } catch (e: Exception) {
        ""
    }
}

@RequiresApi(Build.VERSION_CODES.O)
 fun localDateToDate(localDate: LocalDate): Date {
    return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
}

fun getMonthNameInNominative(month: Int): String {
    val months = arrayOf(
        "январь", "февраль", "март", "апрель", "май", "июнь",
        "июль", "август", "сентябрь", "октябрь", "ноябрь", "декабрь"
    )
    return months[month - 1]
}