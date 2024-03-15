package com.lucagiorgetti.surprix.model

/**
 * Created by Utente on 17/04/2017.
 */
class Year {
    var id: String? = null
        private set
    var year = -1
        private set
    var descr: String? = null
        private set
    var producerId: String? = null
        private set
    var producer_color: String? = null
        private set

    constructor(year: Int, descr: String?, producer: Producer) {
        this.year = year
        producerId = producer.id
        this.descr = descr
        producer_color = producer.color
        id = producer.id + "_" + year
    }

    constructor()

    class SortByDescYear : Comparator<Year> {
        override fun compare(o1: Year, o2: Year): Int {
            return o2.year - o1.year
        }
    }
}
