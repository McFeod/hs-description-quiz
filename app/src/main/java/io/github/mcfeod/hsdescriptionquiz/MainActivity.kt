package io.github.mcfeod.hsdescriptionquiz

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.settingsButton
import kotlinx.android.synthetic.main.activity_main.mainRecycler
import kotlinx.coroutines.launch

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

        adapter.onClickListener = { card -> startActivity(CardActivityIntent(card).pack(this)) }
        adapter.onRemoveListener = { index -> swapCard(index) }

        initEnvironment()
        loadCards(itemCount)
    }

    private fun initEnvironment() {
        // todo implement db and web interactions
        val env = MockEnvironment(this)
        loader = CardLoader(env, env, this) { msg: String -> Log.e("CARD_LOADER", msg)}
    }

    private fun loadCards(count: Int) = launch {
        loader.downloadCardsIfNeeded(locale)
        loader.getRandomCards(count, locale) { card: Card -> adapter.addCard(card) }
    }

    private fun swapCard(index: Int) = launch {
        adapter.removeByIndex(index)
        loader.getRandomCards(1, locale) { card: Card -> adapter.addCard(card) }
    }
}
