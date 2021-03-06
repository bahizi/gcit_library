package com.gcit.training;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class User {
	private Scanner sc;
	public User(){
		sc = new Scanner(System.in);
	}
	public void displayOptions(List<String> options){
		int s= options.size();
		for(int i = 1; i<s+1; i++){
			System.out.println(i+". "+ options.get(i-1));			
		}		
	}
	public int getChoiceNumber(ArrayList<String> options, ArrayList<String> controllers){
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
			List<String> displayPage =  options.subList(curPage*10,(curPage*10)+Math.min(10,remainder));
			displayOptions(displayPage);
			System.out.println("-----------------------------------");
			for(int i = 0; i<controllers.size(); i++){
				System.out.println((displayPage.size()+i+1)+". "+ controllers.get(i));
				controls.put((displayPage.size()+i+1), i+1);					
			}
			System.out.println("-----------------------------------");
			if(curPage>0){				
				System.out.print("<<<<Previous Page ["+(displayPage.size()+controllers.size()+1)+"]");
				actions.put((displayPage.size()+controllers.size()+2), "prev");
			}
			
			if(curPage<pages-1){
				if(actions.size()>0){
					System.out.print(" -|- ");
				}
				System.out.print("Next Page["+(displayPage.size()+controllers.size()+actions.size()+1)+"]>>>>"  );
				actions.put((displayPage.size()+controllers.size()+actions.size()+2), "next");
			}
			
			if(actions.size()>0){
				System.out.println("\n-----------------------------------");
				System.out.println("***Page "+(curPage+1) +" of "+pages+"***");
				System.out.println("-----------------------------------");
			}
			choice = getInputInt(1,displayPage.size()+controllers.size()+actions.size()+1);			
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
