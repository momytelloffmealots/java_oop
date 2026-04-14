package components;

import utils.Theme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class LoginPanel extends JPanel {
    private NavListener listener;

    public LoginPanel(NavListener listener) {
        this.listener = listener;
        setLayout(new GridBagLayout());
        setBackground(Theme.APP_BG);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(40, 0, 0, 0); 

        JPanel loginBox = new JPanel();
        loginBox.setLayout(new BoxLayout(loginBox, BoxLayout.Y_AXIS));
        loginBox.setBackground(Color.WHITE);
        loginBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER_COLOR),
            new EmptyBorder(40, 50, 40, 50)
        ));
        loginBox.setPreferredSize(new Dimension(500, 550));
        loginBox.setMaximumSize(new Dimension(500, 550));

        // 1. Title
        JLabel lblTitle = new JLabel("ĐĂNG NHẬP");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(Theme.TEXT_MAIN);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBox.add(lblTitle);
        loginBox.add(Box.createVerticalStrut(30));

        // 2. Form Fields
        loginBox.add(createLabel("Số điện thoại / Email"));
        JTextField txtUser = new JTextField();
        styleField(txtUser);
        loginBox.add(txtUser);
        loginBox.add(Box.createVerticalStrut(20));

        loginBox.add(createLabel("Mật khẩu"));
        JPasswordField txtPass = new JPasswordField();
        styleField(txtPass);
        loginBox.add(txtPass);
        loginBox.add(Box.createVerticalStrut(15));

        // 3. Checkbox & Forgot link
        JPanel extraPanel = new JPanel(new BorderLayout());
        extraPanel.setBackground(Color.WHITE);
        extraPanel.setMaximumSize(new Dimension(400, 30));
        
        JCheckBox chkRemember = new JCheckBox("Ghi nhớ mật khẩu");
        chkRemember.setBackground(Color.WHITE);
        chkRemember.setFont(Theme.FONT_SMALL);
        
        JLabel lblForgot = new JLabel("Quên mật khẩu?");
        lblForgot.setFont(Theme.FONT_SMALL);
        lblForgot.setForeground(Theme.TEXT_LINK);
        lblForgot.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        lblForgot.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (listener != null) listener.onNavigate("Quên mật khẩu", null);
            }
        });
        
        extraPanel.add(chkRemember, BorderLayout.WEST);
        extraPanel.add(lblForgot, BorderLayout.EAST);
        loginBox.add(extraPanel);
        loginBox.add(Box.createVerticalStrut(30));

        // 4. Login Button
        JButton btnLogin = new JButton("ĐĂNG NHẬP");
        btnLogin.setBackground(Theme.ACCENT_ORANGE);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(Theme.FONT_BOLD);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setOpaque(true);
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(400, 45));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnLogin.addActionListener(e -> {
            String user = txtUser.getText().trim();
            String pass = new String(txtPass.getPassword());
            
            if (user.equals("admin") && pass.equals("123")) {
                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), "Đăng nhập thành công! Chào mừng admin.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                if (this.listener != null) this.listener.onNavigate("Trang chủ", null);
            } else {
                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), "Tài khoản hoặc mật khẩu không đúng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtPass.setText("");
            }
        });
        
        loginBox.add(btnLogin);
        loginBox.add(Box.createVerticalStrut(25));

        // 5. Social Login
        JLabel lblOr = new JLabel("Hoặc đăng nhập bằng");
        lblOr.setFont(Theme.FONT_SMALL);
        lblOr.setForeground(Theme.TEXT_MUTED);
        lblOr.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBox.add(lblOr);
        loginBox.add(Box.createVerticalStrut(15));

        JPanel socialPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        socialPanel.setBackground(Color.WHITE);
        socialPanel.add(createSocialBtn("Google", new Color(219, 68, 55)));
        socialPanel.add(createSocialBtn("Facebook", new Color(66, 103, 178)));
        loginBox.add(socialPanel);
        loginBox.add(Box.createVerticalStrut(30));

        // 6. Sign up link
        JPanel signupPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        signupPanel.setBackground(Color.WHITE);
        signupPanel.add(new JLabel("Chưa có tài khoản?"));
        JLabel lblSignup = new JLabel("Đăng ký ngay");
        lblSignup.setFont(Theme.FONT_BOLD);
        lblSignup.setForeground(Theme.TEXT_LINK);
        lblSignup.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        lblSignup.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (listener != null) listener.onNavigate("Đăng ký", null);
            }
        });
        
        signupPanel.add(lblSignup);
        loginBox.add(signupPanel);

        add(loginBox, gbc);

        // Add a filler at the bottom to push everything up
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        add(new JPanel() {{ setOpaque(false); }}, gbc);
    }

    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.FONT_SMALL);
        l.setForeground(Theme.TEXT_MUTED);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        l.setPreferredSize(new Dimension(400, 20));
        l.setMaximumSize(new Dimension(400, 20));
        return l;
    }

    private void styleField(JTextField f) {
        f.setFont(Theme.FONT_REGULAR);
        f.setPreferredSize(new Dimension(400, 40));
        f.setMaximumSize(new Dimension(400, 40));
        f.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(1, 1, 1, 1, Theme.BORDER_COLOR),
            new EmptyBorder(0, 10, 0, 10)
        ));
    }

    private JButton createSocialBtn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(Theme.FONT_SMALL);
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setPreferredSize(new Dimension(120, 35));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }
}
