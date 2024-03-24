package com.lmar.budgetcalc.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lmar.budgetcalc.R
import com.lmar.budgetcalc.feature.data.UserDataUiEvents
import com.lmar.budgetcalc.core.presentation.AnimalCard
import com.lmar.budgetcalc.core.presentation.ButtonComponent
import com.lmar.budgetcalc.core.presentation.TextComponent
import com.lmar.budgetcalc.core.presentation.TextFieldComponent
import com.lmar.budgetcalc.core.presentation.TopBar
import com.lmar.budgetcalc.ui.UserInputViewModel

@Composable
fun UserInputScreen(
    userInputViewModel: UserInputViewModel,
    showWelcomeScreen: (valuesPair: Pair<String, String>) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            TopBar("Hi there \uD83D\uDE0A")

            TextComponent(
                textValue = "Let's learn about you!",
                textSize = 24.sp
            )

            Spacer(modifier = Modifier.size(20.dp))

            TextComponent(
                textValue = "This app will prepare a details page based on input provided by you!",
                textSize = 18.sp
            )

            Spacer(modifier = Modifier.size(60.dp))

            TextComponent(textValue = "Name", textSize = 18.sp)
            Spacer(modifier = Modifier.size(10.dp))
            TextFieldComponent(onTextChanged = {
                userInputViewModel.onEvent(
                    UserDataUiEvents.UserNameEntered(it)
                )
            })

            Spacer(modifier = Modifier.size(20.dp))

            TextComponent(
                textValue = "What do you like?",
                textSize = 18.sp
            )

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                AnimalCard(
                    image = R.drawable.cat,
                    animalSelected = {
                        userInputViewModel.onEvent(
                            UserDataUiEvents.AnimalSelected(it)
                        )
                    },
                    selected = userInputViewModel.uiState.value.animalSelected == "Cat"
                )

                AnimalCard(
                    image = R.drawable.dog,
                    animalSelected = {
                        userInputViewModel.onEvent(
                            UserDataUiEvents.AnimalSelected(it)
                        )
                    },
                    selected = userInputViewModel.uiState.value.animalSelected == "Dog"
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            if (userInputViewModel.isValidState()) {
                ButtonComponent(
                    goToDetailsScreen = {
                        println("============Coming Here!")
                        println("============${userInputViewModel.uiState.value.nameEntered} and ${userInputViewModel.uiState.value.animalSelected}")
                        showWelcomeScreen(
                            Pair(
                                userInputViewModel.uiState.value.nameEntered,
                                userInputViewModel.uiState.value.animalSelected
                            )
                        )
                    }
                )
            }

        }
    }
}

@Preview
@Composable
fun UserInputScreenPreview() {
    UserInputScreen(UserInputViewModel()) {

    }
}