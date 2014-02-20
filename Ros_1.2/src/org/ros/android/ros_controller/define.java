package org.ros.android.ros_controller;
import org.ros.android.ros_controller.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class define extends Activity {
	private Button button1;
	private MyApp myApp;
	private EditText text;
	

	  protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.define);
	        text=(EditText)this.findViewById(R.id.editTextDefine);
	        button1=(Button)this.findViewById(R.id.buttonDefine);
	        button1.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					myApp=(MyApp)getApplication();
					myApp.setDefindMap("true");
					
					myApp.setRoomName(text.getText().toString());
					PixRecord.number=-1;
					
					define.this.finish();
					
					
				}
			});
	        
	  }

}
