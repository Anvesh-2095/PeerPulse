package frontend.components;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JPasswordField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import frontend.theme.ModernTheme;

public class ModernPasswordField extends JPasswordField {
    private final String placeholder;
    
    public ModernPasswordField(String placeholder) {
        this.placeholder = placeholder;
        setupField();
    }
    
    private void setupField() {
        setFont(ModernTheme.BODY);
        setForeground(ModernTheme.TEXT_PRIMARY);
        setBackground(ModernTheme.SURFACE);
        setBorder(new CompoundBorder(
            new LineBorder(ModernTheme.BORDER, 1, true),
            new EmptyBorder(8, 12, 8, 12)
        ));
        
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                setBorder(new CompoundBorder(
                    new LineBorder(ModernTheme.PRIMARY, 2, true),
                    new EmptyBorder(7, 11, 7, 11)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                setBorder(new CompoundBorder(
                    new LineBorder(ModernTheme.BORDER, 1, true),
                    new EmptyBorder(8, 12, 8, 12)
                ));
            }
        });
    }
}