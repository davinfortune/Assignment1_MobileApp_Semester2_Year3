package org.com.festivalapp.home

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import org.com.festivalapp.R

//ADAPTER BUILT WITH HELP FROM THE FOLLOWING VIDEO https://www.youtube.com/watch?v=afl_i6uvvU0&t=494s
class HomeAdapter(private val homeList: List<HomeItem>) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.home_primary_cardview,
        parent,false)

        return HomeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
       val currentItem = homeList[position]
        holder.imageView.setImageResource(currentItem.imageResouce)
        holder.textView.text = currentItem.primaryTitle
        holder.textView2.text = currentItem.description
        holder.dateAdded.text = currentItem.dateadded
    }

    override fun getItemCount() = homeList.size

    class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.homeCardPP)
        val textView: TextView = itemView.findViewById(R.id.homeCardNewsTitle)
        val textView2: TextView = itemView.findViewById(R.id.homeCardDescription)
        val dateAdded : TextView = itemView.findViewById(R.id.homeCardDateAdded)   }
}