package com.mercadopago.android.px.internal.datasource

import com.mercadopago.android.px.addons.ESCManagerBehaviour
import com.mercadopago.android.px.core.MercadoPagoCheckout
import com.mercadopago.android.px.internal.core.FlowIdProvider
import com.mercadopago.android.px.internal.services.CheckoutService
import com.mercadopago.android.px.internal.services.Response
import com.mercadopago.android.px.internal.services.awaitCallback
import com.mercadopago.android.px.internal.util.JsonUtil
import com.mercadopago.android.px.model.internal.CheckoutFeatures
import com.mercadopago.android.px.model.internal.InitRequest
import java.util.*

internal class PrefetchInitService(private val checkout: MercadoPagoCheckout,
    private val checkoutService: CheckoutService,
    private val escManagerBehaviour: ESCManagerBehaviour,
    private val flowIdProvider: FlowIdProvider) {

    suspend fun get(): Response {
        val checkoutPreference = checkout.checkoutPreference
        val paymentConfiguration = checkout.paymentConfiguration
        val discountParamsConfiguration = checkout.advancedConfiguration.discountParamsConfiguration

        val features = CheckoutFeatures.Builder()
            .setSplit(paymentConfiguration.paymentProcessor.supportsSplitPayment(checkoutPreference))
            .setExpress(checkout.advancedConfiguration.isExpressPaymentEnabled)
            .setOdrFlag(true)
            .build()

        val body = JsonUtil.getMapFromObject(InitRequest.Builder(checkout.publicKey)
            .setCardWithEsc(ArrayList(escManagerBehaviour.escCardIds))
            .setCharges(paymentConfiguration.charges)
            .setDiscountParamsConfiguration(discountParamsConfiguration)
            .setCheckoutFeatures(features)
            .setCheckoutPreference(checkoutPreference)
            .setFlow(flowIdProvider.flowId)
            .build())

        return checkout.preferenceId?.let {
            checkoutService.checkout(it, checkout.privateKey, body).awaitCallback()
        } ?: run {
            checkoutService.checkout(checkout.privateKey, body).awaitCallback()
        }
    }
}