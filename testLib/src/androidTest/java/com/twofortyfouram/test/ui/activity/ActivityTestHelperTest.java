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

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.suitebuilder.annotation.SmallTest;

import com.twofortyfouram.test.ActivityImpl;
import com.twofortyfouram.test.assertion.MoarAsserts;

// TODO: Convert this to Espresso style
public final class ActivityTestHelperTest
        extends ActivityInstrumentationTestCase2<ActivityImpl> {

    public ActivityTestHelperTest() {
        super(ActivityImpl.class);
    }

    public ActivityTestHelperTest(@NonNull final Class<ActivityImpl> activityClass) {
        super(activityClass);
    }

    @SmallTest
    @UiThreadTest
    public void testResultCanceled() {
        final ActivityImpl activity = getActivity();

        activity.setResult(Activity.RESULT_CANCELED);

        activity.finish();

        assertEquals(Activity.RESULT_CANCELED,
                ActivityTestUtil.getActivityResultCodeSync(getInstrumentation(), activity));
    }

    @SmallTest
    @UiThreadTest
    public void testResultOk_ui_thread() {
        final ActivityImpl activity = getActivity();

        activity.setResult(Activity.RESULT_OK);

        activity.finish();

        assertEquals(Activity.RESULT_OK,
                ActivityTestUtil.getActivityResultCodeSync(getInstrumentation(), activity));
    }

    @SmallTest
    public void testResultOk_non_ui_thread() throws Throwable {
        final ActivityImpl activity = getActivity();

        ActivityTestUtil.autoSyncRunnable(getInstrumentation(), new Runnable() {
            @Override
            public void run() {
                activity.setResult(Activity.RESULT_OK);

                activity.finish();
            }
        });

        assertEquals(Activity.RESULT_OK,
                ActivityTestUtil.getActivityResultCodeSync(getInstrumentation(), activity));
    }

    @SmallTest
    @UiThreadTest
    public void testResultIntent_null() {
        final ActivityImpl activity = getActivity();

        activity.setResult(Activity.RESULT_OK, null);

        activity.finish();

        assertNull(ActivityTestUtil.getActivityResultDataSync(getInstrumentation(), activity));
    }

    @SmallTest
    @UiThreadTest
    public void testResultIntent_non_null() {
        final ActivityImpl activity = getActivity();

        final Intent result = new Intent();
        activity.setResult(Activity.RESULT_OK, result);

        activity.finish();

        assertSame(result, ActivityTestUtil.getActivityResultDataSync(getInstrumentation(),
                activity));
    }

    @SmallTest
    public static void testNonInstantiable() {
        MoarAsserts.assertNoninstantiable(ActivityTestUtil.class);
    }
}
