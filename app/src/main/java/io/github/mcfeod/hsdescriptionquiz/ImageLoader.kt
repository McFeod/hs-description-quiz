package io.github.mcfeod.hsdescriptionquiz

import android.graphics.Bitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ImageLoader(
    private val web: IWebRepository,
    private val local: IFileRepository,
    private val db: IDatabaseRepository,
    private val scope: CoroutineScope,
    private val log: (String) -> Unit) {

    suspend fun load(card: Card): Bitmap? {
        var bitmap: Bitmap? = null
        if (card.imageLocalPath != null) {
            try {
                return local.readImage(card.imageLocalPath)
            } catch (e: FileRepoError) {
                log("Can't read local image")
            }
        }
        if (card.imageURL != null) {
            try {
                bitmap = web.fetchImage(card.imageURL)
                cache(card, bitmap)
            } catch (e: WebRepoError) {
                log("Can't download image")
            }
        }
        return bitmap
    }

    private fun cache(card: Card, bitmap: Bitmap) = scope.launch {
        try {
            val updatedCard = card.copy(imageLocalPath = local.generatePath(), shown = true)
            db.updateCard(updatedCard, fields = arrayOf("imageLocalPath", "shown"))
            local.writeImage(updatedCard.imageLocalPath as String, bitmap)
        } catch (e: DBRepoError) {
            log("Can't update card in DB")
        } catch (e: FileRepoError) {
            log("Can't write local image")
        }
    }
}