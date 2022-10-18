package com.lukasbeckercode.forrestwatch.models

import android.text.TextUtils
import com.lukasbeckercode.forrestwatch.database.CloudFireStore
import com.lukasbeckercode.forrestwatch.ui.HomeActivity

class MarkedTree(var lat:Double?,
                 var long:Double?,
                 var name:String?,
                 var user: User?,
                 private val saveActivity: HomeActivity?,
                 var id: String? ) {
    constructor() : this(
        null,null,null,null,null, ""
    )

    fun save():Boolean{
        if (TextUtils.isEmpty(name)){
            return false
        }
        CloudFireStore().saveTree(tree = this,saveActivity!!)
        return true
    }
}