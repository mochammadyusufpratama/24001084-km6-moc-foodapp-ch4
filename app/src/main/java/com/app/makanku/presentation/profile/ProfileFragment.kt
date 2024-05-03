package com.app.makanku.presentation.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.app.makanku.R
import com.app.makanku.databinding.FragmentProfileBinding
import com.app.makanku.presentation.login.LoginActivity
import com.app.makanku.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    private val profileViewModel: ProfileViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupForm()
        showUserData()
        setClickListeners()
        observeData()
    }

    private fun setClickListeners() {
        binding.btnChangeProfile.setOnClickListener {
            if (checkNameValidation()) {
                changeProfileData()
            }
        }
        binding.tvChangePwd.setOnClickListener {
            requestChangePassword()
        }
        binding.tvLogout.setOnClickListener {
            doLogout()
        }
    }

    private fun requestChangePassword() {
        binding.tvChangePwd.setOnClickListener {
            profileViewModel.requestChangePasswordByEmail()
            requestChangePasswordDialog()
        }
    }

    private fun requestChangePasswordDialog() {
        val dialog =
            AlertDialog.Builder(requireContext())
                .setMessage(getString(R.string.text_dialog_when_change_password))
                .setPositiveButton(
                    getString(R.string.text_positive_button_dialog),
                ) { dialog, id ->
                }.create()
        dialog.show()
    }

    private fun doLogout() {
        binding.tvLogout.setOnClickListener {
            profileViewModel.isUserLoggedOut()
            Toast.makeText(requireContext(), "Logout Success!", Toast.LENGTH_SHORT).show()

            navigateToLogin()
            requireActivity().supportFragmentManager.popBackStack(
                null,
                FragmentManager.POP_BACK_STACK_INCLUSIVE,
            )
        }
    }

    private fun navigateToLogin() {
        startActivity(
            Intent(requireContext(), LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            },
        )
    }

    private fun changeProfileData() {
        val fullName = binding.layoutForm.etName.text.toString().trim()
        profileViewModel.updateFullName(fullName)
    }

    private fun checkNameValidation(): Boolean {
        val fullName = binding.layoutForm.etName.text.toString().trim()
        return if (fullName.isEmpty()) {
            binding.layoutForm.tilName.isErrorEnabled = true
            binding.layoutForm.tilName.error = getString(R.string.text_error_name_cannot_empty)
            false
        } else {
            binding.layoutForm.tilName.isErrorEnabled = false
            true
        }
    }

    private fun observeData() {
        profileViewModel.changeProfileResult.observe(
            viewLifecycleOwner,
            Observer { result ->
                result.proceedWhen(
                    doOnSuccess = {
                        binding.pbLoading.isVisible = false
                        binding.btnChangeProfile.isVisible = true
                        Toast.makeText(
                            requireContext(),
                            "Change Profile data Success !",
                            Toast.LENGTH_SHORT,
                        ).show()
                    },
                    doOnError = {
                        binding.pbLoading.isVisible = false
                        binding.btnChangeProfile.isVisible = true
                        Toast.makeText(
                            requireContext(),
                            "Change Profile data Failed !",
                            Toast.LENGTH_SHORT,
                        ).show()
                    },
                    doOnLoading = {
                        binding.pbLoading.isVisible = true
                        binding.btnChangeProfile.isVisible = false
                    },
                )
            },
        )
    }

    private fun setupForm() {
        binding.layoutForm.tilName.isVisible = true
        binding.layoutForm.tilEmail.isVisible = true
        binding.layoutForm.etEmail.isEnabled = false
    }

    private fun showUserData() {
        profileViewModel.getCurrentUser()?.let {
            binding.layoutForm.etName.setText(it.fullName)
            binding.layoutForm.etEmail.setText(it.email)
            binding.ivProfilePict.setImageResource(R.drawable.img_logo_app)
        }
    }
}
