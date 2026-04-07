package components;

import models.Chapter;
import models.Manga;
import utils.ImagePlaceholder;
import utils.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.List;

public class MangaDetailPanel extends JPanel {
    private boolean isFollowed = false;
    private NavListener listener;

    
    public MangaDetailPanel(Manga manga, NavListener listener) {
        this.listener = listener;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Theme.CONTENT_BG);
        setBorder(new EmptyBorder(15, 30, 30, 30));

        // 1. Breadcrumb
        JPanel breadcrumb = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        breadcrumb.setBackground(Theme.CONTENT_BG);
        breadcrumb.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblB = new JLabel("Trang chủ > Thể loại > " + manga.getTitle());
        lblB.setFont(Theme.FONT_SMALL);
        lblB.setForeground(Theme.TEXT_LINK);
        breadcrumb.add(lblB);
        add(breadcrumb);

        // 2. Title & Update time
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Theme.CONTENT_BG);
        titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitle = new JLabel(manga.getTitle().toUpperCase());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Theme.TEXT_MAIN);
        
        JLabel lblUpdate = new JLabel("[Cập nhật lúc: 2026-04-07 15:30:12]");
        lblUpdate.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblUpdate.setForeground(Theme.TEXT_MUTED);

        titlePanel.add(lblTitle);
        titlePanel.add(lblUpdate);
        titlePanel.setBorder(new EmptyBorder(10, 0, 15, 0));
        add(titlePanel);

        // 3. Main Info Section
        JPanel mainInfo = new JPanel(new BorderLayout(25, 0));
        mainInfo.setBackground(Theme.CONTENT_BG);
        mainInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainInfo.setMaximumSize(new Dimension(2000, 350)); // Allow width to grow, but bound height

        // Left: Cover
        ImagePlaceholder cover = new ImagePlaceholder(manga.getCoverImage(), manga.getTitle(), 220, 310);
        cover.setRoundedCorners(0);
        cover.setBorder(BorderFactory.createLineBorder(Theme.BORDER_COLOR));
        mainInfo.add(cover, BorderLayout.WEST);

        // Right/Center: Metadata
        JPanel rightInfo = new JPanel();
        rightInfo.setLayout(new BoxLayout(rightInfo, BoxLayout.Y_AXIS));
        rightInfo.setBackground(Theme.CONTENT_BG);

        // Metadata rows
        JPanel metaDataGrid = new JPanel(new GridBagLayout());
        metaDataGrid.setBackground(Theme.CONTENT_BG);
        metaDataGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(4, 0, 4, 15);

        addMetaRow(metaDataGrid, gbc, 0, "Tên khác", manga.getOtherNames() != null ? manga.getOtherNames() : "Đang cập nhật");
        addMetaRow(metaDataGrid, gbc, 1, "Tác giả", manga.getAuthor() != null ? manga.getAuthor() : "Đang cập nhật");
        addMetaRow(metaDataGrid, gbc, 2, "Tình trạng", manga.getStatus() != null ? manga.getStatus() : "Đang tiến hành");
        
        String genresJoined = (manga.getGenres() != null) ? String.join(" - ", manga.getGenres()) : "Action - Adventure - Manhua";
        addMetaRow(metaDataGrid, gbc, 3, "Thể loại", genresJoined);
        addMetaRow(metaDataGrid, gbc, 4, "Lượt xem", String.format("%,d", manga.getViews() > 0 ? manga.getViews() : 764078));

        rightInfo.add(metaDataGrid);
        
        // Rating Stars
        JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        ratingPanel.setBackground(Theme.CONTENT_BG);
        ratingPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblStars = new JLabel("⭐⭐⭐⭐⭐");
        lblStars.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        JLabel lblRatingText = new JLabel("  " + manga.getRating() + "/5 - " + String.format("%,d", 19799012) + " Lượt đánh giá.");
        lblRatingText.setFont(Theme.FONT_SMALL);
        ratingPanel.add(lblStars);
        ratingPanel.add(lblRatingText);
        rightInfo.add(ratingPanel);

        // Follow Section
        JPanel followSection = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        followSection.setBackground(Theme.CONTENT_BG);
        followSection.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton btnFollow = createButton("Theo dõi", Theme.ACCENT_RED);
        btnFollow.addActionListener(e -> {
            isFollowed = !isFollowed;
            if (isFollowed) {
                btnFollow.setText("Đã theo dõi");
                btnFollow.setBackground(Color.YELLOW);
                btnFollow.setForeground(Color.BLACK);
            } else {
                btnFollow.setText("Theo dõi");
                btnFollow.setBackground(new Color(0, 123, 255)); 
                btnFollow.setForeground(Color.WHITE);
            }
        });

        // Override hover effect for follow button to respect its state
        btnFollow.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (isFollowed) {
                    btnFollow.setBackground(Color.YELLOW.darker());
                } else {
                    btnFollow.setBackground(new Color(0, 123, 255).darker());
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (isFollowed) {
                    btnFollow.setBackground(Color.YELLOW);
                } else {
                    btnFollow.setBackground(new Color(0, 123, 255));
                }
            }
        });

        JLabel lblFollowCount = new JLabel("<html><b>" + String.format("%,d", 6560984) + "</b> Người Đã Theo Dõi</html>");
        lblFollowCount.setFont(Theme.FONT_REGULAR);
        
        followSection.add(btnFollow);
        followSection.add(lblFollowCount);
        rightInfo.add(followSection);

        // Action Buttons
        JPanel readBtns = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        readBtns.setBackground(Theme.CONTENT_BG);
        readBtns.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton btnReadFirst = createButton("Đọc từ đầu", Theme.ACCENT_ORANGE);
        JButton btnReadLatest = createButton("Đọc mới nhất", Theme.ACCENT_ORANGE);
        
        btnReadFirst.addActionListener(e -> {
            if (listener != null && manga.getChapters() != null && !manga.getChapters().isEmpty()) {
                // Read the last chapter in list (Chapter 1)
                listener.onReadManga(manga, manga.getChapters().get(manga.getChapters().size() - 1));
            }
        });
        
        btnReadLatest.addActionListener(e -> {
            if (listener != null && manga.getChapters() != null && !manga.getChapters().isEmpty()) {
                // Read the first chapter in list (Latest)
                listener.onReadManga(manga, manga.getChapters().get(0));
            }
        });

        readBtns.add(btnReadFirst);
        readBtns.add(btnReadLatest);
        rightInfo.add(readBtns);

        mainInfo.add(rightInfo, BorderLayout.CENTER);
        add(mainInfo);

        // 4. Description Section
        add(Box.createVerticalStrut(25));
        JPanel descHeader = createSectionHeader("NỘI DUNG TRUYỆN " + manga.getTitle().toUpperCase());
        descHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(descHeader);
        
        String dummyDesc = "Chào mừng các đạo hữu thân mến của NetTruyen, hãy cùng thưởng thức bộ truyện tranh " + manga.getTitle() + " đầy cuốn hút trên website của chúng tôi. Bộ truyện này thuộc về các thể loại Action, Adventure, Drama, Manhua, Mystery, Truyện Màu và được cập nhật chap mới liên tục trên website.";
        JTextArea txtDesc = new JTextArea(manga.getDescription() != null ? manga.getDescription() : dummyDesc);
        txtDesc.setFont(Theme.FONT_REGULAR);
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setEditable(false);
        txtDesc.setBackground(Theme.CONTENT_BG);
        txtDesc.setBorder(new EmptyBorder(5, 0, 10, 0)); // Reduced bottom border from 20 to 10
        txtDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(txtDesc);

        // 5. Chapter List Section
        JPanel chapterHeader = createSectionHeader("DANH SÁCH CHƯƠNG");
        chapterHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(chapterHeader);
        
        JPanel chapterTable = new JPanel();
        chapterTable.setLayout(new BoxLayout(chapterTable, BoxLayout.Y_AXIS));
        chapterTable.setBackground(Color.WHITE);
        chapterTable.setBorder(BorderFactory.createLineBorder(Theme.BORDER_COLOR));
        chapterTable.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Header Table
        JPanel header = new JPanel(new GridLayout(1, 3));
        header.setBackground(new Color(245, 245, 245));
        header.setPreferredSize(new Dimension(800, 35));
        header.setMaximumSize(new Dimension(2000, 35));
        header.add(new JLabel("  Số chương", JLabel.LEFT));
        header.add(new JLabel("Cập nhật", JLabel.CENTER));
        header.add(new JLabel("Lượt xem  ", JLabel.RIGHT));
        chapterTable.add(header);

        // Rows
        List<Chapter> chapters = manga.getChapters();
        if (chapters != null) {
            for (Chapter c : chapters) {
                JPanel row = new JPanel(new GridLayout(1, 3));
                row.setBackground(Color.WHITE);
                row.setBorder(new MatteBorder(0, 0, 1, 0, Theme.BORDER_COLOR));
                row.setPreferredSize(new Dimension(800, 40));
                row.setMaximumSize(new Dimension(2000, 40));

                JLabel lblName = new JLabel("  " + c.getName());
                lblName.setFont(Theme.FONT_REGULAR);
                lblName.setForeground(Theme.TEXT_MAIN);
                lblName.setCursor(new Cursor(Cursor.HAND_CURSOR));

                JLabel lblTime = new JLabel(c.getTimeAgo(), JLabel.CENTER);
                lblTime.setFont(Theme.FONT_SMALL);
                lblTime.setForeground(Theme.TEXT_MUTED);

                JLabel lblViews = new JLabel(String.format("%,d", c.getViews() > 0 ? c.getViews() : 3351) + "  ", JLabel.RIGHT);
                lblViews.setFont(Theme.FONT_SMALL);
                lblViews.setForeground(Theme.TEXT_MUTED);

                row.add(lblName);
                row.add(lblTime);
                row.add(lblViews);
                chapterTable.add(row);
            }
        }
        add(chapterTable);

        // 6. Comment Section
        add(Box.createVerticalStrut(20));
        JPanel commentHeader = createSectionHeader("BÌNH LUẬN");
        commentHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(commentHeader);

        // Comment Input Area
        JPanel inputPanel = new JPanel(new BorderLayout(15, 10));
        inputPanel.setBackground(Theme.CONTENT_BG);
        inputPanel.setBorder(new EmptyBorder(15, 0, 20, 0));
        inputPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        inputPanel.setMaximumSize(new Dimension(2000, 150));

        JTextArea txtComment = new JTextArea("Mời bạn để lại bình luận...");
        txtComment.setFont(Theme.FONT_REGULAR);
        txtComment.setBorder(BorderFactory.createLineBorder(Theme.BORDER_COLOR));
        txtComment.setLineWrap(true);
        txtComment.setWrapStyleWord(true);
        txtComment.setForeground(Theme.TEXT_MUTED);
        
        // Simple placeholder effect
        txtComment.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (txtComment.getText().equals("Mời bạn để lại bình luận...")) {
                    txtComment.setText("");
                    txtComment.setForeground(Theme.TEXT_MAIN);
                }
            }
        });

        JButton btnSend = createButton("Gửi bình luận", Theme.ACCENT_ORANGE);
        btnSend.setPreferredSize(new Dimension(150, 40));
        
        // Mock Comments List Container
        JPanel commentsList = new JPanel();
        commentsList.setLayout(new BoxLayout(commentsList, BoxLayout.Y_AXIS));
        commentsList.setBackground(Theme.CONTENT_BG);
        commentsList.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnSend.addActionListener(e -> {
            String commentText = txtComment.getText().trim();
            if (commentText.isEmpty() || commentText.equals("Mời bạn để lại bình luận...")) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập nội dung bình luận!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Add to the top of list
            JPanel newComment = createCommentRow("Bạn (Guest)", commentText, "Vừa xong");
            commentsList.add(newComment, 0); // index 0 to put at top
            commentsList.add(Box.createVerticalStrut(15), 1);
            
            // Clear input
            txtComment.setText("");
            
            // Refresh UI
            commentsList.revalidate();
            commentsList.repaint();
        });

        inputPanel.add(new JScrollPane(txtComment), BorderLayout.CENTER);
        inputPanel.add(btnSend, BorderLayout.EAST);
        add(inputPanel);

        String[][] mockComments = {
            {"Lê Văn Tám", "Truyện hay quá, hóng chap mới từng ngày!", "10 phút trước"},
            {"Nguyễn Thị Bưởi", "Main buff hơi quá tay nhưng đọc vẫn cuốn.", "2 giờ trước"},
            {"Trần Dần", "Võ luyện đỉnh phong mãi đỉnh, đọc từ hồi cấp 3 giờ đi làm vẫn chưa hết.", "1 ngày trước"}
        };

        for (String[] c : mockComments) {
            commentsList.add(createCommentRow(c[0], c[1], c[2]));
            commentsList.add(Box.createVerticalStrut(15));
        }
        add(commentsList);
    }

    private JPanel createCommentRow(String user, String content, String time) {
        JPanel row = new JPanel(new BorderLayout(15, 5));
        row.setBackground(Theme.CONTENT_BG);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(2000, 100));

        // User Avatar Circle (Mock)
        JPanel avatar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(200, 200, 200));
                g2.fillOval(0, 0, 40, 40);
                g2.setColor(Color.WHITE);
                g2.setFont(Theme.FONT_BOLD);
                g2.drawString(user.substring(0, 1), 15, 25);
                g2.dispose();
            }
        };
        avatar.setPreferredSize(new Dimension(40, 40));
        avatar.setBackground(Theme.CONTENT_BG);
        
        row.add(avatar, BorderLayout.WEST);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Theme.CONTENT_BG);

        JLabel lblUser = new JLabel(user + " • " + time);
        lblUser.setFont(Theme.FONT_BOLD);
        lblUser.setForeground(Theme.TEXT_LINK);
        
        JLabel lblContent = new JLabel("<html>" + content + "</html>");
        lblContent.setFont(Theme.FONT_REGULAR);
        lblContent.setForeground(Theme.TEXT_MAIN);

        contentPanel.add(lblUser);
        contentPanel.add(Box.createVerticalStrut(3));
        contentPanel.add(lblContent);
        
        row.add(contentPanel, BorderLayout.CENTER);
        row.setBorder(new MatteBorder(0, 0, 1, 0, Theme.BORDER_COLOR));
        return row;
    }

    private void addMetaRow(JPanel panel, GridBagConstraints gbc, int row, String label, String value) {
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.weightx = 0;
        JLabel lblL = new JLabel(label + ":");
        lblL.setFont(Theme.FONT_REGULAR);
        lblL.setForeground(Theme.TEXT_MUTED);
        panel.add(lblL, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        JLabel lblV = new JLabel(value);
        lblV.setFont(Theme.FONT_REGULAR);
        if (label.equals("Thể loại")) lblV.setForeground(Theme.TEXT_LINK);
        panel.add(lblV, gbc);
    }

    private JPanel createSectionHeader(String title) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        p.setBackground(Theme.CONTENT_BG);
        p.setBorder(new MatteBorder(0, 0, 1, 0, Theme.TEXT_LINK));
        
        JLabel lbl = new JLabel(title);
        lbl.setFont(Theme.FONT_BOLD);
        lbl.setForeground(Theme.TEXT_LINK);
        p.add(lbl);
        return p;
    }

    private JButton createButton(String text, Color bg) {
        JButton b = new JButton(text);
        // User requested blue background, white text
        Color blueBg = new Color(0, 123, 255); // Standard Bootstrap Blue
        b.setBackground(blueBg);
        b.setForeground(Color.WHITE);
        b.setFont(Theme.FONT_BOLD);
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(140, 40));
        
        // Add hover effect
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                b.setBackground(blueBg.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                b.setBackground(blueBg);
            }
        });
        
        return b;
    }
}
