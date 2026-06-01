# Polymarket Trading Bot - Android APK Build Guide

This guide will help you build and run the Polymarket Trading Bot as an Android APK.

## Prerequisites

### System Requirements
- **Linux** or **macOS** (Windows is not officially supported for Buildozer, but WSL can work)
- **Python 3.9+** installed
- **Java Development Kit (JDK) 11+** installed
- **Android SDK** with API level 31+
- **Android NDK** version 25b
- **Git** installed

### Install Required Tools

#### 1. Install Java JDK
```bash
# macOS
brew install openjdk@11

# Ubuntu/Debian
sudo apt-get install openjdk-11-jdk

# Add to PATH if needed
export JAVA_HOME=/usr/libexec/java_home -v 11
```

#### 2. Install Android SDK and NDK
Download from: https://developer.android.com/studio

Or use command line:
```bash
# Install Android SDK Command Line Tools
wget https://dl.google.com/android/repository/commandlinetools-linux-10406996_latest.zip
unzip commandlinetools-linux-10406996_latest.zip -d ~/android-sdk

# Set environment variables
export ANDROID_SDK_ROOT=~/android-sdk
export ANDROID_NDK_ROOT=~/android-ndk/r25b
export PATH=$PATH:$ANDROID_SDK_ROOT/tools:$ANDROID_SDK_ROOT/platform-tools
```

#### 3. Install Required Android Components
```bash
# Install SDK tools
$ANDROID_SDK_ROOT/tools/bin/sdkmanager "platform-tools" "platforms;android-31" "ndk;25.2.9519653"
```

## Setup and Build

### Step 1: Install Build Dependencies

```bash
# Install Buildozer and Cython
pip install --upgrade pip
pip install buildozer cython

# Install system dependencies
# macOS
brew install libffi libjpeg

# Ubuntu/Debian
sudo apt-get install -y \
    python3-pip \
    build-essential \
    libffi-dev \
    libssl-dev \
    libjpeg-dev \
    zlib1g-dev \
    autoconf \
    libtool \
    pkg-config \
    git \
    openjdk-11-jdk \
    ant \
    ccache
```

### Step 2: Prepare Your Project

```bash
# 1. Create a new directory for the project
mkdir polymarket-bot-android
cd polymarket-bot-android

# 2. Copy all bot files here
cp -r ../poly_follow_bot-master/* .

# 3. Copy the Kivy main file
cp ../main_android.py main.py

# 4. Copy buildozer.spec
cp ../buildozer.spec .

# 5. Copy requirements.txt
cp ../requirements.txt .

# 6. Create config.json with your settings
cat > config.json << 'EOF'
{
    "wallets": ["0x...your_wallet_address"],
    "private_key": "your_private_key_here",
    "funder": "funder_address_here",
    "signature_type": 2,
    "poll_interval": 10,
    "discord_webhook_url": "",
    "price_filter": {
        "enabled": false,
        "min_price": 0.3,
        "max_price": 0.85
    },
    "no_duplicate": {
        "enabled": true,
        "expire_seconds": 3600
    },
    "tp": {
        "enabled": false,
        "type": "price",
        "value": 0.99
    },
    "sl": {
        "enabled": false,
        "type": "percent",
        "value": 0.5
    }
}
EOF
```

### Step 3: Update buildozer.spec

Edit `buildozer.spec` and update these fields:

```spec
[app]
title = Polymarket Trading Bot
package.name = polymarket_bot
package.domain = org.trading.polymarket

[buildozer]
log_level = 2
```

### Step 4: Build the APK

**Option A: Debug APK** (For testing)
```bash
buildozer android debug
```

**Option B: Release APK** (For production)
```bash
buildozer android release
```

The build process takes **30-60 minutes** on first run (downloads SDK, NDK, and dependencies).

### Step 5: Find Your APK

After successful build:
```bash
# Debug APK
ls -lh bin/polymarket_bot-1.0.0-debug.apk

# Release APK
ls -lh bin/polymarket_bot-1.0.0-release-unsigned.apk
```

## Installation on Android Device

### Method 1: USB Debug Mode
```bash
# Enable Developer Mode on Android device:
# Settings → About Phone → Tap Build Number 7 times
# Settings → Developer Options → Enable USB Debugging

# Connect device via USB and install:
adb install -r bin/polymarket_bot-1.0.0-debug.apk
```

### Method 2: APK File Transfer
1. Transfer APK to your Android device via USB/email
2. Open file manager on device
3. Locate and tap the APK file
4. Accept permissions and install

## Running the Bot on Android

1. **Open the app** from your device's app drawer
2. **Review the status**:
   - Green "Running" = Bot is active
   - Red "Stopped" = Bot is inactive
3. **Click "Start Bot"** to begin monitoring
4. **Monitor real-time logs** in the app
5. **Check balance** and trade statistics
6. **Use "Settings"** to adjust parameters
7. **Click "Stop Bot"** to halt trading

## Important Security Notes

### Private Key Storage
⚠️ **CRITICAL**: Your private key is stored in `config.json` on the device.

**Protect your device:**
- Use a strong lock screen password
- Enable device encryption
- Limit app access to essential accounts only
- Never share your device with untrusted users
- Consider using a dedicated device for trading

### Recommended Setup
1. Use a separate Android device for the bot (not your personal phone)
2. Use a hardware wallet or cold wallet for most funds
3. Keep only trading amounts on the Polymarket account
4. Enable 2FA on associated accounts when possible
5. Regularly audit transaction history

## Troubleshooting

### Build Fails with "Module not found"
```bash
# Update buildozer requirements
pip install --upgrade buildozer cython

# Clear cache and rebuild
buildozer android clean
buildozer android debug
```

### "java.exe not found"
Ensure JAVA_HOME is set correctly:
```bash
export JAVA_HOME=/path/to/jdk
buildozer android debug
```

### APK installation fails
```bash
# Uninstall previous version
adb uninstall org.trading.polymarket

# Install new version
adb install -r bin/polymarket_bot-1.0.0-debug.apk
```

### App crashes on startup
1. Check Android logs: `adb logcat | grep python`
2. Verify `config.json` is valid JSON
3. Ensure all bot dependencies are installed
4. Check if `main.py` is properly configured

### Module import errors at runtime
Update requirements.txt and rebuild:
```bash
# Add missing package to requirements.txt
echo "missing_package>=1.0.0" >> requirements.txt

# Rebuild
buildozer android clean
buildozer android debug
```

## Performance Tips

### Optimize for Battery
- Reduce poll_interval in config.json (larger = less frequent checks)
- Use WiFi instead of mobile data
- Enable battery saver mode carefully (might pause bot)

### Optimize for Storage
- Clear app logs periodically
- Use external storage for config files
- Clean build cache: `buildozer android clean`

### Network Optimization
- Use stable WiFi connection
- Consider running on a server instead for 24/7 reliability
- Monitor data usage (trading requests use minimal bandwidth)

## Advanced Configuration

### Running as Background Service
Edit `main.py` to add background service:
```python
from kivy.core.window import Window
from android.app import AndroidService

# Enable background execution
service = AndroidService('Your Background Service', 'background_service.py')
```

### Logging to File
```python
import logging

logging.basicConfig(
    filename='/sdcard/polymarket_bot.log',
    level=logging.DEBUG,
    format='%(asctime)s - %(levelname)s - %(message)s'
)
```

### Pushing Updates
```bash
# Send new version to device
adb push main.py /data/data/org.trading.polymarket/

# Or rebuild APK with updates
buildozer android debug
adb install -r bin/polymarket_bot-1.0.0-debug.apk
```

## Uninstalling

```bash
adb uninstall org.trading.polymarket
# Or remove from device Settings → Apps → Polymarket Trading Bot
```

## Additional Resources

- **Buildozer Docs**: https://buildozer.readthedocs.io/
- **Kivy Docs**: https://kivy.org/doc/stable/
- **Android NDK**: https://developer.android.com/ndk/
- **Polymarket API**: https://clob.polymarket.com/docs
- **Python on Android**: https://github.com/kivy/python-for-android

## Support

If you encounter issues:
1. Check the troubleshooting section above
2. Review build logs: `cat .buildozer/android/platform/build-[arch]/build/other_builds/python3/[arch]/python3/`
3. Test on a development device first
4. Report issues with complete logs and error messages

---

**Last Updated**: 2024
**Version**: 1.0.0
