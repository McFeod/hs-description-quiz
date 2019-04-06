package io.github.mcfeod.hsdescriptionquiz

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.delay
import kotlin.random.Random

class MockEnvironment(private val context: Context): IDatabaseRepository, IFileRepository, IWebRepository {
    private val random = Random(42)
    private var counter = 0

    private fun randomCard(full: Boolean = false): Card {
        val number = ++counter
        val allFields = full || random.nextBoolean()
        val id = "id_$number"
        val name = "Card $number"
        if (!allFields) {
            return Card(id, name, "")
        }
        return Card(id, name, "", "Description of card #$number", "", "")
    }


    override fun dropAllCards(locale: String) {}

    override fun getRandomCards(amount: Int, locale: String): List<Card> = (1..amount).map { randomCard() }

    override fun writeCards(cards: Array<Card>) {}

    override fun updateCard(card: Card, fields: Array<String>?) {}

    override suspend fun fetchCard(id: String, locale: String): Card {
        delay(random.nextLong(100, 1000))
        return randomCard(true)
    }

    override fun readImage(path: String): Bitmap {
        throw FileRepoError()
    }

    override suspend fun fetchAllCards(locale: String): Array<Card> = arrayOf()

    override fun getCard(id: String, locale: String): Card? = null

    override fun writeImage(path: String, bitmap: Bitmap) {}

    override fun generatePath(): String = ""

    override suspend fun fetchImage(url: String): Bitmap {
        delay(random.nextLong(100, 1000))
        return BitmapFactory.decodeResource(context.resources, R.drawable.example)
    }

    override fun cardsCached(locale: String): Boolean = true
}