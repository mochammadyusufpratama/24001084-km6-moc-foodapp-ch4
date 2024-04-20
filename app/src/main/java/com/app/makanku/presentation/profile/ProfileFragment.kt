package com.app.makanku.presentation.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import coil.load
import coil.transform.CircleCropTransformation
import com.app.makanku.R
import com.app.makanku.data.datasource.category.DummyCategoryDataSource
import com.app.makanku.data.datasource.product.DummyProductDataSource
import com.app.makanku.data.datasource.user.AuthDataSource
import com.app.makanku.data.datasource.user.FirebaseAuthDataSource
import com.app.makanku.data.repository.CategoryRepository
import com.app.makanku.data.repository.CategoryRepositoryImpl
import com.app.makanku.data.repository.ProductRepository
import com.app.makanku.data.repository.ProductRepositoryImpl
import com.app.makanku.data.repository.UserRepository
import com.app.makanku.data.repository.UserRepositoryImpl
import com.app.makanku.data.source.firebase.FirebaseService
import com.app.makanku.data.source.firebase.FirebaseServiceImpl
import com.app.makanku.databinding.FragmentProfileBinding
import com.app.makanku.presentation.home.HomeViewModel
import com.app.makanku.presentation.login.LoginActivity
import com.app.makanku.utils.GenericViewModelFactory
import com.app.makanku.utils.proceedWhen

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private val viewModel: ProfileViewModel by viewModels {
        val service: FirebaseService = FirebaseServiceImpl()

        val authDataSource: AuthDataSource = FirebaseAuthDataSource(service)
        val userRepository: UserRepository = UserRepositoryImpl(authDataSource)

        GenericViewModelFactory.create(ProfileViewModel(userRepository))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
            viewModel.requestChangePasswordByEmail()
            requestChangePasswordDialog()
        }
    }

    private fun requestChangePasswordDialog() {
        val dialog = AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.text_dialog_when_change_password))
            .setPositiveButton(
                getString(R.string.text_positive_button_dialog)
            ) { dialog, id ->

            }.create()
        dialog.show()
    }

    private fun doLogout() {
        binding.tvLogout.setOnClickListener {
            viewModel.isUserLoggedOut()
            Toast.makeText(requireContext(), "Logout Success!", Toast.LENGTH_SHORT).show()
            navigateToLogin()
            requireActivity().supportFragmentManager.popBackStack(
                null,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )

        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(requireContext(), LoginActivity::class.java).apply {})
    }

    private fun changeProfileData() {
        val fullName = binding.layoutForm.etName.text.toString().trim()
        viewModel.updateFullName(fullName)
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
        viewModel.changeProfileResult.observe(viewLifecycleOwner, Observer { result ->
            result.proceedWhen(
                doOnSuccess = {
                    binding.pbLoading.isVisible = false
                    binding.btnChangeProfile.isVisible = true
                    Toast.makeText(
                        requireContext(),
                        "Change Profile data Success !",
                        Toast.LENGTH_SHORT).show()
                },
                doOnError = {
                    binding.pbLoading.isVisible = false
                    binding.btnChangeProfile.isVisible = true
                    Toast.makeText(
                        requireContext(),
                        "Change Profile data Failed !",
                        Toast.LENGTH_SHORT).show()
                },
                doOnLoading = {
                    binding.pbLoading.isVisible = true
                    binding.btnChangeProfile.isVisible = false
                }
            )
        })
    }

    private fun setupForm() {
        binding.layoutForm.tilName.isVisible = true
        binding.layoutForm.tilEmail.isVisible = true
        binding.layoutForm.etEmail.isEnabled = false
    }

    private fun showUserData() {
        viewModel.getCurrentUser()?.let {
            binding.layoutForm.etName.setText(it.fullName)
            binding.layoutForm.etEmail.setText(it.email)
            binding.ivProfilePict.load("https://github.com/mochammadyusufpratama/makanku-asset/blob/main/profile.jpg?raw=true")
        }
    }

}