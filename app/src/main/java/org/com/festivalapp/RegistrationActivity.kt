package org.com.festivalapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register.*

const val RC_SIGN_IN = 123

class RegistrationActivity: AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var googleSignInClient: GoogleSignInClient



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()


        register()
    }
//    TUTORIAL USED : https://www.youtube.com/watch?v=lS3EOKshtyM&t=349s
    private fun register() {

    registerGoogle.setOnClickListener {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        googleSignIn()
    }

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
//                 val currentUserDb = databaseReference?.child((currentUser?.uid!!))
//                 currentUserDb?.child("firstname")?.setValue(firstNameInput.text.toString())
//                 currentUserDb?.child("lastname")?.setValue(lastNameInput.text.toString())
                 saveFireStore(currentUser?.uid!!.toString(),firstNameInput.text.toString(),lastNameInput.text.toString())

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

    private fun saveFireStore(userId : String, firstname: String, lastname : String){
        val db = FirebaseFirestore.getInstance()
        val profile : MutableMap<String, Any> = HashMap()
        profile["userId"] = userId
        profile["firstname"] = firstname
        profile["lastname"] = lastname
        db.collection("profiles").add(profile)
            .addOnSuccessListener {
                println("record added successfully ")
            }
            .addOnFailureListener {
                println("record not added")
            }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            println("ABOUT TO LAUNCH HANDLE SIGN IN RESULT")
            handleSignInResult(task)
        }
    }


    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        println("INSIDE HANDLE SIGN IN RESULT")
//        try {


            val account = completedTask.getResult(ApiException::class.java)
            println("INSIDE TRY CATCH AND ACCOUNT VARIABLE FILLED")
            var fullname = account?.displayName
            val idx = fullname!!.lastIndexOf(' ')
            require(idx != -1) { "Only a single name: $fullname" }
            val firstName = fullname.substring(0, idx)
            val lastName = fullname.substring(idx + 1)
            saveFireStore(account?.id.toString(),firstName,lastName)

//        } catch (e: ApiException) {
//            println("No Account")
//        }

    }

    fun googleSignIn(){

        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)


        var acct : GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this@RegistrationActivity)
        if (acct != null){
            var fullname = acct.displayName
            val idx = fullname!!.lastIndexOf(' ')
            require(idx != -1) { "Only a single name: $fullname" }
            val firstName = fullname.substring(0, idx)
            val lastName = fullname.substring(idx + 1)
            saveFireStore(acct.id.toString(),firstName,lastName)
        }
    }
}