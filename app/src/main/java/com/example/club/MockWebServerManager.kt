package com.example.club

import android.util.Log
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import java.net.HttpURLConnection
import java.util.LinkedList
import java.util.Queue
import java.io.IOException

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import okio.Buffer
import java.time.Instant

class MockWebServerManager(private val context: Context, private val port: Int) {
    private lateinit var server: MockWebServer
    private val dispatcher = object : Dispatcher() {
        private val responses = mutableMapOf<String, Queue<Response>>()

        private var currentTokenInfo: TokenInfo? = null

        @RequiresApi(Build.VERSION_CODES.O)
        override fun dispatch(request: RecordedRequest): MockResponse {
            if (request.path == "/users/login" && request.method == "POST") {
                val body = request.body.readUtf8()
                val login = body.substringAfter("\"email\":\"").substringBefore("\"")
                val password = body.substringAfter("\"password\":\"").substringBefore("\"")


                val responseFile = when {
                    login == "user1@gmail.com" && password == "1234" -> {
                        currentTokenInfo =
                            TokenInfo("eyJpc3MiOiJBdXRoIFNlcnZlciIs", Instant.now(), 30000)
                        "authUser1.json"
                    }

                    login == "user2@gmail.com" && password == "pass" -> "authUser2.json"
                    else -> "error.json"
                }

                val responseBody = getAssetFileContent(responseFile, "application/json")

                return MockResponse().apply {
                    setResponseCode(HttpURLConnection.HTTP_OK)

                    when (responseBody) {
                        is String -> setBody(responseBody)
                        is ByteArray -> {
                            val buffer = Buffer().write(responseBody)
                            setBody(buffer)
                        }

                        null -> {
                            setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
                        }
                    }
                }
            }

            if (request.path == "/tickets" && request.method == "POST") {
                val body = request.body.readUtf8()
                val authToken = request.getHeader("Authorization")

                if (isTokenValid(authToken)) {
                    if (authToken == "Bearer eyJpc3MiOiJBdXRoIFNlcnZlciIs" || authToken == "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ102") {
                        val responseFile = "successful.json"

                        val responseBody = getAssetFileContent(responseFile, "application/json")

                        return MockResponse().apply {
                            setResponseCode(HttpURLConnection.HTTP_OK)

                            when (responseBody) {
                                is String -> setBody(responseBody)
                                is ByteArray -> {
                                    val buffer = Buffer().write(responseBody)
                                    setBody(buffer)
                                }

                                null -> {
                                    setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
                                }
                            }
                        }
                    }else {

                        return MockResponse().apply {
                            setResponseCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                        }
                    }

                }else {
                    return MockResponse().apply {
                        setResponseCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                    }
                }
            }

            if (request.path == "/auth/login/refresh" && request.method == "POST") {
                val body = request.body.readUtf8()
                val refreshToken = body.substringAfter("\"refresh_token\":\"").substringBefore("\"")


                val responseFile = if (refreshToken == "eyJpc3MiOiJBdXRoIFKvvdte") {
                    currentTokenInfo = TokenInfo("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ102", Instant.now(), Long.MAX_VALUE)
                    "newToken.json"
                } else {
                    "error.json"
                }
                val responseBody = getAssetFileContent(responseFile, "application/json")

                return MockResponse().apply {
                    setResponseCode(HttpURLConnection.HTTP_OK)
                    when (responseBody) {
                        is String -> setBody(responseBody)
                        is ByteArray -> {
                            val buffer = Buffer().write(responseBody)
                            setBody(buffer)
                        }

                        null -> {
                            setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
                        }
                    }
                }
            }

            if (request.method == "GET" && request.path?.startsWith("/users/") == true && request.path?.endsWith(
                    "/profile"
                ) == true
            ) {

                val login = request.path!!.substringAfter("/users/").substringBefore("/profile")
                val authToken = request.getHeader("Authorization")

                if (isTokenValid(authToken)) {
                    if (authToken == "Bearer eyJpc3MiOiJBdXRoIFNlcnZlciIs" || authToken == "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ102") {

                        val responseFile = when (login) {
                            "user1@gmail.com" -> "user1.json"
                            "user2@gmail.com" -> "user2.json"
                            else -> "error.json"
                        }

                        val responseBody = getAssetFileContent(responseFile, "application/json")
                        return MockResponse().apply {
                            setResponseCode(HttpURLConnection.HTTP_OK)
                            when (responseBody) {
                                is String -> setBody(responseBody)
                                is ByteArray -> {
                                    val buffer = Buffer().write(responseBody)
                                    setBody(buffer)
                                }

                                null -> setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
                            }
                        }
                    } else {

                        return MockResponse().apply {
                            setResponseCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                        }
                    }
                } else {

                    return MockResponse().apply {
                        setResponseCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                    }
                }

            }

            if (request.method == "GET" && request.path?.startsWith("/events/") == true && request.path?.endsWith(
                    "/hall"
                ) == true
            ) {

                val eventId = request.path!!.substringAfter("/events/").substringBefore("/hall")
                val authToken = request.getHeader("Authorization")

                if (isTokenValid(authToken)) {
                    if (authToken == "Bearer eyJpc3MiOiJBdXRoIFNlcnZlciIs" || authToken == "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ102") {

                        val responseFile = when (eventId) {
                            "4" -> "hall.json"
                            else -> "error.json"
                        }

                        val responseBody = getAssetFileContent(responseFile, "application/json")
                        return MockResponse().apply {
                            setResponseCode(HttpURLConnection.HTTP_OK)
                            when (responseBody) {
                                is String -> setBody(responseBody)
                                is ByteArray -> {
                                    val buffer = Buffer().write(responseBody)
                                    setBody(buffer)
                                }

                                null -> setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
                            }
                        }
                    } else {

                        return MockResponse().apply {
                            setResponseCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                        }
                    }
                } else {

                    return MockResponse().apply {
                        setResponseCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                    }
                }

            }
            // Обработка других запросов
            val response = responses[request.path]?.poll()

            if (response == null) {
                Log.w("MockWebServer", "No response found for path: ${request.path}")
                return MockResponse().apply {
                    setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
                }
            }

            Log.d("MockWebServer", "Loading file from assets: ${response.assetFile}")

            val body = try {
                getAssetFileContent(
                    response.assetFile,
                    response.contentType
                ) // Метод для загрузки из assets
            } catch (e: IOException) {
                Log.e("MockWebServer", "File not found: ${response.assetFile}", e)
                return MockResponse().apply {
                    setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
                }
            }


            return MockResponse().apply {
                setResponseCode(response.statusCode)
                when (body) {
                    is ByteArray -> {
                        val buffer = Buffer().write(body)
                        setBody(buffer)
                    }

                    is String -> setBody(body)
                }
                response.contentType?.let { setHeader("Content-Type", it) }
            }
        }

        fun addMockResponse(requestUrl: String, response: Response) {
            val queue = responses.getOrPut(requestUrl) { LinkedList() }
            queue.add(response)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun isTokenValid(authToken: String?): Boolean {
            currentTokenInfo?.let { tokenInfo ->
                if (authToken == "Bearer ${tokenInfo.token}") {
                    val currentTime = Instant.now()
                    return currentTime.isBefore(tokenInfo.expiryTime)
                }
            }
            return false
        }
    }


    // Метод для загрузки файла из assets
    private fun getAssetFileContent(assetFile: String?, contentType: String?): Any? {
        return when {
            assetFile == null -> null
            contentType?.startsWith("image/") == true -> {
                // Если это изображение, возвращаем его как байтовый массив
                context.assets.open(assetFile).use { inputStream ->
                    inputStream.readBytes() // Возвращаем байтовый массив для изображений
                }
            }

            else -> {
                // Для текстовых файлов
                context.assets.open(assetFile).bufferedReader().use { it.readText() }
            }
        }
    }

    fun start() {
        server = MockWebServer()
        server.dispatcher = dispatcher

        try {
            server.start(port)
            println("MockWebServer successfully launched on port $port")
        } catch (e: IOException) {
            println("Error when starting MockWebServer: ${e.message}")
        }
    }

    fun shutdown() {
        try {
            server.shutdown()
            println("MockWebServer successfully shut down.")
        } catch (e: Throwable) {
            println("Error when shutting down MockWebServer: ${e.message}")
        }
    }

    fun mockResponses(vararg pairs: Pair<String, Response>) {
        pairs.forEach { (request, response) ->
            dispatcher.addMockResponse(request, response)
        }
    }

    fun getUrl(): String {
        return server.url("/").toString()
    }
}

data class Response(
    val assetFile: String? = null,
    val statusCode: Int = HttpURLConnection.HTTP_OK,
    val contentType: String? = null
)

data class TokenInfo(val token: String, val issuedAt: Instant, val expiryDurationMillis: Long) {
    val expiryTime: Instant
        @RequiresApi(Build.VERSION_CODES.O)
        get() = issuedAt.plusMillis(expiryDurationMillis)
}

/*class MockWebServerManager(private val context: Context, private val port: Int) {
    private lateinit var server: MockWebServer
    private val dispatcher = object : Dispatcher() {
        private val responses = mutableMapOf<String, Queue<Response>>()

        // Обработка входящих запросов
        override fun dispatch(request: RecordedRequest): MockResponse {
            val response = responses[request.path]?.poll()

            if (response == null) {
                Log.w("MockWebServer", "No response found for path: ${request.path}")
                return MockResponse().apply {
                    setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
                }
            }

            Log.d("MockWebServer", "Loading file from assets: ${response.assetFile}")

            val body = try {
                getAssetFileContent(response.assetFile, response.contentType) // Метод для загрузки из assets
            } catch (e: IOException) {
                Log.e("MockWebServer", "File not found: ${response.assetFile}", e)
                return MockResponse().apply {
                    setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
                }
            }

            // Формируем и возвращаем ответ
            return MockResponse().apply {
                setResponseCode(response.statusCode)
                when (body) {
                    is ByteArray -> {
                        // Используем Buffer для установки байтового массива
                        val buffer = Buffer().write(body)
                        setBody(buffer) // Устанавливаем тело как Buffer для изображений
                    }
                    is String -> setBody(body) // Устанавливаем тело как строку для текстовых файлов
                }
                response.contentType?.let { setHeader("Content-Type", it) }
            }
        }

        fun addMockResponse(requestUrl: String, response: Response) {
            val queue = responses.getOrPut(requestUrl) { LinkedList() }
            queue.add(response)
        }
    }

    // Метод для загрузки файла из assets
    private fun getAssetFileContent(assetFile: String?, contentType: String?): Any? {
        return when {
            assetFile == null -> null
            contentType?.startsWith("image/") == true -> {
                // Если это изображение, возвращаем его как байтовый массив
                context.assets.open(assetFile).use { inputStream ->
                    inputStream.readBytes() // Возвращаем байтовый массив для изображений
                }
            }
            else -> {
                // Для текстовых файлов
                context.assets.open(assetFile).bufferedReader().use { it.readText() }
            }
        }
    }

    fun start() {
        server = MockWebServer()
        server.dispatcher = dispatcher

        try {
            server.start(port)
            println("MockWebServer successfully launched on port $port")
        } catch (e: IOException) {
            println("Error when starting MockWebServer: ${e.message}")
        }
    }

    fun shutdown() {
        try {
            server.shutdown()
            println("MockWebServer successfully shut down.")
        } catch (e: Throwable) {
            println("Error when shutting down MockWebServer: ${e.message}")
        }
    }

    fun mockResponses(vararg pairs: Pair<String, Response>) {
        pairs.forEach { (request, response) ->
            dispatcher.addMockResponse(request, response)
        }
    }

    fun getUrl(): String {
        return server.url("/").toString()
    }
}

data class Response(
    val assetFile: String? = null,
    val statusCode: Int = HttpURLConnection.HTTP_OK,
    val contentType: String? = null
)*/