package com.mercadopago.android.px.model

import java.io.Serializable

interface CardInformation : Serializable {
    val expirationMonth: Int?
    val expirationYear: Int?
    val cardHolder: Cardholder?
    var lastFourDigits: String
    var firstSixDigits: String?
    fun getSecurityCodeLength(): Int?

    companion object {
        const val CARD_NUMBER_MAX_LENGTH = 16
    }
}