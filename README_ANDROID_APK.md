# Polymarket Trading Bot - Android APK Monitor

Build an **Android APK** to monitor and control your Polymarket trading bot from your phone!

## 🎯 Overview

This project provides:

✅ **Flask REST API** - Exposes your bot's functionality over HTTP  
✅ **Kotlin Android App** - Beautiful UI for monitoring and control  
✅ **APK Distribution** - Ready-to-install Android application  
✅ **Real-time Dashboard** - Monitor bot status, balance, trades, and statistics  
✅ **Remote Control** - Start/stop your bot from anywhere  

## 📦 What You Get

### Files Included

```
├── bot_api.py                          # Flask API wrapper for your bot
├── build.gradle                        # Android build configuration
├── AndroidManifest.xml                 # Android app manifest
├── BotApiService.kt                    # Retrofit API interface
├── BotRepository.kt                    # Data layer
├── BotViewModel.kt                     # State management
├── MainActivity.kt                     # Main UI screen
├── proguard-rules.pro                  # Code obfuscation rules
├── requirements_api.txt                # Python dependencies
├── setup_android_project.sh            # Automated setup script
├── BUILD_AND_SETUP_GUIDE.md           # Detailed build instructions
└── README_ANDROID_APK.md              # This file
```

## ⚡ Quick Start (5 Minutes)

### Step 1: Add Flask API to Your Bot
```bash
# Copy bot_api.py to your bot directory
cp bot_api.py /path/to/your/polymarket/bot/

# Install Flask
pip install -r requirements_api.txt
```

### Step 2: Start the API Server
```bash
cd /path/to/your/bot
python bot_api.py

# Should output:
# * Running on http://0.0.0.0:5000
```

### Step 3: Create Android Project
1. Open **Android Studio**
2. Create new project: **Empty Activity**
3. Name: `PolymarketBot`
4. Package: `com.polymarket.tradingbot`
5. Minimum SDK: **API 24**

### Step 4: Run Setup Script
```bash
chmod +x setup_android_project.sh
./setup_android_project.sh ~/AndroidStudioProjects/PolymarketBot
```

### Step 5: Build APK
In Android Studio:
- Click **Build** → **Build Bundle(s) / APK(s)** → **Build APK(s)**
- Wait for completion
- Click **Locate** to find your APK

### Step 6: Install on Device
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

## 🏗️ Project Structure

### Backend (Python)
```python
# bot_api.py exposes these endpoints:

GET  /api/health                    # Health check
GET  /api/bot/status               # Current bot status
POST /api/bot/start                # Start the bot
POST /api/bot/stop                 # Stop the bot
GET  /api/balance                  # Current USDC balance
GET  /api/config                   # Configuration
GET  /api/trades/summary           # Trade statistics
GET  /api/wallets                  # Monitored wallets
```

### Frontend (Android)
```
MainActivity
├── HeaderSection              # App title
├── ConnectionStatus          # API connection indicator
├── BotStatusCard            # Run status & control buttons
├── BalanceCard              # Current balance display
├── TradeStatisticsCard      # Win rate, P&L, totals
├── WalletsCard              # Number of monitored wallets
└── RefreshButton            # Manual data refresh
```

## 🔌 Network Configuration

### Development (Local Network)
For testing on your home network:
```kotlin
// In MainActivity.kt, update createBotApiService()
.baseUrl("http://192.168.1.100:5000")  // Your computer's local IP
```

Get your IP:
```bash
# Windows
ipconfig

# Mac/Linux
ifconfig
```

### Production (Remote Server)
For cloud-hosted bot:
```kotlin
.baseUrl("https://your-server.com:5000")  // HTTPS recommended
```

### Emulator Testing
Android emulator on same machine:
```kotlin
.baseUrl("http://10.0.2.2:5000")  // Special emulator localhost
```

## 🚀 API Integration Example

The app uses **Retrofit** for HTTP communication:

```kotlin
// API Service
interface BotApiService {
    @GET("/api/bot/status")
    suspend fun getBotStatus(): Response<BotStatus>
}

// Repository layer
class BotRepository(apiService: BotApiService) {
    suspend fun getBotStatus(): Result<BotStatus> { ... }
}

// ViewModel (UI state)
class BotViewModel(repository: BotRepository) : ViewModel() {
    fun refreshData() { ... }
    fun startBot() { ... }
    fun stopBot() { ... }
}
```

## 📊 Dashboard Features

### Display Metrics
- **Bot Status** - Running/Stopped indicator
- **Current Balance** - Real-time USDC balance
- **Trade Statistics**:
  - Total trades executed
  - Successful vs failed
  - Win rate percentage
  - Total P&L
- **Monitored Wallets** - Number of tracked accounts

### Control Features
- **Start Bot** - Launch the trading algorithm
- **Stop Bot** - Pause/stop trading
- **Refresh** - Manual data synchronization
- **Status Indicator** - Connection status to API

## 🔐 Security Best Practices

### For Development
```xml
<!-- Allow HTTP on local networks -->
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">192.168.0.0</domain>
    </domain-config>
</network-security-config>
```

### For Production
1. Use **HTTPS/SSL** on your backend
2. Implement **API authentication** (tokens, API keys)
3. Add **rate limiting** to prevent abuse
4. Never expose private keys over HTTP
5. Remove cleartext traffic permissions

Example HTTPS setup:
```python
# bot_api.py with SSL
app.run(
    host='0.0.0.0',
    port=5000,
    ssl_context=('cert.pem', 'key.pem')  # Your SSL certificates
)
```

## 🛠️ Customization

### Change App Theme
Edit `MainActivity.kt` colors:
```kotlin
colors = CardDefaults.cardColors(
    containerColor = Color(0xFFC8E6C9)  // Change this
)
```

### Add More Metrics
1. Extend `BotApiService.kt` with new endpoints
2. Add data classes for responses
3. Create new UI cards in `MainActivity.kt`

### Add Settings Screen
1. Create `SettingsScreen()` composable
2. Add configuration endpoints
3. Update ViewModel with config state

## 🐛 Troubleshooting

### APK Installation Fails
```bash
# Clear previous installation
adb uninstall com.polymarket.tradingbot

# Install debug APK
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

### App Won't Connect to API
1. Check Flask is running: `curl http://localhost:5000/api/health`
2. Verify API endpoint URL in MainActivity matches your bot
3. Check firewall allows port 5000
4. Ensure device and bot are on same network (for local)

### Build Fails
```bash
# Clean build
./gradlew clean build

# Update Gradle wrapper
./gradlew wrapper --gradle-version 8.0

# Check Java version (must be 11+)
java -version
```

### App Crashes on Launch
Check Android Studio Logcat for errors:
- View → Tool Windows → Logcat
- Filter: `com.polymarket.tradingbot`

## 📱 Device Requirements

- **Minimum Android Version:** 7.0 (API 24)
- **Recommended:** Android 10+ for best performance
- **Internet:** WiFi or mobile data connection
- **Storage:** ~50MB for app and cache

## 🚢 Distribution

### Self-Install via USB
1. Connect Android device via USB
2. Enable Developer Mode (tap Build Number 7x)
3. Run: `adb install app/build/outputs/apk/debug/app-debug.apk`

### Share APK File
1. Build release APK: `./gradlew assembleRelease`
2. Find APK: `app/build/outputs/apk/release/app-release.apk`
3. Send to device or share via cloud storage
4. Open APK file on device to install

### Google Play Store (Optional)
1. Sign release APK
2. Create Play Store account ($25 one-time)
3. Upload APK and set up listing
4. Submit for review (~24 hours)

## 📖 Detailed Documentation

For complete setup and customization instructions, see:
- **BUILD_AND_SETUP_GUIDE.md** - Step-by-step instructions

## 🔗 Technology Stack

### Backend
- **Python 3.8+**
- **Flask 3.0** - Web framework
- **py-clob-client** - Polymarket API client
- **Retrofit** endpoints via REST

### Frontend
- **Kotlin** - Programming language
- **Jetpack Compose** - Modern UI toolkit
- **Retrofit 2** - HTTP client
- **OkHttp3** - Network library
- **Gson** - JSON serialization
- **Coroutines** - Async operations

### Build Tools
- **Android SDK 33+**
- **Gradle 8.0+**
- **JDK 11+**

## 🎓 Learning Resources

- [Kotlin Documentation](https://kotlinlang.org/docs/)
- [Jetpack Compose Tutorial](https://developer.android.com/jetpack/compose/tutorial)
- [Retrofit Guide](https://square.github.io/retrofit/)
- [Android Architecture](https://developer.android.com/topic/architecture)
- [Flask Documentation](https://flask.palletsprojects.com/)

## 💡 Tips & Tricks

### Remote Access (Outside Home Network)
Use **ngrok** to tunnel your local API:
```bash
# Install ngrok: https://ngrok.com/
ngrok http 5000

# Get public URL and update in MainActivity.kt
.baseUrl("https://xxxxx-xx-xxx-xxxx-xx.ngrok.io")
```

### Monitor Logs
```bash
# Watch app logs in real-time
adb logcat -s "com.polymarket.tradingbot"

# Save logs to file
adb logcat > logcat.txt
```

### Test API Endpoints
```bash
# Check API health
curl http://localhost:5000/api/health

# Get bot status
curl http://localhost:5000/api/bot/status

# Start bot
curl -X POST http://localhost:5000/api/bot/start
```

## ❓ FAQ

**Q: Do I need to keep my phone connected?**  
A: No. The bot runs on your computer/server. Phone just monitors it.

**Q: Can I run multiple bots?**  
A: Yes! Run each bot on different ports and create separate Android apps.

**Q: Is it secure?**  
A: Use HTTPS + authentication in production. HTTP is fine for local development.

**Q: Can I publish on Google Play Store?**  
A: Yes, but requires signing, account ($25), and review (~24 hours).

**Q: What if the app crashes?**  
A: Check Logcat, update API endpoint, restart Flask backend.

## 📝 License

This project follows the same license as your Polymarket bot.

## 🤝 Support

Issues or questions?
1. Check BUILD_AND_SETUP_GUIDE.md
2. Review Logcat output in Android Studio
3. Test API with curl
4. Verify network connectivity

---

**Version:** 1.0.0  
**Last Updated:** 2024  
**Status:** Production Ready ✅

**Happy Trading! 🚀📈**
