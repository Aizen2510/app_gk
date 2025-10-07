package com.example.vocab_app.api;

import com.example.vocab_app.models.*;
import com.example.vocab_app.models.requests.*;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {
    
    // Auth endpoints
    @POST("api/auth/register")
    Call<ApiResponse<AuthData>> register(@Body RegisterRequest request);
    
    @POST("api/auth/login")
    Call<ApiResponse<AuthData>> login(@Body LoginRequest request);
    
    @POST("api/auth/logout")
    Call<ApiResponse<Object>> logout();
    
    @GET("api/auth/me")
    Call<ApiResponse<User>> getCurrentUser();
    
    // Deck endpoints
    @POST("api/decks")
    Call<ApiResponse<Deck>> createDeck(@Body CreateDeckRequest request);
    
    @GET("api/decks")
    Call<ApiResponse<List<Deck>>> getDecks(
        @Query("page") int page,
        @Query("limit") int limit,
        @Query("search") String search,
        @Query("sortBy") String sortBy,
        @Query("sortOrder") String sortOrder
    );
    
    @GET("api/decks/{id}")
    Call<ApiResponse<Deck>> getDeck(@Path("id") String id);
    
    @PUT("api/decks/{id}")
    Call<ApiResponse<Deck>> updateDeck(@Path("id") String id, @Body CreateDeckRequest request);
    
    @DELETE("api/decks/{id}")
    Call<ApiResponse<Object>> deleteDeck(@Path("id") String id);
    
    // Card endpoints
    @POST("api/decks/{deckId}/cards")
    Call<ApiResponse<Card>> createCard(@Path("deckId") String deckId, @Body CreateCardRequest request);
    
    @GET("api/decks/{deckId}/cards")
    Call<ApiResponse<List<Card>>> getCards(
        @Path("deckId") String deckId,
        @Query("page") int page,
        @Query("limit") int limit,
        @Query("search") String search,
        @Query("memorized") String memorized,
        @Query("sortBy") String sortBy,
        @Query("sortOrder") String sortOrder
    );
    
    @GET("api/cards/{id}")
    Call<ApiResponse<Card>> getCard(@Path("id") String id);
    
    @PUT("api/cards/{id}")
    Call<ApiResponse<Card>> updateCard(@Path("id") String id, @Body CreateCardRequest request);
    
    @DELETE("api/cards/{id}")
    Call<ApiResponse<Object>> deleteCard(@Path("id") String id);
    
    @PATCH("api/cards/{id}/toggle-memorized")
    Call<ApiResponse<Card>> toggleCardMemorized(@Path("id") String id);
    
    @GET("api/decks/{deckId}/study")
    Call<ApiResponse<List<Card>>> getStudyCards(@Path("deckId") String deckId);
}
