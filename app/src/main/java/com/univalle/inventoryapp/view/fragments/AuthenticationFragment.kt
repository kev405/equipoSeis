package com.univalle.inventoryapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.univalle.inventoryapp.R
import com.univalle.inventoryapp.databinding.FragmentAuthenticationBinding
import com.univalle.inventoryapp.utils.Prefs
import com.univalle.inventoryapp.viewmodel.LoginViewModel
import java.util.concurrent.Executor

class AuthenticationFragment : Fragment() {

    private lateinit var binding: FragmentAuthenticationBinding
    private lateinit var viewModel: LoginViewModel

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_authentication, container, false
        )

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBiometricAuth()

        binding.lottieAnimationView.setOnClickListener {
            showBiometricPrompt()
        }

        controllerOverSystemBackButton()
    }

    private fun setupBiometricAuth() {
        executor = ContextCompat.getMainExecutor(requireContext())

        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    if (errorCode != BiometricPrompt.ERROR_USER_CANCELED) {
                        Toast.makeText(requireContext(),
                            "Error de autenticación: $errString", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)

                    Prefs.setLoggedIn(requireContext(), true)
                    navigateToHome()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autenticación con Biometría")
            .setSubtitle("Ingrese su huella digital")
            .setNegativeButtonText("Cancelar")
            .build()
    }

    private fun showBiometricPrompt() {
        val biometricManager = BiometricManager.from(requireContext())
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                biometricPrompt.authenticate(promptInfo)

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                Toast.makeText(requireContext(), "No hay hardware biométrico disponible", Toast.LENGTH_SHORT).show()

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Toast.makeText(requireContext(), "El hardware biométrico no está disponible actualmente", Toast.LENGTH_SHORT).show()

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                Toast.makeText(requireContext(), "No hay huellas digitales registradas", Toast.LENGTH_SHORT).show()

            else ->
                Toast.makeText(requireContext(), "Error biométrico desconocido", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToHome() {
        findNavController().navigate(R.id.action_authenticationFragment_to_homeInventoryFragment)
    }


    private fun controllerOverSystemBackButton() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().moveTaskToBack(true)
        }
    }
}