[app]

# Application title
title = Polymarket Trading Bot

# Package name
package.name = polymarket_bot

# Package domain
package.domain = org.trading.polymarket

# Source directory
source.dir = .

# Source includes (include the bot Python files)
source.include_exts = py,png,jpg,kv,atlas,json

# Version
version = 1.0.0

# Requirements
# Python 3.9+ recommended for Android
requirements = python3,kivy,requests,cryptography,websocket-client

# Orientation
orientation = portrait

# Icon
#icon.filename = %(source.dir)s/data/icon.png

# Presplash
#presplash.filename = %(source.dir)s/data/presplash.png

# Permissions
android.permissions = INTERNET,ACCESS_NETWORK_STATE,WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE

# Features
android.features = android.hardware.internet

# API target
android.api = 31

# Minimum API level
android.minapi = 21

# NDK version
android.ndk = 25b

# Grant permissions at install time (Android 6+)
android.gradle_dependencies = 

# Java classes to import
#android.add_src = 

# Architecture to build for
android.archs = arm64-v8a,armeabi-v7a

# Java imports
android.java_classes = 

# Service to add for background processes
android.services = org.renpy.android.PythonService:./service/main.py

# Broadcast receiver
android.broadcast_receiver = 

# Use legacy build tools
android.use_legacy_toolchain = False

# Bootstrap
p4a.bootstrap = sdl2

# Analytics
analytics_gaid = 

# Generate presplash
android.generate_presplash = 1

# Use legacy build tools
android.logcat_filters = *:S python:D

# Release config
[buildozer]

# Log level (0 = error only, 1 = info, 2 = debug (with command output))
log_level = 2

# Display warnings
warn_on_root = 1

# Archive mode (0 = no archive, 1 = zip archive, 2 = tar.gz archive)
archive_mode = 0

# Build directory
build_dir = .buildozer

# Binary directory
bin_dir = ./bin
