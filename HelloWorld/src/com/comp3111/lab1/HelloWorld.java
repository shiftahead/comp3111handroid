package com.comp3111.lab1;
import java.util.Date;
import java.text.SimpleDateFormat;

public class HelloWorld {
     public static void main(String[] args){
    	 System.out.println("Hello World");
    	 Date date=new Date();
    	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    	 String s = formatter.format(date); 
    	 System.out.println("It is "+ s );
    	 
     }
}
