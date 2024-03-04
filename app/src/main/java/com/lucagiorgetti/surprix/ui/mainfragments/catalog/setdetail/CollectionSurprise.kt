package com.lucagiorgetti.surprix.ui.mainfragments.catalog.setdetail

import com.lucagiorgetti.surprix.model.Surprise

class CollectionSurprise {
    val isMissing: Boolean
    val surprise: Surprise

    constructor(missing: Boolean, surprise: Surprise) {
        isMissing = missing
        this.surprise = surprise
    }

    constructor(surprise: Surprise) {
        isMissing = false
        this.surprise = surprise
    }
}
