package com.mercadopago.android.px.internal.experiments

sealed class VariantType {
    protected open var experimentName = ""
    protected abstract val variantName: String
    protected open val features: Map<String, Any> = emptyMap()

    object Control: VariantType() {
        override var experimentName = super.experimentName
        override val variantName = "control"

        override fun isExperiment(experimentName: String): Boolean {
            return experimentName == this.experimentName
        }

        override fun isVariant(variantName: String): Boolean {
            return variantName == this.variantName
        }
    }

    fun getName() = experimentName

    fun getAvailableFeatures() = features

    open fun isExperiment(experimentName: String): Boolean {
        return Control.isExperiment(experimentName)
    }

    open fun isVariant(variantName: String): Boolean {
        return Control.isVariant(variantName)
    }
}

sealed class PulseVariant : VariantType()  {
    override var experimentName = "px_nativo/highlight_installments"

    object Pulse: PulseVariant() {
        override val variantName = "pulse"

        override fun isExperiment(experimentName: String): Boolean {
            return super.isExperiment(experimentName) || this.experimentName == experimentName
        }

        override fun isVariant(variantName: String): Boolean {
            return super.isVariant(variantName) || this.variantName == variantName
        }
    }
}

sealed class LabelVariant : VariantType() {
    override var experimentName = "px_nativo/highlight_installments"

    object Label: LabelVariant() {
        override val variantName = "label"

        override fun isExperiment(experimentName: String): Boolean {
            return super.isExperiment(experimentName) || this.experimentName == experimentName
        }

        override fun isVariant(variantName: String): Boolean {
            return super.isVariant(variantName) || this.variantName == variantName
        }
    }
}