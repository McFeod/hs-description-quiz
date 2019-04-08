package io.github.mcfeod.hsdescriptionquiz

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CardLoader(
    private val web: IWebRepository,
    private val db: IDatabaseRepository,
    private val files: IFileRepository,
    private val scope: CoroutineScope,
    private val log: (String) -> Unit) {

    suspend fun downloadCardsIfNeeded (locale: String) = withContext(scope.coroutineContext) {
        if (!db.cardsCached(locale)) {
            try {
                val cards = web.fetchAllCards(locale)
                val existingVersions = mutableMapOf<String, Card>()
                db.getCards(locale).forEach { existingVersions[it.id] = it }
                db.dropAllCards(locale)
                existingVersions.forEach {
                    val path = it.value.imageLocalPath
                    if (path != null) {
                        files.deleteImage(path)
                    }
                }
                db.writeCards(cards.map {
                    if (it.id in existingVersions && existingVersions[it.id]!!.shown) it.copy(shown = true) else it
                })
            } catch (e: WebRepoError) {
                log("Can't download cards")
            }
        }
    }

    suspend fun getRandomCards(count: Int, locale: String, onLoad: (Card) -> Unit) {
        val (notReady, ready) = db.getRandomCards(count, locale).partition { it.shouldFetchDetails() }
        ready.forEach(onLoad)
        notReady.forEach {
            scope.launch {
                try {
                    val updatedCard = web.fetchCard(it.id, it.locale)
                    onLoad(updatedCard)
                    db.updateCard(updatedCard)
                } catch (e: WebRepoError) {
                    log("Can't fetch card ${it.id}")
                }
            }
        }
    }
}