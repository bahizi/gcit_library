package com.gcit.training;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class User {
	private Scanner sc;
	public User(){
		sc = new Scanner(System.in);
	}
	protected Connection getConnection() throws SQLException{
		return DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root","!umug3nI4"); 
	}
	public void displayOptions(ArrayList<String> options){
		int s= options.size();
		for(int i = 1; i<s+1; i++){
			System.out.println(i+". "+ options.get(i-1));			
		}		
	}
	public int getInputInt(int min, int max){
		System.out.println("Input choice number ");
		System.out.print(">>");
		String ina= sc.nextLine();
		int in;
		while(!ina.matches("\\d+")){
			System.out.println("Your input must be a positive integer. Try again:");
			System.out.print(">>");
			ina= sc.nextLine();
		}
		in=Integer.parseInt(ina);		
		while(in<min || in>max){
			System.out.println("Your input has to be between "+min+" and "+ max+". You entered "+ina);
			System.out.println("Try again");
			in = getInputInt(min,max);
		}
		System.out.println("****************************************");
		
		return in;
	}	
	//dont share the scanner
	
	public  String getInputString(){
		System.out.print(">>");
		String input= sc.nextLine();
		while(input.length()<2 | input.length()>45){
			if(input.length()>45){
				System.out.println("Your input is too long. Try again with less than 45 characters");					
			}
			else{
				System.out.println("Your input is too short. Try again with more than 2 characters.");					
			}
			System.out.print(">>");
			input= sc.nextLine();
		}
		System.out.println("****************************************");
		
		return input;
		
	}
	

}
