package com.lmar.budgetcalc.core.util

import androidx.compose.ui.graphics.Color
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

object Utils {

    fun generateRandomColor(): Color {
        return Color(
            red = Random.nextFloat(),
            green = Random.nextFloat(),
            blue = Random.nextFloat(),
            alpha = 1f
        )
    }

    fun formatMoney(value: Double): String {
        val formatter = DecimalFormat("###,###,##0.00")
        return formatter.format(value);
        /*
        val country = "US"
        val lang = "en"
        return NumberFormat.getCurrencyInstance(Locale(lang, country)).format(value)*/
    }

    fun convertLongDate(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd/MM/yyyy")
        return format.format(date)
    }

    fun toInteger(s: String): Int {
        var value: Int = try {
            s.toInt()
        } catch (ex: NumberFormatException) {
            0
        }
        return value
    }

    fun toDouble(s: String): Double {
        var value: Double = try {
            s.toDouble()
        } catch (ex: NumberFormatException) {
            0.0
        }
        return value
    }
}