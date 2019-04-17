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
    private var itemCount = 0
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
        itemCount = preferences.itemCount

        settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        adapter.onClickListener = {
            index, card -> run {
                startActivity(CardActivityIntent.pack(this, card))
                this.swapCard(index)
            }
        }
        adapter.onRemoveListener = { index -> swapCard(index) }

        initEnvironment(this)
        if (savedInstanceState != null && savedInstanceState.containsKey(CARDS_KEY)) {
            val cardsArray = savedInstanceState.getParcelableArray(CARDS_KEY)
            if (cardsArray != null) {
                adapter.resetCards(cardsArray.map { it as Card })
            }
        } else {
            loadCards()
        }
    }

    private fun initEnvironment(context: Context) {
        val db = CardDatabase.getInstance(context).cardDao()
        val web = WebRepository(Dispatchers.IO)
        loader = CardLoader(web, db, this) { Log.e("CARD_LOADER", it) }
    }

    private fun loadCards() = launch {
        loader.downloadCardsIfNeeded(locale)
        adapter.resetCards(loader.getRandomCards(itemCount, locale))
    }

    private fun swapCard(index: Int) = launch {
        adapter.removeByIndex(index)
        val cards = loader.getRandomCards(1, locale)
        if (cards.isNotEmpty()) {
            adapter.addCard(cards[0])
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArray(CARDS_KEY, adapter.cardsAsArray())
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        val preferences = Preferences(this)
        if (preferences.locale != locale || preferences.itemCount != itemCount) {
            locale = preferences.locale
            itemCount = preferences.itemCount
            loadCards()
        }
        super.onResume()
    }
}
