package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public abstract class BaseDAO<T> implements ResultSetExtractor<List<T>>{

	@Autowired
	protected JdbcTemplate template;	
	public int saveWithID(String sql, Object[] args){
		final String final_sql = sql;
		final Object[] final_args = args;
		KeyHolder keyHolder = new GeneratedKeyHolder();
		template.update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						PreparedStatement ps =	connection.prepareStatement(final_sql, Statement.RETURN_GENERATED_KEYS);
						for(int i = 1; i<=final_args.length; i ++){
							ps.setObject(i, final_args[i-1]);								
						}
						return ps;
					}
				},
				keyHolder);
		return keyHolder.getKey().intValue();
	}

	public List<T> readPage(int pageNum) {
		return search("", pageNum);
	}
	public abstract List<T> search (String query, int pageNum);				
	//MAKE SURE THIS IS ONLY USED WITH THE DAO's MAIN TABLE
	public List<T> readFirstLevel(String query, Object[] vals) {
		return template.query(query, vals, this);	
	}		
	
}
