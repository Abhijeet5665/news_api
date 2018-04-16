package com.soumya.wwdablu.sangbad.network.model;

import java.util.LinkedList;

public class ArticleResponse {

    private String status;
    private String source;
    private String sortBy;
    private LinkedList<Articles> articles;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public LinkedList<Articles> getArticles() {
        return articles;
    }

    public void setArticles(LinkedList<Articles> articles) {
        this.articles = articles;
    }
}
