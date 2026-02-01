# 🍽️ Mealify

Mealify is a comprehensive meal planning and recipe discovery Android application designed to help users manage their weekly diets, discover new recipes, and keep track of their favorite meals.

## ✨ Features

*   **Meal of the Day**: Get a daily recipe recommendation to spice up your routine.
*   **Search & Discovery**: Search for recipes by name, category, area, or ingredient.
*   **Favorites**: Save your favorite recipes for quick access later.
*   **Weekly Planner**: Plan your meals for the entire week to stay organized.
*   **Offline Support**: Access your favorite recipes and weekly plan even without an internet connection.
*   **Guest Mode**: Explore the app's features without creating an account (data is not synced to the cloud).
*   **Calendar Integration**: Add planned meals directly to your device's calendar.

## 🏗️ Architecture

This application follows the **MVP (Model-View-Presenter)** architectural pattern to ensure separation of concerns and testability. It delegates data operations to a **Repository** layer that arbitrates between local and remote data sources.

### 🛠️ Tech Stack

*   **Language**: [Java](https://www.java.com/)
*   **Reactive Programming**: [RxJava 3](https://github.com/ReactiveX/RxJava) & [RxAndroid](https://github.com/ReactiveX/RxAndroid) for asynchronous operations and event handling.
*   **Networking**: [Retrofit](https://square.github.io/retrofit/) for API requests.
*   **Local Database**: [Room](https://developer.android.com/training/data-storage/room) for persistent local storage (Favorites, Weekly Plan).
*   **Cloud Backend**: [Firebase](https://firebase.google.com/)
    *   **Authentication**: For user management (Google Sign-In, Guest access).
    *   **Firestore**: For cloud syncing of favorites and meal plans.
*   **Image Loading**: [Glide](https://github.com/bumptech/glide) for efficient image caching and loading.
*   **Animations**: [Lottie](https://airbnb.io/lottie/#/android) for engaging UI animations.
*   **Navigation**: Android Jetpack Navigation Component.

## 📱 Prerequisites

*   Android Studio Ladybug (or newer)
*   JDK 11 or higher
*   Android device or emulator running API level 24 (Nougat) or higher.

## 🚀 Setup Instructions

1.  **Clone the repository**:
    ```bash
    git clone https://github.com/your-username/Mealify.git
    cd Mealify
    ```

2.  **Open in Android Studio**:
    *   Launch Android Studio.
    *   Select "Open" and navigate to the cloned directory.

3.  **Firebase Setup**:
    *   Create a project in the [Firebase Console](https://console.firebase.google.com/).
    *   Add an Android app to your Firebase project with package name `com.mealify.mealify`.
    *   Download the `google-services.json` file.
    *   Place `google-services.json` in the `app/` directory of the project.
    *   Enable **Authentication** (Google Sign-In, Email/Password, Anonymous).
    *   Create a **Firestore** database.

4.  **Build and Run**:
    *   Sync Gradle files.
    *   Connect your device or start an emulator.
    *   Click the "Run" button (green play icon).

## 🔒 Permissions

*   **Internet**: Required to fetch recipes and sync with Firebase.
*   **Access Network State**: Used to monitor connectivity and switch between online and offline modes.
*   **Read/Write Calendar**: Required to add meal events to your personal calendar.

## 🧑‍💻 Authors

*   **Eslam Elnady**
