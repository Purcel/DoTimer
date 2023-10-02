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
import android.content.IntentFilter
import android.os.Build
import kotlin.reflect.full.functions

fun Context.sendBroadcast(string: String = "", action: String) {
    sendBroadcast(Intent(action).apply { putExtra("STRING", string) })
}

fun Context.sendLongBroadcast(long: Long, action: String) {
    sendBroadcast(Intent(action).apply { putExtra("LON", long) })
}

fun Context.addBroadcast(action: String, block: () -> Unit): BroadcastReceiver {
    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            block()
        }
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        registerReceiver(receiver, IntentFilter(action), Context.RECEIVER_EXPORTED)
    } else
        registerReceiver(receiver, IntentFilter(action))
    return receiver
}

fun Context.addStringBroadcast(action: String, block: (String) -> Unit): BroadcastReceiver {
    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            block(intent?.getStringExtra("STRING") ?: "")
        }
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        registerReceiver(receiver, IntentFilter(action), Context.RECEIVER_EXPORTED)
    } else
        registerReceiver(receiver, IntentFilter(action))
    return receiver
}

fun Context.addLongBroadcast(action: String, block: (Long) -> Unit): BroadcastReceiver {
    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            block(intent?.getLongExtra("LON", 0) ?: 0)
        }
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        registerReceiver(receiver, IntentFilter(action), Context.RECEIVER_EXPORTED)
    } else
        registerReceiver(receiver, IntentFilter(action))
    return receiver
}

fun String.callObj(member: String, context: Context, vararg params: Any): Any? =
    Class.forName(this).kotlin.run {
        functions.last { it.name == member }.call(objectInstance, context, *params)
    }