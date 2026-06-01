# Dockerfile for building Polymarket Trading Bot APK
# This eliminates complex local setup requirements

FROM ubuntu:22.04

# Prevent interactive prompts
ENV DEBIAN_FRONTEND=noninteractive
ENV ANDROID_HOME=/android-sdk
ENV ANDROID_NDK_ROOT=/android-ndk
ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64

# Install dependencies
RUN apt-get update && apt-get install -y \
    build-essential \
    ccache \
    curl \
    git \
    libffi-dev \
    libjpeg-dev \
    libssl-dev \
    libtool \
    libzlib-ncurses-dev \
    openjdk-11-jdk \
    pkg-config \
    python3 \
    python3-dev \
    python3-pip \
    unzip \
    wget \
    zip \
    zlib1g-dev \
    autoconf \
    automake \
    ant \
    && rm -rf /var/lib/apt/lists/*

# Install Python build tools
RUN pip3 install --upgrade pip && \
    pip3 install buildozer cython virtualenv

# Create directories for SDK/NDK
RUN mkdir -p $ANDROID_HOME $ANDROID_NDK_ROOT

# Download and install Android SDK
RUN wget -q https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip -O /tmp/sdk-tools.zip && \
    unzip -q /tmp/sdk-tools.zip -d /tmp/sdk-tools && \
    mkdir -p $ANDROID_HOME/cmdline-tools && \
    mv /tmp/sdk-tools/cmdline-tools $ANDROID_HOME/cmdline-tools/latest && \
    rm -rf /tmp/sdk-tools /tmp/sdk-tools.zip

# Set SDK paths
ENV PATH=$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools:$ANDROID_HOME/tools/bin:$PATH

# Install Android components
RUN yes | sdkmanager --sdk_root=$ANDROID_HOME "platform-tools" "platforms;android-31" "build-tools;31.0.0" && \
    wget -q https://dl.google.com/android/repository/android-ndk-r25b-linux.zip -O /tmp/ndk.zip && \
    unzip -q /tmp/ndk.zip -d /tmp && \
    mv /tmp/android-ndk-r25b/* $ANDROID_NDK_ROOT && \
    rm -rf /tmp/ndk.zip /tmp/android-ndk-r25b

# Create app directory
WORKDIR /app

# Copy project files
COPY . /app/

# Build APK
RUN buildozer android debug

# Make build output accessible
RUN mkdir -p /output && \
    cp -r bin /output/ || true && \
    cp -r .buildozer /output/ || true

VOLUME ["/output"]

CMD ["/bin/bash"]
