package io.github.mcfeod.hsdescriptionquiz

import java.lang.Exception

class WebRepoError: Exception()

interface IWebRepository {
    @Throws(WebRepoError::class)
    suspend fun fetchAllCards(locale: String): List<Card>

    @Throws(WebRepoError::class)
    suspend fun fetchCard(id: String, locale: String): Card

    @Throws(WebRepoError::class)
    suspend fun fetchImage(url: String): ByteArray
}