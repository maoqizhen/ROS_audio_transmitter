package org.ros.android.ros_controller;

import android.graphics.PointF;
import android.util.Log;

public class PixRecord {
		
	public static int number = -1;
	float a[][]=new float [4][2];
	PointF center = new PointF(); 
	
	public void record(float x, float y){
		a[number][0]=x;
		a[number][1]=y;
		
		
	}
	
	public int counter()
	{
		number++;
		number = number%4;		
		Log.e("MapLocation", ""+number);
		return number;
		
	}
	public boolean isFull()
	{
		if(number==3)
		{
			return true;
		}else
		{
			return false;
		}
		
	}
	
	public void print()
	{
		for(int k=0; number<=3; number++)
		 for(int l=0; l<=1; l++)
		 {
			 System.out.println(a[k][l]);
		 }
	}
	
	public PointF centerPix()
	{
		center.set(((a[0][0]+a[1][0]+a[2][0]+a[3][0])/4), ((a[0][1]+a[1][1]+a[2][1]+a[3][1])/4));
		return center;
		
	}
	
}
