package database

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lukasbeckercode.forrestwatch.Login
import com.lukasbeckercode.forrestwatch.R
import com.lukasbeckercode.forrestwatch.Register
import com.lukasbeckercode.forrestwatch.models.User

//TODO: collection names as constants!
class CloudFireStore {
    private val db = Firebase.firestore

    fun saveUser(user: User, registerActivity:Register){
        db.collection("user").document(user.id!!).set(user, SetOptions.merge())
            .addOnSuccessListener { registerActivity.registrationSuccess()}
            .addOnFailureListener { e->Log.e(registerActivity.javaClass.name,"Error during user saving to cloud",e) }
    }

    fun getUserData(loginActivity:Login){
        val uid = getCurrentUID()
        if(uid != ""){

            db.collection("user").document(uid).get()
                .addOnSuccessListener { result ->
                    val user = result.toObject(User::class.java)
                    loginActivity.success(user!!)
                }
                .addOnFailureListener { Log.e(loginActivity.javaClass.name,
                    R.string.login_error_user_data.toString()
                ) }
        }
    }

    private fun getCurrentUID(): String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null){
            return currentUser.uid
        }

        return ""
    }
}