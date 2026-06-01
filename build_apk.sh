#!/bin/bash

# Polymarket Trading Bot - APK Build Script
# This script automates the APK building process

set -e

echo "================================"
echo "Polymarket Trading Bot - APK Builder"
echo "================================"
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check prerequisites
check_prerequisites() {
    echo -e "${YELLOW}[1/5] Checking prerequisites...${NC}"
    
    local missing=0
    
    # Check Python
    if ! command -v python3 &> /dev/null; then
        echo -e "${RED}✗ Python 3 not found${NC}"
        missing=1
    else
        echo -e "${GREEN}✓ Python 3 found${NC}"
    fi
    
    # Check Java
    if ! command -v java &> /dev/null; then
        echo -e "${RED}✗ Java not found${NC}"
        missing=1
    else
        echo -e "${GREEN}✓ Java found${NC}"
    fi
    
    # Check git
    if ! command -v git &> /dev/null; then
        echo -e "${RED}✗ Git not found${NC}"
        missing=1
    else
        echo -e "${GREEN}✓ Git found${NC}"
    fi
    
    if [ $missing -eq 1 ]; then
        echo -e "${RED}Missing required tools. Please install missing dependencies.${NC}"
        exit 1
    fi
    
    echo ""
}

# Install build tools
install_build_tools() {
    echo -e "${YELLOW}[2/5] Installing build tools...${NC}"
    
    pip3 install --upgrade pip
    pip3 install buildozer cython virtualenv
    
    echo -e "${GREEN}Build tools installed successfully${NC}"
    echo ""
}

# Setup project
setup_project() {
    echo -e "${YELLOW}[3/5] Setting up project...${NC}"
    
    # Check if config.json exists
    if [ ! -f "config.json" ]; then
        echo -e "${YELLOW}⚠ config.json not found. Creating template...${NC}"
        cat > config.json << 'EOF'
{
    "wallets": ["0x...replace_with_your_wallet"],
    "private_key": "replace_with_your_private_key",
    "funder": "replace_with_funder_address",
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
        echo -e "${YELLOW}⚠ Please edit config.json with your credentials before building${NC}"
        exit 1
    else
        echo -e "${GREEN}✓ config.json found${NC}"
    fi
    
    # Check if main.py is set to use Kivy
    if [ ! -f "main.py" ]; then
        echo -e "${YELLOW}⚠ main.py not found. Using main_android.py...${NC}"
        if [ -f "main_android.py" ]; then
            cp main_android.py main.py
        else
            echo -e "${RED}✗ Neither main.py nor main_android.py found${NC}"
            exit 1
        fi
    fi
    
    echo -e "${GREEN}Project setup complete${NC}"
    echo ""
}

# Build APK
build_apk() {
    echo -e "${YELLOW}[4/5] Building APK...${NC}"
    echo "This may take 30-60 minutes on first run..."
    echo ""
    
    # Clean previous builds
    echo "Cleaning previous builds..."
    buildozer android clean || true
    
    # Build debug APK
    echo "Building debug APK..."
    buildozer android debug
    
    if [ $? -ne 0 ]; then
        echo -e "${RED}✗ Build failed${NC}"
        exit 1
    fi
    
    echo -e "${GREEN}APK built successfully!${NC}"
    echo ""
}

# Package output
package_output() {
    echo -e "${YELLOW}[5/5] Finalizing build...${NC}"
    
    APK_FILE=$(find bin -name "*.apk" -type f 2>/dev/null | head -1)
    
    if [ -z "$APK_FILE" ]; then
        echo -e "${RED}✗ APK file not found${NC}"
        exit 1
    fi
    
    APK_SIZE=$(du -h "$APK_FILE" | cut -f1)
    
    echo -e "${GREEN}✓ Build complete!${NC}"
    echo ""
    echo "================================"
    echo "Build Summary"
    echo "================================"
    echo "APK Location: $(pwd)/$APK_FILE"
    echo "APK Size: $APK_SIZE"
    echo ""
    echo "Next steps:"
    echo "1. Connect Android device via USB"
    echo "2. Enable USB debugging on device"
    echo "3. Run: adb install -r $APK_FILE"
    echo "4. Launch app from device app drawer"
    echo ""
    echo "For more help, see ANDROID_BUILD_GUIDE.md"
}

# Main execution
main() {
    check_prerequisites
    install_build_tools
    setup_project
    build_apk
    package_output
}

# Run main function
main "$@"
