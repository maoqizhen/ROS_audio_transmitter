package org.ros.android.ros_controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.ros.android.ros_controller.R;

import android.os.Bundle;
import android.os.Environment;
import android.renderscript.Font;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MapLocation extends Activity {
	
	
	
	private boolean myself=false;
	
	private int screenHeight;
	private int screenWight;
	 private Bitmap mBitmap1;
	
	private TextView textView4;
	private TextView textView5;
	private TextView textView6;
	private TextView textView7;;
	private Button button1;
	private int Width;
	private int Height;
	private MyApp myApp;
	private PixRecord pixR=new PixRecord();
	private String Define="false";
	private static final String mFalse ="false";
	 static final int NONE = 0; 
     static final int DRAG = 1; 
     static final int ZOOM = 2; 
     int mode = NONE; 
     PointF start = new PointF(); 
     PointF center = new PointF(-10,-10); 
     PointF mypoint =new PointF(-20,-20);
     public static float roomx[]=new float [10];
     public static float roomy[]=new float [10];
     public static String name[]=new String[10];
     //------------------//
     FileOutputStream fos;
     String fileName = "record.txt";
     //-----------------------//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maplocation);
      
      
        textView4=(TextView)this.findViewById(R.id.textView4MapLocation);
        textView5=(TextView)this.findViewById(R.id.textView5MapLocation);
        textView6=(TextView)this.findViewById(R.id.textView6MapLocation);
        textView7=(TextView)this.findViewById(R.id.textView7MapLocation);
        button1=(Button)this.findViewById(R.id.button1MapLocation);
        DisplayMetrics dm =new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Width=dm.widthPixels;
        Height=dm.heightPixels;
      
        textView4.setText(String.valueOf(Width));
        textView5.setText(String.valueOf(Height));
        
        
       mBitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.room);
        
        
        
        myApp=(MyApp)getApplication();
    	
    	
    	View v = new MultiTouchView(this);//�����Լ���view��ΪҪ��ʾ������
    	setContentView(v);
    	/*!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/

    	try {
    	//fos=openFileOutput(fileName, Context.MODE_PRIVATE);
    	fos=openFileOutput(fileName, Context.MODE_APPEND);
    	fos.close();
    	} catch (FileNotFoundException e) {
    	// TODO Auto-generated catch block
    	e.printStackTrace();
    	} catch (IOException e) {
    	// TODO Auto-generated catch block
    	e.printStackTrace();
    	}
    	/*!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/
       
        
       
        button1.setOnClickListener(new View.OnClickListener() {
        	
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(MapLocation.this, define.class);
				startActivity(intent);
			}
		});
    }

   @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
    	switch (item.getItemId())
    	{
    	case R.id.define:
    		if((Define.equals("false")))
    		{
    			Intent intent=new Intent(MapLocation.this, define.class);
				startActivity(intent);
				
				
				
    		}
    	
    	break;
    	
    	case R.id.locate:
    		
    		Intent intent=new Intent(MapLocation.this, locateMyself.class);
			startActivity(intent);
    	
    	break;
    	
    	case R.id.route:
    	
    		break;
    	
    	}
    	
		return super.onOptionsItemSelected(item);
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	getMenuInflater().inflate(R.menu.mapmain, menu);
        
        
        return true;
    }
    
    
    class MultiTouchView extends View  {
    	private float x=-10;
    	
    	
    	private float y=-10;
    	

    	public MultiTouchView(Context context) {
    	super(context);
    	
   
    	}
    	
    	

    public boolean onTouchEvent(MotionEvent event) {
    	// �������ж�һ������ǰ��²����ͻ�ȡ���Ȼ��ִ�з���
    
    	//if (event.getAction() == MotionEvent.ACTION_DOWN) {
    		
    		switch(event.getActionMasked()){
			
			case MotionEvent.ACTION_DOWN:
    		textView6.setText(String.valueOf(event.getX()));
    		textView7.setText(String.valueOf(event.getY()));
    		
    		
    		mode=DRAG;
    		start.set(event.getX(),event.getY());
    		//x=event.getX();
			//y=event.getY();
			//postInvalidate(); 
    		Log.e("MapLocation", "touch");
    		
    	
    		Define=myApp.getDefindMap();
    		myself=myApp.isDefineMyself();
    		
    		
    		
    		
    		//textView2.setText(Define);
    		Log.e("mark2", Define);
    		
    		if(Define.equals("true"))
    		{
    			Log.e("MapLocation", "equal");
    			if(!pixR.isFull())
    			{
    				pixR.counter();
    			pixR.record(event.getX(), event.getY());
    			x=event.getX();
				y=event.getY();
				if(pixR.number==3)
				{
					center=pixR.centerPix();
					x=-10;
					y=-10;
					  
					  myApp.setRoonNO(myApp.getRoonNO()+1);
					  roomx[myApp.getRoonNO()]=center.x;
					  roomy[myApp.getRoonNO()]=center.y;
					  name[myApp.getRoonNO()] =myApp.getRoomName();
					  myApp.setDefindMap("false");
					  Define=myApp.getDefindMap();
					  /*!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/
					  String recordA=String.valueOf((int)center.x);
					  String recordB=String.valueOf((int)center.y);
					  String spaces=" ";
					  String huanHang="\r\n";
					  try {
					  // fos=openFileOutput(fileName, Context.MODE_PRIVATE);
					  fos=openFileOutput(fileName, Context.MODE_APPEND);
					  fos.write(recordA.getBytes());
					  fos.write(spaces.getBytes());
					  fos.write(recordB.getBytes());
					  fos.write(huanHang.getBytes());

					  fos.close();
					  } catch (FileNotFoundException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
					  } catch (IOException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
					  }
					  /*!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/

					  //read out data
					  /*!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/
					  String dataGot=null;
					  FileInputStream fis=null;

					  try {
					  fis=openFileInput(fileName);
					  byte[] dataArray =new byte[fis.available()];

					  while(fis.read(dataArray)!=-1)
					  {
					  dataGot=new String(dataArray);

					  Log.e("String1", dataGot);
					  }
					  } catch (FileNotFoundException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
					  } catch (IOException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
					  }finally
					  {
					  try {
					  fis.close();
					  Log.e("String2", dataGot);
					  } catch (IOException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
					  }
					  }
					  /*!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/
    				invalidate();
				}
				invalidate();
    				//Log.e("MapLocation", "notfull");
    			}
    			else
    			{
    				Log.e("MapLocation", "full");
    				
    				
    				
    				myApp.setDefindMap("false");
    				Define=myApp.getDefindMap();
    				
    			}
    		}
    		
    		
    		if(myself)
    		{
    			mypoint.x=event.getX();
    			mypoint.y=event.getY();
    			invalidate();
    			myApp.setDefineMyself(false);
    			
    			
    		}
    		
    			
    		
    		
    		break;
    		
			
    		
    
    		
			
    	}
    	
    	
    	
    	
    		return super.onTouchEvent(event);
    	}
  


    @SuppressLint("DrawAllocation") @Override
    protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    Log.i("MultiTouch","onDraw()");
    Paint paint = new Paint();
    
    DisplayMetrics dm =new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(dm);
   screenHeight=dm.heightPixels;
    screenWight=dm.widthPixels;
    
    Rect src = new Rect();// ͼƬ
	Rect dst = new Rect();// ��Ļ
	src.left = 0;
	src.top = 0;
	src.right = mBitmap1.getWidth();
	src.bottom = mBitmap1.getHeight();
	dst.left = 0;
	dst.top = 0;
	dst.right = screenWight;
	dst.bottom = screenHeight;
    
   
  canvas.drawBitmap(mBitmap1, src , dst , paint);
    
   
   
   
   
    
    paint.setColor(Color.BLUE);
    
   // canvas.drawBitmap(mBitmap1, 0, 0, paint);
    
   canvas.drawCircle(x, y, 5, paint);
   
   
   
   paint.setColor(Color.RED);
   
   
   for(int index=1; index<=myApp.getRoonNO();index++)
   {
  
    canvas.drawCircle(roomx[index], roomy[index], 15, paint);
    paint.setTextSize(24);
    canvas.drawText(name[index], roomx[index]-25, roomy[index]-20, paint);
    //canvas.drawText("room", 100, 100, paint);
   Log.d("room", ""+roomx[index]);
   Log.d("room", ""+roomy[index]);
   }
   Log.d("name",name[0]+" "+name[1]+" "+name[2]+" "+name[3]+" ");

   
   canvas.drawCircle(screenWight-100, screenHeight/3, 15, paint);
   
   
    
    paint.setColor(Color.GREEN);
    canvas.drawCircle(screenWight-100, (screenHeight/3)-40, 15, paint);
    canvas.drawCircle(mypoint.x, mypoint.y, 20, paint);
    
    paint.setColor(Color.BLACK);
    paint.setTextSize(18);
   
    canvas.drawText("MYSELF", screenWight-75, (screenHeight/3)-35, paint);
    canvas.drawText("ROOM", screenWight-70, (screenHeight/3)+5, paint);
    
   
    }


    }
    
   

	
}
