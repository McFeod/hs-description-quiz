package io.github.mcfeod.hsdescriptionquiz

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ImageLoader(
    private val web: IWebRepository,
    private val local: IFileRepository,
    private val db: IDatabaseRepository,
    private val scope: CoroutineScope,
    private val log: (String) -> Unit) {

    suspend fun load(card: Card): ByteArray? {
        var image: ByteArray? = null
        if (card.imageLocalPath != null) {
            try {
                return local.readImage(card.imageLocalPath)
            } catch (e: FileRepoError) {
                log("Can't read local image")
            }
        }
        if (card.imageURL != null) {
            try {
                image = web.fetchImage(card.imageURL)
                cache(card, image)
            } catch (e: WebRepoError) {
                log("Can't download image")
            }
        }
        return image
    }

    private fun cache(card: Card, image: ByteArray) = scope.launch {
        try {
            val updatedCard = card.copy(imageLocalPath = local.generatePath(), shown = true)
            db.updateCard(updatedCard, fields = arrayOf("imageLocalPath", "shown"))
            local.writeImage(updatedCard.imageLocalPath as String, image)
        } catch (e: DBRepoError) {
            log("Can't update card in DB")
        } catch (e: FileRepoError) {
            log("Can't write local image")
        }
    }
}