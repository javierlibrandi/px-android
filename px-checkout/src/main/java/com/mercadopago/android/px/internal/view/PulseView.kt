package com.mercadopago.android.px.internal.view

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.RelativeLayout
import com.mercadopago.android.px.R
import java.util.*
import kotlin.math.min


class PulseView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : RelativeLayout(context, attrs, defStyleAttr) {
    private var rippleColor = 0
    private var rippleStrokeWidth = 0f
    private var rippleRadius = 0f
    private var rippleDurationTime = 0
    private var rippleAmount = 0
    private var rippleDelay = 0
    private var rippleScale = 0f
    private var rippleType = 0
    private var isRippleAnimationRunning = false
    private lateinit var animatorSet: AnimatorSet
    private lateinit var animatorList: ArrayList<Animator>
    private lateinit var rippleParams: LayoutParams
    private val rippleViewList = ArrayList<RippleView>()

    init {
        init(context, attrs)
    }
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    
    constructor(context: Context) : this(context, null)

    private fun init(context: Context, attrs: AttributeSet?) {
        if (isInEditMode) return
        requireNotNull(attrs) { "Attributes should not be null" }
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PulseView)
        rippleColor = typedArray.getColor(R.styleable.PulseView_pulse_color, ContextCompat.getColor(context, R.color.rippelColor))
        rippleStrokeWidth = typedArray.getDimension(R.styleable.PulseView_pulse_strokeWidth, resources.getDimension(R.dimen.rippleStrokeWidth))
        rippleRadius = typedArray.getDimension(R.styleable.PulseView_pulse_radius, resources.getDimension(R.dimen.rippleRadius))
        rippleDurationTime = typedArray.getInt(R.styleable.PulseView_pulse_duration, DEFAULT_DURATION_TIME)
        rippleAmount = typedArray.getInt(R.styleable.PulseView_pulse_rippleAmount, DEFAULT_RIPPLE_COUNT)
        rippleScale = typedArray.getFloat(R.styleable.PulseView_pulse_scale, DEFAULT_SCALE)
        rippleType = typedArray.getInt(R.styleable.PulseView_pulse_type, DEFAULT_FILL_TYPE)
        typedArray.recycle()

        rippleDelay = rippleDurationTime / rippleAmount

        val paint = Paint()
        paint.isAntiAlias = true

        if (rippleType == DEFAULT_FILL_TYPE) {
            rippleStrokeWidth = 0f
            paint.style = Paint.Style.FILL
        } else {
            paint.style = Paint.Style.STROKE
        }

        paint.color = rippleColor

        rippleParams = LayoutParams((2 * (rippleRadius + rippleStrokeWidth)).toInt(), (2 * (rippleRadius + rippleStrokeWidth)).toInt())
        rippleParams.addRule(CENTER_IN_PARENT, TRUE)
        animatorSet = AnimatorSet()
        animatorSet.interpolator = AccelerateDecelerateInterpolator()
        animatorList = ArrayList()

        for (i in 0 until rippleAmount) {
            val rippleView = RippleView(getContext(), paint)
            addView(rippleView, rippleParams)
            rippleViewList.add(rippleView)
            val scaleXAnimator = ObjectAnimator.ofFloat(rippleView, "ScaleX", 1.0f, rippleScale)
            scaleXAnimator.repeatCount = ObjectAnimator.INFINITE
            scaleXAnimator.repeatMode = ObjectAnimator.REVERSE
            scaleXAnimator.startDelay = i * rippleDelay.toLong()
            scaleXAnimator.duration = rippleDurationTime.toLong()
            animatorList.add(scaleXAnimator)
            val scaleYAnimator = ObjectAnimator.ofFloat(rippleView, "ScaleY", 1.0f, rippleScale)
            scaleYAnimator.repeatCount = ObjectAnimator.INFINITE
            scaleYAnimator.repeatMode = ObjectAnimator.REVERSE
            scaleYAnimator.startDelay = i * rippleDelay.toLong()
            scaleYAnimator.duration = rippleDurationTime.toLong()
            animatorList.add(scaleYAnimator)
        }
        animatorSet.playTogether(animatorList)
    }

    private inner class RippleView(context: Context?, paint: Paint) : View(context) {
        private var paint: Paint
        init {
            visibility = INVISIBLE
            this.paint = paint
        }

        override fun onDraw(canvas: Canvas) {
            val radius = min(width, height) / 2
            canvas.drawCircle(radius.toFloat(), radius.toFloat(), radius - rippleStrokeWidth, paint)
        }
    }

    fun startRippleAnimation() {
        if (!isRippleAnimationRunning) {
            for (rippleView in rippleViewList) {
                rippleView.visibility = View.VISIBLE
            }
            animatorSet.start()
            isRippleAnimationRunning = true
        }
    }

    fun stopRippleAnimation() {
        if (isRippleAnimationRunning) {
            animatorSet.end()
            isRippleAnimationRunning = false
        }
        for (rippleView in rippleViewList) {
            rippleView.visibility = View.GONE
        }
    }

    companion object {
        private const val DEFAULT_RIPPLE_COUNT = 6
        private const val DEFAULT_DURATION_TIME = 3000
        private const val DEFAULT_SCALE = 6.0f
        private const val DEFAULT_FILL_TYPE = 0
    }
}