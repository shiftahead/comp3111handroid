package comp3111.event;

import java.util.ArrayList;
import java.util.Calendar;

import junit.framework.TestCase;

public class EventListTest extends TestCase {
	

	
	
	public void testAdd(){
		Calendar cal1 = Calendar.getInstance(); Calendar cal2 = Calendar.getInstance();
		cal2.add(cal2.MONTH, -1);
		Calendar re1 = (Calendar) cal1.clone(); Calendar re2 = (Calendar) cal2.clone();
		re1.add(re1.HOUR_OF_DAY, -1);
		re2.add(re2.HOUR_OF_DAY, -1);
		
		Event event1=new Event("e1", "UST", cal1, "d1", re1);
		Event event2=new Event("e2", "UST", cal2, "d1", re2);		
		
		EventList list1 = new EventList();
		list1.add("e1", "UST", cal1, "d1", re1);
		list1.add("e2", "UST", cal2, "d1", re2);
		
		assertEquals("e2",((list1.eventList).get(0)).showEventName());
		
		
	}

}
