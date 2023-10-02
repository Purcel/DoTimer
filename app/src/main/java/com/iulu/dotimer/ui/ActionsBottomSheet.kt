/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * copyright 2023, Purcel Iulian
 */

package com.iulu.dotimer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.view.children
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.ChipGroup
import com.iulu.dotimer.databinding.ActionBottomSheetBinding
import com.iulu.dotimer.requireUserPermissionList
import com.iulu.dotimer.timer.TimerPref

class ActionsBottomSheet : BottomSheetDialogFragment() {
    private var _binding: ActionBottomSheetBinding? = null
    private val binding get() = _binding!!

    @DrawableRes
    var checkedChipIconId: Int? = null
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UserPermissionManager.init(this) { isPermissionGranted ->
            if (isPermissionGranted) {
                permissionGranted()
            } else {
                permissionDenied()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.chipGroup.apply {
            restoreCheckedChip()
            setOnCheckedStateChangeListener { _, _ ->
                saveCheckedChip()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ActionBottomSheetBinding.inflate(inflater, container, false).run {
        _binding = this

        if (UserPermissionManager.shouldShowRequestPermissionRationale(requireActivity())) {
            permissionDenied()
        }

        binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun permissionDenied() {
        binding.rationaleMessageView.visibility = View.VISIBLE
        binding.rationaleSpaceView.visibility = View.VISIBLE
        binding.chipGroup.isEnabledChipWithPermissionRequirements = false
        binding.grandButtonView.setOnClickListener {
            UserPermissionManager.launch()
        }
    }

    private fun permissionGranted() {
        binding.rationaleMessageView.visibility = View.GONE
        binding.rationaleSpaceView.visibility = View.GONE
        binding.chipGroup.isEnabledChipWithPermissionRequirements = true
    }

    private var ChipGroup.isEnabledChipWithPermissionRequirements: Boolean
        get() = true //NOT Necessary right for this fragment
        set(value) {
            children.forEach { tc ->
                tc as TimerChip
                val result = requireUserPermissionList.find {
                    tc.tag == it
                }
                if (result != null) {
                    tc.isEnabled = value
                }
            }
        }

    private fun ChipGroup.restoreCheckedChip() {
        val savedAction = TimerPref.getActionClass(context)
        children.forEach {
            it as TimerChip
            if (it.tag as String == savedAction) {
                it.isChecked = true
            }
        }
    }

    private fun ChipGroup.saveCheckedChip() {
        children.forEach {
            it as TimerChip
            if (it.isChecked) {
                checkedChipIconId = it.chipIconRes
                TimerPref.saveActionClass(requireContext(), it.tag as String)
                TimerPref.saveCheckedIcon(requireContext(), checkedChipIconId!!)
            }
        }
    }

    companion object {
        //const val TAG = "ModalBottomSheet"
    }
}