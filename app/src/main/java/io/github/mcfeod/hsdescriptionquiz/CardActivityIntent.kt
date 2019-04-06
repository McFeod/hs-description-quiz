package io.github.mcfeod.hsdescriptionquiz

import android.content.Context
import android.content.Intent

data class CardActivityIntent(val card: Card) {
    fun pack(context: Context): Intent {
        val result = Intent(context, CardActivity::class.java)
        result.putExtra(CARD_KEY, card)
        return result
    }

    companion object {
        private const val CARD_KEY = "CARD"
        fun unpack(intent: Intent): Card = intent.getSerializableExtra(CARD_KEY) as Card
    }
}