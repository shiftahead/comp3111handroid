//package comp3111.event;
//
//
//import android.os.Bundle;
//import android.app.Activity;
//import android.view.Menu;
//import android.view.View;
//import android.view.View.OnClickListener;
////import "Event.java";
//import android.widget.Button;
//import android.widget.TextView;
//
//import java.lang.String;
//import java.util.ArrayList;
//import java.util.Calendar;
//
//
//public class MainActivity extends Activity implements OnClickListener {
//
//	
//	ArrayList<Event> eventList = new ArrayList<Event>(); //Generic ArrayList to Store Event objects
//	Event Testing=new Event(), Testing1;
//	Button btn, btnnew, btnview;
//	TextView TextDisplay;
//	int i=0;
////	TextView TextDisplay=new TextView(this);
//	
//	//Testing.showCurrentTime();
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		
//		
//		
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//		
//		
//		//Initialize instances
//		btn = (Button) findViewById(R.id.btn);
//		btnnew = (Button) findViewById(R.id.btnnew);
//		btnview = (Button) findViewById(R.id.btnview);
//		
//		TextDisplay = (TextView) findViewById(R.id.TextDisplay);
//		
//		//Set Listener
//		btn.setOnClickListener(this);
//		btnnew.setOnClickListener(this);
//		btnview.setOnClickListener(this);
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
//
//	@Override
//	public void onClick(View view) {
//		// TODO Auto-generated method stub
//		if(view == btn)
////			TextDisplay.setText(Testing.showCurrentTime());
//			//eventList.add(new Event("Tesing1", "5569", Testing.timec, "Cool!!"));
////			TextDisplay.setText("LOL");
//		if(view==btnnew){
////			eventList.add(new Event("Tesing2", "409", Testing.timec, "Oh Yeah!!"));
////			Testing1 = new Event("Tesing2", "409", Testing.timec, "Oh Yeah!!");
////			TextDisplay.setText(Testing1.showEvent());
//		}
//		if(view==btnview){
//			if(i == eventList.size())
//				TextDisplay.setText("End of the List!");
//			else
//				TextDisplay.setText(eventList.get(i++).showEvent());
//		}
//	}
//	
//	public void showList(){
//		for(int i=0 ; i < eventList.size() ; i++){
//			TextDisplay.setText(eventList.get(i).showEvent());
//		}
//		TextDisplay.setText("End of the List!");
//	}
//
//}
