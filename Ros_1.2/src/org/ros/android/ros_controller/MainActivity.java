/*
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.ros.android.ros_controller;

import android.content.Intent;

import android.os.Bundle;
import org.ros.address.InetAddressFactory;
import org.ros.android.RosActivity;
import org.ros.android.ros_controller.R;
import org.ros.android.ros_controller.robot.Talker_Real;
import org.ros.node.AbstractNodeMain;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;
import android.view.View;	
import org.ros.android.ros_controller.robot.*;
import org.ros.android.rviz_for_android.*;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;

import android.widget.Spinner;

import android.widget.Toast;
public class MainActivity extends RosActivity {
	private static NodeMainExecutor nodeMainExecutor;
	private static NodeConfiguration nodeConfiguration;
	private static AbstractNodeMain talker;
	private Spinner spinner;
	private static MediaProcessor mediaDealer;
	private int modeCode;
	public MainActivity ()      
	{
		// The RosActivity constructor configures the notification title and ticker
		// messages.
		super("ros basic controller", "ros basic controller");
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
        spinner = (Spinner) findViewById(R.id.modeChooser);
     // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
             R.array.mode_chooser, android.R.layout.simple_spinner_item);
     // Specify the layout to use when the list of choices appears
     adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
     // Apply the adapter to the spinner
     spinner.setAdapter(adapter);
       
	}
	/*
	 * clickHandler, will start new activity when clicked on a button
	 */
    public void myClickHandler(View target)
    { 
    	Intent intent = new Intent();
    if(modeCode==0){                               //simulation mode
        switch (target.getId()) {  
        case R.id.btnManualModeMainNew:   
    		intent.setClass(MainActivity.this, Manual_mode.class);
    		startActivity(intent); 
            break; 

        case R.id.btnMapModeMainNew:   
    		intent.setClass(MainActivity.this, MapLocation.class);
    		startActivity(intent);  
            break; 
        case R.id.btnMeetingModeMainNew:   
    		intent.setClass(MainActivity.this, Video_meeting.class);
    		startActivity(intent);  
            break; 
        case R.id.btnVoiceRecModeMainNew:   
    		intent.setClass(MainActivity.this, Voice.class);
    		startActivity(intent);  
            break; 
        case R.id.btnActionModeMainNew:   
    		intent.setClass(MainActivity.this, Action_mode.class);
    		startActivity(intent);  
            break; 
        }
    }
    else{                                //real control mode
    	switch (target.getId()) {  
        case R.id.btnManualModeMainNew:   
    		intent.setClass(MainActivity.this, Manual_mode_real.class);
    		startActivity(intent); 
            break; 

        case R.id.btnMapModeMainNew:   
        	NodeMainExecutor_dealer.setNodeMainExecutor(getNodeMainExecutor());
        	NodeMainExecutor_dealer.setNodeConfiguration(getNodeConfiguration());
        	NodeMainExecutor_dealer.setMap_talker(MainActivity.getTalker());
    		intent.setClass(MainActivity.this, Rviz_map.class);
    		startActivity(intent);  
            break; 
        case R.id.btnMeetingModeMainNew:   
    		intent.setClass(MainActivity.this, Video_meeting_real.class);
    		startActivity(intent);  
            break; 
        case R.id.btnVoiceRecModeMainNew:   
    		intent.setClass(MainActivity.this, Voice.class);
    		startActivity(intent);  
            break; 
        case R.id.btnActionModeMainNew:   
    		intent.setClass(MainActivity.this, Action_mode_real.class);
    		startActivity(intent);  
            break; 
        }
    	
    }
   }
	@Override
	protected void init(NodeMainExecutor nodeMainEx) {
		// TODO Auto-generated method stub
		nodeMainExecutor = nodeMainEx;
		nodeConfiguration = NodeConfiguration.newPublic(InetAddressFactory.newNonLoopback().getHostAddress(),getMasterUri());
		mediaDealer=new MediaProcessor(nodeMainExecutor,nodeConfiguration);
		talker = new Talker();
		modeCode=0;
		startNodes(talker);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				// TODO Auto-generated method stub
				if(pos==0){
					talker=new Talker();
					modeCode=0;
					startNodes(talker);
				}
				else {
					talker=new Talker_Real();
					modeCode=1;
					startNodes(talker);
					}
				Toast.makeText(parent.getContext(), 
						"Select App Mode : " + parent.getItemAtPosition(pos).toString(),
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
    	});
	}
    public static void startNodes(AbstractNodeMain node)
    {
    	nodeMainExecutor.execute(node, nodeConfiguration.setNodeName("Rose_Talker_Basic"));
    	
    }
    
    /*
     * Getters
     */
    public static AbstractNodeMain getTalker() 
	{
		return talker;
	}
    public static NodeMainExecutor getNodeMainExecutor()
	{
		return nodeMainExecutor;
	}
  
	public static NodeConfiguration getNodeConfiguration()
	{
		return nodeConfiguration;
	}
	public static MediaProcessor getMediaProcessor(){
		return mediaDealer;
	}

}
