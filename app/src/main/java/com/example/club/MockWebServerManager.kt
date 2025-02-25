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
import okio.Buffer

class MockWebServerManager(private val context: Context, private val port: Int) {
    private lateinit var server: MockWebServer
    private val dispatcher = object : Dispatcher() {
        private val responses = mutableMapOf<String, Queue<Response>>()

        // Обработка входящих запросов
        override fun dispatch(request: RecordedRequest): MockResponse {
            // Обработка POST-запроса на "/users/signin"
            if (request.path == "/users/signin" && request.method == "POST") {
                val body = request.body.readUtf8()
                val login = body.substringAfter("\"email\":\"").substringBefore("\"")
                val password = body.substringAfter("\"password\":\"").substringBefore("\"")

                // Проверяем логин и пароль и выбираем соответствующий ответ
                val responseFile = when {
                    login == "user1@gmail.com" && password == "1234" -> "authUser1.json"
                    login == "user2@gmail.com" && password == "pass" -> "authUser2.json"
                    else -> "error.json"
                }

                // Получаем содержимое файла ответа
                val responseBody = getAssetFileContent(responseFile, "application/json")

                return MockResponse().apply {
                    setResponseCode(HttpURLConnection.HTTP_OK)
                    // Проверяем тип responseBody и устанавливаем его в качестве тела ответа
                    when (responseBody) {
                        is String -> setBody(responseBody) // Устанавливаем тело как строку
                        is ByteArray -> {
                            val buffer = Buffer().write(responseBody)
                            setBody(buffer) // Устанавливаем тело как Buffer для байтового массива
                        }

                        null -> {
                            // Обработка случая, когда responseBody равен null
                            setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
                        }
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
)


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