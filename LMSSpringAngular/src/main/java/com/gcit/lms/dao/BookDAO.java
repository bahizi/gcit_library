package com.gcit.lms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.gcit.lms.domain.Author;
import com.gcit.lms.domain.Book;
import com.gcit.lms.domain.Genre;
import com.gcit.lms.domain.LibraryBranch;
import com.gcit.lms.domain.Publisher;

public class BookDAO extends BaseDAO<Book>{
	@Autowired
	PublisherDAO pubDAO;
	@Autowired
	GenreDAO genreDAO;
	@Autowired
	AuthorDAO authorDAO;
	@Autowired
	LibraryBranchDAO libDAO;

	private FirstLevelExtractor fle;
	private class FirstLevelExtractor implements ResultSetExtractor<List<Book>>{
		@Override
		public List<Book> extractData(ResultSet rs) {
			List<Book> books = new ArrayList<Book>();
			try {
				while(rs.next()){
					Book book = new Book();
					book.setBookId(rs.getInt("bookId"));
					book.setTitle(rs.getString("title"));
					book.setIsbn(rs.getString("isbn"));
					book.setGid(rs.getString("gid"));
					books.add(book);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return books;
		}

	}

	public BookDAO() {
		fle = new FirstLevelExtractor();
	}
	public void create(Book book) {
		Integer pubId = (book.getPublisher()!=null) ? book.getPublisher().getPublisherId() : null;
		int bookId =  saveWithID("INSERT INTO tbl_book (title, pubId,isbn,gid) VALUES(?,?,?,?)", new Object[] {book.getTitle(), pubId ,book.getIsbn(),book.getGid()});
		book.setBookId(bookId);
		for(Author author: book.getAuthors()){

			if(author.getAuthorId()<1){//if the author has no valid id
				if(author.getAuthorName()==null || author.getAuthorName().length()<2){
					break;
				}
				List<Author> authors = authorDAO.search(author.getAuthorName(), 0);
				if(authors.size()>0){//the author already exists
					author = authors.get(0);
				}
				else{
					authorDAO.create(author);
				}
			}
			template.update("INSERT INTO tbl_book_authors (bookId,authorId) VALUES(?,?)", new Object[]{book.getBookId(),author.getAuthorId()});
		}
		for(Genre genre: book.getGenres()){
			if(genre.getGenreId()<1){
				if(genre.getGenreName()==null || genre.getGenreName().length()==0){
					break;
				}
				List<Genre> genres = genreDAO.search(genre.getGenreName(), 0);
				if(genres.size()>0){
					genre = genres.get(0);
				}
				else{
					genreDAO.create(genre);					
				}
			}
			template.update("INSERT INTO tbl_book_genres (bookId,genre_id) VALUES(?,?)", new Object[]{book.getBookId(),genre.getGenreId()});
		}
		List<LibraryBranch> branches = libDAO.readAll();
		for(LibraryBranch branch: branches){
			template.update("INSERT INTO tbl_book_copies(branchId,bookId,noOfCopies) VALUES(?,?,?)", new Object[] {branch.getBranchId(),book.getBookId(),0});
		}
	}
	public void update(Book book) {
		System.out.println("Publisher: " +book.getPublisher());
		Publisher pub = book.getPublisher();
		Integer pubId = null;
		if(pub!=null){
			if(pub.getPublisherId()!=null){
				List<Publisher> pubs = pubDAO.search(pub.getPublisherName(), 0);
				if(pubs.size()>0){
					pub = pubs.get(0);
				}
				else{
					pubDAO.create(pub);
				}
				pubId =pub.getPublisherId();
			}
			else{
				pubDAO.create(pub);		
				pubId =pub.getPublisherId();
			}
		}
		
		template.update("UPDATE tbl_book SET title = ? , pubId = ? , isbn =?, gid = ? WHERE bookId = ?",
				new Object[] { book.getTitle(),pubId,book.getIsbn(), book.getGid(),book.getBookId()});	
		template.update("DELETE FROM tbl_book_authors WHERE bookId = ?",new Object[]{book.getBookId()});
		for(Author author: book.getAuthors()){
			if(author.getAuthorId()<1){//if the author has no valid id
				if(author.getAuthorName()==null || author.getAuthorName().length()<2){
					break;
				}
				List<Author> authors = authorDAO.search(author.getAuthorName(), 0);
				if(authors.size()>0){//the author already exists
					author = authors.get(0);
				}
				else{
					authorDAO.create(author);
				}
			}
			
			template.update("INSERT INTO tbl_book_authors (bookId,authorId) VALUES(?,?)", new Object[]{book.getBookId(),author.getAuthorId()});
		}
		template.update("DELETE FROM tbl_book_genres WHERE bookId = ?",new Object[]{book.getBookId()});
		for(Genre genre: book.getGenres()){
			if(genre.getGenreId()<1){
				if(genre.getGenreName()==null || genre.getGenreName().length()==0){
					break;
				}
				List<Genre> genres = genreDAO.search(genre.getGenreName(), 0);
				if(genres.size()>0){
					genre = genres.get(0);
				}
				else{
					genreDAO.create(genre);					
				}
			}
			template.update("INSERT INTO tbl_book_genres (bookId,genre_id) VALUES(?,?)", new Object[]{book.getBookId(),genre.getGenreId()});
		}


	}
	public void delete(Book book) {
		template.update("DELETE FROM tbl_book WHERE bookId = ?", new Object[]{book.getBookId()});
	}
	public Publisher getPublisher(int pubId) {
		return pubDAO.readOne(pubId);
	}

	public List<Author> getAuthors(int bookId){
		String sql = "SELECT * FROM tbl_book_authors JOIN tbl_author ON tbl_book_authors.authorId = tbl_author.authorId WHERE bookId = ?";
		List<Author> authors = (List<Author>) template.query(sql, new Object[] {bookId}, authorDAO);
		return authors;
	}
	public List<Genre> getGenres(int bookId){
		String sql = "SELECT * FROM tbl_book_genres JOIN tbl_genre ON tbl_book_genres.genre_id = tbl_genre.genre_id WHERE bookId = ?";
		List<Genre> genres = (List<Genre>) template.query(sql, new Object[] {bookId}, genreDAO);
		return genres;
	}
	public List<Book> readAll() {
		return  template.query("SELECT * FROM tbl_book ORDER by title", this);		
	}

	public Book readOne(int bookId) {
		List<Book> books =  template.query("SELECT * FROM tbl_book WHERE bookId = ?", new Object[] {bookId}, this);
		if(books!=null && books.size()>0){
			return books.get(0);
		}
		return null;
	}


	@Override
	public List<Book> extractData(ResultSet rs) throws SQLException {
		List<Book> books = new ArrayList<Book>();
		while(rs.next()){
			Book book = new Book();

			book.setBookId(rs.getInt("bookId"));
			book.setTitle(rs.getString("title"));
			book.setIsbn(rs.getString("isbn"));
			book.setGid(rs.getString("gid"));
			book.setPublisher(this.getPublisher(rs.getInt("pubId")));
			///potential infinite method calls below this point
			book.setAuthors(this.getAuthors(rs.getInt("bookId")));
			book.setGenres(this.getGenres(rs.getInt("bookId")));

			books.add(book);

		}
		return books;
	}
	public FirstLevelExtractor getFirstLevelExtractor(){
		return fle;
	}

	public List<Book> searchBookOnly(String query, int pageNum) {
		int start = 0;
		if(pageNum>1){
			start = (pageNum*10)+1;			 
		}
		String s = "%"+query+"%";
		List<Book> books =  template.query("SELECT * FROM tbl_book WHERE title LIKE ?  ORDER BY title LIMIT ?,10", new Object[] {s,start}, this);
		return books;
	}	
	@Override
	public List<Book> search(String query, int pageNum) {
		int start = 0;
		if(pageNum>1){
			start = (pageNum*10)+1;			 
		}
		String s = "%"+query+"%";



		String sql = "SELECT * FROM tbl_book " + 
				"LEFT JOIN tbl_book_authors ON tbl_book.bookId = tbl_book_authors.bookId "+
				"LEFT JOIN tbl_author ON tbl_book_authors.authorId = tbl_author.authorId " +
				"LEFT JOIN tbl_book_genres ON tbl_book.bookId  = tbl_book_genres.bookId "+ 
				"LEFT JOIN tbl_genre ON tbl_genre.genre_id  = tbl_book_genres.genre_id "+
				"WHERE title LIKE ? "+
				"OR authorName LIKE ? "+
				"OR genre_name LIKE ? "+
				"GROUP BY tbl_book.bookId "+
				"LIMIT ?, 10";
		List<Book> books = template.query(sql, new Object[] {s,s,s,start}, this);
		return books;
		//return searchBookOnly(query,pageNum);
	}
	public int getPageCount(String query) {
		String s = "%"+query+"%";
		List<Book> books =  template.query("SELECT * FROM tbl_book WHERE title LIKE ?", new Object[] {s}, this);
		return Math.max(0, Math.abs((books.size()-1)/10 +1) );
	}
	@Override
	public List<Book> readFirstLevel(String query, Object[] vals) {		
		List<Book> books =  template.query(query, vals, this.getFirstLevelExtractor());
		return books;
	}
}
