package com.mercadopago.android.px.internal.experiments

interface VariantHandler {
    fun visit(variant: PulseVariant)
    fun visit(variant: BadgeVariant)
}