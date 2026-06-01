package com.polymarket.tradingbot.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.polymarket.tradingbot.api.BotApiService
import com.polymarket.tradingbot.repository.BotRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PolymarketBotApp()
        }
    }
}

@Composable
fun PolymarketBotApp() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val botApiService = createBotApiService()
            val repository = BotRepository(botApiService)
            val viewModel: BotViewModel = viewModel(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                    return BotViewModel(repository) as T
                }
            })

            DashboardScreen(viewModel)
        }
    }
}

@Composable
fun DashboardScreen(viewModel: BotViewModel) {
    val botStatus by viewModel.botStatus.collectAsState()
    val balance by viewModel.balance.collectAsState()
    val tradesSummary by viewModel.tradesSummary.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isConnected by viewModel.isConnected.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.refreshData()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        HeaderSection()

        // Connection Status
        if (!isConnected) {
            ConnectionWarning()
        }

        // Error Message
        errorMessage?.let { message ->
            if (message.isNotEmpty()) {
                ErrorBanner(message = message, onDismiss = { viewModel.clearError() })
            }
        }

        // Bot Status Card
        BotStatusCard(
            botStatus = botStatus,
            isRunning = botStatus?.running ?: false,
            onStartClick = { viewModel.startBot() },
            onStopClick = { viewModel.stopBot() },
            isLoading = isLoading
        )

        // Balance Card
        BalanceCard(balance = balance)

        // Trade Statistics Card
        TradeStatisticsCard(tradesSummary = tradesSummary)

        // Refresh Button
        RefreshButton(
            isLoading = isLoading,
            onClick = { viewModel.refreshData() }
        )

        // Connected Wallets
        if (botStatus != null) {
            WalletsCard(walletCount = botStatus!!.monitored_wallets)
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun HeaderSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Polymarket Trading Bot",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F1F1F)
        )
        Text(
            text = "Real-time Monitoring & Control",
            fontSize = 14.sp,
            color = Color(0xFF666666)
        )
    }
}

@Composable
fun ConnectionWarning() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "⚠️",
                fontSize = 20.sp,
                modifier = Modifier.width(24.dp)
            )
            Text(
                text = "Cannot connect to bot API. Check your connection.",
                color = Color(0xFFD32F2F),
                fontSize = 13.sp
            )
        }
    }
}

@Composable
fun ErrorBanner(message: String, onDismiss: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9C4))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = message,
                fontSize = 12.sp,
                color = Color(0xFF333333),
                modifier = Modifier.weight(1f)
            )
            TextButton(onClick = onDismiss) {
                Text("Dismiss", fontSize = 11.sp)
            }
        }
    }
}

@Composable
fun BotStatusCard(
    botStatus: com.polymarket.tradingbot.api.BotStatus?,
    isRunning: Boolean,
    onStartClick: () -> Unit,
    onStopClick: () -> Unit,
    isLoading: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isRunning) Color(0xFFC8E6C9) else Color(0xFFF8F8F8)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Bot Status",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F1F1F)
                    )
                    Text(
                        text = if (isRunning) "🟢 Running" else "🔴 Stopped",
                        fontSize = 14.sp,
                        color = if (isRunning) Color(0xFF2E7D32) else Color(0xFFC62828)
                    )
                }
            }

            if (botStatus?.started_at != null && isRunning) {
                Text(
                    text = "Started: ${botStatus.started_at}",
                    fontSize = 11.sp,
                    color = Color(0xFF666666)
                )
            }

            // Control Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onStartClick,
                    modifier = Modifier.weight(1f),
                    enabled = !isRunning && !isLoading,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Icon(Icons.Filled.PlayArrow, contentDescription = "Start")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Start", fontSize = 12.sp)
                }
                Button(
                    onClick = onStopClick,
                    modifier = Modifier.weight(1f),
                    enabled = isRunning && !isLoading,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
                ) {
                    Icon(Icons.Filled.Stop, contentDescription = "Stop")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Stop", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun BalanceCard(balance: com.polymarket.tradingbot.api.BalanceResponse?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Current Balance",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F1F1F)
            )
            Text(
                text = if (balance != null) String.format("$%.2f %s", balance.balance, balance.currency) else "Loading...",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2196F3)
            )
            Text(
                text = balance?.timestamp ?: "",
                fontSize = 10.sp,
                color = Color(0xFF999999)
            )
        }
    }
}

@Composable
fun TradeStatisticsCard(tradesSummary: com.polymarket.tradingbot.api.TradesummaryResponse?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Trade Statistics",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F1F1F)
            )

            if (tradesSummary != null) {
                StatisticRow("Total Trades", tradesSummary.total_trades.toString())
                StatisticRow("Successful", tradesSummary.successful.toString(), Color(0xFF4CAF50))
                StatisticRow("Failed", tradesSummary.failed.toString(), Color(0xFFF44336))
                StatisticRow("Win Rate", String.format("%.2f%%", tradesSummary.win_rate))
                StatisticRow("P&L", String.format("$%.2f", tradesSummary.pnl), 
                    if (tradesSummary.pnl >= 0) Color(0xFF4CAF50) else Color(0xFFF44336))
            } else {
                Text("No data available", fontSize = 12.sp, color = Color(0xFF999999))
            }
        }
    }
}

@Composable
fun StatisticRow(label: String, value: String, color: Color = Color(0xFF1F1F1F)) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 12.sp, color = Color(0xFF666666))
        Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
fun WalletsCard(walletCount: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Monitored Wallets",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F1F1F)
                )
                Text(
                    text = "$walletCount wallets being tracked",
                    fontSize = 12.sp,
                    color = Color(0xFF666666)
                )
            }
            Text(
                text = walletCount.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2196F3)
            )
        }
    }
}

@Composable
fun RefreshButton(isLoading: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        enabled = !isLoading,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
    ) {
        Icon(Icons.Filled.Refresh, contentDescription = "Refresh")
        Spacer(modifier = Modifier.width(8.dp))
        Text("Refresh Data", fontSize = 12.sp)
    }
}

fun createBotApiService(): BotApiService {
    val httpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .build()

    val gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        .create()

    return Retrofit.Builder()
        .baseUrl("http://10.0.2.2:5000")  // Use 10.0.2.2 for Android emulator localhost
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(BotApiService::class.java)
}
