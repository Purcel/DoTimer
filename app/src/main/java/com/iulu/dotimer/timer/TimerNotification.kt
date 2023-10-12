/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * copyright 2023, Purcel Iulian
 */

package com.iulu.dotimer.timer

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.annotation.DrawableRes
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.iulu.dotimer.ui.MainActivity
import com.iulu.dotimer.R
import com.iulu.dotimer.ui.FullscreenAlarmNotification

class TimerNotification(private val context: Context) {
    companion object {
        fun Context.createNotificationChannel(
            id: String,
            name: String,
            description: String,
            importance: Int
        ) {
            val channel = NotificationChannel(id, name, importance).apply {
                this.description = description
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                if (id != CHANNEL_ID_ALARM)
                    setSound(null, null)
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        fun getNotificationChannel(context: Context, channelId: String): NotificationChannel {
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            return notificationManager.getNotificationChannel(channelId)
        }

        private fun getNotificationContentIntent(context: Context): PendingIntent {
            val notificationBodyIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            return PendingIntent
                .getActivity(context, 0, notificationBodyIntent, PendingIntent.FLAG_IMMUTABLE)
        }

        private fun getPendingIntent(context: Context): PendingIntent {
            val mIntent = Intent(context, NotificationActionButtonReceiver::class.java)
            return PendingIntent.getBroadcast(context, 0, mIntent, PendingIntent.FLAG_IMMUTABLE)
        }

        fun Context.showNotification(
            title: String,
            description: String,
            actionButton: NotificationActionButton? = null
        ): Notification {

            val buttonAction = if (actionButton != null) NotificationCompat.Action.Builder(
                actionButton.buttonDrawable,
                actionButton.buttonText,
                getPendingIntent(this)
            ).build()
            else null

            val builder = NotificationCompat.Builder(this, CHANNEL_ID).run {
                setSmallIcon(R.drawable.ic_timer_notification)
                setContentTitle(title)
                setContentText(description)
                setContentIntent(getNotificationContentIntent(this@showNotification))
                setCategory(Notification.CATEGORY_REMINDER)
                setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                setAutoCancel(true)
                if (buttonAction != null)
                    addAction(buttonAction)
                build()
            }

            return with(NotificationManagerCompat.from(this)) {
                if (ActivityCompat.checkSelfPermission(
                        this@showNotification,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    notify(NOTIFICATION_ID, builder)
                }
                builder
            }
        }
    }

    private val notificationBuilder: NotificationCompat.Builder by lazy {
        NotificationCompat.Builder(context, CHANNEL_ID_ALARM)
    }

    fun buildUpdateShowAlarmNotification(
        title: String,
        description: String,
        actionButton: NotificationActionButton? =
            NotificationActionButton(
                context.getString(R.string.notification_alarm_button_name),
                R.drawable.ic_button_stop
            ),
        build: Boolean
    ): Notification {

        if (build) {
            val buttonAction = if (actionButton != null) NotificationCompat.Action.Builder(
                actionButton.buttonDrawable,
                actionButton.buttonText,
                getPendingIntent(context)
            ).build()
            else null

            val fullscreenIntent = Intent(context, FullscreenAlarmNotification::class.java).apply {
                //flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val fullscreenPendingIntent = PendingIntent.getActivity(
                context,
                1,
                fullscreenIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
            )
            notificationBuilder.run {
                setSmallIcon(R.drawable.ic_timer_notification)
                color = context.getColor(R.color.alarm_notification_color)
                setColorized(true)
                setOnlyAlertOnce(true)
                foregroundServiceBehavior = NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE
                setContentIntent(getNotificationContentIntent(context))
                setFullScreenIntent(fullscreenPendingIntent, true)
                if (buttonAction != null)
                    addAction(buttonAction)
                setCategory(Notification.CATEGORY_ALARM)
                setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                setOngoing(true)
                setAutoCancel(true)
                build()
            }
        }

        val notification = notificationBuilder.run {
            setContentTitle(title)
            setContentText(description)
            build()
        }
        showAlarmNotification(notification)
        return notification
    }

    private fun showAlarmNotification(notification: Notification) {
        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                notify(ALARM_NOTIFICATION_ID, notification)
            }
        }
    }

}

data class NotificationActionButton(
    val buttonText: String? = null,
    @DrawableRes val buttonDrawable: Int = 0
)

const val CHANNEL_ID = "CHANNEL_ID"
const val CHANNEL_ID_ALARM = "CHANNEL_ID_ALARM"
const val ALARM_NOTIFICATION_ID = 1
const val NOTIFICATION_ID = 2

