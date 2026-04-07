package components;

import models.Chapter;
import models.Manga;
import utils.Theme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

public class MangaReaderPanel extends JPanel {
    private Manga manga;
    private Chapter chapter;
    private NavListener listener;

    public MangaReaderPanel(Manga manga, Chapter chapter, NavListener listener) {
        this.manga = manga;
        this.chapter = chapter;
        this.listener = listener;

        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        // TOP BAR: Navigation and info
        JPanel topBar = new JPanel(new BorderLayout(20, 0));
        topBar.setBackground(Theme.HEADER_BG);
        topBar.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel lblInfo = new JLabel(manga.getTitle() + " - " + chapter.getName());
        lblInfo.setFont(Theme.FONT_BOLD);
        lblInfo.setForeground(Color.WHITE);
        topBar.add(lblInfo, BorderLayout.WEST);

        JButton btnBack = new JButton("Quay lại");
        btnBack.setUI(new javax.swing.plaf.basic.BasicButtonUI()); // Force custom style
        btnBack.setBackground(new Color(0, 123, 255)); 
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(Theme.FONT_BOLD);
        btnBack.setFocusPainted(false);
        btnBack.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> {
            if (listener != null) listener.onMangaClick(manga);
        });
        topBar.add(btnBack, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        // MAIN CONTENT (Vertical Flow for everything)
        JPanel contentContainer = new JPanel();
        contentContainer.setLayout(new BoxLayout(contentContainer, BoxLayout.Y_AXIS));
        contentContainer.setBackground(new Color(25, 25, 25));

        // Centering Panel for images
        JPanel imagesCentering = new JPanel(new GridBagLayout());
        imagesCentering.setBackground(new Color(25, 25, 25));
        
        JPanel imagesPanel = new JPanel();
        imagesPanel.setLayout(new BoxLayout(imagesPanel, BoxLayout.Y_AXIS));
        imagesPanel.setBackground(new Color(25, 25, 25));

        String folderName = manga.getId() + "_" + chapter.getName().replace(" ", "");
        for (int i = 1; i <= 10; i++) {
            imagesPanel.add(createPageLabel(folderName, i));
            imagesPanel.add(Box.createVerticalStrut(10));
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        imagesCentering.add(imagesPanel, gbc);
        
        contentContainer.add(imagesCentering);

        // BOTTOM BAR (Added directly to contentContainer)
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 25));
        bottomBar.setBackground(new Color(25, 25, 25));
        
        JButton btnPrev = new JButton("<< Chương trước");
        JButton btnNext = new JButton("Chương sau >>");
        
        styleNavBtn(btnPrev);
        styleNavBtn(btnNext);
        
        bottomBar.add(btnPrev);
        bottomBar.add(btnNext);
        
        contentContainer.add(bottomBar);

        add(contentContainer, BorderLayout.CENTER);
    }

    private JLabel createPageLabel(String folderName, int pageNum) {
        JLabel lblImage = new JLabel("", JLabel.CENTER);
        lblImage.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        try {
            // Check for 1.jpg, 1.png, 1.webp, etc.
            String[] extensions = {".jpg", ".png", ".webp", ".jpeg"};
            File imgFile = null;
            for (String ext : extensions) {
                File f = new File("src/images/" + folderName + "/" + pageNum + ext);
                if (f.exists()) {
                    imgFile = f;
                    break;
                }
            }

            if (imgFile != null) {
                ImageIcon original = new ImageIcon(imgFile.getAbsolutePath());
                Image img = original.getImage();
                int targetWidth = 850;
                int targetHeight = (int) (img.getHeight(null) * ((double) targetWidth / img.getWidth(null)));
                
                lblImage.setIcon(new ImageIcon(img.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH)));
            } else {
                // Style placeholder if image is missing - Show clearly the path to use
                lblImage.setText("<html><center>Trang " + pageNum + "<br><font size='3' color='#888888'>(Ném file \"" + pageNum + ".jpg\" vào thư mục \"src/images/" + folderName + "/\")</font></center></html>");
                lblImage.setPreferredSize(new Dimension(850, 600)); // Shorter placeholder
                lblImage.setOpaque(true);
                lblImage.setBackground(Color.WHITE);
                lblImage.setFont(Theme.FONT_BOLD);
                lblImage.setForeground(Color.BLACK);
                lblImage.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            }
        } catch (Exception e) {
            lblImage.setText("Lỗi tải trang " + pageNum);
        }
        
        return lblImage;
    }

    private void styleNavBtn(JButton b) {
        b.setUI(new javax.swing.plaf.basic.BasicButtonUI()); // Force custom style
        b.setBackground(new Color(0, 123, 255)); 
        b.setForeground(Color.WHITE);
        b.setFont(Theme.FONT_BOLD);
        b.setPreferredSize(new Dimension(180, 40));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
