package com.example.vocab_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vocab_app.R;
import com.example.vocab_app.models.Card;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    
    private List<Card> cards = new ArrayList<>();
    private OnCardClickListener listener;
    
    public interface OnCardClickListener {
        void onCardClick(Card card);
        void onMemorizedToggle(Card card);
    }
    
    public CardAdapter(OnCardClickListener listener) {
        this.listener = listener;
    }
    
    public void setCards(List<Card> cards) {
        this.cards = cards;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false);
        return new CardViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        holder.bind(cards.get(position));
    }
    
    @Override
    public int getItemCount() {
        return cards.size();
    }
    
    class CardViewHolder extends RecyclerView.ViewHolder {
        TextView frontText, backText;
        MaterialCheckBox memorizedCheckbox;
        
        CardViewHolder(@NonNull View itemView) {
            super(itemView);
            frontText = itemView.findViewById(R.id.frontText);
            backText = itemView.findViewById(R.id.backText);
            memorizedCheckbox = itemView.findViewById(R.id.memorizedCheckbox);
        }
        
        void bind(Card card) {
            frontText.setText(card.getFrontText());
            backText.setText(card.getBackText());
            memorizedCheckbox.setChecked(card.isMemorized());
            
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCardClick(card);
                }
            });
            
            memorizedCheckbox.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMemorizedToggle(card);
                }
            });
        }
    }
}
