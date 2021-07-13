# Android Firebase App

This is a sample android app for testing Firebase Push notifications.

## Build

This app can be built for debug mode -

```
./gradlew assembleDebug

ls -la app/build/outputs/apk/debug/

```

For production mode APK -

```
./gradlew assemble

ls -la app/build/outputs/apk/release/

```

## Firebase configs

The following Firebase config file is required before running this app.

- google-services.json - This file can be generated from the Firebase console after registering the app, and should be
placed at root of this project or inside the ``<project_root>/app` directory.

## Tools and IDE

- Android Studio
- Kotlin