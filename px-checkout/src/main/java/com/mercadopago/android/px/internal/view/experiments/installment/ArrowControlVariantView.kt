package com.mercadopago.android.px.internal.view.experiments.installment

import android.content.Context
import android.util.AttributeSet
import android.view.animation.Animation
import android.widget.ImageView
import com.mercadopago.android.px.R
import com.mercadopago.android.px.internal.view.experiments.VariantView

class ArrowControlVariantView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : VariantView(context, attrs, defStyleAttr) {

    var arrow: ImageView? = null

    override fun configureView() {
        val view = inflate(context, R.layout.px_experiment_arrow_default, this)
        arrow = view.findViewById(R.id.arrow)
    }

    fun animateArrow(animation: Animation) {
        arrow?.startAnimation(animation)
    }

    fun animationArrowEnd(): Boolean = arrow?.animation?.hasEnded() ?: false

}