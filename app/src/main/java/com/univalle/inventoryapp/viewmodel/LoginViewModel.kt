package com.univalle.inventoryapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.univalle.inventoryapp.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository
) : ViewModel() {

    val email = MutableLiveData<String>("")
    val password = MutableLiveData<String>("")

    private val _passwordError = MutableLiveData<String?>()
    val passwordError: LiveData<String?> get() = _passwordError

    val isButtonEnabled = MediatorLiveData<Boolean>().apply {
        addSource(email) { validateForm() }
        addSource(password) { validateForm() }
    }

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> get() = _loginResult

    private fun validateForm() {
        val emailInput = email.value ?: ""
        val passInput = password.value ?: ""

        if (passInput.isNotEmpty() && passInput.length < 6) {
            _passwordError.value = "Mínimo 6 dígitos"
        } else {
            _passwordError.value = null
        }

        val isEmailValid = emailInput.isNotEmpty()
        val isPassValid = passInput.length in 6..10

        isButtonEnabled.value = isEmailValid && isPassValid
    }

    fun onLoginClicked() {
        val emailInput = email.value!!
        val passInput = password.value!!

        repository.login(emailInput, passInput)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _loginResult.value = LoginResult.Success
                } else {
                    _loginResult.value = LoginResult.Error("Login incorrecto")
                }
            }
    }

    fun onRegisterClicked() {
        val emailInput = email.value!!
        val passInput = password.value!!

        repository.register(emailInput, passInput)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _loginResult.value = LoginResult.Success
                } else {
                    _loginResult.value = LoginResult.Error("Error en el registro")
                }
            }
    }

    sealed class LoginResult {
        object Success : LoginResult()
        data class Error(val message: String) : LoginResult()
    }
}