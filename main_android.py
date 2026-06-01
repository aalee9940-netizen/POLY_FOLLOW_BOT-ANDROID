"""
Polymarket Trading Bot - Android Version with Kivy UI
"""
import os
import sys
import threading
import time
from datetime import datetime
from pathlib import Path

from kivy.app import App
from kivy.uix.boxlayout import BoxLayout
from kivy.uix.gridlayout import GridLayout
from kivy.uix.scrollview import ScrollView
from kivy.uix.label import Label
from kivy.uix.button import Button
from kivy.uix.switch import Switch
from kivy.uix.textinput import TextInput
from kivy.uix.popup import Popup
from kivy.uix.spinner import Spinner
from kivy.clock import Clock
from kivy.core.window import Window

# Set window size for Android
Window.size = (1080, 1920)

# Import bot modules
try:
    from config import config, POLL_INTERVAL, WALLETS
    from trading import trading_client
    from monitor import WalletMonitor
    from notification import notification_manager
except ImportError as e:
    print(f"Import error: {e}")
    # Fallback for testing
    WALLETS = []
    POLL_INTERVAL = 10


class BotState:
    """Global bot state"""
    def __init__(self):
        self.running = False
        self.started_at = None
        self.trades_executed = 0
        self.successful_trades = 0
        self.failed_trades = 0
        self.current_balance = 0.0
        self.total_pnl = 0.0
        self.logs = []
        self.monitor_instance = None
        self.monitor_thread = None

    def log(self, message: str):
        """Add log message"""
        timestamp = datetime.now().strftime("%H:%M:%S")
        log_msg = f"[{timestamp}] {message}"
        self.logs.append(log_msg)
        print(log_msg)
        # Keep only last 100 logs
        if len(self.logs) > 100:
            self.logs = self.logs[-100:]


bot_state = BotState()


class PolymarketBotApp(App):
    """Main Kivy App for Polymarket Trading Bot"""
    
    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        self.title = "Polymarket Trading Bot"
        self.monitor_instance = None
        
    def build(self):
        """Build the UI"""
        main_layout = BoxLayout(orientation='vertical', padding=10, spacing=10)
        
        # Top status bar
        status_layout = GridLayout(cols=2, size_hint_y=0.15, spacing=5)
        status_layout.add_widget(Label(text='Status:', bold=True))
        self.status_label = Label(text='Stopped', color=(1, 0, 0, 1))
        status_layout.add_widget(self.status_label)
        
        status_layout.add_widget(Label(text='Balance:', bold=True))
        self.balance_label = Label(text='0.00 USDC')
        status_layout.add_widget(self.balance_label)
        
        status_layout.add_widget(Label(text='Trades:', bold=True))
        self.trades_label = Label(text='0')
        status_layout.add_widget(self.trades_label)
        
        status_layout.add_widget(Label(text='Win Rate:', bold=True))
        self.winrate_label = Label(text='0%')
        status_layout.add_widget(self.winrate_label)
        
        main_layout.add_widget(status_layout)
        
        # Control buttons
        button_layout = BoxLayout(size_hint_y=0.12, spacing=5)
        self.start_btn = Button(text='Start Bot', background_color=(0, 1, 0, 1))
        self.start_btn.bind(on_press=self.on_start_bot)
        button_layout.add_widget(self.start_btn)
        
        self.stop_btn = Button(text='Stop Bot', background_color=(1, 0, 0, 1))
        self.stop_btn.bind(on_press=self.on_stop_bot)
        button_layout.add_widget(self.stop_btn)
        
        settings_btn = Button(text='Settings', background_color=(0.5, 0.5, 0.5, 1))
        settings_btn.bind(on_press=self.show_settings)
        button_layout.add_widget(settings_btn)
        
        main_layout.add_widget(button_layout)
        
        # Log viewer
        log_label = Label(text='Log Messages:', size_hint_y=0.08, bold=True)
        main_layout.add_widget(log_label)
        
        self.log_scroll = ScrollView(size_hint_y=0.65)
        self.log_text = Label(
            text='Waiting for bot to start...',
            size_hint_y=None,
            markup=True
        )
        self.log_text.bind(texture_size=self.log_text.setter('size'))
        self.log_scroll.add_widget(self.log_text)
        main_layout.add_widget(self.log_scroll)
        
        # Schedule periodic updates
        Clock.schedule_interval(self.update_ui, 1)
        
        return main_layout
    
    def on_start_bot(self, instance):
        """Start the trading bot"""
        if bot_state.running:
            self.show_popup("Info", "Bot is already running")
            return
        
        try:
            bot_state.running = True
            bot_state.started_at = datetime.now().isoformat()
            bot_state.trades_executed = 0
            bot_state.successful_trades = 0
            bot_state.failed_trades = 0
            
            bot_state.log("Starting Polymarket Trading Bot...")
            bot_state.log(f"Monitoring {len(WALLETS)} wallets")
            bot_state.log(f"Poll interval: {POLL_INTERVAL} seconds")
            
            # Start monitor in background thread
            def run_monitor():
                try:
                    bot_state.monitor_instance = WalletMonitor(
                        wallets=WALLETS,
                        poll_interval=POLL_INTERVAL
                    )
                    threads = bot_state.monitor_instance.start()
                    bot_state.log("Bot started successfully!")
                    
                    # Keep threads alive
                    for t in threads:
                        t.join()
                except Exception as e:
                    bot_state.log(f"Bot error: {str(e)}")
                    bot_state.running = False
            
            bot_state.monitor_thread = threading.Thread(target=run_monitor, daemon=True)
            bot_state.monitor_thread.start()
            
        except Exception as e:
            bot_state.log(f"Failed to start bot: {str(e)}")
            bot_state.running = False
            self.show_popup("Error", f"Failed to start bot: {str(e)}")
    
    def on_stop_bot(self, instance):
        """Stop the trading bot"""
        if not bot_state.running:
            self.show_popup("Info", "Bot is not running")
            return
        
        try:
            bot_state.running = False
            if bot_state.monitor_instance:
                bot_state.monitor_instance.stop()
            bot_state.log("Bot stopped")
            self.show_popup("Info", "Bot stopped successfully")
        except Exception as e:
            bot_state.log(f"Error stopping bot: {str(e)}")
            self.show_popup("Error", f"Error stopping bot: {str(e)}")
    
    def show_settings(self, instance):
        """Show settings popup"""
        content = BoxLayout(orientation='vertical', padding=10, spacing=10)
        
        # Add settings fields
        settings_grid = GridLayout(cols=2, spacing=5, size_hint_y=0.8)
        
        settings_grid.add_widget(Label(text='Poll Interval (sec):'))
        poll_input = TextInput(text=str(POLL_INTERVAL), multiline=False, input_filter='int')
        settings_grid.add_widget(poll_input)
        
        settings_grid.add_widget(Label(text='Enable Price Filter:'))
        price_filter_switch = Switch(active=config.price_filter.get('enabled', False))
        settings_grid.add_widget(price_filter_switch)
        
        settings_grid.add_widget(Label(text='Enable TP/SL:'))
        tp_sl_switch = Switch(active=config.tp.get('enabled', False))
        settings_grid.add_widget(tp_sl_switch)
        
        content.add_widget(settings_grid)
        
        # Buttons
        button_layout = BoxLayout(size_hint_y=0.2, spacing=5)
        save_btn = Button(text='Save')
        close_btn = Button(text='Close')
        button_layout.add_widget(save_btn)
        button_layout.add_widget(close_btn)
        
        content.add_widget(button_layout)
        
        popup = Popup(title='Settings', content=content, size_hint=(0.9, 0.6))
        
        def save_settings(instance):
            bot_state.log(f"Settings saved. Poll interval: {poll_input.text}")
            popup.dismiss()
        
        save_btn.bind(on_press=save_settings)
        close_btn.bind(on_press=popup.dismiss)
        
        popup.open()
    
    def show_popup(self, title: str, message: str):
        """Show a popup dialog"""
        content = BoxLayout(orientation='vertical', padding=10, spacing=10)
        content.add_widget(Label(text=message))
        
        close_btn = Button(text='Close', size_hint_y=0.3)
        content.add_widget(close_btn)
        
        popup = Popup(title=title, content=content, size_hint=(0.8, 0.4))
        close_btn.bind(on_press=popup.dismiss)
        popup.open()
    
    def update_ui(self, dt):
        """Update UI elements"""
        # Update status
        status_text = "Running" if bot_state.running else "Stopped"
        status_color = (0, 1, 0, 1) if bot_state.running else (1, 0, 0, 1)
        self.status_label.text = status_text
        self.status_label.color = status_color
        
        # Update balance
        try:
            if trading_client:
                balance = trading_client.get_balance()
                bot_state.current_balance = balance
                self.balance_label.text = f"{balance:.2f} USDC"
        except Exception as e:
            self.balance_label.text = "Error"
        
        # Update trades
        self.trades_label.text = str(bot_state.trades_executed)
        
        # Update win rate
        if bot_state.trades_executed > 0:
            win_rate = (bot_state.successful_trades / bot_state.trades_executed) * 100
            self.winrate_label.text = f"{win_rate:.1f}%"
        
        # Update logs
        if bot_state.logs:
            log_text = "\n".join(bot_state.logs[-20:])  # Show last 20 logs
            self.log_text.text = log_text


if __name__ == '__main__':
    app = PolymarketBotApp()
    app.run()
