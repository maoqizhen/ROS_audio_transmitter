/**
 * 
 */
package org.ros.android.rviz_for_android;


import org.ros.message.MessageFactory;
import org.ros.message.Time;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeConfiguration;
import org.ros.node.topic.Publisher;

/**
 * @author maoqizhen
 *
 */
public class Talker_mapReal extends AbstractNodeMain{

	private ConnectedNode connectedNode;
	private Publisher<std_msgs.Int32> publisher;
	private Publisher<geometry_msgs.PoseStamped> publisher_move_base;
	private Publisher<std_msgs.String> publisher_bounding_box;
	
	@Override
	public GraphName getDefaultNodeName() {
		// TODO Auto-generated method stub
		return  GraphName.of("Rose_Talker_Real");
	}
	@Override
	public void onStart(final ConnectedNode connectedNode) 
	{
		this.connectedNode = connectedNode;
		publisher = connectedNode.newPublisher("goal_sender_mode", std_msgs.Int32._TYPE);
		publisher_move_base = connectedNode.newPublisher("move_base_simple/goal", geometry_msgs.PoseStamped._TYPE);
		publisher_bounding_box=connectedNode.newPublisher("bounding_box", std_msgs.String._TYPE);
	}
	
	/*
	 * While a drive control button is pushed the method sends a message
	 */
	public void startMsg(final int message_value)
	{
		
		std_msgs.Int32 msg = publisher.newMessage();
		msg.setData(message_value);
		publisher.publish(msg);
		
	}
	
	/*
	 * Called when the drive button that started the thread is released
	 */
	public void stopMsg()
	{
		std_msgs.Int32 msg = publisher.newMessage();
		msg.setData(2);
		publisher.publish(msg);
	}
	/*
	 * It is a topic (non-action type) on which one can post a goal if he does not want to monitor its status (success or not) after posting.
	 * base_position 3 elements, base_orientation 4 elements
	 */
	public void publish_move_base(double[] base_position, double[] base_orientation){
		NodeConfiguration nodeConfiguration = NodeConfiguration.newPrivate();
		MessageFactory messageFactory = nodeConfiguration.getTopicMessageFactory();
		geometry_msgs.Point base_point=messageFactory.newFromType(geometry_msgs.Point._TYPE);
		base_point.setX(base_position[0]);
		base_point.setY(base_position[1]);
		base_point.setZ(base_position[2]);
		geometry_msgs.Quaternion base_quaternion=messageFactory.newFromType(geometry_msgs.Quaternion._TYPE);
		base_quaternion.setX(base_orientation[0]);
		base_quaternion.setY(base_orientation[1]);
		base_quaternion.setZ(base_orientation[2]);
		base_quaternion.setW(base_orientation[3]);
		geometry_msgs.Pose base_pose=messageFactory.newFromType(geometry_msgs.Pose._TYPE);
		base_pose.setPosition(base_point);
		base_pose.setOrientation(base_quaternion);
		Time currentTime = connectedNode.getCurrentTime();
	    String frameId = "move_base"; //need to ensure
		geometry_msgs.PoseStamped base_msg=messageFactory.newFromType(geometry_msgs.PoseStamped._TYPE);
		base_msg.getHeader().setStamp(currentTime);
		base_msg.getHeader().setFrameId(frameId);
		base_msg.setPose(base_pose);
		publisher_move_base.publish(base_msg);
	}
	public String publish_bounding_box(int[] boundingPoints, int command_num){
		std_msgs.Int32 msg_command = publisher.newMessage();
		msg_command.setData(command_num);
		publisher.publish(msg_command);
		//Maybe sleep some time
		std_msgs.String msg=publisher_bounding_box.newMessage();
		String msgValue=""+boundingPoints[0]+","+boundingPoints[1]+","+boundingPoints[2]+","+boundingPoints[3];
		msg.setData(msgValue);
		publisher_bounding_box.publish(msg);
		return msgValue;
		
		
	}
	
	

}
