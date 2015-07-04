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
		libMainMenu();		
	}
	public void libMainMenu(){
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
			
			getChoiceNumber(branches, actions);
			int in= getInputInt(1,branches.size());
			if (in>0){
				lib3(in,branches.get(in-1));
			}
			else{
				libMainMenu();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void lib3(int i, LibraryBranch branch){
		
	}

}
