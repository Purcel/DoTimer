/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * copyright 2023, Purcel Iulian
 */

package com.iulu.dotimer

import android.content.Context

/**
 * This interface is used to implement timer actions.
 * */
interface ITimerAction {
    /**
     * This function is triggered when the time expire.
     * @param context Context.
     * */
    fun fire(context: Context)

    /**
     * This function is triggered when the Notification Button is pressed.
     * Note: This is optional.
     * @param context Context.
     * */
    fun notificationButtonPressed(context: Context) {}
}

const val MEMBER_FIRE = "fire"
const val MEMBER_NOTIFICATION_BUTTON_PRESSED = "notificationButtonPressed"