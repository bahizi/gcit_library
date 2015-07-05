package com.gcit.lms.service;

import java.util.ArrayList;



public class Test{
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<String> options= new ArrayList<String>();
		for(int i = 0;i<54;i++){
			String.valueOf(i);
			options.add(String.valueOf(i));
		}
		BaseService andrei = new BaseService();
		ArrayList<String> controls = new ArrayList<String>();
		controls.add("Cancel");
		controls.add("Skip");
		controls.add("Fuck Off");
		controls.add("Go to DC");
		int i= andrei.getChoiceNumber(options, controls);
		System.out.println("You enterd "+i);		

	}

}
