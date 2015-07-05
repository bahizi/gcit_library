package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.gcit.lms.domain.BookCopies;

public class BookCopiesDAO extends BaseDAO<BookCopies>{
	
public BookCopiesDAO(Connection conn) throws Exception {
		super(conn);
	}
	//TODO CREATE, UPDATE, DELETE, ReadAll & ReadOne
	public void create(BookCopies bookcopy) throws Exception{
		 save("INSERT INTO tbl_book_copies (bookId, branchId, noOfCopies) VALUES(?,?,?)", new Object[] {bookcopy.getBookId(),bookcopy.getBranchId(),bookcopy.getNoOfCopies()});		
	}
	public void update(BookCopies bookCopy) throws Exception{
		save("UPDATE tbl_book_copies SET noOfCopies = ? WHERE bookId=? AND branchId = ?", new Object[] {bookCopy.getNoOfCopies(),bookCopy.getBookId(),bookCopy.getBranchId()});
		
	}
	public void delete(BookCopies bookCopy) throws Exception{
		save("DELETE FROM tbl_book_copies WHERE bookId=? AND branchId = ?", new Object[] {bookCopy.getBookId(),bookCopy.getBranchId()});
	}
	@SuppressWarnings("unchecked")
	public List<BookCopies> readAll() throws Exception{
		return (List<BookCopies>) read("SELECT * FROM tbl_book_copies", null);	
	}
	@SuppressWarnings("unchecked")
	public BookCopies readOne(int branchId, int bookId) throws Exception{
		
		List<BookCopies> b = (List<BookCopies>) read("SELECT * FROM tbl_book_copies WHERE bookId= ? AND branchId = ?", new Object[] {bookId,branchId});
		if(b!=null && b.size()>0){
			return b.get(0);
		}
		return null;
	}
	@Override
	public List<BookCopies> extractData(ResultSet rs) throws Exception {
		return extractDataFirstLevel(rs);
		
	}
	@Override
	public List<BookCopies> extractDataFirstLevel(ResultSet rs) throws Exception {
		List<BookCopies> copies = new ArrayList<BookCopies>();
		while(rs.next()){
			BookCopies copy = new BookCopies();			
			copy.setBookId(rs.getInt("bookId"));
			copy.setBranchId(rs.getInt("branchId"));
			copy.setNoOfCopies(rs.getInt("noOfCopies"));
			copies.add(copy);
		}		
		return copies;
	}
}
