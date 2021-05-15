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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_profile.*
import java.lang.Exception


class AddTicketFragment : Fragment() {
    lateinit var auth: FirebaseAuth
    lateinit var User : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
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
            childFragmentManager.beginTransaction().replace(R.id.addTicketLayout, TicketFragment()).setTransition(
                FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit()
            goBackButton.setVisibility(View.GONE)
            saveButton.setVisibility(View.GONE)
        }
        saveButton.setOnClickListener {
            val userDay : EditText = view.findViewById(R.id.userDay)
            val musicType : EditText = view.findViewById(R.id.musicType)
            val userLocation : EditText = view.findViewById(R.id.userLocation)

            try {
                loadProfile()
                saveFireStore(
                    userDay.text.toString(),
                    musicType.text.toString(),
                    userLocation.text.toString()
                )

                childFragmentManager.beginTransaction()
                    .replace(R.id.addTicketLayout, TicketFragment()).setTransition(
                    FragmentTransaction.TRANSIT_FRAGMENT_FADE
                ).addToBackStack(null).commit()
                goBackButton.setVisibility(View.GONE)
                saveButton.setVisibility(View.GONE)
            } catch (e : Exception) {
                Toast.makeText(
                    activity,
                    "Cannot add please Sign in first",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        return view
    }

//    code got from : https://www.youtube.com/watch?v=5UEdyUFi_uQ
    fun saveFireStore(userDay: String, musicType : String, userLocation : String){
       val db = FirebaseFirestore.getInstance()
        val ticket : MutableMap<String, Any> = HashMap()
        ticket["userId"] = auth.currentUser.uid
        ticket["userName"] = User.firstname + " " + User.lastname
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

    private fun loadProfile() {
        val user = auth.currentUser

        val db = FirebaseFirestore.getInstance()

        db.collection("profiles").get().addOnCompleteListener{
            if(it.isSuccessful) {
                for(document in it.result!!){
                    if(document.data.getValue("userId").toString() == user?.uid.toString()) {
                        User = User(
                            document.data.getValue("userId").toString(),
                            user.email,
                            document.data.getValue("firstname").toString(),
                            document.data.getValue("lastname").toString()
                        )
                    }
                }
            }
        }
    }
}