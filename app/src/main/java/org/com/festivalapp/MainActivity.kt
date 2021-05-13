package org.com.festivalapp

import android.os.Bundle
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
    lateinit var firstname : String
    lateinit var lastname : String
    lateinit var email : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()


        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        openFragment(HomeFragment())
        bottomNav.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    openFragment(HomeFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.profile -> {
                    openFragment(ProfileFragment())
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
}