package com.lukasbeckercode.forrestwatch.ui

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.lukasbeckercode.forrestwatch.Constants
import com.lukasbeckercode.forrestwatch.FireBaseAuth
import com.lukasbeckercode.forrestwatch.R
import com.lukasbeckercode.forrestwatch.models.User

/**
 * Class handling User registration process
 */
class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val registerBtn : Button = findViewById(R.id.btn_register_register)
        val loginText:TextView = findViewById(R.id.tv_register_login)

        loginText.setOnClickListener {
            startActivity(Intent(this,Login::class.java))
        }

        registerBtn.setOnClickListener {
            register()
        }
    }


    /**
     * Takes the input data, validates it and writes it to the corresponding Firebase places (auth and Store)
     */
    private fun register(){

        val email:String= findViewById<EditText>(R.id.et_register_email).text.toString()
        val password :String = findViewById<EditText>(R.id.et_register_password).text.toString()
        val confirmPassword :String = findViewById<EditText>(R.id.et_register_confirmpassword).text.toString()
        val firstname :String = findViewById<EditText>(R.id.et_register_firstname).text.toString()
        val lastname :String = findViewById<EditText>(R.id.et_register_lastname).text.toString()

        val terms: CheckBox = findViewById(R.id.cb_register_termsandcondition)

        if(!terms.isChecked){ //check terms and conditions checkbox
            Toast.makeText(this,R.string.register_terms_unchecked,Toast.LENGTH_SHORT).show()
            return
        }

        val user: User?

        try {
            //try to create a new user object
            user  = User(email =email, firstname = firstname, lastname = lastname)
        } catch (e: java.lang.IllegalArgumentException){ //some data is invalid!
            Toast.makeText(this,e.message,Toast.LENGTH_LONG).show() //Tell the user what data is invalid
            return
        }
        //simple email validation,also checks if the 2 entered passwords match
        if(!user.validate(password,confirmPassword)){
            Toast.makeText(baseContext, R.string.error_email_or_password_registration,
                Toast.LENGTH_SHORT).show()
        }else{
            FireBaseAuth().register(user,password,this)
        }
    }

    /**
     * called after registration was successful
     * @see FireBaseAuth
     * @see com.lukasbeckercode.forrestwatch.database.CloudFireStore
     * @param user object of registered user
     */
    fun registrationSuccess(user:User){

        Toast.makeText(baseContext, R.string.registration_success,Toast.LENGTH_SHORT).show()
        val intent = Intent(this,HomeActivity::class.java)
        intent.putExtra(Constants.intentKeyUser,user)
        startActivity(intent)
    }
}