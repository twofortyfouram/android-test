/*
 * android-test-lib https://github.com/twofortyfouram/android-test
 * Copyright 2014 two forty four a.m. LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.twofortyfouram.test.assertion;

import com.twofortyfouram.annotation.NonNull;

import junit.framework.Assert;

import net.jcip.annotations.ThreadSafe;

import android.test.MoreAsserts;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Additional asserts going beyond what is provided by {@link Assert} and {@link MoreAsserts}.
 * <p>
 * This class is only expected to be used in unit tests.
 */
@ThreadSafe
public final class MoarAsserts {

    /**
     * @param cls Class to assert has no public constructors and whose default constructor throws
     *            an
     *            {@code UnsupportedOperationException}.
     */
    public static void assertNoninstantiable(@NonNull final Class<?> cls) {
        if (null == cls) {
            throw new AssertionError();
        }

        Assert.assertEquals(
                cls.getName() + " has public constructors", 0,
                cls.getConstructors().length); //$NON-NLS-1$

        try {
            final Constructor<?> constructor = cls.getDeclaredConstructor();

            constructor.setAccessible(true);

            try {
                Assert.assertNotNull(constructor.newInstance());
                throw new AssertionError(
                        "Able to access default constructor via reflection"); //$NON-NLS-1$
            } catch (final IllegalArgumentException e) {
                throw new RuntimeException(e);
            } catch (final InstantiationException e) {
                throw new RuntimeException(e);
            } catch (final IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (final InvocationTargetException e) {
                if (e.getCause() instanceof UnsupportedOperationException) {
                    /*
                     * Expected exception, because the private constructor should throw
                     * UnsupportedOperationException.
                     */
                } else {
                    throw new RuntimeException(e);
                }
            }
        } catch (final NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Private constructor prevents instantiation.
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private MoarAsserts() {
        throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
    }
}
