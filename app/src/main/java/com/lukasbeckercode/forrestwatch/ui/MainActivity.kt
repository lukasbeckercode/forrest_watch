package com.lukasbeckercode.forrestwatch.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.lukasbeckercode.forrestwatch.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginBtn : Button = findViewById(R.id.login_button_main)
        val registerBtn :Button = findViewById(R.id.register_button_main)

        loginBtn.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }

        registerBtn.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }
    }
}