package com.example.sampleapp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

/**
 * A ViewModel for handling state in our application.
 */
class MainViewModel : ViewModel() {
    private val _buttonState = MutableStateFlow(ButtonState.Idle)
    private val _requestState = MutableStateFlow(RequestState.Pending)
    val buttonState: StateFlow<ButtonState> = _buttonState.asStateFlow()
    val requestState: StateFlow<RequestState> = _requestState.asStateFlow()

    /**
     * Updates the ButtonState to a specified value.
     */
    fun updateButtonState(newButtonState: ButtonState) {
        _buttonState.update {
            newButtonState
        }
    }

    /**
     * Updates the RequestState to a specified value.
     */
    fun updateRequestState(newRequestState: RequestState) {
        _requestState.update {
            newRequestState
        }
    }

    /**
     * Executes a mock request and randomly updates the request state.
     */
    suspend fun executeMockRequest() {
        updateButtonState(ButtonState.Loading)
        updateRequestState(RequestState.Loading)
        delay(3000)
        val successful = Random.nextBoolean()
        if(successful) {
            updateRequestState(RequestState.Success)
        } else {
            updateRequestState(RequestState.Error)
        }

        updateButtonState(ButtonState.Idle)

    }
}

/**
 * An enum representing all possible states of the button.
 */
enum class ButtonState(val title: String) {
    Idle("Idle"), Loading("Loading")
}

/**
 * An enum representing all possible states of the mock request.
 */
enum class RequestState(val title: String) {
    Pending("Pending"), Loading("Loading"), Error("Error"), Success("Success")
}