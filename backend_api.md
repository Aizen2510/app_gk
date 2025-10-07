BACKEND_URL=https://vocab-app-backend-lmao.onrender.com
---

````markdown
# 📘 Vocab App API

**Version:** 1.0.0  
**Specification:** OpenAPI 3.0  
**Description:** API documentation for the Vocabulary Learning App  

**Production Server:**  
`https://vocab-app-backend-lmao.onrender.com`

---

## 🔐 Authentication & User Management

### **POST** `/api/auth/register`
Register a new user.

#### Request Body
```json
{
  "email": "user@example.com",
  "password": "password123",
  "name": "John Doe"
}
````

#### Responses

**201 - User registered successfully**

```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "user": {
      "id": "string",
      "email": "user@example.com",
      "name": "string",
      "createdAt": "2025-10-07T14:15:51.952Z",
      "updatedAt": "2025-10-07T14:15:51.952Z"
    },
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

**400 - Validation error**

```json
{
  "success": false,
  "message": "string",
  "error": "string"
}
```

---

### **POST** `/api/auth/login`

Login user.

#### Request Body

```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

#### Responses

**200 - Login successful**

```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "user": {
      "id": "string",
      "email": "user@example.com",
      "name": "string",
      "createdAt": "2025-10-07T14:15:51.954Z",
      "updatedAt": "2025-10-07T14:15:51.954Z"
    },
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

**401 - Invalid credentials**

```json
{
  "success": false,
  "message": "string",
  "error": "string"
}
```

---

### **POST** `/api/auth/logout`

Logout user.

#### Responses

**200 - Logout successful**

```json
{
  "success": true,
  "message": "Logout successful",
  "data": {}
}
```

**401 - Unauthorized**

```json
{
  "success": false,
  "message": "string",
  "error": "string"
}
```

---

### **GET** `/api/auth/me`

Get current user profile.

#### Responses

**200 - Success**

```json
{
  "success": true,
  "message": "User profile retrieved successfully",
  "data": {
    "id": "string",
    "email": "user@example.com",
    "name": "string",
    "createdAt": "2025-10-07T14:15:51.955Z",
    "updatedAt": "2025-10-07T14:15:51.955Z"
  }
}
```

**401 - Unauthorized**

```json
{
  "success": false,
  "message": "string",
  "error": "string"
}
```

---

### **PUT** `/api/auth/profile`

Update user profile.

#### Request Body

```json
{
  "name": "John Doe Updated",
  "email": "newemail@example.com"
}
```

#### Responses

**200 - Success**

```json
{
  "success": true,
  "message": "Profile updated successfully",
  "data": {
    "id": "string",
    "email": "user@example.com",
    "name": "string",
    "createdAt": "2025-10-07T14:15:51.957Z",
    "updatedAt": "2025-10-07T14:15:51.957Z"
  }
}
```

**400 - Validation error**

```json
{
  "success": false,
  "message": "string",
  "error": "string"
}
```

---

### **PUT** `/api/auth/change-password`

Change user password.

#### Request Body

```json
{
  "oldPassword": "oldpassword123",
  "newPassword": "newpassword123"
}
```

#### Responses

**200 - Success**

```json
{
  "success": true,
  "message": "Password changed successfully",
  "data": {}
}
```

**400 - Invalid current password**

```json
{
  "success": false,
  "message": "string",
  "error": "string"
}
```

---

## 🃏 Cards

### **POST** `/api/decks/{deckId}/cards`

Create a new card in a deck.

**Path Parameter:**

* `deckId` *(string, required)* – Deck ID

#### Request Body

```json
{
  "frontText": "Hello",
  "backText": "Xin chào",
  "memorized": false
}
```

#### Responses

**201 - Success**

```json
{
  "success": true,
  "message": "Card created successfully",
  "data": {
    "id": "string",
    "frontText": "string",
    "backText": "string",
    "memorized": false,
    "deckId": "string",
    "createdAt": "2025-10-07T14:15:51.959Z",
    "updatedAt": "2025-10-07T14:15:51.959Z"
  }
}
```

---

### **GET** `/api/decks/{deckId}/cards`

Get cards in a deck with pagination and filtering.

#### Query Parameters

| Name        | Type    | Default   | Description             |
| ----------- | ------- | --------- | ----------------------- |
| `page`      | integer | 1         | Page number             |
| `limit`     | integer | 10        | Items per page          |
| `search`    | string  | –         | Search term             |
| `memorized` | string  | –         | Filter (`true`/`false`) |
| `sortBy`    | string  | createdAt | Sort field              |
| `sortOrder` | string  | desc      | Sort order              |

---

### **GET** `/api/cards/{id}`

Get card by ID.

**Path Parameter:**

* `id` *(string, required)* – Card ID

---

### **PUT** `/api/cards/{id}`

Update card details.

---

### **DELETE** `/api/cards/{id}`

Delete card.

---

### **PATCH** `/api/cards/{id}/toggle-memorized`

Toggle a card’s memorized status.

---

### **PATCH** `/api/decks/{deckId}/cards/bulk-memorized`

Bulk update memorized status for multiple cards.

---

### **GET** `/api/decks/{deckId}/study`

Get cards for a study session.

---

### **GET** `/api/cards/search`

Search cards across all decks.

---

### **GET** `/api/decks/{deckId}/cards/stats`

Get card statistics for a deck.

---

## 🗂 Decks

### **POST** `/api/decks`

Create a new deck.

#### Request Body

```json
{
  "name": "English Vocabulary",
  "description": "Basic English vocabulary for beginners"
}
```

---

### **GET** `/api/decks`

Get user's decks with pagination, search, and sorting.

---

### **GET** `/api/decks/{id}`

Retrieve a specific deck by ID.

---

### **PUT** `/api/decks/{id}`

Update deck information.

---

### **DELETE** `/api/decks/{id}`

Delete a deck.

---

### **GET** `/api/decks/{id}/stats`

Get deck statistics.

---

### **GET** `/api/decks/search`

Search decks.

---

## ⚙️ System

### **GET** `/api/health`

Health check endpoint.

#### Example Response

```json
{
  "success": true,
  "message": "Service is healthy",
  "data": {
    "timestamp": "2025-10-07T14:15:51.984Z",
    "uptime": 0,
    "version": "1.0.0"
  }
}
```

---

## 📚 Schemas

### **User**

| Field       | Type   | Description   |
| ----------- | ------ | ------------- |
| `id`        | string | User ID       |
| `email`     | string | Email address |
| `name`      | string | Full name     |
| `createdAt` | string | Timestamp     |
| `updatedAt` | string | Timestamp     |

### **Deck**

| Field         | Type    | Description |
| ------------- | ------- | ----------- |
| `id`          | string  | Deck ID     |
| `name`        | string  | Deck name   |
| `description` | string  | Description |
| `cardCount`   | integer | Total cards |
| `userId`      | string  | Deck owner  |
| `createdAt`   | string  | Timestamp   |
| `updatedAt`   | string  | Timestamp   |

### **Card**

| Field       | Type    | Description         |
| ----------- | ------- | ------------------- |
| `id`        | string  | Card ID             |
| `frontText` | string  | Front side text     |
| `backText`  | string  | Back side text      |
| `memorized` | boolean | Memorization status |
| `deckId`    | string  | Parent deck ID      |
| `createdAt` | string  | Timestamp           |
| `updatedAt` | string  | Timestamp           |

### **Error**

```json
{
  "success": false,
  "message": "string",
  "error": "string"
}
```

### **Success**

```json
{
  "success": true,
  "message": "string",
  "data": {}
}

