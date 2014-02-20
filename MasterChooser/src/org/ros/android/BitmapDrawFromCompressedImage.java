package org.ros.android;

import org.jboss.netty.buffer.ChannelBuffer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class BitmapDrawFromCompressedImage implements
MessageCallable<Bitmap, sensor_msgs.CompressedImage> {
private ChannelBuffer buffer;
private byte[] data;
private Bitmap temperBitmap;
private float[] startingPoint={0,0};
private float[] endPoint={0,0};
Paint paint = new Paint();
public float[] getStartingPoint() {
	return startingPoint;
}
public void setStartingPoint(float[] startingPoint) {
	this.startingPoint = startingPoint;
}
public float[] getEndPoint() {
	return endPoint;
}
public void setEndPoint(float[] endPoint) {
	this.endPoint = endPoint;
}

@Override
public Bitmap call(sensor_msgs.CompressedImage message) {
buffer = message.getData();
data = buffer.array();
temperBitmap=BitmapFactory.decodeByteArray(data, buffer.arrayOffset(), buffer.readableBytes());
Bitmap mutableBitmap = temperBitmap.copy(Bitmap.Config.ARGB_8888, true);
Canvas c = new Canvas(mutableBitmap);
float width=(float) (c.getWidth()*0.25);
float height=(float) (c.getHeight()*0.5);
paint.setColor(Color.RED);
c.drawCircle(width, height, 3, paint);
width=(float)(c.getWidth()*0.75);
height=(float)(c.getHeight()*0.5);
c.drawCircle(width, height, 3, paint);
return mutableBitmap;
}
}
