package com.example.vocab_app.models;

import java.util.List;

public class DecksResponse {
    private List<Deck> data;
    private Pagination pagination;

    public DecksResponse() {
    }

    public List<Deck> getData() {
        return data;
    }

    public void setData(List<Deck> data) {
        this.data = data;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
