/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * copyright 2023, Purcel Iulian
 */

package com.iulu.dotimer.timer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationActionButtonReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        TimerPref.getActionClass(context)?.callObj(com.iulu.dotimer.MEMBER_NOTIFICATION_BUTTON_PRESSED, context)
    }
}