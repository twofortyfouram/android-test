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
import com.twofortyfouram.annotation.Nullable;

import net.jcip.annotations.Immutable;
import net.jcip.annotations.NotThreadSafe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Test context to capture all Intents broadcasts. Intents are not broadcast to the
 * rest of the system, although ordered broadcasts are delivered directly the result receiver if
 * provided. Intents
 * broadcast through this class are stored and can be retrieved via {@link
 * #getAndClearSentIntents()}.
 */
@NotThreadSafe
public final class ReceiverContextWrapper extends ContextWrapper {

    @NonNull
    private final Collection<SentIntent> mIntents = new LinkedList<SentIntent>();

    public ReceiverContextWrapper(@NonNull final Context targetContext) {
        super(targetContext);
    }

    /**
     * @return This object, preventing clients from breaking out of the wrapped context.
     */
    @Override
    public Context getApplicationContext() {
        return this;
    }

    @Override
    public void sendBroadcast(final Intent intent) {
        sendBroadcast(intent, null);
    }

    @Override
    public void sendBroadcast(final Intent intent, final String receiverPermission) {
        mIntents.add(new SentIntent(intent, receiverPermission, false, false));
    }

    @Override
    public void sendStickyBroadcast(final Intent intent) {
        mIntents.add(new SentIntent(intent, null, true, false));
    }

    @Override
    public void sendStickyOrderedBroadcast(final Intent intent,
            final BroadcastReceiver resultReceiver, final Handler scheduler, final int initialCode,
            final String initialData, final Bundle initialExtras) {
        mIntents.add(new SentIntent(intent, null, true, true));

        scheduler.post(new Runnable() {
            private final Intent mIntent = intent;

            @Override
            public void run() {
                resultReceiver.onReceive(ReceiverContextWrapper.this, mIntent);
            }
        });
    }

    @Override
    public void sendOrderedBroadcast(final Intent intent, final String receiverPermission) {
        mIntents.add(new SentIntent(intent, receiverPermission, false, true));
    }

    @Override
    public void sendOrderedBroadcast(final Intent intent, String receiverPermission,
            final BroadcastReceiver resultReceiver, Handler scheduler, int initialCode,
            String initialData, Bundle initialExtras) {
        mIntents.add(new SentIntent(intent, null, false, true));

        scheduler.post(new Runnable() {

            private final Intent mIntent = intent;

            @Override
            public void run() {
                resultReceiver.onReceive(ReceiverContextWrapper.this, mIntent);
            }
        });
    }

    @Override
    public void sendBroadcastAsUser(Intent intent, UserHandle user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendBroadcastAsUser(Intent intent, UserHandle user, String receiverPermission) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendStickyBroadcastAsUser(Intent intent, UserHandle user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendStickyOrderedBroadcastAsUser(Intent intent, UserHandle user,
            BroadcastReceiver resultReceiver, Handler scheduler, int initialCode,
            String initialData, Bundle initialExtras) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendOrderedBroadcastAsUser(Intent intent, UserHandle user,
            String receiverPermission, BroadcastReceiver resultReceiver, Handler scheduler,
            int initialCode, String initialData, Bundle initialExtras) {
        throw new UnsupportedOperationException();
    }

    @NonNull
    public Collection<SentIntent> getAndClearSentIntents() {
        try {
            return new LinkedList<SentIntent>(mIntents);
        } finally {
            mIntents.clear();
        }
    }

    /**
     * Represents an Intent that was sent through
     */
    @Immutable
    public final class SentIntent {

        @NonNull
        private final Intent mIntent;

        @Nullable
        private final String mPermission;

        private final boolean mIsSticky;

        private final boolean mIsOrdered;

        private SentIntent(@NonNull final Intent intent, @Nullable final String permission,
                final boolean isSticky, final boolean isOrdered) {
            if (null == intent) {
                throw new AssertionError();
            }

            mIntent = new Intent(intent);
            mPermission = permission;
            mIsSticky = isSticky;
            mIsOrdered = isOrdered;
        }

        /**
         * @return The Intent that was broadcast through {@link ReceiverContextWrapper}.
         * Note that this method always returns a new copy, to prevent exposing the internals of
         * this class.
         */
        @NonNull
        public Intent getIntent() {
            return new Intent(mIntent);
        }

        /**
         * @return The permission enforced on the Intent or null if there was no permission.
         */
        @Nullable
        public String getPermission() {
            return mPermission;
        }

        /**
         * @return True if the Intent is sticky.
         */
        public boolean getIsSticky() {
            return mIsSticky;
        }

        /**
         * @return If the Intent is ordered.
         */
        public boolean getIsOrdered() {
            return mIsOrdered;
        }
    }
}