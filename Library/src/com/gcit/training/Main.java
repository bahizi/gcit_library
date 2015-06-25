package com.gcit.training;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	public static void displayOptions(ArrayList<String> options){
		for(int i = 1; i<options.size()+1; i++){
			System.out.println(i+". "+ options.get(i-1));			
		}			
	}
	
	public static int getInputInt(int topIdx){
		Scanner sc= new Scanner(System.in);
		System.out.print("Input choice number: ");
		String ina= sc.nextLine();
		int in;
		while(!ina.matches("\\d+")){
			System.out.println("Your input must be a positive integer. Try again:");
			ina= sc.nextLine();
		}
		in=Integer.parseInt(ina);		
		while(in<1 || in>topIdx){
			System.out.println("Your input has to be between 1 and "+ topIdx+". You entered "+ina);
			System.out.println("Try again");
			in = getInputInt(topIdx);
			}
		return in;
	}
	
	public static void librarian(){
		System.out.println("Hello, Librarian!");
		ArrayList<String> options= new ArrayList<String>();
		options.add("Enter Branch you manage");
		options.add("Quit to previous menu");
		displayOptions(options);
		int input = getInputInt(options.size()+1);
		System.out.println("You entered "+input);
		switch(input){
			case 1:
				//show library branches				
				try {
					Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root","!umug3nI4");
					Statement stmt= conn.createStatement();
					String selectquery= "SELECT * FROM tbl_library_branch";
					ResultSet rs;
					rs = stmt.executeQuery(selectquery);
					ArrayList<String> branches=new ArrayList<String>();
					while(rs.next()){
						branches.add(rs.getString("branchName"));
					}
					branches.add("Previous Menu");
					System.out.println("Here are the available branches. Pick one:");
					displayOptions(branches);
					int in= getInputInt(branches.size());
					if (in<branches.size()){
						System.out.println("You picked "+branches.get(in-1));
						ArrayList<String> actions= new ArrayList<String>();
						actions.add("Update the details of the library");
						actions.add("Add book copies to the branch");
						actions.add("Previous Menu");
						displayOptions(actions);
						int action=getInputInt(actions.size());
					}
					else{
						librarian();
						System.exit(0);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;
			case 2:
				//GO TO main menuADMIN
				System.out.println("Going back to the main menu");
				mainMenu();
				break;
			default:
				System.out.println("enter a valid input");
				
		}
	}
	
	public static void admin(){
		System.out.println("Hello, Admin!");

	}
	
	public static void borrower(){
		System.out.println("Hello, Peasant!");

	}
	public static void mainMenu(){
		ArrayList<String> users= new ArrayList<String>();
		users.add("Librarian");
		users.add("Administrator");
		users.add("Borrower");
		users.add("Exit");
		System.out.println("Welcome to the GCIT Library Management System. Which type of user are you?");
		displayOptions(users);
		int input = getInputInt(users.size());
		switch(input){
			case 1:
				//GO TO LIBRARIAN
				librarian();
				break;
			case 2:
				//GO TO ADMIN
				admin();
				break;
			case 3:
				//GO TO BORROWER
				borrower();
				break;
			case 4:
				System.out.println("Exiting...");
				System.exit(0);
				break;
			default:
				System.out.println("enter a valid input");				
		}
	}
	
	public static void main(String[] args) {
		mainMenu();
	}
}
