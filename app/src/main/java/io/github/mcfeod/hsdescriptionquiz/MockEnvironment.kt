package io.github.mcfeod.hsdescriptionquiz

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.delay
import java.io.ByteArrayOutputStream

class MockEnvironment(private val context: Context): IFileRepository, IWebRepository {
    private fun randomCard(full: Boolean = false, locale: String = "ruRU"): Card {
        val number = (1..1000000).random()
        val allFields = full || (0..1).random() == 1
        val id = "id_$number"
        val name = "Card $number"
        if (!allFields) {
            return Card(id, name, locale)
        }
        return Card(id, name, locale, "Description of card #$number", "", "")
    }

    override suspend fun fetchCard(id: String, locale: String): Card {
        delay((100..1000).random().toLong())
        return randomCard(true, locale)
    }

    override fun readImage(path: String): ByteArray {
        throw FileRepoError()
    }

    override suspend fun fetchAllCards(locale: String): List<Card> = (1..100).map { randomCard(false, locale) }

    override fun writeImage(path: String, image: ByteArray) {}

    override fun generatePath(): String = ""

    override suspend fun fetchImage(url: String): ByteArray {
        delay((100..1000).random().toLong())
        val stream = ByteArrayOutputStream()
        BitmapFactory.decodeResource(context.resources, R.drawable.example).compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    override fun deleteImage(path: String) {}
}