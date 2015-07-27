package com.gcit.lms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gcit.lms.domain.Borrower;
public class BorrowerDAO extends BaseDAO<Borrower> {
	public void create(Borrower borrower) {
		int id = saveWithID("INSERT INTO tbl_borrower (name, address, phone) VALUES(?,?,?)", new Object[] {borrower.getName(),borrower.getAddress(),borrower.getPhone()});
		if(id!=-1){
			borrower.setCardNo(id);	
		}	
	}

	public void update(Borrower borrower) {
		template.update("UPDATE tbl_borrower SET name = ?, address = ?, phone = ? WHERE cardNo = ?", new Object[]{borrower.getName(), borrower.getAddress(), borrower.getPhone(), borrower.getCardNo()});
		System.out.println("Updated "+ borrower.getCardNo());
	}

	public void delete(Borrower borrower) {
		template.update("DELETE FROM tbl_borrower WHERE cardNo = ?", new Object[]{borrower.getCardNo()});		
	}


	public List<Borrower> readAll() {
		return  template.query("SELECT * FROM tbl_borrower", new Object[] {}, this);
	}

	public Borrower readOne(int cardNo) {
		List<Borrower> borrowers =  template.query("SELECT * FROM tbl_borrower WHERE cardNo = ?", new Object[] {cardNo}, this);
		if(borrowers!=null && borrowers.size()>0){
			return borrowers.get(0);
		}
		return null;
	}

	@Override
	public List<Borrower> extractData(ResultSet rs) throws SQLException {
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

	
	@Override
	public List<Borrower> search(String query, int pageNum){
		int start = 0;
		if(pageNum>1){
			start = (pageNum*10)+1;			 
		}
		String s = "%"+query+"%";
		List<Borrower> bors =  template.query("SELECT * FROM tbl_borrower WHERE name LIKE ? LIMIT ?,10", new Object[] {s,start}, this);
		return bors;
	}
	public int getPageCount(String query) {
		String s = "%"+query+"%";
		List<Borrower> bors =  template.query("SELECT * FROM tbl_borrower WHERE name LIKE ?", new Object[] {s}, this);
		return Math.abs((bors.size()-1)/10 +1);
	}

	@Override
	public List<Borrower> readFirstLevel(String query, Object[] vals) {
		// TODO Auto-generated method stub
		return null;
	}

}
