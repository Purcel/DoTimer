/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * copyright 2023, Purcel Iulian
 */

package com.iulu.dotimer.ui

import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

object UserPermissionManager {
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    fun init(fragment: Fragment, callback: (Boolean) -> Unit) {
        requestPermissionLauncher =
            fragment.registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    callback(true)
                } else {
                    callback(false)
                }
            }
    }

    fun isPermissionGranted(activity: FragmentActivity): Boolean =
        when (ContextCompat.checkSelfPermission(
            activity,
            "android.permission.POST_NOTIFICATIONS"
        )) {
            PackageManager.PERMISSION_GRANTED -> true
            PackageManager.PERMISSION_DENIED -> false
            else -> false
        }

    fun shouldShowRequestPermissionRationale(activity: FragmentActivity): Boolean =
        activity.shouldShowRequestPermissionRationale("android.permission.POST_NOTIFICATIONS")


    fun launch() {
        requestPermissionLauncher.launch("android.permission.POST_NOTIFICATIONS")
    }
}