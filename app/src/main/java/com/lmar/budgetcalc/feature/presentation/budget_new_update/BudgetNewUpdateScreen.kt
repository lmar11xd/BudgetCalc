package com.lmar.budgetcalc.feature.presentation.budget_new_update

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lmar.budgetcalc.core.presentation.HintTextField
import com.lmar.budgetcalc.core.util.BudgetNewUpdateStrings
import com.lmar.budgetcalc.core.util.ContentDescriptions
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
    val scope = rememberCoroutineScope()

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

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(BudgetNewUpdateEvent.SaveTodo)
                },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = ContentDescriptions.SAVE_BUDGET,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        topBar = {
            TopAppBar(
                title = {  },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            viewModel.onEvent(BudgetNewUpdateEvent.Back)
                        },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = ContentDescriptions.BACK,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
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
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .padding(top = topBarHeight)
                    .fillMaxSize()
            ) {
                HintTextField(
                    text = state.budget.title,
                    hint = BudgetNewUpdateStrings.HINT_TITLE,
                    textColor = MaterialTheme.colorScheme.secondary,
                    onValueChange = {
                        viewModel.onEvent(BudgetNewUpdateEvent.EnteredTitle(it))
                    },
                    onFocusChange = {
                        viewModel.onEvent(BudgetNewUpdateEvent.ChangedTitleFocus(it))
                    },
                    isHintVisible = state.isTitleHintVisible,
                    singleLine = false,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(
                            horizontal = horizontalPadding,
                            vertical = verticalPadding
                        )
                )
            }
        }
    }
}