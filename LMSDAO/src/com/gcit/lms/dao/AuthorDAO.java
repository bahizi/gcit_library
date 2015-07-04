package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.gcit.lms.domain.Author;

public class AuthorDAO {
	String driver = "com.mysql.jdbc.Driver";
	String connection = "jdbc:mysql://localhost:3306/library";
	String user = "root";
	String pass = "root";
	
	public void create(Author author) throws Exception{
		Connection conn = getConnection();
		
		String insert = "insert into tbl_author (authorName) values(?)";
		PreparedStatement stmt = conn.prepareStatement(insert);
		stmt.setString(1, author.getAuthorName());
		stmt.executeUpdate();

	}

	public void update(Author author) throws Exception{
		Connection conn = getConnection();
		
		String update = "update tbl_author set authorName = ? where authorId = ?";
		PreparedStatement stmt = conn.prepareStatement(update);
		stmt.setString(1, author.getAuthorName());
		stmt.setInt(2, author.getAuthorId());
		stmt.executeUpdate();
	}

	public void delete(Author author) throws Exception{
		Connection conn = getConnection();
		
		String delete = "delete from tbl_author where authorId = ?";
		PreparedStatement stmt = conn.prepareStatement(delete);
		stmt.setInt(1, author.getAuthorId());
		stmt.executeUpdate();
	}

	private Connection getConnection() throws ClassNotFoundException,
			SQLException {
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(connection, user, pass);
		return conn;
	}

	public List<Author> readAll() {
		return null;

	}

	public Author readOne() {
		return null;
	}

}
