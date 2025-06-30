# Vault-Manager
**VaultManager** is a simple Java-based application that allows users to sign up, log in, and manage personal properties stored securely in a JSON file. Users can add, update, delete, and view key-value pairs (properties) after logging in.

---
## 🚀 Features

- ✅ User Sign Up
- 🔐 Secure Login (basic authentication)
- 📦 Store properties as key-value pairs
- ✏️ Update existing properties
- ❌ Delete unwanted properties
- 👀 View all saved properties
- 💾 All data saved in a JSON file for persistence
---
## 🛠️ Technologies Used

- Java (JDK 8+)
- Gson (for JSON parsing)
- File I/O (for reading and writing data)
- Console-based UI

📂 Project Structure

VaultManager/
│
├── src/
│ ├── com.vault/
│ │ ├── model/
│ │ │ └── User.java
│ │ ├── service/
│ │ │ └── PropertyService.java
│ │ ├── utils/
│ │ │ └── JsonUtils.java
│ │ └── Main.java
│
├── data/
│ └── users.json
│ └── userproperties.json
├── README.md
└── pom.xml 

🗃️ Data Format
Here's how user data and properties are stored in users.json:
[
  {
    "id": "a1b2c3d4",
    "firstName": "Kelvin",
    "lastName": "Nwachukwu",
    "email": "kelvin@email.com",
    "password": "1234"
    
  }
]

[
  {
    "userId": "5fd7ab33-63f3-4c84-8a50-2e829168c7e2",
    "properties": {
      "school": "unn",
      "color": "red"
    }
  }
]

🧑‍💻 Author
Kelvin Nwachukwu

