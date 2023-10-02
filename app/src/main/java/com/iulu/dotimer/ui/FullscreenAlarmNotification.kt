/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * copyright 2023, Purcel Iulian
 */

package com.iulu.dotimer.ui

import android.app.Activity
import android.app.KeyguardManager
import android.content.BroadcastReceiver
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.iulu.dotimer.databinding.ActivityFullscreenAlarmNotificationBinding
import com.iulu.dotimer.timer.*

class FullscreenAlarmNotification : AppCompatActivity() {
    private lateinit var binding: ActivityFullscreenAlarmNotificationBinding
    private lateinit var actionAlarmUpdateBroadcastReceiver: BroadcastReceiver
    private var buttonStopBroadcastReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        hideInsets()
        turnScreenOnAndKeyguardOff()
        super.onCreate(savedInstanceState)
        binding = ActivityFullscreenAlarmNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionAlarmUpdateBroadcastReceiver =
            addLongBroadcast(action = ALARM_EVENT_UPDATE) { timeElapsed ->
                binding.textView.text = fromSecondsToHhMmSs(timeElapsed)
            }

        binding.stopButton.setOnClickListener {
            sendBroadcast(action = ALARM_EVENT_BUTTON_STOP)
        }
        buttonStopBroadcastReceiver = addBroadcast(action = ALARM_EVENT_BUTTON_STOP) {
            finishAndRemoveTask()
        }
    }

    override fun onDestroy() {
        turnScreenOffAndKeyguardOn()
        unregisterReceiver(actionAlarmUpdateBroadcastReceiver)
        unregisterReceiver(buttonStopBroadcastReceiver)
        super.onDestroy()
    }

    private fun Activity.turnScreenOnAndKeyguardOff() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        }

        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
        )

        with(getSystemService(KEYGUARD_SERVICE) as KeyguardManager) {
            requestDismissKeyguard(this@turnScreenOnAndKeyguardOff, null)
        }
    }

    private fun Activity.turnScreenOffAndKeyguardOn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(false)
            setTurnScreenOn(false)
        }

        window.clearFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
        )
    }

    private fun hideInsets() {
        //WindowCompat.setDecorFitsSystemWindows(window, false)
        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        // Configure the behavior of the hidden system bars.
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        window.decorView.setOnApplyWindowInsetsListener { view, windowInsets ->

            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

            view.onApplyWindowInsets(windowInsets)
        }
    }
}