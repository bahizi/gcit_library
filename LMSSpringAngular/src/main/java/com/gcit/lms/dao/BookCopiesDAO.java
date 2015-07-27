package com.gcit.lms.dao;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcit.lms.domain.Book;
import com.gcit.lms.domain.BookCopies;
import com.gcit.lms.domain.BookLoan;
import com.gcit.lms.domain.Borrower;
import com.gcit.lms.domain.LibraryBranch;

public class BookCopiesDAO extends BaseDAO<BookCopies>{
	@Autowired
	BookLoanDAO bookLoanDAO;	
	@Autowired
	BookDAO bookDAO;	
	@Autowired
	LibraryBranchDAO branchDAO;	
	@Autowired
	BorrowerDAO borDAO;	

	//TODO CREATE, UPDATE, DELETE, ReadAll & ReadOne
	public void create(BookCopies bookcopy) {
		template.update("INSERT INTO tbl_book_copies (bookId, branchId, noOfCopies) VALUES(?,?,?)", new Object[] {bookcopy.getBookId(),bookcopy.getBranchId(),bookcopy.getNoOfCopies()});		
	}
	public void update(BookCopies bookCopy){
		template.update("UPDATE tbl_book_copies SET noOfCopies = ? WHERE bookId=? AND branchId = ?", new Object[] {bookCopy.getNoOfCopies(),bookCopy.getBookId(),bookCopy.getBranchId()});

	}
	public void delete(BookCopies bookCopy){
		template.update("DELETE FROM tbl_book_copies WHERE bookId=? AND branchId = ?", new Object[] {bookCopy.getBookId(),bookCopy.getBranchId()});
	}
	public List<BookCopies> readAll(){
		return template.query("SELECT * FROM tbl_book_copies", this);	
	}
	public boolean canBorrow(int branchId, int bookId, int cardNo){

		/*conditions for a bookloan to be possible:
		1. book, branch and user must exist
		2. library must have at least one copy of the book
		3. user must cant have borrowed the book before
		 */
		BookCopies bc = this.readOne(branchId, bookId);
		LibraryBranch branch = branchDAO.readOne(branchId);
		Borrower user = borDAO.readOne(cardNo);
		Book book = bookDAO.readOne(bookId);
		BookLoan bl = bookLoanDAO.readOne(bookId, branchId, cardNo);

		if(bc!=null && branch !=null && user !=null && book!=null && bl ==null){//if the first 2 conditions are satisfied
			return (bc.getNoOfCopies()>0);
		}				
		return false;
	}
	public boolean canReturn(int branchId, int bookId, int cardNo){

		/*conditions for a book return to be possible:
		1. book, branch and user must exist
		2. date in	 must be null
		3. library must have had had the book before		
		 */
		BookCopies bc = this.readOne(branchId, bookId);
		LibraryBranch branch = branchDAO.readOne(branchId);
		Borrower user = borDAO.readOne(cardNo);
		Book book = bookDAO.readOne(bookId);
		BookLoan bl = bookLoanDAO.readOne(bookId, branchId, cardNo);

		if(bc!=null && branch !=null && user !=null && book!=null && bl !=null){
			return (bl.getDateIn()==null);
		}				
		return false;
	}

	public BookCopies readOne(int branchId, int bookId){		
		List<BookCopies> b =  template.query("SELECT * FROM tbl_book_copies WHERE bookId= ? AND branchId = ?", new Object[] {bookId,branchId}, this);
		if(b!=null && b.size()>0){
			return b.get(0);
		}
		return null;
	}
	@Override
	public List<BookCopies> extractData(ResultSet rs) throws SQLException {
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


	@Override
	public List<BookCopies> search(String query, int pageNum) {
		int start = 0;
		if(pageNum >1){
			start = (pageNum*10)+1;

		}
		String s = "%"+query+"%";
		String sql = "SELECT * FROM tbl_book_copies "
				+ "LEFT JOIN tbl_book ON tbl_book_copies.bookId = tbl_book.bookId "
				+ "LEFT JOIN tbl_library_branch ON tbl_book_copies.branchId = tbl_library_branch.bookId "
				+ "WHERE title LIKE ? "
				+ "OR branchName LIKE ? "				
				+ "LIMIT ?, 10";
		return template.query(sql, new Object[] {s,s,start}, this);
	}
	@Override
	public List<BookCopies> readFirstLevel(String query, Object[] vals) {
		// TODO Auto-generated method stub
		return null;
	}	
}
