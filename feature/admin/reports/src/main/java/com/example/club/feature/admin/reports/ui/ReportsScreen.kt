package com.example.club.feature.admin.reports.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.club.feature.admin.reports.presentation.ReportEventViewModel
import com.example.club.feature.admin.reports.presentation.ReportPeriodViewModel
import com.example.club.feature.admin.reports.presentation.ReportUserViewModel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Divider

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.club.feature.admin.reports.R
import com.example.club.feature.admin.reports.presentation.ReportEventState
import com.example.club.feature.admin.reports.presentation.ReportPeriodState
import com.example.club.feature.admin.reports.presentation.ReportUserState
import java.time.Year

import java.util.*


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReportsScreen(
    reportPeriodViewModel: ReportPeriodViewModel,
    reportEventViewModel: ReportEventViewModel,
    reportUserViewModel: ReportUserViewModel,
    onEventsSelected: () -> Unit,
    logOut:() ->Unit

) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var startDate by remember { mutableStateOf("2025-01-01T00:00:00Z") }
    var endDate by remember { mutableStateOf("2025-12-31T00:00:00Z") }

    Scaffold(
        bottomBar = {
            BottomBar(onEventsSelected,logOut)
        }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(selectedTabIndex = selectedTab, modifier = Modifier.padding(top = 20.dp)) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) {
                    Text(
                        stringResource(R.string.tabRow_0)
                    )
                }
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) {
                    Text(
                        stringResource(R.string.tabRow_1)
                    )
                }
                Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }) {
                    Text(
                        stringResource(R.string.tabRow_2)
                    )
                }
            }
            DatePickerSection(
                onStartDateSelected = { newStartDate ->
                    startDate = newStartDate
                    updateReports(
                        startDate,
                        endDate,
                        reportEventViewModel,
                        reportUserViewModel,
                        reportPeriodViewModel
                    )
                },
                onEndDateSelected = { newEndDate ->
                    endDate = newEndDate
                    updateReports(
                        startDate,
                        endDate,
                        reportEventViewModel,
                        reportUserViewModel,
                        reportPeriodViewModel
                    )
                }
            )

            when (selectedTab) {
                0 -> {
                    PeriodReport(reportPeriodViewModel, reportEventViewModel, startDate, endDate)
                }

                1 -> {
                    EventReport(reportEventViewModel, startDate, endDate)
                }

                2 -> {
                    UserReport(reportUserViewModel, startDate, endDate)

                }
            }
        }

    }
}

@Composable
fun BottomBar(onEventsSelected: () -> Unit, logOut:() ->Unit) {

    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .height(60.dp)
            .border(BorderStroke(1.dp, Color.LightGray), RoundedCornerShape(15.dp))
            .clip(RoundedCornerShape(15.dp))
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp)
                .padding(end = 15.dp)
        ) {
            BottomBarItem(
                icon = Icons.Filled.Assessment,
                label = stringResource(id = R.string.bar_report),
                onClick = { }
            )
            BottomBarItem(
                icon = Icons.Filled.Event,
                label = stringResource(id = R.string.event_title),
                onClick = onEventsSelected,
                iconTint = Color.Gray
            )
            BottomBarItem(
                icon = Icons.Filled.ExitToApp,
                label = stringResource(id = R.string.exit_title),
                onClick = logOut ,
                iconTint = Color.Gray
            )
        }
    }


}

@Composable
fun BottomBarItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    iconTint: Color = MaterialTheme.colorScheme.onPrimaryContainer
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxHeight()
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(25.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
        }
        Text(
            label,
            color = iconTint,
            fontSize = 11.sp,
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePickerSection(
    onStartDateSelected: (String) -> Unit,
    onEndDateSelected: (String) -> Unit
) {
    val years = (2015..2060).toList()
    val quarters = listOf("1", "2", "3", "4")
    val months = listOf(
        "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
        "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"
    )
    val currentYear = Year.now().value
    var selectedYear by remember { mutableStateOf(currentYear) }
    var selectedQuarter by remember { mutableStateOf<Int?>(null) }
    var selectedMonth by remember { mutableStateOf<Int?>(null) }

    var expandedYear by remember { mutableStateOf(false) }
    var expandedQuarter by remember { mutableStateOf(false) }
    var expandedMonth by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp)
    ) {
        // Выбор года
        Row(modifier = Modifier.weight(1f)) {
            Text(stringResource(R.string.year), style = MaterialTheme.typography.bodyMedium)
            Text(
                selectedYear.toString(),
                modifier = Modifier.padding(end = 2.dp),
                style = MaterialTheme.typography.bodyMedium
            )
            IconButton(onClick = { expandedYear = true }) {
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Year")
            }
            DropdownMenu(
                expanded = expandedYear,
                onDismissRequest = { expandedYear = false },
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    )
                    .height(250.dp)
            ) {
                years.forEach { year ->
                    Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface)
                    DropdownMenuItem(onClick = {
                        selectedYear = year
                        selectedQuarter = null
                        selectedMonth = null
                        expandedYear = false
                        updateDateRange(
                            "Year",
                            selectedYear,
                            0,
                            0,
                            onStartDateSelected,
                            onEndDateSelected
                        )
                    },
                        text = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Text(year.toString())
                            }
                        }
                    )
                }
            }
        }

        // Выбор квартала
        Row(modifier = Modifier.weight(1f)) {
            Text(stringResource(R.string.quarter), style = MaterialTheme.typography.bodyMedium)
            Text(
                selectedQuarter?.toString() ?: "",
                modifier = Modifier.padding(end = 2.dp),
                style = MaterialTheme.typography.bodyMedium
            )
            IconButton(onClick = { expandedQuarter = true }) {
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Quarter")
            }
            DropdownMenu(
                expanded = expandedQuarter,
                onDismissRequest = { expandedQuarter = false },
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    )
            ) {
                DropdownMenuItem(onClick = {
                    selectedQuarter = null
                    expandedQuarter = false
                    updateDateRange(
                        "Year",
                        selectedYear,
                        0,
                        0,
                        onStartDateSelected,
                        onEndDateSelected
                    )
                },
                    text = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text("Сбросить")
                        }
                    })
                quarters.forEachIndexed { index, quarter ->
                    Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface)
                    DropdownMenuItem(onClick = {
                        selectedQuarter = index + 1
                        selectedMonth = null
                        expandedQuarter = false
                        updateDateRange(
                            "Quarter",
                            selectedYear,
                            selectedQuarter ?: 0,
                            selectedMonth ?: 0,
                            onStartDateSelected,
                            onEndDateSelected
                        )
                    },
                        text = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Text(quarter)
                            }
                        }
                    )
                }

            }
        }

        // Выбор месяца
        Row(modifier = Modifier.weight(1f)) {
            Text(stringResource(R.string.month), style = MaterialTheme.typography.bodyMedium)
            Text(selectedMonth?.let { months[it - 1] } ?: "",
                style = MaterialTheme.typography.bodyMedium)
            IconButton(onClick = { expandedMonth = true }) {
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Month")
            }
            DropdownMenu(
                expanded = expandedMonth,
                onDismissRequest = { expandedMonth = false },
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    )
                    .height(250.dp)
            ) {
                DropdownMenuItem(onClick = {
                    selectedMonth = null
                    expandedMonth = false
                    updateDateRange(
                        "Year",
                        selectedYear,
                        0,
                        0,
                        onStartDateSelected,
                        onEndDateSelected
                    )
                },
                    text = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text("Сбросить")
                        }
                    })
                months.forEachIndexed { index, month ->
                    Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface)
                    DropdownMenuItem(onClick = {
                        selectedMonth = index + 1
                        selectedQuarter = null
                        expandedMonth = false
                        updateDateRange(
                            "Month",
                            selectedYear,
                            selectedQuarter ?: 0,
                            selectedMonth ?: 0,
                            onStartDateSelected,
                            onEndDateSelected
                        )
                    },
                        text = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Text(month)
                            }
                        }
                    )
                }

            }

        }

    }
}

@Composable
fun EventReport(viewModel: ReportEventViewModel, startDate: String, endDate: String) {

    val reportEventState by viewModel.state.collectAsState()
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val error = stringResource(id = R.string.error_unknown_error)

    if (startDate.isNotEmpty() && endDate.isNotEmpty()) {
        LaunchedEffect(Unit) {
            viewModel.loadReportEvent(startDate, endDate)
        }

    }
    LaunchedEffect(reportEventState) {
        when (reportEventState) {
            is ReportEventState.Failure -> {
                showErrorDialog = true
                errorMessage = (reportEventState as ReportEventState.Failure).message ?: error
            }

            is ReportEventState.Content -> {
                if (showErrorDialog) {
                    showErrorDialog = false
                }
            }

            else -> {}
        }
    }



    when (val state = reportEventState) {
        is ReportEventState.Initial,
        is ReportEventState.Loading -> Loading()

        is ReportEventState.Failure -> {}
        is ReportEventState.Content -> EventReportContent(state.reportEvent)
    }

    if (showErrorDialog) {
        Error(
            message = errorMessage,
            onRetry = {
                viewModel.loadReportEvent(startDate, endDate)
                showErrorDialog = false
            },
            onCancel = {
                showErrorDialog = false
            }
        )
    }
}

@Composable
fun UserReport(viewModel: ReportUserViewModel, startDate: String, endDate: String) {
    val reportUserState by viewModel.state.collectAsState()
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val error = stringResource(id = R.string.error_unknown_error)

    if (startDate.isNotEmpty() && endDate.isNotEmpty()) {
        LaunchedEffect(Unit) {
            viewModel.loadReportUser(startDate, endDate)
        }

    }
    LaunchedEffect(reportUserState) {
        when (reportUserState) {
            is ReportUserState.Failure -> {
                showErrorDialog = true
                errorMessage = (reportUserState as ReportUserState.Failure).message ?: error
            }

            is ReportUserState.Content -> {
                if (showErrorDialog) {
                    showErrorDialog = false
                }
            }

            else -> {}
        }
    }

    when (val state = reportUserState) {
        is ReportUserState.Initial,
        is ReportUserState.Loading -> Loading()

        is ReportUserState.Failure -> {}
        is ReportUserState.Content -> UserReportContent(state.reportUser)
    }

    if (showErrorDialog) {
        Error(
            message = errorMessage,
            onRetry = {
                viewModel.loadReportUser(startDate, endDate)
                showErrorDialog = false
            },
            onCancel = {
                showErrorDialog = false
            }
        )
    }
}

@Composable
fun PeriodReport(
    viewModel: ReportPeriodViewModel,
    reportEventViewModel: ReportEventViewModel,
    startDate: String,
    endDate: String
) {
    val reportPeriodState by viewModel.state.collectAsState()
    val reportEventState by reportEventViewModel.state.collectAsState()

    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val error = stringResource(id = R.string.error_unknown_error)

    if (startDate.isNotEmpty() && endDate.isNotEmpty()) {
        LaunchedEffect(Unit) {
            viewModel.loadReportPeriod(startDate, endDate)
            reportEventViewModel.loadReportEvent(startDate, endDate)
        }

    }
    LaunchedEffect(reportPeriodState) {
        when (reportPeriodState) {
            is ReportPeriodState.Failure -> {
                showErrorDialog = true
                errorMessage = (reportPeriodState as ReportPeriodState.Failure).message ?: error
            }

            is ReportPeriodState.Content -> {
                if (showErrorDialog) {
                    showErrorDialog = false
                }
            }

            else -> {}
        }
    }
    LaunchedEffect(reportEventState) {
        when (reportEventState) {
            is ReportEventState.Failure -> {
                showErrorDialog = true
                errorMessage = (reportEventState as ReportEventState.Failure).message ?: error
            }

            is ReportEventState.Content -> {
                if (showErrorDialog) {
                    showErrorDialog = false
                }
            }

            else -> {}
        }
    }
    when (val state = reportPeriodState) {
        is ReportPeriodState.Initial,
        is ReportPeriodState.Loading -> Loading()

        is ReportPeriodState.Failure -> {}
        is ReportPeriodState.Content -> {
            if (reportEventState is ReportEventState.Content) {
                PeriodReportContent(
                    state.reportPeriod,
                    (reportEventState as ReportEventState.Content).reportEvent
                )
            } else {
                Loading()
            }
        }
    }
    if (showErrorDialog) {
        Error(
            message = errorMessage,
            onRetry = {
                viewModel.loadReportPeriod(startDate, endDate)
                showErrorDialog = false
            },
            onCancel = {
                showErrorDialog = false
            }
        )
    }
}


private fun updateReports(
    startDate: String,
    endDate: String,
    reportEventViewModel: ReportEventViewModel,
    reportUserViewModel: ReportUserViewModel,
    reportPeriodViewModel: ReportPeriodViewModel
) {
    reportEventViewModel.loadReportEvent(startDate, endDate)
    reportUserViewModel.loadReportUser(startDate, endDate)
    reportPeriodViewModel.loadReportPeriod(startDate, endDate)
}


private fun updateDateRange(
    dateRangeType: String,
    year: Int,
    quarter: Int,
    month: Int,
    onStartDateSelected: (String) -> Unit,
    onEndDateSelected: (String) -> Unit
) {
    when (dateRangeType) {
        "Year" -> {
            val startDate = "$year-01-01T00:00:00Z"
            val endDate = "$year-12-31T00:00:00Z"
            onStartDateSelected(startDate)
            onEndDateSelected(endDate)
        }

        "Quarter" -> {
            val (startDate, endDate) = getQuarterDateRange(year, quarter)
            Log.d("DatePicker", "Quarter selected: startDate = $startDate, endDate = $endDate")
            onStartDateSelected(startDate)
            onEndDateSelected(endDate)
        }

        "Month" -> {
            val (startDate, endDate) = getMonthDateRange(year, month)
            Log.d("DatePicker", "Month selected: startDate = $startDate, endDate = $endDate")
            onStartDateSelected(startDate)
            onEndDateSelected(endDate)
        }
    }
}


fun getQuarterDateRange(year: Int, quarter: Int): Pair<String, String> {
    val startMonth = when (quarter) {
        1 -> 1
        2 -> 4
        3 -> 7
        4 -> 10
        else -> throw IllegalArgumentException("Некорректный квартал")
    }
    val startDate = "$year-${String.format("%02d", startMonth)}-01T00:00:00Z"
    val endMonth = startMonth + 2
    val endDay = if (endMonth == 2) {
        if (isLeapYear(year)) "29" else "28"
    } else if (endMonth in listOf(4, 6, 9, 11)) {
        "30"
    } else {
        "31"
    }
    val endDate = "$year-${String.format("%02d", endMonth)}-${endDay}T00:00:00Z"
    return Pair(startDate, endDate)
}

fun getMonthDateRange(year: Int, month: Int): Pair<String, String> {
    val startDate = "$year-${String.format("%02d", month)}-01T00:00:00Z"
    val endDay = when (month) {
        2 -> if (isLeapYear(year)) "29" else "28"
        in listOf(4, 6, 9, 11) -> "30"
        else -> "31"
    }
    val endDate = "$year-${String.format("%02d", month)}-${endDay}T00:00:00Z"
    return Pair(startDate, endDate)
}


fun isLeapYear(year: Int): Boolean {
    return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)
}


