package com.lukasbeckercode.forrestwatch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.lukasbeckercode.forrestwatch.models.User
import database.CloudFireStore

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val tvRegister :TextView = findViewById(R.id.tv_login_register)
        tvRegister.setOnClickListener{
            startActivity(Intent(this,Register::class.java))
        }

        val tvForgotPassword : TextView = findViewById(R.id.tv_login_forgotpassword)

        tvForgotPassword.setOnClickListener {
            //TODO: Call forgot password activity
            Log.i("Login","IMPLEMENT ME!")
        }

        val btnLogin:Button = findViewById(R.id.btn_login_login)
        btnLogin.setOnClickListener {
            logInUser()
        }
    }
    private fun logInUser(){
        val email:String= findViewById<EditText>(R.id.et_login_email).text.toString().trim()
        val password :String = findViewById<EditText>(R.id.et_login_password).text.toString().trim()

        when{
            TextUtils.isEmpty(email) -> {
                Toast.makeText(this,R.string.error_empty_email,Toast.LENGTH_SHORT).show()
                return
            }
            TextUtils.isEmpty(password) ->{
                Toast.makeText(this,R.string.error_empty_passwd,Toast.LENGTH_SHORT).show()
                return
            }
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{task->
                run {
                    if (task.isSuccessful) {
                        CloudFireStore().getUserData(this)
                        Toast.makeText(this, R.string.login_success, Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(
                            this,
                            R.string.login_failure.toString() + task.exception.toString(),
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }
            }

    }

    fun success(user : User) {
        val intent = Intent(this,HomeActivity::class.java)
        intent.putExtra("user",user)
        startActivity(intent)
    }
}