package main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UIJAV extends JFrame {

    // ===== DYNAMIC THEME COLORS =====
    private boolean isDarkMode = true;

    private Color COLOR_NAV_RAIL;
    private Color COLOR_CHAT_LIST;
    private Color COLOR_CHAT_BG;
    private Color COLOR_HOVER;
    private Color COLOR_ACTIVE_ITEM;
    private Color COLOR_MY_MSG;
    private Color COLOR_OTHER_MSG;
    private Color COLOR_TEXT_PRIMARY;
    private Color COLOR_TEXT_SECONDARY;
    private Color COLOR_BORDER;
    private Color COLOR_INPUT_BG;

    // ===== COMPONENTS =====
    private JPanel messagePanel;
    private JScrollPane messageScroll;
    private JLabel currentChatTitle;
    private JLabel chatStatusLabel;
    private JTextField inputField;
    private JPanel selectedItem = null;
    private String currentChatName = null;

    private JPanel mainLayout;
    private JPanel centerContainer; // CardLayout container
    private CardLayout centerCardLayout;
    private JPanel chatListPanel; // "chats" card
    private JPanel peoplePanel;   // "people" card
    private JPanel profilePanel;  // "profile" card
    private JPanel rail;
    private JPanel chatArea;
    private JPanel chatFooter;
    private JPanel chatHead;
    private RoundPanel searchBox;
    private RoundPanel inputRound;

    // ===== AUTO REPLY DATA =====
    private Map<String, String[]> autoReplies = new HashMap<>();
    private Map<String, Integer> replyIndex = new HashMap<>();

    // ===== USER PROFILE =====
    private String userName = "Phú";
    private String userNote = "Đang sử dụng Messenger";
    private int userAvatarColorIdx = 0;
    private AvatarPanel navAvatarPanel;
    private Image userAvatarImage = null;

    // ===== CLOCK =====
    private JLabel lblClock;
    private JPanel topClockPanel;

    // ===== AVATAR COLORS =====
    private final Color[] AVATAR_COLORS = {
        new Color(0, 132, 255),   // Blue
        new Color(255, 99, 72),    // Red-Orange
        new Color(160, 90, 220),   // Purple
        new Color(0, 200, 130),    // Green
        new Color(255, 165, 0),    // Orange
        new Color(220, 60, 150),   // Pink
    };

    public UIJAV() {
        updateThemeColors();
        initAutoReplies();

        setTitle("Messenger Modern Edition");
        setSize(1200, 800);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        mainLayout = new JPanel(new BorderLayout());
        mainLayout.setBackground(COLOR_CHAT_BG);

        rail = createNavRail();
        mainLayout.add(rail, BorderLayout.WEST);

        centerCardLayout = new CardLayout();
        centerContainer = new JPanel(centerCardLayout);

        // Sidebar content
        chatListPanel = createSidebar();
        peoplePanel = createPeoplePanel();
        profilePanel = createProfilePanel();
        chatArea = createChatArea();

        // Standard Chat View (Sidebar + Chat Area)
        JPanel chatViewPanel = new JPanel(new BorderLayout());
        chatViewPanel.add(chatListPanel, BorderLayout.WEST);
        chatViewPanel.add(chatArea, BorderLayout.CENTER);

        // Add main views to CardLayout
        centerContainer.add(chatViewPanel, "chats");
        centerContainer.add(peoplePanel, "people");
        centerContainer.add(profilePanel, "profile");
        
        centerCardLayout.show(centerContainer, "chats");

        mainLayout.add(centerContainer, BorderLayout.CENTER);

        // --- REALTIME CLOCK ---
        topClockPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 4));
        topClockPanel.setOpaque(false);
        lblClock = new JLabel();
        lblClock.setFont(new Font("Segoe UI", Font.BOLD, 12));
        topClockPanel.add(lblClock);
        mainLayout.add(topClockPanel, BorderLayout.NORTH);

        Timer clockTimer = new Timer(1000, e -> updateClock());
        clockTimer.start();
        updateClock();

        add(mainLayout);

        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        refreshUI(); // Đồng bộ theme màu cho các icon ngay khi mới mở
    }

    private void updateClock() {
        java.util.Calendar now = java.util.Calendar.getInstance();
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("HH  :  mm  :  ss    |    dd / MM / yyyy");
        lblClock.setText("HA NOI , VIET NAM | " + formatter.format(now.getTime()));
    }

    private void initAutoReplies() {
        autoReplies.put("Elon Musk", new String[]{
            "SpaceX sẽ lên sao Hoả năm 2028! ",
            "Tesla đang phát triển robot mới, đỉnh lắm!",
            "Bạn có muốn đặt vé lên Mars không? ",
            "X.com sắp ra tính năng mới rồi!",
            "Boring Company đào xong đường hầm LA rồi!"
        });
        autoReplies.put("Mark Zuckerberg", new String[]{
            "Meta Quest 4 sắp ra mắt rồi! ",
            "AI nội bộ của Meta mạnh lắm!",
            "Threads đã vượt 200 triệu user! ",
            "Bạn thử dùng Llama 4 chưa?",
            "Metaverse sẽ thành hiện thực, tin tôi đi!"
        });
        autoReplies.put("Taylor Swift", new String[]{
            "Album mới đang #1 Billboard! ",
            "Eras Tour vòng 2 sắp bắt đầu!",
            "Bạn thích bài nào nhất trong album mới?",
            "Shake it off! ",
            "Cảm ơn bạn đã ủng hộ nhé! "
        });
        autoReplies.put("Bill Gates", new String[]{
            "AI sẽ thay đổi giáo dục hoàn toàn! ",
            "Breakthrough Energy đang đầu tư mạnh!",
            "Bạn đọc sách mới của tôi chưa?",
            "Nuclear energy là tương lai đấy!",
            "Hẹn cafe tuần sau nhé! "
        });
        autoReplies.put("Lisa", new String[]{
            "Album solo sắp ra rồi! ",
            "LALISA LOVE! 💛",
            "Bạn có đi concert không?",
            "Cảm ơn Blink nhiều lắm! ",
            "Đang quay MV mới nè!"
        });
        autoReplies.put("Sơn Tùng MTP", new String[]{
            "Bài mới sắp drop rồi! ",
            "Sky ơi chờ tôi nhé!",
            "MV lần này đầu tư khủng lắm!",
            "Hãy tin tôi đi! ",
            "Concert sắp tới sẽ rất đặc biệt!"
        });
    }

    private void updateThemeColors() {
        if (isDarkMode) {
            COLOR_NAV_RAIL = new Color(36, 37, 38);
            COLOR_CHAT_LIST = new Color(24, 25, 26);
            COLOR_CHAT_BG = new Color(24, 25, 26);
            COLOR_HOVER = new Color(58, 59, 60);
            COLOR_ACTIVE_ITEM = new Color(30, 45, 65);
            COLOR_MY_MSG = new Color(0, 132, 255);
            COLOR_OTHER_MSG = new Color(58, 59, 60);
            COLOR_TEXT_PRIMARY = new Color(228, 230, 235);
            COLOR_TEXT_SECONDARY = new Color(176, 179, 184);
            COLOR_BORDER = new Color(50, 50, 50);
            COLOR_INPUT_BG = new Color(58, 59, 60);
        } else {
            COLOR_NAV_RAIL = new Color(245, 245, 245);
            COLOR_CHAT_LIST = new Color(255, 255, 255);
            COLOR_CHAT_BG = new Color(255, 255, 255);
            COLOR_HOVER = new Color(235, 235, 235);
            COLOR_ACTIVE_ITEM = new Color(220, 235, 255);
            COLOR_MY_MSG = new Color(0, 132, 255);
            COLOR_OTHER_MSG = new Color(230, 230, 230);
            COLOR_TEXT_PRIMARY = new Color(0, 0, 0);
            COLOR_TEXT_SECONDARY = new Color(70, 70, 70);
            COLOR_BORDER = new Color(220, 220, 220);
            COLOR_INPUT_BG = new Color(240, 240, 240);
        }
    }

    private void toggleTheme() {
        isDarkMode = !isDarkMode;
        updateThemeColors();
        refreshUI();
    }

    private void refreshUI() {
        mainLayout.setBackground(COLOR_CHAT_BG);
        rail.setBackground(COLOR_NAV_RAIL);
        
        if (topClockPanel != null) {
            topClockPanel.setBackground(COLOR_CHAT_BG);
            topClockPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));
        }
        if (lblClock != null) {
            lblClock.setForeground(COLOR_TEXT_SECONDARY);
        }

        chatListPanel.setBackground(COLOR_CHAT_LIST);
        chatListPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, COLOR_BORDER));
        peoplePanel.setBackground(COLOR_CHAT_LIST);
        peoplePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, COLOR_BORDER));
        chatArea.setBackground(COLOR_CHAT_BG);

        // Chat Header
        chatHead.setBackground(COLOR_CHAT_BG);
        chatHead.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));
        currentChatTitle.setForeground(COLOR_TEXT_PRIMARY);
        chatStatusLabel.setForeground(COLOR_TEXT_SECONDARY);

        // Search bar
        searchBox.setBackground(COLOR_INPUT_BG);

        // Input area
        chatFooter.setBackground(COLOR_CHAT_BG);
        inputRound.setBackground(COLOR_INPUT_BG);
        inputField.setBackground(COLOR_INPUT_BG);
        inputField.setForeground(COLOR_TEXT_PRIMARY);
        inputField.setCaretColor(COLOR_TEXT_PRIMARY);

        // Chat list sidebar
        Component sidebarHeader = ((BorderLayout) chatListPanel.getLayout()).getLayoutComponent(BorderLayout.NORTH);
        if (sidebarHeader instanceof JPanel) {
            refreshAllLabels((Container) sidebarHeader);
        }
        Component scroll = ((BorderLayout) chatListPanel.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        if (scroll instanceof JScrollPane) {
            JViewport viewport = ((JScrollPane) scroll).getViewport();
            Component list = viewport.getView();
            if (list instanceof JPanel) {
                list.setBackground(COLOR_CHAT_LIST);
                for (Component contact : ((JPanel) list).getComponents()) {
                    if (contact instanceof JPanel) {
                        contact.setBackground(COLOR_CHAT_LIST);
                        refreshAllLabels((Container) contact);
                    }
                }
            }
        }

        // People panel - deep refresh backgrounds and buttons
        refreshPeoplePanel();

        // Profile panel update
        refreshProfilePanel();

        // Nav Rail icons
        refreshNavRail();

        // Reset selection
        selectedItem = null;
        messagePanel.setBackground(COLOR_CHAT_BG);

        revalidate();
        repaint();
    }

    private void refreshPeoplePanel() {
        refreshAllLabels(peoplePanel);
        // Deep update backgrounds and buttons in people panel
        Component pScroll = ((BorderLayout) peoplePanel.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        if (pScroll instanceof JScrollPane) {
            Component pList = ((JScrollPane) pScroll).getViewport().getView();
            if (pList instanceof JPanel) {
                pList.setBackground(COLOR_CHAT_LIST);
                for (Component personItem : ((JPanel) pList).getComponents()) {
                    if (personItem instanceof JPanel) {
                        personItem.setBackground(COLOR_CHAT_LIST);
                        refreshButtonsDeep((Container) personItem);
                    }
                }
            }
        }
    }

    private void refreshButtonsDeep(Container container) {
        for (Component c : container.getComponents()) {
            if (c instanceof JButton) {
                JButton btn = (JButton) c;
                String txt = btn.getText();
                // "Thêm bạn" stays blue, "Xóa" adapts to theme
                if (txt.contains("Xóa")) {
                    btn.setBackground(COLOR_HOVER);
                    btn.setForeground(COLOR_TEXT_PRIMARY);
                }
                // "Đã gửi" stays gray
            }
            if (c instanceof Container) {
                refreshButtonsDeep((Container) c);
            }
        }
    }

    private void refreshNavRail() {
        for (Component c : rail.getComponents()) {
            if (c instanceof JPanel && !(c instanceof AvatarPanel)) {
                // Rail button panels contain a JLabel icon
                for (Component inner : ((JPanel) c).getComponents()) {
                    if (inner instanceof JLabel) {
                        inner.setForeground(COLOR_TEXT_PRIMARY);
                    }
                }
                // Also traverse deeper (for nested panels like chatBtn, peopleBtn)
                if (c instanceof Container) {
                    for (Component deep : ((Container) c).getComponents()) {
                        if (deep instanceof JPanel && !(deep instanceof AvatarPanel)) {
                            for (Component label : ((JPanel) deep).getComponents()) {
                                if (label instanceof JLabel) {
                                    label.setForeground(COLOR_TEXT_PRIMARY);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void refreshAllLabels(Container container) {
        for (Component c : container.getComponents()) {
            if (c instanceof JLabel) {
                JLabel lbl = (JLabel) c;
                if (lbl.getFont().getStyle() == Font.BOLD) {
                    lbl.setForeground(COLOR_TEXT_PRIMARY);
                } else {
                    lbl.setForeground(COLOR_TEXT_SECONDARY);
                }
            }
            if (c instanceof Container) {
                refreshAllLabels((Container) c);
            }
        }
    }

    // ================= COLUMN 1: NAV RAIL =================
    private JPanel createNavRail() {
        JPanel r = new JPanel();
        r.setPreferredSize(new Dimension(72, 0));
        r.setBackground(COLOR_NAV_RAIL);
        r.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 25));

        r.add(Box.createVerticalStrut(10));
        navAvatarPanel = new AvatarPanel(userName.substring(0, 1), 44, AVATAR_COLORS[userAvatarColorIdx]);
        navAvatarPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        navAvatarPanel.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                refreshProfilePanel();
                centerCardLayout.show(centerContainer, "profile");
            }
        });
        r.add(navAvatarPanel);
        JPanel chatBtn = createRailButton("💬");
        chatBtn.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                centerCardLayout.show(centerContainer, "chats");
            }
        });
        r.add(chatBtn);

        JPanel peopleBtn = createRailButton("👥");
        peopleBtn.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                centerCardLayout.show(centerContainer, "people");
            }
        });
        r.add(peopleBtn);

        r.add(createRailButton("📦"));

        JPanel settingsIcon = createRailButton("⚙️");
        settingsIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                showSettingsPopup(settingsIcon);
            }
        });
        r.add(settingsIcon);

        return r;
    }

    private void showSettingsPopup(Component invoker) {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem item = new JMenuItem(isDarkMode ? "☀️  Chuyển sang Light Mode" : "🌙  Chuyển sang Dark Mode");
        item.addActionListener(e -> toggleTheme());
        menu.add(item);
        menu.show(invoker, invoker.getWidth(), 0);
    }

    private JPanel createRailButton(String icon) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setPreferredSize(new Dimension(50, 50));
        p.setOpaque(false);
        p.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JLabel l = new JLabel(icon);
        l.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        p.add(l);
        p.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                p.setOpaque(true);
                p.setBackground(COLOR_HOVER);
                p.repaint();
            }

            public void mouseExited(MouseEvent e) {
                p.setOpaque(false);
                p.repaint();
            }
        });
        return p;
    }

    // ================= COLUMN 2B: PEOPLE PANEL =================
    private JPanel createPeoplePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_CHAT_LIST);
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, COLOR_BORDER));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(24, 16, 12, 16));

        JLabel title = new JLabel("Mọi Người");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(COLOR_TEXT_PRIMARY);
        header.add(title, BorderLayout.NORTH);

        JLabel subtitle = new JLabel("Những người bạn có thể biết");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(COLOR_TEXT_SECONDARY);
        subtitle.setBorder(new EmptyBorder(8, 0, 0, 0));
        header.add(subtitle, BorderLayout.SOUTH);

        panel.add(header, BorderLayout.NORTH);

        // People List
        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBackground(COLOR_CHAT_LIST);

        list.add(createPersonItem("Nguyễn Văn A", "12 bạn chung", 0));
        list.add(createPersonItem("Trần Thị B", "5 bạn chung", 1));
        list.add(createPersonItem("Lê Hoàng C", "8 bạn chung", 2));
        list.add(createPersonItem("Phạm Minh D", "3 bạn chung", 3));
        list.add(createPersonItem("Đỗ Quốc E", "15 bạn chung", 4));
        list.add(createPersonItem("Vũ Thanh F", "7 bạn chung", 5));
        list.add(createPersonItem("Hoàng Gia G", "2 bạn chung", 0));
        list.add(createPersonItem("Ngô Bảo H", "10 bạn chung", 1));

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createPersonItem(String name, String mutualInfo, int colorIdx) {
        JPanel item = new JPanel(new BorderLayout(12, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        item.setOpaque(true);
        item.setBackground(COLOR_CHAT_LIST);
        item.setMaximumSize(new Dimension(1100, 80));
        item.setPreferredSize(new Dimension(1100, 80));
        item.setBorder(new EmptyBorder(10, 12, 10, 12));

        // Avatar
        item.add(new AvatarPanel(name.substring(0, 1), 45, AVATAR_COLORS[colorIdx % AVATAR_COLORS.length]), BorderLayout.WEST);

        // Info + Buttons
        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);

        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setOpaque(false);
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setForeground(COLOR_TEXT_PRIMARY);
        JLabel mutualLabel = new JLabel(mutualInfo);
        mutualLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        mutualLabel.setForeground(COLOR_TEXT_SECONDARY);
        infoPanel.add(nameLabel);
        infoPanel.add(mutualLabel);
        center.add(infoPanel, BorderLayout.CENTER);

        // Action Buttons
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 8));
        buttons.setOpaque(false);

        JButton addBtn = new JButton("Thêm bạn");
        addBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        addBtn.setBackground(new Color(0, 132, 255));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);
        addBtn.setBorderPainted(false);
        addBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addBtn.setPreferredSize(new Dimension(90, 30));

        JButton removeBtn = new JButton("Xóa");
        removeBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        removeBtn.setBackground(COLOR_HOVER);
        removeBtn.setForeground(COLOR_TEXT_PRIMARY);
        removeBtn.setFocusPainted(false);
        removeBtn.setBorderPainted(false);
        removeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        removeBtn.setPreferredSize(new Dimension(60, 30));

        addBtn.addActionListener(e -> {
            addBtn.setText("Đã gửi ✓");
            addBtn.setBackground(new Color(80, 80, 80));
            addBtn.setEnabled(false);
        });

        removeBtn.addActionListener(e -> {
            Container parent = item.getParent();
            parent.remove(item);
            parent.revalidate();
            parent.repaint();
        });

        buttons.add(addBtn);
        buttons.add(removeBtn);
        center.add(buttons, BorderLayout.EAST);

        item.add(center, BorderLayout.CENTER);

        // Hover effect
        item.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { item.setBackground(COLOR_HOVER); item.repaint(); }
            public void mouseExited(MouseEvent e) { item.setBackground(COLOR_CHAT_LIST); item.repaint(); }
        });

        return item;
    }

    // ================= COLUMN 2C: PROFILE PANEL =================
    private JTextField profileNameField;
    private JTextArea profileNoteArea;
    private AvatarPanel profileAvatarDisplay;

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_CHAT_LIST);
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, COLOR_BORDER));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(24, 16, 8, 16));

        JLabel title = new JLabel("Trang cá nhân");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(COLOR_TEXT_PRIMARY);
        header.add(title, BorderLayout.NORTH);
        panel.add(header, BorderLayout.NORTH);

        // Content
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(COLOR_CHAT_LIST);
        content.setBorder(new EmptyBorder(10, 20, 20, 20));

        // Avatar (large, centered)
        JPanel avatarSection = new JPanel(new FlowLayout(FlowLayout.CENTER));
        avatarSection.setOpaque(false);
        avatarSection.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        profileAvatarDisplay = new AvatarPanel(userName.substring(0, 1), 80, AVATAR_COLORS[userAvatarColorIdx]);
        profileAvatarDisplay.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        avatarSection.add(profileAvatarDisplay);
        content.add(avatarSection);

        // "Change avatar" section with upload
        JPanel changeAvatarSection = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        changeAvatarSection.setOpaque(false);
        changeAvatarSection.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        JButton btnUploadAvatar = new JButton("Tải ảnh từ thiết bị");
        btnUploadAvatar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnUploadAvatar.setFocusPainted(false);
        btnUploadAvatar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnUploadAvatar.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn ảnh đại diện");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Hình ảnh", "jpg", "png", "jpeg"));
            if (fileChooser.showOpenDialog(UIJAV.this) == JFileChooser.APPROVE_OPTION) {
                try {
                    Image newImg = new ImageIcon(fileChooser.getSelectedFile().getAbsolutePath()).getImage();
                    userAvatarImage = newImg;
                    profileAvatarDisplay.setImage(newImg);
                    navAvatarPanel.setImage(newImg);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(UIJAV.this, "Lỗi khi tải ảnh: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        changeAvatarSection.add(btnUploadAvatar);

        JLabel changeLabel = new JLabel("Hoặc đổi màu ảnh:");
        changeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        changeLabel.setForeground(COLOR_TEXT_SECONDARY);
        changeAvatarSection.add(changeLabel);
        content.add(changeAvatarSection);

        // Avatar Color Picker
        JPanel colorPicker = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        colorPicker.setOpaque(false);
        colorPicker.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        for (int i = 0; i < AVATAR_COLORS.length; i++) {
            final int idx = i;
            JPanel colorDot = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getParent().getBackground());
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.setColor(AVATAR_COLORS[idx]);
                    g2.fill(new Ellipse2D.Double(2, 2, 28, 28));
                    // Border for selected
                    if (idx == userAvatarColorIdx) {
                        g2.setColor(Color.WHITE);
                        g2.setStroke(new BasicStroke(2));
                        g2.draw(new Ellipse2D.Double(2, 2, 28, 28));
                    }
                    g2.dispose();
                }
            };
            colorDot.setOpaque(false);
            colorDot.setPreferredSize(new Dimension(32, 32));
            colorDot.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            colorDot.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    userAvatarColorIdx = idx;
                    userAvatarImage = null; // Clear image when color picked
                    // Update profile avatar
                    profileAvatarDisplay.setImage(null);
                    profileAvatarDisplay.c = AVATAR_COLORS[idx];
                    profileAvatarDisplay.repaint();
                    // Update nav avatar
                    navAvatarPanel.setImage(null);
                    navAvatarPanel.c = AVATAR_COLORS[idx];
                    navAvatarPanel.repaint();
                    // Repaint color picker to show selection
                    colorPicker.repaint();
                }
            });
            colorPicker.add(colorDot);
        }
        content.add(colorPicker);

        content.add(Box.createVerticalStrut(20));

        // Name Section
        JPanel nameSection = new JPanel(new BorderLayout());
        nameSection.setOpaque(false);
        nameSection.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));
        JLabel nameLabel = new JLabel("Tên hiển thị");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setForeground(COLOR_TEXT_PRIMARY);
        nameSection.add(nameLabel, BorderLayout.NORTH);

        profileNameField = new JTextField(userName);
        profileNameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        profileNameField.setBackground(COLOR_INPUT_BG);
        profileNameField.setForeground(COLOR_TEXT_PRIMARY);
        profileNameField.setCaretColor(COLOR_TEXT_PRIMARY);
        profileNameField.setBorder(new EmptyBorder(8, 12, 8, 12));
        nameSection.add(profileNameField, BorderLayout.CENTER);
        content.add(nameSection);

        content.add(Box.createVerticalStrut(15));

        // Note / Bio Section
        JPanel noteSection = new JPanel(new BorderLayout());
        noteSection.setOpaque(false);
        noteSection.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        JLabel noteLabel = new JLabel("Ghi chú cá nhân");
        noteLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        noteLabel.setForeground(COLOR_TEXT_PRIMARY);
        noteSection.add(noteLabel, BorderLayout.NORTH);

        profileNoteArea = new JTextArea(userNote);
        profileNoteArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        profileNoteArea.setBackground(COLOR_INPUT_BG);
        profileNoteArea.setForeground(COLOR_TEXT_PRIMARY);
        profileNoteArea.setCaretColor(COLOR_TEXT_PRIMARY);
        profileNoteArea.setBorder(new EmptyBorder(8, 12, 8, 12));
        profileNoteArea.setLineWrap(true);
        profileNoteArea.setWrapStyleWord(true);
        profileNoteArea.setRows(3);
        noteSection.add(profileNoteArea, BorderLayout.CENTER);
        content.add(noteSection);

        content.add(Box.createVerticalStrut(20));

        // Save Button
        JPanel btnSection = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnSection.setOpaque(false);
        btnSection.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JButton saveBtn = new JButton("Lưu thay đổi");
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveBtn.setBackground(new Color(0, 132, 255));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.setBorderPainted(false);
        saveBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        saveBtn.setPreferredSize(new Dimension(200, 38));
        saveBtn.addActionListener(e -> {
            // Save name
            String newName = profileNameField.getText().trim();
            if (!newName.isEmpty()) {
                userName = newName;
                // Update nav avatar letter
                navAvatarPanel.t = userName.substring(0, 1);
                navAvatarPanel.repaint();
                // Update profile avatar letter
                profileAvatarDisplay.t = userName.substring(0, 1);
                profileAvatarDisplay.repaint();
            }
            // Save note
            userNote = profileNoteArea.getText().trim();

            // Show confirmation
            saveBtn.setText("Đã lưu ✓");
            Timer timer = new Timer(1500, ev -> saveBtn.setText("Lưu thay đổi"));
            timer.setRepeats(false);
            timer.start();
        });
        btnSection.add(saveBtn);
        content.add(btnSection);

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void refreshProfilePanel() {
        // Update fields to current values
        profileNameField.setText(userName);
        profileNoteArea.setText(userNote);
        profileAvatarDisplay.t = userName.substring(0, 1);
        profileAvatarDisplay.c = AVATAR_COLORS[userAvatarColorIdx];
        profileAvatarDisplay.setImage(userAvatarImage);
        profileAvatarDisplay.repaint();

        // Update theme colors
        profilePanel.setBackground(COLOR_CHAT_LIST);
        profilePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, COLOR_BORDER));
        profileNameField.setBackground(COLOR_INPUT_BG);
        profileNameField.setForeground(COLOR_TEXT_PRIMARY);
        profileNameField.setCaretColor(COLOR_TEXT_PRIMARY);
        profileNoteArea.setBackground(COLOR_INPUT_BG);
        profileNoteArea.setForeground(COLOR_TEXT_PRIMARY);
        profileNoteArea.setCaretColor(COLOR_TEXT_PRIMARY);
        refreshAllLabels(profilePanel);

        // Deep update for ScrollPane's content
        Component pScroll = ((BorderLayout) profilePanel.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        if (pScroll instanceof JScrollPane) {
            Component content = ((JScrollPane) pScroll).getViewport().getView();
            if (content instanceof JPanel) {
                content.setBackground(COLOR_CHAT_LIST);
                // Cập nhật màu cho nút tải ảnh để đồng bộ dark mode
                for (Component comp : ((JPanel) content).getComponents()) {
                    if (comp instanceof JPanel) {
                        for (Component inner : ((JPanel) comp).getComponents()) {
                            if (inner instanceof JButton && !((JButton) inner).getText().contains("Lưu")) {
                                inner.setBackground(COLOR_HOVER);
                                inner.setForeground(COLOR_TEXT_PRIMARY);
                            }
                        }
                    }
                }
            }
        }
    }

    // ================= COLUMN 2: CHAT LIST =================
    private JPanel createSidebar() {
        JPanel s = new JPanel(new BorderLayout());
        s.setPreferredSize(new Dimension(340, 0));
        s.setBackground(COLOR_CHAT_LIST);
        s.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, COLOR_BORDER));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(24, 16, 12, 16));

        JLabel title = new JLabel("Đoạn chat");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(COLOR_TEXT_PRIMARY);
        header.add(title, BorderLayout.NORTH);

        searchBox = new RoundPanel(36);
        searchBox.setBackground(COLOR_INPUT_BG);
        searchBox.setLayout(new BorderLayout());
        searchBox.setBorder(new EmptyBorder(0, 14, 0, 14));
        searchBox.setPreferredSize(new Dimension(0, 36));

        JLabel searchIcon = new JLabel("🔍 ");
        searchIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        searchBox.add(searchIcon, BorderLayout.WEST);

        JTextField searchField = new JTextField("Tìm kiếm trên Messenger");
        searchField.setOpaque(false);
        searchField.setBorder(null);
        searchField.setForeground(COLOR_TEXT_SECONDARY);
        searchField.setCaretColor(COLOR_TEXT_PRIMARY);
        searchField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Tìm kiếm trên Messenger")) {
                    searchField.setText("");
                    searchField.setForeground(COLOR_TEXT_PRIMARY);
                }
            }

            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Tìm kiếm trên Messenger");
                    searchField.setForeground(COLOR_TEXT_SECONDARY);
                }
            }
        });
        searchBox.add(searchField, BorderLayout.CENTER);

        header.add(Box.createVerticalStrut(15), BorderLayout.CENTER);
        header.add(searchBox, BorderLayout.SOUTH);
        s.add(header, BorderLayout.NORTH);

        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBackground(COLOR_CHAT_LIST);

        list.add(createContact("Elon Musk", "Cứu hỏa Mars thôi em...", true, 0));
        list.add(createContact("Mark Zuckerberg", "Metaverse vẫn lỗ.", true, 1));
        list.add(createContact("Taylor Swift", "Nghe album mới chưa?", false, 2));
        list.add(createContact("Bill Gates", "Mai đi cafe bàn về AI không?", true, 3));
        list.add(createContact("Lisa", "LALISA LOVE! 💛", false, 4));
        list.add(createContact("Sơn Tùng MTP", "Bài mới sắp drop rồi!", true, 5));

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        s.add(scroll, BorderLayout.CENTER);
        return s;
    }

    private JPanel createContact(String name, String msg, boolean online, int colorIdx) {
        JPanel item = new JPanel(new BorderLayout(12, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        item.setOpaque(true);
        item.setBackground(COLOR_CHAT_LIST);
        item.setMaximumSize(new Dimension(400, 72));
        item.setPreferredSize(new Dimension(400, 72));
        item.setBorder(new EmptyBorder(10, 12, 10, 12));
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Avatar with online indicator
        JPanel avatarContainer = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getParent().getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        avatarContainer.setOpaque(false);
        avatarContainer.setPreferredSize(new Dimension(48, 48));
        avatarContainer.add(new AvatarPanel(name.substring(0, 1), 45, AVATAR_COLORS[colorIdx % AVATAR_COLORS.length]), BorderLayout.CENTER);

        if (online) {
            JPanel onlineDot = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(49, 196, 90));
                    g2.fill(new Ellipse2D.Double(0, 0, 12, 12));
                    g2.dispose();
                }
            };
            onlineDot.setOpaque(false);
            onlineDot.setPreferredSize(new Dimension(12, 12));
            avatarContainer.add(onlineDot, BorderLayout.SOUTH);
        }

        item.add(avatarContainer, BorderLayout.WEST);

        JPanel info = new JPanel(new GridLayout(2, 1));
        info.setOpaque(false);
        JLabel n = new JLabel(name);
        n.setForeground(COLOR_TEXT_PRIMARY);
        n.setFont(new Font("Segoe UI", Font.BOLD, 15));
        JLabel m = new JLabel(msg);
        m.setForeground(COLOR_TEXT_SECONDARY);
        m.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        info.add(n);
        info.add(m);
        item.add(info, BorderLayout.CENTER);

        item.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (item != selectedItem) {
                    item.setBackground(COLOR_HOVER);
                    item.repaint();
                }
            }

            public void mouseExited(MouseEvent e) {
                if (item != selectedItem) {
                    item.setBackground(COLOR_CHAT_LIST);
                    item.repaint();
                }
            }

            public void mousePressed(MouseEvent e) {
                if (selectedItem != null) {
                    selectedItem.setBackground(COLOR_CHAT_LIST);
                    selectedItem.repaint();
                }
                selectedItem = item;
                item.setBackground(COLOR_ACTIVE_ITEM);
                item.repaint();
                switchChat(name, online);
            }
        });
        return item;
    }

    // ================= COLUMN 3: CHAT AREA =================
    private JPanel createChatArea() {
        JPanel chat = new JPanel(new BorderLayout());
        chat.setBackground(COLOR_CHAT_BG);

        // Header
        chatHead = new JPanel(new BorderLayout());
        chatHead.setPreferredSize(new Dimension(0, 65));
        chatHead.setBackground(COLOR_CHAT_BG);
        chatHead.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        titlePanel.setBorder(new EmptyBorder(10, 20, 10, 0));

        currentChatTitle = new JLabel("Chọn một cuộc trò chuyện");
        currentChatTitle.setForeground(COLOR_TEXT_PRIMARY);
        currentChatTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));

        chatStatusLabel = new JLabel("");
        chatStatusLabel.setForeground(COLOR_TEXT_SECONDARY);
        chatStatusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        titlePanel.add(currentChatTitle);
        titlePanel.add(chatStatusLabel);
        chatHead.add(titlePanel, BorderLayout.WEST);

        // Header buttons (call, video, info)
        JPanel headerButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 15));
        headerButtons.setOpaque(false);
        headerButtons.add(createHeaderButton("📞"));
        headerButtons.add(createHeaderButton("📹"));
        headerButtons.add(createHeaderButton("ℹ️"));
        chatHead.add(headerButtons, BorderLayout.EAST);

        chat.add(chatHead, BorderLayout.NORTH);

        // Message Area
        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setBackground(COLOR_CHAT_BG);
        messagePanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        messageScroll = new JScrollPane(messagePanel);
        messageScroll.setBorder(null);
        messageScroll.setBackground(COLOR_CHAT_BG);
        messageScroll.getVerticalScrollBar().setUnitIncrement(16);
        chat.add(messageScroll, BorderLayout.CENTER);

        // Footer (Input Area)
        chatFooter = new JPanel(new BorderLayout(10, 0));
        chatFooter.setBackground(COLOR_CHAT_BG);
        chatFooter.setBorder(new EmptyBorder(10, 16, 10, 16));

        // Emoji + attach buttons
        JPanel leftButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        leftButtons.setOpaque(false);
        leftButtons.add(createFooterButton("➕"));
        leftButtons.add(createFooterButton("📷"));
        leftButtons.add(createFooterButton("🎤"));

        // Input field (rounded)
        inputRound = new RoundPanel(36);
        inputRound.setBackground(COLOR_INPUT_BG);
        inputRound.setLayout(new BorderLayout());
        inputRound.setBorder(new EmptyBorder(0, 16, 0, 16));
        inputRound.setPreferredSize(new Dimension(0, 38));

        inputField = new JTextField();
        inputField.setOpaque(false);
        inputField.setBorder(null);
        inputField.setForeground(COLOR_TEXT_PRIMARY);
        inputField.setCaretColor(COLOR_TEXT_PRIMARY);
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputField.addActionListener(e -> sendMessage());

        // Placeholder
        inputField.setText("Aa");
        inputField.setForeground(COLOR_TEXT_SECONDARY);
        inputField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (inputField.getText().equals("Aa")) {
                    inputField.setText("");
                    inputField.setForeground(COLOR_TEXT_PRIMARY);
                }
            }

            public void focusLost(FocusEvent e) {
                if (inputField.getText().isEmpty()) {
                    inputField.setText("Aa");
                    inputField.setForeground(COLOR_TEXT_SECONDARY);
                }
            }
        });

        inputRound.add(inputField, BorderLayout.CENTER);

        // Send + Like buttons
        JPanel rightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        rightButtons.setOpaque(false);

        JLabel sendBtn = createFooterButton("✈️");
        sendBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                sendMessage();
            }
        });
        rightButtons.add(sendBtn);

        JLabel likeBtn = createFooterButton("👍");
        likeBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (currentChatName != null) addBubble("👍", true);
            }
        });
        rightButtons.add(likeBtn);

        chatFooter.add(leftButtons, BorderLayout.WEST);
        chatFooter.add(inputRound, BorderLayout.CENTER);
        chatFooter.add(rightButtons, BorderLayout.EAST);
        chat.add(chatFooter, BorderLayout.SOUTH);

        return chat;
    }

    private JLabel createHeaderButton(String icon) {
        JLabel btn = new JLabel(icon);
        btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JLabel createFooterButton(String icon) {
        JLabel btn = new JLabel(icon);
        btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ================= CHAT LOGIC =================
    private void switchChat(String name, boolean online) {
        currentChatName = name;
        currentChatTitle.setText(name);
        chatStatusLabel.setText(online ? "● Đang hoạt động" : "Hoạt động 2 giờ trước");
        if (online) chatStatusLabel.setForeground(new Color(49, 196, 90));
        else chatStatusLabel.setForeground(COLOR_TEXT_SECONDARY);

        messagePanel.removeAll();

        // Add some welcome messages
        addBubble("Xin chào! 👋", false);
        addBubble("Bạn khỏe không?", false);

        messagePanel.revalidate();
        messagePanel.repaint();
        inputField.requestFocus();
    }

    private void sendMessage() {
        String text = inputField.getText().trim();
        if (text.isEmpty() || text.equals("Aa")) return;

        addBubble(text, true);
        inputField.setText("");
        inputField.requestFocus();

        // Auto reply after a short delay
        if (currentChatName != null && autoReplies.containsKey(currentChatName)) {
            Timer timer = new Timer(800 + (int) (Math.random() * 1200), e -> {
                String[] replies = autoReplies.get(currentChatName);
                int idx = replyIndex.getOrDefault(currentChatName, 0);
                addBubble(replies[idx % replies.length], false);
                replyIndex.put(currentChatName, idx + 1);
            });
            timer.setRepeats(false);
            timer.start();
        }
    }

    private void addBubble(String msg, boolean mine) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        wrapper.setBorder(new EmptyBorder(2, 0, 2, 0));

        // Inner panel for alignment
        JPanel alignPanel = new JPanel(new FlowLayout(mine ? FlowLayout.RIGHT : FlowLayout.LEFT, 8, 0));
        alignPanel.setOpaque(false);

        // Avatar for received messages
        if (!mine) {
            alignPanel.add(new AvatarPanel(
                currentChatName != null ? currentChatName.substring(0, 1) : "?",
                30, AVATAR_COLORS[1]
            ));
        }

        // Bubble
        RoundPanel bubble = new RoundPanel(18);
        bubble.setBackground(mine ? COLOR_MY_MSG : COLOR_OTHER_MSG);
        bubble.setLayout(new BorderLayout());
        bubble.setBorder(new EmptyBorder(8, 14, 8, 14));

        JLabel msgLabel = new JLabel("<html><body style='width: 250px;'>" + msg + "</body></html>");
        msgLabel.setForeground(mine ? Color.WHITE : COLOR_TEXT_PRIMARY);
        msgLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        bubble.add(msgLabel, BorderLayout.CENTER);

        alignPanel.add(bubble);

        wrapper.add(alignPanel, mine ? BorderLayout.EAST : BorderLayout.WEST);

        // Timestamp
        String timeStr = new SimpleDateFormat("HH:mm").format(new Date());
        JLabel timeLabel = new JLabel(timeStr);
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        timeLabel.setForeground(COLOR_TEXT_SECONDARY);
        timeLabel.setBorder(new EmptyBorder(0, mine ? 0 : 50, 0, mine ? 10 : 0));
        timeLabel.setHorizontalAlignment(mine ? SwingConstants.RIGHT : SwingConstants.LEFT);

        JPanel timeWrapper = new JPanel(new FlowLayout(mine ? FlowLayout.RIGHT : FlowLayout.LEFT));
        timeWrapper.setOpaque(false);
        timeWrapper.add(timeLabel);

        JPanel fullBubble = new JPanel();
        fullBubble.setLayout(new BoxLayout(fullBubble, BoxLayout.Y_AXIS));
        fullBubble.setOpaque(false);
        fullBubble.add(wrapper);
        fullBubble.add(timeWrapper);

        messagePanel.add(fullBubble);
        messagePanel.revalidate();

        SwingUtilities.invokeLater(() ->
            messageScroll.getVerticalScrollBar().setValue(messageScroll.getVerticalScrollBar().getMaximum())
        );
    }

    // ================= CUSTOM COMPONENTS =================
    class RoundPanel extends JPanel {
        private int r;

        public RoundPanel(int r) {
            this.r = r;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(getParent().getBackground());
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), r, r));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    class AvatarPanel extends JPanel {
        private String t;
        private int s;
        private Color c;
        private Image img;

        public AvatarPanel(String t, int s, Color c) {
            this.t = t;
            this.s = s;
            this.c = c;
            setPreferredSize(new Dimension(s, s));
            setOpaque(false);
        }

        public void setImage(Image img) {
            this.img = img;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(getParent().getBackground());
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (img != null) {
                Shape clip = new Ellipse2D.Double(0, 0, s, s);
                g2.setClip(clip);
                g2.drawImage(img, 0, 0, s, s, this);
            } else {
                g2.setColor(c);
                g2.fill(new Ellipse2D.Double(0, 0, s, s));
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, s / 2));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(t, (s - fm.stringWidth(t)) / 2, ((s - fm.getHeight()) / 2) + fm.getAscent());
            }
            g2.dispose();
        }
    }

    // ================= MAIN =================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UIJAV().setVisible(true));
    }
}
