package com.comp3111.lab1;

public class printCustom {
   
   public static void main(String[] args)
   {
	   Custom student = new Custom();
	   student.changename("Zhang Tian");
	   student.changeage(99);
       student.changedept("CSE");
	   System.out.println(student.toString());
   }
}
