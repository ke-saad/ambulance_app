# Step 1: Create the build image
FROM gradle:8.9-jdk17 AS builder

# Set the working directory
WORKDIR /home/gradle/project

# Copy Gradle and project files
COPY --chown=gradle:gradle gradlew ./gradlew
COPY --chown=gradle:gradle gradle/ ./gradle/
COPY --chown=gradle:gradle build.gradle.kts settings.gradle.kts local.properties ./

# Install necessary tools
RUN apt-get update && apt-get install -y wget unzip

# Download and set up the Android SDK
RUN wget -q https://dl.google.com/android/repository/commandlinetools-linux-10406996_latest.zip && \
    unzip -q commandlinetools-linux-10406996_latest.zip -d /opt/android-sdk && \
    mkdir -p /opt/android-sdk/cmdline-tools/latest && \
    mv /opt/android-sdk/cmdline-tools/bin /opt/android-sdk/cmdline-tools/latest/ && \
    mv /opt/android-sdk/cmdline-tools/lib /opt/android-sdk/cmdline-tools/latest/ && \
    mv /opt/android-sdk/cmdline-tools/NOTICE.txt /opt/android-sdk/cmdline-tools/latest/ && \
    mv /opt/android-sdk/cmdline-tools/source.properties /opt/android-sdk/cmdline-tools/latest/

ENV ANDROID_HOME="/opt/android-sdk"
ENV PATH="$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools"

# Install Android SDK components
RUN yes | sdkmanager --sdk_root=/opt/android-sdk --licenses > /dev/null
RUN sdkmanager --sdk_root=/opt/android-sdk "platforms;android-35" "build-tools;35.0.0"

# Touch a file to force Docker cache invalidation
RUN touch /tmp/new_cache

# Copy application source code
COPY --chown=gradle:gradle app ./app

# Build the application
RUN ./gradlew clean assembleDebug --no-daemon --stacktrace

# Step 2: Create the runtime image
FROM ubuntu:20.04 AS release

# Create a non-root user
RUN useradd -m -u 1001 appuser

# Switch to the new user
USER appuser
WORKDIR /app

# Copy the built APK
COPY --from=builder /home/gradle/project/app/build/outputs/apk/debug/app-debug.apk /app/app-debug.apk

# Set the entrypoint
CMD ["java", "-jar", "app-debug.apk"]