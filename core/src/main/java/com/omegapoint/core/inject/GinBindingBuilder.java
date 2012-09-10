package com.omegapoint.core.inject;

import com.google.gwt.inject.client.binder.GinAnnotatedBindingBuilder;

import java.lang.annotation.Annotation;

public class GinBindingBuilder<T> implements BindingBuilder<T> {
    private final GinAnnotatedBindingBuilder<T> bindingBuilder;

    public GinBindingBuilder(GinAnnotatedBindingBuilder<T> bindingBuilder) {
        this.bindingBuilder = bindingBuilder;
    }

    @Override
    public BindingBuilder<T> to(Class<? extends T> aClass) {
        bindingBuilder.to(aClass);
        return this;
    }

    @Override
    public BindingBuilder<T> in(Class<? extends Annotation> aClass) {
        bindingBuilder.in(aClass);
        return this;
    }
}
