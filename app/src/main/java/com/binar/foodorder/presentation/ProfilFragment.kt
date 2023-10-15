package com.binar.foodorder.presentation

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.binar.foodorder.R
import com.binar.foodorder.data.network.firebase.FirebaseAuthDataSourceImpl
import com.binar.foodorder.data.repository.UserRepositoryImpl
import com.binar.foodorder.databinding.FragmentProfilBinding
import com.binar.foodorder.util.GenericViewModelFactory
import com.binar.foodorder.util.proceedWhen
import com.binar.foodorder.viewmodel.ProfileViewModel
import com.binar.foodorder.viewmodel.SplashViewModel
import com.google.firebase.auth.FirebaseAuth


class ProfilFragment : Fragment() {
    private lateinit var binding: FragmentProfilBinding
    private val viewModel: ProfileViewModel by viewModels {
        GenericViewModelFactory.create(createViewModel())
    }
    private val pickMedia =
        this.registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                changePhotoProfile(uri)
            }
        }

    private fun changePhotoProfile(uri: Uri) {
        viewModel.updateProfilePicture(uri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showItemProfile()
        setOnClick()
        observeData()
    }

    private fun setOnClick() {
        binding.btnChangeProfile.setOnClickListener {
            if (checkNameValidation()) {
                changeProfileData()
            }
        }
        binding.btnLogout.setOnClickListener {
            doLogOut()
        }
        binding.tvChangePwd.setOnClickListener {
            requestChangePassword()
        }

        binding.ivProfil.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun doLogOut() {
        val dialog = AlertDialog.Builder(requireContext()).setMessage("Do you want to logout ?")
            .setPositiveButton(
                "Yes"
            ) { dialog, id ->
                viewModel.doLogOut()
                navigateToLogin()
            }
            .setNegativeButton(
                "No"
            ) { dialog, id ->
                //no-op , do nothing
            }.create()
        dialog.show()
    }

    private fun navigateToLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }

    private fun showItemProfile() {
        viewModel.getCurrentUser()?.let {
            binding.editTextName.setText(it.fullName)
            binding.editTextEmail.setText(it.email)
            binding.ivProfil.load(it.photoUrl) {
                crossfade(true)
                placeholder(R.drawable.account)
                error(R.drawable.account)
                transformations(CircleCropTransformation())
            }
        }
    }

    private fun changeProfileData() {
        val fullName = binding.editTextName.text.toString().trim()
        viewModel.updateFullName(fullName)
    }

    private fun checkNameValidation(): Boolean {
        val fullName = binding.editTextName.text.toString().trim()
        return if (fullName.isEmpty()) {
            binding.textInputLayoutName.isErrorEnabled = true
            binding.textInputLayoutName.error = getString(R.string.text_error_name_cannot_empty)
            false
        } else {
            binding.textInputLayoutName.isErrorEnabled = false
            true
        }
    }

    private fun requestChangePassword() {
        viewModel.createChangePwdRequest()
        val dialog = AlertDialog.Builder(requireContext())
            .setMessage("Change password request sended to your email : ${viewModel.getCurrentUser()?.email} Please check to your inbox or spam")
            .setPositiveButton(
                "Okay"
            ) { dialog, id ->

            }.create()
        dialog.show()
    }

    private fun observeData() {
        viewModel.changePhotoResult.observe(viewLifecycleOwner) {
            it.proceedWhen(doOnSuccess = {
                Toast.makeText(
                    requireContext(),
                    "Change Photo Profile Success !",
                    Toast.LENGTH_SHORT
                ).show()
                showItemProfile()
            }, doOnError = {
                Toast.makeText(
                    requireContext(),
                    "Change Photo Profile Failed !",
                    Toast.LENGTH_SHORT
                ).show()
                showItemProfile()
            })
        }
        viewModel.changeProfileResult.observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnSuccess = {
                    Toast.makeText(
                        requireContext(),
                        "Change Profile data Success !",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                doOnError = {
                    Toast.makeText(
                        requireContext(),
                        "Change Profile data Failed !",
                        Toast.LENGTH_SHORT
                    ).show()

                },
            )
        }
    }

    private fun createViewModel(): ProfileViewModel {
        val firebaseAuth = FirebaseAuth.getInstance()
        val dataSource = FirebaseAuthDataSourceImpl(firebaseAuth)
        val repo = UserRepositoryImpl(dataSource)
        return ProfileViewModel(repo)
    }
}

