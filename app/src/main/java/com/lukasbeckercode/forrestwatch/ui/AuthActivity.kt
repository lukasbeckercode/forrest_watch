package com.lukasbeckercode.forrestwatch.ui

import com.lukasbeckercode.forrestwatch.models.User

/**
 * Simple interface used for activities that retrieve user data
 * @author lukas becker
 */
interface AuthActivity {
    /**
     * Method called after data retrieval was successful
     *
     * @param user retrieved user data
     */
    fun success(user: User)


    /**
     * Method called if data retrieval fails
     */
    fun fail()
}