package components;

import models.Chapter;
import models.Manga;
import utils.Theme;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

public class RecommendCard extends JPanel {
    private Manga manga;
    private Image image;
    private boolean isHovered = false;

    public RecommendCard(Manga manga, NavListener listener) {
        this.manga = manga;
        setPreferredSize(new Dimension(190, 250)); // Slightly wider/taller for slider
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        try {
            File imgFile = new File("src/images/" + manga.getCoverImage());
            if (imgFile.exists()) {
                image = ImageIO.read(imgFile);
            }
        } catch (Exception e) {
            System.err.println("Could not load image: " + manga.getCoverImage());
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (listener != null) {
                    listener.onMangaClick(manga);
                }
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // Background / Image
        if (image != null) {
            int imgW = image.getWidth(null);
            int imgH = image.getHeight(null);
            
            if (imgW > 0 && imgH > 0) {
                double targetRatio = (double) w / h;
                double imageRatio = (double) imgW / imgH;
                
                int drawW, drawH, drawX, drawY;
                
                if (imageRatio > targetRatio) {
                    drawH = h;
                    drawW = (int) (h * imageRatio);
                    drawX = (w - drawW) / 2;
                    drawY = 0;
                } else {
                    drawW = w;
                    drawH = (int) (w / imageRatio);
                    drawX = 0;
                    drawY = (h - drawH) / 2;
                }
                
                g2.drawImage(image, drawX, drawY, drawW, drawH, this);
            }
        } else {
            g2.setColor(Color.LIGHT_GRAY);
            g2.fillRect(0, 0, w, h);
            g2.setColor(Color.DARK_GRAY);
            g2.setFont(Theme.FONT_REGULAR);
            g2.drawString("No Image", w / 2 - 30, h / 2);
        }

        // Hover Effect Layer (darkens image slightly)
        if (isHovered) {
            g2.setColor(new Color(0, 0, 0, 60)); // slight dark overlay
            g2.fillRect(0, 0, w, h);
        }

        // Bottom translucent text background
        int textPanelHeight = 50;
        int textYStart = h - textPanelHeight;
        
        g2.setColor(new Color(0, 0, 0, 180)); // 70% opacity black
        g2.fillRect(0, textYStart, w, textPanelHeight);

        // Title
        g2.setColor(isHovered ? Theme.ACCENT_ORANGE : Color.WHITE);
        g2.setFont(Theme.FONT_BOLD);
        FontMetrics fmBold = g2.getFontMetrics();
        
        String drawTitle = manga.getTitle();
        if (fmBold.stringWidth(drawTitle) > w - 20) {
            drawTitle = drawTitle.substring(0, Math.min(drawTitle.length(), 18)) + "...";
        }
        int titleX = 10; // Left aligned with margin
        g2.drawString(drawTitle, titleX, textYStart + 20);
        
        // Chapter & Time
        g2.setColor(Color.WHITE);
        g2.setFont(Theme.FONT_SMALL);
        
        String subText = "";
        List<Chapter> chapters = manga.getChapters();
        if (chapters != null && !chapters.isEmpty()) {
            Chapter latest = chapters.get(0);
            subText = latest.getName() + "   " + latest.getTimeAgo();
        }
        
        int subTextX = 10; // Left aligned with margin
        g2.drawString(subText, subTextX, textYStart + 40);

        g2.dispose();
    }
}
