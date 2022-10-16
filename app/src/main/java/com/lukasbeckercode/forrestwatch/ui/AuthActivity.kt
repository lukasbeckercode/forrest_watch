package com.lukasbeckercode.forrestwatch.ui

import com.lukasbeckercode.forrestwatch.models.User

interface AuthActivity {
    fun success(user: User)
    fun fail()
}