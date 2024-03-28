package com.lmar.budgetcalc.feature.presentation.budget_new_update.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.lmar.budgetcalc.core.presentation.BorderTextField
import com.lmar.budgetcalc.core.util.ContentDescriptions
import com.lmar.budgetcalc.feature.presentation.budget_new_update.BudgetNewUpdateEvent
import com.lmar.budgetcalc.feature.presentation.budget_new_update.BudgetNewUpdateViewModel

@Composable
fun MaterialDialog(
    show: Boolean,
    viewModel: BudgetNewUpdateViewModel,
    onClickAgregar: () -> Unit,
    onClickCancelar: () -> Unit
) {
    if(show) {
        Dialog(
            onDismissRequest = {  },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.background
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Column(modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = ContentDescriptions.MATERIAL,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        BorderTextField(
                            value = viewModel.state.value.descriptionMaterial,
                            title = "Descripción",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            onValueChange = {
                                viewModel.onEvent(BudgetNewUpdateEvent.EnteredDescriptionMaterial(it))
                            },
                            onFocusChange = {}
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        BorderTextField(
                            value = viewModel.state.value.quantityMaterial,
                            title = "Cantidad",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            onValueChange = {
                                viewModel.onEvent(BudgetNewUpdateEvent.EnteredQuantityMaterial(it))
                            },
                            onFocusChange = {}
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        BorderTextField(
                            value = viewModel.state.value.unitPriceMaterial,
                            title = "Precio Unitario",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            onValueChange = {
                                viewModel.onEvent(BudgetNewUpdateEvent.EnteredUnitPriceMaterial(it))
                            },
                            onFocusChange = {}
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            OutlinedButton(onClick = { onClickCancelar() }) {
                                Text(text = ContentDescriptions.CANCEL)
                            }
                            Button(
                                onClick = { onClickAgregar() },
                                enabled = viewModel.isValidFormMaterial(),
                            ) {
                                Text(text = ContentDescriptions.ADD)
                            }
                        }
                    }
                }
            }
        }
    }
}