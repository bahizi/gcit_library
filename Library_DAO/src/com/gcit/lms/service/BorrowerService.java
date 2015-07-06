package com.gcit.lms.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gcit.lms.dao.BookCopiesDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.dao.BookLoanDAO;
import com.gcit.lms.dao.BorrowerDAO;
import com.gcit.lms.dao.LibraryBranchDAO;
import com.gcit.lms.domain.Book;
import com.gcit.lms.domain.BookCopies;
import com.gcit.lms.domain.BookLoan;
import com.gcit.lms.domain.Borrower;
import com.gcit.lms.domain.LibraryBranch;

public class BorrowerService extends BaseService {
	Borrower borrower = null;
	public BorrowerService(){
		super();
		authenticate();
		if (this.borrower!=null){//if the user didn't cancel the athentication
			pickAction();
		}
	}

	private void authenticate(){
		ConnectionUtil c = new ConnectionUtil();
		Connection conn;
		try {
			conn = c.createConnection();
			BorrowerDAO borDAO = new BorrowerDAO(conn);	
			while(this.borrower==null){
				System.out.println("Please input your library Card Number. [O to exit]>>");
				int card= getInputInt(0,100000);
				if(card==0){
					break;
				}
				else{
					borrower = borDAO.readOne(card);
					if (borrower == null){
						System.out.println("Invalid card Number. Try again.");
					}
				}
			}
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Failed to connect to the database. Sorry.");
		}		
	}
	private void pickAction(){
		ArrayList<String> options = new ArrayList<String>();
		ArrayList<String> actions = new ArrayList<String>();
		System.out.println("Hello, "+borrower+". What would like to do today?");
		options.add("Check out a book");
		options.add("Return a Book");
		actions.add("Quit to Main menu");
		int input = getChoiceNumber(options,actions);		
		switch(input){
		case -1:
			//quit to main menu
			System.out.println("Going back to the main menu");
			System.out.println("**************************************");
			break;
		case 1:
			//check out a book
			borrowBook();
			break;
		case 2:
			returnBook();
			break;		
		default:
			//this case should never occur			
			System.err.println("Invalid input");
			break;
		}
	}
	private void borrowBook(){
		System.out.println("Pick a branch you want to check out from");
		LibraryBranch branch = pickLibrary();
		if(branch!=null){
			Book toBorrow = pickBookToBorrow(branch);
			if(toBorrow!=null){
				if(!this.alreadyBorrowedBook(toBorrow, branch)){
					recordBookLoan(branch,toBorrow);
					System.out.println("Success! Enjoy your copy of "+toBorrow.getTitle()+"!");
					System.out.println("It be will due in a week from today\n");
					
				}
				else{
					System.out.println("Sorry, you have already borrowed this book from this branch");
				}
				System.out.println("****************************************");				
				pickAction();
			}		
		}
	}
	private void returnBook(){
		System.out.println("Pick a branch you want to return a book to");
		LibraryBranch returnBranch = pickLibrary();
		if(returnBranch!=null){
			Book book = this.pickBookToReturn(returnBranch);
			if(book!=null){
				recordBookReturn(returnBranch, book);
				System.out.println("Thanks for returning the book.");
				System.out.println("We hope you enjoyed this copy of  "+ book.getTitle());
				System.out.println("****************************************");				
				pickAction();
			}
		}
	}
	private LibraryBranch pickLibrary(){
		LibraryBranch res = null;
		try {
			ConnectionUtil c = new ConnectionUtil();
			Connection conn = c.createConnection();
			LibraryBranchDAO libDAO = new LibraryBranchDAO(conn);
			List<LibraryBranch> branches = libDAO.readAll();
			ArrayList<String> actions= new ArrayList<String>();
			actions.add("Go Back");
			int choice = getChoiceNumber(branches,actions);
			if(choice>0 && choice<branches.size()-1){
				res= branches.get(choice-1);				
			}
			else{
				pickAction();
			}
			conn.close();
		} catch (Exception e) {
			System.err.println("Exiting: error while connecting to the database.");
			System.exit(1);
		}
		return res;

	}
	private Book pickBookToBorrow(LibraryBranch branch){
		Book book =null;
		try {
			ConnectionUtil c = new ConnectionUtil();
			Connection conn = c.createConnection();
			BookDAO bookDAO = new BookDAO(conn);
			String sql = "SELECT * FROM tbl_book JOIN tbl_book_copies ON tbl_book.bookId=tbl_book_copies.bookId WHERE branchId = ?  AND noOfCopies>0";
			List<Book> books = (List<Book>) bookDAO.read(sql, new Object[]{branch.getBranchId()});
			ArrayList<String> actions = new ArrayList<String>();
			actions.add("Cancel operation");
			System.out.println("Available books to borrow: ");			
			int choice=this.getChoiceNumber(books, actions);
			if (choice>0 && choice<=books.size()){
				book =books.get(choice-1);				
			}
			else{
				pickAction();
			}
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Exiting: error while connecting to the database.");
			System.exit(1);
		}		
		return book;
	}

	private void furtherActions(LibraryBranch branch) {
		System.out.println("What would you like to do now?");				
		ArrayList<String> next = new ArrayList<String>();
		next.add("Borrow another book from this library");
		next.add("Return a book to this library");
		next.add("Borrow another book from another library");
		next.add("Return a book to another library");
		next.add("Go to main menu");
		displayOptions(next);
		int action = getInputInt(1,next.size());
		switch(action){
		case 1:
			pickBookToBorrow(branch);
			break;
		case 2:
			pickBookToReturn(branch);
			break;
		case 3:
			borrowBook();
			break;
		case 4:
			returnBook();
			break;
		case 5:
			//exit and go back to the main menu
			break;			
		}

	}

	private void adjustBookNumber(LibraryBranch branch, Book book, int i, Connection conn) throws Exception {
		int incr= 1;
		if(i<0){
			i = -1;
		}
		BookCopiesDAO copiesDAO = new BookCopiesDAO(conn);
		BookCopies copies = copiesDAO.readOne(branch.getBranchId(), book.getBookId());
		copies.setNoOfCopies(copies.getNoOfCopies()+incr);
		copiesDAO.update(copies);				
	}
	private void recordBookLoan(LibraryBranch branch, Book book) {

		try {
			ConnectionUtil c = new ConnectionUtil();
			Connection conn = c.createConnection();

			try {
				BookLoanDAO copiesDAO = new BookLoanDAO(conn);
				BookLoan bookLoan = new BookLoan();
				Date date= new Date();
				Long now = date.getTime();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				bookLoan.setBookId(book.getBookId());
				bookLoan.setBranchId(branch.getBranchId());
				bookLoan.setCardNo(borrower.getCardNo());			
				bookLoan.setDateOut(format.format(now));
				bookLoan.setDueDate(format.format(now+604800000));//7*24*3600*1000=604800000 : the amount of milliseconds in one week			
				bookLoan.setDateIn(null);
				copiesDAO.create(bookLoan);
				adjustBookNumber(branch, book, -1, conn);
				conn.commit();
				conn.close();
			} catch (Exception e) {
				conn.rollback();
				e.printStackTrace();
				System.err.println("Error while recording your book checkout.");
			}			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error while connecting to the database");			
		}		
	}

	private Book pickBookToReturn(LibraryBranch branch){
		Book book = null;
		ConnectionUtil c = new ConnectionUtil();
		try {
			Connection conn = c.createConnection();
			BookLoanDAO bookLoanDAO = new BookLoanDAO(conn);
			String sql = "SELECT * FROM tbl_book_loans WHERE branchId = ? AND cardNo = ? AND dateIn IS NULL";
			List<BookLoan> bookLoans = (List<BookLoan>) bookLoanDAO.read(sql , new Object[] {branch.getBranchId(), borrower.getCardNo()});
			ArrayList<String> actions = new ArrayList<String>();
			actions.add("Cancel");
			int choice = this.getChoiceNumber(bookLoans, actions);
			if(choice>0 && choice <=bookLoans.size()){
				BookDAO bookDAO = new BookDAO(conn);
				book = bookDAO.readOne(bookLoans.get(choice-1).getBookId());				
			}
			else{
				pickAction();
			}
			conn.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return book;

	}
	private void recordBookReturn(LibraryBranch branch,Book book){

		ConnectionUtil c = new ConnectionUtil();
		try {			
			Connection conn = c.createConnection();			
			try {
				BookLoanDAO bookLoanDAO = new BookLoanDAO(conn);
				BookLoan bookLoan = bookLoanDAO.readOne(book.getBookId(), branch.getBranchId(), borrower.getCardNo());
				Date date = new Date();
				Long now = date.getTime();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				bookLoan.setDateIn(format.format(now));
				System.out.println("Returned "+bookLoan.fetchBook()+ "to "+bookLoan.fetchBranch()+" by "+bookLoan.fetchBorrower());
				bookLoanDAO.update(bookLoan);
				adjustBookNumber(branch, book, 1, conn);	
				conn.commit();
				conn.close();				
			} catch (Exception e) {
				conn.rollback();
				e.printStackTrace();
				System.err.println("Sorry. Your book return wasn't recorded. Try again.");
			}			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Exiting: error while connecting to the database.");			
		}
	}	

	private boolean alreadyBorrowedBook(Book book,LibraryBranch branch){
		boolean ret=true;
		ConnectionUtil c = new ConnectionUtil();
		try{
			Connection conn = c.createConnection();
			BookLoanDAO libDAO = new BookLoanDAO(conn);
			BookLoan loan =libDAO.readOne(book.getBookId(), branch.getBranchId(), borrower.getCardNo());
			if(loan==null){
				ret =false;
			}
			conn.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ret;

	}
}


