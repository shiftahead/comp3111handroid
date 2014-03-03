//package comp3111.event;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//
//public class EventList {
//	
////	public ArrayList<Event> eventList = new ArrayList<Event>(); //Generic ArrayList to Store Event objects
//	public ArrayList<Event> eventList; //Generic ArrayList to Store Event objects
//	
//	//add
//	public void add(String iname, String ilocation, Calendar itime, String idescription, Calendar ireminder){
//		if(eventList.isEmpty()){
//			eventList.add(0, new Event(iname, ilocation, itime, idescription, ireminder));
//		}
//		else{
//		int i = 0;
//		while(eventList.get(i).timePriority(itime)){
//			i++;
//		}		
//		eventList.add(i, new Event(iname, ilocation, itime, idescription, ireminder));
//		}
//	}
//	
//	//delete
//	public void remove(){
////		eventList.remove(index);
//	}
//	
//	
//	//modify
//	
//	public void modify(){
//		//remove and add new
//		//OR find and change the parameter of the original event
//	}
//	
//	
//}
