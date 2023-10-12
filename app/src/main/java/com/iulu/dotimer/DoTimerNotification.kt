/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * copyright 2023, Purcel Iulian
 */

package com.iulu.dotimer

import android.content.Context
import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationManagerCompat
import com.iulu.dotimer.timer.ALARM_EVENT_BUTTON_STOP
import com.iulu.dotimer.timer.AlarmService
import com.iulu.dotimer.timer.NOTIFICATION_ID
import com.iulu.dotimer.timer.NotificationActionButton
import com.iulu.dotimer.timer.TimerNotification.Companion.showNotification
import com.iulu.dotimer.timer.TimerPref
import com.iulu.dotimer.timer.sendBroadcast
import com.iulu.dotimer.ui.TimerState

/**
 * This object is used to Start/Stop Alarm and Send Notifications.
 * */
object DoTimerNotification {

    /**
     * Start the Alarm. This is called before [stopAlarm].
     * @param context Context.
     * */
    fun startAlarm(context: Context) {
        TimerPref.saveTimerState(context, TimerState.ALARM)
        context.startForegroundService(Intent(context, AlarmService::class.java))
        context.sendBroadcast(TIMER_ACTION_TYPE_ALARM, action = TIMER_ACTION_FIRE)
    }

    /**
     * Stop the Alarm. This is called after [startAlarm].
     * @param context Context.
     * */
    fun stopAlarm(context: Context) {
        context.sendBroadcast(action = ALARM_EVENT_BUTTON_STOP)
    }

    /**
     * Show notification.
     * @param context App Context.
     * @param title Notification Title.
     * @param description Notification Description.
     * @param buttonText Button text. Default is null = No button is shown.
     * @param buttonDrawable Drawable resource reference for Notification button.
     * */
    fun showNotification(
        context: Context,
        title: String,
        description: String,
        buttonText: String? = null,
        @DrawableRes buttonDrawable: Int = 0
    ) {
        TimerPref.saveTimerState(context, TimerState.INIT)
        context.sendBroadcast(TIMER_ACTION_TYPE_NOTIFY, action = TIMER_ACTION_FIRE)
        val buttonAction =
            if (buttonDrawable != 0) NotificationActionButton(buttonText, buttonDrawable) else null
        context.apply {
            showNotification(title, description, buttonAction)
        }
    }

    /**
     * Cancel notification.
     * @param context App Context.
     * */
    fun cancelNotification(context: Context) {
        NotificationManagerCompat.from(context).cancel(NOTIFICATION_ID)
    }
}

const val TIMER_ACTION_TYPE_NOTIFY = "TIMER_ACTION_TYPE_NOTIFY"
const val TIMER_ACTION_TYPE_ALARM = "TIMER_ACTION_TYPE_ALARM"
const val TIMER_ACTION_FIRE = "FIRE"