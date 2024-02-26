package com.lmar.budgetcalc.ui

import androidx.lifecycle.ViewModel

class FactsViewModel: ViewModel() {

    fun generateRandomFact(selectedAnimal: String): String {
        return if (selectedAnimal == "Dog") {
            getDogFacts().random()
        } else {
            getCatFacts().random()
        }
    }

    fun getDogFacts(): List<String> {
        val facts = listOf(
            "Dogs believe that knocking things off tables is a form of interior decorating.",
            "If dogs had a motto, it would be 'Napping is life'",
            "Dogs considering cardboard boxes to be 5-star accommodations"
        )
        return facts
    }

    fun getCatFacts(): List<String> {
        val facts = listOf(
            "Cats believe that knocking things off tables is a form of interior decorating.",
            "If cats had a motto, it would be 'Napping is life'",
            "Cats considering cardboard boxes to be 5-star accommodations"
        )
        return facts
    }
}