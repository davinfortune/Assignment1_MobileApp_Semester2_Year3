package org.com.festivalapp.home

import android.content.Intent
import android.os.Bundle
import android.text.LoginFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.com.festivalapp.R
import org.com.festivalapp.LoginActivity
import org.com.festivalapp.User
import org.com.festivalapp.tickets.TicketAdapter
import org.com.festivalapp.tickets.TicketItem
import java.lang.Exception


class HomeFragment : Fragment() {

    lateinit var auth : FirebaseAuth
    lateinit var User : User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null){
            loadProfile()
        }
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

        view.loginMover.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }
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

    private fun loadProfile() {
        try {
            val user = auth.currentUser

            val db = FirebaseFirestore.getInstance()

            db.collection("profiles").get().addOnCompleteListener {
                if (it.isSuccessful) {
                    for (document in it.result!!) {
                        if (document.data.getValue("userId").toString() == user?.uid.toString()) {
                            User = User(
                                document.data.getValue("userId").toString(),
                                user.email,
                                document.data.getValue("firstname").toString(),
                                document.data.getValue("lastname").toString()
                            )
                        }
                    }
                    Toast.makeText(
                        activity,
                        "Welcome, " + User.firstname,
                        Toast.LENGTH_LONG
                    ).show()


                    loginMover.text = "Welcome, \n             " + User.firstname
                }
            }
        }
        catch (e : Exception) {
            print("Unable to login")
        }
    }


}