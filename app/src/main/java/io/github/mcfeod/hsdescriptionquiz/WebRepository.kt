package io.github.mcfeod.hsdescriptionquiz

import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import kotlin.coroutines.CoroutineContext


class WebRepository(private val context: CoroutineContext) : IWebRepository {
    private val client = OkHttpClient()

    private suspend fun get(url: String): ResponseBody = withContext(context ){
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        val body: ResponseBody = response.body()!!
        if (!response.isSuccessful || response.body() == null) {
            throw WebRepoError()
        }
        return@withContext body
    }

    override suspend fun fetchAllCards(locale: String): List<Card> = withContext(context) {
        val url = "https://api.hearthstonejson.com/v1/latest/$locale/cards.collectible.json"
        val array = JSONArray(get(url).string())
        (0 until array.length()).map { array.getJSONObject(it) }.asSequence()
            .map {
                Card(
                    id = it.getString("id"),
                    name = it.getString("name"),
                    locale = locale,
                    description = try {
                        it.getString("flavor")
                    } catch (e: JSONException) {
                        ""
                    }
                )
            }
            .filter { it.description != "" }
            .toList()
    }

    override suspend fun fetchImage(card: Card, quality: String): ByteArray = withContext(context) {
        get("https://art.hearthstonejson.com/v1/render/latest/${card.locale}/$quality/${card.id}.png").bytes()
    }
}