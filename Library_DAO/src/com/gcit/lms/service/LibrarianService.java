package com.gcit.lms.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.gcit.lms.dao.LibraryBranchDAO;
import com.gcit.lms.domain.LibraryBranch;

public class LibrarianService extends BaseService {
	public LibrarianService(){
		super(); 
		menu();		
	}
	public void menu(){
		List<Object> options= new ArrayList<Object>();
		ArrayList<String> actions = new ArrayList<String>();
		options.add("Enter Branch you manage");
		actions.add("Quit to main menu");
		int input = this.getChoiceNumber(options, actions);;
		switch(input){
		case 1:
			//show library branches
			viewLibraries();
			break;
		case 2:
			//GO TO main menu
			System.out.println("Going back to the main menu");
			System.out.println("**************************************");
			break;
		default:
			System.err.println("Invalid option: #"+input);
			break;
		}
	}
	private void viewLibraries(){
		try {
			ConnectionUtil c = new ConnectionUtil();
			Connection conn =c.createConnection();
			Statement stmt= conn.createStatement();
			LibraryBranchDAO  libDAO = new LibraryBranchDAO(conn);
			List<LibraryBranch> branches = libDAO.readAll();
			conn.close();
			List<String> actions = new ArrayList<String>();
			actions.add("Previous Menu");
			System.out.println("Here are the available branches. Pick one:");

			int in =getChoiceNumber(branches, actions);
			if (in>0){
				libMenu(branches.get(in-1));
			}
			else{
				menu();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void libMenu(LibraryBranch branch){
		System.out.println("Working with "+branch);
		ArrayList<String> options = new ArrayList<String>();
		options.add("Update the details of the library");
		options.add("Add book copies to the branch");

		ArrayList<String> actions = new ArrayList<String>();		
		actions.add("Previous Menu");

		int choice= getChoiceNumber(options,actions);
		switch(choice){
		case -1:
			viewLibraries();
			break;
		case 1:
			updateLibrary(branch);
			libMenu(branch);
			break;
		case 2:
			addBookCopies(branch);
			libMenu(branch);
			break;
		default:
			//this case should never happen
			//if it does, something is wrong in BaseService's getChoiceNumber();
			System.err.println("Invalid action: Action #"+choice);
			break;
		}
	}

	private void updateLibrary(LibraryBranch branch){
		System.out.println("You have chosen to update the following branch:");
		System.out.println("\t*Branch Name: "+branch.getBranchId());
		System.out.println("\t*Branch Address: "+branch.getAddress());
		String name = renameLibrary();
		String address = changeLibraryAddress();
		if(name != "N/A"){
			branch.setBranchName(name);
		}
		if(address != "N/A"){
			branch.setAddress(address);
		}
		ConnectionUtil c = new ConnectionUtil();
		Connection conn;
		try {
			conn = c.createConnection();
			LibraryBranchDAO libDAO = new LibraryBranchDAO(conn);
			libDAO.update(branch);
			conn.close();
		} catch (Exception e) {
			System.err.println("Error: You changes were not saved. Try again. Sorry");
		}
		
	}
	private String renameLibrary(){
		String res = "";
		System.out.println("Input new Library Name [ N/A to skip ]");
		res = getInputString();
		return res;
	}
	private String changeLibraryAddress(){
		String res = "";
		System.out.println("Input new Library Address. [ N/A to skip ]");
		res = getInputString();
		return res;
	}
	private void addBookCopies(LibraryBranch branch){

	}

}
