package io.github.mcfeod.hsdescriptionquiz

import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import kotlin.coroutines.CoroutineContext

const val API_HOST = "omgvamp-hearthstone-v1.p.rapidapi.com"

class WebRepository(private val apiKey: String, private val context: CoroutineContext) : IWebRepository {
    private val client = OkHttpClient()

    private suspend fun getAsync(path: String, locale: String) = withContext(context) {
        client.newCall(
            Request.Builder()
                .url("https://$API_HOST$path?locale=$locale&collectible=1")
                .addHeader("X-RapidAPI-Host", API_HOST)
                .addHeader("X-RapidAPI-Key", apiKey)
                .build()
        ).execute().body()!!.string()
    }

    override suspend fun fetchAllCards(locale: String): List<Card> = withContext(context) {
        val response = JSONObject(getAsync("/cards", locale))
        response.keys().asSequence()
            .map { cardSetName -> response.getJSONArray(cardSetName) }
            .flatMap { jsonArray -> (0 until jsonArray.length()).map { jsonArray.getJSONObject(it) }.asSequence() }
            .map {
                Card(
                    id = it.getString("cardId"),
                    name = it.getString("name"),
                    locale = it.getString("locale")
                )
            }
            .toList()
    }

    override suspend fun fetchCard(id: String, locale: String): Card = withContext(context) {
        val response = JSONArray(getAsync("/cards/$id", locale)).getJSONObject(0)
try {
            Card(
                id = response.getString("cardId"),
                name = response.getString("name"),
                locale = response.getString("locale"),
                description = response.getString("flavor"),
                imageURL = response.getString("img")
            )
        } catch (e: JSONException) {
            throw WebRepoError()
        }
    }

    override suspend fun fetchImage(url: String): ByteArray = withContext(context) {
        ByteArrayOutputStream().apply {
            client.newCall(
                Request.Builder().url(url).build()
            ).execute().body()!!.byteStream().copyTo(this)
        }.toByteArray()
    }
}

