# web-to-app

The easy and fast way to create a native android app for a website.

## Getting Started
1. Download and Install Android Studio
2. Modify `MainActivity.java`, by defaut web-to-app will open https://successtar.github.io/ on launch
3. Build the application as an APK or debug it in Android Studio's Emulator.
4. Transfer the APK to your device.

## Customisation

### Default URL
Change the `defaultUrl` value in `MainActivity.java` to the URL you want the app to open on.

### Restricting Navagitaiton 
Change the `allowedUrlPrefixes` to prevent the user navigating outside of a given list of allowed URLs. The user will be shown a Toast notification if they attempt to navigate to a URL that is not allowed.

## App Icon and Name
You can do customization such  as changing the app icon with your logo and other rich features.
