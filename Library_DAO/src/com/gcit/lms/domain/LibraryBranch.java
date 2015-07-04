package com.gcit.lms.domain;

public class LibraryBranch {
	private int branchId;
	private String branchName;
	private String address;
	public int getBranchId() {
		return branchId;
	}
	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Override 
	public String toString(){
		return branchName;
	}
}
