package com.example.vocab_app;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vocab_app.api.ApiClient;
import com.example.vocab_app.api.ApiService;
import com.example.vocab_app.databinding.ActivityStudyBinding;
import com.example.vocab_app.models.ApiResponse;
import com.example.vocab_app.models.Card;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudyActivity extends AppCompatActivity {
    
    private ActivityStudyBinding binding;
    private ApiService apiService;
    private String deckId;
    private String deckName;
    private List<Card> cards = new ArrayList<>();
    private int currentCardIndex = 0;
    private boolean showingFront = true;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        deckId = getIntent().getStringExtra("DECK_ID");
        deckName = getIntent().getStringExtra("DECK_NAME");
        
        ApiClient.init(this);
        apiService = ApiClient.getApiService();
        
        binding = ActivityStudyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Study: " + deckName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        binding.toolbar.setNavigationOnClickListener(v -> finish());
        
        binding.flashcard.setOnClickListener(v -> flipCard());
        binding.previousButton.setOnClickListener(v -> previousCard());
        binding.nextButton.setOnClickListener(v -> nextCard());
        
        loadStudyCards();
    }
    
    private void loadStudyCards() {
        apiService.getStudyCards(deckId).enqueue(new Callback<ApiResponse<List<Card>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Card>>> call, Response<ApiResponse<List<Card>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Card>> apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        cards = apiResponse.getData();
                        if (cards.isEmpty()) {
                            Toast.makeText(StudyActivity.this, "No cards to study", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            showCard();
                        }
                    }
                } else {
                    Toast.makeText(StudyActivity.this, "Failed to load study cards", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<List<Card>>> call, Throwable t) {
                Toast.makeText(StudyActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    
    private void showCard() {
        if (cards.isEmpty()) return;
        
        Card card = cards.get(currentCardIndex);
        showingFront = true;
        
        binding.progressText.setText("Card " + (currentCardIndex + 1) + " of " + cards.size());
        binding.cardSideLabel.setText("Front");
        binding.cardText.setText(card.getFrontText());
        binding.tapToFlipHint.setVisibility(View.VISIBLE);
        
        binding.previousButton.setEnabled(currentCardIndex > 0);
        binding.nextButton.setEnabled(currentCardIndex < cards.size() - 1);
    }
    
    private void flipCard() {
        if (cards.isEmpty()) return;
        
        // Simple flip animation
        binding.flashcard.animate()
                .scaleX(0f)
                .setDuration(150)
                .withEndAction(() -> {
                    showingFront = !showingFront;
                    Card card = cards.get(currentCardIndex);
                    
                    if (showingFront) {
                        binding.cardSideLabel.setText("Front");
                        binding.cardText.setText(card.getFrontText());
                        binding.tapToFlipHint.setVisibility(View.VISIBLE);
                    } else {
                        binding.cardSideLabel.setText("Back");
                        binding.cardText.setText(card.getBackText());
                        binding.tapToFlipHint.setVisibility(View.GONE);
                    }
                    
                    binding.flashcard.animate()
                            .scaleX(1f)
                            .setDuration(150)
                            .start();
                })
                .start();
    }
    
    private void previousCard() {
        if (currentCardIndex > 0) {
            currentCardIndex--;
            showCard();
        }
    }
    
    private void nextCard() {
        if (currentCardIndex < cards.size() - 1) {
            currentCardIndex++;
            showCard();
        } else {
            Toast.makeText(this, "Study session complete!", Toast.LENGTH_SHORT).show();
        }
    }
}
