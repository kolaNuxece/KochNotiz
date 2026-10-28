package com.example.rezepteessen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(
    private val items: MutableList<Item>,
    private val onDeleteClick: (Item) -> Unit,
    private val onCookAgainClick: (Item) -> Unit
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textName: TextView = itemView.findViewById(R.id.textViewName)
        val textRezepte: TextView = itemView.findViewById(R.id.textViewIngredients)
        val textNote: TextView = itemView.findViewById(R.id.textViewNote)
        val buttonDelete: Button = itemView.findViewById(R.id.buttonDelete)
        val buttonCookAgain: Button = itemView.findViewById(R.id.buttonCookAgain)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.textName.text = item.essenName
        holder.textRezepte.text = "Zutaten:\n${item.rezepte}"
        holder.textNote.text = "Notiz: ${item.noteZuEnde}"

        holder.buttonDelete.setOnClickListener { onDeleteClick(item) }
        holder.buttonCookAgain.setOnClickListener { onCookAgainClick(item) }
    }

    fun updateList(newList: List<Item>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }
}
