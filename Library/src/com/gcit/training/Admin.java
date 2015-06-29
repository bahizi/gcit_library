package com.gcit.training;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Admin extends User{
	public Admin(){
		adminMenu();
	}
	//show the main objects that the admin can affect
	private void adminMenu(){
		ArrayList<String> options = new ArrayList<String>();
		options.add("Books and Authors");
		options.add("Publishers");
		options.add("Library Branches");
		options.add("Borrowers");
		options.add("Book Loan Due Dates");
		options.add("Exit to main menu");
		System.out.println("What would you like to manage?");
		displayOptions(options);
		int choice= getInputInt(1,options.size());
		switch(choice){
		case 1:
			//manage books and authors;
			bookAuthorMain();
			break;
		case 2:
			//manage publishers;
			publisherMain();
			break;
		case 3:
			//manage library branches;
			libraryBranchMain();
			break;
		case 4:
			//manage borrowers;
			borrowerMain();
			break;
		case 5:
			//manage book loans
			loanMain();
			break;
		case 6:
			//exit;
			System.out.println("Exiting to the main menu");
			break;
		default:
			//this case should never happen. if it does, something is wrong with getInputInt
			System.err.println("Invalid input.");
			System.exit(1);
			}
		}
	//display the possible actions on a book
	//allow books with no author but no authors with no books
	private void bookAuthorMain(){
		ArrayList<String> options = new ArrayList<String>();
		options.add("Add Book and Authors");
		options.add("Edit/Delete Book and Author");
		options.add("Go one step up in the menu");
		System.out.println("What would you like to do with the books and authors?");
		displayOptions(options);
		int action= getInputInt(1,options.size());
		switch(action){
		case 1:
			//add book and authors
			addBook();
			break;
		case 2:
			//add book and authors
			//editBook();
			break;
		case 3:
			//Exit to previous menu
			adminMenu();
			break;
		default:
			//This should never happen.
			System.err.println("Invalid input.");
		}
	}
	
	//receive the necessary information to add a book	
	private void addBook(){
		System.out.println("What is the title of the new book?");
		String title = getInputString();
		ArrayList<String> authors = new ArrayList<String>();
		ArrayList<String> questions = new ArrayList<String>();
		questions.add("Yes");
		questions.add("No");
		boolean moreAuthors= true;
		do {
			System.out.println("Author's name:");			
			authors.add(getInputString());
			System.out.println("More authors?");
			displayOptions(questions);
			int answer = getInputInt(1,2);
			moreAuthors = answer==1;			
		} while (moreAuthors);
		try {
			Connection conn = getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT publisherId, publisherName FROM tbl_publisher");
			ArrayList<String> publisherNames= new ArrayList<String>();
			ArrayList<Integer> publisherIds = new ArrayList<Integer>();
			while(rs.next()){
				publisherIds.add(rs.getInt("publisherId"));
				publisherNames.add(rs.getString("publisherName"));
			}
			publisherNames.add("*Publisher not Listed. I want to enter one myself*");
			displayOptions(publisherNames);
			int publisherChoice= getInputInt(1,publisherNames.size());
			int pubId;
			if(publisherChoice< publisherNames.size()){
				pubId= publisherIds.get(publisherChoice-1);
			}
			else{
				pubId = addPublisher();
			}
			int bookId= insertBook(title,pubId);
			int authorId;
			for (String authorName: authors){
				authorId= insertAuthor(authorName);
				linkAuthorBook(authorId, bookId);
			}
		} catch (SQLException e) {
			System.err.println("Error while connecting to the database");
		}	
	}
	//insert the book in the database and return the new book's id
	private int insertBook(String title,int pubId){
		int id = -1;
		try {
			Connection conn = getConnection();
			PreparedStatement pstmt= conn.prepareStatement("INSERT INTO tbl_book (title, pubId) VALUES(?,?)");
			pstmt.setString(1, title);
			pstmt.setInt(2, pubId);
			pstmt.execute();
			Statement stmt= conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID() AS newId");
			if(rs.next()){
				id= rs.getInt("newId");
			}					
			conn.close();
		} catch (SQLException e) {
			System.err.println("error while connecting to the database");
		}
		if(id<0){
			System.out.println("Failed to record Book.Sorry");
			System.exit(1);
		}
		return id;	
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
	//insert the author in the database, return the new entry's id
	private int insertAuthor(String name){
		int id=-1;
		try {
			Connection conn= getConnection();
			PreparedStatement checkStmt = conn.prepareStatement("SELECT authorId FROM tbl_author WHERE authorName= ?");
			checkStmt.setString(1,name);
			ResultSet check_rs=checkStmt.executeQuery();
			if(check_rs.next()){//the author already exists
				id= check_rs.getInt("authorId");
			}
			else{
				PreparedStatement pstmt= conn.prepareStatement("INSERT INTO tbl_author (authorName) VALUES(?)");
				pstmt.setString(1, name);
				pstmt.execute();
				Statement stmt= conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID() AS newId");
				if(rs.next()){
					id= rs.getInt("newId");
				}	
			}					
			conn.close();
		} catch (SQLException e) {
			System.err.println("error while connecting to the database");
		}
		if(id<0){
			System.out.println("Failed to Record Author.Sorry");
			System.exit(1);
		}
		return id;
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
		} catch (SQLException e) {
			System.err.println("error while connecting to the database");
		}
		if(id<0){
			System.out.println("Failed to Record Publisher.Sorry");
			System.exit(1);
		}
		return id;
	}
	//connect the author and their books in tbl_book_author
	//has to run every time we add a book
	private void linkAuthorBook(int authorId, int bookId){
		try {
			Connection conn =  getConnection();
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO tbl_book_authors (bookId,authorId) VALUES(?,?)");
			pstmt.setInt(1,bookId);
			pstmt.setInt(2,authorId);
			pstmt.execute();
			conn.close();
		} catch (SQLException e) {
			System.err.println("Error while connecting to the Database");
		}		
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

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//System.err.println("Error while connecting to the database. Sorry");
		}

		return id;
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

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Error while connecting to the database. Sorry");
		}
		return id;		
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

		} catch (SQLException e) {
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
