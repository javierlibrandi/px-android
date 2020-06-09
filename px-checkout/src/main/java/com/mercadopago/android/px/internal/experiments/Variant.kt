package com.mercadopago.android.px.internal.experiments

import com.mercadopago.android.px.R

sealed class VariantType {
    protected abstract val variantName: String
    protected open var experimentName: String = ""
    protected open var resVariant: Int = 0

    open fun getVariantResource() = resVariant
    open fun isExperiment(experimentName: String) = this.experimentName == experimentName
    open fun isVariant(variantName: String) = this.variantName == variantName
    abstract fun isDefault(variantName: String): Boolean

    class Default(override var resVariant: Int) : VariantType() {
        override val variantName = "control"
        override fun isDefault(variantName: String) = this.variantName == variantName
    }

    abstract class Variant(override var experimentName: String, override val variantName: String) : VariantType() {
        abstract val default: Default
        fun getDefaultVariant() = default
        override fun isDefault(variantName: String) = default.isDefault(variantName)
    }
}

object PulseVariant : VariantType.Variant("px_nativo/highlight_installments", "animation_pulse") {
    override var resVariant = R.layout.px_experiment_pulse
    override val default = Default(R.layout.px_experiment_arrow_default)
}

object BadgeVariant : VariantType.Variant("px_nativo/highlight_installments", "badge") {
    override var resVariant = R.layout.px_experiment_badge
    override val default = Default(R.layout.px_experiment_text_default)
}