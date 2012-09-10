package com.omegapoint.core.inject;

public interface BindProvider {
    <T> BindingBuilder<T> bind(Class<T> clazz);
}
