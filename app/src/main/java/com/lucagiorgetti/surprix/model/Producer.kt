package com.lucagiorgetti.surprix.model

/**
 * Created by Utente on 17/04/2017.
 */
class Producer {
    var id: String? = null
        private set
    var name: String? = null
        private set
    private var product: String? = null
    var color: String? = null
        private set
    var brandId: String? = null
        private set
    var order = 0

    constructor(name: String, product: String?, order: Int, color: String?) {
        id = if (product != null) name + "_" + product else name
        this.name = if (product != null) "$name $product" else name
        brandId = name
        this.product = product
        this.order = order
        this.color = color
    }

    constructor(name: String?, order: Int, color: String?) {
        id = name
        this.name = name
        brandId = name
        this.color = color
        this.order = order
    }

    constructor()
}