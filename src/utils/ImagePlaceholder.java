package utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePlaceholder extends JPanel {
    private Image image;
    private String filename;
    private String altText;
    private int arcWidth = 0;
    private int arcHeight = 0;

    public ImagePlaceholder(String filename, String altText, int width, int height) {
        this.filename = filename;
        this.altText = altText;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.decode("#dcdcdc")); // Placeholder background
        loadImage();
    }

    public void setRoundedCorners(int arc) {
        this.arcWidth = arc;
        this.arcHeight = arc;
    }

    private void loadImage() {
        try {
            File imgFile = new File("src/images/" + filename);
            if (imgFile.exists()) {
                image = ImageIO.read(imgFile);
            }
        } catch (Exception e) {
            System.err.println("Could not load image: " + filename);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // Draw background placeholder
        if (arcWidth > 0 && arcHeight > 0) {
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, w, h, arcWidth, arcHeight);
        } else {
            g2.setColor(getBackground());
            g2.fillRect(0, 0, w, h);
        }

        if (image != null) {
            int imgW = image.getWidth(null);
            int imgH = image.getHeight(null);
            
            if (imgW > 0 && imgH > 0) {
                double targetRatio = (double) w / h;
                double imageRatio = (double) imgW / imgH;
                
                int drawW, drawH, drawX, drawY;
                
                if (imageRatio > targetRatio) {
                    // Image is wider than component, scale by height
                    drawH = h;
                    drawW = (int) (h * imageRatio);
                    drawX = (w - drawW) / 2;
                    drawY = 0;
                } else {
                    // Image is taller than component, scale by width
                    drawW = w;
                    drawH = (int) (w / imageRatio);
                    drawX = 0;
                    drawY = (h - drawH) / 2;
                }
                
                g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, w, h, arcWidth, arcHeight));
                g2.drawImage(image, drawX, drawY, drawW, drawH, this);
            }
        } else {
            // Draw placeholder text
            g2.setColor(Color.DARK_GRAY);
            g2.setFont(Theme.FONT_SMALL);
            FontMetrics metrics = g2.getFontMetrics();
            int x = (w - metrics.stringWidth("No Image")) / 2;
            int y = (h - metrics.getHeight()) / 2 + metrics.getAscent();
            g2.drawString("No Image", x, y);
            
            int yAlt = y + metrics.getHeight();
            int xAlt = (w - metrics.stringWidth(altText)) / 2;
            if(xAlt < 0) xAlt = 5;
            // only draw if there's enough space
            if (yAlt + metrics.getDescent() < h) {
                g2.drawString(altText, xAlt, yAlt);
            }
        }
        g2.dispose();
    }
}
