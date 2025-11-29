package com.sss.feature.stock.presentation.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sss.feature.stock.domain.model.PriceChange
import com.sss.feature.stock.domain.model.Stock
import kotlinx.coroutines.delay

@Composable
fun StockItem(
    stock: Stock,
    modifier: Modifier = Modifier
) {
    var isFlashing by remember { mutableStateOf(false) }
    var lastPrice by remember { mutableStateOf(stock.price) }

    val flashColor = when (stock.priceChange) {
        PriceChange.UP -> Color(0x334CAF50)
        PriceChange.DOWN -> Color(0x33F44336)
        PriceChange.NEUTRAL -> Color.Transparent
    }

    val backgroundColor by animateColorAsState(
        targetValue = if (isFlashing) flashColor else Color.Transparent,
        animationSpec = tween(durationMillis = 300),
        label = "flash_animation"
    )

    LaunchedEffect(stock.price) {
        if (stock.price != lastPrice && stock.priceChange != PriceChange.NEUTRAL) {
            isFlashing = true
            delay(1000)
            isFlashing = false
        }
        lastPrice = stock.price
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stock.symbol,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "$${String.format("%.2f", stock.price)}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = when (stock.priceChange) {
                            PriceChange.UP -> "↑"
                            PriceChange.DOWN -> "↓"
                            PriceChange.NEUTRAL -> "−"
                        },
                        style = MaterialTheme.typography.titleLarge,
                        color = when (stock.priceChange) {
                            PriceChange.UP -> Color(0xFF4CAF50)
                            PriceChange.DOWN -> Color(0xFFF44336)
                            PriceChange.NEUTRAL -> Color(0xFF9E9E9E)
                        }
                    )
                }
            }
        }
    }
}
