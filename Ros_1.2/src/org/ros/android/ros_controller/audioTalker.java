package org.ros.android.ros_controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.LinkedList;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferOutputStream;
import org.jboss.netty.buffer.ChannelBuffers;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

public class audioTalker extends AbstractNodeMain  {
    private Publisher<std_msgs.ByteMultiArray> publisher;
   
    ChannelBuffer outputBuffer;
	@Override
	public GraphName getDefaultNodeName() {
		// TODO Auto-generated method stub
		return  GraphName.of("Android_audioTalker");
	}
	public void onStart(final ConnectedNode connectedNode) 
	{
		 
		
		publisher = connectedNode.newPublisher("android_audio_sender", std_msgs.ByteMultiArray._TYPE);
		
	}
	
	public void stopAndSend(File file){
		outputBuffer=ChannelBuffers.dynamicBuffer(ByteOrder.LITTLE_ENDIAN,409600);
        FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        int len;
        byte[] bs = new byte[1024];
        try {
			while ((len = fis.read(bs)) > 0) {
			outputBuffer.writeBytes(bs);
      
      }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    std_msgs.ByteMultiArray msg = publisher.newMessage();
	    msg.setData(outputBuffer);
	    publisher.publish(msg);
		
	}
	 

	

}
