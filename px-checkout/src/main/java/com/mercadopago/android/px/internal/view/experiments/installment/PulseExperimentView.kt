package com.mercadopago.android.px.internal.view.experiments.installment

import android.content.Context
import android.util.AttributeSet
import android.view.animation.Animation
import com.mercadopago.android.px.internal.experiments.PulseVariant
import com.mercadopago.android.px.internal.view.experiments.ExperimentView

class PulseExperimentView(context: Context, attrs: AttributeSet?, defStyleAttr: Int): ExperimentView(context, attrs, defStyleAttr) {

    fun animateArrow(animation: Animation) {
        when(variantType) {
            is PulseVariant -> {
                (variantView as PulseVariantView).animateArrow(animation)
            }
            else -> {
                (variantView as ArrowControlVariantView?)?.animateArrow(animation)
            }
        }
    }

    fun startPulse() {
        if (variantType is PulseVariant) {
            (variantView as PulseVariantView).startPulse()
        }
    }

    fun stopPulse() {
        if (variantType is PulseVariant) {
            (variantView as PulseVariantView?)?.stopPulse()
        }
    }
}