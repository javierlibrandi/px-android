package com.mercadopago.android.px.internal.view;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.experiments.VariantType;
import com.mercadopago.android.px.internal.view.experiments.ExperimentHelper;
import com.mercadopago.android.px.internal.viewmodel.GoingToModel;

import static com.mercadopago.android.px.internal.util.ViewUtils.hasEndedAnim;

public class PaymentMethodHeaderView extends FrameLayout {

    /* default */ final View titleView;

    /* default */ final ImageView helper;

    /* default */ final Animation rotateUp;

    /* default */ final Animation rotateDown;

    private FrameLayout experimentContainer;

    private PulseView pulse;

    private ImageView arrow;

    private final TitlePager titlePager;

    private boolean isDisabled;
    private boolean clicked = false;

    public interface Listener {
        void onDescriptorViewClicked();

        void onDisabledDescriptorViewClick();

        void onInstallmentsSelectorCancelClicked();
    }

    public PaymentMethodHeaderView(final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PaymentMethodHeaderView(final Context context, @Nullable final AttributeSet attrs,
        final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.px_view_installments_header, this, true);
        rotateUp = AnimationUtils.loadAnimation(context, R.anim.px_rotate_up);
        rotateDown = AnimationUtils.loadAnimation(context, R.anim.px_rotate_down);
        titleView = findViewById(R.id.installments_title);
        experimentContainer = findViewById(R.id.pulse_experiment_container);
        titlePager = findViewById(R.id.title_pager);
        helper = findViewById(R.id.helper);
        titleView.setVisibility(GONE);
    }

    public void updateData(final boolean hasPayerCost, final boolean isDisabled) {
        final boolean isEspandible = hasPayerCost && !isDisabled;
        this.isDisabled = isDisabled;

        showTitlePager(hasPayerCost);
        setArrowVisibility(isEspandible);
        setHelperVisibility(isDisabled);

        setClickable(hasPayerCost || isDisabled);
    }

    public void setListener(final Listener listener) {
        setOnClickListener(v -> {
            if (isDisabled) {
                listener.onDisabledDescriptorViewClick();
            } else if (hasEndedAnim(arrow)) {
                if (titleView.getVisibility() == VISIBLE) {
                    arrow.startAnimation(rotateDown);
                    listener.onInstallmentsSelectorCancelClicked();
                } else {
                    arrow.startAnimation(rotateUp);
                    listener.onDescriptorViewClicked();
                    if (!clicked && pulse != null) {
                        pulse.stopRippleAnimation();
                        clicked = true;
                    }
                }
            }
        });
    }

    public void configureBadgeExperiments(@NonNull final VariantType variantType) {
        titlePager.setExperimentVariant(variantType);
    }

    public void configurePulseExperiments(@NonNull final VariantType variantType) {
        ExperimentHelper.INSTANCE.applyExperimentViewBy(experimentContainer, variantType);
        if (!(variantType instanceof VariantType.Default)) {
            pulse = experimentContainer.findViewById(R.id.pulse);
            if (!clicked) {
                pulse.startRippleAnimation();
            }
        }
        arrow = experimentContainer.findViewById(R.id.arrow);
    }

    public void showInstallmentsListTitle() {
        titleView.setVisibility(VISIBLE);
        titlePager.setVisibility(GONE);
    }

    public void showTitlePager(final boolean isClickable) {
        if (titleView.getVisibility() == VISIBLE) {
            arrow.startAnimation(rotateDown);
        }

        titlePager.setVisibility(VISIBLE);
        titleView.setVisibility(GONE);

        setClickable(isClickable);
    }

    public void trackPagerPosition(float positionOffset, final Model model) {
        if (model.goingTo == GoingToModel.BACKWARDS) {
            positionOffset = 1.0f - positionOffset;
        }

        if (model.currentIsExpandable) {
            if (model.nextIsExpandable) {
                experimentContainer.setAlpha(1.0f);
            } else {
                experimentContainer.setAlpha(1.0f - positionOffset);
            }
        } else {
            if (model.nextIsExpandable) {
                experimentContainer.setAlpha(positionOffset);
            } else {
                experimentContainer.setAlpha(0.0f);
            }
        }
    }

    public void setArrowVisibility(final boolean visible) {
        experimentContainer.setAlpha(visible ? 1.0f : 0.0f);
    }

    public void setHelperVisibility(final boolean visible) {
        helper.setVisibility(visible ? VISIBLE : GONE);
    }

    @Override
    protected void onRestoreInstanceState(final Parcelable state) {
        final HeaderViewState saveState = (HeaderViewState) state ;
        super.onRestoreInstanceState(saveState.getSuperState());
        clicked = saveState.getClicked();
        if (clicked && pulse != null) {
            pulse.stopRippleAnimation();
        }
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        final HeaderViewState state = new HeaderViewState(super.onSaveInstanceState());
        state.setClicked(clicked);
        return state;
    }

    public static class Model {
        final GoingToModel goingTo;
        final boolean currentIsExpandable;
        final boolean nextIsExpandable;

        public Model(final GoingToModel goingTo, final boolean currentIsExpandable,
            final boolean nextIsExpandable) {
            this.goingTo = goingTo;
            this.currentIsExpandable = currentIsExpandable;
            this.nextIsExpandable = nextIsExpandable;
        }
    }

    static class HeaderViewState extends BaseSavedState {

        private boolean clicked;

        HeaderViewState(final Parcel source) {
            super(source);
            clicked = source.readByte() != (byte) 0;
        }

        HeaderViewState(final Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(final Parcel out, final int flags) {
            super.writeToParcel(out, flags);
            out.writeByte((byte)(clicked ? 1 : 0));
        }

        void setClicked(final boolean clicked) {
            this.clicked = clicked;
        }

        boolean getClicked() {
            return clicked;
        }

        public static final Parcelable.Creator<HeaderViewState> CREATOR =
            new Parcelable.Creator<HeaderViewState>() {
                public HeaderViewState createFromParcel(Parcel in) { return new HeaderViewState(in); }
                public HeaderViewState[] newArray(int size) { return new HeaderViewState[size]; }
            };
    }
}