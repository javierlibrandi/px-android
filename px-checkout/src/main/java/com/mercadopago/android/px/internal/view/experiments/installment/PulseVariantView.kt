package com.mercadopago.android.px.internal.view.experiments.installment

import android.content.Context
import android.util.AttributeSet
import android.view.animation.Animation
import android.widget.ImageView
import com.mercadopago.android.px.R
import com.mercadopago.android.px.internal.view.PulseRippleView
import com.mercadopago.android.px.internal.view.experiments.VariantView

class PulseVariantView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : VariantView(context, attrs, defStyleAttr) {

    private var pulse: PulseRippleView? = null
    private var arrow: ImageView? = null

    override fun configureView() {
        val view = inflate(context, R.layout.px_experiment_pulse, this)
        pulse = view.findViewById(R.id.pulse_container)
        arrow = view.findViewById(R.id.arrow)
    }

    fun animateArrow(animation: Animation) {
        arrow?.startAnimation(animation)
    }

    fun animationArrowEnd(): Boolean = arrow?.animation?.hasEnded() ?: false

    fun startPulse() {
        pulse?.startRippleAnimation()
    }

    fun stopPulse() {
        pulse?.stopRippleAnimation()
    }
}