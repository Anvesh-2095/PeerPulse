package backend.DAO;

import backend.models.University;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UniversityDAO
{
	private static final String URL = "jdbc:mysql://localhost:3306/peerpulse";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "1234";

	public Connection getConnection() throws SQLException
	{
		return DriverManager.getConnection(URL, USERNAME, PASSWORD);
	}

	public void insert(University university)
	{
		String sql = "INSERT INTO university (name, location, students, admin_username) VALUES (?, ?, ?, ?)";

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setString(1, university.getName());
			stmt.setString(2, university.getLocation());
			stmt.setInt(3, university.getStudents());
			stmt.setString(4, university.getAdminUsername());

			stmt.executeUpdate();
			System.out.println("University inserted successfully");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public void delete(String name)
	{
		String sql = "DELETE FROM university WHERE name = ?";

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setString(1, name);
			stmt.executeUpdate();
			System.out.println("University deleted successfully");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public void update(University university)
	{
		String sql = "UPDATE university SET location = ?, students = ?, admin_username = ? WHERE name = ?";

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setString(1, university.getLocation());
			stmt.setInt(2, university.getStudents());
			stmt.setString(3, university.getAdminUsername());
			stmt.setString(4, university.getName());

			stmt.executeUpdate();
			System.out.println("University updated successfully");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public University findByName(String name)
	{
		String sql = "SELECT * FROM university WHERE name = ?";
		University university = null;

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();

			if (rs.next())
			{
				university = new University(rs.getString("name"), rs.getString("location"), rs.getInt("students"), rs.getString("admin_username"));
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return university;
	}

	public List<University> findAll()
	{
		String sql = "SELECT * FROM university";
		List<University> universities = new ArrayList<>();

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery())
		{
			while (rs.next())
			{
				universities.add(new University(rs.getString("name"), rs.getString("location"), rs.getInt("students"), rs.getString("admin_username")));
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return universities;
	}
}
