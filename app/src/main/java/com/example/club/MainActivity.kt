package com.example.club

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
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
import com.example.club.profileUpdate.data.network.ProfileUpdateApi
import com.example.club.profileUpdate.data.repository.ProfileUpdateRepositoryImpl
import com.example.club.profileUpdate.domain.repository.ProfileUpdateRepository
import com.example.club.profileUpdate.domain.usecase.UpdateProfileUseCase
import com.example.club.purchase.data.converter.PurchaseConverter
import com.example.club.purchase.data.network.PurchaseApi
import com.example.club.purchase.data.repository.PurchaseRepositoryImpl
import com.example.club.purchase.domain.repository.PurchaseRepository
import com.example.club.purchase.domain.usecase.PurchaseUseCase
import com.example.club.registration.data.converter.RegConvert
import com.example.club.registration.data.network.UserRegApi
import com.example.club.registration.data.repository.UserRegRepositoryImpl
import com.example.club.registration.domain.repository.UserRegRepository
import com.example.club.registration.domain.usecase.RegUseCase
import com.example.club.tickets.data.converter.OrderConverter
import com.example.club.tickets.data.network.OrderApi
import com.example.club.tickets.data.repository.OrderRepositoryImpl
import com.example.club.tickets.domain.repository.OrderRepository
import com.example.club.tickets.domain.usecase.GetOrderUseCase
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

            val eventPosterApi =retrofit.create(EventPosterApi::class.java)
            val eventPosterConverter = EventPosterConverter()
            val eventPosterRepository: EventPosterRepository =   EventPosterRepositoryImpl(eventPosterApi, eventPosterConverter)
            val getEventPosterUseCase = GetEventPosterUseCase(eventPosterRepository)

            val eventDetailsApi = retrofit.create(EventDetailsApi::class.java)
            val eventDetailsConverter = EventDetailsConverter()
            val eventRepository: EventDetailsRepository = EventRepositoryImpl(eventDetailsApi, eventDetailsConverter)
            val getEventUseCase = GetEventUseCase(eventRepository)

            val userAuthApi = retrofit.create(UserAuthApi::class.java)
            val authConvert = AuthConvert()
            val userAuthRepository: UserAuthRepository = UserAuthRepositoryImpl(userAuthApi, authConvert)
            val authUseCase = AuthUseCase(userAuthRepository)

            val userRegApi = retrofit.create(UserRegApi::class.java)
            val regConvert = RegConvert()
            val userRegRepository: UserRegRepository = UserRegRepositoryImpl(userRegApi, regConvert)
            val regUseCase = RegUseCase(userRegRepository)

            val profileApi = retrofit.create(ProfileApi::class.java)
            val userConverter = UserConverter()
            val profileRepository: ProfileRepository = ProfileRepositoryImpl(profileApi, userConverter)
            val getProfileUseCase = GetProfileUseCase(profileRepository)

            val profileUpdateApi = retrofit.create(ProfileUpdateApi::class.java)
            val profileUpdateRepository: ProfileUpdateRepository = ProfileUpdateRepositoryImpl(profileUpdateApi, userConverter)
            val updateProfileUseCase = UpdateProfileUseCase(profileUpdateRepository)

            val tokenRefreshApi =retrofit.create(TokenRefreshApi::class.java)
            val refreshTokenRepository: RefreshTokenRepository = RefreshTokenRepositoryImpl(tokenRefreshApi, authConvert)
            val refreshTokenUseCase = RefreshTokenUseCase(refreshTokenRepository)

            val hallApi =retrofit.create(HallApi::class.java)
            val hallConverter = HallConverter()
            val hallRepository: HallRepository = HallRepositoryImpl(hallApi, hallConverter)
            val getHallUseCase = GetHallUseCase(hallRepository)

            val purchaseApi =retrofit.create(PurchaseApi::class.java)
            val purchaseConverter = PurchaseConverter()
            val purchaseRepository: PurchaseRepository = PurchaseRepositoryImpl(purchaseApi, purchaseConverter)
            val purchaseUseCase = PurchaseUseCase(purchaseRepository)

            val orderApi=retrofit.create(OrderApi::class.java)
            val orderConverter=OrderConverter()
            val orderRepository:OrderRepository=OrderRepositoryImpl(orderApi,orderConverter)
            val getOrderUseCase=GetOrderUseCase(orderRepository)


            withContext(Dispatchers.Main) {
                setContent {
                    ClubTheme {
                        MainScreen(
                            getEventPosterUseCase = getEventPosterUseCase,
                            getEventUseCase = getEventUseCase,
                            authUseCase = authUseCase,
                            regUseCase=regUseCase,
                            getProfileUseCase = getProfileUseCase,
                            updateProfileUseCase=updateProfileUseCase,
                            getHallUseCase = getHallUseCase,
                            purchaseUseCase=purchaseUseCase,
                            getOrderUseCase = getOrderUseCase,
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

