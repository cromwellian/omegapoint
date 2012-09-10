package com.omegapoint.core.inject;

import java.lang.annotation.Annotation;

public interface BindingBuilder<T> {
    BindingBuilder<T> in(Class<? extends Annotation> aClass);
    BindingBuilder<T> to(Class<? extends T> aClass);
}
