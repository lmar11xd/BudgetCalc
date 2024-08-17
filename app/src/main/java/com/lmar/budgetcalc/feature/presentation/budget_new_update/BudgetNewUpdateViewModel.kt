package com.lmar.budgetcalc.feature.presentation.budget_new_update

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lmar.budgetcalc.core.util.BudgetNewUpdateStrings
import com.lmar.budgetcalc.core.util.ListStrings
import com.lmar.budgetcalc.core.util.Utils
import com.lmar.budgetcalc.feature.data.di.IoDispatcher
import com.lmar.budgetcalc.feature.domain.model.Material
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

    private var currentBudgetId: Int? = null

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
        object SaveBudget: UiEvent()
        object Back: UiEvent()
    }

    init {
        savedStateHandle.get<Int>("budgetId")?.let { id ->
            _state.value = _state.value.copy(
                isLoading = true
            )

            if(id != 0) {
                viewModelScope.launch {
                    budgetUseCases.getBudgetById(id)?.also { budget ->
                        currentBudgetId = id
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
                    if(currentBudgetId != null) {
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
                if(isValidFormMaterial()) {
                    viewModelScope.launch(dispatcher + errorHandler) {
                        try {
                            if(currentBudgetId == null) { //Crear
                                _state.value = _state.value.copy(
                                    budget = _state.value.budget.copy(
                                        total = 0.0,
                                        createdAt = System.currentTimeMillis(),
                                        modifiedAt = System.currentTimeMillis(),
                                        actived = true,
                                        id = null
                                    ),
                                    isLoading = true
                                )

                                budgetUseCases.addBudget(
                                    _state.value.budget
                                ).also { id ->
                                    _state.value = _state.value.copy(
                                        isLoading = false
                                    )
                                    currentBudgetId = id.toInt()
                                    guardarMaterial(id.toInt()).also {
                                        calculateTotal(currentBudgetId!!)
                                        getMaterials(currentBudgetId!!)
                                    }
                                }
                            } else { //Actualizar
                                currentBudgetId?.let {
                                    guardarMaterial(it).also {
                                        calculateTotal(currentBudgetId!!)
                                        getMaterials(currentBudgetId!!)
                                    }
                                }
                            }
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

            is BudgetNewUpdateEvent.DeleteMaterial -> {
                viewModelScope.launch(dispatcher + errorHandler) {
                    if(event.material.id != null) {
                        materialUseCases.deleteMaterial(event.material)
                    }
                    currentBudgetId?.let {
                        calculateTotal(it)
                        getMaterials(it)
                    }
                }
            }

            BudgetNewUpdateEvent.Save -> {
                viewModelScope.launch(dispatcher + errorHandler) {
                    if(currentBudgetId != null) {
                        Log.e("XYZ", "Actualizar Titulo")
                        budgetUseCases.updateBudget(
                            _state.value.budget.copy(
                                modifiedAt = System.currentTimeMillis()
                            )
                        )
                    }
                }
            }
        }
    }

    private fun getMaterials(budgetId: Int) {
        _state.value = _state.value.copy(
            isLoading = true
        )

        getMaterialsJob?.cancel()

        getMaterialsJob = viewModelScope.launch(dispatcher + errorHandler) {
            when(val result = materialUseCases.getMaterials(budgetId)) {
                is MaterialUseCaseResult.Error -> {
                    _state.value = _state.value.copy(
                        error = ListStrings.CANT_GET_MATERIALS,
                        isLoading = false
                    )
                }

                is MaterialUseCaseResult.Success -> {
                    _state.value = _state.value.copy(
                        materials = result.materials,
                        isLoading = false
                    )
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
        return !(_state.value.unitPriceMaterial.isBlank() || unitPriceDouble <= 0)
    }

    private suspend fun guardarMaterial(budgetId: Int) {
        val description = _state.value.descriptionMaterial
        val quantity = Utils.toInteger(_state.value.quantityMaterial)
        val unitPrice = Utils.toDouble(_state.value.unitPriceMaterial)
        val subTotal = quantity * unitPrice

        materialUseCases.addMaterial(Material(
            description = description,
            quantity = quantity,
            unitPrice = unitPrice,
            subTotal = subTotal,
            createdAt = System.currentTimeMillis(),
            modifiedAt = System.currentTimeMillis(),
            actived = true,
            budgetId = budgetId,
            id = null
        ))

        cleanFieldMaterialDialog()
    }

    private suspend fun calculateTotal(budgetId: Int) {
        materialUseCases.getMaterialsByBudget(budgetId).also { materials ->
            val total = materials.sumOf { material -> material.subTotal }
            budgetUseCases.updateBudget(
                _state.value.budget.copy(
                    id = budgetId,
                    total = total,
                    modifiedAt = System.currentTimeMillis()
                )
            )
        }
    }

    fun cleanFieldMaterialDialog() {
        _state.value = _state.value.copy(
            descriptionMaterial = "",
            quantityMaterial = "",
            unitPriceMaterial = ""
        )
    }
}