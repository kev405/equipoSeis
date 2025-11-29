package com.univalle.inventoryapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.univalle.inventoryapp.repository.LoginRepository
import com.univalle.inventoryapp.viewmodel.LoginViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelUnitTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: LoginRepository

    private lateinit var viewModel: LoginViewModel

    @Mock
    private lateinit var task: Task<AuthResult>

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = LoginViewModel(repository)
    }

    @Test
    fun `validateForm with invalid password should set passwordError`() {
        viewModel.isButtonEnabled.observeForever { }
        viewModel.passwordError.observeForever { }

        viewModel.email.value = "test@example.com"
        viewModel.password.value = "12345"

        assertEquals("Mínimo 6 dígitos", viewModel.passwordError.value)
    }

    @Test
    fun `validateForm with valid inputs should not set passwordError`() {
        viewModel.isButtonEnabled.observeForever { }
        viewModel.passwordError.observeForever { }

        viewModel.email.value = "test@example.com"
        viewModel.password.value = "123456"

        assertEquals(null, viewModel.passwordError.value)
    }

    @Test
    fun `validateForm with valid inputs should enable button`() {
        viewModel.isButtonEnabled.observeForever { }

        viewModel.email.value = "test@example.com"
        viewModel.password.value = "123456"

        assertEquals(true, viewModel.isButtonEnabled.value)
    }

    @Test
    fun `onLoginClicked with successful login should post Success`() {
        val email = "test@example.com"
        val password = "123456"
        `when`(repository.login(email, password)).thenReturn(task)

        `when`(task.addOnCompleteListener(any())).thenAnswer {
            val listener = it.getArgument<OnCompleteListener<AuthResult>>(0)
            `when`(task.isSuccessful).thenReturn(true)
            listener.onComplete(task)
            task
        }

        viewModel.email.value = email
        viewModel.password.value = password
        viewModel.loginResult.observeForever { }

        viewModel.onLoginClicked()

        assertTrue(viewModel.loginResult.value is LoginViewModel.LoginResult.Success)
    }

    @Test
    fun `onLoginClicked with failed login should post Error`() {
        val email = "test@example.com"
        val password = "123456"
        `when`(repository.login(email, password)).thenReturn(task)

        `when`(task.addOnCompleteListener(any())).thenAnswer {
            val listener = it.getArgument<OnCompleteListener<AuthResult>>(0)
            `when`(task.isSuccessful).thenReturn(false)
            listener.onComplete(task)
            task
        }

        viewModel.email.value = email
        viewModel.password.value = password
        viewModel.loginResult.observeForever { }

        viewModel.onLoginClicked()

        val result = viewModel.loginResult.value
        assertTrue(result is LoginViewModel.LoginResult.Error)
        assertEquals("Login incorrecto", (result as LoginViewModel.LoginResult.Error).message)
    }

    @Test
    fun `onRegisterClicked with successful registration should post Success`() {
        val email = "test@example.com"
        val password = "123456"
        `when`(repository.register(email, password)).thenReturn(task)
        `when`(task.addOnCompleteListener(any())).thenAnswer {
            val listener = it.getArgument<OnCompleteListener<AuthResult>>(0)
            `when`(task.isSuccessful).thenReturn(true)
            listener.onComplete(task)
            task
        }

        viewModel.email.value = email
        viewModel.password.value = password
        viewModel.loginResult.observeForever { }

        viewModel.onRegisterClicked()

        assertTrue(viewModel.loginResult.value is LoginViewModel.LoginResult.Success)
    }

    @Test
    fun `onRegisterClicked with failed registration should post Error`() {
        val email = "test@example.com"
        val password = "123456"
        `when`(repository.register(email, password)).thenReturn(task)
        `when`(task.addOnCompleteListener(any())).thenAnswer {
            val listener = it.getArgument<OnCompleteListener<AuthResult>>(0)
            `when`(task.isSuccessful).thenReturn(false)
            listener.onComplete(task)
            task
        }

        viewModel.email.value = email
        viewModel.password.value = password
        viewModel.loginResult.observeForever { }

        viewModel.onRegisterClicked()

        val result = viewModel.loginResult.value
        assertTrue(result is LoginViewModel.LoginResult.Error)
        assertEquals("Error en el registro", (result as LoginViewModel.LoginResult.Error).message)
    }
}
