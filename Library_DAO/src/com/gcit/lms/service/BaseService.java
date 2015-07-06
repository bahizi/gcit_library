package com.gcit.lms.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class BaseService {
	private Scanner sc;
	public BaseService(){
		sc = new Scanner(System.in);
	}
	public Connection getConnection() throws Exception{
		ConnectionUtil c = new ConnectionUtil();
		Connection conn = c.createConnection();
		return conn;
	}
	
	public <T> void displayOptions(List<T> options){
		int s= options.size();
		for(int i = 1; i<s+1; i++){
			System.out.println(i+". "+ options.get(i-1));			
		}		
	}
	//displays Menu with pages, returns positive indices if the user picks something off the menu
	//return negative integers if the user picks something from the controllers (e.g. Cancel, skip, up the menu, etc.)
	public <T> int getChoiceNumber(List<T> options, List<String> controllers){
		int items = options.size();
		int pages= items/10;
		if(pages%10 !=0){
			pages++;
		}
		int curPage=0;
		int choice=11;
		boolean f= true;
		while(f ){
			HashMap<Integer, String> actions= new HashMap<Integer,String>();
			HashMap<Integer,Integer> controls = new HashMap<Integer, Integer>();			
			int remainder= options.size()-(curPage*10);
			List<T> displayPage =  options.subList(curPage*10,(curPage*10)+Math.min(10,remainder));
			
			if(displayPage.size()==0){
				System.out.println("***No data to display***");
			}
			else{
				displayOptions(displayPage);
			}
			
			System.out.println("-----------------------------------");
			for(int i = 0; i<controllers.size(); i++){
				System.out.println((displayPage.size()+i+1)+". "+ controllers.get(i));
				controls.put((displayPage.size()+i+1), i+1);					
			}
			System.out.println("-----------------------------------");
			if(curPage>0){				
				System.out.print("<<<<Previous Page ["+(displayPage.size()+controllers.size()+1)+"]");
				actions.put((displayPage.size()+controllers.size()+1), "prev");
			}
			
			if(curPage<pages-1){
				if(actions.size()>0){
					System.out.print(" -|- ");
				}
				System.out.print("Next Page["+(displayPage.size()+controllers.size()+actions.size()+1)+"]>>>>"  );
				actions.put((displayPage.size()+controllers.size()+actions.size()+1), "next");
			}
			
			if(actions.size()>0){
				System.out.println("\n-----------------------------------");
				System.out.println("***Page "+(curPage+1) +" of "+pages+"***");
				System.out.println("-----------------------------------");
			}
			choice = getInputInt(1,displayPage.size()+controllers.size()+actions.size());			
			if(choice>0 && choice<displayPage.size()+1){
				return (curPage*10+choice);
			}
			if(controls.containsKey(choice)){
				return -controls.get(choice);
			}
			else {
				if(actions.get(choice)=="prev"){
					curPage--;
				}
				else{
					curPage++;
				}
			}
		}
		return items;
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

	public String getInputString(){
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
