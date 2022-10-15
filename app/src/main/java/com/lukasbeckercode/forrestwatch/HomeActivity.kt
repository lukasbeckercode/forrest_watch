package com.lukasbeckercode.forrestwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.lukasbeckercode.forrestwatch.models.User

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        var user = User()
        if (intent.hasExtra("user")){
            user = intent.getParcelableExtra("user")!!
        }
        val tvWelcome :TextView = findViewById(R.id.tv_home_title)
        tvWelcome.text = user.firstname
    }
}