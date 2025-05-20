package com.example.club

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import com.example.club.util.manager.token.TokenManager
import com.example.shared.user.auth.data.converter.AuthConvert
import com.example.shared.user.auth.data.network.TokenRefreshApi
import com.example.shared.user.auth.data.network.UserAuthApi
import com.example.shared.user.auth.data.repository.RefreshTokenRepositoryImpl
import com.example.shared.user.auth.data.repository.UserAuthRepositoryImpl
import com.example.shared.user.auth.domain.repository.RefreshTokenRepository
import com.example.shared.user.auth.domain.repository.UserAuthRepository
import com.example.shared.user.auth.domain.usecase.AuthUseCase
import com.example.shared.user.auth.domain.usecase.RefreshTokenUseCase
import com.example.club.shared.event.data.converter.EventDetailsConverter
import com.example.club.shared.event.data.network.EventDetailsApi
import com.example.club.shared.event.data.repository.EventRepositoryImpl
import com.example.club.shared.event.domain.repository.EventDetailsRepository
import com.example.club.shared.event.domain.usecase.GetEventUseCase
import com.example.club.shared.event.data.converter.HallConverter
import com.example.club.shared.event.data.network.HallApi
import com.example.club.shared.event.data.repository.HallRepositoryImpl
import com.example.club.shared.event.domain.repository.HallRepository
import com.example.club.shared.event.domain.usecase.GetHallUseCase
import com.example.club.shared.event.data.converter.EventPosterConverter
import com.example.club.shared.event.data.network.EventPosterApi
import com.example.club.shared.event.data.network.EventsAdminApi
import com.example.club.shared.event.data.repository.EventPosterRepositoryImpl
import com.example.club.shared.event.data.repository.EventsAdminRepositoryImpl
import com.example.club.shared.event.domain.repository.EventPosterRepository
import com.example.club.shared.event.domain.repository.EventsAdminRepository
import com.example.club.shared.event.domain.usecase.GetEventPosterUseCase
import com.example.club.shared.event.domain.usecase.GetEventsAdminUseCase
import com.example.club.shared.report.data.converter.ReportConverter
import com.example.club.shared.report.data.network.EventReportApi
import com.example.club.shared.report.data.network.ReportPeriodApi
import com.example.club.shared.report.data.network.UserReportApi
import com.example.club.shared.report.data.repository.EventReportRepositoryImpl
import com.example.club.shared.report.data.repository.PeriodReportRepositoryImpl
import com.example.club.shared.report.data.repository.UserReportRepositoryImpl
import com.example.club.shared.report.domain.repository.EventReportRepository
import com.example.club.shared.report.domain.repository.PeriodReportRepository
import com.example.club.shared.report.domain.repository.UserReportRepository
import com.example.club.shared.report.domain.usecase.GetReportEventUseCase
import com.example.club.shared.report.domain.usecase.GetReportPeriodUseCase
import com.example.club.shared.report.domain.usecase.GetReportUserUseCase
import com.example.club.shared.tickets.data.converter.BookingConverter
import com.example.club.shared.user.profile.data.converter.UserConverter
import com.example.club.shared.user.profile.data.network.ProfileApi
import com.example.club.shared.user.profile.data.repository.ProfileRepositoryImpl
import com.example.club.shared.user.profile.domain.repository.ProfileRepository
import com.example.club.shared.user.profile.domain.usecase.GetProfileUseCase
import com.example.club.shared.user.profile.data.network.ProfileUpdateApi
import com.example.club.shared.user.profile.data.repository.ProfileUpdateRepositoryImpl
import com.example.club.shared.user.profile.domain.repository.ProfileUpdateRepository
import com.example.club.shared.user.profile.domain.usecase.UpdateProfileUseCase
import com.example.club.shared.tickets.data.converter.PurchaseConverter
import com.example.club.shared.tickets.data.network.PurchaseApi
import com.example.club.shared.tickets.data.repository.PurchaseRepositoryImpl
import com.example.club.shared.tickets.domain.repository.PurchaseRepository
import com.example.club.shared.tickets.domain.usecase.PurchaseUseCase
import com.example.shared.user.auth.data.converter.RegConvert
import com.example.shared.user.auth.data.network.UserRegApi
import com.example.shared.user.auth.data.repository.UserRegRepositoryImpl
import com.example.shared.user.auth.domain.repository.UserRegRepository
import com.example.shared.user.auth.domain.usecase.RegUseCase
import com.example.club.shared.tickets.data.converter.OrderConverter
import com.example.club.shared.tickets.data.network.BookingApi
import com.example.club.shared.tickets.data.network.BookingDetailsApi
import com.example.club.shared.tickets.data.network.OrderApi
import com.example.club.shared.tickets.data.network.CancelOrderApi
import com.example.club.shared.tickets.data.repository.BookedTicketRepositoryImpl
import com.example.club.shared.tickets.data.repository.BookingRepositoryImpl
import com.example.club.shared.tickets.data.repository.CancelOrderRepositoryImpl
import com.example.club.shared.tickets.data.repository.OrderRepositoryImpl
import com.example.club.shared.tickets.domain.repository.BookedTicketRepository
import com.example.club.shared.tickets.domain.repository.BookingRepository
import com.example.club.shared.tickets.domain.repository.OrderRepository
import com.example.club.shared.tickets.domain.repository.CancelOrderRepository
import com.example.club.shared.tickets.domain.usecase.BookingUseCase
import com.example.club.shared.tickets.domain.usecase.CancelOrderUseCase
import com.example.club.shared.tickets.domain.usecase.GetBookedTicketUseCase
import com.example.club.shared.tickets.domain.usecase.GetOrderUseCase
import com.example.club.ui.theme.ClubTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    private lateinit var networkModule: NetworkModule

    //private val networkModule = NetworkModule()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        CoroutineScope(Dispatchers.IO).launch {
            networkModule = NetworkModule(this@MainActivity)
            val tokenManager = TokenManager(this@MainActivity)


            val retrofit = networkModule.getInstance()

            val eventPosterApi = retrofit.create(EventPosterApi::class.java)
            val eventPosterConverter = EventPosterConverter()
            val eventPosterRepository: EventPosterRepository =
                EventPosterRepositoryImpl(eventPosterApi, eventPosterConverter)
            val getEventPosterUseCase = GetEventPosterUseCase(eventPosterRepository)

            val eventDetailsApi = retrofit.create(EventDetailsApi::class.java)
            val eventDetailsConverter = EventDetailsConverter()
            val eventRepository: EventDetailsRepository =
                EventRepositoryImpl(eventDetailsApi, eventDetailsConverter)
            val getEventUseCase = GetEventUseCase(eventRepository)

            val userAuthApi = retrofit.create(UserAuthApi::class.java)
            val authConvert = AuthConvert()
            val userAuthRepository: UserAuthRepository =
                UserAuthRepositoryImpl(
                    userAuthApi,
                    authConvert
                )
            val authUseCase =
                AuthUseCase(userAuthRepository)

            val userRegApi = retrofit.create(UserRegApi::class.java)
            val regConvert = RegConvert()
            val userRegRepository: UserRegRepository = UserRegRepositoryImpl(userRegApi, regConvert)
            val regUseCase = RegUseCase(userRegRepository)

            val profileApi = retrofit.create(ProfileApi::class.java)
            val userConverter = UserConverter()
            val profileRepository: ProfileRepository =
                ProfileRepositoryImpl(profileApi, userConverter)
            val getProfileUseCase = GetProfileUseCase(profileRepository)

            val profileUpdateApi = retrofit.create(ProfileUpdateApi::class.java)
            val profileUpdateRepository: ProfileUpdateRepository =
                ProfileUpdateRepositoryImpl(profileUpdateApi, userConverter)
            val updateProfileUseCase = UpdateProfileUseCase(profileUpdateRepository)

            val tokenRefreshApi = retrofit.create(TokenRefreshApi::class.java)
            val refreshTokenRepository: RefreshTokenRepository =
                RefreshTokenRepositoryImpl(
                    tokenRefreshApi,
                    authConvert
                )
            val refreshTokenUseCase =
                RefreshTokenUseCase(
                    refreshTokenRepository
                )

            val hallApi = retrofit.create(HallApi::class.java)
            val hallConverter = HallConverter()
            val hallRepository: HallRepository = HallRepositoryImpl(hallApi, hallConverter)
            val getHallUseCase = GetHallUseCase(hallRepository)

            val purchaseApi = retrofit.create(PurchaseApi::class.java)
            val purchaseConverter = PurchaseConverter()
            val orderConverter = OrderConverter()
            val purchaseRepository: PurchaseRepository =
                PurchaseRepositoryImpl(purchaseApi, orderConverter /*purchaseConverter*/)
            val purchaseUseCase = PurchaseUseCase(purchaseRepository)

            val bookingApi = retrofit.create(BookingApi::class.java)
            val bookingConverter = BookingConverter()
            val bookingRepository: BookingRepository =
                BookingRepositoryImpl(bookingApi, bookingConverter)
            val bookingUseCase = BookingUseCase(bookingRepository)

            val bookingDetailsApi = retrofit.create(BookingDetailsApi::class.java)
            val bookedTicketRepository: BookedTicketRepository =
                BookedTicketRepositoryImpl(bookingDetailsApi, bookingConverter)
            val getBookedTicketUseCase = GetBookedTicketUseCase(bookedTicketRepository)

            val orderApi = retrofit.create(OrderApi::class.java)

            val orderRepository: OrderRepository = OrderRepositoryImpl(orderApi, orderConverter)
            val getOrderUseCase = GetOrderUseCase(orderRepository)

            val cancelOrderApi = retrofit.create(CancelOrderApi::class.java)
            val cancelOrderRepository: CancelOrderRepository = CancelOrderRepositoryImpl(cancelOrderApi, orderConverter)
            val cancelOrderUseCase = CancelOrderUseCase(cancelOrderRepository)

            val eventReportApi = retrofit.create(EventReportApi::class.java)
            val reportConvert = ReportConverter()
            val eventReportRepository: EventReportRepository =
                EventReportRepositoryImpl(eventReportApi, reportConvert)
            val getReportEventUseCase = GetReportEventUseCase(eventReportRepository)

            val userReportApi = retrofit.create(UserReportApi::class.java)
            val userReportRepository: UserReportRepository =
                UserReportRepositoryImpl(userReportApi, reportConvert)
            val getReportUserUseCase = GetReportUserUseCase(userReportRepository)

            val reportPeriodApi = retrofit.create(ReportPeriodApi::class.java)
            val periodReportRepository: PeriodReportRepository =
                PeriodReportRepositoryImpl(reportPeriodApi, reportConvert)
            val getReportPeriodUseCase = GetReportPeriodUseCase(periodReportRepository)

            val eventsAdminApi = retrofit.create(EventsAdminApi::class.java)
            val eventsAdminRepository: EventsAdminRepository =
                EventsAdminRepositoryImpl(eventsAdminApi, eventDetailsConverter)
            val getEventsAdminUseCase = GetEventsAdminUseCase(eventsAdminRepository)

            withContext(Dispatchers.Main) {
                setContent {
                    ClubTheme {
                        MainScreen(
                            getEventPosterUseCase = getEventPosterUseCase,
                            getEventUseCase = getEventUseCase,
                            authUseCase = authUseCase,
                            regUseCase = regUseCase,
                            getProfileUseCase = getProfileUseCase,
                            updateProfileUseCase = updateProfileUseCase,
                            getHallUseCase = getHallUseCase,
                            purchaseUseCase = purchaseUseCase,
                            bookingUseCase = bookingUseCase,
                            getBookedTicketUseCase = getBookedTicketUseCase,
                            getOrderUseCase = getOrderUseCase,
                            cancelOrderUseCase=cancelOrderUseCase,
                            getReportPeriodUseCase = getReportPeriodUseCase,
                            getReportEventUseCase = getReportEventUseCase,
                            getReportUserUseCase = getReportUserUseCase,
                            getEventsAdminUseCase = getEventsAdminUseCase,
                            tokenManager = tokenManager,
                            refreshTokenUseCase = refreshTokenUseCase

                        )
                    }
                }
            }
        }
    }

    /*override fun onDestroy() {
        super.onDestroy()
        networkModule.shutdownMockServer()
    }*/
}

