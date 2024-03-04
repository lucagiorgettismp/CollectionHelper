package com.lucagiorgetti.surprix.model

import com.lucagiorgetti.surprix.utility.LoginFlowHelper.AuthMode

class Uid {
    var uid: String? = null
    var username: String? = null
    var provider: String? = null

    constructor(uid: String?, username: String?, authMode: AuthMode) {
        this.uid = uid
        this.username = username
        provider = authMode.provider
    }

    constructor()
}
