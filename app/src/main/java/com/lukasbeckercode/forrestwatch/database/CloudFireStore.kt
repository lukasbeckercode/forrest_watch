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

/**
 * Class handling all the read/write/delete functionality used with FireStore
 * @author lukas becker
 */
class CloudFireStore {
    private val db = Firebase.firestore

    /**
     * saves user data (not password!)
     * not used for authentication!
     * @param user User object containing all the necessary data
     * @param registerActivity base Activity calling this function
     */
    fun saveUser(user: User, registerActivity: Register){
        db.collection(Constants.firebaseUserTopic).document(user.id!!).set(user, SetOptions.merge())
            .addOnSuccessListener { registerActivity.registrationSuccess(user)}
            .addOnFailureListener { e->Log.e(registerActivity.javaClass.name,"Error during user saving to cloud",e) }
    }

    /**
     * Method used to save a Tree to FireStore
     * @param tree MarkedTree Object containing all the necessary data
     * @param saveActivity originally calling Activity
     */
    fun saveTree(tree: MarkedTree, saveActivity: HomeActivity){
        db.collection(Constants.firebaseTreeTopic).document(tree.id!!).set(tree, SetOptions.merge())
            .addOnSuccessListener { saveActivity.saveSuccess() }
            .addOnFailureListener { e->Log.e(saveActivity.javaClass.name,"Error during user saving to cloud",e) }
    }

    /**
     * gets user data either by UID or by email
     * @see AuthActivity
     * @param authActivity Activity implementing this interface calling this method
     * @param email email address to be looked for
     * @see com.lukasbeckercode.forrestwatch.ui.ForgotPassword
     */
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

    /**
     * gets all the Trees from FireSotre
     */
    fun getMarkedTrees(treeViewActivity:TreeView){
        db.collection(Constants.firebaseTreeTopic).get().addOnSuccessListener { result->
            val results = result.toObjects(MarkedTree::class.java)
            treeViewActivity.success(results)
        }
    }

    /**
     * deletes the tree
     * @param id the id of the tree to be deleted
     */
    fun deleteTree(id:String, treeViewActivity: TreeView){
        db.collection(Constants.firebaseTreeTopic).document(id).delete().addOnSuccessListener {
            Toast.makeText(treeViewActivity,R.string.tree_view_delete_success,Toast.LENGTH_SHORT)
                .show()
        }
            .addOnFailureListener {
                Toast.makeText(treeViewActivity, R.string.tree_view_delete_failure, Toast.LENGTH_SHORT)
                    .show()
            }
    }

    /**
     * gets a User object by looking for it's email address
     */
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

    /**
     * get UID of any currently logged in User
     */
    private fun getCurrentUID(): String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null){
            return currentUser.uid
        }

        return ""
    }
}