package org.ros.android.ros_controller;

import java.util.ArrayList;

import org.ros.android.ros_controller.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


public class Voice extends Activity implements OnClickListener{
	
	ListView lv;
	static final int check = 1111;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.voice);
		lv = (ListView)findViewById(R.id.lvVoiceReturn);
		Button b = (Button)findViewById(R.id.bVoice);
		b.setOnClickListener(this);
	
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
	    i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
	    i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak up please!");
	    startActivityForResult(i,check);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode == check && resultCode == RESULT_OK){
			ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
/*			ArrayList<TextView> textViews = new ArrayList<TextView>();
			for (String string:results)
			{
				TextView tv = new TextView(this);
				tv.setText(string);
				tv.setTextColor(Color.WHITE);
				textViews.add(tv);
				lv.addView(tv);
			}*/
			
			lv.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,results));
			
			//TextView tv = (TextView)lv.getItemAtPosition(0);
			
			//get the text string of the array list
			for ( String  string : results){
				if(string.contains("forward"))
					{
					Log.d("okokokgood", "good");
					}
				else if(string.contains("backward"))
				{
					Log.d("backward","good");
				}
				else if(string.contains("move left"))
				{
					Log.d("backward","good");
				}
				else if(string.contains("move right"))
				{
					Log.d("backward","good");
				}	
				else if(string.contains("turn left"))
				{
					Log.d("backward","good");
				}
				else if(string.contains("turn right"))
				{
					Log.d("backward","good");
				}
				else
				{
					Toast.makeText(this, R.string.Voice_result_false, Toast.LENGTH_SHORT).show();
				}
			}
			
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}