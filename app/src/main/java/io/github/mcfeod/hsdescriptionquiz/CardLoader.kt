package io.github.mcfeod.hsdescriptionquiz

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext

class CardLoader(
    private val web: IWebRepository,
    private val db: IDatabaseRepository,
    private val scope: CoroutineScope,
    private val log: (String) -> Unit) {

    suspend fun downloadCardsIfNeeded (locale: String) = withContext(scope.coroutineContext) {
        if (!db.cardsCached(locale)) {
            try {
                val cards = web.fetchAllCards(locale)
                val existingVersions = mutableMapOf<String, Card>()
                db.getCards(locale).forEach { existingVersions[it.id] = it }
                db.dropAllCards(locale)
                db.writeCards(cards.map {
                    if (it.id in existingVersions && existingVersions[it.id]!!.shown) it.copy(shown = true) else it
                })
            } catch (e: WebRepoError) {
                log("Can't download cards")
            }
        }
    }

    suspend fun getRandomCards(count: Int, locale: String, onLoad: (Card) -> Unit) {
        db.getRandomCards(count, locale).forEach(onLoad)
    }
}