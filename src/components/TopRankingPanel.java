package components;

import utils.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class TopRankingPanel extends JPanel {

    public TopRankingPanel() {
        setLayout(new BorderLayout());
        setBackground(Theme.CONTENT_BG);
        setBorder(new LineBorder(Theme.BORDER_COLOR, 1, true));
        setPreferredSize(new Dimension(300, 800));

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

        // Add 10 mock ranking items
        for (int i = 1; i <= 10; i++) {
            listPanel.add(createRankItem(i, "Tên truyện Top " + i, (150 - i * 10) + "K Lượt xem"));
            listPanel.add(Box.createVerticalStrut(5));
        }

        add(listPanel, BorderLayout.CENTER);
    }

    private JPanel createRankItem(int rank, String title, String views) {
        JPanel itemPanel = new JPanel(new BorderLayout(10, 0));
        itemPanel.setBackground(Theme.CONTENT_BG);
        itemPanel.setBorder(new EmptyBorder(5, 10, 5, 10));

        // Rank Number
        JLabel lblRank = new JLabel("0" + rank);
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

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(Theme.FONT_BOLD);
        lblTitle.setForeground(Theme.TEXT_MAIN);

        JLabel lblViews = new JLabel(views);
        lblViews.setFont(Theme.FONT_SMALL);
        lblViews.setForeground(Theme.TEXT_MUTED);

        detailPanel.add(lblTitle);
        detailPanel.add(lblViews);

        itemPanel.add(lblRank, BorderLayout.WEST);
        itemPanel.add(detailPanel, BorderLayout.CENTER);

        return itemPanel;
    }
}
