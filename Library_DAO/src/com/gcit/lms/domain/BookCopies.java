package com.gcit.lms.domain;

import java.sql.Connection;

import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.dao.BorrowerDAO;
import com.gcit.lms.dao.LibraryBranchDAO;
import com.gcit.lms.service.ConnectionUtil;


public class BookCopies {
	private int bookId;
	private int branchId;
	private int noOfCopies;
	
	public int getBookId() {
		return bookId;
	}
	public void setBookId(int book) {
		this.bookId = book;
	}
	public int getBranchId() {
		return branchId;
	}
	public void setBranchId(int branch) {
		this.branchId = branch;
	}
	public int getNoOfCopies() {
		return noOfCopies;
	}
	public void setNoOfCopies(int noOfCopies) {
		this.noOfCopies = noOfCopies;
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return branch;
	
	}
	@Override
	public String toString(){
		
		String result = "";		
		result += fetchBook().toString()+" @ ";
		result += fetchBranch().toString()+ ": ";
		result += getNoOfCopies()+" copies";		
		return result;
	}
}
