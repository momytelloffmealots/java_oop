package components;

import utils.Theme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class RegisterPanel extends JPanel {
    private NavListener listener;

    public RegisterPanel(NavListener listener) {
        this.listener = listener;
        setLayout(new GridBagLayout());
        setBackground(Theme.APP_BG);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(40, 0, 0, 0); 

        JPanel registerBox = new JPanel();
        registerBox.setLayout(new BoxLayout(registerBox, BoxLayout.Y_AXIS));
        registerBox.setBackground(Color.WHITE);
        registerBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER_COLOR),
            new EmptyBorder(30, 50, 30, 50)
        ));
        registerBox.setPreferredSize(new Dimension(500, 650));
        registerBox.setMaximumSize(new Dimension(500, 650));

        // 1. Title
        JLabel lblTitle = new JLabel("ĐĂNG KÝ");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(Theme.TEXT_MAIN);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerBox.add(lblTitle);
        registerBox.add(Box.createVerticalStrut(20));

        // 2. Form Fields
        registerBox.add(createLabel("Họ và tên"));
        JTextField txtFullname = new JTextField();
        styleField(txtFullname);
        registerBox.add(txtFullname);
        registerBox.add(Box.createVerticalStrut(15));

        registerBox.add(createLabel("Số điện thoại / Email"));
        JTextField txtUser = new JTextField();
        styleField(txtUser);
        registerBox.add(txtUser);
        registerBox.add(Box.createVerticalStrut(15));

        registerBox.add(createLabel("Mật khẩu"));
        JPasswordField txtPass = new JPasswordField();
        styleField(txtPass);
        registerBox.add(txtPass);
        registerBox.add(Box.createVerticalStrut(15));

        registerBox.add(createLabel("Xác nhận mật khẩu"));
        JPasswordField txtConfirmPass = new JPasswordField();
        styleField(txtConfirmPass);
        registerBox.add(txtConfirmPass);
        registerBox.add(Box.createVerticalStrut(20));

        // 3. Register Button
        JButton btnRegister = new JButton("ĐĂNG KÝ NGAY");
        btnRegister.setBackground(Theme.ACCENT_ORANGE);
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFont(Theme.FONT_BOLD);
        btnRegister.setFocusPainted(false);
        btnRegister.setBorderPainted(false);
        btnRegister.setOpaque(true);
        btnRegister.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegister.setMaximumSize(new Dimension(400, 45));
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnRegister.addActionListener(e -> {
            String name = txtFullname.getText().trim();
            String user = txtUser.getText().trim();
            String pass = new String(txtPass.getPassword());
            String confirm = new String(txtConfirmPass.getPassword());
            
            if (name.isEmpty() || user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!pass.equals(confirm)) {
                JOptionPane.showMessageDialog(this, "Mật khẩu xác nhận không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            JOptionPane.showMessageDialog(this, "Đăng ký thành công! Vui lòng đăng nhập.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            if (this.listener != null) this.listener.onNavigate("Đăng nhập", null);
        });
        
        registerBox.add(btnRegister);
        registerBox.add(Box.createVerticalStrut(25));

        // 4. Agreement
        JLabel lblAgree = new JLabel("<html><center>Bằng cách nhấn đăng ký, bạn đồng ý với các<br>Điều khoản & Quy định của chúng tôi.</center></html>", JLabel.CENTER);
        lblAgree.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblAgree.setForeground(Theme.TEXT_MUTED);
        lblAgree.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerBox.add(lblAgree);
        registerBox.add(Box.createVerticalStrut(25));

        // 5. Back to Login link
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        loginPanel.setBackground(Color.WHITE);
        loginPanel.add(new JLabel("Đã có tài khoản?"));
        JLabel lblLogin = new JLabel("Đăng nhập tại đây");
        lblLogin.setFont(Theme.FONT_BOLD);
        lblLogin.setForeground(Theme.TEXT_LINK);
        lblLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        lblLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (listener != null) listener.onNavigate("Đăng nhập", null);
            }
        });
        
        loginPanel.add(lblLogin);
        registerBox.add(loginPanel);

        add(registerBox, gbc);

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
}
