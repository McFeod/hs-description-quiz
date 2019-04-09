package io.github.mcfeod.hsdescriptionquiz

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.settingsButton
import kotlinx.android.synthetic.main.activity_main.mainRecycler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val CARDS_KEY = "CARDS"

class MainActivity : AsyncActivity() {
    private lateinit var locale: String
    private lateinit var loader: CardLoader
    private lateinit var adapter: CardRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainRecycler.layoutManager = LinearLayoutManager(this)
        adapter = CardRecyclerAdapter()
        mainRecycler.adapter = adapter

        val preferences = Preferences(this)
        locale = preferences.locale
        val itemCount = preferences.itemCount

        settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        adapter.onClickListener = { card -> startActivity(CardActivityIntent.pack(this, card)) }
        adapter.onRemoveListener = { index -> swapCard(index) }

        initEnvironment(this)
        if (savedInstanceState != null && savedInstanceState.containsKey(CARDS_KEY)) {
            val cards = savedInstanceState.getParcelableArray(CARDS_KEY)
            if (cards != null) {
                adapter.resetCards(cards)
            }
        } else {
            loadCards(itemCount)
        }
    }

    private fun initEnvironment(context: Context) {
        // todo implement file interactions
        val db = CardDatabase.getInstance(context).cardDao()
        val web = WebRepository(getString(R.string.rapid_api_key), Dispatchers.IO)
        val env = MockEnvironment()
        loader = CardLoader(web, db, env, this) { Log.e("CARD_LOADER", it) }
    }

    private fun loadCards(count: Int) = launch {
        loader.downloadCardsIfNeeded(locale)
        loader.getRandomCards(count, locale) { adapter.addCard(it) }
    }

    private fun swapCard(index: Int) = launch {
        adapter.removeByIndex(index)
        loader.getRandomCards(1, locale) { adapter.addCard(it) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArray(CARDS_KEY, adapter.cardsAsArray())
        super.onSaveInstanceState(outState)
    }
}
