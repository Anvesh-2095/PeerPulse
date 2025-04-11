package backend.services;

import backend.DAO.TransactionsDAO;
import backend.DAO.UniversityDAO;
import backend.DAO.UserAuthDAO;
import backend.DAO.UserDAO;
import backend.models.Transactions;
import backend.models.University;
import backend.models.User;
import backend.models.UserAuth;
import backend.session.SessionManager;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class UserService
{
	private UserDAO userDAO = new UserDAO();
	private TransactionsDAO transactionsDAO = new TransactionsDAO();
	private UserAuthDAO userAuthDAO = new UserAuthDAO();

	public String login(String username, String password)
	{
		if (SessionManager.isAuthorized(username))
		{
			return "ERROR|User already logged in";
		}

		try
		{
			if (userDAO.validateCredentials(username, password))
			{
				return "SUCCESS|Login successful|" + SessionManager.login(username);
			}
			else
			{
				return "ERROR|Invalid credentials";
			}
		}
		catch (SQLException e)
		{
			System.err.println("Error validating credentials: " + e.getMessage());
			e.printStackTrace();
			return "ERROR|Login failed";
		}
	}

	public String register(String username, String password, String name, Date dob, String university, char sex, String securityQuestion, String securityAnswer)
	{
		User user = new User(username, name, dob, university, 0, sex, new Date());
		UserAuth userAuth = new UserAuth(username, password, securityQuestion, securityAnswer);
		try
		{
			userDAO.insert(user);
			userAuthDAO.insert(userAuth);
			userDAO.incrementUniversityStudentCount(university); // Increment university student count
			return "SUCCESS|Registration successful";
		}
		catch (SQLException e)
		{
			System.err.println("Error registering user: " + e.getMessage());
			e.printStackTrace();
			return "ERROR|Registration failed";
		}
	}

	public String getProfile(String username)
	{
		try
		{
			User user = userDAO.findByUsername(username);
			if (user == null)
			{
				return "ERROR|User not found";
			}

			return String.join("|",
					"SUCCESS",
					user.getUsername(),
					user.getName(),
					user.getDateOfBirth().toString(),
					user.getUniversity(),
					String.valueOf(user.getLikes()),
					String.valueOf(user.getSex()),
					user.getDateJoined().toString()
			);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "ERROR|Could not retrieve user profile";
		}
	}

	public String getRandomProfile()
	{
		try
		{
			User user = userDAO.getRandomUser();
			if (user == null)
			{
				return "ERROR|No random user found";
			}

			return String.join("|",
					"SUCCESS",
					user.getUsername(),
					user.getName(),
					user.getDateOfBirth().toString(),
					user.getUniversity(),
					String.valueOf(user.getLikes()),
					String.valueOf(user.getSex()),
					user.getDateJoined().toString()
			);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "ERROR|Could not retrieve random user profile";
		}
	}

	public String getRandomUserSameUniversity(String username)
	{
		try
		{
			String university;
			User user = userDAO.findByUsername(username);
			if (user == null)
			{
				return "ERROR|User not found";
			}
			university = user.getUniversity();

			UniversityDAO universityDAO = new UniversityDAO();
			if (universityDAO.findByName(university) == null)
			{
				return "ERROR|University not found";
			}
			user = userDAO.getRandomUserByUniversity(university);
			if (user == null)
			{
				return "ERROR|No user found for the specified university";
			}
			else if (user.getUsername().equals(username) && universityDAO.findByName(university).getStudents() >= 1)
			{
				return this.getRandomUserSameUniversity(username);
			}

			return String.join("|",
					"SUCCESS",
					user.getUsername(),
					user.getName(),
					user.getDateOfBirth().toString(),
					user.getUniversity(),
					String.valueOf(user.getLikes()),
					String.valueOf(user.getSex()),
					user.getDateJoined().toString()
			);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "ERROR|Could not retrieve user by university";
		}
	}

	public String logout(String username)
	{
		SessionManager.logout(username);
		return "SUCCESS|Logout successful";
	}

	public String deleteAccount(String username, String password)
	{
		try
		{
			if (!userAuthDAO.validateCredentials(username, password))
			{
				return "ERROR|Invalid credentials";
			}

			// Get user's university before deletion for decrementing the count later
			User user = userDAO.findByUsername(username);
			if (user == null)
			{
				return "ERROR|User not found";
			}
			String university = user.getUniversity();


			// 1. Find all transactions made by this user
			List<Transactions> userTransactions = transactionsDAO.findTransactionsBySender(username);

			// 2. Undo the effect of each transaction on the receivers' like counts
			for (Transactions transaction : userTransactions)
			{
				// Reverse the effect of the transaction (multiply by -1)
				try
				{
					userDAO.updateLikes(transaction.getReceiver(), -transaction.getAmount());
				}
				catch (SQLException e)
				{
					e.printStackTrace();
					return "ERROR|Failed to undo transaction effects";
				}
			}

			// 3. Delete all transactions involving this user
			transactionsDAO.deleteAllTransactionsByUser(username);

			// 4. Delete the user auth record
			userAuthDAO.delete(username);

			// 5. Decrement university student count
			userDAO.decrementUniversityStudentCount(university);

			// 6. Delete the user record
			userDAO.delete(username);

			// 7. Log the user out if they're logged in
			SessionManager.logout(username);

			return "SUCCESS|Account deleted successfully";
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return "ERROR|Could not delete account: " + e.getMessage();
		}
	}
}
