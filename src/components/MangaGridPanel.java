package components;

import models.Chapter;
import models.Manga;
import utils.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MangaGridPanel extends JPanel {
    private List<Manga> mangas;

    public MangaGridPanel(NavListener listener, String title, boolean shuffleData, boolean hasSlider, String emptyMessage) {
        this(listener, title, shuffleData, hasSlider, emptyMessage, null);
    }

    public MangaGridPanel(NavListener listener, String title, boolean shuffleData, boolean hasSlider, String emptyMessage, List<Manga> customMangas) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Theme.APP_BG);

        if (customMangas != null) {
            this.mangas = customMangas;
        } else {
            // -- Generate Mock Manga Data --
            String[] mockTitles = {
                "Võ Luyện Đỉnh Phong", "Đại Quản Gia Là Ma Hoàng", "Ta Là Tà Đế", 
                "Cố Chân Nhân", "Đấu Phá Thương Khung", "Đấu La Đại Lục",
                "Trọng Sinh Đô Thị Tu Tiên", "Tuyệt Thế Đường Môn", "Toàn Chức Pháp Sư",
                "Bách Luyện Thành Thần", "Yêu Thần Ký", "Nguyên Tôn",
                "Sát Thủ Đỉnh Cao", "Vạn Cổ Thần Đế", "Dị Nhân", "Độc Bộ Tiêu Dao"
            };
            
            this.mangas = new ArrayList<>();
            for (int i = 0; i < mockTitles.length; i++) {
                Manga m = new Manga(String.valueOf(i), mockTitles[i], "cover_" + (i + 1) + ".png");
                m.setAuthor("Đang cập nhật");
                m.setStatus("Đang tiến hành");
                m.setGenres(Arrays.asList("Action", "Adventure", "Manhua", "Comedy"));
                m.setViews(764078 + (i * 1000));
                m.setRating(4.5);
                m.setRatingCount(1979901);
                m.setFollowers(6560984);
                m.setChapters(Arrays.asList(
                    new Chapter("Chapter " + (100 + i), "1 giờ trước", 5000),
                    new Chapter("Chapter " + (99 + i), "3 giờ trước", 5000),
                    new Chapter("Chapter " + (98 + i), "5 giờ trước", 4500),
                    new Chapter("Chapter " + (97 + i), "1 ngày trước", 6000)
                ));
                mangas.add(m);
            }

            if (shuffleData) {
                Collections.shuffle(mangas);
                for (Manga m : mangas) {
                    m.setChapters(Arrays.asList(
                        new Chapter(m.getChapters().get(0).getName(), (int)(Math.random() * 24) + " giờ trước", 5000),
                        new Chapter(m.getChapters().get(1).getName(), (int)(Math.random() * 24) + " giờ trước", 5000)
                    ));
                }
            }
        }

        // -- Optional Recommended Slider --
        if (hasSlider) {
            JPanel sliderWrapper = new JPanel(new BorderLayout(0, 10));
            sliderWrapper.setAlignmentX(Component.LEFT_ALIGNMENT); // Ghim lề trái
            sliderWrapper.setBackground(Theme.APP_BG);
            sliderWrapper.setBorder(new javax.swing.border.EmptyBorder(0, 0, 20, 0));

            JLabel lblRecommend = new JLabel("Truyện đề cử >");
            lblRecommend.setFont(Theme.FONT_LARGE_BOLD);
            lblRecommend.setForeground(Theme.ACCENT_ORANGE);
            lblRecommend.setCursor(new Cursor(Cursor.HAND_CURSOR));
            sliderWrapper.add(lblRecommend, BorderLayout.NORTH);

            JPanel sliderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
            sliderPanel.setBackground(Theme.APP_BG);

            for (int i = 0; i < Math.min(10, mangas.size()); i++) {
                sliderPanel.add(new RecommendCard(mangas.get(i), listener));
            }

            JScrollPane hScroll = new JScrollPane(sliderPanel);
            hScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            hScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
            hScroll.setBorder(null);
            hScroll.getHorizontalScrollBar().setUnitIncrement(20);
            hScroll.setPreferredSize(new Dimension(800, 270));
            hScroll.setMaximumSize(new Dimension(800, 270));

            sliderWrapper.add(hScroll, BorderLayout.CENTER);
            sliderWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 320)); // Constrain height
            add(sliderWrapper);
        }

        // -- Main Grid Header --
        JPanel gridHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 15));
        gridHeader.setAlignmentX(Component.LEFT_ALIGNMENT); // Ghim lề trái
        gridHeader.setBackground(Theme.APP_BG);
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(Theme.FONT_LARGE_BOLD);
        lblTitle.setForeground(Theme.ACCENT_ORANGE);
        gridHeader.add(lblTitle);
        gridHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50)); // Constrain title height
        add(gridHeader);

        // If empty message, show it instead
        if (emptyMessage != null && !emptyMessage.isEmpty()) {
            JPanel emptyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            emptyPanel.setBackground(Theme.APP_BG);
            emptyPanel.setBorder(new javax.swing.border.EmptyBorder(50, 0, 0, 0));
            
            JLabel lblMsg = new JLabel(emptyMessage);
            lblMsg.setFont(Theme.FONT_REGULAR);
            lblMsg.setForeground(Theme.TEXT_MUTED);
            emptyPanel.add(lblMsg);
            
            add(emptyPanel);
            return;
        }

        // Grid Panel for Cards
        JPanel gridPanel = new JPanel(new GridLayout(0, 4, 15, 20)); 
        gridPanel.setBackground(Theme.APP_BG);
        gridPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Add to grid
        if (mangas != null) {
            for (Manga m : mangas) {
                gridPanel.add(new MangaCard(m, listener));
            }
        }

        // To prevent GridLayout from squashing in BoxLayout, wrap it
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Theme.APP_BG);
        container.setAlignmentX(Component.LEFT_ALIGNMENT);
        container.add(gridPanel, BorderLayout.NORTH);
        
        // Prevent stretching horizontally beyond 4-column ideal width
        container.setMaximumSize(new Dimension(850, Integer.MAX_VALUE));
        container.setAlignmentX(Component.LEFT_ALIGNMENT); // Ghim lề trái
        
        add(container);
        add(Box.createVerticalGlue());
    }
}
