package com.example.vocab_app;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vocab_app.adapters.CardAdapter;
import com.example.vocab_app.api.ApiClient;
import com.example.vocab_app.api.ApiService;
import com.example.vocab_app.databinding.ActivityDeckBinding;
import com.example.vocab_app.models.ApiResponse;
import com.example.vocab_app.models.Card;
import com.example.vocab_app.models.requests.CreateCardRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeckActivity extends AppCompatActivity implements CardAdapter.OnCardClickListener {
    
    private ActivityDeckBinding binding;
    private ApiService apiService;
    private CardAdapter adapter;
    private String deckId;
    private String deckName;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        deckId = getIntent().getStringExtra("DECK_ID");
        deckName = getIntent().getStringExtra("DECK_NAME");
        
        ApiClient.init(this);
        apiService = ApiClient.getApiService();
        
        binding = ActivityDeckBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(deckName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        binding.toolbar.setNavigationOnClickListener(v -> finish());
        
        adapter = new CardAdapter(this);
        binding.cardsRecyclerView.setAdapter(adapter);
        
        binding.addCardFab.setOnClickListener(v -> showAddCardDialog());
        binding.swipeRefresh.setOnRefreshListener(() -> loadCards());
        
        loadCards();
    }
    
    private void loadCards() {
        binding.swipeRefresh.setRefreshing(true);
        
        apiService.getCards(deckId, 1, 100, null, null, "createdAt", "desc")
                .enqueue(new Callback<ApiResponse<List<Card>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Card>>> call, 
                                 Response<ApiResponse<List<Card>>> response) {
                binding.swipeRefresh.setRefreshing(false);
                
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Card>> apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        adapter.setCards(apiResponse.getData());
                    }
                } else {
                    Toast.makeText(DeckActivity.this, "Failed to load cards", Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<List<Card>>> call, Throwable t) {
                binding.swipeRefresh.setRefreshing(false);
                Toast.makeText(DeckActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void showAddCardDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create New Card");
        
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 20);
        
        EditText frontInput = new EditText(this);
        frontInput.setHint("Front Text (Question)");
        layout.addView(frontInput);
        
        EditText backInput = new EditText(this);
        backInput.setHint("Back Text (Answer)");
        layout.addView(backInput);
        
        builder.setView(layout);
        builder.setPositiveButton("Create", (dialog, which) -> {
            String front = frontInput.getText().toString().trim();
            String back = backInput.getText().toString().trim();
            
            if (!front.isEmpty() && !back.isEmpty()) {
                createCard(front, back);
            } else {
                Toast.makeText(this, "Please fill both fields", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
    
    private void createCard(String frontText, String backText) {
        CreateCardRequest request = new CreateCardRequest(frontText, backText, false);
        apiService.createCard(deckId, request).enqueue(new Callback<ApiResponse<Card>>() {
            @Override
            public void onResponse(Call<ApiResponse<Card>> call, Response<ApiResponse<Card>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(DeckActivity.this, "Card created!", Toast.LENGTH_SHORT).show();
                    loadCards();
                } else {
                    Toast.makeText(DeckActivity.this, "Failed to create card", Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<Card>> call, Throwable t) {
                Toast.makeText(DeckActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    @Override
    public void onCardClick(Card card) {
        Toast.makeText(this, "Card: " + card.getFrontText(), Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onMemorizedToggle(Card card) {
        apiService.toggleCardMemorized(card.getId()).enqueue(new Callback<ApiResponse<Card>>() {
            @Override
            public void onResponse(Call<ApiResponse<Card>> call, Response<ApiResponse<Card>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    card.setMemorized(!card.isMemorized());
                    // Toast.makeText(DeckActivity.this, 
                    //         card.isMemorized() ? "Marked as memorized" : "Marked as not memorized", 
                    //         Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DeckActivity.this, "Failed to update", Toast.LENGTH_SHORT).show();
                    loadCards();
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<Card>> call, Throwable t) {
                Toast.makeText(DeckActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                loadCards();
            }
        });
    }
}
