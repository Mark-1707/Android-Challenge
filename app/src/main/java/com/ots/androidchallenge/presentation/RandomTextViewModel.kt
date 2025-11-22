package com.ots.androidchallenge.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ots.androidchallenge.data.RandomTextEntity
import com.ots.androidchallenge.domain.RandomStringRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RandomTextViewModel @Inject constructor(
    private val repository: RandomStringRepository
): ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    val allStrings: StateFlow<List<RandomTextEntity>> =
        repository.observeAll().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


        fun generateRandomString(length: Int) {
            viewModelScope.launch(Dispatchers.IO) {
                _uiState.value = UiState.Loading
                try {
                    repository.generateRandomString(length)
                    _uiState.value = UiState.Success
                } catch (e: Exception) {
                    _uiState.value = UiState.Error(e.message ?: "Unknown error")
                }
            }
        }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun deleteById(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteById(id)
        }
    }

    fun clearUiState() {
        _uiState.value = UiState.Idle
    }
}

sealed interface UiState {
    object Idle : UiState
    object Loading : UiState
    object Success : UiState
    data class Error(val message: String) : UiState
}