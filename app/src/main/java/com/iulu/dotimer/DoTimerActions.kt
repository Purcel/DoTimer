/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * copyright 2023, Purcel Iulian
 */

package com.iulu.dotimer

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Handler
import android.os.Looper
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity

@Keep
object TimerActionStopMusic : ITimerAction {
    override fun fire(context: Context) {
        val handler = Handler(Looper.getMainLooper())
        val audioManager = context.getSystemService(AppCompatActivity.AUDIO_SERVICE) as AudioManager
        val focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN).run {
            setAudioAttributes(AudioAttributes.Builder().run {
                setUsage(AudioAttributes.USAGE_UNKNOWN)
                setContentType(AudioAttributes.CONTENT_TYPE_UNKNOWN)
                build()
            })
            setAcceptsDelayedFocusGain(true)
            setOnAudioFocusChangeListener({}, handler)
            build()
        }
        val res = audioManager.requestAudioFocus(focusRequest)
        val lock = Any()
        synchronized(lock) {
            when (res) {
                AudioManager.AUDIOFOCUS_REQUEST_FAILED -> {}
                AudioManager.AUDIOFOCUS_REQUEST_GRANTED -> {
                    DoTimerNotification.showNotification(
                        context,
                        context.getString(R.string.notification_stop_music_title),
                        context.getString(R.string.notification_stop_music_description)
                    )
                }

                AudioManager.AUDIOFOCUS_REQUEST_DELAYED -> {}
            }
        }
    }
}

@Keep
object TimerActionAlarm : ITimerAction {
    override fun fire(context: Context) {
        DoTimerNotification.startAlarm(context)
    }

    override fun notificationButtonPressed(context: Context) {
        DoTimerNotification.stopAlarm(context)
    }
}

@Keep
object TimerActionFlashLight : ITimerAction {
    override fun fire(context: Context) {
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            cameraManager.setTorchMode("0", true)
        } catch (_: CameraAccessException) {
        }
        DoTimerNotification.showNotification(
            context,
            context.getString(R.string.notification_flashlight_on_title),
            context.getString(R.string.notification_flashlight_on_description),
            context.getString(R.string.notification_flashlight_on_button),
            R.drawable.ic_button_stop
        )
    }

    override fun notificationButtonPressed(context: Context) {
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            cameraManager.setTorchMode("0", false)
        } catch (_: CameraAccessException) {
        }
        DoTimerNotification.cancelNotification(context)
    }
}
