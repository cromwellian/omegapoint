package com.omegapoint.core.inject;

import com.google.inject.binder.AnnotatedBindingBuilder;

import java.lang.annotation.Annotation;

public class GuiceBindingBuilder<T> implements BindingBuilder<T> {
    private final AnnotatedBindingBuilder<T> bindingBuilder;

    public GuiceBindingBuilder(AnnotatedBindingBuilder<T> bindingBuilder) {
        this.bindingBuilder = bindingBuilder;
    }

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
