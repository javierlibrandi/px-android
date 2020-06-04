package com.mercadopago.android.px.internal.view.experiments

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

abstract class VariantView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : FrameLayout(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)

    init {
        configureView()
    }

    protected abstract fun configureView()
}