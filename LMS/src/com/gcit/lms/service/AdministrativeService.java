package com.gcit.lms.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.gcit.lms.dao.AuthorDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.dao.BorrowerDAO;
import com.gcit.lms.dao.GenreDAO;
import com.gcit.lms.dao.LibraryBranchDAO;
import com.gcit.lms.dao.PublisherDAO;
import com.gcit.lms.domain.Author;
import com.gcit.lms.domain.Book;
import com.gcit.lms.domain.Borrower;
import com.gcit.lms.domain.Genre;
import com.gcit.lms.domain.LibraryBranch;
import com.gcit.lms.domain.Publisher;

public class AdministrativeService {

	public void createAuthor(Author author) throws Exception {
		ConnectionUtil c = new ConnectionUtil();
		Connection conn = c.createConnection();
		try {
			if (author == null || author.getAuthorName() == null
					|| author.getAuthorName().length() == 0
					|| author.getAuthorName().length() > 45) {
				throw new Exception(
						"Author Name cannot be empty or more than 45 Chars");
			} else {
				AuthorDAO adao = new AuthorDAO(conn);
				adao.create(author);
				conn.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
	}

	public void createPublisher(Publisher publisher) throws Exception {
		ConnectionUtil c = new ConnectionUtil();
		Connection conn = c.createConnection();
		try {
			PublisherDAO pdao = new PublisherDAO(conn);
			pdao.create(publisher);
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
	}
	public void createBorrower(Borrower bor) throws Exception {
		ConnectionUtil c = new ConnectionUtil();
		Connection conn = c.createConnection();
		try {
			BorrowerDAO bdao = new BorrowerDAO(conn);
			bdao.create(bor);
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
	}
	
	public void createBook(Book book, String[] authors, String pub, String[] genres) throws Exception {
		ConnectionUtil c = new ConnectionUtil();
		Connection conn = c.createConnection();
		try {
			BookDAO bdao = new BookDAO(conn);
			PublisherDAO pubDAO = new PublisherDAO(conn);
			AuthorDAO authDAO = new AuthorDAO(conn);
			GenreDAO genreDAO = new GenreDAO(conn);
			Publisher publisher = pubDAO.readOne(Integer.parseInt(pub));
			List<Author> auth = new ArrayList<Author>();
			List<Genre> gnr = new ArrayList<Genre>();
			for(String a:authors){
				Author toAdd = authDAO.readOne(Integer.parseInt(a));
				if(toAdd!=null){
					auth.add(toAdd);
				}
			}
			for(String g: genres){
				Genre toAdd = genreDAO.readOne(Integer.parseInt(g));
				if(toAdd!=null){
					gnr.add(toAdd);
				}
			}
			
			if(pub != null){
				book.setPublisher(publisher);				
			}
			book.setAuthors(auth);
			book.setGenres(gnr);
			bdao.create(book);
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
	}

	public List<Author> readAuthors(int pageNum) throws Exception {
		ConnectionUtil c = new ConnectionUtil();
		Connection conn = c.createConnection();
		AuthorDAO adao = new AuthorDAO(conn);
		return adao.readPage(pageNum);
	}
	
	public List<Author> searchAuthors(String query,int pageNum) throws Exception {
		ConnectionUtil c = new ConnectionUtil();
		Connection conn = c.createConnection();
		AuthorDAO adao = new AuthorDAO(conn);
		return adao.search(query, pageNum);
	}
	
	public int getAuthorPageCount(String query) throws Exception {
		ConnectionUtil c = new ConnectionUtil();
		Connection conn = c.createConnection();
		AuthorDAO dao = new AuthorDAO(conn);
		return dao.getPageCount(query);
	}
	public List<Publisher> readPublishers(int pageNum) throws Exception {
		ConnectionUtil c = new ConnectionUtil();
		Connection conn = c.createConnection();
		PublisherDAO pubDao = new PublisherDAO(conn);
		return pubDao.readPage(pageNum);
	}
	
	public List<Publisher> searchPublishers(String query,int pageNum) throws Exception {
		ConnectionUtil c = new ConnectionUtil();
		Connection conn = c.createConnection();
		PublisherDAO dao = new PublisherDAO(conn);
		return dao.search(query, pageNum);
	}
	public int getPublisherPageCount(String query) throws Exception {
		ConnectionUtil c = new ConnectionUtil();
		Connection conn = c.createConnection();
		PublisherDAO dao = new PublisherDAO(conn);
		return dao.getPageCount(query);
	}
	public List<Book> readBooks(int pageNum) throws Exception {
		ConnectionUtil c = new ConnectionUtil();
		Connection conn = c.createConnection();
		BookDAO bookDao = new BookDAO(conn);
		return bookDao.readPage(pageNum);
	}
	
	public List<Book> searchBooks(String query,int pageNum) throws Exception {
		ConnectionUtil c = new ConnectionUtil();
		Connection conn = c.createConnection();
		BookDAO dao = new BookDAO(conn);
		return dao.search(query, pageNum);
	}
	public int getBookPageCount(String query) throws Exception {
		ConnectionUtil c = new ConnectionUtil();
		Connection conn = c.createConnection();
		BookDAO dao = new BookDAO(conn);
		return dao.getPageCount(query);
	}
	public List<Borrower> readBorrowers(int pageNum) throws Exception {
		ConnectionUtil c = new ConnectionUtil();
		Connection conn = c.createConnection();
		BorrowerDAO borDao = new BorrowerDAO(conn);
		return borDao.readPage(pageNum);
	}
	
	public List<Borrower> searchBorrowers(String query,int pageNum) throws Exception {
		ConnectionUtil c = new ConnectionUtil();
		Connection conn = c.createConnection();
		BorrowerDAO dao = new BorrowerDAO(conn);
		return dao.search(query, pageNum);
	}
	public int getBorrowerPageCount(String query) throws Exception {
		ConnectionUtil c = new ConnectionUtil();
		Connection conn = c.createConnection();
		BorrowerDAO dao = new BorrowerDAO(conn);
		return dao.getPageCount(query);
	}
	
	public List<LibraryBranch> readLibraries(int pageNum) throws Exception {
		ConnectionUtil c = new ConnectionUtil();
		Connection conn = c.createConnection();
		LibraryBranchDAO libDao = new LibraryBranchDAO(conn);
		return libDao.readPage(pageNum);
	}
	
	public List<LibraryBranch> searchLibraries(String query,int pageNum) throws Exception {
		ConnectionUtil c = new ConnectionUtil();
		Connection conn = c.createConnection();
		LibraryBranchDAO dao = new LibraryBranchDAO(conn);
		return dao.search(query, pageNum);
	}
	
	public int getLibraryPageCount(String query) throws Exception {
		ConnectionUtil c = new ConnectionUtil();
		Connection conn = c.createConnection();
		LibraryBranchDAO dao = new LibraryBranchDAO(conn);
		return dao.getPageCount(query);
	}
	
	public List<Genre> readGenres(int pageNum) throws Exception {
		ConnectionUtil c = new ConnectionUtil();
		Connection conn = c.createConnection();
		GenreDAO genreDao = new GenreDAO(conn);
		return genreDao.readPage(pageNum);
	}
	
	public List<Genre> searchGenres(String query,int pageNum) throws Exception {
		ConnectionUtil c = new ConnectionUtil();
		Connection conn = c.createConnection();
		GenreDAO dao = new GenreDAO(conn);
		return dao.search(query, pageNum);
	}
	
	public int getGenrePageCount(String query) throws Exception {
		ConnectionUtil c = new ConnectionUtil();
		Connection conn = c.createConnection();
		GenreDAO dao = new GenreDAO(conn);
		return dao.getPageCount(query);
	}
	
	public Author readAuthor(int authorId) throws Exception {
		ConnectionUtil c = new ConnectionUtil();
		Connection conn = c.createConnection();
		AuthorDAO adao = new AuthorDAO(conn);
		return adao.readOne(authorId);
	}
	public Book readBook(int id) throws Exception {
		ConnectionUtil c = new ConnectionUtil();
		Connection conn = c.createConnection();
		BookDAO dao = new BookDAO(conn);
		return dao.readOne(id);
	}
	public Borrower readBorrower(int id) throws Exception {
		ConnectionUtil c = new ConnectionUtil();
		Connection conn = c.createConnection();
		BorrowerDAO dao = new BorrowerDAO(conn);
		return dao.readOne(id);
	}
	public LibraryBranch readLibrary(int id) throws Exception {
		ConnectionUtil c = new ConnectionUtil();
		Connection conn = c.createConnection();
		LibraryBranchDAO dao = new LibraryBranchDAO(conn);
		return dao.readOne(id);
	}
	public Publisher readPublisher(int id) throws Exception {
		ConnectionUtil c = new ConnectionUtil();
		Connection conn = c.createConnection();
		PublisherDAO dao = new PublisherDAO(conn);
		return dao.readOne(id);
	}
	public Genre readGenre(int id) throws Exception {
		ConnectionUtil c = new ConnectionUtil();
		Connection conn = c.createConnection();
		GenreDAO dao = new GenreDAO(conn);
		return dao.readOne(id);
	}

	public void deleteAuthor(Author author) throws Exception {
		ConnectionUtil c = new ConnectionUtil();
		Connection conn = c.createConnection();
		AuthorDAO adao = new AuthorDAO(conn);
		try {
			adao.delete(author);
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
	}

	public void updateAuthor(Author a) throws Exception {
		ConnectionUtil c = new ConnectionUtil();
		Connection conn = c.createConnection();
		try {
				AuthorDAO adao = new AuthorDAO(conn);
				adao.update(a);
				conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
		
	}
}
