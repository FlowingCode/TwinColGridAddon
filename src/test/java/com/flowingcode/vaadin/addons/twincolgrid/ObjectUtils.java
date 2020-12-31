package com.flowingcode.vaadin.addons.twincolgrid;

/*-
 * #%L
 * TwinColGrid add-on
 * %%
 * Copyright (C) 2017 - 2020 Flowing Code
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
