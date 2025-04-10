package frontend.user;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class LogInPanel extends JPanel
{
//	private ActionListener actionListener;

	LogInPanel(ActionListener actionListener)
	{
//		this.actionListener = actionListener;
//		setLayout(FlowLayout);
		setSize(800, 600);
		setVisible(true);

		JLabel usernameLabel = new JLabel("Username:");
		usernameLabel.setBounds(300, 200, 100, 30);
		add(usernameLabel);

		JTextField usernameField = new JTextField();
		usernameField.setBounds(400, 200, 100, 30);
		usernameField.setSize(200, 30);
		add(usernameField);

		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setBounds(300, 250, 100, 30);
		add(passwordLabel);

		JPasswordField passwordField = new JPasswordField();
		passwordField.setBounds(400, 250, 100, 30);
		passwordField.setSize(200, 30);
		add(passwordField);

		JButton logInButton = new JButton("Log In");
		logInButton.setBounds(300, 300, 100, 30);
		add(logInButton);
		logInButton.addActionListener((ActionEvent e) -> actionListener.onActionPerformed("Log In", usernameField.getText().trim(), new String(passwordField.getPassword()).trim()));

		JButton signUpButton = new JButton("Sign Up");
		signUpButton.setBounds(450, 300, 100, 30);
		add(signUpButton);
		signUpButton.addActionListener((ActionEvent e) -> actionListener.onActionPerformed("Navigate Sign Up"));

		JButton forgotPasswordButton = new JButton("Forgot Password");
		forgotPasswordButton.setBounds(350, 350, 100, 30);
		forgotPasswordButton.setSize(150, 30);
		add(forgotPasswordButton);
		forgotPasswordButton.addActionListener((ActionEvent e) -> actionListener.onActionPerformed("Navigate Forgot Password"));
	}
}
