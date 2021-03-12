package org.com.festivalapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_ticket.*
import kotlinx.android.synthetic.main.fragment_ticket_details.*
import org.com.festivalapp.tickets.TicketItem


class TicketDetailsFragment : Fragment() {

    var pos : Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ticket_details, container, false)

        val sharedTicket : TicketItem? = arguments!!.getParcelable<TicketItem>("sharedTicket")
        pos = arguments!!.getInt("position")

        val userName : EditText = view.findViewById(R.id.userName)
        val userDay : EditText = view.findViewById(R.id.userDay)
        val musicType : EditText = view.findViewById(R.id.musicType)
        val userLocation : EditText = view.findViewById(R.id.userLocation)
        userName.setText(sharedTicket?.userName)
        userDay.setText(sharedTicket?.userDay)
        musicType.setText(sharedTicket?.musicType)
        userLocation.setText(sharedTicket?.userLocation)

        val goBackButton : Button = view.findViewById(R.id.goBackButton)
        val saveButton : Button = view.findViewById(R.id.updateTicket)
        val deleteButton : Button = view.findViewById(R.id.deleteTicketButton)

        goBackButton.visibility = View.VISIBLE
        saveButton.visibility = View.VISIBLE

        goBackButton.setOnClickListener {
            childFragmentManager.beginTransaction().replace(R.id.ticketDetailsLayout, TicketFragment()).setTransition(
                    FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit()
            goBackButton.visibility = View.GONE
            saveButton.visibility = View.GONE
            deleteTicketButton.visibility = View.GONE
        }

        saveButton.setOnClickListener {
            val ldf = TicketFragment()
            val args = Bundle()
            args.putInt("position",pos)
            ldf.arguments = args
            updateTicket(
                userName.text.toString(),
                userDay.text.toString(),
                musicType.text.toString(),
                userLocation.text.toString()
            )
            childFragmentManager.beginTransaction().replace(R.id.ticketDetailsLayout, ldf).setTransition(
                    FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit()
            goBackButton.visibility = View.GONE
            saveButton.visibility = View.GONE
            deleteTicketButton.visibility = View.GONE
        }

        deleteButton.setOnClickListener {
            deleteTicket()
            childFragmentManager.beginTransaction().replace(R.id.ticketDetailsLayout, TicketFragment()).setTransition(
                    FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit()
            goBackButton.visibility = View.GONE
            saveButton.visibility = View.GONE
            deleteTicketButton.visibility = View.GONE
        }

        return view
    }

    //    code got from : https://www.youtube.com/watch?v=5UEdyUFi_uQ
    private fun updateTicket(userName: String, userDay: String, musicType: String, userLocation: String){
        val db = FirebaseFirestore.getInstance()

        db.collection("tickets").get().addOnCompleteListener{
            if(it.isSuccessful) {
                var x = 0
                for(document in it.result!!){
                    if (x == pos) {
//                        code taken and change from : https://github.com/mitchtabian/FirestoreGettingStarted/blob/Updating_Data_End/app/src/main/java/codingwithmitch/com/firestoregettingstarted/MainActivity.java
                          val id : String = document.id
                          val ticketRef : DocumentReference =  db.collection("tickets").document(id)
                        ticketRef.update(
                                "userName", userName,
                                      "userDay", userDay,
                                "musicType", musicType,
                                "userLocation", userLocation )
                    }
                    x += 1
                }
            }
        }
    }

    private fun deleteTicket(){
        val db = FirebaseFirestore.getInstance()
        db.collection("tickets").get().addOnCompleteListener{
            if(it.isSuccessful) {
                var x = 0
                for(document in it.result!!){
                    if (x == pos) {
//                        code taken and change from : https://github.com/mitchtabian/FirestoreGettingStarted/blob/Updating_Data_End/app/src/main/java/codingwithmitch/com/firestoregettingstarted/MainActivity.java
                        val id : String = document.id
                        val ticketRef : DocumentReference =  db.collection("tickets").document(id)
                        ticketRef.delete()
                    }
                    x += 1
                }
            }
        }
    }
}