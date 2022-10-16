package com.lukasbeckercode.forrestwatch.database

import android.text.TextUtils
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lukasbeckercode.forrestwatch.Constants
import com.lukasbeckercode.forrestwatch.R
import com.lukasbeckercode.forrestwatch.models.User
import com.lukasbeckercode.forrestwatch.ui.AuthActivity
import com.lukasbeckercode.forrestwatch.ui.Register

//TODO: collection names as constants!
class CloudFireStore {
    private val db = Firebase.firestore

    fun saveUser(user: User, registerActivity: Register){
        db.collection(Constants.firebaseTopic).document(user.id!!).set(user, SetOptions.merge())
            .addOnSuccessListener { registerActivity.registrationSuccess(user)}
            .addOnFailureListener { e->Log.e(registerActivity.javaClass.name,"Error during user saving to cloud",e) }
    }

    fun getUserData(authActivity: AuthActivity,email: String = ""){

        val uid = getCurrentUID()
        if(uid != "" && TextUtils.isEmpty(email)){

            db.collection(Constants.firebaseTopic).document(uid).get()
                .addOnSuccessListener { result ->
                    val user = result.toObject(User::class.java)
                    authActivity.success(user!!)
                }
                .addOnFailureListener { Log.e(authActivity.javaClass.name,
                    R.string.login_error_user_data.toString()
                )
                authActivity.fail()
                }
        }else{
            if (!TextUtils.isEmpty(email)){

                val user =getUserByEmail(email,authActivity)
                if(user != null){
                    authActivity.success(user)
                }
                else{
                    authActivity.fail()
                }
            }
        }
    }

    private fun getUserByEmail(email:String, authActivity: AuthActivity):User?{
        var user: User? = null
        db.collection(Constants.firebaseTopic).document(email).get()
            .addOnSuccessListener { result ->
                user = result.toObject(User::class.java)
            }
            .addOnFailureListener {
                Log.e(authActivity.javaClass.name,R.string.error_email_user_nonexistent.toString())
                authActivity.fail()
            }

        return user
    }

    private fun getCurrentUID(): String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null){
            return currentUser.uid
        }

        return ""
    }
}