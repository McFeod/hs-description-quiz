package io.github.mcfeod.hsdescriptionquiz

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CardRecyclerAdapter : RecyclerView.Adapter<CardRecyclerAdapter.ViewHolder>() {
    private val cards = mutableListOf<Card>()
    var onClickListener: ((index: Int, card: Card) -> Unit)? = null
    var onRemoveListener: ((index: Int) -> Unit)? = null

    fun addCard(card: Card) {
        val index = cards.size
        cards.add(index, card)
        notifyItemInserted(index)
    }

    fun cardsAsArray(): Array<Card> = cards.toTypedArray()

    fun resetCards(newCards: List<Card>) {
        cards.clear()
        cards.addAll(0, newCards)
        notifyDataSetChanged()
    }

    fun removeByIndex(index: Int) {
        cards.removeAt(index)
        notifyItemRemoved(index)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item, parent, false)
        val viewHolder = ViewHolder(view)
        view.findViewById<TextView>(R.id.listItemText).setOnClickListener {
            val index = viewHolder.adapterPosition
            if (index != RecyclerView.NO_POSITION) {
                onClickListener?.invoke(index, cards[index])
            }
        }
        view.findViewById<ImageView>(R.id.listItemDelete).setOnClickListener {
            val index = viewHolder.adapterPosition
            if (index != RecyclerView.NO_POSITION) {
                onRemoveListener?.invoke(index)
            }
        }
        return viewHolder
    }

    override fun getItemCount(): Int = cards.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.listItemText).text = Html.fromHtml(cards[position].description)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}