package com.gcit.training;

import java.util.ArrayList;

public class Main {
	private static User user;	
	public static void main(String[] args){
		ArrayList<String> users= new ArrayList<String>();
		users.add("Librarian");
		users.add("Administrator");
		users.add("Borrower");
		users.add("Exit");
		while(true){
			System.out.println("Welcome to the GCIT Library Management System. Which type of user are you?");
			InputManager.displayOptions(users);
			int input = InputManager.getInputInt(1,users.size());
			switch(input){
			case 1:
				//GO TO LIBRARIAN
				user= new Librarian();				
				break;
			case 2:				
				//GO TO ADMIN
				user= new Admin();
				break;
			case 3:
				//GO TO BORROWER
				user= new Borrower();				
				break;
			case 4:
				System.out.println("Exiting...");
				System.exit(0);
				break;
			default:
				System.out.println("Invalid user choice: "+input);				
			}

		}
		
		
	}
	
}
