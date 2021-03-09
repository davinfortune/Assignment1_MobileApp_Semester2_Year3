package org.com.festivalapp.tickets

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.com.festivalapp.R
import org.com.festivalapp.home.HomeAdapter
import org.com.festivalapp.home.HomeItem

class TicketAdapter(private val ticketList: List<TicketItem>) : RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.home_primary_cardview,
                parent,false)

        return TicketViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
//        val currentItem = ticketList[position]
//        holder.textView.text = currentItem.primaryTitle
//        holder.textView2.text = currentItem.description
//        holder.dateAdded.text = currentItem.dateadded
    }

    override fun getItemCount() = ticketList.size

    class TicketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.homeCardPP)
        val textView: TextView = itemView.findViewById(R.id.homeCardNewsTitle)
        val textView2: TextView = itemView.findViewById(R.id.homeCardDescription)
        val dateAdded : TextView = itemView.findViewById(R.id.homeCardDateAdded)   }
}