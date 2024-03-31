package com.lmar.budgetcalc.feature.presentation.budget_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lmar.budgetcalc.core.util.ListStrings
import com.lmar.budgetcalc.feature.data.di.IoDispatcher
import com.lmar.budgetcalc.feature.domain.model.Budget
import com.lmar.budgetcalc.feature.domain.use_cases.BudgetUseCaseResult
import com.lmar.budgetcalc.feature.domain.use_cases.BudgetUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetListViewModel @Inject constructor(
    private val budgetUseCases: BudgetUseCases,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
): ViewModel() {
    private val _state = mutableStateOf(BudgetListState())
    val state: State<BudgetListState> = _state

    private var undoBudget: Budget? = null
    private var getBudgetsJob: Job? = null
    private val errorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.value = _state.value.copy(
            error = e.message,
            isLoading = false
        )
    }

    fun onEvent(event: BudgetListEvent) {
        when(event) {
            is BudgetListEvent.Delete -> {
                viewModelScope.launch(dispatcher + errorHandler) {
                    budgetUseCases.deleteBudget(event.budget)
                    getBudgets()
                    undoBudget = event.budget
                }
            }

            BudgetListEvent.UndoDelete -> {
                viewModelScope.launch(dispatcher + errorHandler) {
                    budgetUseCases.addBudget(undoBudget ?: return@launch)
                    undoBudget = null
                    getBudgets()
                }
            }
        }
    }

    fun getBudgets() {
        _state.value = _state.value.copy(
            isLoading = true
        )

        getBudgetsJob?.cancel()

        getBudgetsJob = viewModelScope.launch(dispatcher + errorHandler) {
            when(val result = budgetUseCases.getBudgets()) {
                is BudgetUseCaseResult.Error -> {
                    _state.value = _state.value.copy(
                        error = ListStrings.CANT_GET_BUDGETS,
                        isLoading = false
                    )
                }

                is BudgetUseCaseResult.Success -> {
                    _state.value = _state.value.copy(
                        budgets = result.budgets,
                        isLoading = false
                    )
                }
            }
        }

    }
}