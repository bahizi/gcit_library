package com.gcit.training;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class Borrower extends User{
	private Integer cardNumber;
	public Borrower(){
		authenticate();
		if (this.cardNumber!=null){//if the user didn't cancel the athentication
			pickAction();
		}
	}
	//check out a book
	private void authenticate(){
		while(this.cardNumber==null){
			System.out.println("Please input your library Card Number. [O to exit]>>");
			int card= getInputInt(0,100000);
			if(card!=0){
				try {
					Connection conn= getConnection();
					PreparedStatement pstmt= conn.prepareCall("SELECT cardNo FROM tbl_borrower WHERE cardNo=?" );
					pstmt.setInt(1, card);
					ResultSet rs=pstmt.executeQuery();
					if(rs.next()){
						this.cardNumber=card;
					}
					else{
						System.out.println("Invalid card Number. Try again.");
					}
				}
				catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				break;
			}
		}
	}
	private void pickAction(){
		ArrayList<String> options= new ArrayList<String>();
		options.add("Check out a book");
		options.add("Return a Book");
		options.add("Quit to Main menu");
		displayOptions(options);
		int input = getInputInt(1,options.size());
		switch(input){
		case 1:
			bor1();
			//check out a book
			break;
		case 2:
			ret1();
			//return a book;
			break;
		case 3:
			//quit to main menu
			System.out.println("Going back to the main menu");
			System.out.println("**************************************");
			break;
		default:
			//this case should never occur			
			System.err.println("Invalid input");
			break;
		}
	}
	private void bor1(){
		try {
			Connection conn = getConnection();
			PreparedStatement pstmt= conn.prepareStatement("SELECT branchName FROM tbl_library_branch");
			ArrayList<String> branches= new ArrayList<String>();
			ResultSet rs= pstmt.executeQuery();
			while(rs.next()){
				branches.add(rs.getString("branchName"));
			}
			branches.add("Go Back");
			System.out.println("Pick a branch you want to check out from");
			displayOptions(branches);
			int choice=getInputInt(1,branches.size());
			if(choice<branches.size()){
				bor2(choice);
			}
			else{
				pickAction();
			}

		} catch (SQLException e) {
			System.err.println("Exiting: error while connecting to the database.");
			System.exit(1);
		}

	}
	private void bor2(int libraryId){
		try {
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("SELECT title FROM tbl_book JOIN tbl_book_copies ON tbl_book.bookId=tbl_book_copies.bookId WHERE branchId = ?  AND noOfCopies>0");
			pstmt.setInt(1, libraryId);
			ResultSet rs = pstmt.executeQuery();
			ArrayList<String> books= new ArrayList<String>();
			while(rs.next()){
				books.add(rs.getString("title"));
			}
			conn.close();
			books.add("Cancel Operation");
			System.out.println("Available books: ");
			displayOptions(books);
			int book=getInputInt(1,books.size());
			if (book<books.size()){
				if(!this.alreadyBorrowedBook(libraryId, book)){
					recordBookLoan(libraryId,book);
					adjustBookNumber(libraryId,book,-1);
					System.out.println("Success! Enjoy your copy of "+books.get(book-1)+"!");
					System.out.println("It be will due in a week from today\n");
					System.out.println("****************************************");
				}
				else{
					System.out.println("Sorry. You have already borrowed this book from this library.");
				}
				furtherActions(libraryId);
			}
			else{
				bor1();
			}
		} catch (SQLException e) {
			System.err.println("Exiting: error while connecting to the database.");
			System.exit(1);
		}		
	}

	private boolean alreadyBorrowedBook(int libId, int bookId){
		boolean ret=true;
		try{
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM tbl_book_loans WHERE bookId = ? AND branchId = ? AND cardNo =?");
			pstmt.setInt(1, bookId);
			pstmt.setInt(2, libId);
			pstmt.setInt(3, this.cardNumber);
			ResultSet rs = pstmt.executeQuery();
			ret = rs.next();	
			
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;

	}

	private void recordBookLoan(int libId, int bookId){
		try {
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO tbl_book_loans VALUES(?,?,?,?,?,?)");
			Date date= new Date();
			pstmt.setInt(1, bookId);
			pstmt.setInt(2, libId);
			pstmt.setInt(3, this.cardNumber);			
			pstmt.setTimestamp(4, new Timestamp(date.getTime()));
			pstmt.setTimestamp(5, new Timestamp(date.getTime()+ 604800000));//7*24*3600*1000=604800000 : the amount of milliseconds in one week
			pstmt.setTimestamp(6,null);

			pstmt.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			//			System.err.println("Error while connecting to the database");
			//			System.exit(1);
		}		
	}
	private void adjustBookNumber(int libId,int bookId, int direction){
		int increment=1;
		if(direction<0){
			increment=-1;
		}
		try {
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("SELECT noOfCopies FROM tbl_book_copies WHERE branchId = ?  AND bookId = ?");
			pstmt.setInt(1,libId);
			pstmt.setInt(2, bookId);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			
			int num= rs.getInt("noOfCopies")+increment;
			conn.close();
			System.out.println("We now remain with "+ num+" copies");
			Connection ins_conn = getConnection();
			PreparedStatement ins_stmt = ins_conn.prepareStatement("UPDATE tbl_book_copies SET noOfCopies = ? WHERE bookId = ? AND branchId = ?");
			ins_stmt.setInt(1,num);
			ins_stmt.setInt(2, bookId);			
			ins_stmt.setInt(3,libId);
			ins_stmt.execute();
			ins_conn.close();			
		} catch (SQLException e) {
			System.err.println("Error while connecting to the database");
			System.exit(1);
		}		
	}

	private void furtherActions(int libId){
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
			bor2(libId);
			break;
		case 2:
			ret2(libId);
			break;
		case 3:
			bor1();
			break;
		case 4:
			ret1();
			break;
		case 5:
			break;			
		}
	}	
	
	private void ret1(){
		try {
			Connection conn = getConnection();
			PreparedStatement pstmt= conn.prepareStatement("SELECT branchName FROM tbl_library_branch");
			ArrayList<String> branches= new ArrayList<String>();
			ResultSet rs= pstmt.executeQuery();
			while(rs.next()){
				branches.add(rs.getString("branchName"));
			}
			branches.add("Go Up");
			System.out.println("Pick a branch you want to return the book to:");
			displayOptions(branches);
			int choice=getInputInt(1,branches.size());
			if(choice<branches.size()){
				ret2(choice);
			}
			else{
				pickAction();
			}

		} catch (SQLException e) {
			System.err.println("Exiting: error while connecting to the database.");
			System.exit(1);
		}
	}
	private void ret2(int libraryId){
		try {
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("SELECT title, tbl_book.bookId FROM tbl_book JOIN tbl_book_loans ON tbl_book.bookId=tbl_book_loans.bookId WHERE branchId = ?  AND cardNo = ? AND dateIn IS NULL");
			pstmt.setInt(1, libraryId);
			pstmt.setInt(2, this.cardNumber);			
			ResultSet rs = pstmt.executeQuery();
			ArrayList<String> books= new ArrayList<String>();
			ArrayList<Integer> ids= new ArrayList<Integer>();
			while(rs.next()){
				books.add(rs.getString("title"));
				ids.add(rs.getInt("bookId"));
			}
			conn.close();
			books.add("Cancel Operation");
			System.out.println("Borrowed books from this library: ");
			displayOptions(books);
			int book=getInputInt(1,books.size());
			if (book<books.size()){
				recordBookReturn(libraryId,ids.get(book-1));
				adjustBookNumber(libraryId,ids.get(book-1),1);
				System.out.println("Success! We hope that your enjoyed this copy of "+books.get(book-1)+"!");
				System.out.println("****************************************");
				furtherActions(libraryId);
				}
			else{
				ret1();
			}
		} catch (SQLException e) {
			e.printStackTrace();
//			System.err.println("Exiting: error while connecting to the database.");
			System.exit(1);
		}
		
	}
	private void recordBookReturn(int libraryId, int bookId){
		try {
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("UPDATE tbl_book_loans SET dateIn= ? WHERE branchId = ? AND bookId=? AND cardNo=?");
			Date date= new Date();
			pstmt.setTimestamp(1, new Timestamp(date.getTime()));
			pstmt.setInt(2, libraryId);
			pstmt.setInt(3, bookId);
			pstmt.setInt(4, this.cardNumber);
			pstmt.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			System.err.println("Error while connecting to the database");
			System.exit(1);
		}	
	}
}


