package com.example.club

import android.content.Context
import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit

class NetworkModule(context: Context) {
    private companion object {

        //  const val BASE_URL = ""
        const val CONNECT_TIMEOUT = 10L
        const val WRITE_TIMEOUT = 10L
        const val READ_TIMEOUT = 10L
        const val MOCK_SERVER_PORT = 8090
    }

    private val mockWebServerManager = MockWebServerManager(context, MOCK_SERVER_PORT)

    private lateinit var retrofit: Retrofit

    private val initializationDeferred = CompletableDeferred<Unit>()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                mockWebServerManager.start()

                Log.d("NetworkModule", "MockWebServer запущен на: ${mockWebServerManager.getUrl()}")
                repeat(10) {
                    mockWebServerManager.mockResponses(
                        "/events" to Response("events.json", HttpURLConnection.HTTP_OK),
                        "/event/1" to Response("eventDetails1.json", HttpURLConnection.HTTP_OK),
                        "/event/2" to Response("eventDetails2.json", HttpURLConnection.HTTP_OK),
                        "/event/3" to Response("eventDetails3.json", HttpURLConnection.HTTP_OK),
                        "/event/4" to Response("eventDetails4.json", HttpURLConnection.HTTP_OK),
                        "/event/5" to Response("eventDetails5.json", HttpURLConnection.HTTP_OK),
                      //  "/users/signin" to Response("user1.json", HttpURLConnection.HTTP_OK),
                       // "/users/signin" to Response("user2.json", HttpURLConnection.HTTP_OK),
                       // "/users/signin" to Response("error.json", HttpURLConnection.HTTP_UNAUTHORIZED),
                        "/img/poster1" to Response(
                            "poster1.jpg",
                            HttpURLConnection.HTTP_OK,
                            "image/jpg"
                        ),
                        "/img/poster2" to Response(
                            "poster2.jpeg",
                            HttpURLConnection.HTTP_OK,
                            "image/jpeg"
                        ),
                        "/img/poster3" to Response(
                            "poster3.png",
                            HttpURLConnection.HTTP_OK,
                            "image/png"
                        ),
                        "/img/poster4" to Response(
                            "poster4.png",
                            HttpURLConnection.HTTP_OK,
                            "image/png"
                        )

                    )
                }
                retrofit = Retrofit.Builder()
                    .client(provideOkHttpClientWithProgress())
                    .baseUrl(mockWebServerManager.getUrl())
                    .addConverterFactory(provideKotlinXSerializationFactory())
                    .build()

                // Успешная инициализация
                initializationDeferred.complete(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                initializationDeferred.completeExceptionally(e)
            }
        }
    }

    suspend fun getInstance(): Retrofit {
        initializationDeferred.await() // Ожидание завершения инициализации
        return retrofit
    }

    // private val baseUrl = mockWebServerManager.getUrl()
    private val baseUrl: String
        get() = mockWebServerManager.getUrl()

    /*val retrofit = Retrofit.Builder()
        .client(provideOkHttpClientWithProgress())
        .baseUrl(baseUrl)
        .addConverterFactory(provideKotlinXSerializationFactory())
        .build()*/

    private fun provideOkHttpClientWithProgress(): OkHttpClient =
        OkHttpClient().newBuilder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(provideLoggingInterceptor())
            .build()

    private fun provideKotlinXSerializationFactory(): Converter.Factory =
        Json.asConverterFactory("application/json; charset=UTF8".toMediaType())

    private fun provideLoggingInterceptor(): Interceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    fun shutdownMockServer() {
        mockWebServerManager.shutdown()
    }
}


/*private val mockWebServerManager = MockWebServerManager(context, MOCK_SERVER_PORT).apply {
    start()
    mockResponses(
        "/events" to Response("events.json", HttpURLConnection.HTTP_OK),
        "/event/1" to Response("eventDetails1.json", HttpURLConnection.HTTP_OK),
        "/event/2" to Response("eventDetails2.json", HttpURLConnection.HTTP_OK),
        "/event/3" to Response("eventDetails3.json", HttpURLConnection.HTTP_OK),
        "/event/4" to Response("eventDetails4.json", HttpURLConnection.HTTP_OK),
        "/event/5" to Response("eventDetails5.json", HttpURLConnection.HTTP_OK),

        )
}*/