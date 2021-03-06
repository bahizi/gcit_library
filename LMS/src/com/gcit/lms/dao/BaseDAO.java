package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public abstract class BaseDAO<T> {
	
	private Connection connection = null;
	
	public BaseDAO(Connection conn) throws Exception{
		this.connection = conn;
	}
	
	public Connection getConnection() throws Exception{
		return connection;
	}

	public void save(String query, Object[] vals) throws Exception{
		Connection conn = getConnection();
		PreparedStatement stmt = conn.prepareStatement(query);
		int count = 1;
		for(Object o: vals){
			stmt.setObject(count, o);
			count++;
		}		
		stmt.executeUpdate();
		
	}
	public abstract List<?> readPage(int pageNum) throws Exception;
	public abstract List<?> search (String query, int pageNum) throws Exception;
	
	public int saveWithID(String query, Object[] vals) throws Exception{
		Connection conn = getConnection();

		PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		int count = 1;
		for(Object o: vals){
			stmt.setObject(count, o);
			count++;
		}
		stmt.executeUpdate();
		ResultSet rs = stmt.getGeneratedKeys();
		if(rs.next()){
			return rs.getInt(1);
		}else{
			return -1;
		}
	}
	
	public List<?> read(String query, Object[] vals) throws Exception{
		Connection conn = getConnection();
		PreparedStatement stmt = conn.prepareStatement(query);
		
		if(vals!=null){
			int count = 1;
			for(Object o: vals){
				stmt.setObject(count, o);
				count++;
			}
		}
		ResultSet rs = stmt.executeQuery();
		return extractData(rs);
	}
	
	public abstract List<T> extractData(ResultSet rs) throws Exception;
	
	//MAKE SURE THIS IS ONLY USED WITH THE DAO's MAIN TABLE
	public List<T> readFirstLevel(String query, Object[] vals) throws Exception{
		Connection conn = getConnection();
		PreparedStatement stmt = conn.prepareStatement(query);
		
		if(vals!=null){
			int count = 1;
			for(Object o: vals){
				stmt.setObject(count, o);
				count++;
			}
		}
		ResultSet rs = stmt.executeQuery();
		return extractDataFirstLevel(rs);
	}
	
	public abstract List<T> extractDataFirstLevel(ResultSet rs) throws Exception;
}
