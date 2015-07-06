package com.gcit.lms.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gcit.lms.dao.AuthorDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.dao.BookLoanDAO;
import com.gcit.lms.dao.BorrowerDAO;
import com.gcit.lms.dao.GenreDAO;
import com.gcit.lms.dao.LibraryBranchDAO;
import com.gcit.lms.dao.PublisherDAO;
import com.gcit.lms.domain.Author;
import com.gcit.lms.domain.Book;
import com.gcit.lms.domain.BookLoan;
import com.gcit.lms.domain.Borrower;
import com.gcit.lms.domain.Genre;
import com.gcit.lms.domain.LibraryBranch;
import com.gcit.lms.domain.Publisher;

public class AdminService extends BaseService{

	public AdminService(){
		super();
		adminMenu();

	}
	private void adminMenu(){
		ArrayList<String> options = new ArrayList<String>();
		ArrayList<String> actions = new ArrayList<String>();
		options.add("Books");
		options.add("Authors");
		options.add("Publishers");
		options.add("Library Branches");
		options.add("Borrowers");
		options.add("Book Loan Due Dates");
		actions.add("Exit to main menu");
		System.out.println("What would you like to manage?");
		int choice= this.getChoiceNumber(options,actions);
		switch(choice){
		case -1:
			//exit;
			System.out.println("Exiting to the main menu");
			break;
		case 1:
			//manage books and authors;
			bookMain();
			break;
		case 2:
			//manage publishers;
			authorMain();
			break;
		case 3:
			//manage publishers;
			publisherMain();
			break;
		case 4:
			//manage library branches;
			libraryBranchMain();
			break;
		case 5:
			//manage borrowers;
			borrowerMain();
			break;
		case 6:
			//manage book loans
			BookLoan loan = getLoan();
			if(loan!=null){
				editLoan(loan);
			}
			
			break;
		default:
			//this case should never happen. if it does, something is wrong with getInputInt
			System.err.println("Invalid input.");
			System.exit(1);
		}
	}

	//display the possible actions on a book
	//allow books with no author but no authors with no books
	private void bookMain(){
		ArrayList<String> options = new ArrayList<String>();
		ArrayList<String> actions = new ArrayList<String>();
		options.add("Add a book and its author(s)");
		options.add("Edit a book's details");
		options.add("Delete a book");
		actions.add("Go one step up in the menu");
		System.out.println("What would you like to do with the books and authors?");
		int action= this.getChoiceNumber(options, actions);
		switch(action){
		case -1:
			//Exit to previous menu
			adminMenu();
			break;
		case 1:
			//add book and authors
			addBook();
			break;
		case 2:
			System.out.println("Here are all the books in the system:");
			Book toEdit = this.showAllBooks();
			if(toEdit!=null){
				editBook(toEdit);
			}
			break;
		case 3:
			System.out.println("Here are all the books in the system:");
			Book toDelete = this.showAllBooks();
			if(toDelete!=null){
				deleteBook(toDelete);
			}
			break;
		default:
			//This should never happen.
			System.err.println("Invalid input.");
		}
	}

	//receive the necessary information to add a book	
	private void addBook(){
		Connection conn;
		try {
			conn = getConnection();
			Book book = new Book();
			BookDAO bookDAO = new BookDAO(conn);
			AuthorDAO authorDAO = new AuthorDAO(conn);
			PublisherDAO pubDAO = new PublisherDAO(conn);
			GenreDAO genreDAO = new GenreDAO(conn);		
			System.out.println("What is the title of the new book? [N/A to cancel operation]");		
			String title = getInputString();
			if(title.equals("N/A")){
				return;
			}
			List<Author> allAuthors = authorDAO.readAll();
			List<Author> bookAuthors = new ArrayList<Author>();
			List<Genre> allGenres = genreDAO.readAll();
			List<Genre> bookGenres = new ArrayList<Genre>();
			List<Publisher> publishers = pubDAO.readAll();
			Publisher bookPublisher = new Publisher();
			ArrayList<String> questions = new ArrayList<String>();
			ArrayList<String> actions = new ArrayList<String>();
			actions.add("Skip this detail");
			actions.add("Cancel whole operation");
			questions.add("Yes");
			questions.add("No");
			boolean more= true;

			do {
				System.out.println("Choose Author");
				actions.set(0,"Skip adding an author to this book");
				int author = getChoiceNumber (allAuthors,actions);
				if (author == -1){
					more = false;
				}
				else if(author == -2){
					return;
				}
				else{
					bookAuthors.add(allAuthors.get(author-1));
					allAuthors.remove(author-1);
					System.out.println("More authors?");
					displayOptions(questions);
					int next = getInputInt(1,2);
					more = next ==1;
				}

			} while (more);
			more = true;
			do {
				System.out.println("Choose Genre");
				actions.set(0,"Skip adding a genre to this book");
				int genre = getChoiceNumber (allGenres,actions);
				if (genre == -1){
					more = false;
				}
				else if(genre == -2){
					return;
				}
				else{
					bookGenres.add(allGenres.get(genre-1));
					allGenres.remove(genre-1);
					System.out.println("More Genres?");
					displayOptions(questions);
					int next = getInputInt(1,2);
					more = next ==1;
				}

			} while (more);
			System.out.println("Choose Publisher");
			actions.set(0,"Skip adding a publisher to this book");
			int pub = getChoiceNumber (publishers,actions);
			if (pub == -1){
				more = false;
			}
			else if(pub == -2){
				return;
			}
			else{
				bookPublisher = publishers.get(pub-1);
			}
			book.setTitle(title);
			book.setAuthors(bookAuthors);
			book.setGenres(bookGenres);
			book.setPublisher(bookPublisher);
			bookDAO.create(book);
			conn.commit();
			conn.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void editBook(Book book){
		ArrayList<String> options = new ArrayList<String>();
		ArrayList<String> actions = new ArrayList<String>();
		options.add("Change Title");
		options.add("Add Author");
		options.add("Remove Author");
		options.add("Add Genre");
		options.add("Remove Genre");
		options.add("Change Publisher");
		options.add("Remove Publisher");
		actions.add("Cancel");
		int choice = getChoiceNumber(options,actions);
		switch(choice){
		case -1:
			break;
		case 1:
			System.out.println("Input new title [N/A to cancel]");
			String in = this.getInputString();
			if(!in.equals("N/A")){
				book.setTitle(in);
			}
		case 2:
			addBookAuthor(book);
			editBook(book);
			break;
		case 3:
			deleteBookAuthor(book);
			editBook(book);
			break;
		case 4:
			addBookGenre(book);
			editBook(book);
			break;
		case 5:
			deleteBookGenre(book);
			editBook(book);
			break;
		case 6:
			editBookPublisher(book);
			editBook(book);
			break;
		case 7:
			deleteBookPublisher(book);
			editBook(book);
			break;
		default:
			System.out.println("Coming soon");
			break;

		}
	}
	private void addBookAuthor(Book book){
		Connection conn;
		try {
			conn = getConnection();
			AuthorDAO authorDAO = new AuthorDAO(conn);
			BookDAO bookDAO = new BookDAO(conn);
			List<Author> allAuthors = (List<Author>) authorDAO.read("SELECT * FROM tbl_author WHERE tbl_author.authorId NOT IN (SELECT tbl_book_authors.authorId FROM tbl_book_authors WHERE bookId=?)", new Object[]{book.getBookId()});
			ArrayList<Author> newAuthors = new ArrayList<Author>();
			ArrayList<String> actions= new ArrayList<String>();
			actions.add("Cancel");

			ArrayList<String> questions= new ArrayList<String>();
			questions.add("Yes");
			questions.add("No");
			boolean more = true;			
			do {
				System.out.println("Choose Author");
				int author = getChoiceNumber (allAuthors,actions);
				if (author == -1){
					more = false;
				}
				else{
					newAuthors.add(allAuthors.get(author-1));
					allAuthors.remove(author-1);
					System.out.println("Add more authors?");
					displayOptions(questions);
					int next = getInputInt(1,2);
					more = next ==1;
				}
			} while (more);
			for(Author auth: newAuthors){
				bookDAO.save("INSERT INTO tbl_book_authors (bookId,authorId) VALUES(?,?)", new Object[]{book.getBookId(),auth.getAuthorId()});
			}
			conn.commit();

			conn.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private void deleteBookAuthor(Book book){
		Connection conn;
		try {
			conn = getConnection();
			AuthorDAO authorDAO = new AuthorDAO(conn);
			BookDAO bookDAO = new BookDAO(conn);
			List<Author> allAuthors = (List<Author>) authorDAO.read("SELECT * FROM tbl_author JOIN tbl_book_authors ON tbl_author.authorId = tbl_book_authors.authorId WHERE bookId = ?", new Object[]{book.getBookId()});
			ArrayList<Author> toDelete = new ArrayList<Author>();
			ArrayList<String> actions= new ArrayList<String>();
			actions.add("Cancel");

			ArrayList<String> questions= new ArrayList<String>();
			questions.add("Yes");
			questions.add("No");
			boolean more = true;			
			do {
				System.out.println("Choose Author");
				int author = getChoiceNumber (allAuthors,actions);
				if (author == -1){
					more = false;
				}
				else{
					toDelete.add(allAuthors.get(author-1));
					allAuthors.remove(author-1);
					System.out.println("Delete more authors?");
					displayOptions(questions);
					int next = getInputInt(1,2);
					more = next ==1;
				}

			} while (more);
			List<Author> curAuthors = book.getAuthors();
			for(Author auth: toDelete){
				authorDAO.save("DELETE FROM tbl_book_authors WHERE authorId = ? AND bookId =?", new Object[]{auth.getAuthorId(),book.getBookId()});
			}
			conn.commit();
			conn.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private void addBookGenre(Book book){
		Connection conn;
		try {
			conn = getConnection();
			GenreDAO genreDAO = new GenreDAO(conn);
			BookDAO bookDAO = new BookDAO(conn);
			List<Genre> allGenres = (List<Genre>) genreDAO.read("SELECT * FROM tbl_author WHERE tbl_author.authorId NOT IN (SELECT tbl_book_authors.authorId FROM tbl_book_authors WHERE bookId=?)", new Object[]{book.getBookId()});
			ArrayList<Genre> newGenres = new ArrayList<Genre>();
			ArrayList<String> actions= new ArrayList<String>();
			actions.add("Cancel");

			ArrayList<String> questions= new ArrayList<String>();
			questions.add("Yes");
			questions.add("No");
			boolean more = true;			
			do {
				System.out.println("Choose Genre");
				int author = getChoiceNumber (allGenres,actions);
				if (author == -1){
					more = false;
				}
				else{
					newGenres.add(allGenres.get(author-1));
					allGenres.remove(author-1);
					if(allGenres.isEmpty()){
						more = false;
						break;

					}
					System.out.println("Add more genres?");
					displayOptions(questions);
					int next = getInputInt(1,2);
					more = next ==1;
				}
			} while (more);
			for(Genre genre: newGenres){
				bookDAO.save("INSERT INTO tbl_book_genres (bookId,genre_id) VALUES(?,?)", new Object[]{book.getBookId(),genre.getGenreId()});
			}
			conn.commit();			
			conn.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private void deleteBookGenre(Book book){
		Connection conn;
		try {
			conn = getConnection();
			GenreDAO authorDAO = new GenreDAO(conn);
			BookDAO bookDAO = new BookDAO(conn);
			List<Genre> allGenres = (List<Genre>) authorDAO.read("SELECT * FROM tbl_genre JOIN tbl_book_genres ON tbl_genre.genre_id = tbl_book_genres.genre_id WHERE bookId = ?", new Object[]{book.getBookId()});
			ArrayList<Genre> toDelete = new ArrayList<Genre>();
			ArrayList<String> actions= new ArrayList<String>();
			actions.add("Cancel");

			ArrayList<String> questions= new ArrayList<String>();
			questions.add("Yes");
			questions.add("No");
			boolean more = true;			
			do {
				System.out.println("Choose Genre");
				int genre = getChoiceNumber (allGenres,actions);
				if (genre == -1){
					more = false;
				}
				else{
					toDelete.add(allGenres.get(genre-1));
					allGenres.remove(genre-1);
					if(allGenres.isEmpty()){
						more = false;
						break;
					}
					System.out.println("Delete more genres?");
					displayOptions(questions);
					int next = getInputInt(1,2);
					more = next ==1 ;
				}

			} while (more);
			List<Author> curAuthors = book.getAuthors();
			for(Genre genre: toDelete){
				authorDAO.save("DELETE FROM tbl_book_genres WHERE genre_id = ? AND bookId =?", new Object[]{genre.getGenreId(),book.getBookId()});
			}
			conn.commit();
			conn.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private void editBookPublisher(Book book){
		Connection conn;
		try {
			conn = getConnection();
			PublisherDAO pubDAO = new PublisherDAO(conn);
			BookDAO bookDAO = new BookDAO(conn);
			Publisher curPublisher = null;
			List<Publisher> allPublisher = (List<Publisher>) pubDAO.read("SELECT * FROM tbl_publisher WHERE publisherId NOT IN (SELECT pubId FROM tbl_book WHERE bookId = ?)", new Object[] {book.getBookId()});
			ArrayList<String> actions = new ArrayList<String>();
			actions.add("Cancel");
			if(book.getPublisher()==null){
				List<Publisher> pubs = (List<Publisher>) pubDAO.read("SELECT * FROM tbl_publisher JOIN tbl_book ON tbl_publisher.publisherId = tbl_book.pubId WHERE bookId=?", new Object[]{book.getBookId()});
				if(pubs !=null & pubs.size()>0){
					curPublisher = pubs.get(0);
				}
			}
			System.out.println("Pick a new publisher");
			int choice = getChoiceNumber(allPublisher,actions);
			if(choice>0 && choice<=allPublisher.size()){
				book.setPublisher(allPublisher.get(choice-1));
			}
			bookDAO.update(book);
			conn.commit();
			conn.close();			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
	private void deleteBookPublisher(Book book){
		Integer pubId = null;
		try {
			Connection conn = getConnection();
			BookDAO bookDAO = new BookDAO(conn);
			Publisher curPublisher = book.getPublisher();
			if(curPublisher==null){
				curPublisher = new Publisher();				
			}
			curPublisher.setPublisherId(pubId);
			book.setPublisher(curPublisher);
			bookDAO.update(book);
			conn.commit();
			conn.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}
	private void deleteBook(Book book){
		try {
			Connection conn = getConnection();
			BookDAO bookDAO = new BookDAO(conn);
			bookDAO.delete(book);
			conn.commit();
			conn.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private Book showAllBooks( ){
		Connection conn;
		Book book = null;
		try {
			conn = getConnection();
			BookDAO bookDAO = new BookDAO(conn);
			List<Book> books = bookDAO.readAll();
			ArrayList<String> actions = new ArrayList<String>();
			actions.add("Cancel");
			int choice = getChoiceNumber(books,actions);
			if(choice>0 && choice<=books.size()){
				book = books.get(choice-1);
			}


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return book;
	}

	private void authorMain(){
		ArrayList<String> options = new ArrayList<String>();
		ArrayList<String> actions = new ArrayList<String>();
		options.add("Add a new author");
		options.add("Edit an author's name");
		options.add("Delete an author");
		actions.add("Go Back");
		int choice = this.getChoiceNumber(options, actions);
		switch(choice){
		case -1:
			break;
		case 1:
			addAuthor();
			authorMain();
			break;
		case 2:
			Author toEdit = showAllAuthors();
			if(toEdit!=null){
				editAuthor(toEdit);	
			}
			authorMain();
			break;
		case 3: 
			Author toDelete = showAllAuthors();
			System.out.println("DELETING "+toDelete);
			if(toDelete!=null){
				deleteAuthor(toDelete);	
			}
			else{
				System.out.println("Empty author");
			}
			authorMain();
			break;
		default:
			System.err.println("Invalid input");
			break;		
		}
	}
	private void addAuthor(){
		System.out.println("Enter new author's name: [N/A to cancel]");
		String name = getInputString();
		if(!name.equals("N/A")){
			Author author = new Author();
			author.setAuthorName(name);
			try {
				Connection conn = getConnection();
				try{
					AuthorDAO authorDAO = new AuthorDAO(conn);
					authorDAO.create(author);
					conn.commit();
					conn.close();
				}
				catch(Exception e){
					conn.rollback();
				}

			} catch (Exception e) {
				System.err.println("Error while connecting to Database");
				e.printStackTrace();
			}
		}
	}
	private Author showAllAuthors(){
		Author author = null;
		try {
			Connection conn = getConnection();
			AuthorDAO authorDAO = new AuthorDAO(conn);
			List<Author> allAuthors = authorDAO.readAll();
			ArrayList<String> actions = new ArrayList<String>();
			actions.add("Cancel");
			int choice = getChoiceNumber(allAuthors, actions);
			if(choice>0 && choice <=allAuthors.size()){
				author = allAuthors.get(choice-1);
			}			
			conn.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return author;
	}

	private void editAuthor(Author author){
		try {
			Connection conn = getConnection();
			System.out.println("Enter new name for "+author+" :[N/A to cancel]");
			String name = getInputString();
			if(!name.equals("N/A")){
				try{
					System.out.println("Committed!!!!!!!!!!!!!!!!!!!");
					AuthorDAO authorDAO = new AuthorDAO(conn);
					author.setAuthorName(name);
					authorDAO.update(author);
					conn.commit();
					conn.close();
				}
				catch(Exception e){
					conn.rollback();
					conn.close();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println("Error while connecting to database");
			e.printStackTrace();
		}

	}
	private void deleteAuthor(Author author){
		try {
			Connection conn = getConnection();			
			try{
				AuthorDAO authorDAO = new AuthorDAO(conn);
				BookDAO bookDAO = new BookDAO(conn);
				List<Book> authorBooks = (List<Book>) bookDAO.read("SELECT * FROM tbl_book WHERE tbl_book.bookId IN (SELECT tbl_book_authors.bookId FROM tbl_book_authors WHERE authorId = ?)" , new Object[] {author.getAuthorId()});
				if(authorBooks.size()>0){
					ArrayList<String> answers = new ArrayList<String>();
					answers.add("No, nevermind");
					answers.add("Yes, delete this author");
					System.out.println(author+" has (co)authored "+authorBooks.size()+" books in our records");
					System.out.println("Are you sure you still want to delete? Some books may remain with no author");
					displayOptions(answers);
					int in = getInputInt(1,2);
					if(in ==1 ){
						return;
					}
				}
				System.out.println("here");
				
				authorDAO.delete(author);
				conn.commit();
				conn.close();			
			}
			catch(Exception e){
				conn.rollback();
				conn.close();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println("Error while connecting to database");
			e.printStackTrace();
		}

	}
	//diplay the possible effects to a publisher
	private void publisherMain(){
		ArrayList<String> options = new ArrayList<String>();
		options.add("Add new a new publisher");
		options.add("Edit stored publishers");
		options.add("Delete a publisher");
		options.add("Go back up the menu");
		System.out.println("What would like to do with publishers?");
		displayOptions(options);
		int action = getInputInt(1,options.size());
		switch(action){
		case 1:
			this.addPublisher();
			publisherMain();
			break;
		case 2:
			Publisher toEdit = showAllPublishers();
			if(toEdit!=null){
				editPublisher(toEdit);
			}
			publisherMain();
			break;
		case 3:
			Publisher toDelete = showAllPublishers();
			if(toDelete!=null){
				deletePublisher(toDelete);
			}
			publisherMain();
			break;
		default:
			System.err.println("Invalid Input");
			System.exit(1);
		}

	}

	private Publisher showAllPublishers(){
		Publisher pub = null;
		try {
			Connection conn = getConnection();
			PublisherDAO pubDAO = new PublisherDAO(conn);
			List<Publisher> allPublishers = pubDAO.readAll();
			ArrayList<String> actions = new ArrayList<String>();
			actions.add("Cancel");
			int choice = getChoiceNumber(allPublishers, actions);
			if(choice>0 && choice <=allPublishers.size()){
				pub = allPublishers.get(choice-1);
			}			
			conn.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return pub;
	}
	//gather the necessary information to add a publisher
	private void addPublisher(){
		try {
			Connection conn = getConnection();
			try{
				PublisherDAO pubDAO = new PublisherDAO(conn);
				Publisher toAdd = new Publisher();
				System.out.println("What is the Publisher's name?");
				String pubName= getInputString();
				System.out.println("What is the Publisher's address?");
				String pubAddress=getInputString();
				System.out.println("What is the Publisher's phone number?");
				String pubPhone=getInputString();
				toAdd.setPublisherName(pubName);
				toAdd.setPublisherAddress(pubAddress);
				toAdd.setPublisherPhone(pubPhone);
				pubDAO.create(toAdd);
				conn.commit();
				conn.close();				
			}
			catch(Exception e){
				conn.rollback();
				conn.close();
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}
	private void editPublisher(Publisher publisher){
		try {
			Connection conn = getConnection();
			System.out.println("Enter Publisher's new name:");
			String name = getInputString();
			System.out.println("Enter Publisher's new address:");
			String address = getInputString();
			System.out.println("Enter Publisher's new phone:");
			String phone = getInputString();
			try{
				PublisherDAO pubDAO = new PublisherDAO(conn);
				publisher.setPublisherName(name);
				publisher.setPublisherAddress(address);
				publisher.setPublisherPhone(phone);
				pubDAO.update(publisher);
				conn.commit();
				conn.close();
			}
			catch(Exception e){
				conn.rollback();
				conn.close();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println("Error while connecting to database");
			e.printStackTrace();
		}	
	}
	private void deletePublisher(Publisher publisher){
		try {
			Connection conn = getConnection();			
			try{
				PublisherDAO pubDAO = new PublisherDAO(conn);
				BookDAO bookDAO = new BookDAO(conn);
				List<Book> publisherBooks = (List<Book>) bookDAO.read("SELECT * FROM tbl_book WHERE pubId = ?" , new Object[] {publisher.getPublisherId()});
				if(publisherBooks.size()>0){
					ArrayList<String> answers = new ArrayList<String>();
					answers.add("No, nevermind");
					answers.add("Yes, delete this publisher");
					System.out.println(publisher+" has published "+publisherBooks.size()+" books in our records");
					System.out.println("Are you sure you still want to delete? Some books may remain with no publisher");
					displayOptions(answers);
					int in = getInputInt(1,2);
					if(in ==1){
						return;
					}					
					bookDAO.save("UPDATE tbl_book SET pubId = ? WHERE pubId = ?", new Object[]{null, publisher.getPublisherId()});					
				}
				pubDAO.delete(publisher);
				conn.commit();
				conn.close();			
			}
			catch(Exception e){
				conn.rollback();
				conn.close();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println("Error while connecting to database");
			e.printStackTrace();
		}
	}
	//display the possible effects to a library branch
	private void libraryBranchMain(){
		ArrayList<String> options = new ArrayList<String>();
		options.add("Add a new Library Branch");
		options.add("Edit stored libraries");
		options.add("Delete a Library Branch");
		options.add("Go back up the menu");
		System.out.println("What would like to do with libraries?");
		displayOptions(options);
		int action = getInputInt(1,options.size());
		switch(action){
		case 1:
			addLibrary();
			libraryBranchMain();
			break;
		case 2:
			LibraryBranch toEdit = showAllBranches();
			if(toEdit!=null){
				editBranch(toEdit);
			}
			libraryBranchMain();
			break;
		case 3:
			LibraryBranch toDelete = showAllBranches();
			if(toDelete!=null){
				deleteBranch(toDelete);
			}
			libraryBranchMain();
			break;
		case 4:
			//go back to the admin main menu
			adminMenu();
			break;
		default:
			System.err.println("Invalid Input");
			System.exit(1);
		}
	}
	
	private LibraryBranch showAllBranches(){
		LibraryBranch branch = null;
		try {
			Connection conn = getConnection();
			LibraryBranchDAO libDAO = new LibraryBranchDAO(conn);
			List<LibraryBranch> allBranches = libDAO.readAll();
			ArrayList<String> actions = new ArrayList<String>();
			actions.add("Cancel");
			int choice = getChoiceNumber(allBranches, actions);
			if(choice>0 && choice <=allBranches.size()){
				branch = allBranches.get(choice-1);
			}			
			conn.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return branch;
	}
	
	private void addLibrary(){
		try {
			Connection conn = getConnection();
			try{
				LibraryBranchDAO libDAO = new LibraryBranchDAO(conn);
				LibraryBranch toAdd = new LibraryBranch();
				System.out.println("What is the new branch's name?");
				String libName= getInputString();
				System.out.println("What is the Publisher's address?");				
				String libAddress=getInputString();
				toAdd.setBranchName(libName);
				toAdd.setAddress(libAddress);
				libDAO.create(toAdd);
				conn.commit();
				conn.close();				
			}
			catch(Exception e){
				conn.rollback();
				conn.close();
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}
	private void editBranch(LibraryBranch branch){
		try {
			Connection conn = getConnection();
			System.out.println("Enter Branch's new name:");
			String name = getInputString();
			System.out.println("Enter Publisher's new address:");
			String address = getInputString();			
			try{
				LibraryBranchDAO libDAO = new LibraryBranchDAO(conn);
				branch.setBranchName(name);
				branch.setAddress(address);
				libDAO.update(branch);
				conn.commit();
				conn.close();
			}
			catch(Exception e){
				conn.rollback();
				conn.close();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println("Error while connecting to database");
			e.printStackTrace();
		}	
	}
	private void deleteBranch(LibraryBranch branch){
		try {
			Connection conn = getConnection();			
			try{
				LibraryBranchDAO libDAO = new LibraryBranchDAO(conn);
				BookLoanDAO loanDAO = new BookLoanDAO(conn);
				List<BookLoan> branchLoans = (List<BookLoan>) loanDAO.read("SELECT * FROM tbl_book_loans WHERE branchId = ? AND dateIn IS NULL" , new Object[] {branch.getBranchId()});
				if(branchLoans.size()>0){
					ArrayList<String> answers = new ArrayList<String>();
					answers.add("No, nevermind");
					answers.add("Yes, delete this branch");
					System.out.println("This branch has "+branchLoans.size()+" books that are not returned");
					System.out.println("Are you sure you still want to delete? These books will be lost");
					displayOptions(answers);
					int in = getInputInt(1,2);
					if(in ==1){
						return;
					}										
				}
				libDAO.delete(branch);
				conn.commit();
				conn.close();			
			}
			catch(Exception e){
				conn.rollback();
				conn.close();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println("Error while connecting to database");
			e.printStackTrace();
		}
	}


	//display the possible actions on a borrower
	//let the user choose an action and then call the appropriate method
	//when done, display this menu again, unless user chooses to exit
	private void borrowerMain(){
		ArrayList<String> options = new ArrayList<String>();
		options.add("Register a new borrower");
		options.add("Edit stored borrowers");
		options.add("Delete a stored borrower");
		options.add("Go back up the menu");
		System.out.println("What would like to do with borrowers?");
		displayOptions(options);
		int action = getInputInt(1,options.size());
		switch(action){
		case 1:
			addBorrower();
			borrowerMain();
			break;
		case 2:
			Borrower toEdit = showAllBorrowers();
			if(toEdit!=null){
				System.out.println("EDITING "+toEdit);
				editBorrower(toEdit);
			}
			borrowerMain();
			break;
		case 3:
			Borrower toDelete = showAllBorrowers();
			if(toDelete!=null){
				deleteBorrower(toDelete);
			}
			borrowerMain();
			break;			
		case 4:
			//go back to the admin main menu
			adminMenu();
		default:
			System.err.println("Invalid Input");
			System.exit(1);
		}
	}
	
	private Borrower showAllBorrowers(){
		Borrower bor = null;
		try {
			Connection conn = getConnection();
			BorrowerDAO borDAO = new BorrowerDAO(conn);
			List<Borrower> allBorrowers = borDAO.readAll();
			ArrayList<String> actions = new ArrayList<String>();
			actions.add("Cancel");
			int choice = getChoiceNumber(allBorrowers, actions);
			if(choice>0 && choice <=allBorrowers.size()){
				bor = allBorrowers.get(choice-1);
			}			
			conn.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bor;
	}

	private void addBorrower(){
		try {
			Connection conn = getConnection();
			try{
				BorrowerDAO borDAO = new BorrowerDAO(conn);
				Borrower toAdd = new Borrower();
				System.out.println("What is the borrower's name?");
				String borName= getInputString();
				System.out.println("What is the borrower's address?");				
				String borAddress=getInputString();
				System.out.println("What is the borrower's phone address?");				
				String borPhone=getInputString();
				toAdd.setName(borName);
				toAdd.setAddress(borAddress);
				toAdd.setPhone(borPhone);
				borDAO.create(toAdd);
				conn.commit();
				conn.close();				
			}
			catch(Exception e){
				conn.rollback();
				conn.close();
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}
	private void editBorrower(Borrower bor){
		try {
			Connection conn = getConnection();
			System.out.println("Enter new name: [N/A to skip]");
			String name = getInputString();
			System.out.println("Enter new address: [N/A to skip]");
			String address = getInputString();			
			System.out.println("Enter new phone: [N/A to skip]");
			String phone = getInputString();	
			if(!name.equals("N/A")){
				bor.setName(name);
			}
			if(!address.equals("N/A")){
				bor.setAddress(address);
			}
			if(!phone.equals("N/A")){
				bor.setPhone(phone);
			}
			
			try{
				BorrowerDAO borDAO = new BorrowerDAO(conn);
				borDAO.update(bor);	
				conn.commit();
				conn.close();			
			}
			catch(Exception e){
				conn.rollback();
				conn.close();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println("Error while connecting to database");
			e.printStackTrace();
		}	
	}
	private void deleteBorrower(Borrower bor){
		try {
			Connection conn = getConnection();			
			try{
				BorrowerDAO borDAO = new BorrowerDAO(conn);
				BookLoanDAO loanDAO = new BookLoanDAO(conn);
				List<BookLoan> branchLoans = (List<BookLoan>) loanDAO.read("SELECT * FROM tbl_book_loans WHERE cardNo = ? AND  dateIn IS NULL)" , new Object[] {bor.getCardNo()});
				if(branchLoans.size()>0){
					ArrayList<String> answers = new ArrayList<String>();
					answers.add("No, nevermind");
					answers.add("Yes, delete this branch");
					System.out.println("This user has "+branchLoans.size()+" books that are not returned");
					System.out.println("Are you sure you still want to delete? These books will be lost");
					displayOptions(answers);
					int in = getInputInt(1,2);
					if(in ==1){
						return;
					}										
				}
				borDAO.delete(bor);
				conn.commit();
				conn.close();			
			}
			catch(Exception e){
				conn.rollback();
				conn.close();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println("Error while connecting to database");
			e.printStackTrace();
		}
	}

	//display the possible effects on a book loan
	private BookLoan getLoan(){
		System.out.println("Which library processed the loan?");
		LibraryBranch branch = this.showAllBranches();
		Borrower bor = new Borrower();
		Book book = new Book();
		BookLoan loan =null;
		try {
			Connection conn = getConnection();
			try{
				LibraryBranchDAO libDAO = new LibraryBranchDAO(conn);
				BookLoanDAO bookLoanDAO = new BookLoanDAO(conn);
				BorrowerDAO borDAO = new BorrowerDAO(conn);
				BookDAO bookDAO = new BookDAO(conn);
				ArrayList<String> actions = new ArrayList<String>();
				actions.add("Cancel");
				if(branch!= null){
					Borrower borrower = null;
					System.out.println("What is the user's card number? [0 to cancel]");
					do{
						int card = getInputInt (0,10000);
						if(card==0){
							break;
							
						}
						bor = borDAO.readOne(card);		
						if(bor == null){
							System.out.println("Invalid card number.try again.");
						}
					}
					while(bor == null);
					if(bor!=null){
						String sql = "SELECT * FROM tbl_book WHERE tbl_book.bookId IN (SELECT tbl_book_loans.bookId FROM tbl_book_loans WHERE branchId = ? AND cardNo =? AND dateIn IS NULL )";
						List<Book> books = (List<Book>) bookDAO.read(sql, new Object[]{branch.getBranchId(),bor.getCardNo()});
						System.out.println("Which book?");
						int choice = getChoiceNumber(books,actions);
						if(choice>0 && choice<=books.size()){
							book = books.get(choice-1);
							loan = bookLoanDAO.readOne(book.getBookId(), branch.getBranchId(), bor.getCardNo());							
						}
					
					}
				}
				
			}
			catch(Exception e){
				conn.rollback();
				conn.close();
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return loan;
	}
	//update the book loan in the database
	//doesnt allow past times and times beyond a week from now
	private void editLoan(BookLoan loan){
		//credit to stackoverflow for the date validation: http://stackoverflow.com/questions/2149680/regex-date-format-validation-on-java	
		boolean validDate= false;
		String newDate;
		Date today;
		Date parsedDate =new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		do{
			System.out.println("Please put in the new due date [yyyy/mm/dd]");
			newDate= getInputString();
			
			
			try {
				today= new Date();
				parsedDate=format.parse(newDate);
				long timeDiff = parsedDate.getTime()-today.getTime();
				if(timeDiff<0){
					System.out.println("You can't pick a time that is past");
				}
				else if (timeDiff>604800000){//604800000=7*24*3600*1000. A week from now
					System.out.println("The chosen time has to within a week from today");
				}
				else{
					validDate= true;	
				}


			} catch (ParseException e) {
				validDate=false;
			}	
		}while(!validDate);
		loan.setDueDate(format.format(parsedDate));
		try {
			Connection conn = getConnection();
			try{
				BookLoanDAO loanDAO = new BookLoanDAO(conn);
				loanDAO.update(loan);	
				conn.commit();
				conn.close();
			} catch(Exception e){
				conn.rollback();
				conn.close();
			}			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}


