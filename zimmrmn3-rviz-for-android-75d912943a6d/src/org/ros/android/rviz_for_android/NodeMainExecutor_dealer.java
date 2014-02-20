/**
 * 
 */
package org.ros.android.rviz_for_android;

import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

/**
 * @author maoqizhen
 *
 */
public class NodeMainExecutor_dealer {
	private static NodeMainExecutor nodeMainExecutor;
	private static NodeConfiguration nodeConfiguration;
	private static AbstractNodeMain map_talker;
    public static AbstractNodeMain getMap_talker() {
		return map_talker;
	}
	public static void setMap_talker(AbstractNodeMain map_talker) {
		NodeMainExecutor_dealer.map_talker = map_talker;
	}
	public static void setNodeMainExecutor(NodeMainExecutor exeIn){
    	nodeMainExecutor=exeIn;
    }
    public static void setNodeConfiguration(NodeConfiguration configIn){
    	nodeConfiguration=configIn;
    }
	
    public static NodeMainExecutor getNodeMainExecutor(){
    	return nodeMainExecutor;
    }
    public static NodeConfiguration getNodeConfiguration(){
    	return nodeConfiguration;
    }

}
