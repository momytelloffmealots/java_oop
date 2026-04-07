package utils;

import java.awt.Color;
import java.awt.Font;

public class Theme {
    // Colors for Light Theme
    public static final Color APP_BG = Color.decode("#ebebeb");
    public static final Color CONTENT_BG = Color.WHITE;
    public static final Color HEADER_BG = Color.decode("#1e1e1e"); // Dark header
    public static final Color NAV_BG = Color.decode("#ffffff"); // White navbar under header
    
    public static final Color TEXT_MAIN = Color.decode("#333333");
    public static final Color TEXT_HEADER = Color.WHITE;
    public static final Color TEXT_MUTED = Color.decode("#888888");
    public static final Color TEXT_LINK = Color.decode("#2980b9");
    
    public static final Color ACCENT_RED = Color.decode("#e74c3c");
    public static final Color ACCENT_ORANGE = Color.decode("#f39c12");
    public static final Color BORDER_COLOR = Color.decode("#dddddd");

    // Fonts
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_LARGE_BOLD = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_SMALL_BOLD = new Font("Segoe UI", Font.BOLD, 12);
}
