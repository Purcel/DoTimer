/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * copyright 2023, Purcel Iulian
 */

package com.iulu.dotimer.timer

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.os.IBinder
import androidx.core.app.NotificationManagerCompat
import com.iulu.dotimer.R
import com.iulu.dotimer.timer.TimerNotification.Companion.getNotificationChannel
import com.iulu.dotimer.ui.TimerState
import com.iulu.dotimer.ui.fromSecondsToHhMmSs

class AlarmService : Service() {
    private var buttonStopBroadcastReceiver: BroadcastReceiver? = null
    private val timerNotification: TimerNotification by lazy {
        TimerNotification(context = this)
    }

    var timeMillis = 0L
        private set

    private var firstTime = true
    private val countDownTimer: CountDownTimer by lazy {
        object : CountDownTimer(ALARM_TIMEOUT, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeMillis = millisUntilFinished
                val elapsedTimeSeconds = ((timeMillis - ALARM_TIMEOUT) / 1000)

                val notification = timerNotification.buildUpdateShowAlarmNotification(
                    fromSecondsToHhMmSs(elapsedTimeSeconds),
                    getString(R.string.notification_alarm_description),
                    build = firstTime
                )

                if (firstTime) {
                    startForeground(ALARM_NOTIFICATION_ID, notification)
                }

                firstTime = false
                baseContext.sendLongBroadcast(elapsedTimeSeconds, action = ALARM_EVENT_UPDATE)
            }

            override fun onFinish() {
                timeMillis = 0L
                firstTime = true
                baseContext.sendBroadcast(action = ALARM_EVENT_BUTTON_STOP)
            }
        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        playSound()
        countDownTimer.start()
        buttonStopBroadcastReceiver = addBroadcast(action = ALARM_EVENT_BUTTON_STOP) {
            stopAlarm()
        }
        return START_STICKY
    }

    private fun stopAlarm() {
        stopSound()
        countDownTimer.cancel()
        TimerPref.saveTimerState(this, TimerState.INIT)
        NotificationManagerCompat.from(this).cancel(ALARM_NOTIFICATION_ID)
        unregisterReceiver(buttonStopBroadcastReceiver)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private lateinit var player: MediaPlayer
    private fun playSound() {
        val channel = getNotificationChannel(this, CHANNEL_ID_ALARM)

        player = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                    //.setLegacyStreamType(AudioManager.STREAM_ALARM)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            setDataSource(this@AlarmService, channel.sound)
            isLooping = true
            prepare()
            start()
        }
    }

    private fun stopSound() {
        player.stop()
        player.reset()
        player.release()
    }
}

const val ALARM_EVENT_BUTTON_STOP = "STOP"
const val ALARM_EVENT_UPDATE = "ALARM_EVENT_UPDATE"

private const val ALARM_TIMEOUT = 120_000L
