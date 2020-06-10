package com.mercadopago.android.px.internal.extensions

import android.app.Activity
import android.graphics.Rect
import android.view.View

fun CharSequence?.isNotNullNorEmpty() = !isNullOrEmpty()

fun CharSequence?.orIfEmpty(fallback: String) = if (isNotNullNorEmpty()) this!! else fallback

fun View.gone() = apply { visibility = View.GONE }

fun View.visible() = apply { visibility = View.VISIBLE }

fun View.invisible() = apply { visibility = View.INVISIBLE }

fun Any?.runIfNull(action: ()->Unit) {
    if(this == null) {
        action.invoke()
    }
}

internal fun Activity.addKeyBoardListener(
    onKeyBoardOpen: (() -> Unit)? = null,
    onKeyBoardClose: (() -> Unit)? = null
) {
    window.decorView.rootView?.apply {
        viewTreeObserver?.addOnGlobalLayoutListener {
            val r = Rect()

            getWindowVisibleDisplayFrame(r)

            val heightDiff = rootView.height - (r.bottom - r.top)
            if (heightDiff > rootView.height * 0.15) {
                onKeyBoardOpen?.invoke()
            } else {
                onKeyBoardClose?.invoke()
            }
        }
    }
}