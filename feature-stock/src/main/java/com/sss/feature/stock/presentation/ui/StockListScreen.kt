package com.sss.feature.stock.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sss.feature.stock.R
import com.sss.feature.stock.presentation.viewmodel.StockIntent
import com.sss.feature.stock.presentation.viewmodel.StockViewModel

@Composable
fun StockListScreen(
    viewModel: StockViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ConnectionStatusBar(
            isConnected = uiState.isConnected,
            isFeedActive = uiState.isFeedActive,
            onToggle = { viewModel.processIntent(StockIntent.ToggleFeed) }
        )

        when {
            uiState.isLoading && uiState.stocks.isEmpty() -> {
                LoadingContent()
            }
            uiState.error != null && uiState.stocks.isEmpty() -> {
                ErrorContent(
                    error = uiState.error!!,
                    onRetry = { viewModel.processIntent(StockIntent.StartFeed) }
                )
            }
            !uiState.isFeedActive && uiState.stocks.isEmpty() -> {
                EmptyContent(
                    onStart = { viewModel.processIntent(StockIntent.StartFeed) }
                )
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    item { Spacer(modifier = Modifier.height(8.dp)) }

                    items(
                        items = uiState.stocks,
                        key = { it.symbol }
                    ) { stock ->
                        StockItem(stock = stock)
                    }

                    item { Spacer(modifier = Modifier.height(8.dp)) }
                }
            }
        }
    }
}

@Composable
private fun ConnectionStatusBar(
    isConnected: Boolean,
    isFeedActive: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(
                    if (isConnected) colorResource(R.color.connection_active)
                    else colorResource(R.color.connection_inactive)
                )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(
                if (isConnected) R.string.status_connected else R.string.status_disconnected
            ),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Button(
            onClick = onToggle,
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(
                    if (isFeedActive) R.color.price_down else R.color.price_up
                )
            )
        ) {
            Text(
                text = stringResource(
                    if (isFeedActive) R.string.action_stop else R.string.action_start
                )
            )
        }
    }
}

@Composable
private fun LoadingContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.connecting),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun ErrorContent(
    error: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.error_occurred),
            style = MaterialTheme.typography.titleMedium,
            color = colorResource(R.color.price_down)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = error,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text(text = stringResource(R.string.action_retry))
        }
    }
}

@Composable
private fun EmptyContent(
    onStart: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.welcome_message),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.start_tracking_hint),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onStart) {
            Text(text = stringResource(R.string.action_start_tracking))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ConnectionStatusBarConnectedPreview() {
    MaterialTheme {
        ConnectionStatusBar(
            isConnected = true,
            isFeedActive = true,
            onToggle = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ConnectionStatusBarDisconnectedPreview() {
    MaterialTheme {
        ConnectionStatusBar(
            isConnected = false,
            isFeedActive = false,
            onToggle = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingContentPreview() {
    MaterialTheme {
        LoadingContent()
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorContentPreview() {
    MaterialTheme {
        ErrorContent(
            error = "Connection failed. Please check your internet connection.",
            onRetry = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyContentPreview() {
    MaterialTheme {
        EmptyContent(onStart = {})
    }
}
