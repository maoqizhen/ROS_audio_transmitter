package org.ros.android.ros_controller;

import geometry_msgs.Twist;
import java.util.ArrayList;
import java.util.List;
import org.ros.concurrent.CancellableLoop;
import org.ros.message.Duration;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;
import trajectory_msgs.JointTrajectoryPoint;
/**
 * @author Qizhen.Mao
 *
 */
public class Talker extends AbstractNodeMain  {
	private ConnectedNode connectedNode;
	//arm movements
		/*	//right grippers
		private Publisher<pr2_controllers_msgs.Pr2GripperCommand> publisher_right_gripper;
		public pr2_controllers_msgs.Pr2GripperCommand right_grip_msg;  */
			//right arms
		private Publisher<trajectory_msgs.JointTrajectory> publisher_right_arm;
		private Publisher<trajectory_msgs.JointTrajectoryPoint> publisher_right_arm_points;
		private List<JointTrajectoryPoint> msg_points_Right;
		private List<String> joint_names_right;
		
		/*	//left grippers
		private Publisher<pr2_controllers_msgs.Pr2GripperCommand> publisher_left_gripper;
		public pr2_controllers_msgs.Pr2GripperCommand left_grip_msg;  */
			//left arms
		private Publisher<trajectory_msgs.JointTrajectory> publisher_left_arm;
		private Publisher<trajectory_msgs.JointTrajectoryPoint> publisher_left_arm_points;
		private List<JointTrajectoryPoint> msg_points_Left;
		private List<String> joint_names_left;
		
		//drive control
		private Publisher<geometry_msgs.Twist> publisher_drive_controller;
		private Twist drive_message;
		private CancellableLoop msgThread_drive_control;
	@Override
	public GraphName getDefaultNodeName() {
		// TODO Auto-generated method stub
		return  GraphName.of("Rose_Talker");
	}
	@Override
	public void onStart(final ConnectedNode connectedNode) 
	{
		this.connectedNode = connectedNode;
		//arm movements
	/*	//right gripper
	publisher_right_gripper = connectedNode.newPublisher("r_gripper_controller/command", pr2_controllers_msgs.Pr2GripperCommand._TYPE);
    right_grip_msg = publisher_right_gripper.newMessage(); */
    	//right arm
    publisher_right_arm = connectedNode.newPublisher("r_arm_controller/command",trajectory_msgs.JointTrajectory._TYPE);
    publisher_right_arm_points = connectedNode.newPublisher("r_arm_controller/command/points",trajectory_msgs.JointTrajectoryPoint._TYPE);
    msg_points_Right = new ArrayList<JointTrajectoryPoint>();
	msg_points_Right.add(publisher_right_arm_points.newMessage());
	
	/*	//left gripper
    publisher_left_gripper = connectedNode.newPublisher("l_gripper_controller/command", pr2_controllers_msgs.Pr2GripperCommand._TYPE);
    left_grip_msg = publisher_left_gripper.newMessage();  */
    	//left arm
    publisher_left_arm = connectedNode.newPublisher("l_arm_controller/command",trajectory_msgs.JointTrajectory._TYPE);
    publisher_left_arm_points = connectedNode.newPublisher("l_arm_controller/command/points",trajectory_msgs.JointTrajectoryPoint._TYPE);
	msg_points_Left = new ArrayList<JointTrajectoryPoint>();
	msg_points_Left.add(publisher_left_arm_points.newMessage());
	initPoints();
	 addJointNames();
	 //drive control
	    publisher_drive_controller = connectedNode.newPublisher("/base_controller/command", geometry_msgs.Twist._TYPE);
	    drive_message = publisher_drive_controller.newMessage();
	    changeControlMessage(0.0,0.0,0.0,0.0,0.0,0.0);
	}
	private void initPoints() 
	{
		int ind = 0;
		double[] newPositions= {0.2, 0.1, 0.7, 0.5, 0.2, 0.8, 0.9};
		 //points_Position_Right = newPositions ;
		msg_points_Right.get(ind).setPositions(newPositions);
		msg_points_Left.get(ind).setPositions(newPositions);
		//left_grip_msg.setPosition(0.0);
		//right_grip_msg.setPosition(0.0);
	}
	/*
	 * Change the direction the robot has to go.
	 */
	public void changeControlMessage(double linearX, double linearY, double linearZ, double angularX,
		  double angularY,double angularZ)
	{
		drive_message.getLinear().setX(linearX);
		drive_message.getLinear().setY(linearY);
		drive_message.getLinear().setZ(linearZ);
		
		drive_message.getAngular().setX(angularX);
		drive_message.getAngular().setY(angularY);
		drive_message.getAngular().setZ(angularZ);
	}
	/*
	 * While a drive control button is pushed the thread keeps sending messages every half a second
	 */
	public void startMsgThread()
	{
		msgThread_drive_control = new CancellableLoop() {
			@Override
			protected void loop() throws InterruptedException 
			{
				try
				{
					drive_message.setLinear(drive_message.getLinear());
					drive_message.setAngular(drive_message.getAngular());
					publisher_drive_controller.publish(drive_message);
			        Thread.sleep(250);   //initially 500 ms
			    }
				/*
				 * When the thread is interrupted (canceled by stopMsgThread)
				 * the message is put back on 0.0 in all directions and stopped
				 */
				catch(InterruptedException e){
					msgThread_drive_control.cancel();
				    changeControlMessage(0.0,0.0,0.0,0.0,0.0,0.0);
				}
				catch(RuntimeException e){
					msgThread_drive_control.cancel();
					changeControlMessage(0.0,0.0,0.0,0.0,0.0,0.0);
				}
			}
		};
		connectedNode.executeCancellableLoop(msgThread_drive_control);  
	}
	/*
	 * Called when the drive button that started the thread is released
	 */
	public void stopMsgThread()
	{
		msgThread_drive_control.cancel();
	}
	private void addJointNames() 
	{
		joint_names_right = new ArrayList<String>();
		joint_names_right.add("r_shoulder_pan_joint");
		joint_names_right.add("r_shoulder_lift_joint");
		joint_names_right.add("r_upper_arm_roll_joint");
		joint_names_right.add("r_elbow_flex_joint");
		joint_names_right.add("r_forearm_roll_joint");
		joint_names_right.add("r_wrist_flex_joint");
		joint_names_right.add("r_wrist_roll_joint");
		
		joint_names_left = new ArrayList<String>();
		joint_names_left.add("l_shoulder_pan_joint");
		joint_names_left.add("l_shoulder_lift_joint");
		joint_names_left.add("l_upper_arm_roll_joint");
		joint_names_left.add("l_elbow_flex_joint");
		joint_names_left.add("l_forearm_roll_joint");
		joint_names_left.add("l_wrist_flex_joint");
		joint_names_left.add("l_wrist_roll_joint");
	  }
	public void moveRightArm(double shoulderpanpoint, double shoulderliftpoint,
			double upperarmrollpoint, double elbowflexpoint,
			double forearmrollpoint, double wristflexpoint,
			double wristrollpoint) 
	{
		int ind = 0;
		double[] newPositions= {shoulderpanpoint, shoulderliftpoint, upperarmrollpoint, elbowflexpoint, forearmrollpoint, wristflexpoint, wristrollpoint};
		msg_points_Right.get(ind).setPositions(newPositions);
		
		double[] points_Velocity = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		msg_points_Right.get(ind).setVelocities(points_Velocity);
		
		Duration points_Timefromstart = new Duration();
		points_Timefromstart.secs = 1;
		msg_points_Right.get(ind).setTimeFromStart(points_Timefromstart);
		trajectory_msgs.JointTrajectory msg = publisher_right_arm.newMessage();
	
		 msg.setJointNames(joint_names_right);
		 msg.setPoints(msg_points_Right);
		 publisher_right_arm.publish(msg);
	}
	public void moveLeftArm(double shoulderpanpoint, double shoulderliftpoint,
			double upperarmrollpoint, double elbowflexpoint,
			double forearmrollpoint, double wristflexpoint,
			double wristrollpoint) 
	{
		int ind = 0;
		double[] newPositions= {shoulderpanpoint, shoulderliftpoint, upperarmrollpoint, 
				elbowflexpoint, forearmrollpoint, wristflexpoint, wristrollpoint};
		msg_points_Left.get(ind).setPositions(newPositions);
		
		double[] points_Velocity = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		msg_points_Left.get(ind).setVelocities(points_Velocity);
		
		Duration points_Timefromstart = new Duration();
		points_Timefromstart.secs = 1;
		msg_points_Left.get(ind).setTimeFromStart(points_Timefromstart);
		trajectory_msgs.JointTrajectory msg = publisher_left_arm.newMessage();
		
		 msg.setJointNames(joint_names_left);
		 msg.setPoints(msg_points_Left);
		 publisher_left_arm.publish(msg);
	}
	//public void moveLeftGripper(double position) 
	//public void moveRightGripper(double position) 
	
}

