# 📱 Android Menu & Ordering App

![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white) ![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=android&logoColor=white) ![Ktor](https://img.shields.io/badge/Ktor-0095D5?style=for-the-badge&logo=ktor&logoColor=white) ![Koin](https://img.shields.io/badge/Koin-F78C40?style=for-the-badge&logo=koin&logoColor=white)

A modern, responsive Android application built to demonstrate a scalable **Multi-Module Architecture**. [cite_start]The app allows users to browse food categories, search for products, and manage a temporary shopping cart/order system[cite: 2, 41].

## 🏗️ Architecture

This project adopts a **Layered Multi-Module Architecture** to ensure separation of concerns, scalability, and testability. [cite_start]It follows the **MVVM/MVI** design pattern[cite: 35, 75].

### Module Structure
* **:app** - The entry point and DI graph orchestration.
* **:core** - Common utility classes and extensions.
* [cite_start]**:data** - Repositories, API implementation (Ktor) [cite: 30][cite_start], and Local Storage (Room)[cite: 32].
* **:domain** - Use cases, interface definitions, and pure business logic.
* [cite_start]**:presentation** - UI screens (Jetpack Compose) [cite: 34] and ViewModels.

## 🛠️ Tech Stack

* **Language:** Kotlin
* [cite_start]**UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) & Material 3 [cite: 33, 34]
* [cite_start]**Networking:** KTOR [cite: 30]
* [cite_start]**Dependency Injection:** KOIN [cite: 31, 76]
* [cite_start]**Local Database:** Room [cite: 32, 73]
* [cite_start]**Architecture:** MVVM/MVI Pattern [cite: 35]
* **Testing:** JUnit & Mockk (Unit Tests included)

## ✨ Features

### 1. Products & Categories
* [cite_start]**Browsing:** Browse products categorized by type (Breakfast, Lunch, Dinner, etc.)[cite: 41].
* [cite_start]**Search:** Filter products instantly by name using the search bar[cite: 41].
* [cite_start]**Product Details:** Displays images, descriptions, and prices for every item[cite: 41].

### 2. Order Management
* [cite_start]**Add to Order:** Users can tap a product to add it to the current order[cite: 41].
* [cite_start]**Dynamic Pricing:** The "View Order" bar updates in real-time with the total price and quantity of items[cite: 41].
* [cite_start]**Reset Logic:** Pressing the "View Order" button simulates placing the order and clears the held data/cart[cite: 41].

### 3. Data Persistence
* [cite_start]**Offline Support:** Fetched categories and products are cached locally using **Room**, allowing the app to function without an immediate network connection[cite: 41].

### 4. UI/UX
* [cite_start]**Responsive Design:** Optimized for portrait mode but adjusts UI elements for different screen resolutions and sizes[cite: 41].

## 🔌 API & Data

[cite_start]The application mocks a backend using **Mockaroo**[cite: 41, 43].

### Schema
The app consumes the following data structure:

[cite_start]**Categories Endpoint** [cite: 44-46]
```json
{
  "id": "String",
  "name": "String"
}
