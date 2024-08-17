package com.lmar.budgetcalc.feature.presentation.budget_new_update

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lmar.budgetcalc.core.presentation.HintTextField
import com.lmar.budgetcalc.core.util.BudgetNewUpdateStrings
import com.lmar.budgetcalc.core.util.ContentDescriptions
import com.lmar.budgetcalc.feature.presentation.budget_new_update.components.MaterialCard
import com.lmar.budgetcalc.feature.presentation.budget_new_update.components.MaterialDialog
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetNewUpdateScreen (
    navController: NavController,
    viewModel: BudgetNewUpdateViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val snackbarHostState = remember { SnackbarHostState() }

    val configuration = LocalConfiguration.current
    val isPortrait =
        configuration.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT

    val topBarHeight = if(isPortrait) 64.dp else 0.dp
    val horizontalPadding = 16.dp
    val verticalPadding = if(isPortrait) 16.dp else 2.dp

    LaunchedEffect(key1 = true ) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                BudgetNewUpdateViewModel.UiEvent.Back -> {
                    navController.navigateUp()
                }
                BudgetNewUpdateViewModel.UiEvent.SaveBudget -> {
                    navController.navigateUp()
                }
                is BudgetNewUpdateViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(message = event.message)
                }
            }
        }
    }

    DisposableEffect(key1 = true) {
        onDispose {
            viewModel.onEvent(BudgetNewUpdateEvent.Save)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(BudgetNewUpdateEvent.ChangeShowMaterialDialog(true))
                },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = ContentDescriptions.ADD_MATERIAL,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            viewModel.onEvent(BudgetNewUpdateEvent.Back)
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = ContentDescriptions.BACK,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                actions = {
                },
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
                    rememberTopAppBarState()
                )
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {
        Box(
            contentAlignment = Alignment.TopStart,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = horizontalPadding,
                    vertical = verticalPadding
                )
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .padding(top = topBarHeight)
                    .fillMaxSize()
            ) {
                HintTextField(
                    value = state.budget.title,
                    hint = BudgetNewUpdateStrings.HINT_TITLE,
                    onValueChange = {
                        viewModel.onEvent(BudgetNewUpdateEvent.EnteredTitle(it))
                    },
                    onFocusChange = {
                        viewModel.onEvent(BudgetNewUpdateEvent.ChangedTitleFocus(it))
                    },
                    isHintVisible = state.isTitleHintVisible,
                    singleLine = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                )

                Spacer(Modifier.height(30.dp))

                Text(
                    text = ContentDescriptions.MATERIALS,
                    fontSize = 24.sp,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(state.materials) {material ->
                        MaterialCard(
                            material = material,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp),
                            onDeleteClick = {
                                viewModel.onEvent(BudgetNewUpdateEvent.DeleteMaterial(material))
                            },
                            onCardClick = {}
                        )
                    }
                }

            }
        }
    }

    MaterialDialog(
        show = state.showMaterialDialog,
        viewModel = viewModel,
        onClickAgregar = {
            viewModel.onEvent(BudgetNewUpdateEvent.ChangeShowMaterialDialog(false))
            viewModel.onEvent(BudgetNewUpdateEvent.AddMaterial)
        },
        onClickCancelar = {
            viewModel.onEvent(BudgetNewUpdateEvent.ChangeShowMaterialDialog(false))
            viewModel.cleanFieldMaterialDialog()
        }
    )
}