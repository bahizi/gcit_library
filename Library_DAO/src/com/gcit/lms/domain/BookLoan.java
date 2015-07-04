package com.gcit.lms.domain;

import java.sql.Connection;
import java.sql.Timestamp;

import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.dao.BorrowerDAO;
import com.gcit.lms.dao.LibraryBranchDAO;
import com.gcit.lms.service.ConnectionUtil;

public class BookLoan {
	private int bookId;
	private int branchId;
	private int cardNo;
	private Timestamp dateOut;
	private Timestamp dueDate;
	private Timestamp dateIn;
	
	public int getBookId() {
		return bookId;
	}
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	public int getBranchId() {
		return branchId;
	}
	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}
	public int getCardNo() {
		return cardNo;
	}
	public void setCardNo(int cardNo) {
		this.cardNo = cardNo;
	}
	public Timestamp getDateOut() {
		return dateOut;
	}
	public void setDateOut(Timestamp dateOut) {
		this.dateOut = dateOut;
	}
	public Timestamp getDueDate() {
		return dueDate;
	}
	public void setDueDate(Timestamp dateTime) {
		this.dueDate = dateTime;
	}
	public Timestamp getDateIn() {
		return dateIn;
	}
	public void setDateIn(Timestamp dateIn) {
		this.dateIn = dateIn;
	}
	public Book fetchBook(){
		ConnectionUtil c= new ConnectionUtil();
		Connection conn;
		BookDAO bookDAO;
		Book book = null;
		try {
			conn = c.createConnection();
			bookDAO = new BookDAO(conn);
			book = bookDAO.readOne(getBookId());
			conn.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return book;
	}
	public LibraryBranch fetchBranch(){
		ConnectionUtil c= new ConnectionUtil();
		Connection conn;
		LibraryBranch branch = null;
		try {
			conn = c.createConnection();
			LibraryBranchDAO libDAO= new LibraryBranchDAO(conn);
			branch = libDAO.readOne(getBranchId());
			conn.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return branch;	
	}
	public Borrower fetchBorrower(){
		ConnectionUtil c= new ConnectionUtil();
		Connection conn;
		Borrower borrower = null;
		try {
			conn = c.createConnection();
			BorrowerDAO borDAO = new BorrowerDAO(conn);
			borrower = borDAO.readOne(getCardNo());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return borrower;	
	}
	@Override
	public String toString(){
		String result = "";
		result += fetchBorrower().toString() ;
		result += " : "+fetchBook().toString();
		result += " @ " + fetchBranch().toString();
		result += "[Due Date:"+ this.getDueDate()+"]";		
		return result;
	}
}
