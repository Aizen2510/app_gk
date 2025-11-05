package com.example.vocab_app.models;

public class DeckStats {
    private String deckId;
    private String deckName;
    private int totalCards;
    private int memorizedCards;
    private int unmemorizedCards;
    private int progressPercentage;
    private int recentCards;

    public String getDeckId() {
        return deckId;
    }

    public String getDeckName() {
        return deckName;
    }

    public int getTotalCards() {
        return totalCards;
    }

    public int getMemorizedCards() {
        return memorizedCards;
    }

    public int getUnmemorizedCards() {
        return unmemorizedCards;
    }

    public int getProgressPercentage() {
        return progressPercentage;
    }

    public int getRecentCards() {
        return recentCards;
    }
}


