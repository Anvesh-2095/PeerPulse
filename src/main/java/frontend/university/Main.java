package frontend.university;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Main
{
	public static void main(String[] args)
	{
		String host = "localhost";
		int port = 12345;

		try
		{
			Socket socket = new Socket(host, port);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));
			Scanner sc = new Scanner(System.in);

			while (true)
			{
				System.out.println("1. Add University");
				System.out.println("2. Delete University");
				System.out.println("3. Get University Details");
				System.out.println("4. Exit");

				int choice = sc.nextInt();
				sc.nextLine(); // Consume newline character

				switch (choice)
				{
					case 1:
						System.out.print("Enter university name: ");
						String universityName = sc.nextLine();
						System.out.print("Enter University location: ");
						String universityLocation = sc.nextLine();
						out.println("ADD_UNIVERSITY|" + universityName + "|" + universityLocation);
						String addResponse = in.readLine();
						System.out.println("Response: " + addResponse);
						break;
					case 2:
						System.out.print("Enter university name: ");
						String deleteUniversityName = sc.nextLine();
						out.println("DELETE_UNIVERSITY|" + deleteUniversityName);
						String deleteResponse = in.readLine();
						System.out.println("Response: " + deleteResponse);
						break;
					case 3:
						System.out.print("Enter university name: ");
						String getUniversityName = sc.nextLine();
						out.println("GET_UNIVERSITY|" + getUniversityName);
						String response = in.readLine();
						System.out.println("Response: " + response);
						break;
					case 4:
						out.println("EXIT");
						socket.close();
						return;
					default:
						System.out.println("Invalid choice. Please try again.");
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
