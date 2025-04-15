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
import android.app.DatePickerDialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.club.feature.admin.reports.R
import com.example.club.feature.admin.reports.presentation.ReportEventState
import com.example.club.feature.admin.reports.presentation.ReportPeriodState
import com.example.club.feature.admin.reports.presentation.ReportUserState
import com.example.club.shared.report.domain.entity.ReportEvent
import com.example.club.util.formatting.formatDateSelected
import java.text.SimpleDateFormat

import java.util.*



@Composable
fun ReportsScreen(
    reportPeriodViewModel: ReportPeriodViewModel,
    reportEventViewModel: ReportEventViewModel,
    reportUserViewModel: ReportUserViewModel,
    onEventsSelected:()-> Unit

) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var startDate by remember { mutableStateOf("2025-03-10T00:00:00Z") }
    var endDate by remember { mutableStateOf("2025-04-10T00:00:00Z") }
    val reportEventState by reportEventViewModel.state.collectAsState()

    Scaffold(
        bottomBar = {
            BottomBar( onEventsSelected)
        }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(selectedTabIndex = selectedTab , modifier = Modifier.padding(top=20.dp)) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) { Text(
                    stringResource(R.string.tabRow_0)
                ) }
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) { Text( stringResource(R.string.tabRow_1)) }
                Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }) { Text( stringResource(R.string.tabRow_2)) }
            }

            DatePickerSection(
                startDate = startDate,
                endDate = endDate,
                onStartDateSelected = { newStartDate ->
                    startDate = newStartDate
                    // Повторный запрос при изменении начальной даты
                    reportEventViewModel.loadReportEvent(startDate, endDate)
                    reportUserViewModel.loadReportUser(startDate, endDate)
                    reportPeriodViewModel.loadReportPeriod(startDate, endDate)
                },
                onEndDateSelected = { newEndDate ->
                    endDate = newEndDate
                    // Повторный запрос при изменении конечной даты
                    reportEventViewModel.loadReportEvent(startDate, endDate)
                    reportUserViewModel.loadReportUser(startDate, endDate)
                    reportPeriodViewModel.loadReportPeriod(startDate, endDate)
                }
            )

            when (selectedTab) {
                0 -> {
                    if (startDate.isNotEmpty() && endDate.isNotEmpty()) {
                        LaunchedEffect(Unit) {
                            reportEventViewModel.loadReportEvent(startDate, endDate)
                        }

                    }
                    when (val eventState = reportEventState) {
                        is ReportEventState.Initial,
                        is ReportEventState.Loading -> Loading()

                        is ReportEventState.Failure ->Error(message = eventState.message
                            ?: stringResource(id = R.string.error_unknown_error),
                            onRetry = { reportEventViewModel.loadReportEvent(startDate, endDate) }

                        ){}
                        is ReportEventState.Content -> {
                            PeriodReport(reportPeriodViewModel, eventState.reportEvent, startDate, endDate)
                        }
                    }

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
fun BottomBar( onEventsSelected: () -> Unit,/* onOrderSelected: () -> Unit*/) {

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
                onClick =  onEventsSelected,
                iconTint = Color.Gray
            )
            BottomBarItem(
                icon = Icons.Filled.Person,
                label = stringResource(id = R.string.profile_title),
                onClick = {} /*onProfileSelected*/,
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

@Composable
fun DatePickerSection(
    startDate: String,
    endDate: String,
    onStartDateSelected: (String) -> Unit,
    onEndDateSelected: (String) -> Unit
) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(top=5.dp),
    ) {
        // Кнопка выбора начальной даты
        Button(onClick = {
            DatePickerDialog(
                context,
                R.style.CustomDatePickerDialog,
                { _, selectedYear, selectedMonth, selectedDay ->
                    calendar.set(Calendar.YEAR, selectedYear)
                    calendar.set(Calendar.MONTH, selectedMonth)
                    calendar.set(Calendar.DAY_OF_MONTH, selectedDay)
                    calendar.set(Calendar.HOUR_OF_DAY, 0)
                    calendar.set(Calendar.MINUTE, 0)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)

                    val formattedDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(calendar.time)
                    onStartDateSelected(formattedDate)
                },
                year, month, day
            ).show()
        }) {
            val date=formatDateSelected(startDate)
            Text("Выбрать начальную дату:$date ")
        }

            Button(onClick = {
                DatePickerDialog(
                    context,
                    R.style.CustomDatePickerDialog,
                    { _, selectedYear, selectedMonth, selectedDay ->
                        calendar.set(Calendar.YEAR, selectedYear)
                        calendar.set(Calendar.MONTH, selectedMonth)
                        calendar.set(Calendar.DAY_OF_MONTH, selectedDay)
                        calendar.set(Calendar.HOUR_OF_DAY, 0)
                        calendar.set(Calendar.MINUTE, 0)
                        calendar.set(Calendar.SECOND, 0)
                        calendar.set(Calendar.MILLISECOND, 0)

                        val formattedDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(calendar.time)
                        onEndDateSelected(formattedDate)
                    },
                    year, month, day
                ).show()
            }) {
                val date=formatDateSelected(endDate)
                Text("Выбрать конечную дату: $date")
            }

    }
}

@Composable
fun EventReport(viewModel: ReportEventViewModel, startDate: String, endDate: String) {
    if (startDate.isNotEmpty() && endDate.isNotEmpty()) {
        LaunchedEffect(Unit) {
            viewModel.loadReportEvent(startDate, endDate)
        }

    }

    // Обработка состояния и отображение графика
    val reportEventState by viewModel.state.collectAsState()

    when (val state = reportEventState) {
        is ReportEventState.Initial,
        is ReportEventState.Loading -> Loading()

        is ReportEventState.Failure ->Error(message = state.message
            ?: stringResource(id = R.string.error_unknown_error),
            onRetry = { viewModel.loadReportEvent(startDate, endDate) }

        ){}
        is ReportEventState.Content ->  EventReportContent(state.reportEvent)
    }
}

@Composable
fun UserReport(viewModel: ReportUserViewModel, startDate: String, endDate: String) {
    if (startDate.isNotEmpty() && endDate.isNotEmpty()) {
        LaunchedEffect(Unit) {
            viewModel.loadReportUser(startDate, endDate)
        }

    }

    // Обработка состояния и отображение графика
    val reportUserState by viewModel.state.collectAsState()

    when (val state = reportUserState) {
        is ReportUserState.Initial,
        is ReportUserState.Loading -> Loading()

        is ReportUserState.Failure ->Error(message = state.message
            ?: stringResource(id = R.string.error_unknown_error),
            onRetry = { viewModel.loadReportUser(startDate, endDate) }

        ){}
        is ReportUserState.Content ->  UserReportContent(state.reportUser)
    }
}
@Composable
fun PeriodReport(
    viewModel: ReportPeriodViewModel,
    reportEvents: List<ReportEvent>,
    startDate: String,
    endDate: String
) {
    if (startDate.isNotEmpty() && endDate.isNotEmpty()) {
        LaunchedEffect(Unit) {
            viewModel.loadReportPeriod(startDate, endDate)
        }

    }


    // Обработка состояния и отображение графика
    val reportPeriodState by viewModel.state.collectAsState()


    when (val state = reportPeriodState) {
        is ReportPeriodState.Initial,
        is ReportPeriodState.Loading -> Loading()

        is ReportPeriodState.Failure ->Error(message = state.message
            ?: stringResource(id = R.string.error_unknown_error),
            onRetry = { viewModel.loadReportPeriod(startDate, endDate) }

        ){}
        is ReportPeriodState.Content -> PeriodReportContent(state.reportPeriod,reportEvents)
    }
}