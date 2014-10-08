package database.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Scanner;
import java.io.*;
/**
 * 
 * Class for database connection
 *
 */
public class DBConnect {

	Connection con = null;
	public Statement st;

	public Connection getCon() {
		return con;
	}

	public void setCon(Connection con) {
		this.con = con;
	}

	public Statement getSt() {
		return st;
	}

	public void setSt(Statement st) {
		this.st = st;
	}

	/**
	 * Connect to DB
	 * 
	 * @return
	 */
	public boolean connectToDb()// connect to database
	{
		String password = "ishan";
		String user = "root";
		//Scanner scanner=null;
		boolean success = false; // returns true if successfully connected
		/*try{
		scanner = new Scanner(new File("password.txt"));
		
		if(scanner.hasNextLine()) {
			 password = scanner.nextLine();
			 user = scanner.nextLine();
		}
		}catch(Exception e){
			e.printStackTrace();
		}*/
		try {
			if (con == null) {
				Class.forName("com.mysql.jdbc.Driver");
				System.out.println("Driver Loaded");
				con = DriverManager.getConnection(
						"jdbc:mysql://localhost:3306/musicanalytics", user,
						password);
				System.out.println("Connection established");
				st = con.createStatement();
				success = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//scanner.close();
		return success;
	}

	/**
	 * Closes the database connection
	 * 
	 * @return
	 */
	public boolean close() // to close database connection
	{
		boolean success = false;
		try {
			if (con != null) {
				con.close();
				st.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return success;
	}

}