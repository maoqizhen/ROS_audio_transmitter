package org.ros.android.rviz_for_android.layers;

import android.content.Context;
import android.os.Handler;


import org.ros.android.renderer.Camera;
import org.ros.android.renderer.Vertices;
import org.ros.android.renderer.VisualizationView;
import org.ros.android.renderer.layer.TfLayer;
import org.ros.android.renderer.shapes.Color;
import org.ros.android.rviz_for_android.Rviz_map.AvailableLayerType;

import org.ros.android.rviz_for_android.prop.ColorProperty;
import org.ros.android.rviz_for_android.prop.LayerWithProperties;
import org.ros.android.rviz_for_android.prop.Property;
import org.ros.android.rviz_for_android.prop.Property.PropertyUpdateListener;
import org.ros.android.rviz_for_android.prop.ReadOnlyProperty.StatusColor;

import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.rosjava_geometry.FrameTransformTree;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.microedition.khronos.opengles.GL10;

/**
 * @author damonkohler@google.com (Damon Kohler)
 */
public class GridCellsLayer extends EditableStatusSubscriberLayer<nav_msgs.GridCells> implements LayerWithProperties, TfLayer {

  private Color drawColor;
  //private final Lock lock;

  //private GraphName frame;
  //private Camera camera;
  private volatile boolean isReady = false;
  private nav_msgs.GridCells mostRecent;
  private Context context;
  private FrameTransformTree frameTransformTree;
  public GridCellsLayer(Camera cam, GraphName topicName, Context context) {
	  super(topicName, nav_msgs.GridCells._TYPE, cam);
	  this.context = context;
	  drawColor = new Color(1f, 1f, 1f, 1f);
	  prop.addSubProperty(new ColorProperty("Color", drawColor, new PropertyUpdateListener<Color>() {
			@Override
			public void onPropertyChanged(Color newval) {
				drawColor = newval;
			}
		}));
  }
  @Override
	public void onStart(ConnectedNode connectedNode, Handler handler, final FrameTransformTree frameTransformTree, final Camera camera) {
		super.onStart(connectedNode, handler, frameTransformTree, camera);

		this.frameTransformTree = frameTransformTree;

		updateStatus(frameTransformTree, camera);
	}
  private void updateStatus(FrameTransformTree frameTransformTree, Camera camera) {
		if(!isReady) {
			statusController.setStatus("No signal exists!", StatusColor.ERROR);
		} else {
			statusController.setFrameChecking(true);
		}
	}
  @Override
	protected void onMessageReceived(nav_msgs.GridCells data) {
		super.onMessageReceived(data);
		
		statusController.setFrameChecking(false);
		statusController.setStatus("Signal loading...", StatusColor.OK);
		mostRecent = data;
		isReady = true;
		updateStatus(frameTransformTree, camera);
	}
  @Override
  public void draw(GL10 gl) {
	  if(isReady) {
			super.draw(gl);
			float pointSize =
			        (float) (Math.max(mostRecent.getCellWidth(), mostRecent.getCellHeight()) * camera.getZoom());
			    float[] vertices = new float[3 * mostRecent.getCells().size()];
			    int i = 0;
			    for (geometry_msgs.Point cell : mostRecent.getCells()) {
			      vertices[i] = (float) cell.getX();
			      vertices[i + 1] = (float) cell.getY();
			      vertices[i + 2] = 0.0f;
			      i += 3;
			    }
			    gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			    gl.glVertexPointer(3, GL10.GL_FLOAT, 0, Vertices.toFloatBuffer(vertices));
			    drawColor.apply(gl);
			    gl.glPointSize(pointSize);
			    gl.glDrawArrays(GL10.GL_POINTS, 0, mostRecent.getCells().size());
			    gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		}
   
    
    
  }

 /*@Override
  public void onStart(ConnectedNode connectedNode, Handler handler,
      final FrameTransformTree frameTransformTree, Camera camera) {
    super.onStart(connectedNode, handler, frameTransformTree, camera);
    this.camera = camera;
    getSubscriber().addMessageListener(new MessageListener<nav_msgs.GridCells>() {
      @Override
      public void onNewMessage(nav_msgs.GridCells data) {
        frame = GraphName.of(data.getHeader().getFrameId());
        if (frameTransformTree.lookUp(frame) != null) {
          if (lock.tryLock()) {
            message = data;
            ready = true;
            lock.unlock();
          }
        }
      }
    });
  }*/
  @Override
	public boolean isEnabled() {
		return prop.getValue();
	}
  @Override
	public void setName(String name) {
		super.setName(name);
	}
  @Override
	public Property<?> getProperties() {
		return prop;
	}
  @Override
	public void onShutdown(VisualizationView view, Node node) {
		super.onShutdown(view, node);
	}
  @Override
	protected String getMessageFrameId(nav_msgs.GridCells msg) {
		return msg.getHeader().getFrameId();
	}
  @Override
	public GraphName getFrame() {
		return super.frame;
	}
  @Override
	public AvailableLayerType getType() {
		return AvailableLayerType.GridCells;
	}
}
