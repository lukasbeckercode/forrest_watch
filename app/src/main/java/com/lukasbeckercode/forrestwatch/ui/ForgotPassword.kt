package com.lukasbeckercode.forrestwatch.ui

import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lukasbeckercode.forrestwatch.FireBaseAuth
import com.lukasbeckercode.forrestwatch.R
import com.lukasbeckercode.forrestwatch.database.CloudFireStore
import com.lukasbeckercode.forrestwatch.models.User

/**
 * activity to reset password via email
 * @author lukas becker
 * @see AuthActivity
 */
class ForgotPassword : AppCompatActivity(),AuthActivity {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgotpassword)
        val forgotBtn: Button = findViewById(R.id.btn_forgot_forgot)
        forgotBtn.setOnClickListener {
            val email = findViewById<EditText>(R.id.et_forgot_email).text.toString()
            if(TextUtils.isEmpty(email))
            {
                Toast.makeText(this,R.string.error_empty_email,Toast.LENGTH_LONG).show()
            }
           CloudFireStore().getUserData(this,email)
        }


    }

    /**
     * User data matching with the entered email address found
     * @param user User data stored in FireStore
     * @see AuthActivity
     */
    override fun success(user: User) {
        FireBaseAuth().forgotPassword(user.email!!,this)
        Toast.makeText(this,R.string.forgot_password_success,Toast.LENGTH_LONG).show()
    }

    /**
     * No user with matching email address found
     * @see AuthActivity
     */
    override fun fail() {
        Toast.makeText(this,R.string.error_email_user_nonexistent,Toast.LENGTH_LONG).show()
    }
}