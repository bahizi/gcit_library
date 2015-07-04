package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
		save("UPDATE tbl_book_loans SET bookId = ?, branchId =?, cardNo = ?, dateOut = ?, dueDate = ?, dateIn = ?", new Object[]{bookLoan.getBookId(),bookLoan.getBranchId(),bookLoan.getCardNo(),bookLoan.getDateOut(),bookLoan.getDueDate(), bookLoan.getDateIn()});		
	}
	public void delete(BookLoan bookLoan) throws Exception{
		save("DELETE FROM tbl_book_loans WHERE bookId = ? AND branchId = ? AND cardNo = ?",new Object[]{bookLoan.getBookId(),bookLoan.getBranchId(),bookLoan.getCardNo()});		
	}
	public List<BookLoan> readAll() throws Exception{
		return (List<BookLoan>) read("SELECT * FROM tbl_book_loans", null);
	}
	
	public BookLoan readOne(int bookId,int branchId, int cardNo) throws Exception{
		List<BookLoan> bookLoans = (List<BookLoan>) read("SELECT * FROM tbl_book WHERE bookId = ?, branchId = ?, cardNo = ?", new Object[] {bookId,branchId,cardNo});
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
			while(rs.next()){
				loan.setBookId(rs.getInt("bookId"));
				loan.setBranchId(rs.getInt("branchId"));
				loan.setCardNo(rs.getInt("cardNo"));
				loan.setDateOut(rs.getTimestamp("dateOut"));
				loan.setDueDate(rs.getTimestamp("dueDate"));
				loan.setDateIn(rs.getTimestamp("dateIn"));
				bookLoans.add(loan);
			}
		}
		return bookLoans;
	}
}
