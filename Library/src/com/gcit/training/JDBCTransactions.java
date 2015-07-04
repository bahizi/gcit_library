package com.gcit.training;
import java.sql.PreparedStatement;
public class JDBCTransactions {
	static String driver = "com.mysql.jdbc.driver";
	static String connection= "jdbc:mysql ://localhost:3306/library";
	static String user = "root";
	static String pass = "!umug3nI4";

	public static void main(String[] args) throws Exception{
		String createQuery = "INSERT INTO tbl_author(authorName) VALUES(?)";
		String updateQuery  = "UPDATE tbl_author SET authorName =  WHERE  = ?";
		PreparedStatement update;
		PreparedStatement create;	
	}
}
