"""
Polymarket Bot REST API - Exposes bot functionality over HTTP
"""
import threading
import json
from flask import Flask, jsonify, request
from datetime import datetime
from config import config, WALLETS
from trading import trading_client
from monitor import WalletMonitor, monitor
from typing import Dict, Any, List

app = Flask(__name__)

# Global state
bot_state = {
    "running": False,
    "started_at": None,
    "trades_executed": 0,
    "successful_trades": 0,
    "failed_trades": 0,
    "current_balance": 0.0,
    "total_pnl": 0.0,
}

monitor_instance = None
monitor_thread = None


@app.route('/api/health', methods=['GET'])
def health():
    """Health check endpoint"""
    return jsonify({
        "status": "online",
        "timestamp": datetime.now().isoformat(),
        "version": "1.0.0"
    }), 200


@app.route('/api/bot/status', methods=['GET'])
def get_bot_status():
    """Get current bot status"""
    balance = trading_client.get_balance() if trading_client else 0.0
    bot_state["current_balance"] = balance
    
    return jsonify({
        "running": bot_state["running"],
        "started_at": bot_state["started_at"],
        "current_balance": balance,
        "trades_executed": bot_state["trades_executed"],
        "successful_trades": bot_state["successful_trades"],
        "failed_trades": bot_state["failed_trades"],
        "total_pnl": bot_state["total_pnl"],
        "monitored_wallets": len(WALLETS),
        "timestamp": datetime.now().isoformat()
    }), 200


@app.route('/api/bot/start', methods=['POST'])
def start_bot():
    """Start the bot"""
    global monitor_instance, monitor_thread
    
    if bot_state["running"]:
        return jsonify({
            "success": False,
            "message": "Bot is already running"
        }), 400
    
    try:
        bot_state["running"] = True
        bot_state["started_at"] = datetime.now().isoformat()
        bot_state["trades_executed"] = 0
        bot_state["successful_trades"] = 0
        bot_state["failed_trades"] = 0
        
        # Start monitor in background thread
        monitor_instance = WalletMonitor(
            wallets=WALLETS,
            poll_interval=config.poll_interval
        )
        monitor_thread = threading.Thread(
            target=_run_monitor,
            args=(monitor_instance,),
            daemon=True
        )
        monitor_thread.start()
        
        return jsonify({
            "success": True,
            "message": "Bot started successfully",
            "started_at": bot_state["started_at"]
        }), 200
        
    except Exception as e:
        bot_state["running"] = False
        return jsonify({
            "success": False,
            "message": f"Failed to start bot: {str(e)}"
        }), 500


@app.route('/api/bot/stop', methods=['POST'])
def stop_bot():
    """Stop the bot"""
    if not bot_state["running"]:
        return jsonify({
            "success": False,
            "message": "Bot is not running"
        }), 400
    
    try:
        bot_state["running"] = False
        if monitor_instance:
            monitor_instance.stop()
        
        return jsonify({
            "success": True,
            "message": "Bot stopped successfully"
        }), 200
        
    except Exception as e:
        return jsonify({
            "success": False,
            "message": f"Failed to stop bot: {str(e)}"
        }), 500


@app.route('/api/config', methods=['GET'])
def get_config():
    """Get current configuration"""
    return jsonify({
        "wallets": config.wallets,
        "poll_interval": config.poll_interval,
        "price_filter": config.price_filter,
        "no_duplicate": config.no_duplicate,
        "tp": config.tp,
        "sl": config.sl
    }), 200


@app.route('/api/config', methods=['POST'])
def update_config():
    """Update configuration"""
    try:
        data = request.get_json()
        
        # Update config.json
        config_path = "/path/to/config.json"  # Update this path
        with open(config_path, 'r') as f:
            config_data = json.load(f)
        
        config_data.update(data)
        
        with open(config_path, 'w') as f:
            json.dump(config_data, f, indent=2)
        
        return jsonify({
            "success": True,
            "message": "Configuration updated successfully"
        }), 200
        
    except Exception as e:
        return jsonify({
            "success": False,
            "message": f"Failed to update config: {str(e)}"
        }), 500


@app.route('/api/trades/summary', methods=['GET'])
def get_trades_summary():
    """Get trade summary statistics"""
    return jsonify({
        "total_trades": bot_state["trades_executed"],
        "successful": bot_state["successful_trades"],
        "failed": bot_state["failed_trades"],
        "win_rate": (bot_state["successful_trades"] / max(bot_state["trades_executed"], 1) * 100),
        "pnl": bot_state["total_pnl"],
        "current_balance": bot_state["current_balance"]
    }), 200


@app.route('/api/balance', methods=['GET'])
def get_balance():
    """Get current USDC balance"""
    try:
        balance = trading_client.get_balance() if trading_client else 0.0
        return jsonify({
            "balance": balance,
            "currency": "USDC",
            "timestamp": datetime.now().isoformat()
        }), 200
    except Exception as e:
        return jsonify({
            "success": False,
            "message": f"Failed to get balance: {str(e)}"
        }), 500


@app.route('/api/wallets', methods=['GET'])
def get_monitored_wallets():
    """Get list of monitored wallets"""
    return jsonify({
        "wallets": WALLETS,
        "count": len(WALLETS)
    }), 200


@app.errorhandler(404)
def not_found(error):
    """Handle 404 errors"""
    return jsonify({
        "success": False,
        "message": "Endpoint not found"
    }), 404


@app.errorhandler(500)
def internal_error(error):
    """Handle 500 errors"""
    return jsonify({
        "success": False,
        "message": "Internal server error"
    }), 500


def _run_monitor(monitor_obj):
    """Run the monitor in a thread"""
    try:
        threads = monitor_obj.start()
        # Keep threads alive
        for t in threads:
            t.join()
    except Exception as e:
        print(f"Monitor error: {e}")
        bot_state["running"] = False


def increment_trade_counter(success: bool):
    """Increment trade counters"""
    bot_state["trades_executed"] += 1
    if success:
        bot_state["successful_trades"] += 1
    else:
        bot_state["failed_trades"] += 1


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=False)
