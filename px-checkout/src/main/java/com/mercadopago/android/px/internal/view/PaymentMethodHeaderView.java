package com.mercadopago.android.px.internal.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.experiments.PulseVariant;
import com.mercadopago.android.px.internal.experiments.VariantType;
import com.mercadopago.android.px.internal.view.experiments.installment.PulseExperimentView;
import com.mercadopago.android.px.internal.viewmodel.GoingToModel;
import com.mercadopago.android.px.model.internal.experiments.Experiment;

import static com.mercadopago.android.px.internal.util.ViewUtils.hasEndedAnim;

public class PaymentMethodHeaderView extends FrameLayout {

    /* default */ final View titleView;

    /* default */ PulseExperimentView arrow;

    /* default */ final PulseRippleView rippleContent;

    /* default */ final ImageView helper;

    /* default */ final Animation rotateUp;

    /* default */ final Animation rotateDown;

    private final TitlePager titlePager;

    private boolean isDisabled;

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
        titlePager = findViewById(R.id.title_pager);
        arrow = findViewById(R.id.pulse_experiment);
        rippleContent = findViewById(R.id.pulse_container);
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
                    arrow.animateArrow(rotateDown);
                    listener.onInstallmentsSelectorCancelClicked();
                } else {
                    arrow.animateArrow(rotateUp);
                    listener.onDescriptorViewClicked();
                }
            }
        });
    }

    public void configureExperiments(@Nullable final Experiment experiment) {
        if (experiment != null) {
            loadExperimentView();
        } else {
            loadDefaultExperimentView();
        }
    }

    private void loadExperimentView() {
        arrow.applyVariant(PulseVariant.Pulse.INSTANCE);
    }

    private void loadDefaultExperimentView() {
        arrow.applyVariant(PulseVariant.Control.INSTANCE);
    }

    public void showInstallmentsListTitle() {
        titleView.setVisibility(VISIBLE);
        titlePager.setVisibility(GONE);
    }

    public void showTitlePager(final boolean isClickable) {
        if (titleView.getVisibility() == VISIBLE) {
            arrow.animateArrow(rotateDown);
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
                arrow.setAlpha(1.0f);
            } else {
                arrow.setAlpha(1.0f - positionOffset);
            }
        } else {
            if (model.nextIsExpandable) {
                arrow.setAlpha(positionOffset);
            } else {
                arrow.setAlpha(0.0f);
            }
        }
    }

    public void setArrowVisibility(final boolean visible) {
        arrow.setAlpha(visible ? 1.0f : 0.0f);
        if (visible) {
            arrow.startPulse();
        } else {
            arrow.stopPulse();
        }
    }

    public void setHelperVisibility(final boolean visible) {
        helper.setVisibility(visible ? VISIBLE : GONE);
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
}