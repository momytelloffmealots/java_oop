package models;

import java.util.List;

public class Manga {
    private String id;
    private String title;
    private String otherNames;
    private String coverImage;
    private String author;
    private String status;
    private List<String> genres;
    private int views;
    private double rating;
    private int ratingCount;
    private int followers;
    private String description;
    private List<Chapter> chapters;

    public Manga(String id, String title, String coverImage) {
        this.id = id;
        this.title = title;
        this.coverImage = coverImage;
    }

    // Getters and Setters
    public String getId() { return id; }
    public String getTitle() { return title; }
    
    public String getOtherNames() { return otherNames; }
    public void setOtherNames(String otherNames) { this.otherNames = otherNames; }
    
    public String getCoverImage() { return coverImage; }
    
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public List<String> getGenres() { return genres; }
    public void setGenres(List<String> genres) { this.genres = genres; }
    
    public int getViews() { return views; }
    public void setViews(int views) { this.views = views; }
    
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    
    public int getRatingCount() { return ratingCount; }
    public void setRatingCount(int ratingCount) { this.ratingCount = ratingCount; }
    
    public int getFollowers() { return followers; }
    public void setFollowers(int followers) { this.followers = followers; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public List<Chapter> getChapters() { return chapters; }
    public void setChapters(List<Chapter> chapters) { this.chapters = chapters; }
}
