package eu.domainobjects.presentation.main.process;

import java.awt.Color;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import eu.domainobjects.controller.ProcessEngineFacade;
//import eu.fbk.das.graphInput.ServiceDiagram;
//import eu.fbk.das.graphInput.ServiceTransition;
//import eu.fbk.das.graphInput.ServiceTransition;
import eu.fbk.das.process.engine.api.domain.ObjectDiagram;
import eu.fbk.das.process.engine.api.domain.ObjectTransition;

public class ObjectDiagramPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String STYLE_EDGE = "shape=connector;strokeColor=666666;strokeWidth=2;edgeStyle=elbowEdgeStyle;rounded=true;defaultMarkerSize=50;spacingTop=10;spacingBottom=5;fontSize=15;fontColor=#000000;verticalAlign=bottom;horizontalAlign=middle";
	private static final String STYLE_STATE = "rounded=true;strokeColor=666666;strokeWidth=2;fontColor=#C2185B;spacingLeft=10;spacingRight=10;spacingTop=10;spacingBottom=10;fontSize=15;verticalAlign=middle;fillColor=FFFFFF";
	private static final String BACKGROUD_PROCESS_COLOR = "#E0E0E0";
	// private static final String STYLE_IMAGE = "resources/Mail.png";
	// private static final String STYLE_IMAGE = "setBackgroundImage=Mail.png";
	private ProcessEngineFacade pef;
	private mxGraph graph = new mxGraph();
	private Object parent;
	private mxGraphComponent graphComponent;
	private Object v1 = null, v2 = null, edge = null;

	public ObjectDiagramPanel(ProcessEngineFacade pef) {
		this.pef = pef;
		graph = new mxGraph();
		parent = graph.getDefaultParent();
		graphComponent = new mxGraphComponent(graph);
		// add graphComponent to panel
		graphComponent.setBorder(null);
		graphComponent.setEnabled(false);
		graphComponent.getViewport().setBackground(new Color(1, 1, 1, 0f));
		this.setBackground(Color.decode(BACKGROUD_PROCESS_COLOR));
		this.add(graphComponent);

		// test for scroll by dragging
		setAutoscrolls(true);
		this.setRequestFocusEnabled(true);
		// DraggingMouseAdapterServices ma = new
		// DraggingMouseAdapterServices(this);
		// addMouseListener(ma);
		// addMouseMotionListener(ma);
	}

	public void clear() {
		mxGraph current = graphComponent.getGraph();
		((mxGraphModel) current.getModel()).clear();
	}

	public void defineObjectDiagramGraph(ObjectDiagram property) {

		double widthLayout = graphComponent.getLayoutAreaSize().getWidth();
		double heightLayout = graphComponent.getLayoutAreaSize().getHeight();

		Set<ObjectTransition> transitions = property.getTransitions();
		ObjectTransition[] arrayOrderedTransitions = new ObjectTransition[transitions
				.size()];
		arrayOrderedTransitions = transitions.toArray(arrayOrderedTransitions);

		Set<String> states = property.getStates();

		int x = 200;
		Iterator<String> iterator = states.iterator();
		for (int i = 0; i < states.size(); i++) {
			String activityName = iterator.next();

			v2 = graph.insertVertex(parent, null, activityName, x, 50, 150, 50,
					STYLE_STATE);
			graph.updateCellSize((mxCell) v2);
			x = x + 300;
		}

		int x1 = 50;
		for (int i = 0; i < arrayOrderedTransitions.length; i++) {
			ObjectTransition first = arrayOrderedTransitions[i];
			mxCell vertex1 = null, vertex2 = new mxCell();

			Object[] vertexArray1 = graph.getChildVertices(parent);
			for (int j = 0; j < vertexArray1.length; j++) {
				if (((mxCell) vertexArray1[j]).getValue().equals(
						first.getFrom())) {
					vertex2 = (mxCell) vertexArray1[j];
				}
			}

			Object[] vertexArray = graph.getChildVertices(parent);
			for (int a = 0; a < vertexArray.length; a++) {
				if (((mxCell) vertexArray[a]).getValue().equals(first.getTo())) {
					vertex1 = (mxCell) vertexArray[a];
					// if (i == arrayOrderedTransitions.length - 1) {
					// vertex1.getGeometry().setX(x1 + 150);
					// } else {
					// vertex1.getGeometry().setX(x1);
					// }
					// x1 = x1 + 150;
				}
			}

			edge = graph.insertEdge(parent, null, first.getEvent(), vertex2,
					vertex1, STYLE_EDGE);
			double edgeX = ((mxCell) edge).getGeometry().getWidth()
					+ first.getEvent().length() + 5;
			((mxCell) edge).getGeometry().setWidth(edgeX);
			// mxGeometry edgeGeometry = (mxGeometry) ((mxCell) edge)
			// .getGeometry().clone();
			// edgeGeometry.setWidth(first.getEvent().length());
			// ((mxCell) edge).setGeometry(edgeGeometry);
			// graph.updateCellSize(edge);
		}

		double width = graph.getGraphBounds().getWidth();
		double height = graph.getGraphBounds().getHeight();

		graph.getModel().setGeometry(graph.getDefaultParent(),
				new mxGeometry(30, 40, widthLayout, heightLayout));

		mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
		layout.setOrientation(SwingConstants.WEST);
		layout.execute(graph.getDefaultParent());

		graphComponent.setRequestFocusEnabled(true);
		this.setRequestFocusEnabled(true);

		graph.getModel().endUpdate();
		graph.removeCells();
		graph.refresh();

	}
}
