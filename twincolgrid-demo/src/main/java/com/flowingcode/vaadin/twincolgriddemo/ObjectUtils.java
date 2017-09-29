package com.flowingcode.vaadin.twincolgriddemo;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

public class ObjectUtils {

    @SafeVarargs
    public static <T> boolean equals(final T me, final T other, final Function<T, ?>... properties) {
        if (me == null || other == null) {
            return false;
        }
        if (me == other) {
            return true;
        }
        if (!me.getClass().isInstance(other)) {
            return false;
        }
        final boolean equ = Stream.of(properties).allMatch(property -> Objects.equals(property.apply(me), property.apply(other)));
        return equ;
    }

}
