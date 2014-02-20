/*
 * Copyright (C) 2011 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.ros.android.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;
import org.ros.android.MessageCallable;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeMain;
import org.ros.node.topic.Subscriber;
import org.ros.android.BitmapDrawFromCompressedImage;

/**
 * Displays incoming sensor_msgs/CompressedImage messages.
 * 
 * @author ethan.rublee@gmail.com (Ethan Rublee)
 * @author damonkohler@google.com (Damon Kohler)
 */
public class RosImageView<T> extends ImageView implements NodeMain {

  private String topicName;
  private String messageType;
  private int width;
  private int height;
  private int recordTime=0;
  private MessageCallable<Bitmap, T> callable;

  public RosImageView(Context context) {
    super(context);
  }

  public RosImageView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public RosImageView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public void setTopicName(String topicName) {
    this.topicName = topicName;
  }

  public void setMessageType(String messageType) {
    this.messageType = messageType;
  }

  public void setMessageToBitmapCallable(MessageCallable<Bitmap, T> callable) {
    this.callable = callable;
    recordTime=0;
  }

  @Override
  public GraphName getDefaultNodeName() {
    return GraphName.of("ros_image_view");
  }

  @Override
  public void onStart(ConnectedNode connectedNode) {
    Subscriber<T> subscriber = connectedNode.newSubscriber(topicName, messageType);
    subscriber.addMessageListener(new MessageListener<T>() {
      @Override
      public void onNewMessage(final T message) {
    	  if(recordTime==0){
    		  Bitmap firstBitMap=callable.call(message);
    		  width=firstBitMap.getWidth();
    		  height=firstBitMap.getHeight();
    		  recordTime=1;
    	  }
        post(new Runnable() {
          @Override
          public void run() {
            setImageBitmap(callable.call(message));
          }
        });
        postInvalidate();
      }
    });
  }

  @Override
  public void onShutdown(Node node) {
  }

  @Override
  public void onShutdownComplete(Node node) {
  }

  @Override
  public void onError(Node node, Throwable throwable) {
  }
  public int getBitmapWidthPix(){
	 return width;
  }
  public int getBitmapHeightPix(){
	  return height;
  }
  public void changeCallable(float[] start,float[] end){
	  BitmapDrawFromCompressedImage drawDecoder=(BitmapDrawFromCompressedImage)callable;
	  drawDecoder.setStartingPoint(start);
	  drawDecoder.setEndPoint(end);
	  callable=(MessageCallable<Bitmap, T>)drawDecoder;
  }
}
