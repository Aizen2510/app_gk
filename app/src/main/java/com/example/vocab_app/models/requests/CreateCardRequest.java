package com.example.vocab_app.models.requests;

public class CreateCardRequest {
    private String frontText;
    private String backText;
    private boolean memorized;

    public CreateCardRequest(String frontText, String backText, boolean memorized) {
        this.frontText = frontText;
        this.backText = backText;
        this.memorized = memorized;
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
}
