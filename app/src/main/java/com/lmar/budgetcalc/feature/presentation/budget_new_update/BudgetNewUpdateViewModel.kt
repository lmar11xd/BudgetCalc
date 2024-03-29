package com.lmar.budgetcalc.feature.presentation.budget_new_update

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lmar.budgetcalc.core.util.BudgetListStrings
import com.lmar.budgetcalc.core.util.BudgetNewUpdateStrings
import com.lmar.budgetcalc.core.util.Utils
import com.lmar.budgetcalc.feature.data.di.IoDispatcher
import com.lmar.budgetcalc.feature.domain.model.Material
import com.lmar.budgetcalc.feature.domain.use_cases.BudgetUseCaseResult
import com.lmar.budgetcalc.feature.domain.use_cases.BudgetUseCases
import com.lmar.budgetcalc.feature.domain.use_cases.MaterialUseCaseResult
import com.lmar.budgetcalc.feature.domain.use_cases.MaterialUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetNewUpdateViewModel @Inject constructor (
    private val budgetUseCases: BudgetUseCases,
    private val materialUseCases: MaterialUseCases,
    savedStateHandle: SavedStateHandle,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
): ViewModel() {
    private val _state = mutableStateOf(BudgetNewUpdateState())
    val state: State<BudgetNewUpdateState> = _state

    private var getMaterialsJob: Job? = null

    private val errorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.value = _state.value.copy(
            error = e.message,
            isLoading = false
        )
    }

    private var currentTodoId: Int? = null
    private var materialsTemp = mutableListOf<Material>()

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
                        getMaterials(id)
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
            is BudgetNewUpdateEvent.ChangeShowMaterialDialog -> {
                _state.value = _state.value.copy(
                    showMaterialDialog = event.show
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
            BudgetNewUpdateEvent.Save -> {
                viewModelScope.launch(dispatcher + errorHandler) {
                    try {
                        if(currentTodoId != null) {
                            budgetUseCases.updateBudget(_state.value.budget)
                        } else {
                            budgetUseCases.addBudget(
                                _state.value.budget.copy(
                                    createdAt = System.currentTimeMillis(),
                                    modifiedAt = System.currentTimeMillis(),
                                    actived = true,
                                    id = null
                                )
                            ).also {
                                materialsTemp.map { material -> material.budgetId = it.toInt() }
                                materialUseCases.addAllMaterials(materialsTemp)
                            }
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

            is BudgetNewUpdateEvent.EnteredDescriptionMaterial -> {
                _state.value = _state.value.copy(
                    descriptionMaterial = event.value
                )
            }
            is BudgetNewUpdateEvent.EnteredQuantityMaterial -> {
                _state.value = _state.value.copy(
                    quantityMaterial = event.value
                )
            }
            is BudgetNewUpdateEvent.EnteredUnitPriceMaterial -> {
                _state.value = _state.value.copy(
                    unitPriceMaterial = event.value
                )
            }

            BudgetNewUpdateEvent.AddMaterial -> {
                val description = _state.value.descriptionMaterial
                val quantity = _state.value.quantityMaterial
                val unitPrice = _state.value.unitPriceMaterial

                if(isValidFormMaterial()) {
                    addMaterialTemp(
                        Material(
                            description = description,
                            quantity = Utils.toInteger(quantity),
                            unitPrice = Utils.toDouble(unitPrice),
                            subTotal = 0.0,
                            createdAt = System.currentTimeMillis(),
                            modifiedAt = System.currentTimeMillis(),
                            actived = true,
                            budgetId = 0,
                            id = null
                        )
                    )
                    _state.value = _state.value.copy(
                        materials = materialsTemp
                    )
                }
                cleanFieldMaterialDialog()
            }
        }
    }

    fun getMaterials(budgetId: Int) {
        getMaterialsJob?.cancel()

        getMaterialsJob = viewModelScope.launch(dispatcher + errorHandler) {
            when(val result = materialUseCases.getMaterials(budgetId)) {
                is MaterialUseCaseResult.Error -> {
                    _state.value = _state.value.copy(
                        error = BudgetListStrings.CANT_GET_BUDGETS,
                        isLoading = false
                    )
                }

                is MaterialUseCaseResult.Success -> {
                    _state.value = _state.value.copy(
                        materials = result.materials,
                        isLoading = false
                    )
                    result.materials.map { material -> materialsTemp.add(material) }
                }
            }
        }
    }

    fun isValidFormMaterial(): Boolean {
        if (_state.value.descriptionMaterial.isBlank()) {
            return false
        }

        val quantityInt = Utils.toInteger(_state.value.quantityMaterial)
        if (_state.value.quantityMaterial.isBlank() || quantityInt <= 0) {
            return false
        }

        val unitPriceDouble = Utils.toDouble(_state.value.unitPriceMaterial)
        if (_state.value.unitPriceMaterial.isBlank() || unitPriceDouble <= 0) {
            return false
        }

        return true
    }

    private fun addMaterialTemp(material: Material) {
        materialsTemp.add(material)
    }

    fun getMaterialsTemp() = materialsTemp

    fun cleanFieldMaterialDialog() {
        _state.value = _state.value.copy(
            descriptionMaterial = "",
            quantityMaterial = "",
            unitPriceMaterial = ""
        )
    }
}