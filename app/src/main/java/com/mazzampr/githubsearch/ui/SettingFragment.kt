package com.mazzampr.githubsearch.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mazzampr.githubsearch.databinding.FragmentSettingBinding
import com.mazzampr.githubsearch.viewmodel.SettingsViewModel
import com.mazzampr.githubsearch.viewmodel.ViewModelFactory

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<SettingsViewModel> { ViewModelFactory.getInstance(requireActivity()) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSettingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            val action = SettingFragmentDirections.actionSettingFragmentToHomeFragment()
            findNavController().navigate(action)
        }

        val switchTheme = binding.switchTheme

        viewModel.getThemeSetting().observe(viewLifecycleOwner) {isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }
        switchTheme.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.saveThemeSetting(isChecked)
        }
    }


}