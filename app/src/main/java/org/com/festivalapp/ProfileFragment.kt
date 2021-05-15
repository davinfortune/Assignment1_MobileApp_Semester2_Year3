package org.com.festivalapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*


class ProfileFragment : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        view.signouttext.setOnClickListener {
            auth.signOut()
        }
        return view
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

                fullname.text = User.firstname + " " + User.lastname
                email.text = User.email

            }
        }
    }


}