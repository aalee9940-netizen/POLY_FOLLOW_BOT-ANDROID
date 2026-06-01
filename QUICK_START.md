# Polymarket Trading Bot Android APK - Quick Start

**Ready to build your Android APK in minutes?** Choose your preferred method below.

---

## 🚀 Option 1: Fastest Setup with Docker (Recommended)

No need to install Java, Android SDK, or NDK. Docker handles everything!

### Prerequisites
- Docker Desktop installed ([download here](https://www.docker.com/products/docker-desktop))
- Your Polymarket credentials

### Quick Build (3 steps)

```bash
# 1. Edit config.json with your credentials
nano config.json  # Replace with your private_key, funder, wallets

# 2. Start Docker build
docker-compose up

# 3. Get your APK
ls -lh output/bin/*.apk
```

**Done!** Your APK is in `output/bin/` ready to install.

---

## 💻 Option 2: Local Build (Linux/macOS)

### Prerequisites
```bash
# Install dependencies
sudo apt-get install -y python3 python3-dev python3-pip openjdk-11-jdk
# macOS: brew install openjdk@11

# Install build tools
pip3 install buildozer cython
```

### Quick Build (2 steps)

```bash
# 1. Prepare project
chmod +x build_apk.sh
./build_apk.sh

# 2. Follow prompts to setup and build
# Build takes ~30-60 minutes on first run
```

**Done!** Your APK is in `bin/` ready to install.

---

## 📱 Option 3: Windows with WSL2

### Quick Setup

```bash
# In WSL2 terminal
wsl --install -d Ubuntu-22.04

# Inside Ubuntu:
sudo apt-get update
sudo apt-get install -y python3 python3-pip openjdk-11-jdk build-essential

pip3 install buildozer cython
./build_apk.sh
```

---

## 📲 Install on Your Android Device

### Step 1: Prepare Device
```bash
# Enable USB Debugging:
# Settings > About Phone > Build Number (tap 7 times)
# Settings > Developer Options > USB Debugging > ON
```

### Step 2: Install APK
```bash
# Connect device via USB cable
adb install -r bin/polymarket_bot-1.0.0-debug.apk

# Or install via file transfer:
# 1. Copy APK to device
# 2. Open Files app > tap APK file > Install
```

### Step 3: Run the App
1. Open "Polymarket Trading Bot" from app drawer
2. Click **"Start Bot"** to begin trading
3. Monitor real-time logs and statistics

---

## ⚙️ Configuration Guide

### Edit config.json Before Building

```json
{
    "wallets": ["0x123...your_address"],      // Wallet(s) to monitor
    "private_key": "0xabc...your_key",        // Your Polygon private key
    "funder": "0xdef...funder_addr",          // Funder address
    "signature_type": 2,                       // Usually 2 for EOA
    "poll_interval": 10,                       // Check interval (seconds)
    
    "price_filter": {                          // Filter by price range
        "enabled": false,
        "min_price": 0.3,
        "max_price": 0.85
    },
    
    "no_duplicate": {                          // Prevent repeat trades
        "enabled": true,
        "expire_seconds": 3600
    },
    
    "tp": {                                    // Take profit
        "enabled": false,
        "type": "price",
        "value": 0.99
    },
    
    "sl": {                                    // Stop loss
        "enabled": false,
        "type": "percent",
        "value": 0.5
    }
}
```

---

## 🔒 Security Checklist

⚠️ **IMPORTANT**: Your private key will be on the Android device!

- ✅ Use a dedicated phone for the bot (not your personal device)
- ✅ Enable device encryption in Settings
- ✅ Use a strong lock screen PIN (6+ digits)
- ✅ Keep only trading funds on the account (~$50-$500 max)
- ✅ Store main assets in a cold wallet
- ✅ Regularly check transaction history
- ✅ Never share or screenshot the config.json file
- ✅ Consider using a VPN for network traffic

---

## 📊 App Features

Once installed, the app shows:

| Feature | Description |
|---------|-------------|
| **Status** | Running/Stopped indicator |
| **Balance** | Current USDC balance |
| **Trades** | Total trades executed |
| **Win Rate** | Success percentage |
| **Logs** | Real-time activity feed |
| **Start/Stop** | Control bot execution |
| **Settings** | Adjust poll interval, filters |

---

## ❌ Troubleshooting

### "Buildozer not found"
```bash
pip3 install --upgrade buildozer cython
```

### "Java not found"
```bash
# Install Java
sudo apt-get install openjdk-11-jdk
# Or macOS: brew install openjdk@11
```

### "Build failed with permission error"
```bash
# Clear cache and rebuild
buildozer android clean
./build_apk.sh
```

### "APK won't install"
```bash
# Uninstall old version
adb uninstall org.trading.polymarket

# Install new APK
adb install -r bin/polymarket_bot-1.0.0-debug.apk
```

### "App crashes on startup"
1. Check device has ~500MB free storage
2. Verify config.json is valid (no syntax errors)
3. Ensure all bot dependencies are included
4. Check logs: `adb logcat | grep python`

---

## 📞 Getting Help

- **Full guide**: See `ANDROID_BUILD_GUIDE.md`
- **Docker issues**: Run `docker-compose logs`
- **Build logs**: Check `.buildozer/android/platform/build-*/build.log`
- **APK installation**: Run `adb logcat` to see errors

---

## 📦 Build File Locations

After successful build, you'll find:

```
project/
├── bin/
│   └── polymarket_bot-1.0.0-debug.apk    ← Your APK file
├── .buildozer/
│   └── android/platform/...               ← Build intermediate files
├── output/                                 ← Docker output (if using Docker)
│   └── bin/
│       └── polymarket_bot-1.0.0-debug.apk
└── build_apk.sh                           ← Build script
```

---

## 🎯 Next Steps

1. **Choose a build method** (Docker recommended)
2. **Edit config.json** with your credentials
3. **Run the build** (Docker or local)
4. **Install on device** via adb or file transfer
5. **Start trading!** Click Start Bot in the app

---

## 📝 Tips for Success

- **Test first**: Run on a test device/account first
- **Start small**: Use small bet amounts initially
- **Monitor regularly**: Check app logs daily
- **Keep device charged**: Trading bot runs continuously
- **Use WiFi**: More reliable than mobile data
- **Backup config**: Keep a copy of config.json (secured)
- **Update regularly**: Check for bot updates periodically

---

## 🔐 Support & Security

- **Never share** your config.json or private key
- **Don't use** on rooted/jailbroken devices
- **Report bugs** if you find any issues
- **Check logs** if bot stops unexpectedly
- **Verify transactions** on Polymarket regularly

---

## 🚀 Performance Notes

- **Battery**: Use ~5-10% per hour depending on poll_interval
- **Data**: Uses minimal data (mostly API calls)
- **Storage**: APK is ~200MB, requires ~500MB free space
- **RAM**: Requires ~200-300MB RAM
- **CPU**: Low CPU usage, runs background service

---

**Version**: 1.0.0  
**Last Updated**: 2024  
**Status**: Production Ready

Enjoy your automated Polymarket trading! 🎉
