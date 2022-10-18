package com.lukasbeckercode.forrestwatch.database

import android.content.res.Resources
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lukasbeckercode.forrestwatch.Constants
import com.lukasbeckercode.forrestwatch.R
import com.lukasbeckercode.forrestwatch.models.MarkedTree
import com.lukasbeckercode.forrestwatch.models.User
import com.lukasbeckercode.forrestwatch.ui.AuthActivity
import com.lukasbeckercode.forrestwatch.ui.HomeActivity
import com.lukasbeckercode.forrestwatch.ui.Register
import com.lukasbeckercode.forrestwatch.ui.TreeView

class CloudFireStore {
    private val db = Firebase.firestore

    fun saveUser(user: User, registerActivity: Register){
        db.collection(Constants.firebaseUserTopic).document(user.id!!).set(user, SetOptions.merge())
            .addOnSuccessListener { registerActivity.registrationSuccess(user)}
            .addOnFailureListener { e->Log.e(registerActivity.javaClass.name,"Error during user saving to cloud",e) }
    }
    fun saveTree(tree: MarkedTree, saveActivity: HomeActivity){
        db.collection(Constants.firebaseTreeTopic).document(tree.id!!).set(tree, SetOptions.merge())
            .addOnSuccessListener { saveActivity.saveSuccess() }
            .addOnFailureListener { e->Log.e(saveActivity.javaClass.name,"Error during user saving to cloud",e) }
    }

    fun getUserData(authActivity: AuthActivity,email: String = ""){

        val uid = getCurrentUID()
        if(uid != "" && TextUtils.isEmpty(email)){

            db.collection(Constants.firebaseUserTopic).document(uid).get()
                .addOnSuccessListener { result ->
                    val user = result.toObject(User::class.java)
                    authActivity.success(user!!)
                }
                .addOnFailureListener { Log.e(authActivity.javaClass.name,
                    Resources.getSystem().getString(R.string.login_error_user_data)
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

    fun getMarkedTrees(treeViewActivity:TreeView){
        db.collection(Constants.firebaseTreeTopic).get().addOnSuccessListener { result->
            val results = result.toObjects(MarkedTree::class.java)
            treeViewActivity.success(results)
        }
    }
    fun deleteTree(id:String, treeViewActivity: TreeView){
        db.collection(Constants.firebaseTreeTopic).document(id).delete().addOnSuccessListener {
            Toast.makeText(treeViewActivity,R.string.tree_view_delete_success,Toast.LENGTH_SHORT)
                .show()
        }
            .addOnFailureListener {
                Toast.makeText(treeViewActivity, R.string.tree_view_delete_failure, Toast.LENGTH_SHORT)
                    .show()
            }
      //  getMarkedTrees(treeViewActivity)
    }

    private fun getUserByEmail(email:String, authActivity: AuthActivity):User?{
        var user: User? = null
        db.collection(Constants.firebaseUserTopic).document(email).get()
            .addOnSuccessListener { result ->
                user = result.toObject(User::class.java)
            }
            .addOnFailureListener {
                Log.e(authActivity.javaClass.name, Resources.getSystem().getString(R.string.error_email_user_nonexistent))
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