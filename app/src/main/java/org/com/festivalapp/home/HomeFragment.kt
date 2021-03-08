package org.com.festivalapp.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.com.festivalapp.MainActivity
import org.com.festivalapp.R

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val homeList = generateDummyList(50)

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val recycler_view : RecyclerView = view.findViewById(R.id.home_r_view)

        recycler_view.adapter = HomeAdapter(homeList)
        recycler_view.layoutManager = LinearLayoutManager(activity)
        recycler_view.setHasFixedSize(true)
        return view


    }

    private fun generateDummyList(size: Int) : List<HomeItem>{
        val list = ArrayList<HomeItem>()

        for(i in 0 until size) {
            val drawable = R.drawable.crowd
            val item = HomeItem(drawable, "Welcome Back to Longitude!", "We are so excited to be back for 2021 with more music than ever!!", "1 Day Ago")
            list += item
        }
        return list
    }
}