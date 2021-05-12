package org.com.festivalapp

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register.*
import org.com.festivalapp.R
import java.lang.Exception

class RegistrationActivity: AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    var databaseReference : DatabaseReference? = null
    var database : FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")


        register()
    }
//    TUTORIAL USED : https://www.youtube.com/watch?v=lS3EOKshtyM&t=349s
    private fun register() {

        registerButton.setOnClickListener {
            if(TextUtils.isEmpty(firstNameInput.text.toString())){
                firstNameInput.setError("Please enter first name")
                return@setOnClickListener
            } else if(TextUtils.isEmpty(lastNameInput.text.toString())){
                lastNameInput.setError("Please enter last name")
                return@setOnClickListener
            } else if(TextUtils.isEmpty(usernameInput.text.toString())){
                usernameInput.setError("Please enter user name")
                return@setOnClickListener
            } else if(TextUtils.isEmpty(passwordInput.text.toString())){
                passwordInput.setError("Please enter password")
                return@setOnClickListener
            }
     println("\n\n\n\n\n\n\n" + "HELLO DOS THIS WORk" + "\n\n\n\n\n\n\n\n\n")
     auth.createUserWithEmailAndPassword(
         usernameInput.text.toString(),
         passwordInput.text.toString()
     )
         .addOnCompleteListener {
             println("\n" + "INSIDE ON COMPLETE" + "\n")
             if (it.isSuccessful) {
                 println("\n" + "ITS IS SUCCESSFUL" + "\n")
                 val currentUser = auth.currentUser
                 val currentUserDb = databaseReference?.child((currentUser?.uid!!))
                 currentUserDb?.child("firstname")?.setValue(firstNameInput.text.toString())
                 currentUserDb?.child("lastname")?.setValue(lastNameInput.text.toString())

                 Toast.makeText(
                     this@RegistrationActivity,
                     "Registration Success",
                     Toast.LENGTH_LONG
                 ).show()
                 finish()
             }
             else {
                 println(it)
                 Toast.makeText(
                     this@RegistrationActivity,
                     "Registration Failed, Make Sure to Enter a Valid Email",
                     Toast.LENGTH_LONG
                 ).show()
             }
         }





        }
    }
}