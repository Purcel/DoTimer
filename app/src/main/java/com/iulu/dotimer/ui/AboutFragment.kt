/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * copyright 2023, Purcel Iulian
 */

package com.iulu.dotimer.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iulu.dotimer.R
import com.iulu.dotimer.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {
    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)

        binding.githubButton.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(requireContext().getString(R.string.github_link))
            )

            requireActivity().startActivity(intent)
        }

        binding.policyLink.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(requireContext().getString(R.string.github_policy_link))
            )

            requireActivity().startActivity(intent)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}