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

package com.twofortyfouram.test.context;


import net.jcip.annotations.Immutable;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.test.mock.MockPackageManager;

import java.util.Arrays;

/**
 * Context for faking environment capabilities and permissions. This allows dynamic feature
 * switching to be exercised in a test environment.
 */
@Immutable
public final class FeatureContextWrapper extends ContextWrapper {

    /**
     * Sorted set of permissions this application is allowed to access.
     */
    @Nullable
    private final String[] mAllowedPermissions;

    /**
     * Sorted set of system features that are available.
     */
    @Nullable
    private final String[] mAvailableFeatures;

    /**
     * Mock package manager for faking permission results.
     */
    @NonNull
    private final PackageManager mMockPackageManager;

    /**
     * @param baseContext        Base context to wrap.
     * @param allowedPermissions Set of allowed permissions.
     * @param availableFeatures  Set of available features.
     */
    public FeatureContextWrapper(final Context baseContext,
            @Nullable final String[] allowedPermissions,
            @Nullable final String[] availableFeatures) {
        super(baseContext);
        if (null != allowedPermissions) {
            mAllowedPermissions = copyArray(allowedPermissions);
            Arrays.sort(mAllowedPermissions);
        } else {
            mAllowedPermissions = null;
        }

        if (null != availableFeatures) {
            mAvailableFeatures = copyArray(availableFeatures);
            Arrays.sort(mAvailableFeatures);
        } else {
            mAvailableFeatures = null;
        }

        mMockPackageManager = new MockPackageManager() {
            @Override
            public int checkPermission(final String permName, final String pkgName) {
                return checkPermissionInternal(permName);
            }

            @Override
            public boolean hasSystemFeature(final String featureName) {
                return checkFeatureInternal(featureName);
            }
        };
    }

    @Override
    public PackageManager getPackageManager() {
        return mMockPackageManager;
    }

    @Override
    public int checkCallingOrSelfPermission(final String permission) {
        return checkPermissionInternal(permission);
    }

    private int checkPermissionInternal(final String permission) {
        boolean isPermissionGranted;
        if (null == mAllowedPermissions) {
            isPermissionGranted = false;
        } else {
            isPermissionGranted = 0 <= Arrays.binarySearch(mAllowedPermissions, permission);
        }

        return isPermissionGranted ? PackageManager.PERMISSION_GRANTED
                : PackageManager.PERMISSION_DENIED;
    }

    private boolean checkFeatureInternal(@NonNull final String feature) {
        boolean isFeatureAvailable;
        if (null == mAvailableFeatures) {
            isFeatureAvailable = false;
        } else {
            if (0 <= Arrays.binarySearch(mAvailableFeatures, feature)) {
                isFeatureAvailable = true;
            } else {
                isFeatureAvailable = false;
            }
        }

        return isFeatureAvailable;
    }

    @Override
    public Context getApplicationContext() {
        return this;
    }

    @NonNull
    private static String[] copyArray(@NonNull final String[] toCopy) {
        final String[] dest = new String[toCopy.length];

        System.arraycopy(toCopy, 0, dest, 0, toCopy.length);

        return dest;
    }
}
