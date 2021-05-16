package org.com.festivalapp


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*

private const val RC_SIGN_IN = 123


class LoginActivity: AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()


        val RegistrationText : TextView = findViewById(R.id.registerText)
        RegistrationText.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }


        login()
    }

    private fun login(){

        loginGoogle.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            googleSignInClient = GoogleSignIn.getClient(this, gso)

            googleSignIn()
        }

        loginButton.setOnClickListener {

            if(TextUtils.isEmpty(loginUsername.text.toString())){
                loginUsernameText.setError("Please Enter a Valid Username")
                return@setOnClickListener
            } else if(TextUtils.isEmpty(loginPassword.text.toString())) {
                loginUsernameText.setError("Please Enter a Valid Password")
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(loginUsername.text.toString(),loginPassword.text.toString())
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        Toast.makeText(
                            this@LoginActivity,
                            "You are Logged In!",
                            Toast.LENGTH_LONG
                        ).show()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else{
                        Toast.makeText(
                            this@LoginActivity,
                            "Login Failed, Make Sure Your Credientials are Correct",
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

        val db = FirebaseFirestore.getInstance()

        db.collection("profiles").get().addOnCompleteListener{
            var userExists = false
            if(it.isSuccessful) {
                for(document in it.result!!){
                    if (document.data.getValue("userId").toString() == account?.id.toString()) {
                        userExists = true
                    }
                }
            }
            if (!userExists) {
                saveFireStore(account?.id.toString(),firstName,lastName)
            }
            firebaseAuthWithGoogle(account?.idToken.toString())
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)


        }



//        } catch (e: ApiException) {
//            println("No Account")
//        }

    }

    fun googleSignIn(){

        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)

    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {

                        val user: FirebaseUser = auth.getCurrentUser()
                    } else {
                        println("unable to add user")
                    }
                })
    }

}