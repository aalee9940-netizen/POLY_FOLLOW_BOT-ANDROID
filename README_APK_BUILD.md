# Polymarket Trading Bot - Android APK Build Package

## 📦 Package Contents

This package contains everything needed to build your Polymarket trading bot as an Android APK.

### Files Included

```
├── main_android.py              # Kivy UI wrapper for the bot
├── bot_api.py                   # REST API for bot control
├── buildozer.spec               # Android build configuration
├── requirements.txt             # Python dependencies
├── polymarket.kv                # Kivy UI layout file
├── Dockerfile                   # Docker build environment
├── docker-compose.yml           # Docker Compose configuration
├── build_apk.sh                 # Automated build script
├── QUICK_START.md               # Quick start guide (READ THIS FIRST!)
├── ANDROID_BUILD_GUIDE.md       # Detailed build guide
└── README_APK_BUILD.md          # This file
```

---

## 🚀 Getting Started (Choose One Method)

### Method 1: Docker Build (Easiest & Recommended) ⭐

**Advantages:**
- ✅ No local setup required
- ✅ Works on any OS (Windows, macOS, Linux)
- ✅ Consistent build environment
- ✅ Fastest to get started

**Steps:**
```bash
# 1. Install Docker Desktop
# Visit: https://www.docker.com/products/docker-desktop

# 2. Edit config.json with your credentials
vim config.json

# 3. Build APK with Docker
docker-compose up

# 4. Find your APK
ls -lh output/bin/polymarket_bot-*.apk
```

**Build Time:** 45-90 minutes (first run), 10-15 minutes (subsequent)

---

### Method 2: Shell Script Build (Linux/macOS)

**Steps:**
```bash
# 1. Make script executable
chmod +x build_apk.sh

# 2. Run the build script
./build_apk.sh

# 3. Follow interactive prompts
# The script will:
# - Check prerequisites
# - Install build tools
# - Build the APK

# 4. Find your APK
ls -lh bin/polymarket_bot-*.apk
```

**Prerequisites:**
- Python 3.9+
- Java JDK 11+
- Git
- ~50GB disk space

---

### Method 3: Manual Build (Advanced)

For detailed manual setup, see `ANDROID_BUILD_GUIDE.md`

---

## 📱 Installation on Android Device

### Via ADB (Fastest)
```bash
# 1. Enable USB debugging on Android device:
#    Settings → About Phone → Build Number (tap 7 times)
#    Settings → Developer Options → USB Debugging

# 2. Connect device via USB
# 3. Install APK
adb install -r bin/polymarket_bot-1.0.0-debug.apk
```

### Via File Transfer
```bash
# 1. Copy APK to your device via USB/email/cloud
# 2. Open Files app on device
# 3. Locate the APK file
# 4. Tap to install
# 5. Grant permissions as requested
```

---

## ⚙️ Configuration

### Edit config.json

```json
{
    "wallets": [
        "0x123...abc",           // Wallet address(es) to monitor
        "0x456...def"            // Add multiple wallets as needed
    ],
    
    "private_key": "0xabc...123",  // Your Polygon private key
    "funder": "0xdef...456",       // Funder address
    "signature_type": 2,           // Usually 2 for EOA wallets
    "poll_interval": 10,           // Check interval in seconds
    
    "discord_webhook_url": "",     // Optional: Discord notifications
    
    "price_filter": {
        "enabled": false,
        "min_price": 0.3,          // Only trade in this price range
        "max_price": 0.85
    },
    
    "no_duplicate": {
        "enabled": true,
        "expire_seconds": 3600     // Prevent repeat trades within 1 hour
    },
    
    "tp": {                        // Take Profit
        "enabled": false,
        "type": "price",
        "value": 0.99              // Sell at 0.99
    },
    
    "sl": {                        // Stop Loss
        "enabled": false,
        "type": "percent",
        "value": 0.5               // Sell if down 50%
    }
}
```

---

## 🎮 Using the App

### Dashboard Features

| Feature | What It Shows |
|---------|--------------|
| **Status** | Bot running/stopped (green/red) |
| **Balance** | Current USDC balance |
| **Trades** | Total trades executed |
| **Win Rate** | Success percentage |
| **Activity Log** | Real-time trade execution log |

### Controls

- **START BOT** - Begin monitoring wallets and executing trades
- **STOP BOT** - Halt bot operation
- **SETTINGS** - Adjust poll interval and filters

### Monitoring

The app displays:
- Current USDC balance
- Number of trades executed
- Win rate percentage
- Real-time activity log with timestamps

---

## 🔐 Security Best Practices

⚠️ **CRITICAL**: Your private key will be stored on this device!

### Recommended Setup
1. Use a **dedicated Android phone** (not your personal device)
2. Enable **device encryption** in settings
3. Use a **strong lock screen PIN** (6+ digits, no patterns)
4. Keep **only trading funds** on the account
5. Store **main assets** in a hardware wallet
6. Never **share your device** with others
7. Regularly **check transaction history**
8. **Backup config.json** (keep it secure offline)

### Network Security
- Use a **secure WiFi** connection (home/business, not public)
- Consider using a **VPN** for additional security
- Enable **Find My Mobile** for device tracking
- Keep **device updated** with latest security patches

### Financial Safety
- Start with **small bet amounts** ($10-$50)
- **Monitor trades daily** for unusual activity
- **Set daily limits** on trading amounts
- **Report suspicious** activity immediately
- **Verify transactions** on Polymarket website

---

## 📊 Performance & Optimization

### Battery Usage
- Idle: ~2-3% per hour
- Active trading: ~5-10% per hour
- Solution: Keep device plugged in

### Storage
- APK size: ~200 MB
- Required free space: ~500 MB
- Config file: ~5 KB
- Logs: ~100 MB (auto-truncated)

### Network
- Data usage: ~1-5 MB per day (minimal)
- Bandwidth: <1 Mbps average
- Connection: Stable WiFi recommended

### RAM
- Idle memory: ~150-200 MB
- Peak memory: ~300-400 MB
- Device minimum: 2GB RAM (4GB+ recommended)

### CPU
- CPU usage: <5% average
- Low thermal impact
- Can run on budget phones

---

## ❌ Troubleshooting

### Build Issues

**"Python 3 not found"**
```bash
# Linux
sudo apt-get install python3 python3-pip

# macOS
brew install python3
```

**"Java not found"**
```bash
# Linux
sudo apt-get install openjdk-11-jdk

# macOS
brew install openjdk@11
```

**"Buildozer not found"**
```bash
pip3 install --upgrade buildozer cython
```

**Build fails with permission error**
```bash
buildozer android clean
./build_apk.sh  # or Docker
```

### Installation Issues

**"APK won't install"**
```bash
# Check device storage
adb shell df -h

# Uninstall old version
adb uninstall org.trading.polymarket

# Install fresh
adb install -r bin/polymarket_bot-1.0.0-debug.apk
```

**"Unknown source" error**
```
Android 10+: Settings → Apps & Notifications → Advanced → 
Special App Access → Install Unknown Apps → Allow from Files
```

### Runtime Issues

**"App crashes on startup"**
1. Check device has 500MB+ free storage
2. Verify config.json is valid JSON
3. Check logs: `adb logcat | grep polymarket`
4. Try uninstalling and reinstalling

**"Bot won't start"**
1. Ensure config.json has valid credentials
2. Check internet connection
3. Verify wallet has USDC balance
4. Check app permissions in Settings

**"Very slow or freezes"**
1. Increase poll_interval in config.json
2. Reduce number of monitored wallets
3. Restart the device
4. Check available RAM: `adb shell free`

---

## 📝 Building from Source

### Step-by-Step

```bash
# 1. Clone/download the repository
git clone <repo-url>
cd polymarket-bot-android

# 2. Setup Python virtual environment (optional)
python3 -m venv venv
source venv/bin/activate  # Linux/macOS
# or
venv\Scripts\activate  # Windows

# 3. Install Python dependencies
pip3 install -r requirements.txt

# 4. Configure buildozer.spec
# Edit if needed for your setup

# 5. Build APK
buildozer android debug
# or
docker-compose up

# 6. Wait for build to complete (30-90 minutes first time)

# 7. Find APK
ls bin/polymarket_bot-1.0.0-debug.apk
```

---

## 🔄 Updating the Bot

### Update Bot Code
```bash
# 1. Update Python files
# 2. Rebuild APK
docker-compose up
# or
buildozer android clean
buildozer android debug

# 3. Uninstall old version
adb uninstall org.trading.polymarket

# 4. Install new version
adb install -r bin/polymarket_bot-1.0.0-debug.apk
```

### Update Configuration
Edit `config.json` and rebuild, or manually update on device if possible.

---

## 📚 Additional Resources

### Documentation
- **QUICK_START.md** - Get up and running fast
- **ANDROID_BUILD_GUIDE.md** - Comprehensive build guide
- **Buildozer Docs** - https://buildozer.readthedocs.io/
- **Kivy Docs** - https://kivy.org/doc/stable/

### External Tools
- **Android Studio** - https://developer.android.com/studio
- **ADB Installation** - https://developer.android.com/tools/adb
- **Docker Desktop** - https://www.docker.com/products/docker-desktop

### Polymarket Resources
- **API Docs** - https://clob.polymarket.com/docs
- **Trading Guide** - https://polymarket.com/learn
- **Risk Disclaimer** - https://polymarket.com/disclaimer

---

## ⚖️ Legal & Disclaimers

### Risk Warning
- Trading involves **substantial risk of loss**
- Past performance does not guarantee future results
- Automated trading can amplify losses
- Cryptocurrency markets are highly volatile
- You may lose your entire investment

### Compliance
- Check local regulations before automated trading
- Ensure compliance with your jurisdiction
- Verify tax reporting requirements
- Use at your own risk

### Support
This is a community-built project. Use at your own risk and do your own research.

---

## 🤝 Contributing & Support

### Issues & Bug Reports
1. Check existing issues on GitHub
2. Provide detailed error logs
3. Include Android version and device model
4. Describe steps to reproduce

### Getting Help
1. **Quick issues**: See QUICK_START.md troubleshooting
2. **Build issues**: Check `.buildozer/` logs
3. **Runtime issues**: Check `adb logcat` output
4. **General help**: Consult ANDROID_BUILD_GUIDE.md

---

## 📋 Checklist Before Building

- ✅ Read QUICK_START.md
- ✅ Have valid Polymarket credentials
- ✅ Confirmed wallet has USDC balance
- ✅ Edited config.json with your settings
- ✅ Choose build method (Docker recommended)
- ✅ Have ~50GB free disk space
- ✅ Prepared Android device for installation
- ✅ Read security best practices section

---

## 🎉 You're Ready!

Choose your build method from the Getting Started section and follow the steps. Good luck with your Polymarket trading!

---

**Version:** 1.0.0  
**Last Updated:** 2024  
**Status:** Production Ready  

**Questions?** See the detailed guides or check the troubleshooting section.
