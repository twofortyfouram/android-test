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

import com.twofortyfouram.annotation.NonNull;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.test.AndroidTestCase;
import android.test.MoreAsserts;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.text.format.DateUtils;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public final class ReceiverContextWrapperTest extends AndroidTestCase {

    // TODO: These tests are not particularly DRY

    @NonNull
    public void testBreakOut() {
        final ReceiverContextWrapper fContext = new ReceiverContextWrapper(getContext());

        assertSame(fContext, fContext.getApplicationContext());
    }

    @NonNull
    public void testGetAndClearEmpty() {
        final ReceiverContextWrapper fContext = new ReceiverContextWrapper(getContext());

        final Collection<ReceiverContextWrapper.SentIntent> intents = fContext
                .getAndClearSentIntents();
        assertNotNull(intents);
        MoreAsserts.assertEmpty(intents);
    }

    @MediumTest
    public void testSendBroadcast() {
        final ReceiverContextWrapper fContext = new ReceiverContextWrapper(getContext());

        /*
         * Verifies the Intent is not broadcast to the rest of the system and the Intent is captured.
         */

        final HandlerThread handlerThread = new HandlerThread(getName(),
                android.os.Process.THREAD_PRIORITY_DEFAULT);
        handlerThread.start();
        try {

            final CountDownLatch latch = new CountDownLatch(1);
            final BroadcastReceiver receiver = new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {
                    latch.countDown();
                }
            };

            final String intentAction = "com.twofortyfouram.test.intent.action." + getName();
            final IntentFilter filter = new IntentFilter(intentAction);
            getContext().registerReceiver(receiver, filter);

            final Intent intentToSend = new Intent(intentAction);
            fContext.sendBroadcast(intentToSend);

            try {
                assertFalse(latch.await(DateUtils.SECOND_IN_MILLIS, TimeUnit.MILLISECONDS));
            } catch (final InterruptedException e) {
                fail(e.getMessage());
            } finally {
                getContext().unregisterReceiver(receiver);
            }

            final Collection<ReceiverContextWrapper.SentIntent> intents = fContext
                    .getAndClearSentIntents();
            assertEquals(1, intents.size());
            for (final ReceiverContextWrapper.SentIntent i : intents) {
                assertTrue(intentToSend.filterEquals(i.getIntent()));
                assertNotSame(intentToSend, i.getIntent());

                assertNull(i.getPermission());
                assertFalse(i.getIsSticky());
                assertFalse(i.getIsOrdered());
            }

        } finally {
            handlerThread.getLooper().quit();
        }
    }

    @MediumTest
    public void testSendBroadcast_permission() {
        final ReceiverContextWrapper fContext = new ReceiverContextWrapper(getContext());

        /*
         * Verifies the Intent is not broadcast to the rest of the system and the Intent is captured.
         */

        final HandlerThread handlerThread = new HandlerThread(getName(),
                android.os.Process.THREAD_PRIORITY_DEFAULT);
        handlerThread.start();
        try {

            final CountDownLatch latch = new CountDownLatch(1);
            final BroadcastReceiver receiver = new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {
                    latch.countDown();
                }
            };

            final String intentAction = "com.twofortyfouram.test.intent.action." + getName();
            final String permissionString = "com.twofortyfouram.test.permission." + getName();
            final IntentFilter filter = new IntentFilter(intentAction);
            getContext().registerReceiver(receiver, filter, permissionString, null);

            final Intent intentToSend = new Intent(intentAction);
            fContext.sendBroadcast(intentToSend, permissionString);

            try {
                assertFalse(latch.await(DateUtils.SECOND_IN_MILLIS, TimeUnit.MILLISECONDS));
            } catch (final InterruptedException e) {
                fail(e.getMessage());
            } finally {
                getContext().unregisterReceiver(receiver);
            }

            final Collection<ReceiverContextWrapper.SentIntent> intents = fContext
                    .getAndClearSentIntents();
            assertEquals(1, intents.size());
            for (final ReceiverContextWrapper.SentIntent i : intents) {
                assertTrue(intentToSend.filterEquals(i.getIntent()));
                assertNotSame(intentToSend, i.getIntent());

                assertEquals(permissionString, i.getPermission());
                assertFalse(i.getIsSticky());
                assertFalse(i.getIsOrdered());
            }

        } finally {
            handlerThread.getLooper().quit();
        }
    }

    @MediumTest
    public void testSendStickyBroadcast() {
        final ReceiverContextWrapper fContext = new ReceiverContextWrapper(getContext());

        /*
         * Verifies the Intent is not broadcast to the rest of the system and the Intent is captured.
         */

        final HandlerThread handlerThread = new HandlerThread(getName(),
                android.os.Process.THREAD_PRIORITY_DEFAULT);
        handlerThread.start();
        try {

            final CountDownLatch latch = new CountDownLatch(1);
            final BroadcastReceiver receiver = new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {
                    latch.countDown();
                }
            };

            final String intentAction = "com.twofortyfouram.test.intent.action." + getName();
            final IntentFilter filter = new IntentFilter(intentAction);
            getContext().registerReceiver(receiver, filter);

            final Intent intentToSend = new Intent(intentAction);
            fContext.sendStickyBroadcast(intentToSend);

            try {
                assertFalse(latch.await(DateUtils.SECOND_IN_MILLIS, TimeUnit.MILLISECONDS));
            } catch (final InterruptedException e) {
                fail(e.getMessage());
            } finally {
                getContext().unregisterReceiver(receiver);
            }

            final Collection<ReceiverContextWrapper.SentIntent> intents = fContext
                    .getAndClearSentIntents();
            assertEquals(1, intents.size());
            for (final ReceiverContextWrapper.SentIntent i : intents) {
                assertTrue(intentToSend.filterEquals(i.getIntent()));
                assertNotSame(intentToSend, i.getIntent());

                assertNull(i.getPermission());
                assertTrue(i.getIsSticky());
                assertFalse(i.getIsOrdered());
            }

        } finally {
            handlerThread.getLooper().quit();
        }
    }

    @MediumTest
    public void testSendOrderedBroadcast() {
        final ReceiverContextWrapper fContext = new ReceiverContextWrapper(getContext());

        /*
         * Verifies the Intent is not broadcast to the rest of the system and the Intent is captured.
         */

        final HandlerThread handlerThread = new HandlerThread(getName(),
                android.os.Process.THREAD_PRIORITY_DEFAULT);
        handlerThread.start();
        try {

            final CountDownLatch latch = new CountDownLatch(1);
            final BroadcastReceiver receiver = new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {
                    latch.countDown();
                }
            };

            final String intentAction = "com.twofortyfouram.test.intent.action." + getName();
            final IntentFilter filter = new IntentFilter(intentAction);
            getContext().registerReceiver(receiver, filter);

            final Intent intentToSend = new Intent(intentAction);
            fContext.sendOrderedBroadcast(intentToSend, null);

            try {
                assertFalse(latch.await(DateUtils.SECOND_IN_MILLIS, TimeUnit.MILLISECONDS));
            } catch (final InterruptedException e) {
                fail(e.getMessage());
            } finally {
                getContext().unregisterReceiver(receiver);
            }

            final Collection<ReceiverContextWrapper.SentIntent> intents = fContext
                    .getAndClearSentIntents();
            assertEquals(1, intents.size());
            for (final ReceiverContextWrapper.SentIntent i : intents) {
                assertTrue(intentToSend.filterEquals(i.getIntent()));
                assertNotSame(intentToSend, i.getIntent());

                assertNull(i.getPermission());
                assertFalse(i.getIsSticky());
                assertTrue(i.getIsOrdered());
            }

        } finally {
            handlerThread.getLooper().quit();
        }
    }

    @MediumTest
    public void testSendOrderedBroadcast_with_result_receiver() {
        final ReceiverContextWrapper fContext = new ReceiverContextWrapper(getContext());

        /*
         * Verifies the Intent is not broadcast to the rest of the system and the Intent is captured.
         */

        final HandlerThread handlerThread = new HandlerThread(getName(),
                android.os.Process.THREAD_PRIORITY_DEFAULT);
        handlerThread.start();
        try {

            final CountDownLatch latch = new CountDownLatch(1);
            final BroadcastReceiver receiver = new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {
                    latch.countDown();
                }
            };

            final CountDownLatch resultLatch = new CountDownLatch(1);
            final BroadcastReceiver resultReceiver = new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {
                    assertSame(handlerThread.getLooper(), Looper.myLooper());
                    resultLatch.countDown();
                }
            };

            final String intentAction = "com.twofortyfouram.test.intent.action." + getName();
            final IntentFilter filter = new IntentFilter(intentAction);
            getContext().registerReceiver(receiver, filter);

            final Intent intentToSend = new Intent(intentAction);
            fContext.sendOrderedBroadcast(intentToSend, null, resultReceiver,
                    new Handler(handlerThread.getLooper()), 0, null, null);

            try {
                assertFalse(latch.await(DateUtils.SECOND_IN_MILLIS, TimeUnit.MILLISECONDS));
            } catch (final InterruptedException e) {
                fail(e.getMessage());
            } finally {
                getContext().unregisterReceiver(receiver);
            }

            try {
                assertTrue(resultLatch.await(DateUtils.SECOND_IN_MILLIS, TimeUnit.MILLISECONDS));
            } catch (final InterruptedException e) {
                fail(e.getMessage());
            }

            final Collection<ReceiverContextWrapper.SentIntent> intents = fContext
                    .getAndClearSentIntents();
            assertEquals(1, intents.size());
            for (final ReceiverContextWrapper.SentIntent i : intents) {
                assertTrue(intentToSend.filterEquals(i.getIntent()));
                assertNotSame(intentToSend, i.getIntent());

                assertNull(i.getPermission());
                assertFalse(i.getIsSticky());
                assertTrue(i.getIsOrdered());
            }

        } finally {
            handlerThread.getLooper().quit();
        }
    }

    @MediumTest
    public void testSendStickyOrderedBroadcast() {
        final ReceiverContextWrapper fContext = new ReceiverContextWrapper(getContext());

        /*
         * Verifies the Intent is not broadcast to the rest of the system and the Intent is captured.
         */

        final HandlerThread handlerThread = new HandlerThread(getName(),
                android.os.Process.THREAD_PRIORITY_DEFAULT);
        handlerThread.start();
        try {

            final CountDownLatch latch = new CountDownLatch(1);
            final BroadcastReceiver receiver = new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {
                    latch.countDown();
                }
            };

            final CountDownLatch resultLatch = new CountDownLatch(1);
            final BroadcastReceiver resultReceiver = new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {
                    assertSame(handlerThread.getLooper(), Looper.myLooper());
                    resultLatch.countDown();
                }
            };

            final String intentAction = "com.twofortyfouram.test.intent.action." + getName();
            final IntentFilter filter = new IntentFilter(intentAction);
            getContext().registerReceiver(receiver, filter);

            final Intent intentToSend = new Intent(intentAction);
            fContext.sendStickyOrderedBroadcast(intentToSend, resultReceiver,
                    new Handler(handlerThread.getLooper()), 0, null, null);

            try {
                assertFalse(latch.await(DateUtils.SECOND_IN_MILLIS, TimeUnit.MILLISECONDS));
            } catch (final InterruptedException e) {
                fail(e.getMessage());
            } finally {
                getContext().unregisterReceiver(receiver);
            }

            try {
                assertTrue(resultLatch.await(DateUtils.SECOND_IN_MILLIS, TimeUnit.MILLISECONDS));
            } catch (final InterruptedException e) {
                fail(e.getMessage());
            }

            final Collection<ReceiverContextWrapper.SentIntent> intents = fContext
                    .getAndClearSentIntents();
            assertEquals(1, intents.size());
            for (final ReceiverContextWrapper.SentIntent i : intents) {
                assertTrue(intentToSend.filterEquals(i.getIntent()));
                assertNotSame(intentToSend, i.getIntent());

                assertNull(i.getPermission());
                assertTrue(i.getIsSticky());
                assertTrue(i.getIsOrdered());
            }

        } finally {
            handlerThread.getLooper().quit();
        }
    }

    @SmallTest
    public void testSendBroadcastAsUser() {
        final ReceiverContextWrapper fContext = new ReceiverContextWrapper(getContext());

        try {
            fContext.sendBroadcastAsUser(new Intent(), null);
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @SmallTest
    public void testSendBroadcastAsUser_permission() {
        final ReceiverContextWrapper fContext = new ReceiverContextWrapper(getContext());

        try {
            fContext.sendBroadcastAsUser(new Intent(), null, null);
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }


    @SmallTest
    public void testSendStickyBroadcastAsUser() {
        final ReceiverContextWrapper fContext = new ReceiverContextWrapper(getContext());

        try {
            fContext.sendStickyBroadcastAsUser(new Intent(), null);
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @SmallTest
    public void testSendStickyOrderedBroadcastAsUser() {
        final ReceiverContextWrapper fContext = new ReceiverContextWrapper(getContext());

        try {
            fContext.sendStickyOrderedBroadcastAsUser(new Intent(), null, null, null, 0, null,
                    null);
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @SmallTest
    public void testSendOrderedBroadcastAsUser() {
        final ReceiverContextWrapper fContext = new ReceiverContextWrapper(getContext());

        try {
            fContext.sendOrderedBroadcastAsUser(new Intent(), null, null, null, null, 0, null,
                    null);
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

}
