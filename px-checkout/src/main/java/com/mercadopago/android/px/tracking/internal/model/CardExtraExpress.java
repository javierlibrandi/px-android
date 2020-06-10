package com.mercadopago.android.px.tracking.internal.model;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mercadopago.android.px.model.CardMetadata;
import com.mercadopago.android.px.model.PayerCost;

@SuppressWarnings("unused")
@Keep
public final class CardExtraExpress extends CardExtraInfo {

    private final boolean hasSplit;
    private final boolean hasInterestFree;
    private final boolean hasReimbursement;

    private CardExtraExpress(@Nullable final String cardId, final boolean hasEsc, @Nullable final Long issuerId,
        @Nullable final PayerCostInfo payerCostTrackModel, final boolean hasSplit, final boolean hasInterestFree, final boolean hasReimbursement) {
        super(cardId, hasEsc, issuerId, payerCostTrackModel);
        this.hasSplit = hasSplit;
        this.hasInterestFree = hasInterestFree;
        this.hasReimbursement = hasReimbursement;
    }

    // esto es lo que se ofrece
    @NonNull
    public static CardExtraExpress expressSavedCard(final CardMetadata card, final boolean hasEsc,
        final boolean hasSplit, final boolean hasInterestFree, final boolean hasReimbursement) {
        return new CardExtraExpress(card.getId(),
            hasEsc,
            card.getDisplayInfo().issuerId, null, hasSplit, hasInterestFree, hasReimbursement);
    }

    // este es el de confirm
    @NonNull
    public static CardExtraExpress selectedExpressSavedCard(final CardMetadata card, final PayerCost payerCost,
        final boolean hasEsc,
        final boolean hasSplit) {
        return new CardExtraExpress(card.getId(), hasEsc, card.getDisplayInfo().issuerId, new PayerCostInfo(payerCost),
            hasSplit, false, false);
    }
}
