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

/**
 * Class that handles all Firebase Authentication functionality
 * @author lukas becker
 */
class FireBaseAuth {
    private val auth = Firebase.auth
    private val currentUser = auth.currentUser

    /**
     * Log an already registered user in
     * @param email email address associated with the user
     * @param password the users password
     */
    fun logInUser(email:String,password:String, loginActivity:Login){

        //Check validity of user input
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

        //authenticate user and get user data
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{task->
                run {
                    if (task.isSuccessful) {
                        CloudFireStore().getUserData(loginActivity)
                        Toast.makeText(loginActivity, R.string.login_success, Toast.LENGTH_LONG).show()
                    } else {
                        val base = loginActivity.getText(R.string.login_failure)
                        val exception = task.exception.toString()
                        val errorText =  "$base$exception"
                        Toast.makeText(
                            loginActivity,
                             errorText,
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }
            }

    }

    /**
     * sings a user out
     * Caution: does not destroy User() Object, do that in the corresponding Class
     * @see com.lukasbeckercode.forrestwatch.ui.HomeActivity
     */
    fun logOut(){
        auth.signOut()
    }

    /**
     * registers a user and stores user data
     * DOES NOT STORE PASSWORD LOCALLY!
     *
     * @param user the user object that will be registered
     * @param password the users password in cleartext
     * @param registerActivity the calling Activity
     */
    fun register(user: User,password: String, registerActivity:Register){

        if(currentUser != null){
            logOut() //log out current user ifa new user wants to register
            Toast.makeText(registerActivity,R.string.auth_current_user_logged_out, Toast.LENGTH_SHORT).show()
        }

        //register user with firebase auth
        auth.createUserWithEmailAndPassword(user.email!!, password)
            .addOnCompleteListener(registerActivity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "createUserWithEmail:success")
                    val firebaseUser = auth.currentUser
                    if (firebaseUser != null) {
                        user.id = firebaseUser.uid
                        CloudFireStore().saveUser(user,registerActivity) //save user data
                    }
                } else {
                    val base = registerActivity.getString(R.string.registration_failure)
                    val exception = task.exception.toString()
                    // If sign in fails, display a message to the user.
                    Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(registerActivity,"$base$exception",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    /**
     * function to reset user password via email
     *
     * @param email email address of the user who wants to reset their password
     * @param forgotPasswordActivity the calling Activity
     */
    fun forgotPassword(email: String, forgotPasswordActivity:ForgotPassword){
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener(forgotPasswordActivity){task->
                if (!task.isSuccessful){
                    forgotPasswordActivity.fail()
                }
            }
    }
}