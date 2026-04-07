package components;

import utils.Theme;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NavBar extends JPanel {
    private NavListener listener;

    public NavBar(NavListener listener) {
        this.listener = listener;
        setBackground(Theme.NAV_BG);
        setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));
        setBorder(new MatteBorder(0, 0, 1, 0, Theme.BORDER_COLOR));

        String[] menuItems = {
            "Trang chủ", "HOT", "Theo dõi", "Lịch sử", "Thể loại", "Xếp hạng", "Tìm truyện", "Con gái", "Con trai"
        };

        for (String item : menuItems) {
            JLabel lblItem = new JLabel(item.toUpperCase());
            lblItem.setFont(Theme.FONT_BOLD);
            if (item.equals("HOT")) {
                lblItem.setForeground(Theme.ACCENT_RED);
            } else {
                lblItem.setForeground(Theme.TEXT_MAIN);
            }
            lblItem.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Create dropdown menu for "Thể loại"
            JPopupMenu categoryMenu = null;
            if (item.equals("Thể loại")) {
                categoryMenu = createCategoryMenu();
            }

            // Create dropdown menu for "Xếp hạng"
            JPopupMenu rankingMenu = null;
            if (item.equals("Xếp hạng")) {
                rankingMenu = createRankingMenu();
            }

            final JPopupMenu finalMenu = (categoryMenu != null) ? categoryMenu : rankingMenu;

            // Hover effect and click
            lblItem.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (listener != null) {
                        listener.onNavigate(item, null);
                    }
                }
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!item.equals("HOT")) {
                        lblItem.setForeground(Theme.ACCENT_ORANGE);
                    }
                    
                    // Show dropdown if it exists
                    if (finalMenu != null) {
                        finalMenu.show(lblItem, 0, lblItem.getHeight());
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (!item.equals("HOT")) {
                        lblItem.setForeground(Theme.TEXT_MAIN);
                    }
                    
                    if (finalMenu != null) {
                        // Delay check to see if cursor moved to the menu
                        Timer timer = new Timer(200, evt -> {
                            if (finalMenu.isVisible()) {
                                try {
                                    Point p = MouseInfo.getPointerInfo().getLocation();
                                    Rectangle menuBounds = finalMenu.getBounds();
                                    menuBounds.setLocation(finalMenu.getLocationOnScreen());
                                    Rectangle labelBounds = lblItem.getBounds();
                                    labelBounds.setLocation(lblItem.getLocationOnScreen());
                                    
                                    if (!menuBounds.contains(p) && !labelBounds.contains(p)) {
                                        finalMenu.setVisible(false);
                                    }
                                } catch (Exception ex) {
                                    // Handle cases where component is disposed
                                }
                            }
                        });
                        timer.setRepeats(false);
                        timer.start();
                    }
                }
            });

            add(lblItem);
        }
    }

    private JPopupMenu createCategoryMenu() {
        JPopupMenu menu = new JPopupMenu();
        menu.setLayout(new GridLayout(0, 4, 10, 10)); // 4 columns
        menu.setBackground(Theme.NAV_BG);
        menu.setBorder(new javax.swing.border.LineBorder(Theme.BORDER_COLOR, 1));

        String[] categories = {
            "Action", "Adventure", "Chuyển Sinh", "Cổ Đại", 
            "Comedy", "Comic", "Đam Mỹ", "Drama", 
            "Fantasy", "Harem", "Historical", "Horror", 
            "Josei", "Manga", "Manhua", "Manhwa", 
            "Martial Arts", "Ngôn Tình", "Romance", "School Life", 
            "Sci-fi", "Seinen", "Shoujo", "Shounen", 
            "Slice of Life", "Sports", "Supernatural", "Tragedy"
        };

        for (String cat : categories) {
            JMenuItem menuItem = new JMenuItem(cat);
            menuItem.setBackground(Theme.NAV_BG);
            menuItem.setForeground(Theme.TEXT_MAIN);
            menuItem.setFont(Theme.FONT_REGULAR);
            menuItem.setBorder(new javax.swing.border.EmptyBorder(5, 10, 5, 10));
            menuItem.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            menuItem.addActionListener(e -> {
                if (listener != null) {
                    listener.onNavigate("Thể loại", cat);
                }
            });

            menuItem.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    menuItem.setForeground(Theme.ACCENT_ORANGE);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    menuItem.setForeground(Theme.TEXT_MAIN);
                }
            });

            menu.add(menuItem);
        }
        return menu;
    }

    private JPopupMenu createRankingMenu() {
        JPopupMenu menu = new JPopupMenu();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS)); 
        menu.setBackground(Theme.NAV_BG);
        menu.setBorder(new javax.swing.border.LineBorder(Theme.BORDER_COLOR, 1));

        String[] options = {
            "Ngày", "Tháng", "Năm", "Quý", "Tất cả"
        };

        for (String opt : options) {
            JMenuItem menuItem = new JMenuItem(opt);
            menuItem.setBackground(Theme.NAV_BG);
            menuItem.setForeground(Theme.TEXT_MAIN);
            menuItem.setFont(Theme.FONT_REGULAR);
            menuItem.setBorder(new javax.swing.border.EmptyBorder(8, 20, 8, 50));
            menuItem.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            menuItem.addActionListener(e -> {
                if (listener != null) {
                    listener.onNavigate("Xếp hạng", opt);
                }
            });

            menuItem.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    menuItem.setForeground(Theme.ACCENT_ORANGE);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    menuItem.setForeground(Theme.TEXT_MAIN);
                }
            });

            menu.add(menuItem);
        }
        return menu;
    }
}
