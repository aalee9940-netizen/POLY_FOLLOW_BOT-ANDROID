package com.polymarket.tradingbot.repository

import com.polymarket.tradingbot.api.*
import android.util.Log

class BotRepository(private val apiService: BotApiService) {

    companion object {
        private const val TAG = "BotRepository"
    }

    // Health check
    suspend fun checkHealth(): Result<HealthResponse> {
        return try {
            val response = apiService.checkHealth()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Health check failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Health check error", e)
            Result.failure(e)
        }
    }

    // Get bot status
    suspend fun getBotStatus(): Result<BotStatus> {
        return try {
            val response = apiService.getBotStatus()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to get bot status: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Get bot status error", e)
            Result.failure(e)
        }
    }

    // Start bot
    suspend fun startBot(): Result<StartBotResponse> {
        return try {
            val response = apiService.startBot()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to start bot: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Start bot error", e)
            Result.failure(e)
        }
    }

    // Stop bot
    suspend fun stopBot(): Result<String> {
        return try {
            val response = apiService.stopBot()
            if (response.isSuccessful && response.body() != null) {
                if (response.body()!!.success) {
                    Result.success(response.body()!!.message)
                } else {
                    Result.failure(Exception(response.body()!!.message))
                }
            } else {
                Result.failure(Exception("Failed to stop bot: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Stop bot error", e)
            Result.failure(e)
        }
    }

    // Get configuration
    suspend fun getConfig(): Result<ConfigResponse> {
        return try {
            val response = apiService.getConfig()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to get config: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Get config error", e)
            Result.failure(e)
        }
    }

    // Update configuration
    suspend fun updateConfig(config: Map<String, Any>): Result<String> {
        return try {
            val response = apiService.updateConfig(config)
            if (response.isSuccessful && response.body() != null) {
                if (response.body()!!.success) {
                    Result.success(response.body()!!.message)
                } else {
                    Result.failure(Exception(response.body()!!.message))
                }
            } else {
                Result.failure(Exception("Failed to update config: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Update config error", e)
            Result.failure(e)
        }
    }

    // Get trades summary
    suspend fun getTradesSummary(): Result<TradesummaryResponse> {
        return try {
            val response = apiService.getTradesSummary()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to get trades summary: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Get trades summary error", e)
            Result.failure(e)
        }
    }

    // Get balance
    suspend fun getBalance(): Result<BalanceResponse> {
        return try {
            val response = apiService.getBalance()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to get balance: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Get balance error", e)
            Result.failure(e)
        }
    }

    // Get monitored wallets
    suspend fun getMonitoredWallets(): Result<WalletsResponse> {
        return try {
            val response = apiService.getMonitoredWallets()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to get wallets: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Get wallets error", e)
            Result.failure(e)
        }
    }
}
