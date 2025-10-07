package com.example.vocab_app.models;

public class Card {
    private String id;
    private String frontText;
    private String backText;
    private boolean memorized;
    private String deckId;
    private String createdAt;
    private String updatedAt;

    public Card() {
    }

    public Card(String id, String frontText, String backText, boolean memorized, String deckId, String createdAt, String updatedAt) {
        this.id = id;
        this.frontText = frontText;
        this.backText = backText;
        this.memorized = memorized;
        this.deckId = deckId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrontText() {
        return frontText;
    }

    public void setFrontText(String frontText) {
        this.frontText = frontText;
    }

    public String getBackText() {
        return backText;
    }

    public void setBackText(String backText) {
        this.backText = backText;
    }

    public boolean isMemorized() {
        return memorized;
    }

    public void setMemorized(boolean memorized) {
        this.memorized = memorized;
    }

    public String getDeckId() {
        return deckId;
    }

    public void setDeckId(String deckId) {
        this.deckId = deckId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
