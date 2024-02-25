package com.lmar.budgetcalc.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.lmar.budgetcalc.data.UserDataUiEvents
import com.lmar.budgetcalc.data.UserInputScreenState

class UserInputViewModel: ViewModel() {
    var uiState = mutableStateOf(UserInputScreenState())

    fun onEvent(event: UserDataUiEvents) {
        when(event) {
            is UserDataUiEvents.UserNameEntered -> {
                uiState.value = uiState.value.copy(
                    nameEntered = event.name
                )
            }

            is UserDataUiEvents.AnimalSelected -> {
                uiState.value = uiState.value.copy(
                    animalSelected = event.animalValue
                )
            }
        }
    }
}