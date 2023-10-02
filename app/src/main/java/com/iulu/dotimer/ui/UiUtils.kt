/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * copyright 2023, Purcel Iulian
 */

package com.iulu.dotimer.ui

import kotlin.math.sign

fun fromSecondsToHhMmSs(seconds: Long): String {
    val s = seconds.sign
    val sec = (seconds % 60) * s
    val min = ((seconds / 60) % 60) * s
    val hor = ((seconds / 3600) % 60) * s
    val secFormat = String.format("%02d", sec)
    val minFormat = String.format("%02d", min)
    val horFormat = String.format("%02d", hor)

    return (if (s == -1) "-" else "") +
            (if (hor <= 0) "" else "$horFormat:") +
            (if (min <= 0 && hor <= 0) "" else "$minFormat:") +
            secFormat
}