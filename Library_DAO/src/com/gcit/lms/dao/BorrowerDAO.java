package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.gcit.lms.domain.Borrower;
@SuppressWarnings("unchecked")
public class BorrowerDAO extends BaseDAO<Borrower> {
	public BorrowerDAO(Connection conn) throws Exception {
		super(conn);
	}

	public void create(Borrower borrower) throws Exception{
		int id = saveWithID("INSERT INTO tbl_borrower (name, address, phone) VALUES(?,?,?)", new Object[] {borrower.getName(),borrower.getAddress(),borrower.getPhone()});
		if(id!=-1){
			borrower.setCardNo(id);	
		}	
	}
	
	public void update(Borrower borrower) throws Exception{
		save("UPDATE tbl_borrower SET name = ?, address = ?, phone = ? WHERE cardNo = ?", new Object[]{borrower.getName(), borrower.getAddress(), borrower.getPhone()});		
	}
	
	public void delete(Borrower borrower) throws Exception{
		save("DELETE FROM tbl_borrower WHERE cardNo = ?", new Object[]{borrower.getCardNo()});		
	}
	
	
	public List<Borrower> readAll() throws Exception{
		return (List<Borrower>) read("SELECT * FROM tbl_borrower", new Object[] {});
	}
	
	public Borrower readOne(int cardNo) throws Exception{
		List<Borrower> borrowers = (List<Borrower>) read("SELECT * FROM tbl_borrower WHERE cardNo = ?", new Object[] {cardNo});
		if(borrowers!=null && borrowers.size()>0){
			return borrowers.get(0);
		}
		return null;
	}
	@Override
	public List<Borrower> extractData(ResultSet rs) throws Exception {
		return extractDataFirstLevel(rs);
	}

	@Override
	public List<Borrower> extractDataFirstLevel(ResultSet rs) throws Exception {
List<Borrower> borrowers = new ArrayList<Borrower>();
		
		while(rs.next()){
			Borrower bor = new Borrower();
			bor.setCardNo(rs.getInt("cardNo"));
			bor.setName(rs.getString("name"));
			bor.setAddress(rs.getString("address"));
			bor.setPhone(rs.getString("phone"));
			borrowers.add(bor);
		}
		return borrowers;
	}

}
