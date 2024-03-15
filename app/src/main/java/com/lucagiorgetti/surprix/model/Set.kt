package com.lucagiorgetti.surprix.model

/**
 * Created by Utente on 17/04/2017.
 */
class Set {
    var id: String? = null
        private set
    var code: String? = null
        private set
    var name: String? = null
        private set
    var year_id: String? = null
        private set
    var year_desc: String? = null
        private set
    var year_year = -1
        private set
    var producer_id: String? = null
        private set
    var producer_name: String? = null
        private set
    var producer_color: String? = null
        private set
    var thanks_to: String? = null
        private set
    var nation: String? = null
        private set
    var img_path: String? = null
        private set
    var category: String? = null
        private set
    var isEffectiveCode = true
        private set


    constructor(name: String?, code: String, year: Year, producer: Producer, nation: String?, img_path: String?, category: String?) {
        this.name = name
        this.code = code
        producer_id = producer.id
        producer_name = producer.name
        producer_color = producer.color
        year_id = year.id
        year_desc = year.descr
        year_year = year.year
        this.nation = nation
        this.img_path = img_path
        this.category = category
        id = producer.name + "_" + year.year + "_" + code
    }

    constructor(name: String?, code: String, year: Year, producer: Producer, nation: String?, img_path: String?, category: String?, effectiveCode: Boolean) {
        this.name = name
        this.code = code
        producer_id = producer.id
        producer_name = producer.name
        producer_color = producer.color
        year_id = year.id
        year_desc = year.descr
        year_year = year.year
        this.nation = nation
        this.img_path = img_path
        this.category = category
        id = producer.name + "_" + year.year + "_" + code
        isEffectiveCode = effectiveCode
    }

    constructor()
}