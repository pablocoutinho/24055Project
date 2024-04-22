package com.kharedji.onlineshopping.presentation.screens.signup.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.kharedji.onlineshopping.data.utils.State
import com.kharedji.onlineshopping.domain.models.UsersData
import com.kharedji.onlineshopping.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: UserRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(State<AuthResult>())
    val uiState: StateFlow<State<AuthResult>> = _uiState

    fun signUp(usersData: UsersData, password: String) {
        viewModelScope.launch {
            repository.signUpUser(usersData, password).onEach { state ->
                _uiState.emit(state)
            }.launchIn(viewModelScope)
        }
    }

    fun resetUiState() {
        viewModelScope.launch {
            _uiState.emit(State())
        }
    }
}