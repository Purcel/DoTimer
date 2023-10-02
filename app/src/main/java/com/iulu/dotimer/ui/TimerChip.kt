/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * copyright 2023, Purcel Iulian
 */

package com.iulu.dotimer.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import com.google.android.material.chip.Chip
import com.iulu.dotimer.R

@SuppressLint("CustomViewStyleable", "PrivateResource")
class TimerChip : Chip {
    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        val tp = context.obtainStyledAttributes(attributeSet, R.styleable.TimerChipStyleable)
        chipIconRes = tp.getResourceId(com.google.android.material.R.styleable.Chip_chipIcon, 0)
        needsPostNotificationPermission =
            tp.getBoolean(R.styleable.TimerChipStyleable_needsPostNotificationPermission, false)
        tp.recycle()
    }

    @DrawableRes
    var chipIconRes: Int = 0
        private set

    var needsPostNotificationPermission = false
}