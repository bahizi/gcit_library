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
import com.gcit.lms.dao.GenreDAO;
import com.gcit.lms.dao.PublisherDAO;
import com.gcit.lms.domain.Author;
import com.gcit.lms.domain.Book;
import com.gcit.lms.domain.Genre;
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
			loanMain();
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
	private void authorMain(){
		ArrayList<String> options = new ArrayList<String>();
		ArrayList<String> actions = new ArrayList<String>();
		options.add("Add a new author");
		options.add("Edit an author's name");
		options.add("Delete an author");
		actions.add("Go Back");
		int choice = this.getChoiceNumber(options, actions);


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

	//diplay the possible effects to a publisher
	private void publisherMain(){
		ArrayList<String> options = new ArrayList<String>();
		options.add("Edit stored publishers");
		options.add("Add new publishers");
		options.add("Go back up the menu");
		System.out.println("What would like to do with publishers?");
		displayOptions(options);
		int action = getInputInt(1,options.size());
		switch(action){
		case 1:
			//edit publishers
			break;
		case 2:
			this.addPublisher();
			publisherMain();
			break;
		case 3:
			//go back to the admin main menu
			adminMenu();
		default:
			System.err.println("Invalid Input");
			System.exit(1);
		}

	}

	//gather the necessary information to add a publisher
	private int addPublisher(){
		System.out.println("What is the Publisher's name?");
		String pubName= getInputString();
		System.out.println("What is the Publisher's address?");
		String pubAddress=getInputString();
		System.out.println("What is the Publisher's phone number?");
		String pubPhone=getInputString();
		int pubId= insertPublisher(pubName,pubAddress,pubPhone);
		return pubId;
	}

	//insert the publisher in the database
	private int insertPublisher(String name, String address, String phone){
		int id=-1;
		try {
			Connection conn= getConnection();
			PreparedStatement pstmt= conn.prepareStatement("INSERT INTO tbl_publisher (publisherName, publisherAddress, publisherPhone) VALUES(?,?,?)");
			pstmt.setString(1, name);
			pstmt.setString(2, address);
			pstmt.setString(3, phone);
			pstmt.execute();
			Statement stmt= conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID() AS newId");
			if(rs.next()){
				id= rs.getInt("newId");
			}	

			conn.close();
		} catch (Exception e) {
			System.err.println("error while connecting to the database");
		}
		if(id<0){
			System.out.println("Failed to Record Publisher.Sorry");
			System.exit(1);
		}
		return id;
	}

	//display the possible effects to a library branch
	private void libraryBranchMain(){
		ArrayList<String> options = new ArrayList<String>();
		options.add("Edit stored libraries");
		options.add("Add a new Library Branch");
		options.add("Delete a stored Library Branch");
		options.add("Go back up the menu");
		System.out.println("What would like to do with libraries?");
		displayOptions(options);
		int action = getInputInt(1,options.size());
		switch(action){
		case 1:
			//edit libraries
			break;
		case 2:
			addLibrary();
			libraryBranchMain();
			break;
		case 3:
			//delete library
			libraryBranchMain();
			break;
		case 4:
			//go back to the admin main menu
			adminMenu();
		default:
			System.err.println("Invalid Input");
			System.exit(1);
		}
	}

	//gather the library's information
	//insert the library in the database
	private int addLibrary(){
		int id = -1;
		System.out.println("Branch Name: ");
		String name= getInputString();
		System.out.println("Address: ");
		String address= getInputString();;
		try {
			Connection conn= getConnection();
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO tbl_library_branch (branchName, branchAddress) VALUES(?,?)");
			pstmt.setString(1, name);
			pstmt.setString(2, address);
			pstmt.execute();
			Statement stmt= conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID() AS newId");
			if(rs.next()){
				id= rs.getInt("newId");
			} 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//System.err.println("Error while connecting to the database. Sorry");
		}

		return id;
	}

	//display the possible actions on a borrower
	//let the user choose an action and then call the appropriate method
	//when done, display this menu again, unless user chooses to exit
	private void borrowerMain(){
		ArrayList<String> options = new ArrayList<String>();
		options.add("Edit stored borrowers");
		options.add("Register a new borrower");
		options.add("Delete a stored borrower");
		options.add("Go back up the menu");
		System.out.println("What would like to do with borrowers?");
		displayOptions(options);
		int action = getInputInt(1,options.size());
		switch(action){
		case 1:
			//TODO: Edit borrowers and come back to the menu
			borrowerMain();
			break;
		case 2:
			addBorrower();
			borrowerMain();
			break;
		case 3:
			//delete borrowers;
			borrowerMain();
		case 4:
			//go back to the admin main menu
			adminMenu();
		default:
			System.err.println("Invalid Input");
			System.exit(1);
		}
	}

	//gather the borrower's information
	private int addBorrower(){
		int id = -1;
		System.out.println("Name: ");
		String name= getInputString();
		System.out.println("Address: ");
		String address= getInputString();
		System.out.println("Phone: ");
		String phone= getInputString();
		try {
			Connection conn= getConnection();
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO tbl_borrower (name, address, phone) VALUES(?,?,?)");
			pstmt.setString(1, name);
			pstmt.setString(2, address);
			pstmt.setString(3, phone);
			pstmt.execute();
			Statement stmt= conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID() AS newId");
			if(rs.next()){
				id= rs.getInt("newId");
			} 

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println("Error while connecting to the database. Sorry");
		}
		return id;		
	}

	private Book pickBook(){
		Book book = null;
		Connection conn;
		try {
			conn = getConnection();
			BookDAO bookDAO = new BookDAO(conn);
			List<Book> books = bookDAO.readAll();
			ArrayList<String> actions = new ArrayList<String>();
			actions.add("Cancel");
			int b = getChoiceNumber(books,actions);
			if(b>0 && b<=books.size()){
				book = books.get(b-1);
			}
			conn.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return book;
	}
	//display the possible effects on a book loan
	private void loanMain(){
		System.out.println("Which library processed the loan?");
		try {
			Connection conn = getConnection();
			PreparedStatement pstmt= conn.prepareStatement("SELECT branchName, branchId FROM tbl_library_branch");
			ArrayList<String> branches= new ArrayList<String>();
			ArrayList<Integer> branchIds= new ArrayList<Integer>();
			ResultSet rs= pstmt.executeQuery();

			while(rs.next()){
				branches.add(rs.getString("branchName"));
				branchIds.add(rs.getInt("branchId"));
			}
			branches.add("Go Back");
			System.out.println("Pick a branch you want to check out from");
			displayOptions(branches);
			int choice=getInputInt(1,branches.size());
			int branchId;
			if(choice<branches.size()){
				branchId=branchIds.get(choice-1);
				System.out.println("What is the user's card number?");
				int cardNumber= getInputInt(1,10000);
				//TODO check if that user actually exists in the database
				pstmt = conn.prepareStatement("SELECT title, dueDate, tbl_book.bookId FROM tbl_book JOIN tbl_book_loans ON tbl_book.bookId=tbl_book_loans.bookId WHERE branchId = ?  AND cardNo = ? AND dateIn IS NULL");
				pstmt.setInt(1, branchId);
				pstmt.setInt(2, cardNumber);			
				rs = pstmt.executeQuery();
				ArrayList<String> books= new ArrayList<String>();
				ArrayList<Integer> ids= new ArrayList<Integer>();
				while(rs.next()){
					books.add(rs.getString("title")+" \tdue on "+rs.getString("dueDate"));
					ids.add(rs.getInt("bookId"));
				}
				if(books.size()>0){
					books.add("Cancel Operation");
					System.out.println("Pick the book for which you would like to extend the due date");
					displayOptions(books);
					int book= getInputInt(1, books.size());
					if(book<books.size()){
						updateBookLoan(branchId, cardNumber,ids.get(book-1));
						this.adminMenu();
					}
					else{
						System.out.println("Returning to the menu up");
					}
				}
				else{
					System.out.println("Sorry, this user has no unreturned books for this library");
					System.out.println("*********************************************************");
					loanMain();
				}
			}

			else{
				System.out.println("Going up the menu");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	//update the book loan in the database
	//doesnt allow past times and times beyond a week from now
	private void updateBookLoan(int branchId, int cardNumber, Integer bookId) {
		//credit to stackoverflow for the date validation: http://stackoverflow.com/questions/2149680/regex-date-format-validation-on-java	
		boolean validDate= false;
		String newDate;
		Date today;
		Date parsedDate =new Date();
		do{
			System.out.println("Please put in the new due date [dd/mm/yyyy]");
			newDate= getInputString();
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
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
		try {
			Connection conn = getConnection();
			PreparedStatement pstmt= conn.prepareStatement("UPDATE tbl_book_loans SET dueDate = ? WHERE branchId= ? AND bookId=? AND cardNo = ?");
			pstmt.setTimestamp(1, new Timestamp(parsedDate.getTime()));
			pstmt.setInt(2, branchId);
			pstmt.setInt(3, bookId);
			pstmt.setInt(4, cardNumber);
			pstmt.execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}


