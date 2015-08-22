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
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.test.filters.SdkSuppress;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

public final class FeatureContextWrapperTest extends AndroidTestCase {

    @SmallTest
    public void testBreakOut() {
        final FeatureContextWrapper fContext = new FeatureContextWrapper(getContext(), null, null,
                null);

        assertSame(fContext, fContext.getApplicationContext());
    }

    @SmallTest
    public void testAllowedFeature_null() {
        final FeatureContextWrapper fContext = new FeatureContextWrapper(getContext(), null, null,
                null);

        assertFalse(fContext.getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_LIVE_WALLPAPER));
    }

    @SmallTest
    public void testAllowedFeature_empty() {
        final FeatureContextWrapper fContext = new FeatureContextWrapper(getContext(), null, null,
                new String[0]);

        assertFalse(fContext.getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_LIVE_WALLPAPER));
    }

    @SmallTest
    public void testAllowedFeature() {
        final FeatureContextWrapper fContext = new FeatureContextWrapper(getContext(),
                null, null, new String[]{PackageManager.FEATURE_LIVE_WALLPAPER});

        assertTrue(fContext.getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_LIVE_WALLPAPER));
        assertFalse(fContext.getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_LOCATION));
    }

    @SmallTest
    public void testAllowedPermission_null() {
        final FeatureContextWrapper fContext = new FeatureContextWrapper(getContext(), null, null,
                null);

        assertEquals(PackageManager.PERMISSION_DENIED, fContext.getPackageManager()
                .checkPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                        fContext.getPackageName()));
        assertEquals(PackageManager.PERMISSION_DENIED,
                fContext.checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    @SmallTest
    public void testAllowedPermission_empty() {
        final FeatureContextWrapper fContext = new FeatureContextWrapper(getContext(),
                null, new String[0], null);

        assertEquals(PackageManager.PERMISSION_DENIED, fContext.getPackageManager()
                .checkPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                        fContext.getPackageName()));
        assertEquals(PackageManager.PERMISSION_DENIED,
                fContext.checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    @SmallTest
    public void testAllowedPermission() {
        final FeatureContextWrapper fContext = new FeatureContextWrapper(getContext(),
                null, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, null);

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

    @SmallTest
    @SdkSuppress(minSdkVersion = Build.VERSION_CODES.M)
    public void testAllowedPermission_marshmallow() {
        final FeatureContextWrapper fContext = new FeatureContextWrapper(getContext(),
                null, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, null);

        assertEquals(PackageManager.PERMISSION_GRANTED,
                fContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION));

        assertEquals(PackageManager.PERMISSION_DENIED,
                fContext.checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE));
    }

    @SmallTest
    public void testRequestedPermission() throws PackageManager.NameNotFoundException {
        final FeatureContextWrapper fContext = new FeatureContextWrapper(getContext(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, null, null);

        final PackageInfo info = fContext.getPackageManager()
                .getPackageInfo(fContext.getPackageName(), PackageManager.GET_PERMISSIONS);

        assertNotNull(info.requestedPermissions);
        assertEquals(1, info.requestedPermissions.length);
        assertEquals(Manifest.permission.ACCESS_FINE_LOCATION, info.requestedPermissions[0]);
    }


    @SmallTest
    public void testRequestedPermission_null() throws PackageManager.NameNotFoundException {
        final FeatureContextWrapper fContext = new FeatureContextWrapper(getContext(),
                null, null, null);

        final PackageInfo info = fContext.getPackageManager()
                .getPackageInfo(fContext.getPackageName(), PackageManager.GET_PERMISSIONS);

        assertNull(info.requestedPermissions);
    }


    @SmallTest
    public void testRequestedPermission_empty() throws PackageManager.NameNotFoundException {
        final FeatureContextWrapper fContext = new FeatureContextWrapper(getContext(),
                new String[]{}, null, null);

        final PackageInfo info = fContext.getPackageManager()
                .getPackageInfo(fContext.getPackageName(), PackageManager.GET_PERMISSIONS);

        assertNotNull(info.requestedPermissions);
        assertEquals(0, info.requestedPermissions.length);
    }

}
