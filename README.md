# 📱 Foodics Lite

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
```
**Products Endpoint**
```json
{
  "id": "String",
  "category": { "id": "String", "name": "String" },
  "name": "String",
  "description": "String",
  "image": "URL String",
  "price": 0.0
}
```
**Screenshots

<img width="300" alt="image" src="https://github.com/user-attachments/assets/b2facc06-a144-4089-adb7-e7d83c5219c0" />
<img width="300" alt="image" src="https://github.com/user-attachments/assets/b69cc71e-f1ce-4da6-be57-52de735f874d" />
<img width="300" alt="image" src="https://github.com/user-attachments/assets/d2e7577a-46a2-4554-aced-3af839d71f1c" />
<img width="300" alt="image" src="https://github.com/user-attachments/assets/83a4f7b5-5ff6-476e-b289-31e24d8437f5" />
<img width="300" alt="image" src="https://github.com/user-attachments/assets/70d43060-c670-4240-95a1-2325701b334d" />
<img width="300" alt="image" src="https://github.com/user-attachments/assets/a3e89d7b-3dce-4940-8d8b-0762fc118dc4" />
<img width="300" alt="image" src="https://github.com/user-attachments/assets/bdabaf98-7fcf-4b6b-a381-6c9a1b65358f" />
<img width="2798" height="1837" alt="Screenshot_20260125_210319" src="https://github.com/user-attachments/assets/cf3a096b-d22c-40f4-a4c8-bb4b0ae9d599" />


