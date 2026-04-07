package components;

import utils.Theme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class ForgotPasswordPanel extends JPanel {
    private NavListener listener;
    private JPanel stepPanel;
    private CardLayout stepLayout;
    private JTextField txtPhone;
    private JTextField txtOTP;

    public ForgotPasswordPanel(NavListener listener) {
        this.listener = listener;
        setLayout(new GridBagLayout());
        setBackground(Theme.APP_BG);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(80, 0, 0, 0);

        JPanel mainBox = new JPanel();
        mainBox.setLayout(new BoxLayout(mainBox, BoxLayout.Y_AXIS));
        mainBox.setBackground(Color.WHITE);
        mainBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER_COLOR),
            new EmptyBorder(40, 50, 40, 50)
        ));
        mainBox.setPreferredSize(new Dimension(500, 450));
        mainBox.setMaximumSize(new Dimension(500, 450));

        // 1. Title
        JLabel lblTitle = new JLabel("QUÊN MẬT KHẨU");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(Theme.TEXT_MAIN);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainBox.add(lblTitle);
        mainBox.add(Box.createVerticalStrut(30));

        // 2. Steps (CardLayout)
        stepLayout = new CardLayout();
        stepPanel = new JPanel(stepLayout);
        stepPanel.setOpaque(false);
        stepPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Step 1: Input Phone
        JPanel step1 = createStep1();
        // Step 2: Input OTP
        JPanel step2 = createStep2();

        stepPanel.add(step1, "Step1");
        stepPanel.add(step2, "Step2");

        mainBox.add(stepPanel);
        
        // 3. Back to Login
        mainBox.add(Box.createVerticalStrut(20));
        JLabel lblBack = new JLabel("Quay lại Đăng nhập");
        lblBack.setFont(Theme.FONT_BOLD);
        lblBack.setForeground(Theme.TEXT_LINK);
        lblBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblBack.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblBack.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (listener != null) listener.onNavigate("Đăng nhập", null);
            }
        });
        mainBox.add(lblBack);

        add(mainBox, gbc);

        // Add a filler at the bottom to push everything up
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        add(new JPanel() {{ setOpaque(false); }}, gbc);

        // Reset state whenever navigation shows this panel
        addHierarchyListener(e -> {
            if ((e.getChangeFlags() & java.awt.event.HierarchyEvent.SHOWING_CHANGED) != 0 && isShowing()) {
                reset();
            }
        });
    }

    public void reset() {
        if (stepLayout != null && stepPanel != null) {
            stepLayout.show(stepPanel, "Step1");
            if (txtPhone != null) txtPhone.setText("");
            if (txtOTP != null) txtOTP.setText("");
        }
    }

    private JPanel createStep1() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);

        JLabel lbl = new JLabel("Nhập số điện thoại để nhận mã OTP");
        lbl.setFont(Theme.FONT_SMALL);
        lbl.setForeground(Theme.TEXT_MUTED);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(lbl);
        p.add(Box.createVerticalStrut(15));

        txtPhone = new JTextField();
        styleField(txtPhone);
        p.add(txtPhone);
        p.add(Box.createVerticalStrut(20));

        JButton btnNext = createButton("TIẾP TỤC");
        btnNext.addActionListener(e -> {
            if (txtPhone.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            stepLayout.show(stepPanel, "Step2");
        });
        p.add(btnNext);

        return p;
    }

    private JPanel createStep2() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);

        JLabel lbl = new JLabel("Nhập mã OTP đã được gửi đến thiết bị");
        lbl.setFont(Theme.FONT_SMALL);
        lbl.setForeground(Theme.TEXT_MUTED);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(lbl);
        p.add(Box.createVerticalStrut(15));

        txtOTP = new JTextField();
        styleField(txtOTP);
        p.add(txtOTP);
        p.add(Box.createVerticalStrut(20));

        JButton btnVerify = createButton("XÁC THỰC");
        btnVerify.addActionListener(e -> {
            if (txtOTP.getText().trim().equals("3636")) {
                JOptionPane.showMessageDialog(this, "Xác thực thành công! Mật khẩu mới đã gửi về máy.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                if (listener != null) listener.onNavigate("Đăng nhập", null);
            } else {
                JOptionPane.showMessageDialog(this, "Mã OTP không đúng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        p.add(btnVerify);

        return p;
    }

    private void styleField(JTextField f) {
        f.setFont(Theme.FONT_BOLD); // Make OTP big
        f.setHorizontalAlignment(JTextField.CENTER);
        f.setPreferredSize(new Dimension(300, 45));
        f.setMaximumSize(new Dimension(300, 45));
        f.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(1, 1, 1, 1, Theme.BORDER_COLOR),
            new EmptyBorder(0, 10, 0, 10)
        ));
    }

    private JButton createButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(Theme.ACCENT_ORANGE);
        b.setForeground(Color.WHITE);
        b.setFont(Theme.FONT_BOLD);
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setMaximumSize(new Dimension(300, 45));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }
}
