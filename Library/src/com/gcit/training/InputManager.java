package com.gcit.training;

import java.util.ArrayList;
import java.util.Scanner;

public class InputManager {
	public static void displayOptions(ArrayList<String> options){
		for(int i = 1; i<options.size()+1; i++){
			System.out.println(i+". "+ options.get(i-1));			
		}			
	}
	public static int getInputInt(int min, int max){
		Scanner sc= new Scanner(System.in);
		System.out.print("Input choice number ");
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
		return in;
	}
}
