package io.github.mcfeod.hsdescriptionquiz

import android.graphics.Bitmap
import java.lang.Exception

class WebRepoError: Exception()

interface IWebRepository {
    @Throws(WebRepoError::class)
    suspend fun fetchAllCards(locale: String): Array<Card>

    @Throws(WebRepoError::class)
    suspend fun fetchCard(id: String, locale: String): Card

    @Throws(WebRepoError::class)
    suspend fun fetchImage(url: String): Bitmap
}