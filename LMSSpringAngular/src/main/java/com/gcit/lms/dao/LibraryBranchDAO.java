package com.gcit.lms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gcit.lms.domain.LibraryBranch;

public class LibraryBranchDAO extends BaseDAO<LibraryBranch>{
	
	public void create(LibraryBranch branch){
		int id = saveWithID("INSERT INTO tbl_library_branch (branchName, branchAddress) VALUES(?,?)", new Object[]{branch.getBranchName(),branch.getAddress()});
		if(id!=-1){
			branch.setBranchId(id);
		}
	}
	public void update(LibraryBranch branch){
		template.update("UPDATE tbl_library_branch SET branchName = ?, branchAddress = ? WHERE branchId = ?", new Object[]{branch.getBranchName(), branch.getAddress(),branch.getBranchId()});
		System.out.println("updated "+branch.getBranchName()+" "+branch.getBranchId()+" "+branch.getAddress());
	}
	public void delete(LibraryBranch branch){
	template.update("DELETE FROM tbl_library_branch WHERE branchId = ?", new Object[]{branch.getBranchId()});	
	}

	public List<LibraryBranch> readAll(){
		return template.query("SELECT * FROM tbl_library_branch", this);		
	}

	public LibraryBranch readOne(int branchId){
		List<LibraryBranch> branches = template.query("SELECT * FROM tbl_library_branch WHERE branchId = ?", new Object[] {branchId}, this);
		if(branches!=null && branches.size()>0){
			return branches.get(0);
		}
		return null;
	}

@Override
	public List<LibraryBranch> extractData(ResultSet rs) throws SQLException {
		List<LibraryBranch> branches = new ArrayList<LibraryBranch>();		
		while(rs.next()){
			LibraryBranch branch = new LibraryBranch();
			branch.setBranchId(rs.getInt("branchId"));
			branch.setBranchName(rs.getString("branchName"));
			branch.setAddress(rs.getString("branchAddress"));
			branches.add(branch);
		}
		return branches;
	}

	
	@Override
	public List<LibraryBranch> search(String query, int pageNum){
		int start = 0;
		if(pageNum != 0){
			start = (pageNum*10);
			
		}
		String s = "%"+query+"%";
		List<LibraryBranch> libs = template.query("SELECT * FROM tbl_library_branch WHERE branchName LIKE ? LIMIT ?,10", new Object[] {s,start}, this);
		return libs;
	}
	
	public int getPageCount(String query){
		String s = "%"+query+"%";
		List<LibraryBranch> libs =  template.query("SELECT * FROM tbl_library_branch WHERE branchName LIKE ?", new Object[] {s}, this);
		return Math.abs((libs.size()-1)/10 +1);
	}
	@Override
	public List<LibraryBranch> readFirstLevel(String query, Object[] vals) {
		// TODO Auto-generated method stub
		return null;
	}

}
