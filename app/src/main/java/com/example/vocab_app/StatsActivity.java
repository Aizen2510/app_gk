package com.example.vocab_app;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vocab_app.api.ApiClient;
import com.example.vocab_app.api.ApiService;
import com.example.vocab_app.databinding.ActivityStatsBinding;
import com.example.vocab_app.models.ApiResponse;
import com.example.vocab_app.models.DeckStats;
 

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatsActivity extends AppCompatActivity {

    private ActivityStatsBinding binding;
    private ApiService apiService;
    private String deckId;
    private String deckName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        deckId = getIntent().getStringExtra("DECK_ID");
        deckName = getIntent().getStringExtra("DECK_NAME");

        ApiClient.init(this);
        apiService = ApiClient.getApiService();

        binding = ActivityStatsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Statistics: " + deckName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        loadStats();
    }

    private void loadStats() {
        apiService.getDeckStats(deckId).enqueue(new Callback<ApiResponse<DeckStats>>() {
            @Override
            public void onResponse(Call<ApiResponse<DeckStats>> call, Response<ApiResponse<DeckStats>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    DeckStats stats = response.body().getData();
                    if (stats != null) {
                        binding.deckName.setText(stats.getDeckName());
                        binding.totalCards.setText(String.valueOf(stats.getTotalCards()));
                        binding.memorizedCards.setText(String.valueOf(stats.getMemorizedCards()));
                        binding.unmemorizedCards.setText(String.valueOf(stats.getUnmemorizedCards()));
                        binding.progressPercentage.setText(stats.getProgressPercentage() + "%");
                        binding.recentCards.setText(String.valueOf(stats.getRecentCards()));

                        // Không vẽ biểu đồ: giữ thông tin dạng text như trước đây
                    }
                } else {
                    Toast.makeText(StatsActivity.this, "Không lấy được thống kê", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<DeckStats>> call, Throwable t) {
                Toast.makeText(StatsActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}


