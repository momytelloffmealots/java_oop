package components;

import models.Chapter;
import models.Manga;
import utils.ImagePlaceholder;
import utils.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class MangaCard extends JPanel {
    public MangaCard(Manga manga, NavListener listener) {
        setLayout(new BorderLayout(0, 8));
        setBackground(Theme.CONTENT_BG);
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setPreferredSize(new Dimension(185, 290));
        setMaximumSize(new Dimension(185, 290)); // CRITICAL: Stop scaling up
        setMinimumSize(new Dimension(185, 290));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Image Placeholder (Cover)
        ImagePlaceholder cover = new ImagePlaceholder(manga.getCoverImage(), manga.getTitle(), 160, 210);
        cover.setRoundedCorners(10);
        
        // Details Panel
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(Theme.CONTENT_BG);
        detailsPanel.setBorder(new EmptyBorder(0, 5, 0, 5)); // horizontal padding

        // Title
        JLabel lblTitle = new JLabel(manga.getTitle(), SwingConstants.LEFT);
        lblTitle.setFont(Theme.FONT_BOLD);
        lblTitle.setForeground(Theme.TEXT_MAIN);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        detailsPanel.add(lblTitle);
        detailsPanel.add(Box.createVerticalStrut(5));

        // Chapters
        List<Chapter> chapters = manga.getChapters();
        if (chapters != null) {
            for (int i = 0; i < Math.min(2, chapters.size()); i++) {
                Chapter c = chapters.get(i);
                JPanel chapterRow = createChapterRow(c.getName(), c.getTimeAgo());
                chapterRow.setAlignmentX(Component.LEFT_ALIGNMENT);
                detailsPanel.add(chapterRow);
                if (i == 0) detailsPanel.add(Box.createVerticalStrut(3));
            }
        }

        add(cover, BorderLayout.CENTER);
        add(detailsPanel, BorderLayout.SOUTH);

        // -- Unified Mouse Listener for Click and Hover --
        MouseAdapter unifiedListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (listener != null) {
                    listener.onMangaClick(manga);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                lblTitle.setForeground(Theme.ACCENT_ORANGE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                lblTitle.setForeground(Theme.TEXT_MAIN);
            }
        };

        // Attach listener to all components that can intercept clicks
        addMouseListener(unifiedListener);
        lblTitle.addMouseListener(unifiedListener);
        cover.addMouseListener(unifiedListener);
        detailsPanel.addMouseListener(unifiedListener);
    }

    private JPanel createChapterRow(String chapter, String time) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.CONTENT_BG);
        
        JLabel lblChapter = new JLabel(chapter);
        lblChapter.setFont(Theme.FONT_SMALL);
        lblChapter.setForeground(Theme.TEXT_MAIN);
        
        JLabel lblTime = new JLabel(time);
        lblTime.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblTime.setForeground(Theme.TEXT_MUTED);

        panel.add(lblChapter, BorderLayout.WEST);
        panel.add(lblTime, BorderLayout.EAST);
        
        // Hover for chapter link
        lblChapter.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                lblChapter.setForeground(Theme.ACCENT_ORANGE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                lblChapter.setForeground(Theme.TEXT_MAIN);
            }
        });
        
        return panel;
    }
}
