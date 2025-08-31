package com.abone.abonex.ui.features


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abone.abonex.data.local.TokenManager
import com.abone.abonex.data.remote.dto.RegisterRequest
import com.abone.abonex.domain.repository.AuthRepository
import com.abone.abonex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class RegisterUiState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val dateOfBirth: String? = null,
    val gender: String = "",
    val phoneNumber: String = "",
    val registrationStatus: Resource<String> = Resource.Idle()
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    fun onFirstNameChange(name: String) {
        _uiState.update { it.copy(firstName = name) }
    }
    fun onLastNameChange(name: String) {
        _uiState.update { it.copy(lastName = name) }
    }
    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email) }
    }
    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password) }
    }
    fun onPhoneNumberChange(phone: String) {
        _uiState.update { it.copy(phoneNumber = phone) }
    }
    fun onGenderChange(gender: String) {
        _uiState.update { it.copy(gender = gender) }
    }
    fun onDateOfBirthChange(millis: Long?) {
        millis?.let {
            // Eski API'larla uyumlu tarih formatlama
            val date = Date(it)
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dateString = formatter.format(date)
            _uiState.update { state -> state.copy(dateOfBirth = dateString) }
        }
    }

    fun registerUser() {
        viewModelScope.launch {
            _uiState.update { it.copy(registrationStatus = Resource.Loading()) }

            val currentState = _uiState.value
            val request = RegisterRequest(
                firstName = currentState.firstName.trim(),
                lastName = currentState.lastName.trim(),
                email = currentState.email.trim(),
                password = currentState.password,
                dateOfBirth = currentState.dateOfBirth!!,
                gender = currentState.gender,
                phoneNumber = currentState.phoneNumber.trim()
            )

            when (val result = authRepository.register(request)) {
                is Resource.Success -> {
                    result.data?.token?.let { token ->
                        tokenManager.saveToken(token)
                        _uiState.update { it.copy(registrationStatus = Resource.Success("Kayıt başarılı!")) }
                    }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(registrationStatus = Resource.Error(result.message)) }
                }
                else -> {}
            }
        }
    }
}
