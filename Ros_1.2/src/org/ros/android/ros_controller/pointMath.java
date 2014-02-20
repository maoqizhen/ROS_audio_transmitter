package org.ros.android.ros_controller;

import android.graphics.PointF;

public class pointMath {

	 public PointF pointMove=new PointF(0,0);
	 public float totalScale=1;
	
	
	
	public float getTotalScale() {
		return totalScale;
	}



	public void setTotalScale(float totalScale) {
		this.totalScale = totalScale;
	}



	public PointF getPointMove() {
		return pointMove;
	}



	public void setPointMove(PointF pointMove) {
		this.pointMove = pointMove;
	}



	public void MoveCounter(PointF point)
	{
		float x= pointMove.x;
		float y= pointMove.y;
		pointMove.set(x+point.x, y+point.y);
		
		
	}
	
	public void ZoomCounter(float scale)
	{
		
		totalScale=scale*totalScale;
		
	}
	
	public void RealPoint(PointF point)
	{
		float i =point.x;
		float j =point.y;
		point.set((i-pointMove.x)/totalScale, (j-pointMove.y)/totalScale);
	}
	
}
