package com.lmar.budgetcalc.feature.presentation.budget_new_update.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lmar.budgetcalc.core.util.Utils
import com.lmar.budgetcalc.feature.domain.model.Material

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialCard(
    material: Material,
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit,
    onCardClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        //border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
        onClick = onCardClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp, horizontal = 20.dp)
        ) {
            Text(
                text = material.description,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Cantidad: " + material.quantity.toString())
                    Text(text = "Precio: S/ " + Utils.formatMoney(material.unitPrice))
                }

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "S/ " + Utils.formatMoney(material.subTotal),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Right,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = { onDeleteClick() },
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text(text = "Eliminar", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}