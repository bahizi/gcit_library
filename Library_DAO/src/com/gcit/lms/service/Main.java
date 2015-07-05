package com.gcit.lms.service;

import java.util.ArrayList;

public class Main extends BaseService{
	private static BaseService service = new BaseService();
	
	public static void main(String[] args){
		ArrayList<String> users= new ArrayList<String>();
		ArrayList<String> controls= new ArrayList<String>();
		users.add("Librarian");
		users.add("Administrator");
		users.add("Borrower");
		controls.add("Exit");
		while(true){
			System.out.println("Welcome to the GCIT Library Management System. Which type of user are you?");
			int input = service.getChoiceNumber(users, controls);
			switch(input){
			case -1:
				//exit
				System.out.println("Exiting...");
				System.exit(0);
				break;
			case 1:
				//GO TO LIBRARIAN
				service= new LibrarianService();				
				break;
			case 2:				
				//GO TO ADMIN
				service= new Admin();
				break;
			case 3:
				//GO TO BORROWER
				service= new BorrowerService();				
				break;			
			default:
				System.err.println("Invalid user choice: "+input);				
			}

		}		
	}	
}
