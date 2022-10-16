package com.lukasbeckercode.forrestwatch

import android.content.ContentValues
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lukasbeckercode.forrestwatch.database.CloudFireStore
import com.lukasbeckercode.forrestwatch.models.User
import com.lukasbeckercode.forrestwatch.ui.ForgotPassword
import com.lukasbeckercode.forrestwatch.ui.Login
import com.lukasbeckercode.forrestwatch.ui.Register

class FireBaseAuth {
    private val auth = Firebase.auth
    private val currentUser = auth.currentUser


    fun logInUser(email:String,password:String, loginActivity:Login){

        when{
            TextUtils.isEmpty(email) -> {
                Toast.makeText(loginActivity, R.string.error_empty_email, Toast.LENGTH_SHORT).show()
                return
            }
            TextUtils.isEmpty(password) ->{
                Toast.makeText(loginActivity, R.string.error_empty_passwd, Toast.LENGTH_SHORT).show()
                return
            }
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{task->
                run {
                    if (task.isSuccessful) {
                        CloudFireStore().getUserData(loginActivity)
                        Toast.makeText(loginActivity, R.string.login_success, Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(
                            loginActivity,
                            R.string.login_failure.toString() + task.exception.toString(),
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }
            }

    }

    fun register(user: User,password: String, registerActivity:Register){

        if(currentUser != null){
            //TODO: Do something
        }
        auth.createUserWithEmailAndPassword(user.email!!, password)
            .addOnCompleteListener(registerActivity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "createUserWithEmail:success")
                    val firebaseUser = auth.currentUser
                    if (firebaseUser != null) {
                        user.id = firebaseUser.uid
                        CloudFireStore().saveUser(user,registerActivity)
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(registerActivity, R.string.registration_failure.toString() + task.exception,
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun forgotPassword(email: String, forgotPasswordActivity:ForgotPassword){
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener(forgotPasswordActivity){task->
                if (!task.isSuccessful){
                    forgotPasswordActivity.fail()
                }
            }
    }
}