package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.gcit.lms.domain.Author;
import com.gcit.lms.domain.Book;

public class AuthorDAO extends BaseDAO<Author> {

	public AuthorDAO(Connection conn) throws Exception {
		super(conn);		
	}

	public void create(Author author) throws Exception {
		int id = saveWithID("INSERT INTO tbl_author (authorName) VALUES(?)",
				new Object[] { author.getAuthorName() });
		if(id!=-1){
			author.setAuthorId(id);
		}
	}

	public void update(Author author) throws Exception {
		save("UPDATE tbl_author SET authorName = ? WHERE authorId = ?",
				new Object[] { author.getAuthorName(), author.getAuthorId() });

	}

	public void delete(Author author) throws Exception {
		save("DELETE FROM tbl_author WHERE authorId = ?",
				new Object[] { author.getAuthorId() });
		System.out.println("Deleted "+author );
	}

	public List<Author> readAll() throws Exception{
		@SuppressWarnings("unchecked")
		List<Author> result = (List<Author>) read("SELECT * FROM tbl_author ORDER BY authorName ASC", null);
		return result;
		
	}
	public List<Author> readPage(int pageNum) throws Exception{
		int start = 0;
		if(pageNum != 0){
			start = (pageNum*10);
			
		}		
		List<Author> result = (List<Author>) read("SELECT * FROM tbl_author ORDER BY authorName ASC LIMIT ?,10", new Object[] {start});
		return result;
		
	}

	public Author readOne(int authorId) throws Exception {
		@SuppressWarnings("unchecked")
		List<Author> authors = (List<Author>) read("SELECT * FROM tbl_author WHERE authorId = ?", new Object[] {authorId});
		if(authors!=null && authors.size()>0){
			return authors.get(0);
		}
		return null;
	}
	@Override
	public List<Author> search(String query, int pageNum) throws Exception{
		int start = 0;
		if(pageNum != 0){
			start = (pageNum*10);
			
		}
		String s = "%"+query+"%";
		List<Author> authors = (List<Author>) read("SELECT * FROM tbl_author WHERE authorName LIKE ?  ORDER BY authorName LIMIT ?, 10", new Object[] {s, start});
		
		return authors;
		
	}
	public int getPageCount(String query) throws Exception{
		String s = "%"+query+"%";
		List<Author> authors = (List<Author>) read("SELECT * FROM tbl_author WHERE authorName LIKE ? ", new Object[] {s});
		return Math.abs((authors.size()-1)/10 +1);
	}
	@Override
	public List<Author> extractData(ResultSet rs) throws Exception {
		List<Author> authors =  new ArrayList<Author>();
		BookDAO bDao = new BookDAO(getConnection());
		
		while(rs.next()){
			Author a = new Author();
			a.setAuthorId(rs.getInt("authorId"));
			a.setAuthorName(rs.getString("authorName"));
			List<Book> books = (List<Book>) bDao.readFirstLevel("SELECT * FROM tbl_book WHERE bookId In"
					+ "(SELECT bookId FROM tbl_book_authors WHERE authorId=?)", new Object[] {rs.getInt("authorId")});
			a.setBooks(books);
			authors.add(a);
		}
		return authors;
	}
	
	@Override
	public List<Author> extractDataFirstLevel(ResultSet rs) throws Exception {
		List<Author> authors =  new ArrayList<Author>();		
		while(rs.next()){
			Author a = new Author();
			a.setAuthorId(rs.getInt("authorId"));
			a.setAuthorName(rs.getString("authorName"));			
			authors.add(a);
		}
		return authors;
	}		
	
}
