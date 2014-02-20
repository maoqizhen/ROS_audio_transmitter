/**
 * 
 */
package org.ros.android.ros_controller;
import org.ros.android.BitmapFromCompressedImage;
import org.ros.android.BitmapDrawFromCompressedImage;
import org.ros.android.view.RosImageView;
import org.ros.android.view.camera.RosCameraPreviewView;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;


/**
 * @author maoqizhen
 *
 */
public class MediaProcessor {
	private static BitmapFromCompressedImage bitMapDecoder;
	private static BitmapDrawFromCompressedImage drawBitmapDecoder;
	private RosCameraPreviewView rosCameraPreviewView;
	private audioListener audioListenProcess;
	private audioTalker audioTalkerProcess;
	private static NodeMainExecutor nodeMainExecutor;
	private static NodeConfiguration nodeConfiguration;
	public MediaProcessor(NodeMainExecutor executorIn,NodeConfiguration configurationIn){
		bitMapDecoder=new BitmapFromCompressedImage();
		drawBitmapDecoder=new BitmapDrawFromCompressedImage();
		nodeMainExecutor = executorIn;
		nodeConfiguration = configurationIn;
	}
	public void startImage(final RosImageView<sensor_msgs.CompressedImage> image,final String nodeName)
	{
		image.setMessageToBitmapCallable(bitMapDecoder);
		Runnable r2 = new Runnable() 
		{
			public void run() 
			{
				nodeMainExecutor.execute(image, nodeConfiguration.setNodeName(nodeName));
			}
		};
		Thread thr2 = new Thread(r2);
		thr2.start();    //maybe don't need thread
	}
	/*
	 * stop image
	 */
	public void stopImage(RosImageView<sensor_msgs.CompressedImage> image)
	{
		nodeMainExecutor.shutdownNodeMain(image);
	}
	public void startdrawImage(final RosImageView<sensor_msgs.CompressedImage> image,final String nodeName)
	{
		image.setMessageToBitmapCallable(drawBitmapDecoder);
		Runnable r2 = new Runnable() 
		{
			public void run() 
			{
				nodeMainExecutor.execute(image, nodeConfiguration.setNodeName(nodeName));
			}
		};
		Thread thr2 = new Thread(r2);
		thr2.start();    //maybe don't need thread
	}
	/*
	 * stop image
	 */
	public void stopdrawImage(RosImageView<sensor_msgs.CompressedImage> image)
	{
		nodeMainExecutor.shutdownNodeMain(image);
	}
	public void setDrawPoint(float[] start,float[] end){
		drawBitmapDecoder.setStartingPoint(start);
		drawBitmapDecoder.setEndPoint(end);
		
		
	}
	
	public void startCamera(final RosCameraPreviewView cameraView)
	{
		this.rosCameraPreviewView = cameraView;
		Runnable r2 = new Runnable() 
		{
			public void run() 
			{
				
				nodeMainExecutor.execute(rosCameraPreviewView, nodeConfiguration.setNodeName("android/camera_view_meeting"));
			}
		};
		Thread thr2 = new Thread(r2);
		thr2.start();
	}
	public void stopCamera(RosCameraPreviewView cameraView)
	{
		nodeMainExecutor.shutdownNodeMain(cameraView);
	}
	public void startAudio(audioListener listenerIn,audioTalker talkerIn){
		
		audioListenProcess=listenerIn;
		audioTalkerProcess=talkerIn;
		Runnable r2 = new Runnable() 
		{
			public void run() 
			{
				
				nodeMainExecutor.execute(audioListenProcess, nodeConfiguration.setNodeName("audio_listener_android"));
			    nodeMainExecutor.execute(audioTalkerProcess, nodeConfiguration.setNodeName("audio_talker_android"));
			}
		};
		Thread thr2 = new Thread(r2);
		thr2.start();
	}
	public void stopAudio(audioListener listenerIn,audioTalker talkerIn)
	{
		
		nodeMainExecutor.shutdownNodeMain(listenerIn);
		nodeMainExecutor.shutdownNodeMain(talkerIn);
	}

}
