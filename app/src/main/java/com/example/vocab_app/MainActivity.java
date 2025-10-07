package com.example.vocab_app;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vocab_app.adapters.DeckAdapter;
import com.example.vocab_app.api.ApiClient;
import com.example.vocab_app.api.ApiService;
import com.example.vocab_app.databinding.ActivityMainBinding;
import com.example.vocab_app.models.ApiResponse;
import com.example.vocab_app.models.Deck;
import com.example.vocab_app.models.PaginatedResponse;
import com.example.vocab_app.models.requests.CreateDeckRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements DeckAdapter.OnDeckClickListener {
    
    private ActivityMainBinding binding;
    private ApiService apiService;
    private DeckAdapter adapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ApiClient.init(this);
        apiService = ApiClient.getApiService();
        
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        setSupportActionBar(binding.toolbar);
        
        adapter = new DeckAdapter(this);
        binding.decksRecyclerView.setAdapter(adapter);
        
        binding.addDeckFab.setOnClickListener(v -> showAddDeckDialog());
        binding.swipeRefresh.setOnRefreshListener(() -> loadDecks());
        
        loadDecks();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void loadDecks() {
        binding.swipeRefresh.setRefreshing(true);
        
        apiService.getDecks(1, 100, null, "createdAt", "desc")
                .enqueue(new Callback<ApiResponse<PaginatedResponse<Deck>>>() {
            @Override
            public void onResponse(Call<ApiResponse<PaginatedResponse<Deck>>> call, 
                                 Response<ApiResponse<PaginatedResponse<Deck>>> response) {
                binding.swipeRefresh.setRefreshing(false);
                
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<PaginatedResponse<Deck>> apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        adapter.setDecks(apiResponse.getData().getItems());
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Failed to load decks", Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<PaginatedResponse<Deck>>> call, Throwable t) {
                binding.swipeRefresh.setRefreshing(false);
                Toast.makeText(MainActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void showAddDeckDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create New Deck");
        
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 20);
        
        EditText nameInput = new EditText(this);
        nameInput.setHint("Deck Name");
        layout.addView(nameInput);
        
        EditText descInput = new EditText(this);
        descInput.setHint("Description");
        layout.addView(descInput);
        
        builder.setView(layout);
        builder.setPositiveButton("Create", (dialog, which) -> {
            String name = nameInput.getText().toString().trim();
            String description = descInput.getText().toString().trim();
            
            if (!name.isEmpty()) {
                createDeck(name, description);
            } else {
                Toast.makeText(this, "Please enter a deck name", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
    
    private void createDeck(String name, String description) {
        CreateDeckRequest request = new CreateDeckRequest(name, description);
        apiService.createDeck(request).enqueue(new Callback<ApiResponse<Deck>>() {
            @Override
            public void onResponse(Call<ApiResponse<Deck>> call, Response<ApiResponse<Deck>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(MainActivity.this, "Deck created!", Toast.LENGTH_SHORT).show();
                    loadDecks();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to create deck", Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<Deck>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    @Override
    public void onDeckClick(Deck deck) {
        Intent intent = new Intent(this, DeckActivity.class);
        intent.putExtra("DECK_ID", deck.getId());
        intent.putExtra("DECK_NAME", deck.getName());
        startActivity(intent);
    }
    
    @Override
    public void onStudyClick(Deck deck) {
        if (deck.getCardCount() > 0) {
            Intent intent = new Intent(this, StudyActivity.class);
            intent.putExtra("DECK_ID", deck.getId());
            intent.putExtra("DECK_NAME", deck.getName());
            startActivity(intent);
        } else {
            Toast.makeText(this, "This deck has no cards to study", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void logout() {
        apiService.logout().enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                ApiClient.clearToken();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
            
            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                ApiClient.clearToken();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadDecks();
    }
}
