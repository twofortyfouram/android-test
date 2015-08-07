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


import android.Manifest;
import android.content.pm.PackageManager;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

public final class FeatureContextWrapperTest extends AndroidTestCase {

    @SmallTest
    public void testBreakOut() {
        final FeatureContextWrapper fContext = new FeatureContextWrapper(getContext(), null, null);

        assertSame(fContext, fContext.getApplicationContext());
    }

    @SmallTest
    public void testAllowedFeature_null() {
        final FeatureContextWrapper fContext = new FeatureContextWrapper(getContext(), null, null);

        assertFalse(fContext.getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_LIVE_WALLPAPER));
    }

    @SmallTest
    public void testAllowedFeature_empty() {
        final FeatureContextWrapper fContext = new FeatureContextWrapper(getContext(), null,
                new String[0]);

        assertFalse(fContext.getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_LIVE_WALLPAPER));
    }

    @SmallTest
    public void testAllowedFeature() {
        final FeatureContextWrapper fContext = new FeatureContextWrapper(getContext(),
                null, new String[]{PackageManager.FEATURE_LIVE_WALLPAPER});

        assertTrue(fContext.getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_LIVE_WALLPAPER));
        assertFalse(fContext.getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_LOCATION));
    }

    @SmallTest
    public void testAllowedPermission_null() {
        final FeatureContextWrapper fContext = new FeatureContextWrapper(getContext(), null, null);

        assertEquals(PackageManager.PERMISSION_DENIED, fContext.getPackageManager()
                .checkPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                        fContext.getPackageName()));
        assertEquals(PackageManager.PERMISSION_DENIED,
                fContext.checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    @SmallTest
    public void testAllowedPermission_empty() {
        final FeatureContextWrapper fContext = new FeatureContextWrapper(getContext(),
                new String[0], null);

        assertEquals(PackageManager.PERMISSION_DENIED, fContext.getPackageManager()
                .checkPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                        fContext.getPackageName()));
        assertEquals(PackageManager.PERMISSION_DENIED,
                fContext.checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    @SmallTest
    public void testAllowedPermission() {
        final FeatureContextWrapper fContext = new FeatureContextWrapper(getContext(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, null);

        assertEquals(PackageManager.PERMISSION_GRANTED, fContext.getPackageManager()
                .checkPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                        fContext.getPackageName()));
        assertEquals(PackageManager.PERMISSION_GRANTED,
                fContext.checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION));

        assertEquals(PackageManager.PERMISSION_DENIED, fContext.getPackageManager()
                .checkPermission(Manifest.permission.ACCESS_NETWORK_STATE,
                        fContext.getPackageName()));
        assertEquals(PackageManager.PERMISSION_DENIED,
                fContext.checkCallingOrSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE));
    }

}
