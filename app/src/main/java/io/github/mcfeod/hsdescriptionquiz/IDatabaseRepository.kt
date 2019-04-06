package io.github.mcfeod.hsdescriptionquiz

class DBRepoError: Exception()

interface IDatabaseRepository {
    @Throws(DBRepoError::class)
    fun getRandomCards(amount: Int, locale: String): List<Card>

    @Throws(DBRepoError::class)
    fun writeCards(cards: Array<Card>)

    @Throws(DBRepoError::class)
    fun updateCard(card: Card, fields: Array<String>? = null)

    @Throws(DBRepoError::class)
    fun getCard(id: String, locale: String): Card?

    @Throws(DBRepoError::class)
    fun dropAllCards(locale: String)

    @Throws(DBRepoError::class)
    fun cardsCached(locale: String): Boolean
}