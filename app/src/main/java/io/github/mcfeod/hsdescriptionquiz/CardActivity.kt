package io.github.mcfeod.hsdescriptionquiz

import android.os.Bundle
import android.view.View
import kotlinx.coroutines.launch
import kotlinx.android.synthetic.main.activity_card.cardImage
import kotlinx.android.synthetic.main.activity_card.cardTitle
import kotlinx.coroutines.delay

class CardActivity : AsyncActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card)

        val intentData = CardActivityIntent.unpack(this.intent)
        cardTitle.text = intentData.cardName
        loadImage(intentData)
    }

    private fun loadImage(intentData: CardActivityIntent) = launch {
        // TODO
        delay(400)
        cardImage.visibility = View.VISIBLE
    }
}
