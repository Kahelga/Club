package com.example.club

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.club.feature.admin.events.EventsRoute
import com.example.club.feature.admin.events.presentation.EventsViewModel
import com.example.club.feature.admin.events.presentation.EventsViewModelFactory
import com.example.club.feature.admin.events.ui.EventsScreen
import com.example.club.feature.admin.reports.ReportsRoute
import com.example.club.feature.admin.reports.presentation.ReportEventViewModel
import com.example.club.feature.admin.reports.presentation.ReportEventViewModelFactory
import com.example.club.feature.admin.reports.presentation.ReportPeriodViewModel
import com.example.club.feature.admin.reports.presentation.ReportPeriodViewModelFactory
import com.example.club.feature.admin.reports.presentation.ReportUserViewModel
import com.example.club.feature.admin.reports.presentation.ReportUserViewModelFactory
import com.example.club.feature.admin.reports.ui.ReportsScreen
import com.example.club.feature.auth.AuthRoute
import com.example.club.util.manager.token.TokenManager
import com.example.shared.user.auth.domain.usecase.AuthUseCase
import com.example.shared.user.auth.domain.usecase.RefreshTokenUseCase
import com.example.club.feature.auth.presentation.AuthState
import com.example.club.feature.auth.presentation.AuthViewModel
import com.example.club.feature.auth.presentation.AuthViewModelFactory
import com.example.club.feature.auth.ui.AuthScreen
import com.example.club.feature.eventdetails.EventDetailsRoute
import com.example.club.shared.event.domain.usecase.GetEventUseCase
import com.example.club.feature.eventdetails.presentation.EventDetailsViewModel
import com.example.club.feature.eventdetails.presentation.EventDetailsViewModelFactory
import com.example.club.feature.eventdetails.ui.EventDetailsScreen
import com.example.club.feature.hall.HallRoute
import com.example.club.shared.event.domain.usecase.GetHallUseCase
import com.example.club.feature.hall.presentation.HallViewModel
import com.example.club.feature.hall.presentation.HallViewModelFactory
import com.example.club.feature.hall.ui.HallScreen
import com.example.club.feature.poster.PosterRoute
import com.example.club.shared.event.domain.usecase.GetEventPosterUseCase
import com.example.club.feature.poster.presentation.PosterViewModel
import com.example.club.feature.poster.presentation.PosterViewModelFactory
import com.example.club.feature.poster.ui.PosterScreen
import com.example.club.feature.profile.ProfileRoute
import com.example.club.shared.user.profile.domain.usecase.GetProfileUseCase
import com.example.club.feature.profile.presentation.ProfileViewModel
import com.example.club.feature.profile.presentation.ProfileViewModelFactory
import com.example.club.feature.profile.ui.ProfileScreen
import com.example.club.feature.profileupdate.ProfileUpdateRoute
import com.example.club.shared.user.profile.domain.usecase.UpdateProfileUseCase
import com.example.club.feature.profileupdate.presentation.ProfileUpdateViewModel
import com.example.club.feature.profileupdate.presentation.ProfileUpdateViewModelFactory
import com.example.club.feature.profileupdate.ui.ProfileUpdateScreen
import com.example.club.feature.purchase.DataRoute
import com.example.club.feature.purchase.PurchaseRoute
import com.example.club.shared.tickets.domain.usecase.PurchaseUseCase
import com.example.club.feature.purchase.presentation.PurchaseViewModel
import com.example.club.feature.purchase.presentation.PurchaseViewModelFactory
import com.example.club.feature.purchase.ui.DataScreen
import com.example.club.feature.purchase.ui.PurchaseScreen
import com.example.club.feature.registration.RegRoute
import com.example.shared.user.auth.domain.usecase.RegUseCase
import com.example.club.feature.registration.presentation.RegViewModel
import com.example.club.feature.registration.presentation.RegViewModelFactory
import com.example.club.feature.registration.ui.RegScreen
import com.example.club.feature.tickets.OrderRoute
import com.example.club.shared.tickets.domain.usecase.GetOrderUseCase
import com.example.club.feature.tickets.presentation.OrderViewModel
import com.example.club.feature.tickets.presentation.OrderViewModelFactory
import com.example.club.feature.tickets.ui.OrderScreen
import com.example.club.shared.event.domain.usecase.GetEventsAdminUseCase
import com.example.club.shared.report.domain.usecase.GetReportEventUseCase
import com.example.club.shared.report.domain.usecase.GetReportPeriodUseCase
import com.example.club.shared.report.domain.usecase.GetReportUserUseCase


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    getEventPosterUseCase: GetEventPosterUseCase,
    getEventUseCase: GetEventUseCase,
    authUseCase: AuthUseCase,
    regUseCase: RegUseCase,
    getProfileUseCase: GetProfileUseCase,
    updateProfileUseCase: UpdateProfileUseCase,
    getHallUseCase: GetHallUseCase,
    getOrderUseCase: GetOrderUseCase,
    purchaseUseCase: PurchaseUseCase,
    getReportPeriodUseCase:GetReportPeriodUseCase,
    getReportEventUseCase:GetReportEventUseCase,
    getReportUserUseCase:GetReportUserUseCase,
    getEventsAdminUseCase: GetEventsAdminUseCase,
    tokenManager: TokenManager,
    refreshTokenUseCase: RefreshTokenUseCase
) {

    val navController = rememberNavController()
    val authViewModel: AuthViewModel =
        viewModel(factory = AuthViewModelFactory(authUseCase, tokenManager))
    val authState by authViewModel.state.collectAsState()

    val viewModelProfile = viewModel(
        ProfileViewModel::class.java,
        factory = ProfileViewModelFactory(
           "",
            getProfileUseCase,
            refreshTokenUseCase,
            tokenManager
        )
    )
    Surface {
        NavHost(navController = navController, startDestination = AuthRoute/*PosterRoute*/) {
            composable<PosterRoute> {
                val viewModel: PosterViewModel =
                    viewModel(factory = PosterViewModelFactory(getEventPosterUseCase))
                PosterScreen(
                    viewModel,
                    authViewModel,
                    onItemSelected = { navController.navigate(EventDetailsRoute(eventId = it)) },
                    onProfileSelected = {
                        if (authState is AuthState.Success) {
                            navController.navigate(ProfileRoute(login = it))
                        } else {
                            navController.navigate(AuthRoute)
                        }
                    },
                    onOrderSelected = {
                        if (authState is AuthState.Success) {
                            navController.navigate(OrderRoute)
                        } else {
                            navController.navigate(AuthRoute)
                        }
                    }
                )
            }
            composable<EventDetailsRoute> {
                val destination = it.toRoute<EventDetailsRoute>()
                val viewModel = viewModel(
                    EventDetailsViewModel::class.java,
                    factory = EventDetailsViewModelFactory(destination.eventId, getEventUseCase)
                )
                EventDetailsScreen(
                    viewModel,
                    onBackPressed = { navController.navigate(PosterRoute) },
                    toBuySelected = {/*navController.navigate(HallRoute(eventId = destination.eventId))*/
                        if (authState is AuthState.Success) {
                            navController.navigate(HallRoute(eventId = destination.eventId))//it
                        } else {
                            authViewModel.setPreviousRoute(EventDetailsRoute(destination.eventId))
                            navController.navigate(AuthRoute)
                        }
                    }
                )
            }
            composable<AuthRoute> {
                AuthScreen(
                    authViewModel,
                    onLoginSuccess = {
                        viewModelProfile.setLogin(it)
                        val previousRoute = authViewModel.getPreviousRoute()
                        if (previousRoute != null) {
                            navController.navigate(previousRoute) {
                                popUpTo(AuthRoute) { inclusive = true }
                            }
                        } else {//(if role=admin)
                            navController.navigate(ReportsRoute/*PosterRoute*/)//ProfileRoute(login = it)
                        }
                    },
                    onRegisterPressed={navController.navigate(RegRoute){popUpTo(AuthRoute) { inclusive = true }} },
                    onBackPressed = {
                        val previousRoute = navController.previousBackStackEntry?.destination?.route
                        Log.d("Navigation", "Previous Route: $previousRoute")
                        if (previousRoute == "com.example.club.feature.tickets.OrderRoute") {
                            navController.navigate(PosterRoute)
                        }
                        else {
                            navController.popBackStack()
                        }
                    }
                )
            }
            composable<RegRoute> {
                val viewModel: RegViewModel =
                    viewModel(factory = RegViewModelFactory(regUseCase))
                RegScreen(
                    viewModel,
                    onRegSuccess = {
                        navController.navigate(AuthRoute){ popUpTo(RegRoute) { inclusive = true }}
                    },
                    onBackPressed = {
                        navController.navigate(AuthRoute){ popUpTo(RegRoute) { inclusive = true }}
                    }
                )
            }
            composable<ProfileRoute> {
                val destinationProfile = it.toRoute<ProfileRoute>()

                ProfileScreen(
                    viewModelProfile,
                    onPosterSelected = { navController.navigate(PosterRoute) },
                    onLogout = {
                        authViewModel.logout()
                        navController.navigate(AuthRoute) {
                            popUpTo(ProfileRoute(login = destinationProfile.login)) { inclusive = true }
                        }

                    },
                    onOrderSelected = { navController.navigate(OrderRoute) },
                    onUpdateData ={navController.navigate(ProfileUpdateRoute(destinationProfile.login)) }
                )
            }
            composable<ProfileUpdateRoute>{
                val destination = it.toRoute<ProfileUpdateRoute>()

                val viewModel = viewModel(
                    ProfileUpdateViewModel::class.java,
                    factory = ProfileUpdateViewModelFactory(
                        destination.login,
                        updateProfileUseCase,
                        refreshTokenUseCase,
                        tokenManager
                    )
                )
                ProfileUpdateScreen(
                    viewModelProfile,
                    viewModel,
                    onBackPressed = { navController.popBackStack() },
                )

            }
            composable<HallRoute> {
                val destination = it.toRoute<HallRoute>()
                val viewModel = viewModel(
                    HallViewModel::class.java,
                    factory = HallViewModelFactory(
                        destination.eventId,
                        getHallUseCase,
                        refreshTokenUseCase,
                        tokenManager
                    )
                )
                HallScreen(
                    viewModel,
                    onBackPressed = { navController.popBackStack() },
                    toBuySelected = { selectedTickets, totalPrice ->
                        navController.navigate(
                            PurchaseRoute(
                                eventId = destination.eventId,
                                seats = selectedTickets,
                                totalPrice = totalPrice
                            )
                        )
                    }
                )

            }
            composable<PurchaseRoute> {
                val destination = it.toRoute<PurchaseRoute>()
               /* val viewModel = viewModel(
                    PurchaseViewModel::class.java,
                    factory = PurchaseViewModelFactory(
                        purchaseUseCase,
                        refreshTokenUseCase,
                        tokenManager
                    )
                )*/
                val viewModelEvent = viewModel(
                    EventDetailsViewModel::class.java,
                    factory = EventDetailsViewModelFactory(destination.eventId, getEventUseCase)
                )
                PurchaseScreen(
                   // viewModel,
                    viewModelEvent,
                    destination.seats,
                    destination.totalPrice,
                    onBackPressed = { navController.popBackStack() },
                    onData = {navController.navigate(DataRoute(eventId = destination.eventId, seats = destination.seats)) }//navController.navigate(PosterRoute)
                )

            }
            composable<DataRoute>{
                val destination = it.toRoute<DataRoute>()
                DataScreen(
                    viewModelProfile,

                    onBackPressed = { navController.popBackStack() },
                    toBuySelected = {}
                )

            }
            composable<OrderRoute> {
                val viewModel = viewModel(
                    OrderViewModel::class.java,
                    factory = OrderViewModelFactory(
                        getOrderUseCase,
                        refreshTokenUseCase,
                        tokenManager
                    )
                )
                OrderScreen(
                    viewModel,
                    authViewModel,
                    onProfileSelected = { navController.navigate(ProfileRoute(login = it)) },
                    onPosterSelected = { navController.navigate(PosterRoute) }
                )
            }
            composable<ReportsRoute>{
                val reportEventViewModel=viewModel(
                    ReportEventViewModel::class.java,
                    factory = ReportEventViewModelFactory(
                        refreshTokenUseCase,
                        tokenManager,
                        getReportEventUseCase
                    )
                )
                val reportUserViewModel=viewModel(
                    ReportUserViewModel::class.java,
                    factory = ReportUserViewModelFactory(
                        refreshTokenUseCase,
                        tokenManager,
                        getReportUserUseCase
                    )
                )
                val reportPeriodViewModel=viewModel(
                    ReportPeriodViewModel::class.java,
                    factory = ReportPeriodViewModelFactory(
                        refreshTokenUseCase,
                        tokenManager,
                        getReportPeriodUseCase
                    )
                )
                ReportsScreen(
                    reportPeriodViewModel,
                    reportEventViewModel,
                    reportUserViewModel,
                    onEventsSelected = { navController.navigate(EventsRoute)}

                )
            }
            composable<EventsRoute>{
                val viewModel=viewModel(
                   EventsViewModel::class.java,
                    factory = EventsViewModelFactory(
                        refreshTokenUseCase,
                        tokenManager,
                        getEventsAdminUseCase
                    )
                )
             //   val destination = it.toRoute<HallRoute>()
                val  hallViewModel = viewModel(
                    HallViewModel::class.java,
                    factory = HallViewModelFactory(
                        "",
                        getHallUseCase,
                        refreshTokenUseCase,
                        tokenManager
                    )
                )

                EventsScreen(
                    viewModel,
                    hallViewModel,
                    onReportsSelected={ navController.navigate(ReportsRoute)}
                )


            }
        }
    }
}
