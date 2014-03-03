package comp3111.event;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Event {
	private String name;
	private String location;
	private Calendar time;
	private	String description;
	private Calendar reminder;
//	Button btn;  ((error
	
	//type will have to write a enium
	private enum type {
		   WORKING,
		   ENTERTAINMENT,
		   ACADEMIC
	}
	//transportation type will have to write a enium
	private enum transportationType {
		   BUS, 
		   MTR	
	}
	
	
	//Date(int year, int month, int day, int hour, int minute)
	Event(){
		name = "Testing1";
		location = "UST";
		
		time=Calendar.getInstance(); // for tetsing
		description = "Any Remarks?";
		
		reminder = (Calendar) time.clone();
		reminder.add(reminder.HOUR_OF_DAY, -1);  // to set the reminder

	}
	
	Event(String iname, String ilocation, Calendar itime, String idescription, Calendar ireminder){
		name = iname;
		location = ilocation;
		time = itime;
		description = idescription;
		reminder = ireminder;
				
	}
	
	//Constructor
	
	//Add
	//Delete
	//Modify
	
//	int 	compareTo(Date date)
//	Compare the receiver to the specified Date to determine the relative ordering.
//	boolean 	equals(Object object)
//	Compares the specified object to this Date and returns if they are equal.
	
	
	public String showEventName(){
		return name;
	}
	
	public String showEventLocation(){
		return location;
	}
	
	public String showEventDate(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd - MM - yyyy");
		return dateFormat.format(time.getTime());
	}

	public String showEventTime(){
		SimpleDateFormat timeFormat = new SimpleDateFormat("hh : mm");
		return timeFormat.format(time.getTime());
	}
	
	public String showDescription(){
		return description;
	}
	
	public String showReminder(){
		SimpleDateFormat timeFormat = new SimpleDateFormat("hh : mm");
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd - MM - yyyy");
		
		return timeFormat.format(reminder.getTime())+" on "+dateFormat.format(reminder.getTime());
	}
	
	
		
	
	public String autoCorrectName(String name){   //Java is passed by value
		while(!Character.isLetterOrDigit(name.charAt(0)))  //return true if it is a letter or digit
			name = name.substring(1);
		while(Character.isWhitespace(name.charAt(name.length())))	//return true if it is a whitespace
			name = name.substring(0, name.length()-1);
		return name;
	}
	
	public String autoCorrectDescription(String string){
		while(Character.isLetterOrDigit(string.charAt(0)))    //return true if it is a letter or digit
			string = string.substring(1);
		while(Character.isWhitespace(string.charAt(string.length())))	//return true if it is a whitespace
			string = string.substring(0, string.length()-1);
		return string;		
		
	}

	
	
	public Boolean timePriority(Event next){ //return true for earlier event(or happens at the same time)
		return !time.after(next.time);
	}	
	// for after(), return true when this Calendar is after calendar, false otherwise.

	public Boolean timePriority(Calendar next){ //return true for earlier event(or happens at the same time)
		return !time.after(next);
	}	

	
	
	
	public String showCurrentTime(){
		SimpleDateFormat timeFormat = new SimpleDateFormat("hh : mm");
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd - MM - yyyy");
		
		return timeFormat.format(time.getTime())+" at "+dateFormat.format(time.getTime());
//		return timeFormat.format(time)+" at "+dateFormat.format(time);
	}
	
	public String showReminderTime(){
		SimpleDateFormat timeFormat = new SimpleDateFormat("hh : mm");
//		SimpleDateFormat dateFormat = new SimpleDateFormat("dd - MM - yyyy");
		return timeFormat.format(reminder.getTime());
//		return timeFormat.format(reminder.getTime())+" at "+dateFormat.format(reminder.getTime());
	}

	public String showEvent(){
		return "Name:  " + name
				+ "\nTime:  " + showCurrentTime() 
				+ "\nLocation:  "+ location 
				+ "\nDescription:  "+ description
				+ "\nTimed Reminder:  "+showReminderTime();
	}
	
	
	//Reminder, probably timed one, date
	//location-based should wait API implementation.
}

//arraylist

//Lateleng
//
//getposition