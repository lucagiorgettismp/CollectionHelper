package com.lucagiorgetti.surprix.ui.mainfragments.catalog.sets

import com.lucagiorgetti.surprix.model.Set

class CatalogSet {
    val set: Set
    var isInCollection: Boolean
    private val hasMissing: Boolean

    constructor(set: Set, inCollection: Boolean, hasMissing: Boolean) {
        isInCollection = inCollection
        this.set = set
        this.hasMissing = hasMissing
    }

    constructor(set: Set, hasMissing: Boolean) {
        this.set = set
        isInCollection = true
        this.hasMissing = hasMissing
    }

    fun hasMissing(): Boolean {
        return hasMissing
    }
}
