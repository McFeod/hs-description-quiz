package io.github.mcfeod.hsdescriptionquiz

import android.content.Context
import android.content.Intent


const val CARD_KEY = "CARD"

object CardActivityIntent {
    fun pack(context: Context, card: Card): Intent {
        val result = Intent(context, CardActivity::class.java)
        result.putExtra(CARD_KEY, card)
        return result
    }
    fun unpack(intent: Intent): Card = intent.getParcelableExtra(CARD_KEY) as Card
}