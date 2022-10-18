package com.lukasbeckercode.forrestwatch.models

import android.text.TextUtils
import com.lukasbeckercode.forrestwatch.database.CloudFireStore
import com.lukasbeckercode.forrestwatch.ui.HomeActivity
import java.util.*

class MarkedTree(var lat:Double,
                 var lang:Double,
                 var name:String,
                 var user: User,
                 private val saveActivity: HomeActivity,
                 val id: UUID = UUID.randomUUID()) {

    fun save():Boolean{
        if (TextUtils.isEmpty(name)){
            return false
        }
        CloudFireStore().saveTree(tree = this,saveActivity)
        return true
    }
}