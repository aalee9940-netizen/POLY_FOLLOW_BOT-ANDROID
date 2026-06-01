# Polymarket Trading Bot - Android APK Build Guide

This guide explains how to build and deploy the Android monitoring app for your Polymarket trading bot.

## 📋 Architecture Overview

```
┌─────────────────────────────────┐
│   Python Trading Bot (Backend)  │
│  - Monitors wallets             │
│  - Executes trades              │
│  - Manages balance              │
└──────────────┬──────────────────┘
               │ REST API (Flask)
               │ localhost:5000
┌──────────────▼──────────────────┐
│   Android App (Frontend)        │
│  - Monitoring Dashboard         │
│  - Real-time Statistics         │
│  - Bot Control (Start/Stop)     │
│  - Balance Display              │
└─────────────────────────────────┘
```

## 🚀 Step 1: Prepare Python Backend with API

### 1.1 Install Flask
```bash
pip install flask
```

### 1.2 Add API Layer to Your Bot
Copy the provided `bot_api.py` file to your bot directory alongside your existing files:
- main.py
- config.py
- trading.py
- monitor.py
- api.py
- etc.

### 1.3 Update Your main.py
Modify your main entry point to optionally start the Flask API:

```python
from bot_api import app

if __name__ == "__main__":
    # Option 1: Start API server (for Android app monitoring)
    app.run(host='0.0.0.0', port=5000, debug=False)
    
    # Option 2: Run trading bot normally
    # main()
```

### 1.4 Test the API Locally
```bash
# Start the Flask API
python bot_api.py

# In another terminal, test endpoints:
curl http://localhost:5000/api/health
curl http://localhost:5000/api/bot/status
curl http://localhost:5000/api/balance
```

You should see JSON responses from these endpoints.

## 🏗️ Step 2: Set Up Android Development Environment

### 2.1 Install Requirements
- **Android Studio** (latest version): https://developer.android.com/studio
- **Java Development Kit (JDK)** 11 or higher
- **Gradle** (included with Android Studio)

### 2.2 Create Android Project in Android Studio
1. Open Android Studio
2. Click "New Project"
3. Select "Empty Activity"
4. Configure:
   - Name: `PolymarketBot`
   - Package name: `com.polymarket.tradingbot`
   - Minimum SDK: API 24 (Android 7.0)
   - Language: Kotlin

## 🔧 Step 3: Configure Android Project Files

### 3.1 Copy Kotlin Files
Place these Kotlin files in your Android project:

**Location**: `app/src/main/java/com/polymarket/tradingbot/`

- `api/BotApiService.kt` - Retrofit API interface
- `repository/BotRepository.kt` - Data repository layer
- `ui/BotViewModel.kt` - ViewModel for state management
- `ui/MainActivity.kt` - Main UI screen

Create the directory structure:
```
app/src/main/java/com/polymarket/tradingbot/
├── api/
│   └── BotApiService.kt
├── repository/
│   └── BotRepository.kt
├── ui/
│   ├── BotViewModel.kt
│   └── MainActivity.kt
└── service/
    └── BotService.kt (optional - for background service)
```

### 3.2 Update build.gradle
Replace `app/build.gradle` with the provided `build.gradle` content.

### 3.3 Update AndroidManifest.xml
Replace `app/src/main/AndroidManifest.xml` with the provided `AndroidManifest.xml` content.

### 3.4 Add ProGuard Rules
Place `proguard-rules.pro` in the `app/` directory. This file is referenced in build.gradle.

### 3.5 Create Resource Files
Create `app/src/main/res/values/strings.xml`:
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">Polymarket Bot Monitor</string>
    <string name="title_main">Trading Dashboard</string>
</resources>
```

Create `app/src/main/res/values/colors.xml`:
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="purple_200">#FFBB86FC</color>
    <color name="purple_500">#FF6200EE</color>
    <color name="purple_700">#FF3700B3</color>
    <color name="teal_200">#FF03DAC5</color>
    <color name="teal_700">#FF018786</color>
    <color name="black">#FF000000</color>
    <color name="white">#FFFFFFFF</color>
</resources>
```

Create `app/src/main/res/values/themes.xml`:
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <style name="Theme.PolymarketBot" parent="android:Theme.Material.Light">
        <item name="android:colorPrimary">@color/purple_500</item>
        <item name="android:colorSecondary">@color/teal_200</item>
    </style>
</resources>
```

## 🔌 Step 4: Configure API Endpoint

In `MainActivity.kt`, update the base URL to match your bot's location:

**For Local Testing (Emulator):**
```kotlin
.baseUrl("http://10.0.2.2:5000")  // Emulator localhost
```

**For Real Device on Same Network:**
```kotlin
.baseUrl("http://YOUR_COMPUTER_IP:5000")  // e.g., http://192.168.1.100:5000
```

**For Remote Server:**
```kotlin
.baseUrl("https://your-server.com:5000")  // Use HTTPS for production
```

## 📦 Step 5: Build the APK

### 5.1 Using Android Studio GUI
1. Open Android Studio
2. Click **Build** → **Build Bundle(s) / APK(s)** → **Build APK(s)**
3. Wait for the build to complete
4. Android Studio will show a notification with the APK location

### 5.2 Using Command Line (Gradle)
```bash
# Navigate to your project root
cd /path/to/PolymarketBot

# Build debug APK
./gradlew assembleDebug

# Build release APK (optimized)
./gradlew assembleRelease

# APK output location:
# Debug: app/build/outputs/apk/debug/app-debug.apk
# Release: app/build/outputs/apk/release/app-release.apk
```

### 5.3 Build Output
After successful build, you'll find the APK at:
- **Debug:** `app/build/outputs/apk/debug/app-debug.apk`
- **Release:** `app/build/outputs/apk/release/app-release.apk`

## 📱 Step 6: Install on Android Device

### 6.1 Using Android Studio
1. Connect your Android device via USB
2. Click **Run** → **Run 'app'**
3. Select your device from the list
4. Android Studio will build and install automatically

### 6.2 Using Command Line
```bash
# List connected devices
adb devices

# Install APK
adb install app/build/outputs/apk/debug/app-debug.apk

# Uninstall
adb uninstall com.polymarket.tradingbot
```

### 6.3 Using APK File
1. Transfer the APK file to your Android device
2. Open file manager and tap the APK file
3. Follow the installation prompts
4. Grant necessary permissions

## 🔐 Step 7: Security Configuration

### For Development (HTTP)
If your bot API uses HTTP (not HTTPS), you need to allow cleartext traffic:

Create `app/src/main/res/xml/network_security_config.xml`:
```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">10.0.2.2</domain>
        <domain includeSubdomains="true">192.168.1.0</domain>
        <domain includeSubdomains="true">localhost</domain>
    </domain-config>
</network-security-config>
```

Then in `AndroidManifest.xml`:
```xml
<application
    ...
    android:networkSecurityConfig="@xml/network_security_config">
```

### For Production (HTTPS)
- Set up SSL/TLS certificates on your bot backend
- Use HTTPS URLs in the Android app
- Remove `android:usesCleartextTraffic="true"` from manifest

## 🌐 Step 8: Network Configuration for Remote Access

### Option A: Local Network (LAN)
1. Ensure device and computer are on same WiFi network
2. Find computer's IP: `ipconfig` (Windows) or `ifconfig` (Mac/Linux)
3. Update API endpoint: `http://192.168.1.100:5000`

### Option B: Using ngrok for Remote Access
```bash
# Install ngrok: https://ngrok.com/
ngrok http 5000

# You'll get a public URL like: https://xxxxx-xx-xxx-xxxx-xx.ngrok.io
# Use this URL in the Android app
```

### Option C: Cloud Server
1. Deploy bot to cloud server (AWS, DigitalOcean, etc.)
2. Configure firewall to allow port 5000
3. Use your server's domain/IP in the Android app

## 🧪 Step 9: Testing

### Test Checklist
- [ ] Flask API server is running: `http://localhost:5000/api/health`
- [ ] Android device is connected to same network as bot
- [ ] API endpoint in MainActivity matches your bot's location
- [ ] App successfully connects and shows bot status
- [ ] Start/Stop buttons work
- [ ] Balance and trade statistics display correctly

### Debug Logs
Monitor Android logs in Android Studio:
1. Open **Logcat** (View → Tool Windows → Logcat)
2. Filter by: `com.polymarket.tradingbot`
3. Watch for API calls and errors

## 📊 Features in the App

### Dashboard Display
- ✅ Bot running status (green/red indicator)
- ✅ Current USDC balance
- ✅ Trade statistics (total, successful, failed, win rate)
- ✅ P&L (Profit/Loss)
- ✅ Monitored wallets count
- ✅ Last update timestamp

### Control Features
- ✅ Start Bot button
- ✅ Stop Bot button
- ✅ Refresh Data button
- ✅ Connection status indicator
- ✅ Error messages and notifications

### Real-time Updates
- Auto-refresh data every time you return to the app
- Manual refresh button for on-demand updates

## 🐛 Troubleshooting

### Issue: "Cannot connect to bot API"
**Solution:** Check API endpoint URL and ensure bot is running on correct port
```bash
curl http://localhost:5000/api/health
```

### Issue: "Connection refused"
**Solution:** Ensure Flask API is running and listening on all interfaces
```python
app.run(host='0.0.0.0', port=5000)  # ✅ Correct
app.run(port=5000)  # ❌ Only localhost
```

### Issue: Emulator can't reach localhost
**Solution:** Use `10.0.2.2` instead of `127.0.0.1` in MainActivity

### Issue: Device can't reach bot
**Solution:** Check firewall, use same WiFi network, or forward port correctly

### Issue: Build fails
**Solution:** 
1. Clean build: `./gradlew clean`
2. Update Gradle: `./gradlew wrapper`
3. Check JDK version: Minimum JDK 11 required

## 🚀 Deployment Checklist

- [ ] Bot API running successfully
- [ ] Android Studio installed and updated
- [ ] Project files organized correctly
- [ ] build.gradle configured
- [ ] API endpoint URL set correctly
- [ ] App builds successfully (no errors)
- [ ] App runs on device/emulator
- [ ] App connects to bot API
- [ ] All features working (status, balance, trades, start/stop)
- [ ] Testing completed

## 📚 Additional Resources

- [Kotlin Coroutines Documentation](https://kotlinlang.org/docs/coroutines-overview.html)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Retrofit Documentation](https://square.github.io/retrofit/)
- [Android Studio Guide](https://developer.android.com/studio/intro)
- [Flask Documentation](https://flask.palletsprojects.com/)

## 🤝 Support

For issues or questions:
1. Check the Troubleshooting section
2. Review logs in Android Studio Logcat
3. Test API manually with curl
4. Check network connectivity

---

**Version:** 1.0.0  
**Last Updated:** 2024
