package frontend.user;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ErrorMessageDisplayPanel extends JPanel
{
		ErrorMessageDisplayPanel(String errorMessage, ActionListener actionListener)
		{
			setSize(800, 600);
			setVisible(true);

			JLabel errorMessageLabel = new JLabel(errorMessage);
			errorMessageLabel.setBounds(300, 200, 200, 30);

			JButton okButton = new JButton("OK");
			okButton.setBounds(300, 250, 100, 30);
			okButton.addActionListener((ActionEvent e) -> actionListener.onActionPerformed("Navigate Log In"));

			add(errorMessageLabel);
			add(okButton);
		}
}
