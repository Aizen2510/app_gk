package com.example.vocab_app;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vocab_app.adapters.CardAdapter;
import android.view.Menu;
import android.view.MenuItem;
import com.example.vocab_app.api.ApiClient;
import com.example.vocab_app.api.ApiService;
import com.example.vocab_app.databinding.ActivityDeckBinding;
import com.example.vocab_app.models.ApiResponse;
import com.example.vocab_app.models.Card;
import com.example.vocab_app.models.requests.CreateCardRequest;
import com.google.android.material.textfield.TextInputEditText;

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

        binding.searchCardInput.setOnEditorActionListener((v, actionId, event) -> {
            doSearchCards();
            return true;
        });
        binding.searchCardInput.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(android.text.Editable s) {
                if (s.toString().trim().isEmpty()) {
                    loadCards();
                }
            }
        });
        
        loadCards();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void doSearchCards() {
        String q = binding.searchCardInput.getText().toString().trim();
        if (q.isEmpty()) {
            loadCards();
            return;
        }
        binding.swipeRefresh.setRefreshing(true);
        apiService.searchCards(deckId, q, 1, 100).enqueue(new Callback<ApiResponse<List<Card>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Card>>> call, Response<ApiResponse<List<Card>>> response) {
                binding.swipeRefresh.setRefreshing(false);
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<Card> cards = response.body().getData();
                    adapter.setCards(cards);
                } else {
                    Toast.makeText(DeckActivity.this, "Search failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Card>>> call, Throwable t) {
                binding.swipeRefresh.setRefreshing(false);
                Toast.makeText(DeckActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.statistical) {
            Toast.makeText(this, "Opening statistics...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, StatsActivity.class);
            intent.putExtra("DECK_ID", deckId);
            intent.putExtra("DECK_NAME", deckName);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        // Inflate layout
        View dialogView = getLayoutInflater().inflate(R.layout.input_card, null);

        // Ánh xạ view
        TextInputEditText frontInput = dialogView.findViewById(R.id.inputFrontText);
        TextInputEditText backInput = dialogView.findViewById(R.id.inputBackText);

        // Tạo dialog
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Create New Card")
                .setView(dialogView)
                .setPositiveButton("Create", null) // để override hành vi sau
                .setNegativeButton("Cancel", (d, w) -> d.dismiss())
                .create();

        dialog.show();

        // Ghi đè hành vi nút "Create" (để kiểm tra input trước khi đóng dialog)
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String front = frontInput.getText().toString().trim();
            String back = backInput.getText().toString().trim();

            if (front.isEmpty() || back.isEmpty()) {
                Toast.makeText(this, "Please fill both fields", Toast.LENGTH_SHORT).show();
            } else {
                createCard(front, back);
                dialog.dismiss();
            }
        });
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
