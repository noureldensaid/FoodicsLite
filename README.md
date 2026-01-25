# 📱 Android Menu & Ordering App

![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white) ![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=android&logoColor=white) ![Ktor](https://img.shields.io/badge/Ktor-0095D5?style=for-the-badge&logo=ktor&logoColor=white) ![Koin](https://img.shields.io/badge/Koin-F78C40?style=for-the-badge&logo=koin&logoColor=white)

A modern, responsive Android application built to demonstrate a scalable **Multi-Module Architecture**. The app allows users to browse food categories, search for products, and manage a temporary shopping cart/order system.

## 🏗️ Architecture

This project adopts a **Layered Multi-Module Architecture** to ensure separation of concerns, scalability, and testability. It follows the **MVI** design pattern

### Module Structure
* **:app** - The entry point and DI graph orchestration.
* **:network** - Ktor client
* **:database** - Room database
* **:core** - Common utility classes and extensions.
* **:data** - Repositories, API implementation (Ktor), and Local Storage (Room).
* **:domain** - Use cases, interface definitions, and pure business logic.
* **:presentation** - UI screens (Jetpack Compose) and ViewModels.

## 🛠️ Tech Stack

* **Language:** Kotlin
* **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) & Material 3  
* **Networking:** KTOR  
* **Dependency Injection:** KOIN  
* **Local Database:** Room  
* **Architecture:** MVI Pattern
* **Testing:** JUnit & Mockk (Unit Tests included)

## ✨ Features

### 1. Products & Categories
* **Browsing:** Browse products categorized by type (Breakfast, Lunch, Dinner, etc.).
* **Search:** Filter products instantly by name using the search bar.
* **Product Details:** Displays images, descriptions, and prices for every item.

### 2. Order Management
* **Add to Order:** Users can tap a product to add it to the current order.
* **Dynamic Pricing:** The "View Order" bar updates in real-time with the total price and quantity of items.
* **Reset Logic:** Pressing the "View Order" button simulates placing the order and clears the held data/cart.

### 3. Data Persistence
* **Offline Support:** Fetched categories and products are cached locally using **Room**, allowing the app to function without an immediate network connection.

### 4. UI/UX
* **Responsive Design:** Optimized for portrait mode but adjusts UI elements for different screen resolutions and sizes.

## 🔌 API & Data

The application mocks a backend using **Mockaroo**.

### Schema
The app consumes the following data structure:

**Categories Endpoint**
```json
{
  "id": "String",
  "name": "String"
}
