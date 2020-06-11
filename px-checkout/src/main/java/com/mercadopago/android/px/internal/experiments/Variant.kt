package com.mercadopago.android.px.internal.experiments

import com.mercadopago.android.px.R

abstract class Variant(var resVariant: Int, private val experimentName: String, private val variantName: String) {
    abstract val default: Variant

    open fun isExperiment(experimentName: String) = this.experimentName == experimentName
    open fun isVariant(variantName: String) = this.variantName == variantName
    abstract fun process(handler: VariantHandler)
}

class PulseVariant(resVariant: Int = R.layout.px_experiment_pulse) : Variant(resVariant, "px_nativo/highlight_installments", "animation_pulse") {
    override val default by lazy { PulseVariant(R.layout.px_experiment_arrow_default) }
    override fun process(handler: VariantHandler) {
        handler.visit(this)
    }
}

class BadgeVariant(resVariant : Int = R.layout.px_experiment_badge) : Variant(resVariant, "px_nativo/highlight_installments", "badge") {
    override val default by lazy { BadgeVariant(R.layout.px_experiment_text_default) }
    override fun process(handler: VariantHandler) {
        handler.visit(this)
    }
}