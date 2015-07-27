package com.gcit.lms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcit.lms.domain.Author;
import com.gcit.lms.domain.Book;

public class AuthorDAO extends BaseDAO<Author> {
	@Autowired
	BookDAO bookDAO;

	public void create(Author author) {
		int id = saveWithID("INSERT INTO tbl_author (authorName) VALUES(?)",
				new Object[] { author.getAuthorName() });
		if(id!=-1){
			author.setAuthorId(id);			
		}
	}

	public void update(Author author) {
		template.update("UPDATE tbl_author SET authorName = ? WHERE authorId = ?",
				new Object[] { author.getAuthorName(), author.getAuthorId() });

	}

	public void delete(Author author)  {
		template.update("DELETE FROM tbl_author WHERE authorId = ?",
				new Object[] { author.getAuthorId() });
		}

	public List<Author> readAll() {
		List<Author> result =  template.query("SELECT * FROM tbl_author ORDER BY authorName ASC", this);
		return result;
		
	}	

	public Author readOne(int authorId) {
		List<Author> authors =  template.query("SELECT * FROM tbl_author WHERE authorId = ?", new Object[] {authorId}, this);
		if(authors!=null && authors.size()>0){
			return authors.get(0);
		}
		return null;
	}
	@Override
	public List<Author> search(String query, int pageNum) {
		int start = 0;
		if(pageNum>1){
			start = (pageNum*10)+1;			 
		}
		String s = "%"+query+"%";
		List<Author> authors =  template.query("SELECT * FROM tbl_author WHERE authorName LIKE ?  ORDER BY authorName LIMIT ?, 10", new Object[] {s, start}, this);
		
		return authors;
		
	}
	public int getPageCount(String query) {
		String s = "%"+query+"%";
		List<Author> authors =  template.query("SELECT * FROM tbl_author WHERE authorName LIKE ? ", new Object[] {s}, this);
		return Math.max(0,Math.abs((authors.size()-1)/10 +1));
	}
	@Override
	public List<Author> extractData(ResultSet rs) throws SQLException {
		List<Author> authors =  new ArrayList<Author>();
		while(rs.next()){
			Author a = new Author();
			a.setAuthorId(rs.getInt("authorId"));
			a.setAuthorName(rs.getString("authorName"));
			List<Book> books = bookDAO.readFirstLevel("SELECT * FROM tbl_book WHERE bookId In"
					+ "(SELECT bookId FROM tbl_book_authors WHERE authorId=?)", new Object[] {rs.getInt("authorId")});
			a.setBooks(books);
			authors.add(a);
		}
		return authors;
	}

	@Override
	public List<Author> readFirstLevel(String query, Object[] vals) {
		// TODO Auto-generated method stub
		return null;
	}	
}
