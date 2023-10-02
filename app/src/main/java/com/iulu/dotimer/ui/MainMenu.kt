/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * copyright 2023, Purcel Iulian
 */

package com.iulu.dotimer.ui

import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.iulu.dotimer.R

fun mainMenuDispatcher(menu: MenuItem, fragment: Fragment) {
    menu.setOnMenuItemClickListener {
        when (menu.itemId) {
            R.id.billing -> {
                fragment.findNavController()
                    .navigate(MainFragmentDirections.actionMainFragmentToBillingNavGraph())
            }

            R.id.about -> {
                val action = MainFragmentDirections.actionMainFragmentToAboutFragment()
                fragment.findNavController().navigate(action)
            }
        }
        false
    }
}