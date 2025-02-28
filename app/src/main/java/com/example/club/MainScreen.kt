package com.example.club

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


@Composable
fun MainScreen(
    getEventPosterUseCase: GetEventPosterUseCase,
    getEventUseCase: GetEventUseCase,
    authUseCase: AuthUseCase,
    getProfileUseCase: GetProfileUseCase,
    tokenManager: TokenManager,
    refreshTokenUseCase: RefreshTokenUseCase
) {

    val navController = rememberNavController()
    val authViewModel: AuthViewModel =
        viewModel(factory = AuthViewModelFactory(authUseCase, tokenManager))
    val authState by authViewModel.state.collectAsState()

    Surface {
        NavHost(navController = navController, startDestination = PosterRoute) {
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
                    onBackPressed = { navController.popBackStack() },
                    toBuySelected = {
                        if (authState is AuthState.Success) {
                            // navController.navigate(ProfileRoute(login = it))
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
                        val previousRoute = authViewModel.getPreviousRoute()
                        if (previousRoute != null) {
                            navController.navigate(previousRoute) {
                                popUpTo(AuthRoute) { inclusive = true }
                            }
                        } else {
                            navController.navigate(ProfileRoute(login = it))
                        }
                    },
                    onBackPressed = { navController.popBackStack() }
                )
            }
            composable<ProfileRoute> {
                val destination = it.toRoute<ProfileRoute>()
                val viewModel = viewModel(
                    ProfileViewModel::class.java,
                    factory = ProfileViewModelFactory(
                        destination.login,
                        getProfileUseCase,
                        refreshTokenUseCase,
                        tokenManager
                    )
                )
                ProfileScreen(
                    viewModel,
                    onPosterSelected = { navController.navigate(PosterRoute) },
                    onLogout = {
                        authViewModel.logout()
                        navController.navigate(AuthRoute) {
                            popUpTo(ProfileRoute(login = destination.login)) { inclusive = true }
                        }
                    }
                )
            }

        }
    }
}
