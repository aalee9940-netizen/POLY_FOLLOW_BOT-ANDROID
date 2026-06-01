#!/bin/bash

# Polymarket Bot - Android Project Setup Script
# This script helps set up the Android project structure

set -e

echo "🚀 Polymarket Trading Bot - Android Setup"
echo "=========================================="
echo ""

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check if Android project path is provided
if [ $# -eq 0 ]; then
    echo "Usage: ./setup_android_project.sh /path/to/your/android/project"
    echo ""
    echo "Example:"
    echo "  ./setup_android_project.sh ~/Android/PolymarketBot"
    exit 1
fi

PROJECT_PATH="$1"
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

echo -e "${BLUE}Project Path: ${PROJECT_PATH}${NC}"
echo ""

# Verify project structure
if [ ! -d "$PROJECT_PATH" ]; then
    echo -e "${YELLOW}Creating project directory...${NC}"
    mkdir -p "$PROJECT_PATH"
fi

if [ ! -f "$PROJECT_PATH/app/build.gradle" ]; then
    echo -e "${YELLOW}⚠️  Android project structure not found.${NC}"
    echo "Please ensure you have created an Android project with Android Studio first."
    echo ""
    echo "Steps:"
    echo "1. Open Android Studio"
    echo "2. Create New Project > Empty Activity"
    echo "3. Set Name: PolymarketBot"
    echo "4. Set Package: com.polymarket.tradingbot"
    echo "5. Click Finish"
    echo ""
    exit 1
fi

echo -e "${GREEN}✓ Android project structure found${NC}"
echo ""

# Create necessary directories
echo -e "${BLUE}Creating directory structure...${NC}"

JAVA_PATH="$PROJECT_PATH/app/src/main/java/com/polymarket/tradingbot"
RES_PATH="$PROJECT_PATH/app/src/main/res"

mkdir -p "$JAVA_PATH/api"
mkdir -p "$JAVA_PATH/repository"
mkdir -p "$JAVA_PATH/ui"
mkdir -p "$JAVA_PATH/service"
mkdir -p "$RES_PATH/xml"
mkdir -p "$RES_PATH/values"

echo -e "${GREEN}✓ Directories created${NC}"
echo ""

# Copy Kotlin source files
echo -e "${BLUE}Copying Kotlin source files...${NC}"

if [ -f "$SCRIPT_DIR/BotApiService.kt" ]; then
    cp "$SCRIPT_DIR/BotApiService.kt" "$JAVA_PATH/api/"
    echo -e "${GREEN}✓ BotApiService.kt${NC}"
fi

if [ -f "$SCRIPT_DIR/BotRepository.kt" ]; then
    cp "$SCRIPT_DIR/BotRepository.kt" "$JAVA_PATH/repository/"
    echo -e "${GREEN}✓ BotRepository.kt${NC}"
fi

if [ -f "$SCRIPT_DIR/BotViewModel.kt" ]; then
    cp "$SCRIPT_DIR/BotViewModel.kt" "$JAVA_PATH/ui/"
    echo -e "${GREEN}✓ BotViewModel.kt${NC}"
fi

if [ -f "$SCRIPT_DIR/MainActivity.kt" ]; then
    cp "$SCRIPT_DIR/MainActivity.kt" "$JAVA_PATH/ui/"
    echo -e "${GREEN}✓ MainActivity.kt${NC}"
fi

echo ""

# Copy build configuration files
echo -e "${BLUE}Copying build configuration...${NC}"

if [ -f "$SCRIPT_DIR/build.gradle" ]; then
    cp "$SCRIPT_DIR/build.gradle" "$PROJECT_PATH/app/"
    echo -e "${GREEN}✓ build.gradle${NC}"
fi

if [ -f "$SCRIPT_DIR/proguard-rules.pro" ]; then
    cp "$SCRIPT_DIR/proguard-rules.pro" "$PROJECT_PATH/app/"
    echo -e "${GREEN}✓ proguard-rules.pro${NC}"
fi

if [ -f "$SCRIPT_DIR/AndroidManifest.xml" ]; then
    cp "$SCRIPT_DIR/AndroidManifest.xml" "$PROJECT_PATH/app/src/main/"
    echo -e "${GREEN}✓ AndroidManifest.xml${NC}"
fi

echo ""

# Create resource files
echo -e "${BLUE}Creating resource files...${NC}"

# strings.xml
cat > "$RES_PATH/values/strings.xml" << 'EOF'
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">Polymarket Bot Monitor</string>
    <string name="title_main">Trading Dashboard</string>
    <string name="action_refresh">Refresh</string>
    <string name="action_start">Start Bot</string>
    <string name="action_stop">Stop Bot</string>
</resources>
EOF
echo -e "${GREEN}✓ strings.xml${NC}"

# colors.xml
cat > "$RES_PATH/values/colors.xml" << 'EOF'
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
EOF
echo -e "${GREEN}✓ colors.xml${NC}"

# themes.xml
cat > "$RES_PATH/values/themes.xml" << 'EOF'
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <style name="Theme.PolymarketBot" parent="android:Theme.Material.Light">
        <item name="android:colorPrimary">@color/purple_500</item>
        <item name="android:colorSecondary">@color/teal_200</item>
    </style>
</resources>
EOF
echo -e "${GREEN}✓ themes.xml${NC}"

# network_security_config.xml
cat > "$RES_PATH/xml/network_security_config.xml" << 'EOF'
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">10.0.2.2</domain>
        <domain includeSubdomains="true">192.168.0.0</domain>
        <domain includeSubdomains="true">192.168.1.0</domain>
        <domain includeSubdomains="true">localhost</domain>
    </domain-config>
</network-security-config>
EOF
echo -e "${GREEN}✓ network_security_config.xml${NC}"

echo ""
echo -e "${GREEN}✅ Setup Complete!${NC}"
echo ""
echo "📋 Next Steps:"
echo "1. Open the project in Android Studio: ${PROJECT_PATH}"
echo "2. Update the API endpoint in MainActivity.kt if needed"
echo "3. Run: ./gradlew build"
echo "4. Start the bot API (Python backend)"
echo "5. Click Run in Android Studio to build and install"
echo ""
echo "For detailed instructions, see BUILD_AND_SETUP_GUIDE.md"
echo ""
