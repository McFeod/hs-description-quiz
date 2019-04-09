package io.github.mcfeod.hsdescriptionquiz

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async

class CardLoader(
    private val web: IWebRepository,
    private val db: IDatabaseRepository,
    private val scope: CoroutineScope,
    private val log: (String) -> Unit) {

    suspend fun downloadCardsIfNeeded(locale: String) {
        try {
            if (db.cardsCached(locale)) {
                return
            }
        } catch (e: DBRepoError) {
            log("Card existence check failed")
        }
        try {
            val cards = web.fetchAllCards(locale)
            // todo decide what's better:
            //  1) keep card's "shown" flag and image path
            //  or
            //  2) remove related images
            try {
                db.dropAllCards(locale)
            } catch (e: DBRepoError) {
                log("Can't drop cards")
            }

            db.writeCards(cards)
        } catch (e: WebRepoError) {
            log("Can't download cards")
        } catch (e: DBRepoError) {
            log("Can't save cards")
        }
    }

    suspend fun getRandomCards(count: Int, locale: String, onLoad: (Card) -> Unit) {
        try {
            val (notReady, ready) = db.getRandomCards(count, locale).partition { it.shouldFetchDetails() }
            ready.forEach(onLoad)
            notReady.map { card ->
                scope.async {
                    try {
                        val updatedCard = web.fetchCard(card.id, card.locale)
                        onLoad(updatedCard)
                        db.updateCard(updatedCard)
                    } catch (e: WebRepoError) {
                        log("Can't fetch card ${card.id}")
                    } catch (e: DBRepoError) {
                        log("Can't save card ${card.id}")
                    }
                }
            }
        } catch (e: DBRepoError) {
            log("Can't get random cards from db")
        }
    }
}