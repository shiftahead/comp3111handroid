# System Requirements #

[User Interface](System_Requirement#User_Interface.md)
  * [1. Map View](System_Requirement#1._Map_View.md)
  * [2. List View](System_Requirement#2._List_View.md)
  * [3. Settings View](System_Requirement#3._Settings_View.md)
[Functional Requirements](System_Requirement#Functional_Requirements.md)
  * [1. Map](System_Requirement#1._Map.md)
    * [1.1 Default Map Properties Settings](System_Requirement#1.1_Default_Map_Properties_Settings.md)
    * [1.2 Drawing on the Map](System_Requirement#1.2_Drawing_on_the_Map.md)
      * [1.2.1 Marker](System_Requirement#1.2.1_Marker.md)
      * [1.2.2 Info Window](System_Requirement#1.2.2_Info_Window.md)
      * [1.2.3 Shapes](System_Requirement#1.2.3_Shapes.md)
    * [1.3 Interacting with the Map](System_Requirement#1.3_Interacting_with_the_Map.md)
    * [1.4 Indoor Maps (Optional)](System_Requirement#1.4_Indoor_Maps_(Optional).md)
  * [2. Event List](System_Requirement#2._Event_List.md)
    * [2.1 Event Creation](System_Requirement#2.1_Event_Creation.md)
    * [2.2 Event Name](System_Requirement#2.2_Event_Name.md)
    * [2.3 Date and Time](System_Requirement#2.3_Date_and_Time.md)
    * [2.4 Location](System_Requirement#2.4_Location.md)
    * [2.4 Event Type](System_Requirement#2.5_Event_Type.md)
    * [2.6 Reminder](System_Requirement#2.6_Reminder.md)
    * [2.7 Description](System_Requirement#2.7_Description.md)
    * [2.8 Transportation Type](System_Requirement#2.8_Transportation_Type.md)
    * [2.9 Event Manipulation](System_Requirement#2.9_Event_Manipulation.md)
    * [2.10 Compulsory or Optional](System_Requirement#2.10_Compulsory_or_Optional.md)
  * [3. Settings](System_Requirement#3._Settings.md)
    * [3.1 General Settings](System_Requirement#3.1_General_Settings.md)
    * [3.2 Login](System_Requirement#3.2_Login.md)
    * [3.3 About us](System_Requirement#3.3_About_us.md)


---


# User Interface #

## 1. Map View ##
The Map View is the startup page of the app on the middle page. It can be loaded by tapping the middle icon "Map" at the bottom of the main page. It shows the scheduled events on Google Maps as following.<br><br>
<img src='https://comp3111handroid.googlecode.com/svn/wiki/map1.png' width='40%' height='40%' />
<img src='https://comp3111handroid.googlecode.com/svn/wiki/map2.png' width='40%' height='40%' />
<br>
Some Markers will be shown on the map as well, based on the locations of the scheduled events.<br>
<br>
<h2>2. List View</h2>
The List View can be loaded by tapping the left icon "Events" at the bottom of the main page. It shows the list of the user's scheduled events in chronological order as following.<br><br>
<img src='https://comp3111handroid.googlecode.com/svn/wiki/list.png' width='40%' height='40%' />
<br>

<h2>3. Settings View</h2>
The Settings View  can be loaded by tapping the right icon "Settings" at the bottom of the main page. It shows the settings of this app, which contains user's preference, account, map and event settings and other information as following.<br><br>
<img src='https://comp3111handroid.googlecode.com/svn/wiki/settings.png' width='40%' height='40%' />
<br>
<hr />

<h1>Functional Requirements</h1>

<h2>1. Map</h2>

The default page of this application is a world map based on the Google Maps Android API v2.<br>
<br>
<h3>1.1 Default Map Properties Settings</h3>

<ul><li>The default location of the Google Map is set to London, in this Application it is modified to HKUST with proper Latitude and Longitude (22.3375, 114.2630) and proper zoom in scale (13).<br>
</li><li>The default Map Type is set to "Normal", as following.<br>
<img src='https://comp3111handroid.googlecode.com/svn/wiki/map3.png' width='40%' height='40%' />
</li><li>Also, the type can be set as "Satellite" or "Hybrid".<br>
<img src='https://comp3111handroid.googlecode.com/svn/wiki/map4.png' width='335' height='595' />
<img src='https://comp3111handroid.googlecode.com/svn/wiki/map5.png' width='335' height='595' /></li></ul>

<h3>1.2 Drawing on the Map</h3>

<h4>1.2.1 Marker</h4>

<ul><li>The user can draw a marker on the map by creating a event.<br>
</li><li>The user can draw a marker on the map by searching on a place.<br>
</li><li>The user can draw a marker on the map by clicking on the map.<br>
</li><li>Clicking on the existing marker can show the info window of the specific event.<br>
</li><li>Clicking and dragging the marker can change the position of the marker, after dragging there will be a popup window asking whether to change the location of the event to the newer position.<br>
</li><li>The user can define the color of the marker to categorize his or her event.<br>
<br>
<img src='https://comp3111handroid.googlecode.com/svn/wiki/marker.png' width='40%' height='40%' /><br>
A marker is shown on the map, indicating the location of the event.</li></ul>

<h4>1.2.2 Info Window</h4>

<ul><li>The info window will pop up if the user clicks on the event marker.<br>
</li><li>The info window will pop up if the user clicks on the event from the event list.<br>
</li><li>Once an event is created or edited under the Map page, the info window and the marker of the event will automatically show up.<br>
</li><li>Click on the info window will bring the user to the event page, showing the detailed information of the specific event.<br>
</li><li>The title of the info window should be the title of the event.<br>
</li><li>The content of the info window should be the Date, Time and Location of the event.</li></ul>

<h4>1.2.3 Shapes</h4>

<ul><li>Polylines are used to show the proper guidance on how to reach the destination.</li></ul>

<h3>1.3 Interacting with the Map</h3>

<ul><li>Find the user's current location and put that view at the center<br>
</li><li>Users can use special gesture to zoom, rotate or shift map view<br>
</li><li>When rotating maps, on the left top corner of there will be a compass to show direction.</li></ul>

<h3>1.4 Indoor Maps (Optional)</h3>

<ul><li>The user could check the indoor map based on the Google Indoor Map.<br>
</li><li>The user's indoor location could be generated using Wifi Technology.</li></ul>

<hr />

<h2>2. Event List</h2>

<h3>2.1 Event Creation</h3>
In "Adding event"  page, user will be required to input the following information: Name of the event, Date and time, Location(to be confirmed upon a map), Event type and optional fields including: Timed reminder, Description and Transportation type in their corresponding fields.<br>
<br>
After filling in all the required fields of an event, taping "Add", the event will be successfully added and being showed on event list and map while taping Cancel (or return ), a message will be prompted to ask whether user is sure to return before going back to the map or event list(depending on where the user jumps into adding section).<br>
<br>
This is a proposed adding event page.<br>
<img src='https://comp3111handroid.googlecode.com/svn/wiki/addevent.png' width='40%' height='40%' />
<h3>2.2 Event Name</h3>
User will be required to input the name of the event which must consist of at least 1 character and at most 64 characters. All space or special characters before first English or number characters will be ignored. Other than that, there are no further restrictions.<br>
<br>
<h3>2.3 Date and Time</h3>
User will be required to fill in two different fields, namely "Date" and "Time". User can set both fields by scrolling up and down the scroll bars and tapping on particular item. (Time will be shown in 24 hour format)<br>
<br>
<h3>2.4 Location</h3>
User will be required to first input the location (Auto fill function will be provided), and Taped search. A map will be showed with a marker on the particular location. User will be required to confirm whether the marker is at the correct position. User can also perform various task like changing location by dragging the marker.<br>
<br>
<h3>2.5 Event Type</h3>
User will be required to choose from "Work", "Academic" and Entertainment" in a scroll bar. User may also create its own type<br>
<br>
<h3>2.6 Reminder</h3>
<ul><li>User may initiate the reminder function for the event by setting a time for the notification to pop up. User will set the time by scroll bar. Note that the time cannot be later than the time of the event.</li></ul>

<ul><li>The system will check the remaining time for the next event, calculate the time needed based on user's current location and remind the user if the remaining time is shorter than the time needed.</li></ul>

<h3>2.7 Description</h3>
User may add any remarks about the event. The maximum length of this field is 256 characters. As mentioned before, all spaces or special characters before first English or number characters will be ignored. Other than that, there are no further restrictions.<br>
<br>
<h3>2.8 Transportation Type</h3>
User may select from a few choices like bus, on foot, MTR, minibus, etc.<br>
<br>
<h3>2.9 Event Manipulation</h3>
In any detailed information page of an event, there will be two buttons, namely "Modify" and "Delete",<br>
<ul><li>When "Modify" is tapped, user will be allowed to input information same as they were when they are adding the event.<br>
</li><li>When "Delete" is tapped, a warning message will be prompted to confirm the action. Tap "Confirm", the specific event wil be deleted. Tap "Cancel", it will return to the page you were in (Either map or event list).</li></ul>

<h3>2.10 Compulsory or Optional</h3>
A check button will be provided to the user to specify whether the event is compulsory or optional, if an event is set as compulsory, the user’s location will be checked at the start time of the event.<br>
<br>
<br>
<hr />

<h2>3. Settings</h2>

<h3>3.1 General Settings</h3>

<ul><li>The default color of the marker.<br>
</li><li>The default location of the map.<br>
</li><li>The default map type: Normal, Satellite or Hybrid.<br>
</li><li>The default page: Map or Event List<br>
</li><li>The default event periods showing on the map (1 - 7 Day).</li></ul>

<h3>3.2 Login</h3>

<ul><li>Log in with Google account and synchronize the event from today to Google Calendar.</li></ul>

<h3>3.3 About us</h3>

<ul><li>An introduction to our team.<br>
</li><li>Privacy issue.<br>
</li><li>Miscellaneous information.