package com.mercadopago.android.px.internal.view.experiments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mercadopago.android.px.internal.experiments.Variant
import com.mercadopago.android.px.model.internal.Experiment

object ExperimentHelper {

    fun getVariantsFrom(experiments: List<Experiment>?, vararg localVariants: Variant) = localVariants.map {
        getVariantFrom(it, experiments)
    }

    private fun getVariantFrom(variant: Variant, experiments: List<Experiment>?): Variant {

        if (experiments != null) {
            for (experiment in experiments) {
                val variantName = experiment.variant.name
                if (variant.isExperiment(experiment.name) && variant.isVariant(variantName)) {
                    return variant
                }
            }
        }
        return variant.default
    }

    fun applyExperimentViewBy(root: ViewGroup, variant: Variant): View = LayoutInflater.from(root.context).inflate(variant.resVariant, root)
}