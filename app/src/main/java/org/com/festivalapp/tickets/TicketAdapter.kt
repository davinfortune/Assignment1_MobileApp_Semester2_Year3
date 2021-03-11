package org.com.festivalapp.tickets

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.com.festivalapp.AddTicketFragment
import org.com.festivalapp.R
import org.com.festivalapp.home.HomeAdapter
import org.com.festivalapp.home.HomeItem

class TicketAdapter(private val ticketList: List<TicketItem>,
                    private val ticketFragment: OnTicketClickLister)
    : RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.ticket_cardview,
                parent,false)

        return TicketViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val currentItem = ticketList[position]
        holder.textView.text = currentItem.userName
        holder.textView2.text = currentItem.userLocation
    }

    override fun getItemCount() = ticketList.size

    inner class TicketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener{
        val textView: TextView = itemView.findViewById(R.id.ticketCardName)
        val textView2: TextView = itemView.findViewById(R.id.ticketCardDescription)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position : Int = adapterPosition
            if (position != RecyclerView.NO_POSITION){
                ticketFragment.onItemClick(position)
            }
        }
    }

    interface OnTicketClickLister {
        fun onItemClick(position: Int)
    }
}