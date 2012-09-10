package com.omegapoint.core.inject;


import com.google.gwt.inject.client.binder.GinAnnotatedBindingBuilder;
import com.google.gwt.inject.client.binder.GinBinder;

public class GinBindProvider implements BindProvider {
    private GinBinder binder;

    @Override
    public <T> GinBindingBuilder<T> bind(Class<T> clazz) {
        GinAnnotatedBindingBuilder<T> bindingBuilder = binder.bind(clazz);

        return new GinBindingBuilder(bindingBuilder);
    }

    public void setBinder(GinBinder binder) {
        this.binder = binder;
    }
}
