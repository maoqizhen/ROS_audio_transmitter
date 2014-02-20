
package org.ros.android.ros_controller;


import org.ros.android.ros_controller.R;

import org.ros.android.view.RosImageView;
import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.Toast;


/**
 * @author maoqizhen
 *
 */
public class Action_mode extends Activity implements OnTouchListener,OnGestureListener{
	
	private  static RosImageView<sensor_msgs.CompressedImage> imageView;
	private  static MediaProcessor imageAction;
	GestureDetector mGestureDetector;
	private int screenHeight;
	private int screenWight;
	//private float imageHeight;
	private boolean move=true;
	private boolean isMove=false;
	private View targetView;
	
	//private ImageView imageView;
	//private  Matrix matrix =new Matrix();
	
	 private Matrix matrix=new Matrix();
	 private Matrix savedMatrix=new Matrix();
	 
	 pointMath pm = new pointMath();
	 /*!@!#!@#!@#!@#!@!@#!@#!@#!@#!@*/
	 private static final int imageH = 360; 
	 private static final int imageW = 480; 
	 private float ratio;
	 /*!@!#!@#!@#!@#!@!@#!@#!@#!@#!@*/
	  
	    
	     static final int NONE = 0; 
	     static final int DRAG = 1; 
	     static final int ZOOM = 2; 
	     int mode = NONE; 

	     // Remember some things for zooming 

	     PointF start = new PointF(); 

	     PointF mid = new PointF(); 
	     PointF point1=new PointF();
	     PointF point2=new PointF();
	     PointF recordMove =new PointF();
	     PointF recordZoom =new PointF();
	     

	     float oldDist = 1f; 

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
        setContentView(R.layout.action_mode); 
		this.imageView = (RosImageView<sensor_msgs.CompressedImage>) findViewById(R.id.rosImageViewAction);
	    mGestureDetector = new GestureDetector(this); 
        
        DisplayMetrics dm =new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        /*!@!#!@#!@#!@#!@!@#!@#!@#!@#!@*/
        screenHeight=dm.heightPixels;
        screenWight=dm.widthPixels;
        ratio=(imageW*screenHeight)/imageH;
        /*!@!#!@#!@#!@#!@!@#!@#!@#!@#!@*/
        
        imageView.setOnTouchListener(this);
        
        
        imageView.setLongClickable(true);
	    targetView=findViewById(R.id.btnGrabL);
		
	}
	
	
	private float spacing(MotionEvent event) { 
        float x = event.getX(0) - event.getX(1); 
        float y = event.getY(0) - event.getY(1); 
        return FloatMath.sqrt(x * x + y * y);
} 


private void midPoint(PointF point, MotionEvent event) { 
float x = event.getX(0) + event.getX(1); 
float y = event.getY(0) + event.getY(1); 
point.set(x / 2, y / 2); 

} 




	@Override
	public void onStart()
	{
		super.onStart();
		imageAction=MainActivity.getMediaProcessor();
		
	}
	 @Override
	    protected void onResume() {
	        super.onResume();
	        imageView.setTopicName("wide_stereo/left/image_color/compressed");				///usb_cam/image_raw/compressed
			imageView.setMessageType(sensor_msgs.CompressedImage._TYPE);
			imageAction.startImage(imageView,"android/video_viewAction");
	    }

	    @Override
	    protected void onPause() {
	        super.onPause();
	        imageAction.stopImage(imageView);
	    }

	    @Override
	    protected void onStop() {
	        super.onStop();
	        imageAction=null;
	    }
	    @Override
	    protected void onDestroy() {
	        super.onDestroy();
	        imageView=null;
	        
	    }
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
	
		imageView.setScaleType(ImageView.ScaleType.MATRIX);

		if(event.getActionMasked()==MotionEvent.ACTION_POINTER_UP)
			Log.d("Infor", "¶àµã²Ù×÷");
		
			switch(event.getActionMasked()){
			
				case MotionEvent.ACTION_DOWN:
					 
						matrix.set(imageView.getImageMatrix());
						savedMatrix.set(matrix);
						start.set(event.getX(),event.getY());
						Log.d("Infor", "Ž¥ÃþÁË...");
						mode=DRAG;
						
				break;
				
				case MotionEvent.ACTION_POINTER_DOWN:  //¶àµãŽ¥¿Ø
						oldDist=this.spacing(event);
							if(oldDist>10f){
									Log.d("Infor", "oldDist"+oldDist);
									savedMatrix.set(matrix);
									midPoint(mid,event);
									mode=ZOOM;
										}
				break;
				
				
				case MotionEvent.ACTION_POINTER_UP:
					
					float newDist=spacing(event);
					if(newDist>10f){
						
						float scale=newDist/oldDist;
						
						pm.ZoomCounter(scale);
						
						/*!@!#!@#!@#!@#!@!@#!@#!@#!@#!@*/
						if(pm.totalScale<1)
						{
						matrix.set(savedMatrix);

						matrix.postScale(1, 1); 
						pm.ZoomCounter(1/scale);
						}
						/*!@!#!@#!@#!@#!@!@#!@#!@#!@#!@*/
						Log.e("Infor", "scale record "+scale);
					}
					
					mode=NONE;
					
				break;
				
				case MotionEvent.ACTION_UP:
					
					
					point2.set(event.getX(), event.getY());
					pm.RealPoint(point2);
					
					if(move&&isMove)
					{	
						Log.e("Infor", "move record "+move);
						recordMove.set(event.getX()-start.x, event.getY()-start.y);
						pm.MoveCounter(recordMove);
						//Log.e("Infor", "movex  "+(event.getX()-start.x));
					//	Log.e("Infor", "moveY  "+(event.getY()-start.y));
						/*!@!#!@#!@#!@#!@!@#!@#!@#!@#!@*/
						if(pm.pointMove.x>0||pm.pointMove.x<(-screenWight*(pm.getTotalScale()-1))||pm.pointMove.y>0||pm.pointMove.y<(-screenHeight*(pm.getTotalScale()-1)))
						{
						matrix.set(savedMatrix);
						recordMove.set(-(event.getX()-start.x), -(event.getY()-start.y));
						matrix.postTranslate(0, 0);
						Log.e("Infor", "movex2 "+(recordMove.x));
						Log.e("Infor", "movey2 "+(recordMove.y));
						pm.MoveCounter(recordMove);
						}
						/*!@!#!@#!@#!@#!@!@#!@#!@#!@#!@*/
					}
					else if(!move){
						//Log.e("Infor", "X(0)+Y(0)+X(1)+Y(1)  "+""+point1.x+"     "+point1.y+"     "+point2.x+"     "+point2.y);
						/*!@!#!@#!@#!@#!@!@#!@#!@#!@#!@*/
						float[] start=new float[2];
						start[0]=(point1.x-((screenWight-ratio)/2))/ratio;
						start[1]= point1.y/screenHeight;
						float a=start[0]*imageW;
						float b=start[1]*imageH;
						point1.set(a,b);
						float[] end=new float[2];
						end[0]=(point2.x-((screenWight-ratio)/2))/ratio;
						end[1]=point2.y/screenHeight;
						float c=end[0]*imageW;
						float d=end[1]*imageH;
						point2.set(c,d);
						
						Log.e("Infor", "X(0)+Y(0)+X(1)+Y(1) real "+""+point1.x+" "+point1.y+" "+point2.x+" "+point2.y);
						//imageView.changeCallable(start, end);
						Toast.makeText(this, 
								"bounding box sent"+point1.x+","+point1.y+","+point2.x+","+point2.y,
								Toast.LENGTH_SHORT).show();
						targetView.setEnabled(true);
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						start[0]=0;start[1]=0;
						end[0]=0;end[1]=0;
						//imageView.changeCallable(start, end);
						/*!@!#!@#!@#!@#!@!@#!@#!@#!@#!@*/
					
						
					}
					mode=NONE;
					move=true;
					isMove=false;
					
				break;
				
				case MotionEvent.ACTION_MOVE:
					if(mode==DRAG&&move){         //ÍÏ¶¯
						matrix.set(savedMatrix);
						matrix.postTranslate(event.getX()-start.x, event.getY()-start.y);
						//Log.e("Infor", "move");
						//Log.e("Infor", "xmove"+(event.getX()-start.x));
						//Log.e("Infor", "ymove"+(event.getY()-start.y));
						isMove=true;
						
						}
					else if(mode==ZOOM){// Ëõ·Å
						 newDist=spacing(event);
						if(newDist>10f){
							matrix.set(savedMatrix);
							float scale=newDist/oldDist;
						matrix.postScale(scale, scale); 
						//matrix.postScale(scale, scale,  ((screenWight/2)+pm.getPointMove().x), ((screenHeight/2)+pm.getPointMove().y));
							 //matrix.postScale(screenHeight/imageHeight, screenHeight/imageHeight, screenWight/2, screenHeight/2);
							//Log.e("Infor", "ŒÓµÄÐÅÏ¢  "+pm.getPointMove().x+" "+pm.getPointMove().y);
						
							}
							}
					else if(!move)
					{
						point1.set(start.x, start.y);
						pm.RealPoint(point1);
						
					}
				
			    break;
			     
			   }
			imageView.setImageMatrix(matrix);
			

			 return mGestureDetector.onTouchEvent(event);  
	}
	/*
	 * clickHandler, will start new activity when clicked on a button
	 */
    public void actionClickHandler(View target)
    { 
    	target.setEnabled(false);
        targetView=target;                       //simulation mode
        switch (target.getId()) {  
        case R.id.btnGrabL:   
        	Toast.makeText(this, 
					"grab L",
					Toast.LENGTH_SHORT).show();
            break; 

        case R.id.btnPlaceL:   
        	Toast.makeText(this, 
					"place L",
					Toast.LENGTH_SHORT).show();
            break; 
        case R.id.btnOpenL:   
        	Toast.makeText(this, 
					"open L",
					Toast.LENGTH_SHORT).show();
            break; 
        case R.id.btnCleanL:   
        	Toast.makeText(this, 
					"clean L",
					Toast.LENGTH_SHORT).show();
            break; 
        case R.id.btnGrabR:   
        	Toast.makeText(this, 
					"grab R",
					Toast.LENGTH_SHORT).show();
            break; 
        case R.id.btnPlaceR:   
        	Toast.makeText(this, 
					"place R",
					Toast.LENGTH_SHORT).show();
            break; 
        case R.id.btnOpenR:   
        	Toast.makeText(this, 
					"open R",
					Toast.LENGTH_SHORT).show();
            break; 
        case R.id.btnCleanR:   
        	Toast.makeText(this, 
					"clean R",
					Toast.LENGTH_SHORT).show();
            break; 
        }
    
   }
	
		 
	
	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		//Log.e("Infor", "ondown  ");
		return false;
	}

	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		//Log.e("Infor", "onFling ");
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		//Log.e("Infor", "longPress  ");
		move=false;
	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		//Log.e("Infor", "onShowPress  ");
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		//Log.e("Infor", "onSingleTapUp  ");
		return false;
	}
    

}
