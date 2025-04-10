package frontend.components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.BorderUIResource;

import frontend.ActionListener;
import frontend.theme.ModernTheme;


public abstract class BasePanel extends JPanel {
    protected final ActionListener actionListener;

    public BasePanel(ActionListener actionListener) {
        this.actionListener = actionListener;
        setupPanel();
    }

    private void setupPanel() {
        setLayout(new BorderLayout(ModernTheme.SPACING, ModernTheme.SPACING));
        setBackground(ModernTheme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));
    }

    protected JPanel createCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ModernTheme.SURFACE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(),
                        ModernTheme.BORDER_RADIUS, ModernTheme.BORDER_RADIUS));
                g2.dispose();
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(ModernTheme.SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new BorderUIResource.EmptyBorderUIResource(24, 24, 24, 24),
            BorderFactory.createLineBorder(ModernTheme.BORDER, 1, true)
        ));
        return card;
    }

    protected JLabel createHeader(String text) {
        JLabel header = new JLabel(text);
        header.setFont(ModernTheme.HEADING);
        header.setForeground(ModernTheme.TEXT_PRIMARY);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        return header;
    }

    protected ModernTextField createTextField(String placeholder) {
        ModernTextField field = new ModernTextField(placeholder);
        field.setMaximumSize(new Dimension(300, ModernTheme.BUTTON_HEIGHT));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        return field;
    }
}