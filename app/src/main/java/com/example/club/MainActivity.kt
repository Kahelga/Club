package com.example.club

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.club.authorization.data.TokenManager
import com.example.club.authorization.data.converter.AuthConvert
import com.example.club.authorization.data.network.TokenRefreshApi
import com.example.club.authorization.data.network.UserAuthApi
import com.example.club.authorization.data.repository.RefreshTokenRepositoryImpl
import com.example.club.authorization.data.repository.UserAuthRepositoryImpl
import com.example.club.authorization.domain.repository.RefreshTokenRepository
import com.example.club.authorization.domain.repository.UserAuthRepository
import com.example.club.authorization.domain.usecase.AuthUseCase
import com.example.club.authorization.domain.usecase.RefreshTokenUseCase
import com.example.club.eventDetails.data.converter.EventDetailsConverter
import com.example.club.eventDetails.data.network.EventDetailsApi
import com.example.club.eventDetails.data.repository.EventRepositoryImpl
import com.example.club.eventDetails.domain.repository.EventDetailsRepository
import com.example.club.eventDetails.domain.usecase.GetEventUseCase
import com.example.club.hall.data.converter.HallConverter
import com.example.club.hall.data.network.HallApi
import com.example.club.hall.data.repository.HallRepositoryImpl
import com.example.club.hall.domain.repository.HallRepository
import com.example.club.hall.domain.usecase.GetHallUseCase
import com.example.club.poster.data.converter.EventPosterConverter
import com.example.club.poster.data.network.EventPosterApi
import com.example.club.poster.data.repository.EventPosterRepositoryImpl
import com.example.club.poster.domain.repository.EventPosterRepository
import com.example.club.poster.domain.usecase.GetEventPosterUseCase
import com.example.club.profile.data.converter.UserConverter
import com.example.club.profile.data.network.ProfileApi
import com.example.club.profile.data.repository.ProfileRepositoryImpl
import com.example.club.profile.domain.repository.ProfileRepository
import com.example.club.profile.domain.usecase.GetProfileUseCase
import com.example.club.purchase.data.converter.PurchaseConverter
import com.example.club.purchase.data.network.PurchaseApi
import com.example.club.purchase.data.repository.PurchaseRepositoryImpl
import com.example.club.purchase.domain.repository.PurchaseRepository
import com.example.club.purchase.domain.usecase.PurchaseUseCase
import com.example.club.ui.theme.ClubTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    private lateinit var networkModule: NetworkModule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Запускаем корутину для выполнения сетевых операций
        CoroutineScope(Dispatchers.IO).launch {
            networkModule = NetworkModule(this@MainActivity)
            val tokenManager = TokenManager(this@MainActivity)
            // Получаем экземпляр Retrofit
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
                UserAuthRepositoryImpl(userAuthApi, authConvert)
            val authUseCase = AuthUseCase(userAuthRepository)

            val profileApi = retrofit.create(ProfileApi::class.java)
            val userConverter = UserConverter()
            val profileRepository: ProfileRepository =
                ProfileRepositoryImpl(profileApi, userConverter)
            val getProfileUseCase = GetProfileUseCase(profileRepository)

            val tokenRefreshApi = retrofit.create(TokenRefreshApi::class.java)
            val refreshTokenRepository: RefreshTokenRepository =
                RefreshTokenRepositoryImpl(tokenRefreshApi, authConvert)
            val refreshTokenUseCase = RefreshTokenUseCase(refreshTokenRepository)

            val hallApi = retrofit.create(HallApi::class.java)
            val hallConverter = HallConverter()
            val hallRepository: HallRepository = HallRepositoryImpl(hallApi, hallConverter)
            val getHallUseCase = GetHallUseCase(hallRepository)

            val purchaseApi = retrofit.create(PurchaseApi::class.java)
            val purchaseConverter = PurchaseConverter()
            val purchaseRepository: PurchaseRepository =
                PurchaseRepositoryImpl(purchaseApi, purchaseConverter)
            val purchaseUseCase = PurchaseUseCase(purchaseRepository)

            // Переключаемся на главный поток для обновления UI
            withContext(Dispatchers.Main) {
                setContent {
                    ClubTheme {
                        MainScreen(
                            getEventPosterUseCase = getEventPosterUseCase,
                            getEventUseCase = getEventUseCase,
                            authUseCase = authUseCase,
                            getProfileUseCase = getProfileUseCase,
                            getHallUseCase = getHallUseCase,
                            purchaseUseCase=purchaseUseCase,
                            tokenManager = tokenManager,
                            refreshTokenUseCase = refreshTokenUseCase

                        )
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        networkModule.shutdownMockServer()
    }
}

