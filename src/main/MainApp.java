package main;

import components.HeaderPanel;
import components.MangaGridPanel;
import components.NavBar;
import components.NavListener;
import components.TopRankingPanel;
import utils.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainApp implements NavListener {

    private JFrame frame;
    private JPanel cardsPanel;
    private CardLayout cardLayout;
    private TopRankingPanel rightPanel;
    private JPanel rightWrapper;
    private List<models.Manga> followedMangas = new ArrayList<>(); // Kho lưu trữ truyện theo dõi
    private List<models.Manga> historyMangas = new ArrayList<>(); // Kho lưu trữ lịch sử
    private List<models.Manga> masterMangaList; // Danh sách tổng hợp để xếp hạng
    private JPanel mainContentPanel;

    public MainApp() {
        masterMangaList = generateMasterList();
        initialize();
    }

    // Removed duplicate empty generateMasterList method.

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("NetTruyen - Đọc Truyện Tranh Online");
        frame.setBounds(100, 100, 1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Theme.APP_BG);
        frame.setLayout(new BorderLayout());

        // TOP AREA: Header + NavBar overlay
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
        topContainer.add(new HeaderPanel(this));
        topContainer.add(new NavBar(this));

        frame.add(topContainer, BorderLayout.NORTH);

        // MAIN CONTENT SCROLL AREA
        mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new GridBagLayout());
        mainContentPanel.setBackground(Theme.APP_BG);
        mainContentPanel.setBorder(new EmptyBorder(10, 20, 20, 20));

        // LEFT: CardLayout Panel for dynamic routing
        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);
        cardsPanel.setBackground(Theme.APP_BG);

        // -- Setup Pages --
        // 1. Home
        cardsPanel.add(new MangaGridPanel(this, "TRUYỆN MỚI CẬP NHẬT", false, true, null), "Trang chủ");

        // 2. Hot
        cardsPanel.add(new MangaGridPanel(this, "TRUYỆN HOT NHẤT", true, false, null), "HOT");

        // 3. History
        MangaGridPanel historyGrid = new MangaGridPanel(this, "LỊCH SỬ ĐỌC TRUYỆN", true, false, null);
        historyGrid.setName("Lịch sử");
        cardsPanel.add(historyGrid, "Lịch sử");

        // 4. Follow
        MangaGridPanel followGrid = new MangaGridPanel(this, "TRUYỆN ĐANG THEO DÕI", false, false, "Bạn chưa theo dõi truyện nào cả");
        followGrid.setName("Theo dõi");
        cardsPanel.add(followGrid, "Theo dõi");

        // 5. Gender filters
        cardsPanel.add(new MangaGridPanel(this, "TRUYỆN CHO CON GÁI", true, false, null), "Con gái");
        cardsPanel.add(new MangaGridPanel(this, "TRUYỆN CHO CON TRAI", true, false, null), "Con trai");

        // 6. Generic Default page for unhandled like "Xếp hạng", "Tìm truyện" etc
        cardsPanel.add(new MangaGridPanel(this, "hey guy ", false, false, "thanh tìm kiếm ở trên kìa "), "Default");

        // 7. Login & Register Pages
        cardsPanel.add(new components.LoginPanel(this), "Đăng nhập");
        cardsPanel.add(new components.RegisterPanel(this), "Đăng ký");
        cardsPanel.add(new components.ForgotPasswordPanel(this), "Quên mật khẩu");

        // RIGHT: Top Ranking SideBar
        rightPanel = new TopRankingPanel(new ArrayList<>(masterMangaList), this);

        // ADD to Main Content Panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weighty = 1.0;

        // 1. Cards Container (Left)
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(0, 0, 0, 30); // 30px gap
        JPanel cardsWrapper = new JPanel(new BorderLayout());
        cardsWrapper.setOpaque(false);
        cardsWrapper.add(cardsPanel, BorderLayout.NORTH);
        mainContentPanel.add(cardsWrapper, gbc);

        // 2. Ranking Container (Right)
        gbc.gridx = 1;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(0, 0, 0, 0);
        rightWrapper = new JPanel(new BorderLayout());
        rightWrapper.setOpaque(false);
        rightWrapper.add(rightPanel, BorderLayout.NORTH);
        mainContentPanel.add(rightWrapper, gbc);

        // 3. Glue removed to allow expansion
        // mainContentPanel.add(Box.createGlue(), gbc);

        // Scroll Pane
        JScrollPane scrollPane = new JScrollPane(mainContentPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // smooth scroll
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null); // remove border

        frame.add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void onNavigate(String page, String detail) {
        if (page.equals("Thể loại") && detail != null) {
            String cardId = "TheLoai_" + detail;
            // Iterate and remove previous instance if exists to ensure reshuffling
            for (Component comp : cardsPanel.getComponents()) {
                if (cardId.equals(comp.getName())) {
                    cardsPanel.remove(comp);
                }
            }
            MangaGridPanel panel = new MangaGridPanel(this, "THỂ LOẠI: " + detail.toUpperCase(), true, false, null);
            panel.setName(cardId);
            cardsPanel.add(panel, cardId);
            cardsPanel.revalidate();
            cardsPanel.repaint();
            cardLayout.show(cardsPanel, cardId);
            return;
        }

        if (page.equals("Xếp hạng") && detail != null) {
            String cardId = "XepHang_" + detail;
            // Iterate and remove previous instance if exists to ensure reshuffling
            for (Component comp : cardsPanel.getComponents()) {
                if (cardId.equals(comp.getName())) {
                    cardsPanel.remove(comp);
                }
            }
            MangaGridPanel panel = new MangaGridPanel(this, "XẾP HẠNG: " + detail.toUpperCase(), true, false, null);
            panel.setName(cardId);
            cardsPanel.add(panel, cardId);
            cardsPanel.revalidate();
            cardsPanel.repaint();
            cardLayout.show(cardsPanel, cardId);
            return;
        }

        // Ensure the card exists, otherwise show default placeholder
        String[] supportedPages = { "Trang chủ", "HOT", "Lịch sử", "Theo dõi", "Con gái", "Con trai", "Xếp hạng",
                "Đăng nhập", "Đăng ký", "Quên mật khẩu" };
        boolean found = false;
        for (String p : supportedPages) {
            if (p.equals(page)) {
                found = true;
                break;
            }
        }

        updateSidebarVisibility(page);

        if (found) {
            if (page.equals("Theo dõi")) {
                refreshFollowPage();
            } else if (page.equals("Lịch sử")) {
                refreshHistoryPage();
            }
            cardLayout.show(cardsPanel, page);
        } else {
            cardLayout.show(cardsPanel, "Default");
        }

        resetMainScroll();
    }

    private void resetMainScroll() {
        JScrollPane scroll = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, cardsPanel);
        if (scroll != null) {
            SwingUtilities.invokeLater(() -> scroll.getVerticalScrollBar().setValue(0));
        }
    }

    private void updateSidebarVisibility(String cardId) {
        if (cardId == null)
            return;

        GridBagLayout layout = (GridBagLayout) mainContentPanel.getLayout();
        GridBagConstraints gbcCards = layout.getConstraints(cardsPanel.getParent()); // cardsWrapper

        // Hide sidebar for login/register/forgot/reader pages
        if (cardId.equals("Đăng nhập") || cardId.equals("Đăng ký") || cardId.equals("Quên mật khẩu")
                || cardId.startsWith("Reader_")) {
            rightPanel.setVisible(false);
            if(rightWrapper != null) rightWrapper.setVisible(false);
            
            // Allow the main content to fill and center
            gbcCards.weightx = 1.0;
            gbcCards.fill = GridBagConstraints.BOTH; // Quan trọng: Chiếm hết chỗ theo chiều ngang
            gbcCards.anchor = GridBagConstraints.CENTER;
            gbcCards.insets = new Insets(0, 0, 0, 0);
            gbcCards.gridwidth = 2; // Chiếm toàn bộ 2 cột để không bị dồn
        } else {
            rightPanel.setVisible(true);
            if(rightWrapper != null) rightWrapper.setVisible(true);
            
            // Allow main content to fill space even with sidebar
            gbcCards.weightx = 1.0;
            gbcCards.fill = GridBagConstraints.BOTH; // Quan trọng: Chiếm hết chỗ
            gbcCards.anchor = GridBagConstraints.NORTH;
            gbcCards.insets = new Insets(0, 0, 0, 30);
            gbcCards.gridwidth = 1; // Trả về 1 cột
        }
        
        layout.setConstraints(cardsPanel.getParent(), gbcCards);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    // -- LOGIC THEO DÕI --
    @Override
    public void onToggleFollow(models.Manga manga) {
        boolean removed = followedMangas.removeIf(m -> m.getId().equals(manga.getId()));
        if (!removed) {
            followedMangas.add(manga);
        }
    }

    @Override
    public boolean isMangaFollowed(String mangaId) {
        return followedMangas.stream().anyMatch(m -> m.getId().equals(mangaId));
    }

    private void addToHistory(models.Manga manga) {
        // Nếu đã có trong lịch sử thì xóa cái cũ đi để đưa lên đầu
        historyMangas.removeIf(m -> m.getId().equals(manga.getId()));
        historyMangas.add(0, manga); // Luôn thêm vào vị trí đầu tiên
        
        // Giới hạn lịch sử khoảng 20 bộ cho nhẹ máy
        if (historyMangas.size() > 20) {
            historyMangas.remove(historyMangas.size() - 1);
        }
    }

    private void refreshFollowPage() {
        // Xóa trang cũ và tạo mới với dữ liệu mới nhất
        for (Component comp : cardsPanel.getComponents()) {
            if ("Theo dõi".equals(comp.getName())) {
                cardsPanel.remove(comp);
                break;
            }
        }
        
        MangaGridPanel followGrid;
        if (followedMangas.isEmpty()) {
            followGrid = new MangaGridPanel(this, "TRUYỆN ĐANG THEO DÕI", false, false, "Bạn chưa theo dõi bộ truyện nào!");
        } else {
            followGrid = new MangaGridPanel(this, "TRUYỆN ĐANG THEO DÕI", false, false, null, followedMangas);
        }
        followGrid.setName("Theo dõi");
        cardsPanel.add(followGrid, "Theo dõi");
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private void refreshHistoryPage() {
        // Xóa trang cũ và tạo mới với dữ liệu lịch sử mới nhất
        for (Component comp : cardsPanel.getComponents()) {
            if ("Lịch sử".equals(comp.getName())) {
                cardsPanel.remove(comp);
                break;
            }
        }
        
        MangaGridPanel historyGrid;
        if (historyMangas.isEmpty()) {
            historyGrid = new MangaGridPanel(this, "LỊCH SỬ ĐỌC TRUYỆN", true, false, "Bạn chưa xem bộ truyện nào!");
        } else {
            historyGrid = new MangaGridPanel(this, "LỊCH SỬ ĐỌC TRUYỆN", true, false, null, historyMangas);
        }
        historyGrid.setName("Lịch sử");
        cardsPanel.add(historyGrid, "Lịch sử");
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private List<models.Manga> generateMasterList() {
        List<models.Manga> list = new ArrayList<>();
        String[] titles = {
            "Võ Luyện Đỉnh Phong", "Bách Luyện Thành Thần", "Dị Tộc Trùng Sinh", "Nguyên Tôn",
            "Đại Quản Gia Là Ma Hoàng", "Phàm Nhân Tu Tiên", "Ta Là Tà Đế", "Toàn Chức Pháp Sư",
            "Đấu Phá Thương Khung", "Đấu La Đại Lục", "Yêu Thần Ký", "Tuyệt Thế Đường Môn",
            "Thần Đạo Đan Tôn", "Vạn Cổ Thần Đế", "Kiếm Đạo Độc Tôn", "Tinh Thần Biến",
            "Thôn Phệ Tinh Không", "Vũ Động Càn Khôn", "Thế Giới Hoàn Mỹ", "Tuyết Ưng Lĩnh Chủ"
        };
        
        for (int i = 0; i < titles.length; i++) {
            models.Manga m = new models.Manga(String.valueOf(i), titles[i], "cover_" + ((i % 16) + 1) + ".png");
            // Random views to make ranking interesting
            m.setViews(100000 + (new java.util.Random().nextInt(900000)));
            list.add(m);
        }
        return list;
    }

    @Override
    public void onMangaClick(models.Manga manga) {
        // Ghi vào lịch sử trước khi mở trang chi tiết
        addToHistory(manga);

        String cardId = "Detail_" + manga.getId();
        // Remove existing detail panel if any
        for (Component comp : cardsPanel.getComponents()) {
            if (comp.getName() != null && comp.getName().startsWith("Detail_")) {
                cardsPanel.remove(comp);
            }
        }
        components.MangaDetailPanel detailPanel = new components.MangaDetailPanel(manga, this);
        detailPanel.setName(cardId);
        cardsPanel.add(detailPanel, cardId);
        cardsPanel.revalidate();
        cardsPanel.repaint();

        updateSidebarVisibility(cardId);
        cardLayout.show(cardsPanel, cardId);

        resetMainScroll();
    }

    @Override
    public void onReadManga(models.Manga manga, models.Chapter chapter) {
        String cardId = "Reader_" + manga.getId() + "_" + chapter.getName().replace(" ", "");

        // Remove existing reader panel if any (only keep one active session)
        for (Component comp : cardsPanel.getComponents()) {
            if (comp.getName() != null && comp.getName().startsWith("Reader_")) {
                cardsPanel.remove(comp);
            }
        }

        components.MangaReaderPanel readerPanel = new components.MangaReaderPanel(manga, chapter, this);
        readerPanel.setName(cardId);
        cardsPanel.add(readerPanel, cardId);
        cardsPanel.revalidate();
        cardsPanel.repaint();

        updateSidebarVisibility(cardId);
        cardLayout.show(cardsPanel, cardId);

        resetMainScroll();
    }

    @Override
    public void onSearch(String query) {
        System.out.println(">>> NAVIGATING TO SEARCH RESULTS for: " + query);
        String cardId = "Search_Results";

        // Master list for demo - Includes common NetTruyen titles
        String[] titles = {
                "Võ Luyện Đỉnh Phong", "Bách Luyện Thành Thần", "Dị Tộc Trùng Sinh", "Nguyên Tôn",
                "Đại Quản Gia Là Ma Hoàng", "Phàm Nhân Tu Tiên", "Ta Là Tà Đế", "Toàn Chức Pháp Sư",
                "Đấu Phá Thương Khung", "Đấu La Đại Lục", "Yêu Thần Ký", "Tuyệt Thế Đường Môn",
                "Thần Đạo Đan Tôn", "Vạn Cổ Thần Đế", "Kiếm Đạo Độc Tôn", "Tinh Thần Biến",
                "Thôn Phệ Tinh Không", "Vũ Động Càn Khôn", "Thế Giới Hoàn Mỹ", "Tuyết Ưng Lĩnh Chủ"
        };

        java.util.List<models.Manga> results = new java.util.ArrayList<>();
        String lowerQuery = query.toLowerCase().trim();

        for (int i = 0; i < titles.length; i++) {
            if (titles[i].toLowerCase().contains(lowerQuery)) {
                models.Manga m = new models.Manga(String.valueOf(i), titles[i], "cover_" + ((i % 16) + 1) + ".png");
                m.setAuthor("Manga Author " + (i + 1));
                m.setViews(1000000 + (i * 5000));
                m.setChapters(java.util.Arrays.asList(new models.Chapter("Chap " + (100 + i), "Mới đây", 500)));
                results.add(m);
            }
        }

        // Fresh panel
        MangaGridPanel resultPanel;
        if (results.isEmpty()) {
            System.out.println(">>> NO RESULTS FOUND for: " + query);
            resultPanel = new MangaGridPanel(this, "TÌM KIẾM: " + query, false, false,
                    "Không tìm thấy bộ truyện nào khớp với: \"" + query + "\"");
        } else {
            System.out.println(">>> FOUND " + results.size() + " results for: " + query);
            resultPanel = new MangaGridPanel(this, "KẾT QUẢ TÌM KIẾM: " + query, false, false, null, results);
        }

        // Remove old if any
        for (Component c : cardsPanel.getComponents()) {
            if (cardId.equals(c.getName())) {
                cardsPanel.remove(c);
            }
        }

        resultPanel.setName(cardId);
        cardsPanel.add(resultPanel, cardId);
        cardsPanel.revalidate();
        cardsPanel.repaint();

        // Reset scroll
        JScrollPane scroll = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, cardsPanel);
        if (scroll != null) {
            SwingUtilities.invokeLater(() -> scroll.getVerticalScrollBar().setValue(0));
        }

        cardLayout.show(cardsPanel, cardId);
    }

    public static void main(String[] args) {
        // Set Look and Feel to System
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            try {
                MainApp window = new MainApp();
                window.frame.setLocationRelativeTo(null); // Center on screen
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
