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
import com.example.club.authorization.AuthRoute
import com.example.club.authorization.data.TokenManager
import com.example.club.authorization.domain.usecase.AuthUseCase
import com.example.club.authorization.domain.usecase.RefreshTokenUseCase
import com.example.club.authorization.presentation.AuthState
import com.example.club.authorization.presentation.AuthViewModel
import com.example.club.authorization.presentation.AuthViewModelFactory
import com.example.club.authorization.ui.AuthScreen
import com.example.club.eventDetails.EventDetailsRoute
import com.example.club.eventDetails.domain.usecase.GetEventUseCase
import com.example.club.eventDetails.presentation.EventDetailsViewModel
import com.example.club.eventDetails.presentation.EventDetailsViewModelFactory
import com.example.club.eventDetails.ui.EventDetailsScreen
import com.example.club.hall.HallRoute
import com.example.club.hall.domain.entity.Ticket
import com.example.club.hall.domain.usecase.GetHallUseCase
import com.example.club.hall.presentation.HallViewModel
import com.example.club.hall.presentation.HallViewModelFactory
import com.example.club.hall.ui.HallScreen
import com.example.club.poster.PosterRoute
import com.example.club.poster.domain.usecase.GetEventPosterUseCase
import com.example.club.poster.presentation.PosterViewModel
import com.example.club.poster.presentation.PosterViewModelFactory
import com.example.club.poster.ui.PosterScreen
import com.example.club.profile.ProfileRoute
import com.example.club.profile.domain.usecase.GetProfileUseCase
import com.example.club.profile.presentation.ProfileViewModel
import com.example.club.profile.presentation.ProfileViewModelFactory
import com.example.club.profile.ui.ProfileScreen
import com.example.club.profileUpdate.ProfileUpdateRoute
import com.example.club.profileUpdate.domain.usecase.UpdateProfileUseCase
import com.example.club.profileUpdate.presentation.ProfileUpdateViewModel
import com.example.club.profileUpdate.presentation.ProfileUpdateViewModelFactory
import com.example.club.profileUpdate.ui.ProfileUpdateScreen
import com.example.club.purchase.PurchaseRoute
import com.example.club.purchase.domain.usecase.PurchaseUseCase
import com.example.club.purchase.presentation.PurchaseViewModel
import com.example.club.purchase.presentation.PurchaseViewModelFactory
import com.example.club.purchase.ui.PurchaseScreen
import com.example.club.registration.RegRoute
import com.example.club.registration.domain.usecase.RegUseCase
import com.example.club.registration.presentation.RegViewModel
import com.example.club.registration.presentation.RegViewModelFactory
import com.example.club.registration.ui.RegScreen
import com.example.club.tickets.OrderRoute
import com.example.club.tickets.domain.usecase.GetOrderUseCase
import com.example.club.tickets.presentation.OrderViewModel
import com.example.club.tickets.presentation.OrderViewModelFactory
import com.example.club.tickets.ui.OrderScreen
import kotlinx.serialization.json.Json


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    getEventPosterUseCase: GetEventPosterUseCase,
    getEventUseCase: GetEventUseCase,
    authUseCase: AuthUseCase,
    regUseCase: RegUseCase,
    getProfileUseCase: GetProfileUseCase,
    updateProfileUseCase:UpdateProfileUseCase,
    getHallUseCase: GetHallUseCase,
    getOrderUseCase: GetOrderUseCase,
    purchaseUseCase: PurchaseUseCase,
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
        NavHost(navController = navController, startDestination =/* AuthRoute*/PosterRoute) {
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
                        } else {
                            navController.navigate(PosterRoute)//ProfileRoute(login = it)
                        }
                    },
                    onRegisterPressed={navController.navigate(RegRoute){popUpTo(AuthRoute) { inclusive = true }} },
                    onBackPressed = {
                        val previousRoute = navController.previousBackStackEntry?.destination?.route
                        Log.d("Navigation", "Previous Route: $previousRoute")
                        if (previousRoute == "com.example.club.tickets.OrderRoute") {
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
                val viewModel = viewModel(
                    PurchaseViewModel::class.java,
                    factory = PurchaseViewModelFactory(
                        purchaseUseCase,
                        refreshTokenUseCase,
                        tokenManager
                    )
                )
                PurchaseScreen(
                    viewModel,
                    destination.seats,
                    destination.eventId,
                    destination.totalPrice,
                    onBackPressed = { navController.popBackStack() },
                    onPosterScreen = { navController.navigate(PosterRoute) }
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
        }
    }
}
