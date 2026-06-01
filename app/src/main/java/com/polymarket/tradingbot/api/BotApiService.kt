package com.polymarket.tradingbot.api

import retrofit2.Response
import retrofit2.http.*
import com.google.gson.annotations.SerializedName

/**
 * Data Models
 */
data class BotStatus(
    val running: Boolean,
    val started_at: String?,
    val current_balance: Double,
    val trades_executed: Int,
    val successful_trades: Int,
    val failed_trades: Int,
    val total_pnl: Double,
    val monitored_wallets: Int,
    val timestamp: String
)

data class HealthResponse(
    val status: String,
    val timestamp: String,
    val version: String
)

data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null
)

data class BalanceResponse(
    val balance: Double,
    val currency: String,
    val timestamp: String
)

data class ConfigResponse(
    val wallets: List<String>,
    val poll_interval: Int,
    val price_filter: PriceFilter,
    val no_duplicate: NoDuplicate,
    val tp: TakeProfitConfig,
    val sl: StopLossConfig
)

data class PriceFilter(
    val enabled: Boolean,
    val min_price: Double,
    val max_price: Double
)

data class NoDuplicate(
    val enabled: Boolean,
    val expire_seconds: Int
)

data class TakeProfitConfig(
    val enabled: Boolean,
    val type: String,
    val value: Double
)

data class StopLossConfig(
    val enabled: Boolean,
    val type: String,
    val value: Double
)

data class TradesummaryResponse(
    val total_trades: Int,
    val successful: Int,
    val failed: Int,
    val win_rate: Double,
    val pnl: Double,
    val current_balance: Double
)

data class WalletsResponse(
    val wallets: List<String>,
    val count: Int
)

data class StartBotResponse(
    val success: Boolean,
    val message: String,
    val started_at: String?
)

/**
 * Retrofit API Service Interface
 */
interface BotApiService {

    @GET("/api/health")
    suspend fun checkHealth(): Response<HealthResponse>

    @GET("/api/bot/status")
    suspend fun getBotStatus(): Response<BotStatus>

    @POST("/api/bot/start")
    suspend fun startBot(): Response<StartBotResponse>

    @POST("/api/bot/stop")
    suspend fun stopBot(): Response<ApiResponse<String>>

    @GET("/api/config")
    suspend fun getConfig(): Response<ConfigResponse>

    @POST("/api/config")
    suspend fun updateConfig(@Body config: Map<String, Any>): Response<ApiResponse<String>>

    @GET("/api/trades/summary")
    suspend fun getTradesSummary(): Response<TradesummaryResponse>

    @GET("/api/balance")
    suspend fun getBalance(): Response<BalanceResponse>

    @GET("/api/wallets")
    suspend fun getMonitoredWallets(): Response<WalletsResponse>
}
