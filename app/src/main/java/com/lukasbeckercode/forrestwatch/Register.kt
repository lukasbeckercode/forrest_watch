package com.lukasbeckercode.forrestwatch

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lukasbeckercode.forrestwatch.models.User
import database.CloudFireStore

class Register : AppCompatActivity() {
    private lateinit var auth :FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = Firebase.auth
        val currentUser = auth.currentUser

        if(currentUser != null){
            //TODO: Do something
        }
        val registerBtn : Button = findViewById(R.id.btn_register_register)


        registerBtn.setOnClickListener {
            register()
        }
    }



    private fun register(){

        val email:String= findViewById<EditText>(R.id.et_register_email).text.toString()
        val password :String = findViewById<EditText>(R.id.et_register_password).text.toString()
        val confirmPassword :String = findViewById<EditText>(R.id.et_register_confirmpassword).text.toString()
        val firstname :String = findViewById<EditText>(R.id.et_register_firstname).text.toString()
        val lastname :String = findViewById<EditText>(R.id.et_register_lastname).text.toString()
        val user: User?
        try {
            user  = User(email =email, firstname = firstname, lastname = lastname)
        } catch (e: java.lang.IllegalArgumentException){
            val error:Toast = Toast.makeText(this,e.message,Toast.LENGTH_LONG)
            error.show()
            return
        }
        if(!user.validate(password,confirmPassword)){
            Toast.makeText(baseContext, R.string.error_email_or_password_registration,
                Toast.LENGTH_SHORT).show()
        }
        auth.createUserWithEmailAndPassword(user.email!!, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                     val firebaseUser = auth.currentUser
                    if (firebaseUser != null) {
                        user.id = firebaseUser.uid
                        val cloud:CloudFireStore = CloudFireStore()
                        cloud.saveUser(user,this)
                    }
                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, R.string.registration_failure.toString() + task.exception,
                        Toast.LENGTH_SHORT).show()
                    // updateUI(null)
                }
            }
    }

    fun registrationSuccess(){

        Toast.makeText(baseContext,R.string.registration_success,Toast.LENGTH_SHORT).show()
    }
}