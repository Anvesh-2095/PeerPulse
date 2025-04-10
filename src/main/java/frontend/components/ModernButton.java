package frontend.components;

import frontend.theme.ModernTheme;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ModernButton extends JButton {
    private final Color defaultBackground;
    private final Color hoverBackground;
    
    public ModernButton(String text, Color bgColor, Color hoverColor) {
        super(text);
        this.defaultBackground = bgColor;
        this.hoverBackground = hoverColor;
        
        setOpaque(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setFont(ModernTheme.BODY);
        setForeground(Color.WHITE);
        setPreferredSize(new Dimension(200, ModernTheme.BUTTON_HEIGHT));
        
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(hoverBackground);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(defaultBackground);
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw background
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 
                ModernTheme.BORDER_RADIUS, ModernTheme.BORDER_RADIUS));
        
        // Draw text
        FontMetrics metrics = g2.getFontMetrics(getFont());
        int x = (getWidth() - metrics.stringWidth(getText())) / 2;
        int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
        g2.setColor(getForeground());
        g2.drawString(getText(), x, y);
        
        g2.dispose();
    }
}