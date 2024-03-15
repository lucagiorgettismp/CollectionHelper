package com.lucagiorgetti.surprix.model

import com.lucagiorgetti.surprix.utility.LoginFlowHelper.AuthMode

/**
 * Created by Utente on 17/04/2017.
 */
class User {
    var email: String? = null
        private set
    var username: String? = null
        private set
    var country: String? = null
        private set
    var provider: String? = null
        private set

    constructor(email: String?, username: String?, country: String?, authMode: AuthMode) {
        this.email = email
        this.username = username
        this.country = country
        provider = authMode.provider
    }

    constructor()

    fun clearedEmail(): String {
        return email!!.replace(",".toRegex(), "\\.")
    }
}