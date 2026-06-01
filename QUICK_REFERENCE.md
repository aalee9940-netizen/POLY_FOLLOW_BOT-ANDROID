# Polymarket Bot Android APK - Quick Reference

## 📁 Files Created

```
files/
├── bot_api.py                      # Flask REST API wrapper
├── build.gradle                    # Android Gradle build configuration
├── AndroidManifest.xml             # Android manifest file
├── BotApiService.kt                # Retrofit API service interface
├── BotRepository.kt                # Data repository layer
├── BotViewModel.kt                 # ViewModel for state management
├── MainActivity.kt                 # Main activity with UI
├── proguard-rules.pro              # ProGuard obfuscation rules
├── setup_android_project.sh        # Automated setup script
├── requirements_api.txt            # Python backend dependencies
├── BUILD_AND_SETUP_GUIDE.md       # Detailed build instructions
├── README_ANDROID_APK.md          # Complete documentation
└── QUICK_REFERENCE.md             # This file
```

## 🔌 API Endpoints

### Health & Status
```
GET  /api/health              → {"status": "online", "version": "1.0.0"}
GET  /api/bot/status          → {running, balance, trades, etc.}
```

### Bot Control
```
POST /api/bot/start           → Start the trading bot
POST /api/bot/stop            → Stop the trading bot
```

### Data & Config
```
GET  /api/balance             → {"balance": 1000.50, "currency": "USDC"}
GET  /api/config              → {wallets, poll_interval, filters, etc.}
POST /api/config              → Update configuration
GET  /api/trades/summary      → {total, successful, failed, win_rate, pnl}
GET  /api/wallets             → {wallets: [...], count: 5}
```

## 🚀 Quick Start Commands

```bash
# Step 1: Copy Flask API to bot directory
cp bot_api.py /path/to/your/bot/

# Step 2: Install Python requirements
pip install -r requirements_api.txt

# Step 3: Start Flask API server
python bot_api.py

# Step 4: Test API
curl http://localhost:5000/api/health

# Step 5: Set up Android project
./setup_android_project.sh /path/to/PolymarketBot

# Step 6: Build Android APK
cd /path/to/PolymarketBot
./gradlew assembleDebug

# Step 7: Install on device
adb install app/build/outputs/apk/debug/app-debug.apk
```

## 🔧 Key Configuration Points

### API Endpoint (MainActivity.kt)
```kotlin
// Local network (home WiFi)
.baseUrl("http://192.168.1.100:5000")

// Emulator (same machine)
.baseUrl("http://10.0.2.2:5000")

// Remote server
.baseUrl("https://your-server.com:5000")
```

### Flask Settings (bot_api.py)
```python
# Default: listens on all interfaces
if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=False)
```

### Android Target (build.gradle)
```gradle
compileSdk 33
minSdk 24
targetSdk 33
```

## 📊 Dashboard Metrics

| Metric | Source | Unit |
|--------|--------|------|
| Bot Status | `/api/bot/status` | Running/Stopped |
| Balance | `/api/balance` | USDC |
| Total Trades | `/api/trades/summary` | Count |
| Successful | `/api/trades/summary` | Count |
| Failed | `/api/trades/summary` | Count |
| Win Rate | `/api/trades/summary` | % |
| P&L | `/api/trades/summary` | USDC |
| Monitored Wallets | `/api/bot/status` | Count |

## 🔐 Security Headers

For production, add to `bot_api.py`:
```python
from flask_cors import CORS

CORS(app, resources={r"/api/*": {
    "origins": ["https://yourdomain.com"],
    "methods": ["GET", "POST"],
    "allow_headers": ["Content-Type", "Authorization"]
}})
```

## 🧪 Testing Endpoints

```bash
# Health check
curl http://localhost:5000/api/health

# Get bot status
curl http://localhost:5000/api/bot/status

# Get balance
curl http://localhost:5000/api/balance

# Start bot
curl -X POST http://localhost:5000/api/bot/start

# Stop bot
curl -X POST http://localhost:5000/api/bot/stop

# Get config
curl http://localhost:5000/api/config

# Get trades summary
curl http://localhost:5000/api/trades/summary

# Get monitored wallets
curl http://localhost:5000/api/wallets
```

## 🏗️ Android Project Structure

```
PolymarketBot/
├── app/
│   ├── src/main/
│   │   ├── java/com/polymarket/tradingbot/
│   │   │   ├── api/
│   │   │   │   └── BotApiService.kt
│   │   │   ├── repository/
│   │   │   │   └── BotRepository.kt
│   │   │   ├── ui/
│   │   │   │   ├── MainActivity.kt
│   │   │   │   └── BotViewModel.kt
│   │   │   └── service/
│   │   │       └── BotService.kt
│   │   └── res/
│   │       ├── values/
│   │       │   ├── strings.xml
│   │       │   ├── colors.xml
│   │       │   └── themes.xml
│   │       └── xml/
│   │           └── network_security_config.xml
│   ├── build.gradle
│   ├── AndroidManifest.xml
│   └── proguard-rules.pro
└── gradle/
    └── wrapper/
        └── gradle-wrapper.properties
```

## 📱 Android Device Setup

```bash
# Enable Developer Mode
Settings → About Phone → Build Number (tap 7 times)

# Enable USB Debugging
Settings → Developer Options → USB Debugging

# List connected devices
adb devices

# Install APK
adb install app/build/outputs/apk/debug/app-debug.apk

# Uninstall app
adb uninstall com.polymarket.tradingbot

# View logs
adb logcat -s "com.polymarket.tradingbot"

# Clear app data
adb shell pm clear com.polymarket.tradingbot
```

## 🐛 Common Issues & Fixes

| Issue | Cause | Solution |
|-------|-------|----------|
| Cannot connect | API not running | Start Flask: `python bot_api.py` |
| 404 Not Found | Wrong endpoint | Check URL matches API routes |
| Connection refused | Wrong host/port | Verify IP and port 5000 |
| Build fails | Missing JDK | Install JDK 11+ |
| App crashes | Gradle sync failed | Run `./gradlew clean build` |
| Emulator timeout | Network issues | Increase timeout in BotRepository |

## 💻 Environment Variables

Create `.env` in bot directory:
```
POLYMARKET_PRIVATE_KEY=your_key_here
POLYMARKET_FUNDER=your_funder_address
DISCORD_WEBHOOK_URL=your_webhook_url
```

Load in `config.py`:
```python
from dotenv import load_dotenv
import os

load_dotenv()
PRIVATE_KEY = os.getenv('POLYMARKET_PRIVATE_KEY')
```

## 🔄 Data Flow

```
Phone (Android App)
        ↓
   HTTP Request
        ↓
Flask API Server (localhost:5000)
        ↓
Trading Bot (Python)
        ↓
Polymarket CLOB
```

## 📦 Build Commands

```bash
# Debug APK (development)
./gradlew assembleDebug
# Output: app/build/outputs/apk/debug/app-debug.apk

# Release APK (production)
./gradlew assembleRelease
# Output: app/build/outputs/apk/release/app-release.apk

# Run on connected device
./gradlew installDebug

# Run app in Android Studio
./gradlew installDebug
# Then click "Run" in Android Studio

# Clean build
./gradlew clean build

# View Gradle tasks
./gradlew tasks
```

## 🌐 Network Configuration Examples

### Local Network
```kotlin
// Get device IP
val apiUrl = "http://192.168.1.${YOUR_BOT_COMPUTER_LAST_OCTET}:5000"
```

### Using ngrok (Remote Access)
```bash
ngrok http 5000
# Copy the ngrok URL and use in app
```

### Docker Container
```bash
# Run bot in Docker
docker run -p 5000:5000 polymarket-bot
# Use container IP in app
```

## ✅ Verification Checklist

- [ ] Python requirements installed: `pip install -r requirements_api.txt`
- [ ] Flask API starts without errors: `python bot_api.py`
- [ ] Health endpoint responds: `curl http://localhost:5000/api/health`
- [ ] Android Studio installed and JDK 11+ available
- [ ] Android project created with correct package name
- [ ] All Kotlin files copied to correct directories
- [ ] build.gradle and AndroidManifest.xml updated
- [ ] API endpoint URL set correctly in MainActivity.kt
- [ ] App builds successfully: `./gradlew build`
- [ ] App installs on device without errors
- [ ] App connects to API and displays bot status

## 📚 File Purposes

| File | Purpose |
|------|---------|
| `bot_api.py` | Flask REST API wrapper around bot |
| `build.gradle` | Android Gradle build configuration |
| `AndroidManifest.xml` | App permissions & activities |
| `BotApiService.kt` | Retrofit HTTP interface |
| `BotRepository.kt` | Data access layer |
| `BotViewModel.kt` | UI state management |
| `MainActivity.kt` | Main UI screen |
| `setup_android_project.sh` | Automated setup helper |

## 🎯 Next Steps

1. **Setup Backend:** Add `bot_api.py` to your bot, run Flask
2. **Create Android Project:** Use Android Studio
3. **Run Setup Script:** `./setup_android_project.sh /path`
4. **Configure Endpoint:** Update URL in MainActivity.kt
5. **Build:** `./gradlew assembleDebug`
6. **Test:** Install on device, verify connection
7. **Deploy:** Share APK or publish to Play Store

---

**Version:** 1.0.0  
**Last Updated:** 2024
