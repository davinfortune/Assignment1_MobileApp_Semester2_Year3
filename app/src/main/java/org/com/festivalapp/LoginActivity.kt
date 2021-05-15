package org.com.festivalapp


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity: AppCompatActivity() {

    lateinit var auth: FirebaseAuth

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

}