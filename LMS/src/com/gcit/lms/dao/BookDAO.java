package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.gcit.lms.domain.Author;
import com.gcit.lms.domain.Book;
import com.gcit.lms.domain.Genre;
import com.gcit.lms.domain.LibraryBranch;
import com.gcit.lms.domain.Publisher;
@SuppressWarnings("unchecked")
public class BookDAO extends BaseDAO<Book>{

	public BookDAO(Connection conn) throws Exception {
		super(conn);
	}
	public void create(Book book) throws Exception{
		int bookId =  saveWithID("INSERT INTO tbl_book (title, pubId) VALUES(?,?)", new Object[] {book.getTitle(),book.getPublisher().getPublisherId()});
		book.setBookId(bookId);
		for(Author author: book.getAuthors()){
			save("INSERT INTO tbl_book_authors (bookId,authorId) VALUES(?,?)", new Object[]{book.getBookId(),author.getAuthorId()});
		}
		for(Genre genre: book.getGenres()){
			save("INSERT INTO tbl_book_genres (bookId,genre_id) VALUES(?,?)", new Object[]{book.getBookId(),genre.getGenreId()});
		}
		LibraryBranchDAO libDAO = new LibraryBranchDAO(getConnection());
		List<LibraryBranch> branches = libDAO.readAll();
		for(LibraryBranch branch: branches){
			save("INSERT INTO tbl_book_copies(branchId,bookId,noOfCopies) VALUES(?,?,?)", new Object[] {branch.getBranchId(),book.getBookId(),0});
		}
	}
	public void update(Book book) throws Exception{
		save("UPDATE tbl_book SET title = ?, pubId = ? WHERE bookId = ?",
				new Object[] { book.getTitle(),book.getPublisher().getPublisherId(),book.getBookId()});
		GenreDAO genreDAO = new GenreDAO(getConnection());
		AuthorDAO authorDAO = new AuthorDAO(getConnection());		
	}
	public void delete(Book book) throws Exception{
		save("DELETE FROM tbl_book WHERE bookId = ?", new Object[]{book.getBookId()});
		//TODO : SET UP CASCADING ON DELETION FOR BOOKID IN tbl_book_authors and tbl_book_genres

		save("DELETE FROM tbl_book_authors WHERE bookId =?", new Object[] {book.getBookId()});		
		save("DELETE FROM tbl_book_genres WHERE bookId =?", new Object[] {book.getBookId()});		
	}
	public Publisher getPublisher(int pubId) throws Exception{
		PublisherDAO pubDAO = new PublisherDAO(getConnection());
		return pubDAO.readOne(pubId);
	}

	public List<Author> getAuthors(int bookId) throws Exception{
		AuthorDAO authorDAO = new AuthorDAO(getConnection());
		String sql = "SELECT * FROM tbl_book_authors JOIN tbl_author ON tbl_book_authors.authorId = tbl_author.authorId WHERE bookId = ?";
		List<Author> authors = (List<Author>) authorDAO.read(sql, new Object[] {bookId});
		return authors;
	}
	public List<Genre> getGenres(int bookId) throws Exception{
		GenreDAO genreDAO = new GenreDAO(getConnection());
		String sql = "SELECT * FROM tbl_book_genres JOIN tbl_genre ON tbl_book_genres.genre_id = tbl_genre.genre_id WHERE bookId = ?";
		List<Genre> genres = (List<Genre>) genreDAO.read(sql, new Object[] {bookId});
		return genres;
	}
	public List<Book> readAll() throws Exception{
		return (List<Book>) read("SELECT * FROM tbl_book ORDER by title", null);		
	}

	public Book readOne(int bookId) throws Exception {
		List<Book> books = (List<Book>) read("SELECT * FROM tbl_book WHERE bookId = ?", new Object[] {bookId});
		if(books!=null && books.size()>0){
			return books.get(0);
		}
		return null;
	}


	@Override
	public List<Book> extractData(ResultSet rs) throws Exception {
		List<Book> books = new ArrayList<Book>();
		while(rs.next()){
			Book book = new Book();

			book.setBookId(rs.getInt("bookId"));
			book.setTitle(rs.getString("title"));
			book.setPublisher(this.getPublisher(rs.getInt("pubId")));
			///potential infinite method calls below this point
			book.setAuthors(this.getAuthors(rs.getInt("bookId")));
			book.setGenres(this.getGenres(rs.getInt("bookId")));

			books.add(book);

		}
		return books;
	}
	@Override
	public List<Book> extractDataFirstLevel(ResultSet rs) throws Exception {
		List<Book> books = new ArrayList<Book>();

		while(rs.next()){
			Book book = new Book();
			book.setBookId(rs.getInt("bookId"));
			book.setTitle(rs.getString("title"));
			books.add(book);
		}
		return books;
	}
	@Override
	public List<Book> readPage(int pageNum) throws Exception {
		int start = 0;
		if(pageNum != 0){
			start = (pageNum*10);
			
		}		
		List<Book> result = (List<Book>) read("SELECT * FROM tbl_book ORDER BY title ASC LIMIT ?,10", new Object[] {start});
		return result;
	}
	@Override
	public List<Book> search(String query, int pageNum) throws Exception {
		int start = 0;
		if(pageNum != 0){
			start = (pageNum*10);
			
		}
		String s = "%"+query+"%";
		List<Book> books = (List<Book>) read("SELECT * FROM tbl_book WHERE title LIKE ?  ORDER BY title LIMIT ?,10", new Object[] {s,start});
		return books;
	}
	public int getPageCount(String query) throws Exception{
		String s = "%"+query+"%";
		List<Book> books = (List<Book>) read("SELECT * FROM tbl_book WHERE title LIKE ?", new Object[] {s});
		return Math.abs((books.size()-1)/10 +1);
	}
}
