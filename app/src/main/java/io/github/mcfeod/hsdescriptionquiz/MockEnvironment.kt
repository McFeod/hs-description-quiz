package io.github.mcfeod.hsdescriptionquiz

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.delay

class MockEnvironment(private val context: Context): IDatabaseRepository, IFileRepository, IWebRepository {
    override fun getRandomCards(amount: Int, locale: String): Array<Card> = arrayOf()

    override fun writeCards(cards: Array<Card>) {}

    override fun updateCard(card: Card, fields: Array<String>?) {}

    override fun fetchCard(id: String, locale: String): Card {
        throw WebRepoError()
    }

    override fun readImage(path: String): Bitmap {
        throw FileRepoError()
    }

    override fun fetchAllCards(locale: String): Array<Card> = arrayOf()

    override fun getCard(id: String, locale: String): Card? = null

    override fun writeImage(path: String, bitmap: Bitmap) {}

    override fun generatePath(): String = ""

    override suspend fun fetchImage(url: String): Bitmap {
        delay(400)
        return BitmapFactory.decodeResource(context.resources, R.drawable.example)
    }
}