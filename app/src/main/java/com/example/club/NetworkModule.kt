package com.example.club

import android.content.Context
import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.InetAddress
import java.util.concurrent.TimeUnit

class NetworkModule(context: Context) {
    private companion object {

        const val BASE_URL = "http://25.8.78.26:8762/"
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
                        "/events/preview" to Response("events.json", HttpURLConnection.HTTP_OK),
                        "/events/1" to Response("eventDetails1.json", HttpURLConnection.HTTP_OK),
                        "/events/2" to Response("eventDetails2.json", HttpURLConnection.HTTP_OK),
                        "/events/3" to Response("eventDetails3.json", HttpURLConnection.HTTP_OK),
                        "/events/4" to Response("eventDetails4.json", HttpURLConnection.HTTP_OK),
                        "/events/5" to Response("eventDetails5.json", HttpURLConnection.HTTP_OK),
                        "/img/poster1" to Response("poster1.jpg", HttpURLConnection.HTTP_OK, "image/jpg"),
                        "/img/poster2" to Response("poster2.jpeg", HttpURLConnection.HTTP_OK, "image/jpeg"),
                        "/img/poster3" to Response("poster3.png", HttpURLConnection.HTTP_OK, "image/png"),
                        "/img/poster4" to Response("poster4.png", HttpURLConnection.HTTP_OK, "image/png"),


                    )
                }
                retrofit = Retrofit.Builder().client(provideOkHttpClientWithProgress())
                    .baseUrl(mockWebServerManager.getUrl())
                    .addConverterFactory(provideKotlinXSerializationFactory()).build()

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
    /*private val baseUrl: String
        get() = mockWebServerManager.getUrl()*/

   /* val retrofit = Retrofit.Builder()
        .client(provideOkHttpClientWithProgress())
        .baseUrl(BASE_URL)
        .addConverterFactory(provideKotlinXSerializationFactory())
        .build()*/
   val cacheSize = 10 * 1024 * 1024 // 10 MB
    val cache = Cache(File(context.cacheDir, "http_cache"), cacheSize.toLong())
    private fun provideOkHttpClientWithProgress(): OkHttpClient =
        OkHttpClient().newBuilder()./*cache(cache).*/connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS).addInterceptor(provideLoggingInterceptor())
            .build()

    private fun provideKotlinXSerializationFactory(): Converter.Factory =
        Json.asConverterFactory("application/json; charset=UTF8".toMediaType())

    private fun provideLoggingInterceptor(): Interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    /*fun pingServer(ipAddress: String): Boolean {
        return try {
            // Создаем процесс для выполнения команды ping
            val process = ProcessBuilder()
                .command("ping", "-c", "4", ipAddress) // -c 4 означает 4 пинга
                .redirectErrorStream(true)
                .start()

            // Читаем вывод из процесса
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val output = StringBuilder()
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                output.append(line).append("\n")
            }

            // Ждем завершения процесса
            process.waitFor()

            // Выводим результат (для отладки)
            println(output.toString())

            // Если процесс завершился с кодом 0, значит, пинг успешен
            process.exitValue() == 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }*/


    /* fun shutdownMockServer() {
         mockWebServerManager.shutdown()
     }*/
}

