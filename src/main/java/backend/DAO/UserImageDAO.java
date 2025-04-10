package backend.DAO;

import backend.models.UserImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;

public class UserImageDAO
{
	private static final String URL = "jdbc:mysql://localhost:3306/peerpulse";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "1234";

	private Connection getConnection() throws SQLException
	{
		return DriverManager.getConnection(URL, USERNAME, PASSWORD);
	}

	public void insertUserImage(String username, File imageFile) throws SQLException
	{
		String sql = "INSERT INTO user_image (username, image) VALUES (?, ?)" +
				"ON DUPLICATE KEY UPDATE image = ?";

		try (
				Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				FileInputStream fis = new FileInputStream(imageFile)
			)
		{
			stmt.setString(1, username);
			stmt.setBinaryStream(2, fis, (int) imageFile.length());

			stmt.executeUpdate();
			System.out.println("User image inserted/updated successfully");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public UserImage getUserImage(String username) throws SQLException, IOException
	{
		String sql = "SELECT * FROM user_image WHERE username = ?";
		UserImage userImage = null;

		try (Connection conn = getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();

			if (rs.next())
			{
				byte[] imageData = rs.getBytes("image");
				userImage = new UserImage(username, imageData);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return userImage;
	}

	public void saveImageToFile(String username, String outputPath) throws IOException, SQLException
	{
		UserImage userImage = getUserImage(username);
		if (userImage != null)
		{
			byte[] imageData = userImage.getImage();
			File outputFile = new File(outputPath);
			try (FileOutputStream fos = new FileOutputStream(outputFile))
			{
				fos.write(imageData);
				System.out.println("Image saved to " + outputPath);
			}
		}
		else
		{
			System.out.println("No image found for user: " + username);
		}
	}

	public void deleteUserImage(String username) throws SQLException
	{
		String sql = "DELETE FROM user_image WHERE username = ?";

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
		{
			stmt.setString(1, username);
			stmt.executeUpdate();
			System.out.println("User image deleted successfully");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
