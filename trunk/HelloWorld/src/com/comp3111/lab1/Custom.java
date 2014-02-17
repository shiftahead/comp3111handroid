package com.comp3111.lab1;

public class Custom {
     private String name;
     private int age;
     private String dept;
     public void changename(String a){
    	 name = a;
     }
     public void changeage(int a){
    	 age = a;
     }
     public void changedept(String a){
    	 dept = a;
     }
     public String toString(){
    	 String s = name + " is "+ age + " years old and  a student in "+ dept ;     
    	 return s;
    	 }
 }
