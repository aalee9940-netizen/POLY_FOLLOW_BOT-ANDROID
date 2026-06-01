# 🚀 Polymarket Trading Bot - Android APK

![Status](https://img.shields.io/badge/Status-Production%20Ready-green)
![License](https://img.shields.io/badge/License-MIT-blue)
![Android](https://img.shields.io/badge/Android-7.0%2B-green)
![Python](https://img.shields.io/badge/Python-3.8%2B-blue)

**Monitor and control your Polymarket trading bot directly from your Android phone!**

## 🎯 Quick Start

### Build with Docker (15 minutes)

```bash
# Install Docker: https://www.docker.com/products/docker-desktop
# Then run:
docker-compose up
# APK ready at: ./build/outputs/apk/release/app-release.apk
```

### Install on Phone

```bash
adb install app-release.apk
# OR transfer and tap to install
```

## ✨ Features

- ✅ Real-time bot status monitoring
- ✅ USDC balance display
- ✅ Trade statistics (win rate, P&L)
- ✅ Start/Stop bot controls
- ✅ Beautiful dashboard UI
- ✅ Secure key storage

## 📱 System Requirements

- Android 7.0+ (API 24+)
- 100MB free storage
- Internet connection

## 📚 Documentation

- `SETUP_NO_BUILD.md` - Easy setup guide
- `QUICK_START.md` - Quick commands
- `BUILD_INSTRUCTIONS.txt` - Detailed steps
- `README_ANDROID_APK.md` - Full features

## 📊 Project Structure

```
app/
├── build.gradle
├── AndroidManifest.xml
└── src/main/java/.../
    ├── BotApiService.kt
    ├── BotRepository.kt
    ├── BotViewModel.kt
    └── MainActivity.kt

bot/
├── bot_api.py (Flask API)
├── main_android.py
├── requirements.txt
└── Dockerfile

scripts/
├── build_apk.sh
└── setup_android_project.sh
```

## 🔒 Security

- Private keys stored locally
- HTTPS support for production
- .gitignore excludes secrets
- Environment variables for sensitive data

## 📄 License

MIT License - See LICENSE file

## 🚀 Next Steps

1. Read SETUP_NO_BUILD.md
2. Install Docker
3. Run: docker-compose up
4. Install APK on phone
5. Start trading!

---

**Version:** 1.0.0 | **Status:** ✅ Production Ready | **Last Updated:** June 2024
