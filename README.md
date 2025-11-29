# Real-Time Price Tracker App

An Android application built with Jetpack Compose that displays real-time price updates for 25 stock symbols using WebSocket connection.

## Features

- Real-time price tracking for 25 stock symbols
- WebSocket integration with echo server
- Price change indicators (green ↑ / red ↓)
- Price flash animation on updates
- Sorted list by price (highest at top)
- Start/Stop price feed toggle
- Connection status indicator
- Light/Dark theme support
- Arabic/English localization
- Portrait and Landscape orientation support

## Architecture

This project follows **MVI (Model-View-Intent)** architecture with **Clean Architecture** principles.

### Multi-Module Structure

```
trading-app/
├── app/                    # Main application module
├── core/                   # Shared utilities (WebSocket, DI)
└── feature-stock/          # Stock tracking feature
    ├── data/               # DTOs, Repository impl, WebSocket service
    ├── domain/             # Models, Repository interface, Use cases
    └── presentation/       # ViewModel, UI State, Compose UI
```

### MVI Pattern

```
User Action → Intent → ViewModel → State Update → UI Recomposition
```

- **Model**: `StockUiState` - Single immutable state object
- **View**: `StockListScreen` - Composable observing state
- **Intent**: `StockIntent` - Sealed class representing user actions

### Key Components

| Component | Purpose |
|-----------|---------|
| `WebSocketClient` | OkHttp-based WebSocket wrapper |
| `StockWebSocketService` | Manages price generation and WebSocket communication |
| `StockRepository` | Abstracts data layer, tracks price changes |
| `StockViewModel` | Processes intents, manages UI state |
| `StockListScreen` | Displays stock list with LazyColumn |

## Tech Stack

- **UI**: Jetpack Compose (100%)
- **Architecture**: MVI + Clean Architecture
- **DI**: Hilt
- **Async**: Kotlin Coroutines + Flow + StateFlow
- **WebSocket**: OkHttp
- **Serialization**: Kotlinx Serialization
- **Persistence**: DataStore
- **Testing**: JUnit, MockK, Turbine

## How to Run

### Prerequisites

- Android Studio Hedgehog or newer
- JDK 17
- Android SDK 35

### Steps

1. Clone the repository
   ```bash
   git clone https://github.com/yourusername/trading-app.git
   ```

2. Open in Android Studio

3. Sync Gradle files

4. Run on emulator or device (minSdk 24)

## Running Tests

```bash
# Run all unit tests
./gradlew test

# Run feature-stock module tests
./gradlew :feature-stock:testDebugUnitTest
```

### Test Coverage

| Test Class | Coverage |
|------------|----------|
| `StockViewModelTest` | ViewModel MVI logic, state updates |
| `StockRepositoryImplTest` | Repository data handling, sorting |
| `StockTest` | Domain model price change logic |

## Assumptions & Trade-offs

### Assumptions

1. **Echo Server**: Using `wss://ws.postman-echo.com/raw` which echoes back sent messages
2. **Price Generation**: Random prices generated client-side (±5% of base price)
3. **Update Interval**: Fixed 2-second interval for all symbols

### Trade-offs

1. **Multi-module vs Single module**: Chose multi-module for scalability demonstration, though single module would suffice for this scope
2. **MVI vs MVVM**: Chose MVI for unidirectional data flow and predictable state management
3. **Unit tests vs UI tests**: Prioritized unit tests for business logic; UI tests would be next step for production

## Bonus Features Implemented

- [x] Price flash animation (green/red for 1 second on change)
- [x] Unit tests (ViewModel, Repository, Domain model)
- [x] Light and Dark theme support
- [x] Additional: Arabic/English localization with RTL support
- [x] Additional: Portrait and Landscape orientation support (state preserved on rotation)

## Stock Symbols (25)

AAPL, GOOG, TSLA, AMZN, MSFT, NVDA, META, NFLX, AMD, INTC, CRM, ORCL, CSCO, ADBE, PYPL, UBER, LYFT, SNAP, TWTR, SPOT, ZM, SHOP, SQ, COIN, HOOD

## WebSocket Flow

1. Connect to `wss://ws.postman-echo.com/raw`
2. Every 2 seconds, for each symbol:
   - Generate random price (base price ±5%)
   - Send JSON: `{"symbol": "AAPL", "price": 175.50}`
   - Receive echoed message
   - Parse and update UI state
3. Display sorted by price (highest first)
4. Show price change indicator and flash animation
