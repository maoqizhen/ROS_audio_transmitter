package org.ros.android.ros_controller;

import org.ros.android.ros_controller.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class locateMyself extends Activity{
	
	private Button button1;
	private MyApp myApp;
	
	 protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.locatemyself);
	        
	        button1=(Button)this.findViewById(R.id.buttonLocatemyself);
	        button1.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					myApp=(MyApp)getApplication();
					myApp.setDefineMyself(true);
					
					
					
					locateMyself.this.finish();
					
					
				}
			});
	        
	  }
}
