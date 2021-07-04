/**
 * 
 */
package com.example.jsf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author swapnilsarwade
 *
 */
public class TestDbConnection {

	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.jdbc.Driver"); //com.mysql.cj.jdbc.Driver
			Connection con = DriverManager.getConnection("jdbc:mysql://shadalwali.co:3306/admin_db", "dg_user",
					"dg_user");
			// here sonoo is database name, root is username and password
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from test_table");
			while (rs.next())
				System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3));
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
