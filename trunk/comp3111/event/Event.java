package comp3111.event;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Event {
	private String name;
	private String location;
	private Calendar time;
	private	String description;
	private Calendar reminder;
	private Boolean compulsory;
	private eventType type;
	private transportationType ttype;
	
	//type will have to write a enium
	private enum eventType {
		   WORKING,
		   ENTERTAINMENT,
		   ACADEMIC
	}
	//transportation type will have to write a enium
	private enum transportationType {
		   BUS, 
		   MTR,
		   MINIBUS
	}
	
	
	//Date(int year, int month, int day, int hour, int minute)
	Event(){
	}
	
	Event(String iname, String ilocation, Calendar itime, String idescription, Calendar ireminder, Boolean icompulsory, eventType itype, transportationType ittype){
		name = iname;
		location = ilocation;
		time = itime;
		description = idescription;
		reminder = ireminder;
		compulsory = icompulsory;
		type = itype;
		ttype = ittype;
	}

	
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

	
	public String getName(){
		return name;
	}
	public String getLocation(){
		return location;
	}
	public Calendar getTime(){
		return time;
	}
	public String getDescription(){
		return description;
	}
	public Calendar getReminder(){
		return reminder;
	}
	public Boolean getCompulsory(){
		return compulsory;
	}
	public String getEventType(){
		switch(type){
			 case WORKING:
				 return "Working";
			 case ENTERTAINMENT:
				 return "Entertainment";
			 case ACADEMIC:
				 return "Academic";
		}
		return "Error Occurs";
	}
	public String getTransportationType(){
		switch(ttype){
		 case BUS:
			 return "Working";
		 case MTR:
			 return "Entertainment";
		 case MINIBUS:
			 return "Academic";
		}
		return "Error Occurs";
	}
	
//	
//	public Boolean timePriority(Event next){ //return true for earlier event(or happens at the same time)
//		return !time.after(next.time);
//	}	
//	// for after(), return true when this Calendar is after calendar, false otherwise.
//
//	public Boolean timePriority(Calendar next){ //return true for earlier event(or happens at the same time)
//		return !time.after(next);
//	}	
}