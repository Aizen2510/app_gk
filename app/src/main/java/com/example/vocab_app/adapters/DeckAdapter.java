package com.example.vocab_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vocab_app.R;
import com.example.vocab_app.models.Deck;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class DeckAdapter extends RecyclerView.Adapter<DeckAdapter.DeckViewHolder> {
    
    private List<Deck> decks = new ArrayList<>();
    private OnDeckClickListener listener;
    
    public interface OnDeckClickListener {
        void onDeckClick(Deck deck);
        void onStudyClick(Deck deck);
    }
    
    public DeckAdapter(OnDeckClickListener listener) {
        this.listener = listener;
    }
    
    public void setDecks(List<Deck> decks) {
        this.decks = decks;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public DeckViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_deck, parent, false);
        return new DeckViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull DeckViewHolder holder, int position) {
        holder.bind(decks.get(position));
    }
    
    @Override
    public int getItemCount() {
        return decks.size();
    }
    
    class DeckViewHolder extends RecyclerView.ViewHolder {
        TextView deckName, deckDescription, cardCount;
        MaterialButton studyButton;
        
        DeckViewHolder(@NonNull View itemView) {
            super(itemView);
            deckName = itemView.findViewById(R.id.deckName);
            deckDescription = itemView.findViewById(R.id.deckDescription);
            cardCount = itemView.findViewById(R.id.cardCount);
            studyButton = itemView.findViewById(R.id.studyButton);
        }
        
        void bind(Deck deck) {
            deckName.setText(deck.getName());
            deckDescription.setText(deck.getDescription());
            cardCount.setText(deck.getCardCount() + " cards");
            
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeckClick(deck);
                }
            });
            
            studyButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onStudyClick(deck);
                }
            });
        }
    }
}
