/**
 * 
 */
package org.ros.android.ros_controller;

import org.ros.android.ros_controller.R;
import org.ros.android.view.RosImageView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * @author maoqizhen
 *
 */
public class Manual_mode extends Activity implements OnTouchListener{
	private View movementView;
	private View turningView;
	private TextView moveText;
	private TextView turnText;
	private final static int STANDSTILL = 0;
	private final static int FORWARD = 1;
	private final static int BACKWARD = 2;
	private final static int LEFT = 3;
	private final static int RIGHT = 4;
	private static Talker talker;
	private static RosImageView<sensor_msgs.CompressedImage> image;
	private TextView state;
	private static MediaProcessor mediaDealer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manual_mode);
		state=(TextView) findViewById(R.id.state);
		 //voice recognition
        Button button7 = (Button) findViewById(R.id.button7Main);
        button7.setOnClickListener(new OnClickListener(){
        	
        	@Override
        	public void onClick(View arg0){
        		Intent intent = new Intent();
				intent.setClass(Manual_mode.this, Voice.class);
				startActivity(intent);	
        	}
        });
        
      //the Action_mode button
        Button button8 = (Button) findViewById(R.id.button8Main);
        button8.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(Manual_mode.this, Action_mode.class);
	    		startActivity(intent); 		
			}
        });
        
      //the Video_meeting button
        Button button9 = (Button) findViewById(R.id.button9Main);
        button9.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(Manual_mode.this, Video_meeting.class);
				startActivity(intent);			
			}
        });
        ImageView mapView=(ImageView)findViewById(R.id.map);
        mapView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(Manual_mode.this, MapLocation.class);
				startActivity(intent);			
			}
        });
     initDriveButtons();
     talker = (Talker)MainActivity.getTalker();
	}
	@Override
    protected void onStart() {
        super.onStart();
        
        mediaDealer=MainActivity.getMediaProcessor();
        /*NodeMainExecutor nodeMainExecutor=MainActivity.getNodeMainExecutor();
        NodeConfiguration nodeConfiguration=MainActivity.getNodeConfiguration();
        nodeMainExecutor.execute(talker, nodeConfiguration.setNodeName("Rose_Talker_Basic"));*/
    }
	@Override
    protected void onResume() {
        super.onResume();
        image = (RosImageView<sensor_msgs.CompressedImage>) findViewById(R.id.rosImageViewMain);
		image.setTopicName("wide_stereo/left/image_color/compressed");
		image.setMessageType(sensor_msgs.CompressedImage._TYPE);
		mediaDealer.startImage(image, "android/video_viewManual");
        
    }
	 @Override
	    protected void onPause() {
	        super.onPause();
	        mediaDealer.stopImage(image);
	    }
	 @Override
	    protected void onStop() {
	        super.onStop();
	       // talker = null;
	        mediaDealer=null;
	    }
	 @Override
	    protected void onDestroy() {
	        super.onDestroy();
	        image=null;
	        talker=null;
	    }
	/*
	  * Initialize the drive control buttons and the seekbars.
	  */
	 private void initDriveButtons()
	 {
		 movementView = (View) findViewById(R.id.movecircle);		
			movementView.setOnTouchListener(this);
			
			turningView = (View) findViewById(R.id.turnslider);
			turningView.setOnTouchListener(this);
			
			moveText = (TextView) findViewById(R.id.moveText);
			turnText = (TextView) findViewById(R.id.turnText);
			
	 }
	
	 @Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.main, menu);
			return true;
		}
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
		    // Handle item selection
			Intent intent = new Intent();
		    switch (item.getItemId()) {
		        case R.id.action_mode:
					intent.setClass(Manual_mode.this, Action_mode.class);
		    		startActivity(intent); 
		            return true;
		        case R.id.video_meeting:
					intent.setClass(Manual_mode.this, Video_meeting.class);
		    		startActivity(intent); 
		            return true;
		        case R.id.map:
					intent.setClass(Manual_mode.this, MapLocation.class);
		    		startActivity(intent); 
		            return true;
		        default:
		            return super.onOptionsItemSelected(item);
		    }
		}
		
		@Override
		public boolean onTouch(View touchedView, MotionEvent event) {
			int eventaction = event.getAction(); 
	        int X = (int)event.getX(); 
	        int Y = (int)event.getY();
	        int direction = STANDSTILL;
	        boolean moving = false;
	        
	        if(touchedView.equals(movementView))
	        {          
		        switch (eventaction ) {
		        	case MotionEvent.ACTION_MOVE:
		            	if(Y<X && X>100)
		            	{
		            		direction = RIGHT;
		            	}
		            	if(Y>X && Y>100)            	
		            	{
		            		direction = BACKWARD;
		            	}
		            	if(Y>X && X<-100)
		            	{
		            		direction = LEFT;
		            	}
		            	if(Y<X && Y<-100)
		            	{
		            		direction = FORWARD;
		            	}
		            	
		        		switch (direction) {
			        		case STANDSTILL:
			        			if(moving)
				        		{
				        			talker.stopMsgThread();
				        			moving = false;
				        		}
			        			moveText.setText("Standstill, drag in a direction to move.");
			        			break;
			        		case FORWARD:
			        			talker.changeControlMessage(1.0, 0.0, 0.0, 0.0, 0.0, 0.0);
					  			talker.startMsgThread();
					  			state.setText("Moving forward...");
			            		moveText.setText("You are moving forward.");
			            		moving = true;
			        			break;
			        		case BACKWARD:
			        			talker.changeControlMessage(-1.0, 0.0, 0.0, 0.0, 0.0, 0.0);
					  			talker.startMsgThread();
					  			state.setText("Moving back...");
			            		moveText.setText("You are moving backwards.");
			            		moving = true;
			        			break;
			        		case LEFT:
			        			talker.changeControlMessage(0.0, 1.0, 0.0, 0.0, 0.0, 0.0);
					  			talker.startMsgThread();
					  			state.setText("Moving left...");
			            		moveText.setText("You are moving left.");
			            		moving = true;
			        			break;
			        		case RIGHT:
			        			talker.changeControlMessage(0.0, -1.0, 0.0, 0.0, 0.0, 0.0);
					  			talker.startMsgThread();
					  			state.setText("Moving right...");
			            		moveText.setText("You are moving right.");
			            		moving = true;			            		
			        			break;	        			
		        		}
		                break; 
		                
		        	case MotionEvent.ACTION_UP: 
		        		if(moving)
		        		{
		        			talker.stopMsgThread();
		        			moving = false;
		        		}
		        		moveText.setText("Stopped moving, drag white circle again to move.");        		
		                break;         		
		        }	
	        }
	        if(touchedView.equals(turningView))
	        {
	        	switch (eventaction ) {
	        		case MotionEvent.ACTION_MOVE:
	        			if(X<200)
	        			{
	        				direction = LEFT;
	        			}
	        			if(X>450)
	        			{
	        				direction = RIGHT;
	        			}      
	        			switch (direction) {
		        			case STANDSTILL:
		        				if(moving)
				        		{
				        			talker.stopMsgThread();
				        			moving = false;
				        		}
			        			turnText.setText("Standstill, drag in a direction to turn.");
			        			
			        			break;
		        			case LEFT:
		        				talker.changeControlMessage(0.0, 0.0, 0.0, 0.0, 0.0, 1.0);
		    		  			talker.startMsgThread();
		    		  			state.setText("Turning left...");
		        				turnText.setText("You are turning counter-clockwise.");
		        				moving = true;
			        			break;
			        		case RIGHT:
			        			talker.changeControlMessage(0.0, 0.0, 0.0, 0.0, 0.0, -1.0);
					  			talker.startMsgThread();
					  			state.setText("Turning right...");
			        			turnText.setText("You are turning clockwise.");
			        			moving = true;
			        			break;	
	        			}
	        			break;
	        			
		        	case MotionEvent.ACTION_UP:
		        		if(moving)
		        		{
		        			talker.stopMsgThread();
		        			moving = false;
		        		}
		        		turnText.setText("Stopped moving, drag blue circle again to turn.");        		
		                break; 
	        	}
	        }
	        touchedView.invalidate();		
			return true;
		}
}
