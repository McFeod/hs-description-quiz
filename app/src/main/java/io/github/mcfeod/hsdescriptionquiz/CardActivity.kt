package io.github.mcfeod.hsdescriptionquiz

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.coroutines.launch
import kotlinx.android.synthetic.main.activity_card.cardImage
import kotlinx.android.synthetic.main.activity_card.cardTitle
import kotlinx.coroutines.Dispatchers


const val IMAGE_KEY = "IMAGE"

class CardActivity : AsyncActivity() {
    private lateinit var preferences: Preferences
    private var image: ByteArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card)
        val card = CardActivityIntent.unpack(this.intent)
        cardTitle.text = card.name
        if (savedInstanceState != null && savedInstanceState.containsKey(IMAGE_KEY)) {
            image = savedInstanceState.getByteArray(IMAGE_KEY)
        }
        preferences = Preferences(this)
        loadImage(card)
    }

    private fun loadImage(card: Card) = launch {
        try {
            if (image == null) {
                image = WebRepository(Dispatchers.IO).fetchImage(card, preferences.quality)
            }
            if (image != null) {
                cardImage.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image!!.size))
            }
            cardImage.visibility = View.VISIBLE
        } catch (e: WebRepoError) {
            Log.e("LOAD_IMAGE", "Can't download image")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (image != null) {
            outState.putByteArray(IMAGE_KEY, image)
        }
        super.onSaveInstanceState(outState)
    }
}
