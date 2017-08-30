package eu.domainobjects.presentation.main.process;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.primitives.Ints;
import com.mxgraph.layout.mxGraphLayout;
import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import eu.domainobjects.controller.ProcessEngineFacade;
import eu.domainobjects.presentation.main.MainWindow;
import eu.domainobjects.presentation.main.action.DraggingMouseAdapter;
import eu.domainobjects.presentation.main.action.listener.MouseActivityListener;
import eu.domainobjects.utils.OrderedGraph;
import eu.fbk.das.process.engine.api.domain.AbstractActivity;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;
import eu.fbk.das.process.engine.api.domain.ScopeActivity;
import eu.fbk.das.process.engine.api.domain.SwitchActivity;
import eu.fbk.das.process.engine.api.domain.WhileActivity;

/**
 * Panel for process model visualization using {@link mxGraph}
 */
public class ProcessModelPanel extends JPanel {

	private static final long serialVersionUID = 3711288024677922473L;

	private static final Logger logger = LogManager
			.getLogger(ProcessModelPanel.class);

	// graph styles whiteSpace=wrap; verticalAlign=middle; strokeColor=000000;
	private static final String STYLE_ABSTRACT = "strokeColor=666666;strokeWidth=2;fontColor=000000;spacingLeft=10;spacingRight=10;spacingTop=10;spacingBottom=10;fontSize=15;horizontalAlign=center;verticalAlign=top;dashed=true;dashPattern=5;fillColor=FFFFFF";
	private static final String STYLE_RUNNING = "strokeColor=666666;strokeWidth=2;fontColor=000000;spacingLeft=10;spacingRight=10;spacingTop=10;spacingBottom=10;fontSize=15;verticalAlign=top;fillColor=FF0000";
	private static final String STYLE_ABSTRACT_EXECUTED = "strokeColor=666666;strokeWidth=2;fontColor=000000;spacingLeft=10;spacingRight=10;spacingTop=10;spacingBottom=10;fontSize=15;verticalAlign=top;dashed=true;dashPattern=5;fillColor=6ECE74";
	private static final String STYLE_ABSTRACT_RUNNING = "strokeColor=666666;strokeWidth=2;fontColor=000000;spacingLeft=10;spacingRight=10;spacingTop=10;spacingBottom=10;fontSize=15;verticalAlign=top;dashed=true;dashPattern=5;fillColor=6ECE74";
	private static final String STYLE_SWITCH = "strokeColor=666666;strokeWidth=2;fontColor=000000;spacingLeft=10;spacingRight=10;spacingTop=10;spacingBottom=10;fontSize=15;verticalAlign=top;fillColor=FFFFFF";
	private static final String STYLE_SWITCH_EXECUTED = "strokeColor=666666;strokeWidth=2;fontColor=000000;spacingLeft=10;spacingRight=10;spacingTop=10;spacingBottom=10;fontSize=15;verticalAlign=top;fillColor=6ECE74";
	private static final String STYLE_SWITCH_RUNNING = "strokeColor=666666;strokeWidth=2;fontColor=000000;spacingLeft=10;spacingRight=10;spacingTop=10;spacingBottom=10;fontSize=15;verticalAlign=top;fillColor=6ECE74";
	private static final String STYLE_EXECUTED = "strokeColor=666666;strokeWidth=2;fontColor=000000;spacingLeft=10;spacingRight=10;spacingTop=10;spacingBottom=10;fontSize=15;verticalAlign=top;fillColor=6ECE74";
	private static final String STYLE_DEFAULT = "strokeColor=666666;strokeWidth=2;fontColor=000000;spacingLeft=10;spacingRight=10;spacingTop=10;spacingBottom=10;fontSize=15;verticalAlign=top;fillColor=FFFFFF;margin=20px";
	private static final String STYLE_WHILE = "strokeColor=666666;strokeWidth=2;fontColor=000000;spacingLeft=10;spacingRight=10;spacingTop=10;spacingBottom=10;fontSize=15;verticalAlign=top;fillColor=FFFFFF";
	private static final String STYLE_SCOPE = "strokeColor=666666;strokeWidth=2;fontColor=000000;spacingLeft=10;spacingRight=10;spacingTop=10;spacingBottom=10;fontSize=15;verticalAlign=center;strokeWidth=2;dashed=true;dashed=1;shadow=1;dashPattern=1;rounded=true;fillColor=FFFFFF";
	private static final String STYLE_WHILE_EXECUTED = "strokeColor=666666;strokeWidth=2;fontColor=000000;spacingLeft=10;spacingRight=10;spacingTop=10;spacingBottom=10;fontSize=15;verticalAlign=top;fillColor=6ECE74";
	private static final String STYLE_WHILE_RUNNING = "strokeColor=666666;strokeWidth=2;fontColor=000000;spacingLeft=10;spacingRight=10;spacingTop=10;spacingBottom=10;fontSize=15;verticalAlign=top;fillColor=FFA500";
	private static final String STYLE_EDGE = "strokeColor=666666;strokeWidth=2";

	// colors
	private static final String BACKGROUD_PROCESS_COLOR = "#E0E0E0";

	// private variables
	private mxGraphComponent graphComponent;
	private ProcessEngineFacade pef;
	private int idgenerator = 0;
	/** save refinements for later use, use pid and name of activity for key **/
	private Map<String, OrderedGraph> refinements = new HashMap<String, OrderedGraph>();
	// temporary map current processes's activity names
	private Map<String, ProcessActivity> activityName = new HashMap<String, ProcessActivity>();
	private mxGraphLayout layout;
	private int sx;
	private int sy = 20;
	private mxCell lastCell;
	private final int xOffset = 20;
	private final int yOffset = 15;

	public ProcessModelPanel(ProcessEngineFacade pef) {
		this.pef = pef;
		// create graphic component
		graphComponent = new mxGraphComponent(new mxGraph());
		// build layout for graph
		layout = new mxParallelEdgeLayout(graphComponent.getGraph());
		// add graphComponent to panel
		graphComponent.setBorder(null);
		graphComponent.setEnabled(false);
		graphComponent.getViewport().setBackground(new Color(1, 1, 1, 0f)); // transparent
																			// hack
																			// (for
																			// MAC)
		this.setBackground(Color.decode(BACKGROUD_PROCESS_COLOR));
		this.add(graphComponent);

		// test for scroll by dragging
		setAutoscrolls(true);

		DraggingMouseAdapter ma = new DraggingMouseAdapter(this);
		addMouseListener(ma);
		addMouseMotionListener(ma);
	}

	/**
	 * Init panel: add click listener on graph
	 * 
	 * @param mainWindow
	 */
	public void init(MainWindow mainWindow) {
		if (graphComponent != null) {
			graphComponent.getGraphControl().addMouseListener(
					new MouseActivityListener(graphComponent, mainWindow));
		} else {
			logger.warn("GraphComponet is null, impossible to ini processModelPanel");
		}
	}

	/**
	 * Clear graph
	 * 
	 * @see {@link mxGraphModel#clear}
	 */
	public void clear() {
		mxGraph current = graphComponent.getGraph();
		((mxGraphModel) current.getModel()).clear();
		activityName.clear();
		lastCell = null;
		sx = 0;
		sy = 20;
	}

	/**
	 * Update internal graph using {@link ProcessDiagram} informations
	 * 
	 * @param pd
	 *            , {@link ProcessDiagram} used to display information as graph
	 */
	public void updateProcess(ProcessDiagram pd) {
		if (pd.getActivities() == null || pd.getActivities().isEmpty()) {
			logger.warn("No activities to show, updateProcess failed");
			return;
		}
		clear();
		// update
		mxGraph current = graphComponent.getGraph();
		current = getGraphFromProcessDiagram(pd, current).getGraph();
		// apply layout to graph
		layout.execute(current.getDefaultParent());
	}

	private OrderedGraph getGraphFromProcessDiagram(ProcessDiagram pd,
			mxGraph current) {
		List<String> order = new ArrayList<String>();
		OrderedGraph response = new OrderedGraph();
		Object parent = current.getDefaultParent();
		current.getModel().beginUpdate();
		try {
			// build graph using list of activities
			Object lastInserted = null;
			for (int i = 0; i < pd.getActivities().size(); i++) {
				ProcessActivity pa = pd.getActivities().get(i);
				activityName.put(pa.getName(), pa.clone());
				if (lastInserted == null) {
					Object v = insertVertex(current, parent, pa.getName(),
							getStyle(pa));
					order.add(pa.getName());
					lastInserted = v;
					// handle abstract activity case
					current = getGraphForActivity(pd, current, pa);

				} else {
					Object v = insertVertex(current, parent, pa.getName(),
							getStyle(pa));
					current.insertEdge(parent, null, "", lastInserted, v,
							STYLE_EDGE);
					order.add(pa.getName());
					lastInserted = v;
					// handle abstract activity case
					current = getGraphForActivity(pd, current, pa);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			current.getModel().endUpdate();
		}
		response.setGraph(current);
		response.setOrder(order);
		return response;
	}

	private mxGraph getGraphForActivity(ProcessDiagram pd, mxGraph current,
			ProcessActivity pa) {
		if (pef != null && pa != null && pd.getCurrentActivity() != null
				&& pd.getCurrentActivity().getName() != null
				&& pa.getName() != null) {
			if (pa.isAbstract()) {
				return getGraphForAbstractActivity(pd, current,
						(AbstractActivity) pa);
			} else if (pa.isScope()) {
				return getGraphForScopeActivity(pd, current, (ScopeActivity) pa);
			} else if (pa.isWhile()) {
				return getGraphForWhileActivity(pd, current, (WhileActivity) pa);
			} else if (pa.isSwitch()) {
				return getGraphForSwitchActivity(pd, current,
						(SwitchActivity) pa);
			}
		}
		return current;
	}

	private mxGraph getGraphForWhileActivity(ProcessDiagram pd,
			mxGraph current, WhileActivity pa) {
		logger.debug(pa.getName());
		ProcessDiagram subProcess = pa.getDefaultBranch();
		OrderedGraph ref = new OrderedGraph();
		ref = getGraphFromProcessDiagram(subProcess, ref.getGraph());
		ref.setActivityName(pa.getName());
		refinements.put(pd.getpid() + "_" + pa.getName(), ref);
		current = insertIn(current, ref, pa, getStyle(pa));
		return current;
	}

	private mxGraph getGraphForScopeActivity(ProcessDiagram pd,
			mxGraph current, ScopeActivity pa) {
		logger.debug(pa.getName());
		ProcessDiagram subProcess = pa.getBranch();
		OrderedGraph ref = new OrderedGraph();
		ref = getGraphFromProcessDiagram(subProcess, ref.getGraph());
		ref.setActivityName(pa.getName());
		refinements.put(pd.getpid() + "_" + pa.getName(), ref);
		current = insertIn(current, ref, pa, getStyle(pa));
		return current;
	}

	private mxGraph getGraphForAbstractActivity(ProcessDiagram pd,
			mxGraph current, AbstractActivity pa) {
		if ((pef.hasRefinements(pd) && pd.getCurrentActivity().getName()
				.equals(pa.getName()))) {
			logger.debug(pa.getName());
			ProcessDiagram subProcess = pef.getRefinement(pd);
			OrderedGraph ref = new OrderedGraph();
			ref = getGraphFromProcessDiagram(subProcess, ref.getGraph());
			ref.setActivityName(pa.getName());
			refinements.put(pd.getpid() + "_" + pa.getName(), ref);
			current = insertIn(current, ref, pa, getStyle(pa));
		} else if (!pa.isRunning() && pa.isExecuted()) {
			OrderedGraph gra = refinements
					.get(pd.getpid() + "_" + pa.getName());
			current = insertIn(current, gra, pa, STYLE_EXECUTED);
		} else if (!pa.isRunning() || pa.isExecuted()) {
			OrderedGraph gra = refinements
					.get(pd.getpid() + "_" + pa.getName());
			current = insertIn(current, gra, pa, STYLE_EXECUTED);
		}
		return current;
	}

	private mxGraph getGraphForSwitchActivity(ProcessDiagram pd,
			mxGraph current, SwitchActivity pa) {
		if ((pef.hasRefinements(pd) && pd.getCurrentActivity().getName()
				.equals(pa.getName()))) {
			logger.debug(pa.getName());
			ProcessDiagram subProcess = pef.getRefinement(pd);
			OrderedGraph ref = new OrderedGraph();
			ref = getGraphFromProcessDiagram(subProcess, ref.getGraph());
			ref.setActivityName(pa.getName());
			refinements.put(pd.getpid() + "_" + pa.getName(), ref);
			current = insertIn(current, ref, pa, getStyle(pa));
		} else if (!pa.isRunning() && pa.isExecuted()) {
			OrderedGraph gra = refinements
					.get(pd.getpid() + "_" + pa.getName());
			current = insertIn(current, gra, pa, STYLE_EXECUTED);
		} else if (!pa.isRunning() || pa.isExecuted()) {
			OrderedGraph gra = refinements
					.get(pd.getpid() + "_" + pa.getName());
			current = insertIn(current, gra, pa, STYLE_EXECUTED);
		}
		return current;
	}

	private String getStyle(ProcessActivity pa) {
		if (pa.isAbstract() && pa.isExecuted()) {
			return STYLE_ABSTRACT_EXECUTED;
		}
		if (pa.isAbstract() && pa.isRunning()) {
			return STYLE_ABSTRACT_RUNNING;
		}
		if (pa.isAbstract()) {
			return STYLE_ABSTRACT;
		}
		if (pa.isSwitch() && pa.isExecuted()) {
			return STYLE_SWITCH_EXECUTED;
		}
		if (pa.isSwitch() && pa.isRunning()) {
			return STYLE_SWITCH_RUNNING;
		}
		if (pa.isSwitch()) {
			return STYLE_SWITCH;
		}
		if (pa.isWhile() && pa.isExecuted()) {
			return STYLE_WHILE_EXECUTED;
		}
		if (pa.isWhile() && pa.isRunning()) {
			return STYLE_WHILE_RUNNING;
		}
		if (pa.isWhile()) {
			return STYLE_WHILE;
		}
		if (pa.isScope()) {
			return STYLE_SCOPE;
		}
		// For better display, set running&executed style after type activity
		// check
		if (pa.isRunning()) {
			return STYLE_RUNNING;
		}
		if (pa.isExecuted()) {
			return STYLE_EXECUTED;
		}
		return STYLE_DEFAULT;
	}

	private mxGraph insertIn(mxGraph graph, OrderedGraph ref,
			ProcessActivity pa, String style) {
		if (ref == null || ref.getGraph() == null) {
			return graph;
		}
		// update current Graph using processdiagram in input, using pa as
		// reference
		Object parent = graph.getDefaultParent();

		graph.getModel().beginUpdate();
		try {
			mxCell target = (mxCell) ((mxGraphModel) graph.getModel())
					.getCell(pa.getName());
			target.getGeometry()
					.setHeight(target.getGeometry().getHeight() * 3);

			activityName.put(pa.getName(), pa.clone());

			// add link all refGraph cells to graph, using order as reference
			mxICell lastInserted = null;
			Map<String, Object> cells = ((mxGraphModel) ref.getGraph()
					.getModel()).getCells();
			double tempw = 0;
			int dx = xOffset;
			for (String key : ref.getOrder()) {
				// avoid to use numbered keys (edges)
				if (Ints.tryParse(key) == null) {
					mxCell cell = (mxCell) cells.get(key);
					if (activityName.get(cell.getId()) != null) {
						ProcessActivity act = activityName.get(cell.getId());
						style = getStyle(act);
					}
					cell.setStyle(style);

					if (lastInserted == null) {
						lastInserted = target.insert(cell);
						cell.getGeometry().setY(
								target.getGeometry().getY() + yOffset);
						cell.getGeometry().setX(dx);
						dx += xOffset + cell.getGeometry().getWidth();
					} else {
						mxICell t1 = target.insert(cell);
						cell.getGeometry().setY(
								target.getGeometry().getY() + yOffset);
						cell.getGeometry().setX(dx);
						dx += xOffset + cell.getGeometry().getWidth();
						graph.insertEdge(parent, String.valueOf(++idgenerator),
								"", lastInserted, t1, STYLE_EDGE);
						lastInserted = t1;
					}
					tempw += cell.getGeometry().getWidth() + xOffset;
				}
			}
			// set father width
			target.getGeometry().setWidth(tempw + xOffset);
		} finally {
			graph.getModel().endUpdate();
		}
		return graph;
	}

	private Object insertVertex(mxGraph current, Object parent, String name,
			String style) {
		double w = 170;
		double x = 0;
		if (lastCell == null) {
			x = 0;
		} else {
			if (isParentAbstract(lastCell) || isParentScope(lastCell)
					|| isParentWhile(lastCell) || isParentSwitch(lastCell)) {
				// last cell was inside refinement, we need to find father of
				// this refinement and use his coordinate to calculate
				// coordinate for placement
				mxICell pc = getRoot(lastCell);
				if (pc.getGeometry() != null) {
					x = pc.getGeometry().getX() + pc.getGeometry().getWidth()
							+ 30;
				}
			} else {
				x = lastCell.getGeometry().getX()
						+ lastCell.getGeometry().getWidth() + 30;
			}
		}
		Object v = current
				.insertVertex(parent, name, name, x, sy, w, 30, style);
		// activate autosize for this cell
		current.updateCellSize(v);
		lastCell = (mxCell) v;
		return v;
	}

	private boolean isParentScope(mxCell cell) {
		mxICell parent = getRoot(cell.getParent());
		if (parent == null || parent.getStyle() == null) {
			return false;
		}
		return parent.getStyle().equalsIgnoreCase(STYLE_SCOPE);
	}

	private boolean isParentWhile(mxCell cell) {
		mxICell parent = getRoot(cell.getParent());
		if (parent == null || parent.getStyle() == null) {
			return false;
		}
		return parent.getStyle().equalsIgnoreCase(STYLE_WHILE)
				|| parent.getStyle().equalsIgnoreCase(STYLE_WHILE_RUNNING)
				|| parent.getStyle().equalsIgnoreCase(STYLE_WHILE_EXECUTED);
	}

	private boolean isParentAbstract(mxCell cell) {
		mxICell parent = getRoot(cell.getParent());
		if (parent == null || parent.getStyle() == null) {
			return false;
		}
		return parent.getStyle().equalsIgnoreCase(STYLE_ABSTRACT)
				|| parent.getStyle().equalsIgnoreCase(STYLE_ABSTRACT_RUNNING)
				|| parent.getStyle().equalsIgnoreCase(STYLE_ABSTRACT_EXECUTED);
	}

	private boolean isParentSwitch(mxCell cell) {
		mxICell parent = getRoot(cell.getParent());
		if (parent == null || parent.getStyle() == null) {
			return false;
		}
		return parent.getStyle().equalsIgnoreCase(STYLE_SWITCH)
				|| parent.getStyle().equalsIgnoreCase(STYLE_SWITCH_RUNNING)
				|| parent.getStyle().equalsIgnoreCase(STYLE_SWITCH_EXECUTED);
	}

	private mxICell getRoot(mxICell cell) {
		if (cell == null || cell.getGeometry() == null) {
			return null;
		}
		mxICell parent = getRoot(cell.getParent());
		if (parent != null) {
			return parent;
		}
		return cell;
	}

}
