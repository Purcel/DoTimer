/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * copyright 2023, Purcel Iulian
 */

package com.iulu.dotimer.timer

import android.app.Activity
import android.app.AlarmManager
import android.app.AlarmManager.RTC_WAKEUP
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import androidx.lifecycle.*
import com.iulu.dotimer.*
import com.iulu.dotimer.timer.TimerNotification.Companion.createNotificationChannel

class Timer(private val context: Activity) {
    private lateinit var actionFiredBroadcastReceiver: BroadcastReceiver
    private lateinit var actionAlarmUpdateBroadcastReceiver: BroadcastReceiver
    private lateinit var actionAlarmButtonStopBroadcastReceiver: BroadcastReceiver
    private var countDownTimer: CountDownTimer? = null

    private val _onTickLiveData: MutableLiveData<Long> = MutableLiveData()
    val onTickLiveData: LiveData<Long> = _onTickLiveData

    fun initTimer() {
        context.apply {
            createNotificationChannel(
                CHANNEL_ID_ALARM,
                getString(R.string.notification_channel_alarm_name),
                getString(R.string.notification_channel_alarm_desc),
                NotificationManager.IMPORTANCE_HIGH
            )
            createNotificationChannel(
                CHANNEL_ID,
                getString(R.string.notification_channel_name),
                getString(R.string.notification_channel_desc),
                NotificationManager.IMPORTANCE_HIGH
            )
        }
        TimerPref.apply {
            firstStart(context) {
                saveActionClass(context, "com.iulu.dotimer.TimerActionStopMusic")
                saveCheckedIcon(context, R.drawable.ic_music_off)
            }
        }
    }

    fun setBackgroundTimer(wakeUpTime: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, TimeExpiredReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms())
                alarmManager.setExactAndAllowWhileIdle(RTC_WAKEUP, wakeUpTime, pendingIntent)
        } else
            alarmManager.setExactAndAllowWhileIdle(RTC_WAKEUP, wakeUpTime, pendingIntent)
        TimerPref.saveWakeupTime(context, wakeUpTime)
    }

    fun removeBackgroundTimer() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, TimeExpiredReceiver::class.java)
        PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            .apply {
                alarmManager.cancel(this)
            }
    }

    private fun fireAlarm() {
        val intent = Intent(context, TimeExpiredReceiver::class.java)
        context.sendBroadcast(intent)
    }

    fun stopAlarm() {
        context.sendBroadcast(action = ALARM_EVENT_BUTTON_STOP)
    }

    fun alarmUpdateListener(tick: (Long) -> Unit, stopButton: () -> Unit) {

        actionAlarmUpdateBroadcastReceiver =
            context.addLongBroadcast(action = ALARM_EVENT_UPDATE) { timeElapsed ->
                tick(timeElapsed)
            }

        actionAlarmButtonStopBroadcastReceiver =
            context.addBroadcast(action = ALARM_EVENT_BUTTON_STOP) {
                stopButton()
            }
    }

    fun timerFireListener(typeNotify: (String) -> Unit, typeAlarm: (String) -> Unit) {
        actionFiredBroadcastReceiver = context.addStringBroadcast(action = TIMER_ACTION_FIRE) {
            when (it) {
                TIMER_ACTION_TYPE_NOTIFY -> typeNotify(it)
                TIMER_ACTION_TYPE_ALARM -> typeAlarm(it)
            }
        }
    }

    fun unregisterTimer() {
        context.unregisterReceiver(actionFiredBroadcastReceiver)
        context.unregisterReceiver(actionAlarmUpdateBroadcastReceiver)
        context.unregisterReceiver(actionAlarmButtonStopBroadcastReceiver)
    }

    fun startForegroundTimer(millis: Long = 0L) {
        if (millis == 0L)
            countDownTimer?.start()
        else
            countDownTimer = object : CountDownTimer(millis, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    _onTickLiveData.value = millisUntilFinished
                }

                override fun onFinish() {
                    fireAlarm()
                }
            }.start()
    }

    fun pauseForegroundTimer() {
        countDownTimer?.cancel()
    }

    fun stopForegroundTimer() {
        pauseForegroundTimer()
        countDownTimer = null
    }
}

