# ML Learn

ML Learn is an offline Android app for exploring machine-learning ideas through short theory lessons and interactive playgrounds. It is built with Kotlin and Jetpack Compose. The algorithm engines use Kotlin math only; the app makes no network requests and does not use external ML libraries.

## Lessons

Every lesson has a **Theory** tab and a **Playground** tab with controls, a live plot, and a result summary.

- Gradient Descent: batch, SGD, and mini-batch variants on a quadratic loss
- Loss Functions: MSE, MAE, and cross-entropy
- Bias–Variance Tradeoff: polynomial model complexity and training noise
- Simple Linear Regression: gradient-descent line fitting
- Logistic Regression: sigmoid probabilities and a moving decision boundary
- K-Means: interactive centroid updates
- Spectral Clustering: concentric-ring adjacency graph and connected components
- Perceptron: online updates and a linear decision boundary
- MLP + Backpropagation: a small network trained on XOR
- Confusion Matrix: threshold, class balance, classification metrics, and ROC plot

The spectral lesson uses a deliberately disconnected neighborhood graph so its connected components illustrate the Laplacian's zero-eigenvalue intuition. It is not a general-purpose eigenvector solver.

## Requirements

- Android Studio with Android SDK Platform 34 installed
- JDK 17 (Android Studio's bundled JDK is suitable)
- An Android emulator or device running API 24 or newer
- Gradle 8.2 or newer available on `PATH` for terminal builds; this repository does not currently include the Gradle wrapper

The app's `minSdk` is 24 and `targetSdk` is 34. The Gradle namespace and application ID are `com.yourname.mllearn`.

## Run in Android Studio

1. Open the repository root directory in Android Studio.
2. Allow Gradle project sync to finish.
3. In SDK Manager, install **Android 14 / API 34** if it is not already installed.
4. Create or select an emulator with API 24 or newer, or connect an Android device with USB debugging enabled.
5. Select the `app` run configuration and press **Run**.

## Build and Run from a Terminal

Make sure `ANDROID_HOME` points to the Android SDK. A typical Linux SDK location is `$HOME/Android/Sdk`:

```bash
export ANDROID_HOME="$HOME/Android/Sdk"
export ANDROID_SDK_ROOT="$ANDROID_HOME"
```

Build the debug APK from the repository root:

```bash
gradle :app:assembleDebug
```

The APK is created at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

With an emulator running or a device connected, install and launch the app:

```bash
gradle :app:installDebug
adb shell monkey -p com.yourname.mllearn 1
```

To confirm Android devices are visible to ADB, run `adb devices`.

## Project Layout

```text
app/src/main/java/com/yourname/mllearn/
├── data/                         # Topic catalog
├── navigation/                   # Compose Navigation graph
├── ui/                           # Theme, home, and reusable components
└── topics/                       # Per-topic engines, theory, and playgrounds
```

Each `*Engine.kt` file is plain Kotlin and contains no Compose or Android imports. Shared Compose layouts live under `topics/common` and `ui/components`.

## Useful Commands

```bash
gradle :app:dependencies --configuration debugCompileClasspath
gradle :app:assembleDebug
gradle :app:installDebug
```