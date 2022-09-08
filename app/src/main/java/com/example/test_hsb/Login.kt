package com.example.test_hsb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import androidx.core.text.set
import com.example.test_hsb.databinding.ActivityLoginBinding
import com.example.test_hsb.databinding.ActivityMainBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        if (loggedIn){
            binding.loginStatusTV.text = "Inloggad"

       //     val editable = Editable.Factory.getInstance().newEditable(Firebase.auth.currentUser.toString())
            var editable = Editable.Factory.getInstance().newEditable(Firebase.auth.currentUser?.email.toString())
            val mString = Firebase.auth.currentUser?.email.toString()!!.split("@").toTypedArray()
            editable = Editable.Factory.getInstance().newEditable(mString[0])

           binding.loginEmailET.text = editable
        //    Firebase.auth.currentUser.email


        }
        else {
            binding.loginStatusTV.text= "Utloggad"
        }

        binding.loginLoginBtn.setOnClickListener {
            var email = binding.loginEmailET.text.toString()
            if (email != "") {
                email = email + "@icloud.com"
                val password = binding.loginPasswordET.text.toString()
                login(email, password)
            }
        }

        binding.loginRegisterBtn.setOnClickListener {
            var email = binding.loginEmailET.text.toString()
            email = email + "@icloud.com"
            val password = binding.loginPasswordET.text.toString()
            signup(email, password)
        }

        binding.logoutBtn.setOnClickListener {
            logout()
        }

    }

        fun login(email : String, password : String)
        {
            val auth = Firebase.auth

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    loginOK.value = true
                    loginStatus.value = LoginResult.LOGINOK
                    binding.loginStatusTV.text = "Inloggad"
                    loggedIn = true
                } else {
                    loginStatus.value = LoginResult.LOGINFAIL
                    binding.loginStatusTV.text = "Inloggning misslyckades"
                }
                loginStatus.value = null
            }
        }

        fun signup(email : String, password : String)
        {
            val auth = Firebase.auth

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    loginOK.value = true
                    loginStatus.value = LoginResult.LOGINOK
                    binding.loginStatusTV.text = "Registrerad och inloggad"
                    loggedIn = true
                } else {
                    loginStatus.value = LoginResult.REGISTERFAIL
                    binding.loginStatusTV.text = "Registrering misslyckades"
                }
                loginStatus.value = null
            }
        }

        fun logout()
        {
            val auth = Firebase.auth

            auth.signOut()
            loginOK.value = false
            binding.loginStatusTV.text = "Utloggad"
            loggedIn = false
        }




        /*   val loginobserver = Observer<LoginResult> {
               Log.i("PIAXDEBUG", "LOGIN STATUS")
               if(it == LoginResult.LOGINFAIL)
               {
                   Log.i("PIAXDEBUG", "LOGIN FAIL")
                   Snackbar.make(view, "Felaktig inloggning", Snackbar.LENGTH_LONG).show()
               }
               if(it == LoginResult.REGISTERFAIL)
               {
                   Log.i("PIAXDEBUG", "REGISTER FAIL")
                   Snackbar.make(view, "Felaktig registrering", Snackbar.LENGTH_LONG).show()
               }
           }*/


}