package org.ros.android.ros_controller;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;


public class MyApp extends Application{
	
	public 	String DefindMap;
	public  boolean longPress;
	public boolean defineMyself;
	public String roomName;
	public int roonNO=0;
	
	public int getRoonNO() {
		return roonNO;
	}

	public void setRoonNO(int roonNO) {
		this.roonNO = roonNO;
	}

	public String getRoomName() {
		Log.d("get", roomName);
		return roomName;
		
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
		Log.d("set2", this.roomName);
	}

	public boolean isDefineMyself() {
		return defineMyself;
	}

	public void setDefineMyself(boolean defineMyself) {
		this.defineMyself = defineMyself;
	}

	public boolean isLongPress() {
		return longPress;
	}

	public void setLongPress(boolean longPress) {
		this.longPress = longPress;
	}

	public String getDefindMap() {
		return DefindMap;
	}

	public void setDefindMap(String defindMap) {
		DefindMap = defindMap;
		Log.d("set1", DefindMap);
	}
	
	 public void onCreate() {
	        super.onCreate();
	        
	        setDefindMap("false");
	        setRoomName("aaa");
	       
	 }
	
	
	}
