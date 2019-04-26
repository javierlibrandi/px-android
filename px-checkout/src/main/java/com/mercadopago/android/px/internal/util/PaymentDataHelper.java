package com.mercadopago.android.px.internal.util;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.model.PaymentData;
import java.math.BigDecimal;
import java.util.Collection;

public final class PaymentDataHelper {

    private PaymentDataHelper() {
    }

    public static BigDecimal getPrettyAmountToPay(@NonNull final PaymentData paymentData) {
        if (paymentData.getDiscount() != null) {
            return paymentData.getRawAmount().subtract(paymentData.getDiscount().getCouponAmount());
        }
        return paymentData.getRawAmount();
    }

    public static boolean isSplitPayment(@NonNull final Collection<PaymentData> paymentDataList) {
        return paymentDataList.size() > 1;
    }

    public static BigDecimal getTotalDiscountAmount(final Iterable<PaymentData> paymentDataList) {
        BigDecimal totalDiscountAmount = BigDecimal.ZERO;

        for (final PaymentData paymentData : paymentDataList) {
            if (paymentData.getDiscount() != null) {
                totalDiscountAmount = totalDiscountAmount.add(paymentData.getDiscount().getCouponAmount());
            }
        }
            return totalDiscountAmount;
    }
}
