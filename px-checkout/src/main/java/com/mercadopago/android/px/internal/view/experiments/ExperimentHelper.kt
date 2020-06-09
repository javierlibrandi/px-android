package com.mercadopago.android.px.internal.view.experiments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mercadopago.android.px.internal.experiments.VariantType
import com.mercadopago.android.px.model.internal.experiments.Experiment

object ExperimentHelper {

    fun getVariantFrom(variantType: VariantType.Variant, experiments: List<Experiment>): VariantType {
        for (experiment in experiments) {
            val variantName = experiment.variant.name
            if (variantType.isExperiment(experiment.name) && variantType.isVariant(variantName)) {
                return if (variantType.isDefault(variantName)) variantType.getDefaultVariant() else variantType
            }
        }
        return variantType.getDefaultVariant()
    }

    fun <T: View?> applyExperimentViewBy(root: ViewGroup, variantType: VariantType) = applyExperimentViewInParent<T>(root, variantType.getVariantResource())

    @Suppress("UNCHECKED_CAST")
    fun <T: View?> applyExperimentViewInParent(root: ViewGroup, resVariantLayout: Int) = LayoutInflater.from(root.context).inflate(resVariantLayout, root) as T
}