package components;

public interface NavListener {
    void onNavigate(String page, String detail);
    void onMangaClick(models.Manga manga);
    void onSearch(String query);
    void onReadManga(models.Manga manga, models.Chapter chapter);
    void onToggleFollow(models.Manga manga);
    boolean isMangaFollowed(String mangaId);
}
