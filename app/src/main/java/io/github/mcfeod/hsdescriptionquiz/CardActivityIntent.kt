package io.github.mcfeod.hsdescriptionquiz

import android.content.Context
import android.content.Intent

data class CardActivityIntent(val cardName: String, val imageURL: String, val localPath: String?) {
    fun pack(context: Context): Intent {
        val result = Intent(context, CardActivity::class.java)
        result.putExtra("cardName", cardName)
        result.putExtra("imageURL", imageURL)
        result.putExtra("localPath", localPath)
        return result
    }

    companion object {
        fun unpack(intent: Intent): CardActivityIntent = CardActivityIntent(
            intent.getStringExtra("cardName"),
            intent.getStringExtra("imageURL"),
            intent.getStringExtra("localPath")
        )
    }
}