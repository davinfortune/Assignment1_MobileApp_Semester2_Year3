package org.com.festivalapp


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_ticket.*
import kotlinx.android.synthetic.main.fragment_ticket.view.*
import kotlinx.android.synthetic.main.ticket_cardview.*
import org.com.festivalapp.tickets.TicketAdapter
import org.com.festivalapp.tickets.TicketItem


class TicketFragment : Fragment(), TicketAdapter.OnTicketClickLister {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ticket, container, false)

        val recycler_view : RecyclerView = view.findViewById(R.id.ticket_r_view)

        readFromFirebase(recycler_view)

        recycler_view.visibility = View.VISIBLE

        recycler_view.adapter?.notifyDataSetChanged()

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
        val ticketList = ArrayList<TicketItem>()

        db.collection("tickets").get().addOnCompleteListener{
            if(it.isSuccessful) {
                ticketList.clear()
               for(document in it.result!!){
                   val item = TicketItem( document.data.getValue("userName").toString(), document.data.getValue("userDay").toString(),
                           document.data.getValue("musicType").toString(), document.data.getValue("userLocation").toString() )
                   ticketList += item
               }
                recycler_View.adapter = TicketAdapter(ticketList, this)
                recycler_View.layoutManager = LinearLayoutManager(activity)
                recycler_View.setHasFixedSize(true)
            }
        }
    }



    override fun onItemClick(position: Int) {
        val pos = position
        val db = FirebaseFirestore.getInstance()
        val ticketList = ArrayList<TicketItem>()

        db.collection("tickets").get().addOnCompleteListener{
            if(it.isSuccessful) {
                for(document in it.result!!){
                    val item = TicketItem(document.data.getValue("userName").toString(), document.data.getValue("userDay").toString(),
                            document.data.getValue("musicType").toString(), document.data.getValue("userLocation").toString())
                    ticketList += item
                }
                val ticketClicked : TicketItem = ticketList[pos]

                val ldf = TicketDetailsFragment()
                val args = Bundle()
                args.putParcelable("sharedTicket",ticketClicked)
                args.putInt("position",pos)
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
