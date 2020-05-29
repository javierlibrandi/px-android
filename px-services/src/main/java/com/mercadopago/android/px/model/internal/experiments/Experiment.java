package com.mercadopago.android.px.model.internal.experiments;

import com.mercadopago.android.px.model.internal.Variant;
import java.io.Serializable;

public final class Experiment implements Serializable {

    private int id;
    private String name;
    private Variant variant;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Variant getVariant() {
        return variant;
    }
}
