package com.mercadopago.android.px.internal.features.payment_result.remedies

import com.mercadopago.android.px.internal.repository.AmountRepository
import com.mercadopago.android.px.internal.repository.UserSelectionRepository
import com.mercadopago.android.px.internal.viewmodel.mappers.Mapper
import com.mercadopago.android.px.model.PaymentData
import com.mercadopago.android.px.model.internal.remedies.RemediesBody
import com.mercadopago.android.px.model.internal.remedies.RemedyPaymentMethod

internal class RemediesBodyMapper(private val userSelectionRepository: UserSelectionRepository,
    private val amountRepository: AmountRepository, private val customOptionId: String,
    private val esc: Boolean, private val alternativePayerPaymentMethods: List<RemedyPaymentMethod>)
    : Mapper<PaymentData, RemediesBody>() {

    override fun map(data: PaymentData): RemediesBody {
        val (secCodeLocation, secCodeLength, escStatus) = userSelectionRepository.card?.let {
            Triple(it.getSecurityCodeLocation(), it.getSecurityCodeLength(), it.escStatus)
        } ?: data.token?.let {
            Triple(DEFAULT_CVV_LOCATION, it.getSecurityCodeLength(), null)
        } ?: Triple(null, null, null)
        with(data) {
            val payerPaymentMethodRejected = RemedyPaymentMethod(customOptionId, payerCost?.installments,
                issuer?.name, token?.lastFourDigits, paymentMethod.id, paymentMethod.paymentTypeId,
                secCodeLength, secCodeLocation, amountRepository.getAmountToPay(paymentMethod.paymentTypeId, payerCost),
                null, escStatus, esc)
            return RemediesBody(payerPaymentMethodRejected, alternativePayerPaymentMethods)
        }
    }

    companion object {
        private const val DEFAULT_CVV_LOCATION = "back"
    }
}