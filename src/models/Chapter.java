package models;

public class Chapter {
    private String name;
    private String timeAgo;
    private int views;

    public Chapter(String name, String timeAgo, int views) {
        this.name = name;
        this.timeAgo = timeAgo;
        this.views = views;
    }

    public String getName() { return name; }
    public String getTimeAgo() { return timeAgo; }
    public int getViews() { return views; }
}
