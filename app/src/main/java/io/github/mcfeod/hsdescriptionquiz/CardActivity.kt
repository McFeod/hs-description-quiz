package io.github.mcfeod.hsdescriptionquiz

import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.coroutines.launch
import kotlinx.android.synthetic.main.activity_card.cardImage
import kotlinx.android.synthetic.main.activity_card.cardTitle

class CardActivity : AsyncActivity() {
    private lateinit var loader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card)
        initEnvironment()
        val card = CardActivityIntent.unpack(this.intent)
        cardTitle.text = card.name
        loadImage(card)
    }

    private fun initEnvironment() {
        // todo implement db, file and web interactions
        val env = MockEnvironment(this)
        loader = ImageLoader(env, env, env, this) { msg: String -> Log.e("IMAGE_LOADER", msg)}
    }

    private fun loadImage(card: Card) = launch {
        cardImage.setImageBitmap(loader.load(card))
        cardImage.visibility = View.VISIBLE
    }
}
