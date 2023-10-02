/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * copyright 2023, Purcel Iulian
 */

package com.iulu.dotimer.ui

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.iulu.dotimer.R

enum class FABState(@DrawableRes val id: Int) {
    START(R.drawable.ic_button_start),
    PAUSE(R.drawable.ic_button_pause),
    STOP(R.drawable.ic_button_stop);
}

fun FloatingActionButton.setState(context: Context, state: FABState) {
    setImageDrawable(AppCompatResources.getDrawable(context, state.id))
}