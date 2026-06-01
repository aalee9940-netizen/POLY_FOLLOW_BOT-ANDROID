package com.polymarket.tradingbot.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polymarket.tradingbot.api.BotStatus
import com.polymarket.tradingbot.api.BalanceResponse
import com.polymarket.tradingbot.api.TradesummaryResponse
import com.polymarket.tradingbot.repository.BotRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log

class BotViewModel(private val repository: BotRepository) : ViewModel() {

    companion object {
        private const val TAG = "BotViewModel"
    }

    // UI State
    private val _botStatus = MutableStateFlow<BotStatus?>(null)
    val botStatus: StateFlow<BotStatus?> = _botStatus.asStateFlow()

    private val _balance = MutableStateFlow<BalanceResponse?>(null)
    val balance: StateFlow<BalanceResponse?> = _balance.asStateFlow()

    private val _tradesSummary = MutableStateFlow<TradesummaryResponse?>(null)
    val tradesSummary: StateFlow<TradesummaryResponse?> = _tradesSummary.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    init {
        checkConnection()
    }

    fun refreshData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Get bot status
                val statusResult = repository.getBotStatus()
                statusResult.onSuccess { status ->
                    _botStatus.value = status
                    _isConnected.value = true
                    _errorMessage.value = null
                }.onFailure { error ->
                    _errorMessage.value = error.message ?: "Unknown error"
                    _isConnected.value = false
                }

                // Get balance
                val balanceResult = repository.getBalance()
                balanceResult.onSuccess { balance ->
                    _balance.value = balance
                }.onFailure { error ->
                    Log.e(TAG, "Failed to get balance", error)
                }

                // Get trades summary
                val tradesResult = repository.getTradesSummary()
                tradesResult.onSuccess { trades ->
                    _tradesSummary.value = trades
                }.onFailure { error ->
                    Log.e(TAG, "Failed to get trades summary", error)
                }

            } finally {
                _isLoading.value = false
            }
        }
    }

    fun startBot() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.startBot()
                result.onSuccess { response ->
                    _errorMessage.value = if (response.success) "Bot started successfully" else response.message
                    refreshData()
                }.onFailure { error ->
                    _errorMessage.value = error.message ?: "Failed to start bot"
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun stopBot() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.stopBot()
                result.onSuccess { message ->
                    _errorMessage.value = message
                    refreshData()
                }.onFailure { error ->
                    _errorMessage.value = error.message ?: "Failed to stop bot"
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun checkConnection() {
        viewModelScope.launch {
            try {
                val result = repository.checkHealth()
                result.onSuccess {
                    _isConnected.value = true
                }.onFailure {
                    _isConnected.value = false
                    _errorMessage.value = "Cannot connect to bot API"
                }
            } catch (e: Exception) {
                _isConnected.value = false
                _errorMessage.value = "Connection failed: ${e.message}"
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
