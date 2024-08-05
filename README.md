# Pokemon Character Viewer

This is an Android application that displays a list of characters from the Pokemon series using the Poke API and Jetpack libraries. The app follows the MVVM (Model-View-ViewModel) architecture for better separation of concerns and maintainability.

## Demo Video
[Link to demo video](https://drive.google.com/file/d/1F7nYMmLZUUjehjmnsj6tVNYY0emsN3XJ/view?usp=sharing)

## Apk Link
[Link to Apk ](https://drive.google.com/file/d/17rT2JHwDV-aOu_bTaXS5VW2Zh9RrZeFc/view?usp=sharing)

## Table of Contents
- [Features](#features)
- [Architecture](#architecture)
- [Libraries Used](#libraries-used)
- [Installation](#installation)
- [Usage](#usage)

## Features
- **Character List**: Displays a list of characters with their names and images.
- **Character Details**: View detailed information about each character, including weight, height, abilities, stats, and total progress.
- **Splash Screen**: splash screen with logo redirecting to main screen .
- **Offline Caching**: Caches API responses to provide offline support.

## Architecture
The app is structured following the MVVM architecture pattern for better organization and maintainability:
- **Model**: Defines data structures and handles data operations.
- **View**: Responsible for displaying data and UI components.
- **ViewModel**: Manages UI-related data and handles communication between the View and the Model.
- **Repository**: Provides a clean API for data access to the rest of the application, managing data operations and network/database interactions.
- **Network**: Contains API service interfaces and network-related classes, such as Retrofit instances.

## Libraries Used
- **SplashScreen**: To provide splash screen as recommended .
- **Retrofit**: A type-safe HTTP client for Android and Kotlin. Used for making network requests to the Poke API.
- **Glide**: An image loading and caching library for Android focused on smooth scrolling. Used to load character images.
- **Gson**: A library to convert Java Objects into JSON and back. Used in conjunction with Retrofit for JSON parsing.
- **OkHttp**: An HTTP client for Android and Kotlin applications. Used for implementing API response caching.
- **Dagger-Hilt**: Simplifies dependency injection by providing a set of standard components and annotations, which reduces boilerplate code and improves code maintainability.


## Installation
1. Clone the repository: git clone https://github.com/Himanshu6124/rick-and-morty-ridecell-assignment
2. Open the project in Android Studio.
3. Checkout the `main` branch.
4. Sync the project with Gradle files.

## Usage
1. Run the application on an Android emulator or physical device.
2. The splash screen will appear with an animation.
3. The main screen displays a Pokemon characters.
4. Use the pagination to dynamic loading next pages.
5. Tap on a character to view detailed information.

