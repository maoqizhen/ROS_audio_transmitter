/**
 * 
 */
package org.ros.android.ros_controller;
import java.io.File;

import org.ros.android.ros_controller.R;
import org.ros.android.view.RosImageView;
import org.ros.android.view.camera.RosCameraPreviewView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * @author maoqizhen
 *
 */
public class Video_meeting extends Activity {
	private static RosImageView<sensor_msgs.CompressedImage> image;
	private static MediaProcessor mediaAction;
	private int cameraId;
	private static RosCameraPreviewView rosCameraPreviewView;
	private AlertDialog dialog;
    private static audioListener listenerProcess;
    private static audioTalker talkerProcess;
    private static Hertz recorder;
    private Button btnRecord;
    private Button btnStop;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.video_meeting);
	    rosCameraPreviewView = (RosCameraPreviewView) findViewById(R.id.rosCameraPreviewViewVideoMeeting);
	    image=(RosImageView<sensor_msgs.CompressedImage>) findViewById(R.id.rosImageViewVideoMeeting);
	    
	    cameraId = 0;
	    
	 // get a generic dialog ready for alerts
	    dialog = new AlertDialog.Builder(this).create();
	    dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
	      @Override
	      public void onClick(DialogInterface dialogInterface, int which) {
	        dialogInterface.dismiss();
	      }
	    });
	   
	    btnRecord=(Button)findViewById(R.id.btnRecord);
	    btnStop=(Button)findViewById(R.id.btnStop);
	    
	  }
	@Override
	public void onStart()
	{
		super.onStart();
		mediaAction=MainActivity.getMediaProcessor();
		recorder=new Hertz("androidOutAudio.wav",44100,dialog);
		listenerProcess=new audioListener();
		talkerProcess=new audioTalker();
		btnRecord.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnRecord.setEnabled(false);
				recorder.beginRecording();
		 		btnStop.setEnabled(true);
			}
		});
	    btnStop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnStop.setEnabled(false);
				recorder.endRecording();
		 		
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 String sdDirectory = Environment.getExternalStorageDirectory().toString();
			 File sentFile=new File(sdDirectory+"/androidOutAudio.wav");
		 	talkerProcess.stopAndSend(sentFile);
		 	btnRecord.setEnabled(true);
			}
		});
	}
	@Override
    protected void onResume() {
        super.onResume();
        image.setTopicName("wide_stereo/left/image_color/compressed");				///usb_cam/image_raw/compressed
		image.setMessageType(sensor_msgs.CompressedImage._TYPE);
		rosCameraPreviewView.setCamera(Camera.open(cameraId));
		mediaAction.startImage(image,"android/video_viewMeeting");
		mediaAction.startCamera(rosCameraPreviewView);
		mediaAction.startAudio(listenerProcess, talkerProcess);
		
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaAction.stopImage(image);
        mediaAction.stopCamera(rosCameraPreviewView);
        mediaAction.stopAudio(listenerProcess, talkerProcess);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaAction=null;
		recorder=null;
		listenerProcess=null;
		talkerProcess=null;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        image=null;
        rosCameraPreviewView=null;
        
    }
	@SuppressLint("ShowToast")
	@Override
	  public boolean onTouchEvent(MotionEvent event) {
	    if (event.getAction() == MotionEvent.ACTION_UP) {
	      int numberOfCameras = Camera.getNumberOfCameras();
	      final Toast toast;
	      if (numberOfCameras > 1) {
	        cameraId = (cameraId + 1) % numberOfCameras;
	        rosCameraPreviewView.releaseCamera();
	        rosCameraPreviewView.setCamera(Camera.open(cameraId));
	        toast = Toast.makeText(this, "Switching cameras.", Toast.LENGTH_SHORT);
	      } else {
	        toast = Toast.makeText(this, "No alternative cameras to switch to.", Toast.LENGTH_SHORT);
	      }
	      runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	          toast.show();
	        }
	      });
	    }
	    return true;
	  }


}
