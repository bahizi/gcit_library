package com.gcit.training;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class Librarian extends User{
	public Librarian(){
		super();
		lib1();		
	}
	private void lib1(){
		ArrayList<String> options= new ArrayList<String>();
		options.add("Enter Branch you manage");
		options.add("Quit to main menu");
		displayOptions(options);
		int input = getInputInt(1,options.size()+1);
		switch(input){
		case 1:
			//show library branches
			lib2();
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

	//displays the available library branches
	//Prompts to  user to choose which one to work with.
	private void lib2(){
		try {
			Connection conn =getConnection();//
			Statement stmt= conn.createStatement();
			String selectquery= "SELECT * FROM tbl_library_branch ORDER BY branchId ASC";
			ResultSet rs;
			rs = stmt.executeQuery(selectquery);
			ArrayList<String> branches=new ArrayList<String>();
			while(rs.next()){
				branches.add(rs.getString("branchName"));
			}
			conn.close();
			branches.add("Previous Menu");
			System.out.println("Here are the available branches. Pick one:");
			displayOptions(branches);
			int in= getInputInt(1,branches.size());
			if (in<branches.size()){
				lib3(in,branches.get(in-1));
			}
			else{
				lib1();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void lib3(int libId,String libName){
		System.out.println("Working with "+libName);
		ArrayList<String> actions= new ArrayList<String>();
		actions.add("Update the details of the library");
		actions.add("Add book copies to the branch");
		actions.add("Previous Menu");
		displayOptions(actions);
		int action=getInputInt(1,actions.size());
		switch(action){
		case 1:
			updateLibrary(libId,libName);
			lib3(libId,libName);
			break;
		case 2:
			addBookCopies(libId);
			lib3(libId,libName);
			break;
		case 3:
			lib2();
			break;			
		default:
			//this case should never happen
			System.err.println("Invalid action: Action #"+action);
			break;
		}
	}	
	private void updateLibrary(int libId, String libName){
		System.out.println("You have chosen to update the following branch:");
		System.out.println("\t*BranchId: "+libId);
		System.out.println("\t*Branch Name: "+libName);
		System.out.println("Enter ‘quit’ at any prompt to cancel operation.");
		if(renameLibrary(libId)){
			changeLibraryAddress(libId);
		}
	}
	private boolean renameLibrary(int libId){
		boolean cont = false;//this boolean will determine whether we continue with the next steps or not..
		System.out.println("Please enter new branch name or enter N/A for no change");
		String input= getInputString();
		switch(input){
		case "quit":
			cont=false;
			break;
		case "N/A":
			System.out.println("No changes to name..");
			cont=true;
			break;
		default:
			try {
				Connection conn= getConnection();
				PreparedStatement pstmt= conn.prepareStatement("UPDATE tbl_library_branch SET branchName= ? WHERE branchId= ?");
				pstmt.setString(1, input);
				pstmt.setInt(2, libId);
				pstmt.execute();
				conn.close();
				System.out.println("Success!");
				cont=true;
			} catch (SQLException e) {
				System.err.println("Error while preparing update statement.Exiting program...");
				System.exit(1);
			}				
		}
		return cont;
	}
	private void changeLibraryAddress(int libId){
		System.out.println("Please enter new branch address or enter N/A for no change");
		String address= getInputString();
		switch(address){
		case "quit":
			lib1();
			break;
		case "N/A":
			System.out.println("No changes to the address");
			break;
		default:
			try {
				Connection conn= getConnection();
				PreparedStatement pstmt= conn.prepareStatement("UPDATE tbl_library_branch SET branchAddress=? WHERE branchId=?");
				pstmt.setString(1, address);
				pstmt.setInt(2, libId);
				pstmt.executeUpdate();
				conn.close();
				System.out.println("Success!");
			} catch (SQLException e) {
				System.err.println("Error while preparing update statement.Exiting program...");
				System.exit(1);
			}				
		}
	}
	private void addBookCopies(int libId){
		System.out.println("Adding books to the library id #"+ libId);
		System.out.println("Pick the Book you want to add copies of, to your branch:");
		try {
			Connection conn= getConnection();
			Statement stmt= conn.createStatement();
			ResultSet rs=stmt.executeQuery("SELECT title FROM tbl_book ORDER by tbl_book.bookId");
			ArrayList<String> titles= new ArrayList<String>();
			while(rs.next()){
				titles.add(rs.getString("title"));
			}
			conn.close();
			titles.add("Cancel Operation");
			displayOptions(titles);
			int title= getInputInt(1,titles.size());
			if(title==titles.size()){
				lib1();
			}
			else{
				Connection pconn= getConnection();
				PreparedStatement pstmt= pconn.prepareStatement("SELECT noOfCopies FROM tbl_book_copies WHERE bookId=? LIMIT 1");
				pstmt.setInt(1, title);
				ResultSet prs=pstmt.executeQuery();
				prs.next();
				int numCopies= prs.getInt("noOfCopies");
				pconn.close();
				System.out.println("Existing number of Copies: "+numCopies);
				System.out.println("Enter new number of copies:");
				int n= getInputInt(0,10000);
				pconn= getConnection();
				pstmt= pconn.prepareStatement("UPDATE tbl_book_copies SET noOfCopies=? WHERE bookId=? AND branchId = ?");
				pstmt.setInt(1, n);
				pstmt.setInt(2, title);
				pstmt.setInt(3,libId);
				
				pstmt.executeUpdate();
				pconn.close();
				System.out.println("Updated!");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
