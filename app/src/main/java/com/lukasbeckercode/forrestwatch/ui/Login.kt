package com.lukasbeckercode.forrestwatch.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lukasbeckercode.forrestwatch.Constants
import com.lukasbeckercode.forrestwatch.FireBaseAuth
import com.lukasbeckercode.forrestwatch.R
import com.lukasbeckercode.forrestwatch.models.User

class Login : AppCompatActivity(), AuthActivity {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val tvRegister :TextView = findViewById(R.id.tv_login_register)
        tvRegister.setOnClickListener{
            startActivity(Intent(this, Register::class.java))
        }

        val tvForgotPassword : TextView = findViewById(R.id.tv_login_forgotpassword)

        tvForgotPassword.setOnClickListener {
            startActivity(Intent(this,ForgotPassword::class.java))
        }

        val btnLogin:Button = findViewById(R.id.btn_login_login)
        btnLogin.setOnClickListener {
            val email:String= findViewById<EditText>(R.id.et_login_email).text.toString().trim()
            val password :String = findViewById<EditText>(R.id.et_login_password).text.toString().trim()
            FireBaseAuth().logInUser(email,password,this)
        }
    }

    override fun success(user : User) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra(Constants.intentKey,user)
        startActivity(intent)
    }

    override fun fail() {
        Toast.makeText(this,R.string.login_failure,Toast.LENGTH_LONG).show()
    }
}