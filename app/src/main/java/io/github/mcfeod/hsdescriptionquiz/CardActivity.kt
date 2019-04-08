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
    private lateinit var loader: ImageLoader
    private var image: ByteArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card)
        initEnvironment()
        val card = CardActivityIntent.unpack(this.intent)
        cardTitle.text = card.name
        if (savedInstanceState != null && savedInstanceState.containsKey(IMAGE_KEY)) {
            image = savedInstanceState.getByteArray(IMAGE_KEY)
        }
        loadImage(card)
    }

    private fun initEnvironment() {
        // todo file interactions
        val db = CardDatabase.getInstance(this).cardDao()
        val env = MockEnvironment()
        val web = WebRepository(getString(R.string.rapid_api_key), Dispatchers.IO)
        loader = ImageLoader(web, env, db, this) { Log.e("IMAGE_LOADER", it) }
    }

    private fun loadImage(card: Card) = launch {
        if (image == null) {
            image = loader.load(card)
        }
        if (image != null) {
            cardImage.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image!!.size))
        }
        cardImage.visibility = View.VISIBLE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (image != null) {
            outState.putByteArray(IMAGE_KEY, image)
        }
        super.onSaveInstanceState(outState)
    }
}
