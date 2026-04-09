package components;

import utils.Theme;
// Removed unused import

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TopRankingPanel extends JPanel {

    public TopRankingPanel(List<models.Manga> mangas, NavListener listener) {
        setLayout(new BorderLayout());
        setBackground(Theme.CONTENT_BG);
        setBorder(new LineBorder(Theme.BORDER_COLOR, 1, true));
        setPreferredSize(new Dimension(300, 800));

        // -- LOGIC SẮP XẾP THEO LƯỢT XEM --
        if (mangas != null) {
            Collections.sort(mangas, new Comparator<models.Manga>() {
                @Override
                public int compare(models.Manga m1, models.Manga m2) {
                    return Integer.compare(m2.getViews(), m1.getViews()); // Giảm dần
                }
            });
        }

        // Header Title
        JLabel lblTitle = new JLabel("BẢNG XẾP HẠNG");
        lblTitle.setFont(Theme.FONT_LARGE_BOLD);
        lblTitle.setForeground(Theme.TEXT_MAIN);
        lblTitle.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(lblTitle, BorderLayout.NORTH);

        // List body
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Theme.CONTENT_BG);

        // Add top 10 ranking items
        if (mangas != null) {
            int limit = Math.min(10, mangas.size());
            for (int i = 0; i < limit; i++) {
                models.Manga m = mangas.get(i);
                listPanel.add(createRankItem(i + 1, m, listener));
                listPanel.add(Box.createVerticalStrut(5));
            }
        }

        add(listPanel, BorderLayout.CENTER);
    }

    private JPanel createRankItem(int rank, models.Manga manga, NavListener listener) {
        JPanel itemPanel = new JPanel(new BorderLayout(10, 0));
        itemPanel.setBackground(Theme.CONTENT_BG);
        itemPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
        itemPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Click event
        itemPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (listener != null) listener.onMangaClick(manga);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                itemPanel.setBackground(new Color(245, 245, 245));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                itemPanel.setBackground(Theme.CONTENT_BG);
            }
        });

        // Rank Number
        JLabel lblRank = new JLabel(String.format("%02d", rank));
        if (rank >= 10) lblRank.setText(String.valueOf(rank));
        
        lblRank.setFont(new Font("Segoe UI", Font.BOLD, 20));
        
        // Colors for top 3
        if (rank == 1) lblRank.setForeground(Color.decode("#e74c3c")); // Top 1 Red
        else if (rank == 2) lblRank.setForeground(Color.decode("#2ecc71")); // Top 2 Green
        else if (rank == 3) lblRank.setForeground(Color.decode("#f39c12")); // Top 3 Orange
        else lblRank.setForeground(Theme.TEXT_MUTED);
        
        lblRank.setPreferredSize(new Dimension(35, 30));

        // Details
        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        detailPanel.setBackground(Theme.CONTENT_BG);

        JLabel lblTitle = new JLabel(manga.getTitle());
        lblTitle.setFont(Theme.FONT_BOLD);
        lblTitle.setForeground(Theme.TEXT_MAIN);

        JLabel lblViews = new JLabel(String.format("%,d Lượt xem", manga.getViews()));
        lblViews.setFont(Theme.FONT_SMALL);
        lblViews.setForeground(Theme.TEXT_MUTED);

        detailPanel.add(lblTitle);
        detailPanel.add(lblViews);

        itemPanel.add(lblRank, BorderLayout.WEST);
        itemPanel.add(detailPanel, BorderLayout.CENTER);

        return itemPanel;
    }
}
