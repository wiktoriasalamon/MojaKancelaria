package com.piwniczna.mojakancelaria.trackingmore

class TrackingStatus {
    companion object{
        val DELIVERED = "Odebrano"
        val PICKUP = "Awizo"
        val TRANSIT = "W drodze"
        val RETURNED = "Przesyłka zwrócona"
        val UNKNOWN_CARRIER = "Status nieznany"
        val UNKNOWN_DAYS = "??"
    }
}