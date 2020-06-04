package com.mercadopago.android.px.internal.view.experiments

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.mercadopago.android.px.R
import com.mercadopago.android.px.internal.experiments.VariantType

abstract class ExperimentView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : FrameLayout(context, attrs, defStyleAttr) {

    protected var variantView: VariantView? = null
    protected lateinit var variantType: VariantType
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)

    init {
        configureView()
    }

    private fun configureView() {
        inflate(context, R.layout.px_experiment_view, this)
    }

    fun applyVariant(variantType: VariantType) {

        var id = 0
        context?.apply {
            id = resources.getIdentifier(variantType.getName(), "id", packageName)
        }

        check(id != 0) {
            "wrong id for variantView"
        }

        variantView = findViewById(id)

        check(variantView != null) {
            "variantView not be null"
        }

        this.variantType = variantType

        for (i in 0 until childCount) {
           getChildAt(i)?.apply {
               if (this.id == id) {
                   variantView?.visibility = VISIBLE
               } else {
                   visibility = GONE
               }
           }
        }
    }

    fun getVariant() = variantView
}