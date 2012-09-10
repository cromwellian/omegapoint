package com.omegapoint.core.inject;


import com.google.inject.Binder;
import com.google.inject.binder.AnnotatedBindingBuilder;

public class GuiceBindProvider implements BindProvider {
    private Binder binder;

    @Override
    public <T> BindingBuilder<T> bind(Class<T> clazz) {
        AnnotatedBindingBuilder<T> bindingBuilder = binder.bind(clazz);

        return new GuiceBindingBuilder(bindingBuilder);
    }

    public void setBinder(Binder binder) {
        this.binder = binder;
    }
}
