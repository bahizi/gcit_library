package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.gcit.lms.domain.Book;
import com.gcit.lms.domain.BookLoan;
@SuppressWarnings("unchecked")
public class BookLoanDAO extends BaseDAO<BookLoan>{
	public BookLoanDAO(Connection conn) throws Exception {
		super(conn);
	}
	public void create(BookLoan bookLoan) throws Exception{
		save("INSERT INTO tbl_book_loans (bookId,branchId,cardNo,dateOut,dueDate, dateIn) VALUES(?,?,?,?,?,?)", new Object[]{bookLoan.getBookId(),bookLoan.getBranchId(),bookLoan.getCardNo(),bookLoan.getDateOut(),bookLoan.getDueDate(), bookLoan.getDateIn()});		
	}
	public void update(BookLoan bookLoan) throws Exception{
		save("UPDATE tbl_book_loans SET dateIn = ?, dueDate = ? WHERE  bookId = ? AND branchId =? AND cardNo = ? AND dateOut = ? ", new Object[]{bookLoan.getDateIn(),bookLoan.getDueDate(),bookLoan.getBookId(),bookLoan.getBranchId(),bookLoan.getCardNo(), bookLoan.getDateOut()});		
	}
	public void delete(BookLoan bookLoan) throws Exception{
		save("DELETE FROM tbl_book_loans WHERE bookId = ? AND branchId = ? AND cardNo = ? AND dateOut = ?",new Object[]{bookLoan.getBookId(),bookLoan.getBranchId(),bookLoan.getCardNo(), bookLoan.getDateOut()});		
	}
	public List<BookLoan> readAll() throws Exception{
		return (List<BookLoan>) read("SELECT * FROM tbl_book_loans", null);
	}

	public BookLoan readOne(int bookId,int branchId, int cardNo) throws Exception{
		List<BookLoan> bookLoans = (List<BookLoan>) read("SELECT * FROM tbl_book_loans WHERE bookId = ? AND  branchId = ? AND cardNo = ? ", new Object[] {bookId,branchId,cardNo});
		if(bookLoans!=null && bookLoans.size()>0){
			return bookLoans.get(0);
		}
		return null;
	}
	@Override
	public List<BookLoan> extractData(ResultSet rs) throws Exception {
		return extractDataFirstLevel(rs);
	}
	@Override
	public List<BookLoan> extractDataFirstLevel(ResultSet rs) throws Exception {
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
	public List<BookLoan> readPage(int pageNum) throws Exception {
		int start = 0;
		if(pageNum>1){
			start = (pageNum*10)+1;			 
		}		
		List<BookLoan> result = (List<BookLoan>) read("SELECT * FROM tbl_book_loans LIMIT ?,10", new Object[] {start});
		return result;
	}
	@Override
	public List<BookLoan> search(String query, int pageNum) throws Exception {
		return null;
	}
}
