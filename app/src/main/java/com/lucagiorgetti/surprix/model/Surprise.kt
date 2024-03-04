package com.lucagiorgetti.surprix.model

/**
 * Created by Utente on 17/04/2017.
 */
class Surprise : Comparable<Surprise> {
    var id: String? = null
        private set
    var description: String? = null
        private set
    var img_path: String? = null
        private set
    var code: String? = null
        private set
    var set_name: String? = null
    var set_year_year = -1
    var set_year_id: String? = null
    var set_year_name: String? = null
    var set_producer_name: String? = null
    var set_producer_id: String? = null
    var set_producer_color: String? = null
    var set_nation: String? = null
    var set_category: String? = null
    var set_id: String? = null
        private set
    var rarity: String? = null
        private set
    var isSet_effective_code = true
        private set

    constructor()
    constructor(description: String?, img_path: String?, code: String, set: Set, rarity: Int?) {
        this.description = description
        this.img_path = img_path
        this.code = code
        set_name = set.name
        set_producer_color = set.producer_color
        set_producer_name = set.producer_name
        set_producer_id = set.producer_id
        isSet_effective_code = set.isEffectiveCode
        set_category = set.category
        set_nation = set.nation
        set_year_year = set.year_year
        set_year_id = set.year_id
        set_year_name = set.year_desc
        id = set_producer_name + "_" + set_year_year + "_" + set.code + "_" + code
        set_id = set.id
        this.rarity = rarity?.toString()
    }

    val intRarity: Int?
        get() = if (rarity != null) rarity!!.toInt() else null

    override fun compareTo(surprise: Surprise): Int {
        try {
            val a = this.code!!.toInt()
            val b = surprise.code!!.toInt()
            return a - b
        } catch (ignored: Exception) {
        }
        return this.code!!.compareTo(surprise.code!!)
    }
}
