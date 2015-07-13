package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.gcit.lms.domain.Genre;
import com.gcit.lms.domain.LibraryBranch;
@SuppressWarnings("unchecked")
public class LibraryBranchDAO extends BaseDAO<LibraryBranch>{
	
		
	public LibraryBranchDAO(Connection conn) throws Exception {
		super(conn);		
	}
	
	public void create(LibraryBranch branch) throws Exception{
		int id = saveWithID("INSERT INTO tbl_library_branch (branchName, branchAddress) VALUES(?,?)", new Object[]{branch.getBranchName(),branch.getAddress()});
		if(id!=-1){
			branch.setBranchId(id);
		}
	}
	public void update(LibraryBranch branch) throws Exception{
		save("UPDATE tbl_library_branch SET branchName = ?, branchAddress = ? WHERE branchId = ?", new Object[]{branch.getBranchName(), branch.getAddress(),branch.getBranchId()});
		System.out.println("updated "+branch.getBranchName()+" "+branch.getBranchId()+" "+branch.getAddress());
	}
	public void delete(LibraryBranch branch) throws Exception{
	save("DELETE FROM tbl_library_branch WHERE branchId = ?", new Object[]{branch.getBranchId()});	
	}

	public List<LibraryBranch> readAll() throws Exception{
		return (List<LibraryBranch>) read("SELECT * FROM tbl_library_branch", null);		
	}

	public LibraryBranch readOne(int branchId) throws Exception {
		List<LibraryBranch> branches = (List<LibraryBranch>) read("SELECT * FROM tbl_library_branch WHERE branchId = ?", new Object[] {branchId});
		if(branches!=null && branches.size()>0){
			return branches.get(0);
		}
		return null;
	}

	@Override
	public List<LibraryBranch> extractData(ResultSet rs) throws Exception {
		return extractDataFirstLevel(rs);
	}

	@Override
	public List<LibraryBranch> extractDataFirstLevel(ResultSet rs)
			throws Exception {
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
	public List<LibraryBranch> readPage(int pageNum) throws Exception {
		int start = 0;
		if(pageNum != 0){
			start = (pageNum*10);
			
		}		
		List<LibraryBranch> result = (List<LibraryBranch>) read("SELECT * FROM tbl_library_branch ORDER BY branchName ASC LIMIT ?,10", new Object[] {start});
		return result;
	}

	@Override
	public List<LibraryBranch> search(String query, int pageNum) throws Exception {
		int start = 0;
		if(pageNum != 0){
			start = (pageNum*10);
			
		}
		String s = "%"+query+"%";
		List<LibraryBranch> libs = (List<LibraryBranch>) read("SELECT * FROM tbl_library_branch WHERE branchName LIKE ? LIMIT ?,10", new Object[] {s,start});
		return libs;
	}
	
	public int getPageCount(String query) throws Exception{
		String s = "%"+query+"%";
		List<LibraryBranch> libs = (List<LibraryBranch>) read("SELECT * FROM tbl_library_branch WHERE branchName LIKE ?", new Object[] {s});
		return Math.abs((libs.size()-1)/10 +1);
	}

}
