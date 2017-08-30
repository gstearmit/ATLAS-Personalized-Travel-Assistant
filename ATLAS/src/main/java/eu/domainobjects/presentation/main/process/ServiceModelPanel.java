package eu.domainobjects.presentation.main.process;

import java.awt.Color;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JPanel;

import com.mxgraph.layout.mxGraphLayout;
import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import eu.domainobjects.controller.ProcessEngineFacade;
import eu.fbk.das.process.engine.api.domain.ServiceDiagram;
import eu.fbk.das.process.engine.api.domain.ServiceTransition;

public class ServiceModelPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final String BACKGROUD_PROCESS_COLOR = "#E0E0E0";
	private static final String STYLE_EDGE = "strokeColor=666666;strokeWidth=2";
	private static final String STYLE_ABSTRACT = "strokeColor=666666;strokeWidth=2;fontColor=000000;spacingLeft=10;spacingRight=10;spacingTop=10;spacingBottom=10;fontSize=15;horizontalAlign=center;verticalAlign=top;dashed=true;dashPattern=5;fillColor=FFFFFF";
	private static final String STYLE_DEFAULT = "strokeColor=666666;strokeWidth=2;fontColor=000000;spacingLeft=10;spacingRight=10;spacingTop=10;spacingBottom=10;fontSize=15;verticalAlign=top;fillColor=FFFFFF;margin=20px";
	private static final String STYLE_IMG = "shape=image;image=/images/mailGreen.png";
	private mxGraph graph;
	private ProcessEngineFacade pef;
	private Object parent;
	private mxGraphLayout layout;
	private mxGraphComponent graphComponent;
	private Object v1 = null, v2 = null, v3 = null, v4 = null, v5 = null,
			v6 = null;

	public ServiceModelPanel(ProcessEngineFacade pef) {
		this.pef = pef;
		graph = new mxGraph();
		parent = graph.getDefaultParent();
		graphComponent = new mxGraphComponent(graph);
		layout = new mxParallelEdgeLayout(graphComponent.getGraph());
		// add graphComponent to panel
		graphComponent.setBorder(null);
		graphComponent.setEnabled(false);
		graphComponent.getViewport().setBackground(new Color(1, 1, 1, 0f));
		this.setBackground(Color.decode(BACKGROUD_PROCESS_COLOR));
		this.add(graphComponent);

		// test for scroll by dragging
		setAutoscrolls(true);
	}

	public void clear() {
		mxGraph current = graphComponent.getGraph();
		((mxGraphModel) current.getModel()).clear();
	}

	public void defineServiceDiagramProcess(ServiceDiagram service) {

		Set<String> abstractsActivities = service.getAbstracts();
		Set<String> inputsActivities = service.getInputs();
		Set<String> outputsActivities = service.getOutputs();
		Set<String> concretesActivities = service.getConcretes();

		cleanActivitiesSets(abstractsActivities, inputsActivities,
				outputsActivities, concretesActivities);

		int x = 20;
		int y = 20;
		x = parseInputActivities(inputsActivities, x, y);
		x = parseConcreteActivities(concretesActivities, x, y);
		x = parseAbstractActivities(abstractsActivities, x, y);
		x = parseOutputActivities(outputsActivities, x, y);

		ServiceTransition[] arrayOrderedTransitions = defineOrderedFragmentTransition(service);

		parseServiceTransitions(inputsActivities, outputsActivities,
				arrayOrderedTransitions);

		// mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
		// layout.setOrientation(SwingConstants.WEST);
		// layout.execute(graph.getDefaultParent());

		graph.getModel().endUpdate();
		graph.refresh();

	}

	private void parseServiceTransitions(Set<String> inputsActivities,
			Set<String> outputsActivities,
			ServiceTransition[] arrayOrderedTransitions) {
		int x1 = 50;
		for (int i = 0; i < arrayOrderedTransitions.length - 1; i++) {
			ServiceTransition first = arrayOrderedTransitions[i];
			ServiceTransition second = arrayOrderedTransitions[i + 1];
			mxCell vertex1 = new mxCell();
			mxCell vertex2 = new mxCell();
			mxCell smsVertex = new mxCell();

			if (first.getTo().equalsIgnoreCase(second.getFrom())
					&& !first.getAction().equals("RESET")) {
				Object[] vertexArray = graph.getChildVertices(parent);
				for (int j = 0; j < vertexArray.length; j++) {
					if (((mxCell) vertexArray[j]).getValue().equals(
							first.getAction())) {
						vertex2 = (mxCell) vertexArray[j];
						graph.getChildVertices(vertex2);
						vertex2.getGeometry().setX(x1);

						if (inputsActivities.contains(vertex2.getValue())) {
							for (int z = 0; z < vertexArray.length; z++) {
								smsVertex = (mxCell) vertexArray[z];
								if (vertex2.getValue()
										.equals(smsVertex.getId())) {
									graph.getChildVertices(smsVertex);
									smsVertex.getGeometry().setX(x1 + 75);
								}
							}
						} else if (outputsActivities.contains(vertex2
								.getValue())) {
							for (int k = 0; k < vertexArray.length; k++) {
								smsVertex = (mxCell) vertexArray[k];
								if (vertex2.getValue()
										.equals(smsVertex.getId())) {
									graph.getChildVertices(smsVertex);
									smsVertex.getGeometry().setX(x1 + 75);
								}
							}
						}
						x1 = x1 + 125;
						break;
					}
				}

				for (int j = 0; j < vertexArray.length; j++) {
					if (((mxCell) vertexArray[j]).getValue().equals(
							second.getAction())) {
						vertex1 = (mxCell) vertexArray[j];
						graph.getChildVertices(vertex1);

						if (i + 1 == arrayOrderedTransitions.length - 1) {
							vertex1.getGeometry().setX(x1 + 125);
						} else {
							vertex1.getGeometry().setX(x1);
						}

						if (inputsActivities.contains(vertex1.getValue())) {
							for (int o = 0; o < vertexArray.length; o++) {
								smsVertex = (mxCell) vertexArray[o];
								if (vertex1.getValue()
										.equals(smsVertex.getId())) {
									graph.getChildVertices(smsVertex);
									smsVertex.getGeometry().setX(x1 + 200);
								}
							}
						} else if (outputsActivities.contains(vertex1
								.getValue())) {
							for (int o = 0; o < vertexArray.length; o++) {
								smsVertex = (mxCell) vertexArray[o];
								if (vertex1.getValue()
										.equals(smsVertex.getId())) {
									graph.getChildVertices(smsVertex);
									smsVertex.getGeometry().setX(x1 + 200);
								}
							}
						}
						x1 = x1 + 125;
						break;
					}
				}
			}
			graph.insertEdge(parent, null, "", vertex2, vertex1, STYLE_EDGE);
		}
		this.setVisible(true);
	}

	private int parseOutputActivities(Set<String> outputsActivities, int x,
			int y) {
		Iterator<String> iterator = outputsActivities.iterator();
		for (int i = 0; i < outputsActivities.size(); i++) {
			String activityName = iterator.next();

			v4 = graph.insertVertex(parent, null, activityName, x, y, 200, 40,
					STYLE_DEFAULT);
			// graph.updateCellSize((mxCell) v4);
			v6 = graph.insertVertex(parent, null, "", x + 75, 85, 50, 50,
					STYLE_IMG);
			((mxCell) v6).setId(activityName);

			graph.insertEdge(parent, null, "", v6, v4, STYLE_EDGE);
			x = x + 300;
		}
		return x;
	}

	private int parseAbstractActivities(Set<String> abstractsActivities, int x,
			int y) {
		Iterator<String> iterator = abstractsActivities.iterator();
		for (int i = 0; i < abstractsActivities.size(); i++) {
			String activityName = iterator.next();

			v3 = graph.insertVertex(parent, null, activityName, x, y, 200, 40,
					STYLE_ABSTRACT);
			// graph.updateCellSize((mxCell) v3);
			x = x + 300;

		}
		return x;
	}

	private int parseConcreteActivities(Set<String> concretesActivities, int x,
			int y) {
		Iterator<String> iterator = concretesActivities.iterator();
		for (int i = 0; i < concretesActivities.size(); i++) {
			String activityName = iterator.next();

			v2 = graph.insertVertex(parent, null, activityName, x, y, 200, 40,
					STYLE_DEFAULT);
			// graph.updateCellSize((mxCell) v2);
			x = x + 300;
		}
		return x;
	}

	private int parseInputActivities(Set<String> inputsActivities, int x, int y) {

		Iterator<String> iterator = inputsActivities.iterator();
		for (int i = 0; i < inputsActivities.size(); i++) {
			String activityName = iterator.next();

			v1 = graph.insertVertex(parent, null, activityName, x, y, 200, 40,
					STYLE_DEFAULT);
			// graph.updateCellSize((mxCell) v1);

			v5 = graph.insertVertex(parent, null, "", x + 75, 85, 50, 50,
					STYLE_IMG);

			((mxCell) v5).setId(activityName);

			graph.insertEdge(parent, null, "", v1, v5, STYLE_EDGE);
			x = x + 300;
		}
		return x;
	}

	private void cleanActivitiesSets(Set<String> abstractsActivities,
			Set<String> inputsActivities, Set<String> outputsActivities,
			Set<String> concretesActivities) {
		inputsActivities.remove("RESET");
		outputsActivities.remove("RESET");
		Iterator<String> abstractIterator = abstractsActivities.iterator();
		Iterator<String> concreteIterator = concretesActivities.iterator();

		for (int i = 0; i < abstractsActivities.size(); i++) {
			String activityName = abstractIterator.next();

			if (inputsActivities.contains(activityName)) {
				inputsActivities.remove(activityName);
			}
		}

		for (int i = 0; i < concretesActivities.size(); i++) {
			String activityName = concreteIterator.next();

			if (outputsActivities.contains(activityName)) {
				outputsActivities.remove(activityName);
			}
		}
	}

	private ServiceTransition[] defineOrderedFragmentTransition(
			ServiceDiagram service) {
		Set<ServiceTransition> transitions = service.getTransitions();
		int arraySize = 0;
		Iterator<ServiceTransition> iter = transitions.iterator();
		while (iter.hasNext()) {
			if (!iter.next().getAction().equals("RESET")) {
				arraySize += 1;
			}
		}

		ServiceTransition[] arrayOrderedTransitions = new ServiceTransition[arraySize];

		for (int i = 0; i < transitions.size(); i++) {
			Iterator<ServiceTransition> iterator = transitions.iterator();
			while (iterator.hasNext()) {
				ServiceTransition st = iterator.next();
				if (st.getFrom().equals("ST".concat(Integer.toString(i)))
						&& !st.getAction().equals("RESET")) {
					arrayOrderedTransitions[i] = st;
					break;
				}
			}
		}
		return arrayOrderedTransitions;
	}
}