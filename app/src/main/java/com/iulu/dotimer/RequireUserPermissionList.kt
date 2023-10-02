/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * copyright 2023, Purcel Iulian
 */

package com.iulu.dotimer

/**
 * This is the list of actions that can't work without user permission.
 * Like for example: Alarm.
 * Is used to disable the Chip button in ActionBottomSheet Fragment if the user
 * notification permission is not granted.
 * */
val requireUserPermissionList = listOf("com.iulu.dotimer.TimerActionAlarm")