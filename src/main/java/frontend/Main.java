package frontend;

import backend.models.User;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main implements frontend.ActionListener
{
	DisplayFrame displayFrame;

	String currentSessionUserName;
	String currentHashedPassword;

	String hostname = "localhost";
	int port = 12345;

	Socket socket;
	BufferedReader in;
	PrintWriter out;

	UUID uuid;

	Main()
	{
		try
		{
			socket = new Socket(hostname, port);
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));
			System.out.println("Connected to server at " + hostname + ":" + port);

			this.displayFrame = new DisplayFrame();
			showLoginPanel();

			// Add WindowListener to handle logout on exit
			displayFrame.addWindowListener(new WindowAdapter()
			{
				@Override
				public void windowClosing(WindowEvent e)
				{
					logoutOnExit();
				}
			});
		}
		catch (Exception e)
		{
			System.err.println("Could not connect to server or an I/O error occurred: " + e.getMessage());
			System.exit(1);
		}
	}
	public static void main(String[] args)
	{
		new Main();
	}

	void showLoginPanel()
	{
		this.displayFrame.getContentPane().removeAll();
		this.displayFrame.revalidate();
		LogInPanel logInPanel = new LogInPanel(this);
		this.displayFrame.add(logInPanel);
		// displayFrame.revalidate();
		this.displayFrame.repaint();
	}

	void showSignUpPanel()
	{
		this.displayFrame.getContentPane().removeAll();
		this.displayFrame.revalidate();
		SignUpPanel signUpPanel = new SignUpPanel(this);
		this.displayFrame.add(signUpPanel);
		// displayFrame.revalidate();
		this.displayFrame.repaint();
	}

	void showForgotPasswordPanel()
	{
		this.displayFrame.getContentPane().removeAll();
		this.displayFrame.revalidate();
		ForgotPasswordPanel forgotPasswordPanel = new ForgotPasswordPanel(this);
		this.displayFrame.add(forgotPasswordPanel);
		// displayFrame.revalidate();
		this.displayFrame.repaint();
	}

	void showErrorMessagePanel(String errorMessage)
	{
		this.displayFrame.getContentPane().removeAll();
		this.displayFrame.revalidate();
		ErrorMessageDisplayPanel errorMessageDisplayPanel = new ErrorMessageDisplayPanel(errorMessage, this);
		this.displayFrame.add(errorMessageDisplayPanel);
		// displayFrame.revalidate();
		this.displayFrame.repaint();
	}

	void showHomePanel()
	{
		// Request a random profile from the server
		out.println("GET_RANDOM_PROFILE");
		String serverResponse = null;
		try
		{
			serverResponse = in.readLine();
		}
		catch (IOException e)
		{
			System.out.println("Could not read server response: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Server response: " + serverResponse);

		String[] parts = serverResponse.split("\\|");
		if (parts[0].equals("ERROR"))
		{
			showErrorMessagePanel(parts[1]);
			return;
		}

		// Extract profile details from the response
		String fetchedUsername = parts[1];
		String fetchedName = parts[2];
		int upvoteCount = 0;
		
		// Check if upvote count is included in the response
		if (parts.length > 5) {
			try {
				upvoteCount = Integer.parseInt(parts[5]);
			} catch (NumberFormatException e) {
				System.out.println("Error parsing upvote count: " + e.getMessage());
			}
		}

		this.displayFrame.getContentPane().removeAll();
		this.displayFrame.revalidate();
		HomePanel homePanel = new HomePanel(this, fetchedUsername, fetchedName, upvoteCount);
		this.displayFrame.add(homePanel);
		displayFrame.revalidate(); // Why do I have to revalidate image here, but not all other components everywhere else?
//		this.displayFrame.repaint();
	}

	void showDialogBox(String message)
	{
		JOptionPane.showMessageDialog(this.displayFrame, message);
	}

	private String hashPassword(String password)
	{
		try
		{
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
			byte[] hash = md.digest(password.getBytes("UTF-8"));
			StringBuilder hexString = new StringBuilder();

			for (byte b : hash)
			{
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}

			return hexString.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void onActionPerformed(String action)
	{
		System.out.println(action);

		if (action.equals("Navigate Log In"))
		{
			System.out.println("Navigate Log In");
			showLoginPanel();
		}
		else if (action.equals("Navigate Sign Up"))
		{
			System.out.println("Navigate Sign Up");
			showSignUpPanel();
		}
		else if (action.equals("Navigate Forgot Password"))
		{
			System.out.println("Navigate Forgot Password");
			showForgotPasswordPanel();
		}
		else if (action.equals("next"))
		{
			System.out.println("next");
			showHomePanel(); // Refresh with next profile
		}
		else if (action.equals("exit"))
		{
			System.out.println("exit");
			logoutOnExit();
			System.exit(0);
		}
		else if (action.equals("deleteAccount"))
		{
			System.out.println("deleteAccount");
			showDialogBox("Do you want to delete your account?");
			out.println("DELETE_ACCOUNT|" + currentSessionUserName + currentHashedPassword);
			String serverResponse = null;
			try
			{
				serverResponse = in.readLine();
			}
			catch (IOException e)
			{
				System.out.println("Could not read server response: " + e.getMessage());
				e.printStackTrace();
				System.exit(1);
			}
			System.out.println("Server response: " + serverResponse);
			String[] parts = serverResponse.split("\\|");
			if (parts[0].equals("ERROR"))
			{
				showErrorMessagePanel(parts[1]);
			}
			else if (parts[0].equals("SUCCESS"))
			{
				System.out.println("Account deleted successfully");
				showDialogBox("Account deleted successfully");
				showLoginPanel(); // Redirect to login page
			}
		}
		else if (action.equals("deleteProfile"))
		{
			// Show password confirmation dialog
			JPasswordField passwordField = new JPasswordField();
			int option = JOptionPane.showConfirmDialog
			(
					displayFrame,
					passwordField,
					"Enter your password to confirm account deletion:",
					JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE
			);

			if (option == JOptionPane.OK_OPTION)
			{
				String password = new String(passwordField.getPassword());

				// Hash the password (assuming you have the same hashing method used during login)
				String hashedPassword = hashPassword(password);

				// Send delete account request to server
				out.println("DELETE_ACCOUNT|" + uuid + "|" + currentSessionUserName + "|" + hashedPassword);

				try
				{
					String response = in.readLine();
					System.out.println("Server response: " + response);

					String[] parts = response.split("\\|");
					if (parts[0].equals("SUCCESS"))
					{
						showDialogBox("Your account has been deleted successfully.");
						showLoginPanel(); // Redirect to login page
					}
					else if (parts[0].equals("ERROR"))
					{
						if (parts[1].contains("password") || parts[1].contains("Password"))
						{
							// Password verification failed
							showDialogBox("Incorrect password. Account deletion failed.");
						}
						else
						{
							// Session expired or other error
							showErrorMessagePanel(parts[1]);
						}
					}
				}
				catch (IOException e)
				{
					System.out.println("Could not read server response: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
		else
		{
			System.out.println("Unknown action: " + action);
		}
	}

	@Override
	public void onActionPerformed(String action, String username, String password)
	{
		System.out.println(action + " " + username + " " + password);
		String hashedPassword = hashPassword(password);
		System.out.println("Hashed Password: " + hashedPassword);

		if (action.equals("Log In"))
		{
			System.out.println("Log In");
			// validate username and password

			out.println("LOGIN|" + username + "|" + hashedPassword);
			String serverResponse = null;
			try
			{
				serverResponse = in.readLine();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			System.out.println("Server response: " + serverResponse);
			String[] parts = serverResponse.split("\\|");

			if (parts[0].equals("ERROR"))
			{
				showErrorMessagePanel(parts[1]);
			}
			else if (parts[0].equals("SUCCESS"))
			{
				System.out.println("Login successful");
				currentSessionUserName = username;
				currentHashedPassword = hashedPassword;
				uuid = UUID.fromString(parts[2]);
				showHomePanel();
			}
		}
	}

	@Override
	public void onActionPerformed(String action, String username, String password, String confirmPassword, String securityQuestion, String securityAnswer, String name, String dob, String university, String sex)
	{
		System.out.println(action + " " + username + " " + password + " " + confirmPassword + " " + securityQuestion + " " + securityAnswer);
		String hashedPassword = hashPassword(password);
		if (action.equals("Sign Up"))
		{
			System.out.println("Sign Up");

			if (!password.equals(confirmPassword))
			{
				showErrorMessagePanel("Passwords do not match");
				return;
			}

			out.println("REGISTER|" + username + "|" + hashedPassword + "|" + name + "|" + dob + "|" + university + "|" + sex + "|" + securityQuestion + "|" + securityAnswer);

			showLoginPanel();
		}
	}

	@Override
	public void onActionPerformed(String action, String username, String securityAnswer, String password, String confirmPassword)
	{
		System.out.println(action + " " + username + " " + password + " " + confirmPassword);
		if (!password.equals(confirmPassword))
		{
			showErrorMessagePanel("Passwords do not match");
			return;
		}
		String hashedPassword = hashPassword(password);
		if (action.equals("Reset Password")) // TODO implement later at backend
		{
			System.out.println("Reset Password");
			out.println("RESET_PASSWORD|" + username + "|" + hashedPassword + "|" + securityAnswer);
			String serverResponse = null;
			try
			{
				serverResponse = in.readLine();
			}
			catch (IOException e)
			{
				System.out.println("Could not read server response: " + e.getMessage());
				e.printStackTrace();
				System.exit(1);
			}
			System.out.println("Server response: " + serverResponse);
			String[] parts = serverResponse.split("\\|");
			if (parts[0].equals("ERROR"))
			{
				showErrorMessagePanel(parts[1]);
			}
			else if (parts[0].equals("SUCCESS"))
			{
				System.out.println("Password reset successful");
				showDialogBox("Password reset successful");
				showLoginPanel();
			}
		}
	}

	@Override
	public void onActionPerformed(String action, String username)
	{
		System.out.println(action + " " + username);

		if (action.equals("viewProfile"))
		{
			System.out.println("viewProfile");
		}
		else if (action.equals("upvote"))
		{
			System.out.println("upvote");
			out.println("UPVOTE|" + uuid + "|" + currentSessionUserName + "|" + username);
			String serverResponse = null;
			try
			{
				serverResponse = in.readLine();
			}
			catch (IOException e)
			{
				System.out.println("Could not read server response: " + e.getMessage());
				e.printStackTrace();
				System.exit(1);
			}
			System.out.println("Server response: " + serverResponse);
			String[] parts = serverResponse.split("\\|");
			if (parts[0].equals("ERROR"))
			{
				// Check if the error is due to upvoting oneself
				if (parts[1].equals("Cannot upvote yourself") || parts[1].equals("Already upvoted"))
				{
					showDialogBox(parts[1]); // Show the error in a dialog box
				} else
				{
					showErrorMessagePanel(parts[1]); // Redirect to login for other errors
				}
			}
			else if (parts[0].equals("SUCCESS"))
			{
				System.out.println("Upvote successful");
				showDialogBox("Upvote successful");
				showHomePanel(); // Refresh with next profile
			}
		}
		else if (action.equals("downvote"))
		{
			System.out.println("downvote");
			out.println("DOWNVOTE|" + uuid + "|" + currentSessionUserName + "|" + username);
			String serverResponse = null;
			try
			{
				serverResponse = in.readLine();
			}
			catch (IOException e)
			{
				System.out.println("Could not read server response: " + e.getMessage());
				e.printStackTrace();
				System.exit(1);
			}
			System.out.println("Server response: " + serverResponse);
			String[] parts = serverResponse.split("\\|");
			if (parts[0].equals("ERROR"))
			{
				// Check if the error is due to downvoting oneself
				if (parts[1].equals("Cannot downvote yourself") || parts[1].equals("Already downvoted"))
				{
					showDialogBox(parts[1]); // Show the error in a dialog box
				}
				else
				{
					showErrorMessagePanel(parts[1]); // Redirect to login for other errors
				}
			}
			else if (parts[0].equals("SUCCESS"))
			{
				System.out.println("Downvote successful");
				showDialogBox("Downvote successful");
				showHomePanel(); // Refresh with next profile
			}
		}
		else if (action.equals("next"))
		{
			System.out.println("next");
			showHomePanel(); // Refresh with next profile
		}
		else if (action.equals("deleteAccount"))
		{
			System.out.println("deleteAccount");
			showDialogBox("Do you want to delete your account?");
			out.println("DELETE_ACCOUNT|" + currentSessionUserName + currentHashedPassword);
			String serverResponse = null;
			try
			{
				serverResponse = in.readLine();
			}
			catch (IOException e)
			{
				System.out.println("Could not read server response: " + e.getMessage());
				e.printStackTrace();
				System.exit(1);
			}
			System.out.println("Server response: " + serverResponse);
			String[] parts = serverResponse.split("\\|");
			if (parts[0].equals("ERROR"))
			{
				showErrorMessagePanel(parts[1]);
			}
			else if (parts[0].equals("SUCCESS"))
			{
				System.out.println("Account deleted successfully");
				showDialogBox("Account deleted successfully");
				showLoginPanel(); // Redirect to login page
			}
		}
	}


	private void logoutOnExit()
	{
		if (currentSessionUserName != null)
		{
			System.out.println("Logging out " + currentSessionUserName + " on exit");
			out.println("LOGOUT|" + uuid + "|" + currentSessionUserName);
			try
			{
				String serverResponse = in.readLine();
				System.out.println("Server response: " + serverResponse);
			} catch (IOException e)
			{
				System.out.println("Could not read server response: " + e.getMessage());
				e.printStackTrace();
			} finally
			{
				try
				{
					socket.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		else
		{
			try
			{
				socket.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
