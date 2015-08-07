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

package com.twofortyfouram.test.ui.activity;

import com.twofortyfouram.test.assertion.MoarAsserts;

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.suitebuilder.annotation.SmallTest;


public final class ActivityTestHelperTest
        extends ActivityInstrumentationTestCase2<FragmentTestActivity> {

    public ActivityTestHelperTest(
            Class<FragmentTestActivity> activityClass) {
        super(activityClass);
    }

    @SmallTest
    @UiThreadTest
    public void testResultCanceled() {
        final FragmentTestActivity activity = getActivity();

        activity.setResult(Activity.RESULT_CANCELED);

        activity.finish();

        assertEquals(Activity.RESULT_CANCELED, ActivityTestUtil.getActivityResultCode(activity));
    }

    @SmallTest
    @UiThreadTest
    public void testResultOk() {
        final FragmentTestActivity activity = getActivity();

        activity.setResult(Activity.RESULT_OK);

        activity.finish();

        assertEquals(Activity.RESULT_OK, ActivityTestUtil.getActivityResultCode(activity));
    }

    @SmallTest
    @UiThreadTest
    public void testResultIntent_null() {
        final FragmentTestActivity activity = getActivity();

        activity.setResult(Activity.RESULT_OK, null);

        activity.finish();

        assertNull(ActivityTestUtil.getActivityResultData(activity));
    }

    @SmallTest
    @UiThreadTest
    public void testResultIntent_non_null() {
        final FragmentTestActivity activity = getActivity();

        final Intent result = new Intent();
        activity.setResult(Activity.RESULT_OK, result);

        activity.finish();

        assertSame(result, ActivityTestUtil.getActivityResultData(activity));
    }

    @SmallTest
    public static void testNonInstantiable() {
        MoarAsserts.assertNoninstantiable(ActivityTestUtil.class);
    }
}
