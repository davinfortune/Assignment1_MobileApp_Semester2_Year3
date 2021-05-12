package org.com.festivalapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.com.festivalapp.home.HomeFragment


class MainActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    var databaseReference : DatabaseReference? = null
    var database : FirebaseDatabase? = null
    var firstname : String = "e"
    var lastname : String = "e"
    var email : String = "e"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")

        if(auth.currentUser != null){
            loadProfile()
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        openFragment(HomeFragment())
        bottomNav.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    openFragment(HomeFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.gallery -> {
                    openFragment(GalleryFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.tickets -> {
                    openFragment(TicketFragment())
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })
    }

    fun openFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainLayout, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    fun loadProfile() {
        val user = auth.currentUser
        val userref = databaseReference?.child(user.uid)

        email = user.email

        userref?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                println("inside value event listener")
                loginMover.text = "Welcome, \n             " + snapshot.child("firstname").value.toString()
//                lastname = snapshot.child("lastname").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        Toast.makeText(
            this@MainActivity,
            "Welcome, " + firstname,
            Toast.LENGTH_LONG
        ).show()
    }
}