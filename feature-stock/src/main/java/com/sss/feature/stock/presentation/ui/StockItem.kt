package com.sss.feature.stock.presentation.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sss.core.util.CurrencyFormatter
import com.sss.feature.stock.R
import com.sss.feature.stock.domain.model.PriceChange
import com.sss.feature.stock.domain.model.Stock
import kotlinx.coroutines.delay
import java.math.BigDecimal

@Composable
fun StockItem(
    stock: Stock,
    modifier: Modifier = Modifier
) {
    var isFlashing by remember { mutableStateOf(false) }
    var lastPrice by remember { mutableStateOf(stock.price) }

    val normalPriceColor = MaterialTheme.colorScheme.onSurface
    val flashColor = when (stock.priceChange) {
        PriceChange.UP -> colorResource(R.color.flash_green)
        PriceChange.DOWN -> colorResource(R.color.flash_red)
        PriceChange.NEUTRAL -> normalPriceColor
    }

    val priceTextColor by animateColorAsState(
        targetValue = if (isFlashing) flashColor else normalPriceColor,
        animationSpec = tween(durationMillis = 300),
        label = "price_flash_animation"
    )

    val arrowColor = when (stock.priceChange) {
        PriceChange.UP -> colorResource(R.color.price_up)
        PriceChange.DOWN -> colorResource(R.color.price_down)
        PriceChange.NEUTRAL -> colorResource(R.color.price_neutral)
    }

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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stock.symbol,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = CurrencyFormatter.formatUsd(stock.price),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = priceTextColor
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = when (stock.priceChange) {
                    PriceChange.UP -> "↑"
                    PriceChange.DOWN -> "↓"
                    PriceChange.NEUTRAL -> "−"
                },
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = arrowColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StockItemPreview() {
    MaterialTheme {
        StockItem(
            stock = Stock(
                symbol = "AAPL",
                price = BigDecimal("175.50"),
                previousPrice = BigDecimal("170.00")
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StockItemPriceDownPreview() {
    MaterialTheme {
        StockItem(
            stock = Stock(
                symbol = "GOOG",
                price = BigDecimal("135.25"),
                previousPrice = BigDecimal("140.00")
            )
        )
    }
}
