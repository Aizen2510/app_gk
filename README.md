# Vocab App - Android Application

## Overview
A clean and simple Android vocabulary learning app that connects to the backend API. The app allows users to:
- Register and login
- Create and manage vocabulary decks
- Add cards to decks
- Study cards with a flashcard interface
- Track memorization progress

## Project Structure

### Models (`models/`)
- **User.java**: User data model
- **Deck.java**: Deck data model
- **Card.java**: Flashcard data model
- **ApiResponse.java**: Generic API response wrapper
- **AuthData.java**: Authentication response data
- **PaginatedResponse.java**: Paginated list responses
- **requests/**: Request body models (LoginRequest, RegisterRequest, CreateDeckRequest, CreateCardRequest)

### API Layer (`api/`)
- **ApiService.java**: Retrofit interface defining all API endpoints
- **ApiClient.java**: Singleton client for Retrofit configuration with auth interceptor

### Activities
1. **LoginActivity**: User login with email/password
2. **RegisterActivity**: New user registration
3. **MainActivity**: List of user's decks with add/delete functionality
4. **DeckActivity**: View cards in a deck, add new cards, toggle memorization
5. **StudyActivity**: Flashcard study mode with flip animation

### Adapters
- **DeckAdapter**: RecyclerView adapter for deck list
- **CardAdapter**: RecyclerView adapter for card list

## Key Features

### Authentication
- JWT token-based authentication
- Token stored in SharedPreferences
- Automatic token injection in API requests via interceptor
- Auto-redirect to login if not authenticated

### Deck Management
- View all decks in a scrollable list
- Create new decks with name and description
- Each deck shows card count
- Swipe-to-refresh functionality

### Card Management
- View all cards in a deck
- Create new flashcards with front/back text
- Toggle memorization status with checkbox
- Persistent state updates via API

### Study Mode
- Clean flashcard interface
- Tap to flip between front and back
- Navigate between cards with Previous/Next buttons
- Progress indicator showing current card position
- Simple flip animation for better UX

## API Configuration
- Base URL: `https://vocab-app-backend-lmao.onrender.com/`
- Configured in `app/build.gradle` as BuildConfig field
- All requests use Bearer token authentication

## Dependencies
- **Retrofit 2.9.0**: REST API client
- **OkHttp 4.12.0**: HTTP client with logging
- **Gson 2.10.1**: JSON serialization
- **Material Components 1.13.0**: Modern UI components
- **RecyclerView & CardView**: List and card layouts
- **SwipeRefreshLayout**: Pull-to-refresh functionality

## UI Design
The app follows Material Design 3 guidelines with:
- Clean, minimalist interface
- Material color scheme
- Card-based layouts for content
- Floating action buttons for primary actions
- Outlined text fields for input
- Proper navigation with back buttons
- Toast messages for user feedback

## Build Instructions
1. Open project in Android Studio
2. Sync Gradle dependencies
3. Ensure minimum SDK 24 (Android 7.0) or higher
4. Build and run on emulator or device

## Network Permissions
The app requires internet access and uses cleartext traffic for HTTP connections (configured in AndroidManifest.xml).

## Future Enhancements (Optional)
- Search functionality for decks and cards
- Edit/delete cards and decks
- User profile management
- Offline mode with local database
- Spaced repetition algorithm
- Statistics and progress tracking
- Card categories and tags
- Export/import functionality
