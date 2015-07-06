package com.gcit.lms.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.gcit.lms.dao.BookCopiesDAO;
import com.gcit.lms.dao.LibraryBranchDAO;
import com.gcit.lms.domain.BookCopies;
import com.gcit.lms.domain.LibraryBranch;
@SuppressWarnings("unchecked")
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
		case -1:
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
		ConnectionUtil c = new ConnectionUtil();		
		try {
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
			conn.close();
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
		actions.add("Pick a different library");
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
		System.out.println("\t*Branch Name: "+branch);
		System.out.println("\t*Branch Address: "+branch.getAddress());
		String name = renameLibrary();
		String address = changeLibraryAddress();
		if(!name.equals("N/A")){			
			branch.setBranchName(name);
		}
		if(!address.equals("N/A")){
			branch.setAddress(address);
		}
		ConnectionUtil c = new ConnectionUtil();
		Connection conn = null;
		try {
			conn = c.createConnection();
			LibraryBranchDAO libDAO = new LibraryBranchDAO(conn);
			libDAO.update(branch);
			conn.commit();
			conn.close();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				//bad
				System.err.println("Unexpected Error while rolling back changes");
				System.exit(1);
			}
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
		System.out.println("Adding books to "+branch);
		ConnectionUtil c = new ConnectionUtil();
		try {
			Connection conn = c.createConnection();
			BookCopiesDAO bookCopiesDAO = new BookCopiesDAO(conn);
			
			List<BookCopies> bookCopies = (List<BookCopies>) bookCopiesDAO.read("SELECT * FROM tbl_book_copies WHERE branchId = ?", new Object[] {branch.getBranchId()});
			System.out.println("Pick a book: ");
			ArrayList<String> actions = new ArrayList<String>();
			actions.add("Cancel");
			int choice = getChoiceNumber(bookCopies,actions);
			if(choice<0){
				//go back
			}
			else{
				BookCopies toAdd = bookCopies.get(choice-1);
				System.out.println("Enter number of books to add:[0 to cancel]");
				int in = getInputInt(0,10000-bookCopies.size());
				if(in==0){
					System.out.println("No changes made");
				}
				else{
					toAdd.setNoOfCopies(toAdd.getNoOfCopies()+in);
					bookCopiesDAO.update(toAdd);
				}			
			}
			conn.commit();
			conn.close();
		} catch (Exception e) {
			System.err.println("Error while fetching books for this branch #"+branch.getBranchId());
			e.printStackTrace();
		}
	}
	

}
