package org.com.festivalapp


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_ticket.*
import org.com.festivalapp.tickets.TicketAdapter
import org.com.festivalapp.tickets.TicketItem
import java.lang.NullPointerException
import java.util.*
import kotlin.collections.ArrayList


class TicketFragment : Fragment(), TicketAdapter.OnTicketClickLister {
    private var pos : Int = -1
    private val ticketList = ArrayList<TicketItem>()
    private val searchList = ArrayList<TicketItem>()
    lateinit var adapter : TicketAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ticket, container, false)

        val search : SearchView= view.findViewById(R.id.searchBar)

        val recycler_view : RecyclerView = view.findViewById(R.id.ticket_r_view)
        try {
            pos = arguments!!.getInt("position")
            if ( pos != -1){
                println("Im in")
                recycler_view.adapter?.notifyItemChanged(pos)
            }
        } catch (e: NullPointerException){

        }

        readFromFirebase(recycler_view)

        recycler_view.visibility = View.VISIBLE

        search.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isNotEmpty()) {
                    ticketList.clear()
                    val userSearch = newText?.toLowerCase(Locale.getDefault())
                    searchList.forEach {
                        if (it.userName.toLowerCase().contains(userSearch.toString())) {
                            ticketList.add(it)
                        }
                    }
                    adapter.notifyDataSetChanged()
                } else {
                    ticketList.clear()
                    searchList.clear()
                    readFromFirebase(recycler_view)
                }
                return true
            }

        })

        val addTicketButton : Button = view.findViewById(R.id.addTicketButton)
        addTicketButton.visibility = View.VISIBLE
        addTicketButton.setOnClickListener {
            childFragmentManager.beginTransaction().replace(R.id.ticketLayout, AddTicketFragment()).setTransition(
                FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit()
            addTicketButton.visibility = View.GONE
            recycler_view.visibility = View.GONE
        }
        return view
    }
//    code taken from : https://www.youtube.com/watch?v=5UEdyUFi_uQ
    private fun readFromFirebase(recycler_View: RecyclerView) {
        val db = FirebaseFirestore.getInstance()

        db.collection("tickets").get().addOnCompleteListener{
            if(it.isSuccessful) {
                ticketList.clear()
               for(document in it.result!!){
                   val item = TicketItem( document.data.getValue("userName").toString(), document.data.getValue("userDay").toString(),
                           document.data.getValue("musicType").toString(), document.data.getValue("userLocation").toString() )
                   ticketList += item
                   searchList += item
               }
                adapter = TicketAdapter(ticketList, this)
                recycler_View.adapter = adapter
                recycler_View.adapter?.notifyDataSetChanged()
                recycler_View.layoutManager = LinearLayoutManager(activity)
                recycler_View.setHasFixedSize(true)
            }
        }
    }



    override fun onItemClick(position: Int) {
        val db = FirebaseFirestore.getInstance()
        val ticketList = ArrayList<TicketItem>()

        db.collection("tickets").get().addOnCompleteListener{
            if(it.isSuccessful) {
                for(document in it.result!!){
                    val item = TicketItem(document.data.getValue("userName").toString(), document.data.getValue("userDay").toString(),
                            document.data.getValue("musicType").toString(), document.data.getValue("userLocation").toString())
                    ticketList += item
                }
                val ticketClicked : TicketItem = ticketList[position]

                val ldf = TicketDetailsFragment()
                val args = Bundle()
                args.putParcelable("sharedTicket",ticketClicked)
                args.putInt("position", position)
                ldf.arguments = args
                childFragmentManager.beginTransaction().replace(R.id.ticketLayout, ldf).setTransition(
                    FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit()
                addTicketButton.visibility = View.GONE
                ticket_r_view.visibility = View.GONE
            }
        }
    }

//    @Override
//    fun onResume(layout:View){
//        super.onResume()
//        layout.ticket_r_view.adapter?.
//        println("On resume started")
//    }

}
