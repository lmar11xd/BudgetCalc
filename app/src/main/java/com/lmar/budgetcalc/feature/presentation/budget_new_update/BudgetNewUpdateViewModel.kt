package com.lmar.budgetcalc.feature.presentation.budget_new_update

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lmar.budgetcalc.core.util.BudgetNewUpdateStrings
import com.lmar.budgetcalc.feature.data.di.IoDispatcher
import com.lmar.budgetcalc.feature.domain.use_cases.BudgetUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetNewUpdateViewModel @Inject constructor (
    private val budgetUseCases: BudgetUseCases,
    savedStateHandle: SavedStateHandle,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
): ViewModel() {
    private val _state = mutableStateOf(BudgetNewUpdateState())
    val state: State<BudgetNewUpdateState> = _state

    private val errorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.value = _state.value.copy(
            error = e.message,
            isLoading = false
        )
    }

    private var currentTodoId: Int? = null

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
        object SaveBudget: UiEvent()
        object Back: UiEvent()
    }

    init {
        savedStateHandle.get<Int>("budgetId")?.let { id ->
            if(id != 0) {
                viewModelScope.launch {
                    budgetUseCases.getBudgetById(id)?.also { budget ->
                        currentTodoId = id
                        _state.value = _state.value.copy(
                            budget = budget,
                            isLoading = false,
                            isTitleHintVisible = budget.title.isBlank(),
                        )
                    }
                }
            } else {
                _state.value = _state.value.copy(
                    isLoading = false
                )
            }
        }
    }

    fun onEvent(event: BudgetNewUpdateEvent) {
        when(event) {
            BudgetNewUpdateEvent.Back -> {
                viewModelScope.launch(dispatcher + errorHandler) {
                    _eventFlow.emit(UiEvent.Back)
                }
            }
            is BudgetNewUpdateEvent.ChangedTitleFocus -> {
                val shouldTitleHintBeVisible = !event.focusState.isFocused && _state.value.budget.title.isBlank()
                _state.value = _state.value.copy(
                    isTitleHintVisible = shouldTitleHintBeVisible
                )
            }
            BudgetNewUpdateEvent.Delete -> {
                viewModelScope.launch(dispatcher + errorHandler) {
                    if(currentTodoId != null) {
                        budgetUseCases.deleteBudget(_state.value.budget)
                    }
                    _eventFlow.emit(UiEvent.Back)
                }
            }
            is BudgetNewUpdateEvent.EnteredTitle -> {
                _state.value = _state.value.copy(
                    budget = _state.value.budget.copy(
                        title = event.value
                    )
                )
            }
            BudgetNewUpdateEvent.SaveTodo -> {
                viewModelScope.launch(dispatcher + errorHandler) {
                    try {
                        if(currentTodoId != null) {
                            budgetUseCases.updateBudget(_state.value.budget)
                        } else {
                            budgetUseCases.addBudget(_state.value.budget.copy(
                                createdAt = System.currentTimeMillis(),
                                modifiedAt = System.currentTimeMillis(),
                                actived = true,
                                id = null
                            ))
                        }
                        _eventFlow.emit(UiEvent.SaveBudget)
                    } catch (e: Exception) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: BudgetNewUpdateStrings.SAVE_BUDGET_ERROR
                            )
                        )
                    }
                }
            }
        }
    }
}