package comp3111.event;

import java.util.Calendar;

import junit.framework.TestCase;

public class EventTest extends TestCase {

	Event event=new Event("e1", "UST", Calendar.getInstance(), "d1", Calendar.getInstance());

	public void testShowEventName() {
		assertEquals("e12",event.showEventName());
	}
	
	
	public void testShowEventLocation() {
		assertEquals("UST",event.showEventLocation());
	}

	public void testShowEventDate() {
		assertEquals("UST",event.showEventDate());
	}

	public void testShowEventTime() {
		fail("Not yet implemented");
	}

	public void testShowDescription() {
		fail("Not yet implemented");
	}

	public void testShowReminder() {
		fail("Not yet implemented");
	}

	public void testAutoCorrectName() {
		fail("Not yet implemented");
	}

	public void testAutoCorrectDescription() {
		fail("Not yet implemented");
	}

	public void testTimePriorityEvent() {
		fail("Not yet implemented");
	}

	public void testTimePriorityCalendar() {
		fail("Not yet implemented");
	}

	public void testShowCurrentTime() {
		fail("Not yet implemented");
	}

	public void testShowReminderTime() {
		fail("Not yet implemented");
	}

	public void testShowEvent() {
		fail("Not yet implemented");
	}

}
