package frontend.user;

import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.UUID;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main implements ActionListener
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
		out.println("GET_USER_BY_UNIVERSITY|" + uuid + "|" + currentSessionUserName);
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

	void showViewProfile(String fetchedUsername, String fetchedName, String fetchedUniversity, Date fetchedDob, Date fetchedDateJoined, int upvoteCount, char fetchedSex)
	{
		// Clear the frame contents properly
		displayFrame.getContentPane().removeAll();

		// Create the panel
		ViewProfilePanel viewProfilePanel = new ViewProfilePanel(
				this,  // assuming 'this' implements ActionListener
				fetchedUsername,
				fetchedName,
				fetchedUniversity,
				fetchedDob,
				fetchedDateJoined,
				upvoteCount,
				fetchedSex
		);

		// Set a preferred size if needed
		viewProfilePanel.setPreferredSize(new Dimension(displayFrame.getWidth(), displayFrame.getHeight()));

		// Add the panel to the frame
		displayFrame.add(viewProfilePanel);

		// Make sure to call these in this order
		displayFrame.pack();
		displayFrame.revalidate();
		displayFrame.repaint();
		displayFrame.setVisible(true);  // Ensure the frame is visible
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
			out.println("DELETE_ACCOUNT|" + uuid + "|" + currentSessionUserName + "|" + currentHashedPassword);
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
		else if (action.equals("viewMyProfile"))
		{
			System.out.println("viewMyProfile");

			// Move profile view operation to a background thread
			new Thread(() ->
			{
				// Request the current user's profile data
				out.println("GET_PROFILE|" + uuid + "|" + currentSessionUserName + "|" + currentSessionUserName);
				try
				{
					final String serverResponse = in.readLine();

					SwingUtilities.invokeLater(() ->
					{
						System.out.println("Server response: " + serverResponse);
						String[] parts = serverResponse.split("\\|");
						if (parts[0].equals("ERROR"))
						{
							showErrorMessagePanel(parts[1]);
							return;
						}

						try
						{
							// Make sure we have enough parts
							if (parts.length < 7) {
								showErrorMessagePanel("Incomplete profile data received");
								return;
							}

							String fetchedUsername = parts[1];
							String fetchedName = parts[2];

							// Parse DOB
							Date fetchedDob = null;
							try
							{
								fetchedDob = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(parts[3]);
							}
							catch (Exception e)
							{
								System.out.println("Error parsing DOB: " + e.getMessage());
							}

							String fetchedUniversity = parts[4];

							// Parse upvote count
							int upvoteCount = 0;
							try
							{
								upvoteCount = Integer.parseInt(parts[5]);
							}
							catch (NumberFormatException e)
							{
								System.out.println("Error parsing upvote count: " + e.getMessage());
							}

							// Parse sex
							char fetchedSex = 'U'; // Default if parsing fails
							if (parts[6].length() > 0)
							{
								fetchedSex = parts[6].charAt(0);
							}

							// Parse date joined
							Date fetchedDateJoined = null;
							if (parts.length > 7)
							{
								try
								{
									fetchedDateJoined = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(parts[7]);
								}
								catch (Exception e)
								{
									System.out.println("Error parsing date joined: " + e.getMessage());
								}
							}

							// Show the ViewMyProfile panel
							showViewMyProfile(fetchedUsername, fetchedName, fetchedUniversity,
									fetchedDob, fetchedDateJoined, upvoteCount, fetchedSex);
						}
						catch (Exception e)
						{
							e.printStackTrace();
							showErrorMessagePanel("Error processing profile data: " + e.getMessage());
						}
					});
				}
				catch (IOException e)
				{
					e.printStackTrace();
					SwingUtilities.invokeLater(() ->
													   showErrorMessagePanel("Connection error while fetching profile")
					);
				}
			}).start();
		}
		else if (action.equals("viewLogs"))
		{
			System.out.println("viewLogs");

			// Move log fetching operation to a background thread
			new Thread(() ->
			{
				// Request user logs from the server
				out.println("GET_LOGS|" + uuid + "|" + currentSessionUserName);
				try
				{
					final String serverResponse = in.readLine();

					SwingUtilities.invokeLater(() ->
					{
						System.out.println("Server response: " + serverResponse);
						String[] parts = serverResponse.split("\\|");
						if (parts[0].equals("ERROR"))
						{
							showErrorMessagePanel(parts[1]);
							return;
						}

						try
						{
							// Process log entries
							List<String[]> logs = new java.util.ArrayList<>();

							// Start from index 1 to skip the SUCCESS part
							for (int i = 1; i < parts.length; i++)
							{
								if (parts[i].contains("~"))
								{
									String[] logEntry = parts[i].split("~");
									if (logEntry.length >= 2)
									{
										logs.add(new String[] {logEntry[1], logEntry[0]});
									}
								}
							}

							// Show logs panel
							showLogsPanel(logs);
						}
						catch (Exception e)
						{
							e.printStackTrace();
							showErrorMessagePanel("Error processing logs data: " + e.getMessage());
						}
					});
				}
				catch (IOException e)
				{
					e.printStackTrace();
					SwingUtilities.invokeLater(() ->
													   showErrorMessagePanel("Connection error while fetching logs")
					);
				}
			}).start();
		}
		else if (action.equals("back"))
		{
			System.out.println("back");
			displayFrame.getContentPane().removeAll();
			displayFrame.revalidate();
			HomePanel homePanel = new HomePanel(this, currentSessionUserName, currentSessionUserName, 0);
			displayFrame.add(homePanel);
			displayFrame.revalidate();
			displayFrame.repaint();
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
		if (action.equals("Log In"))
		{
			System.out.println("Log In");
			String hashedPassword = hashPassword(password);
			System.out.println("Hashed Password: " + hashedPassword);
			performLogin(username, hashedPassword);
		}
		else if (action.equals("deleteProfile"))
		{
			System.out.println("deleteProfile");
			showDialogBox("Do you want to delete your account?");
			out.println("DELETE_ACCOUNT|" + uuid + "|" + currentSessionUserName + "|" + currentHashedPassword);
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

			// Move signup network operation to a background thread
			new Thread(() ->
			{
				out.println("REGISTER|" + username + "|" + hashedPassword + "|" + name + "|" + dob + "|" + university + "|" + sex + "|" + securityQuestion + "|" + securityAnswer);

				try
				{
					// Wait for server response
					final String serverResponse = in.readLine();

					// Update UI on EDT
					SwingUtilities.invokeLater(() ->
					{
						String[] parts = serverResponse.split("\\|");
						if (parts[0].equals("ERROR"))
						{
							showErrorMessagePanel(parts[1]);
						}
						else if (parts[0].equals("SUCCESS"))
						{
							showDialogBox("Registration successful! Please login.");
							showLoginPanel();
						}
					});
				}
				catch (IOException e)
				{
					e.printStackTrace();
					SwingUtilities.invokeLater(() ->
													   showErrorMessagePanel("Connection error during registration"));
				}
			}).start();
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
			System.out.println("viewProfile" + " " + username);

			// Move profile view operation to a background thread
			new Thread(() ->
			{
				out.println("GET_PROFILE|" + uuid + "|" + username);
				try
				{
					final String serverResponse = in.readLine();

					SwingUtilities.invokeLater(() ->
					{
						System.out.println("Server response: " + serverResponse);
						String[] parts = serverResponse.split("\\|");
						if (parts[0].equals("ERROR"))
						{
							showErrorMessagePanel(parts[1]);
							return;
						}

						try
						{
							// Make sure we have enough parts
							if (parts.length < 7) {
								showErrorMessagePanel("Incomplete profile data received");
								return;
							}

							String fetchedUsername = parts[1];
							String fetchedName = parts[2];

							// Parse DOB
							Date fetchedDob = null;
							try
							{
								fetchedDob = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(parts[3]);
							}
							catch (Exception e)
							{
								System.out.println("Error parsing DOB: " + e.getMessage());
							}

							String fetchedUniversity = parts[4];

							// Parse upvote count
							int upvoteCount = 0;
							try
							{
								upvoteCount = Integer.parseInt(parts[5]);
							}
							catch (NumberFormatException e)
							{
								System.out.println("Error parsing upvote count: " + e.getMessage());
							}

							// Parse sex
							char fetchedSex = 'U'; // Default if parsing fails
							if (parts[6].length() > 0)
							{
								fetchedSex = parts[6].charAt(0);
							}

							// Parse date joined
							Date fetchedDateJoined = null;
							if (parts.length > 7)
							{
								try
								{
									fetchedDateJoined = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(parts[7]);
								}
								catch (Exception e)
								{
									System.out.println("Error parsing date joined: " + e.getMessage());
								}
							}

							// Debug information
							System.out.println("Creating ViewProfilePanel with:");
							System.out.println("Username: " + fetchedUsername);
							System.out.println("Name: " + fetchedName);
							System.out.println("University: " + fetchedUniversity);
							System.out.println("DOB: " + fetchedDob);
							System.out.println("Date Joined: " + fetchedDateJoined);
							System.out.println("Upvotes: " + upvoteCount);
							System.out.println("Sex: " + fetchedSex);

							showViewProfile(fetchedUsername, fetchedName, fetchedUniversity, fetchedDob, fetchedDateJoined, upvoteCount, fetchedSex);
						}
						catch (Exception e)
						{
							e.printStackTrace();
							showErrorMessagePanel("Error processing profile data: " + e.getMessage());
						}
					});
				}
				catch (IOException e)
				{
					e.printStackTrace();
					SwingUtilities.invokeLater(() ->
													   showErrorMessagePanel("Connection error while fetching profile")
					);
				}
			}).start();
		}
		else if (action.equals("upvote"))
		{
			System.out.println("upvote");
			handleVote("UPVOTE", username);
		}
		else if (action.equals("downvote"))
		{
			System.out.println("downvote");
			handleVote("DOWNVOTE", username);
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
			out.println("DELETE_ACCOUNT|" + uuid + "|" + currentSessionUserName + "|" + currentHashedPassword);
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

	private void performLogin(String username, String hashedPassword)
	{
		// Create a new thread for network operations
		new Thread(() ->
		{
			out.println("LOGIN|" + username + "|" + hashedPassword);
			try
			{
				final String serverResponse = in.readLine();

				// Update UI on the EDT using SwingUtilities.invokeLater
				SwingUtilities.invokeLater(() ->
				{
					String[] parts = serverResponse.split("\\|");
					if (parts[0].equals("ERROR"))
					{
						showErrorMessagePanel(parts[1]);
					}
					else if (parts[0].equals("SUCCESS"))
					{
						currentSessionUserName = username;
						currentHashedPassword = hashedPassword;
						uuid = UUID.fromString(parts[2]);

						// Call to load home panel also in a separate thread
						loadHomePanel();
					}
				});
			}
			catch (IOException e)
			{
				e.printStackTrace();
				SwingUtilities.invokeLater(() ->
												   showErrorMessagePanel("Connection error"));
			}
		}).start();
	}

	private void loadHomePanel()
	{
		new Thread(() ->
		{
			out.println("GET_RANDOM_PROFILE");
			try
			{
				final String serverResponse = in.readLine();

				SwingUtilities.invokeLater(() ->
				{
					String[] parts = serverResponse.split("\\|");
					if (parts[0].equals("ERROR"))
					{
						showErrorMessagePanel(parts[1]);
						return;
					}

					String fetchedUsername = parts[1];
					String fetchedName = parts[2];
					int upvoteCount = 0;

					if (parts.length > 5)
					{
						try
						{
							upvoteCount = Integer.parseInt(parts[5]);
						}
						catch (NumberFormatException e)
						{
							System.out.println("Error parsing upvote count: " + e.getMessage());
						}
					}

					displayFrame.getContentPane().removeAll();
					displayFrame.revalidate();
					HomePanel homePanel = new HomePanel(Main.this, fetchedUsername, fetchedName, upvoteCount);
					displayFrame.add(homePanel);
					displayFrame.revalidate();
					displayFrame.repaint();
				});
			}
			catch (IOException e)
			{
				e.printStackTrace();
				SwingUtilities.invokeLater(() ->
												   showErrorMessagePanel("Connection error"));
			}
		}).start();
	}

	private void handleVote(String voteType, String username)
	{
		System.out.println("Sending " + voteType + " for user " + username);

		new Thread(() ->
		{
			// Debug the command being sent
			String command = voteType + "|" + uuid + "|" + currentSessionUserName + "|" + username;
			System.out.println("Sending command: " + command);

			out.println(command);
			try
			{
				final String serverResponse = in.readLine();
				System.out.println("Raw server response: " + serverResponse);

				if (serverResponse == null) {
					SwingUtilities.invokeLater(() ->
													   showErrorMessagePanel("No response from server. Server may be down.")
					);
					return;
				}

				SwingUtilities.invokeLater(() ->
				{
					String[] parts = serverResponse.split("\\|");
					if (parts[0].equals("ERROR"))
					{
						showDialogBox(parts[1]);
					}
					else if (parts[0].equals("SUCCESS"))
					{
						showDialogBox(voteType + " successful");

						// Load a new profile after successful vote
						loadHomePanel();
					}
				});
			}
			catch (IOException e)
			{
				e.printStackTrace();
				SwingUtilities.invokeLater(() ->
												   showErrorMessagePanel("Connection error during " + voteType.toLowerCase())
				);
			}
		}).start();
	}

	private void showViewMyProfile(String fetchedUsername, String fetchedName, String fetchedUniversity,
								   Date fetchedDob, Date fetchedDateJoined, int upvoteCount, char fetchedSex)
	{
		// Clear the frame contents properly
		displayFrame.getContentPane().removeAll();

		// Create the panel
		ViewMyProfile viewMyProfilePanel = new ViewMyProfile(
				this,  // 'this' implements ActionListener
				fetchedUsername,
				fetchedName,
				fetchedUniversity,
				fetchedDob,
				fetchedDateJoined,
				upvoteCount,
				fetchedSex
		);

		// Set a preferred size if needed
		viewMyProfilePanel.setPreferredSize(new Dimension(displayFrame.getWidth(), displayFrame.getHeight()));

		// Add the panel to the frame
		displayFrame.add(viewMyProfilePanel);

		// Make sure to call these in this order
		displayFrame.pack();
		displayFrame.revalidate();
		displayFrame.repaint();
		displayFrame.setVisible(true);  // Ensure the frame is visible
	}

	private void showLogsPanel(List<String[]> logs)
	{
		// Clear the frame contents properly
		displayFrame.getContentPane().removeAll();

		// Create the logs panel
		LogsPanel logsPanel = new LogsPanel(this, logs);

		// Set a preferred size if needed
		logsPanel.setPreferredSize(new Dimension(displayFrame.getWidth(), displayFrame.getHeight()));

		// Add the panel to the frame
		displayFrame.add(logsPanel);

		// Make sure to call these in this order
		displayFrame.pack();
		displayFrame.revalidate();
		displayFrame.repaint();
		displayFrame.setVisible(true);
	}
}
