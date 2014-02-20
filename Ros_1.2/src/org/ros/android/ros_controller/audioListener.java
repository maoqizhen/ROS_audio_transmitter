/**
 * 
 */
package org.ros.android.ros_controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeMain;
import org.ros.node.topic.Subscriber;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;

/**
 * A simple {@link Subscriber} {@link NodeMain}.
 * 
 * @author damonkohler@google.com (Damon Kohler)
 */
public class audioListener extends AbstractNodeMain {
	 ChannelBuffer inputBuffer;
     File file;
  @Override
  public GraphName getDefaultNodeName() {
    return GraphName.of("audio_listener");
  }

  @Override
  public void onStart(ConnectedNode connectedNode) {
	
	  file=new File(Environment.getExternalStorageDirectory() + "/androidInAudio.wav");
    Subscriber<std_msgs.ByteMultiArray> subscriber = connectedNode.newSubscriber("audio_sender", std_msgs.ByteMultiArray._TYPE);
    subscriber.addMessageListener(new MessageListener<std_msgs.ByteMultiArray>() {
      @Override
      public void onNewMessage(std_msgs.ByteMultiArray message) {
    	  inputBuffer=message.getData();
    	
    	  FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(file);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	    FileChannel localfileChannel = outputStream.getChannel();
    	    ByteBuffer byteBuffer = inputBuffer.toByteBuffer();
    	    int written = 0;
    	    try {
				written += localfileChannel.write(byteBuffer);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	    inputBuffer.readerIndex(inputBuffer.readerIndex() + written);
    	    try {
				localfileChannel.force(false);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	    try {
				localfileChannel.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	    try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	   Runnable r1 = new Runnable() 
    		{
    			public void run() 
    			{
    				playStream(Environment.getExternalStorageDirectory() + "/androidInAudio.wav");
    			}
    		};
    		Thread thr1 = new Thread(r1);
    		thr1.start();
    	  
        
      }
    });
  }
  public void playStream(String filePath){
	  Log.e("media","ok");
	  MediaPlayer mediaPlayer = new MediaPlayer();
	  mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
	  try {
		mediaPlayer.setDataSource(filePath);
	} catch (IllegalArgumentException e) {
		// TODO Auto-generated catch block
		Log.e("1","1");
	} catch (SecurityException e) {
		// TODO Auto-generated catch block
		Log.e("2","2");
	} catch (IllegalStateException e) {
		// TODO Auto-generated catch block
		Log.e("3","3");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		Log.e("4","4");
	}
	  try {
		mediaPlayer.prepare();
	} catch (IllegalStateException e) {
		// TODO Auto-generated catch block
		Log.e("5","5");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		Log.e("6","6");
	}
	  Log.e("toPlay","toPlay");
	  mediaPlayer.start();
	  Log.e("media","finished");
  }
  
}