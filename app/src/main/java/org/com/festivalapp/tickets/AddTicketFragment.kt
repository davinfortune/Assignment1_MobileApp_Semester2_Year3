package org.com.festivalapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.firestore.FirebaseFirestore


class AddTicketFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_ticket, container, false)

        val goBackButton : Button = view.findViewById(R.id.goBackButton)
        val saveButton : Button = view.findViewById(R.id.saveNewTicket)

        goBackButton.setVisibility(View.VISIBLE)
        saveButton.setVisibility(View.VISIBLE)
        goBackButton.setOnClickListener {
            childFragmentManager.beginTransaction().replace(R.id.addTicketLayout, TicketFragment()).addToBackStack(null).commit()
            goBackButton.setVisibility(View.GONE)
            saveButton.setVisibility(View.GONE)
        }
        saveButton.setOnClickListener {
            val userName : EditText = view.findViewById(R.id.userName)
            val userDay : EditText = view.findViewById(R.id.userDay)
            val musicType : EditText = view.findViewById(R.id.musicType)
            val userLocation : EditText = view.findViewById(R.id.userLocation)

            saveFireStore(userName.text.toString(),userDay.text.toString(),musicType.text.toString(),userLocation.text.toString())
        }

        return view
    }

//    code got from : https://www.youtube.com/watch?v=5UEdyUFi_uQ
    fun saveFireStore(userName : String, userDay: String, musicType : String, userLocation : String){
       val db = FirebaseFirestore.getInstance()
        val ticket : MutableMap<String, Any> = HashMap()
        ticket["userName"] = userName
        ticket["userDay"] = userDay
        ticket["musicType"] = musicType
        ticket["userLocation"] = userLocation
        db.collection("tickets").add(ticket)
                .addOnSuccessListener {
            println("record added successfully ")
        }
                .addOnFailureListener {
                   println("record not added")
                }
    }
}