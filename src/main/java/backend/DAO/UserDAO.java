package backend.DAO;

import backend.models.University;
import backend.models.User;
import backend.DAO.UniversityDAO;
import com.mysql.cj.exceptions.ConnectionIsClosedException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO
{
	private static final String URL = "jdbc:mysql://localhost:3306/peerpulse";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "1234";

	private Connection getConnection()
	{
		Connection connection = null;
		try
		{
			// TODO: check classpath for the MySQL JDBC driver
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return connection;
	}

	public void insert(User user) throws SQLException
	{
		String sql = "INSERT INTO user (username, name, date_of_birth, university, likes, sex, date_joined) VALUES (?, ?, ?, ?, ?, ?, ?)";

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getName());
			stmt.setDate(3, new java.sql.Date(user.getDateOfBirth().getTime()));
			stmt.setString(4, user.getUniversity());
			stmt.setInt(5, user.getLikes());
			stmt.setString(6, String.valueOf(user.getSex()));
			stmt.setDate(7, new java.sql.Date(user.getDateJoined().getTime()));

			stmt.executeUpdate();
			System.out.println("User inserted successfully");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void delete(String username) throws SQLException
	{
		String sql = "DELETE FROM user WHERE username = ?";

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setString(1, username);
			stmt.executeUpdate();
			System.out.println("User deleted successfully");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public User findByUsername(String username) throws SQLException
	{
		String sql = "SELECT * FROM user WHERE username = ?";
		User user = null;

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();

			if (rs.next())
			{
				user = new User(rs.getString("username"), rs.getString("name"), rs.getDate("date_of_birth"), rs.getString("university"), rs.getInt("likes"), rs.getString("sex").charAt(0), rs.getDate("date_joined"));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return user;
	}

	public List<User> listAllUsers() throws SQLException
	{
		List<User> users = new ArrayList<>();
		String sql = "SELECT * FROM user";

		try (
			Connection conn = getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery()
		)
		{
			while (rs.next())
			{
				users.add(new User(
						rs.getString("username"),
						rs.getString("name"),
						rs.getDate("date_of_birth"),
						rs.getString("university"),
						rs.getInt("likes"),
						rs.getString("sex").charAt(0),
						rs.getDate("date_joined")
				));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return users;
	}

	public void update(User user) throws SQLException
	{
		String sql = "UPDATE user SET name = ?, university = ?, likes = ?, sex = ? WHERE username = ?";

		try (
			Connection conn = getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql)
		)
		{

			stmt.setString(1, user.getName());
			stmt.setString(2, user.getUniversity());
			stmt.setInt(3, user.getLikes());
			stmt.setString(4, String.valueOf(user.getSex()));
			stmt.setString(5, user.getUsername());

			stmt.executeUpdate();
			System.out.println("User updated successfully.");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void updateLikes(String username, int likes) throws SQLException
	{
		String sql = "UPDATE user SET likes = likes + ? WHERE username = ?";

		try (Connection conn = getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql))
		{

			stmt.setInt(1, likes);
			stmt.setString(2, username);

			stmt.executeUpdate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void updateLikesValue(String username, int likes) throws SQLException
	{
		String sql = "UPDATE user SET likes = ? WHERE username = ?";

		try (Connection conn = getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql))
		{

			stmt.setInt(1, likes);
			stmt.setString(2, username);

			stmt.executeUpdate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public boolean validateCredentials(String username, String password) throws SQLException
	{
		return new UserAuthDAO().validateCredentials(username, password);
	}


	public void incrementUniversityStudentCount(String university)
	{
		UniversityDAO universityDAO = new UniversityDAO();
		University uni = universityDAO.findByName(university);
		if (uni != null)
		{
			int newCount = uni.getStudents() + 1;
			uni.setStudents(newCount);
			universityDAO.update(uni);
		}
		else
		{
			System.out.println("University not found: " + university);
		}
	}

	public void decrementUniversityStudentCount(String university)
	{
		UniversityDAO universityDAO = new UniversityDAO();
		University uni = universityDAO.findByName(university);
		if (uni != null)
		{
			int newCount = uni.getStudents() - 1;
			uni.setStudents(newCount);
			universityDAO.update(uni);
		}
		else
		{
			System.out.println("University not found: " + university);
		}
	}

	public boolean isUsernameTaken(String username)
	{
		String sql = "SELECT COUNT(*) FROM user WHERE username = ?";
		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
			{
				return rs.getInt(1) > 0;
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public boolean isUserNameExists(String username)
	{
		return isUsernameTaken(username);
	}

	public User getRandomUser()
	{
		String sql = "SELECT * FROM user ORDER BY RAND() LIMIT 1";
		User user = null;
		try (Connection conn = getConnection(); Statement stmt = conn.createStatement())
		{
			ResultSet rs = stmt.executeQuery(sql);

			if (rs.next())
			{
				user = new User(rs.getString("username"), rs.getString("name"), rs.getDate("date_of_birth"), rs.getString("university"), rs.getInt("likes"), rs.getString("Sex").charAt(0), rs.getDate("date_joined"));
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return user;
	}

	public int getUserCount()
	{
		String sql = "SELECT COUNT(*) FROM user";
		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
		{
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
			{
				return rs.getInt(1);
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	public User getRandomUserByUniversity(String university)
	{
		String sql = "SELECT * FROM user WHERE university = ? ORDER BY RAND() LIMIT 1";
		User user = null;

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setString(1, university);
			ResultSet rs = stmt.executeQuery();

			if (rs.next())
			{
				user = new User(rs.getString("username"), rs.getString("name"), rs.getDate("date_of_birth"), rs.getString("university"), rs.getInt("likes"), rs.getString("sex").charAt(0), rs.getDate("date_joined"));
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return user;
	}
}
