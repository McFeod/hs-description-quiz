package io.github.mcfeod.hsdescriptionquiz

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface IDatabaseRepository {
    @Query("""
        SELECT * FROM card
        WHERE locale = :locale
        ORDER BY shown, RANDOM()
        LIMIT :amount
    """)
    suspend fun getRandomCards(amount: Int, locale: String): List<Card>

    @Insert
    suspend fun writeCards(cards: Iterable<Card>)

    @Update
    suspend fun updateCard(card: Card)

    @Query("SELECT * FROM card WHERE locale = :locale AND id IN (:ids)")
    suspend fun getCards(locale: String, ids: List<String>): List<Card>

    @Query("SELECT * FROM card WHERE id = :id AND locale = :locale LIMIT 1")
    suspend fun getCard(id: String, locale: String): Card?

    @Query("DELETE FROM card WHERE locale = :locale")
    suspend fun dropAllCards(locale: String)

    @Query("SELECT EXISTS (SELECT id FROM card WHERE locale = :locale LIMIT 1)")
    suspend fun cardsCached(locale: String): Boolean
}