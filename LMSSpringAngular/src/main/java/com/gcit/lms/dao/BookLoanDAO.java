package com.gcit.lms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gcit.lms.domain.BookLoan;
public class BookLoanDAO extends BaseDAO<BookLoan>{
	public void create(BookLoan bookLoan) {
		template.update("INSERT INTO tbl_book_loans (bookId,branchId,cardNo,dateOut,dueDate, dateIn) VALUES(?,?,?,?,?,?)", new Object[]{bookLoan.getBookId(),bookLoan.getBranchId(),bookLoan.getCardNo(),bookLoan.getDateOut(),bookLoan.getDueDate(), bookLoan.getDateIn()});		
	}
	public void update(BookLoan bookLoan) {
		template.update("UPDATE tbl_book_loans SET dateIn = ?, dueDate = ? WHERE  bookId = ? AND branchId =? AND cardNo = ? AND dateOut = ? ", new Object[]{bookLoan.getDateIn(),bookLoan.getDueDate(),bookLoan.getBookId(),bookLoan.getBranchId(),bookLoan.getCardNo(), bookLoan.getDateOut()});		
	}
	public void delete(BookLoan bookLoan) {
		template.update("DELETE FROM tbl_book_loans WHERE bookId = ? AND branchId = ? AND cardNo = ? AND dateOut = ?",new Object[]{bookLoan.getBookId(),bookLoan.getBranchId(),bookLoan.getCardNo(), bookLoan.getDateOut()});		
	}
	public List<BookLoan> readAll() {
		return  template.query("SELECT * FROM tbl_book_loans", this);
	}

	public BookLoan readOne(int bookId,int branchId, int cardNo) {
		List<BookLoan> bookLoans =  template.query("SELECT * FROM tbl_book_loans WHERE bookId = ? AND  branchId = ? AND cardNo = ? ", new Object[] {bookId,branchId,cardNo}, this);
		if(bookLoans!=null && bookLoans.size()>0){
			return bookLoans.get(0);
		}
		return null;
	}
	@Override
	public List<BookLoan> extractData(ResultSet rs) throws SQLException {
		List<BookLoan> bookLoans = new ArrayList<BookLoan>();
		while(rs.next()){
			BookLoan loan = new BookLoan();			
			loan.setBookId(rs.getInt("bookId"));
			loan.setBranchId(rs.getInt("branchId"));
			loan.setCardNo(rs.getInt("cardNo"));
			loan.setDateOut(rs.getString("dateOut"));
			loan.setDueDate(rs.getString("dueDate"));
			loan.setDateIn(rs.getString("dateIn"));
			bookLoans.add(loan);		
		}
		return bookLoans;
	}	
	@Override
	public List<BookLoan> search(String query, int pageNum) {
		return null;
	}
	@Override
	public List<BookLoan> readFirstLevel(String query, Object[] vals) {
		// TODO Auto-generated method stub
		return null;
	}
}
