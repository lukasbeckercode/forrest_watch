package com.lukasbeckercode.forrestwatch.models

import android.text.TextUtils
import com.lukasbeckercode.forrestwatch.database.CloudFireStore
import com.lukasbeckercode.forrestwatch.ui.HomeActivity

/**
 * Represents a tree that has been marked as infested
 * @param lat latitude of marked tree
 * @param long longitude of marked tree
 * @param name name for the tree chosen by the user
 * @param user the current user marking the tree
 * @param saveActivity original calling activity (needed for context)
 * @param id UUID as String to uniquely identify trees (names are not unique!)
 */
class MarkedTree(var lat:Double?,
                 var long:Double?,
                 var name:String?,
                 var user: User?,
                 private val saveActivity: HomeActivity?,
                 var id: String? ) {
    constructor() : this(
        null,null,null,null,null, ""
    )

    /**
     * Saves a Tree to FireStore
     */
    fun save():Boolean{
        //name cannot be empty
        if (TextUtils.isEmpty(name)){
            return false
        }
        CloudFireStore().saveTree(tree = this,saveActivity!!)
        return true
    }
}