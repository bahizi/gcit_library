package com.gcit.lms;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.gcit.lms.dao.AuthorDAO;
import com.gcit.lms.dao.BookCopiesDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.dao.BookLoanDAO;
import com.gcit.lms.dao.BorrowerDAO;
import com.gcit.lms.dao.GenreDAO;
import com.gcit.lms.dao.LibraryBranchDAO;
import com.gcit.lms.dao.PublisherDAO;
import com.mongodb.MongoClient;

@EnableTransactionManagement
@Configuration
public class LMSConfig {
	
	private static final String driver = "com.mysql.jdbc.Driver";
	private static final String url = "jdbc:mysql://localhost:3306/library";
	private static final String user = "root";
	private static final String pwd = "!umug3nI4";
	
	@Bean
	public AuthorDAO authDao() {
		return new AuthorDAO();
	}
	@Bean
	public BookDAO bookDAO() {
		return new BookDAO();
	}
	@Bean
	public BookCopiesDAO bookCopiesDAO() {
		return new BookCopiesDAO();
	}
	@Bean
	public BookLoanDAO bookLoanDAO() {
		return new BookLoanDAO();
	}
	@Bean
	public BorrowerDAO borrowerDAO() {
		return new BorrowerDAO();
	}
	@Bean
	public GenreDAO genreDAO() {
		return new GenreDAO();
	}
	@Bean
	public LibraryBranchDAO libDAO() {
		return new LibraryBranchDAO();
	}
	@Bean
	public PublisherDAO pubDao() {
		return new PublisherDAO();
	}
	
	@Bean
	public PlatformTransactionManager txManager(){
		DataSourceTransactionManager tx = new DataSourceTransactionManager();
		tx.setDataSource(datasource());		
		return tx;
	}
	
	@Bean
	public JdbcTemplate template() {
		JdbcTemplate jdbc = new JdbcTemplate();
		jdbc.setDataSource(datasource());
		
		return jdbc;
	}
	
	@Bean
	public BasicDataSource datasource(){
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(driver);
		ds.setUrl(url);
		ds.setUsername(user);
		ds.setPassword(pwd);

		return ds;
	}
	
	@Bean
	public MongoDbFactory getMongoDbFactory() throws Exception{
		return new SimpleMongoDbFactory(new MongoClient("localhost", 27017), "local");
	}
	
	@Bean
	public MongoTemplate getMongoTemplate() throws Exception{
		MongoTemplate mongoTemplate = new MongoTemplate(getMongoDbFactory());
		return mongoTemplate;
	}
}
