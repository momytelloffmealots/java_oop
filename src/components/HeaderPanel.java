package components;

import utils.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HeaderPanel extends JPanel {
    public HeaderPanel(NavListener listener) {
        setBackground(Theme.HEADER_BG);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 40, 10, 40));
        setPreferredSize(new Dimension(1000, 70));

        // LEFT: Logo
        JLabel lblLogo = new JLabel("LetTruyen");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblLogo.setForeground(Color.WHITE);
        
        // Add Icon to the left of the label
        try {
            ImageIcon icon = new ImageIcon("src/images/logo_icon.png");
            if (icon.getImage() != null) {
                // Scale if necessary, assuming 40x40 is a good size
                Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                lblLogo.setIcon(new ImageIcon(img));
                lblLogo.setIconTextGap(10); // Space between icon and text
            }
        } catch (Exception e) {
            System.err.println("Note: Logo icon not found yet. Use src/images/logo_icon.png to add one.");
        }

        lblLogo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblLogo.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (listener != null)
                    listener.onNavigate("Trang chủ", null);
            }
        });

        // CENTER: Search Bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        searchPanel.setOpaque(false);
        searchPanel.setBorder(new EmptyBorder(5, 50, 0, 0));
        JTextField txtSearch = new JTextField(30);
        txtSearch.setPreferredSize(new Dimension(350, 35));
        txtSearch.setFont(Theme.FONT_REGULAR);

        JButton btnSearch = new JButton("Tìm kiếm");
        btnSearch.setBackground(new Color(0, 132, 255)); // Professional Blue
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFont(Theme.FONT_BOLD);
        btnSearch.setPreferredSize(new Dimension(100, 35));
        btnSearch.setFocusPainted(false);
        btnSearch.setBorderPainted(false);
        btnSearch.setOpaque(true);
        btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnChat = new JButton("Chat");
        btnChat.setBackground(new Color(49, 196, 90)); // Active Green
        btnChat.setForeground(Color.WHITE);
        btnChat.setFont(Theme.FONT_BOLD);
        btnChat.setPreferredSize(new Dimension(80, 35));
        btnChat.setFocusPainted(false);
        btnChat.setBorderPainted(false);
        btnChat.setOpaque(true);
        btnChat.setToolTipText("Mở khung chat");
        btnChat.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnChat.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                try {
                    main.UIJAV chatWindow = new main.UIJAV();
                    chatWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    chatWindow.setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        });

        // Action when searching
        java.awt.event.ActionListener searchAction = e -> {
            String query = txtSearch.getText().trim();
            System.out.println("HEADER_DEBUG: Search button/enter clicked. Query: [" + query + "]");
            if (!query.isEmpty() && listener != null) {
                listener.onSearch(query);
            }
        };

        btnSearch.addActionListener(searchAction);
        txtSearch.addActionListener(searchAction);

        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnChat);

        // RIGHT: Auth Buttons
        JPanel authPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        authPanel.setOpaque(false);
        authPanel.setBorder(new EmptyBorder(5, 0, 0, 0));

        JButton btnLogin = new JButton("Đăng nhập");
        btnLogin.setContentAreaFilled(false);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(Theme.FONT_REGULAR);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setBorder(null);

        JButton btnRegister = new JButton("Đăng ký");
        btnRegister.setContentAreaFilled(false);
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFont(Theme.FONT_REGULAR);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegister.setBorder(null);

        btnLogin.addActionListener(e -> {
            if (listener != null)
                listener.onNavigate("Đăng nhập", null);
        });

        btnRegister.addActionListener(e -> {
            if (listener != null)
                listener.onNavigate("Đăng ký", null);
        });

        authPanel.add(btnLogin);
        authPanel.add(btnRegister);

        add(lblLogo, BorderLayout.WEST);
        add(searchPanel, BorderLayout.CENTER);
        add(authPanel, BorderLayout.EAST);
    }
}
