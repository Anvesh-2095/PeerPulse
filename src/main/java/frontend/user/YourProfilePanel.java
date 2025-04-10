package frontend.user;

import javax.swing.*;

public class YourProfilePanel extends JPanel
{
	YourProfilePanel(String username, ActionListener actionListener)
	{
		// only shows your account and allows you to delete it

		// fetch data from database and display it

		JLabel usernameLabel = new JLabel("Username: " + username);
		usernameLabel.setBounds(5, 5, 100, 20);
		add(usernameLabel);


		// UI Components


		JButton deleteAccountButton = new JButton("Delete Account");
		deleteAccountButton.setBounds(5, 25, 100, 20);
		deleteAccountButton.addActionListener(e -> actionListener.onActionPerformed("deleteAccount", username));
		add(deleteAccountButton);
	}
}
