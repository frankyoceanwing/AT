/*
 * Copyright (C) 2012 The Android Open Source Project
 *
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
 */

package com.oceanwing.at;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

public class BroadcastNotifier {

    public static final String BROADCAST_ACTION = "com.oceanwing.at.action.BROADCAST";
    public static final String EXTENDED_DATA_STATUS = "com.oceanwing.at.data.STATUS";
    public static final String EXTENDED_DATA_LOG = "com.oceanwing.at.data.LOG";
    public static final String EXTENDED_DATA_STEP = "com.oceanwing.at.data.STEP";


    public static final int STATE_ACTION_ERROR = -1;
    public static final int STATE_ACTION_STARTED = 0;
    public static final int STATE_ACTION_CONNECTING = 1;
    public static final int STATE_ACTION_PARSING = 2;
    public static final int STATE_ACTION_MOCKING = 3;
    public static final int STATE_ACTION_COMPLETE = 4;
    public static final int STATE_ACTION_PAUSING = 5;
    public static final int STATE_ACTION_NO_GPS = 6;

    private LocalBroadcastManager mBroadcaster;

    public BroadcastNotifier(Context context) {
        mBroadcaster = LocalBroadcastManager.getInstance(context);
    }

    public void broadcast(int status, int step, String log) {
        Intent localIntent = new Intent();
        // The Intent contains the custom broadcast action for this app
        localIntent.setAction(BROADCAST_ACTION);
        localIntent.putExtra(EXTENDED_DATA_STATUS, status);
        localIntent.putExtra(EXTENDED_DATA_STEP, step);
        localIntent.putExtra(EXTENDED_DATA_LOG, log);
        localIntent.addCategory(Intent.CATEGORY_DEFAULT);
        // Broadcasts the Intent
        mBroadcaster.sendBroadcast(localIntent);
    }

    public void broadcast(int status, String log) {
        broadcast(status, -1, log);
    }
}
