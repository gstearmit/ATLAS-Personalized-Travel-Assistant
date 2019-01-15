package eu.domainobjects.presentation.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.google.common.collect.ArrayListMultimap;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;

import eu.fbk.das.process.engine.api.DomainObjectInstance;

public class SystemViewPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	// private static final String STYLE_EDGE =
	// "strokeColor=666666;strokeWidth=1;fontColor=000000;fontSize=14;horizontalAlign=center;verticalAlign=middle;dashed=true;dashPattern=2;fillColor=FFFFFF;rounded=true;edgeStyle=segment";
	// // edgeStyle=elbowEdgeStyle;
	private static final String STYLE_VERTEX = "shape=rectangle;strokeColor=666666;strokeWidth=2;fontColor=000000;spacingLeft=10;spacingRight=10;spacingTop=10;spacingBottom=10;fontSize=16;horizontalAlign=center;verticalAlign=middle;fillColor=FFFFFF;rounded=true";

	private static final String BACKGROUD_COLOR = "#E0E0E0";
	private static final String BORDER_COLOR = "#9E9E9E";
	// private static final Font LABEL_FONT = new Font("Verdana", Font.PLAIN,
	// 17);
	// private static final Font TAB_FONT = new Font("Verdana", Font.PLAIN, 18);

	private MainWindow window;

	private mxGraph designTimeGraph = new mxGraph();
	private Object designTimeParent = designTimeGraph.getDefaultParent();
	private mxGraphComponent designTimeGraphComponent = new mxGraphComponent(
			designTimeGraph);
	private mxHierarchicalLayout layout;
	private JPanel designTimePanel;

	private mxGraph runTimeGraph = new mxGraph();
	private Object runTimeParent = runTimeGraph.getDefaultParent();
	private mxGraphComponent runTimeGraphComponent = new mxGraphComponent(
			runTimeGraph);
	private mxHierarchicalLayout runtimeLayout;
	private JPanel runTimePanel;

	public SystemViewPanel(MainWindow mainWindow) {

		this.window = mainWindow;
		this.setBorder(new EmptyBorder(10, 10, 10, 10));
		this.setBackground(Color.decode(BACKGROUD_COLOR));
		BoxLayout mainLayout = new BoxLayout(this, BoxLayout.X_AXIS);
		this.setLayout(mainLayout);

		// scroll pane for the design time hierarchy panel
		designTimePanel = new JPanel();
		JScrollPane designTimeScrollPane = hierarchyPanel(designTimePanel);

		// scroll pane for the runtime hierarchy panel
		runTimePanel = new JPanel();
		JScrollPane runTimescrollPane = hierarchyPanel(runTimePanel);

		// design time graph
		designTimeGraph = new mxGraph();
		designTimeParent = designTimeGraph.getDefaultParent();
		designTimeGraphComponent = new mxGraphComponent(designTimeGraph);
		layout = new mxHierarchicalLayout(designTimeGraph, SwingConstants.NORTH);

		designTimeGraphComponent.setBorder(null);
		designTimeGraphComponent.setEnabled(false);
		// graphComponent.setAlignmentY(SwingConstants.BOTTOM);
		designTimeGraphComponent.getViewport().setBackground(
				new Color(1, 1, 1, 0f));
		// add graphComponent to panel
		designTimePanel.add(designTimeGraphComponent);

		// runtime graph
		runTimeGraph = new mxGraph();
		runTimeParent = runTimeGraph.getDefaultParent();
		runTimeGraphComponent = new mxGraphComponent(runTimeGraph);
		runtimeLayout = new mxHierarchicalLayout(runTimeGraph,
				SwingConstants.NORTH);

		runTimeGraphComponent.setBorder(null);
		runTimeGraphComponent.setEnabled(false);
		// graphComponent.setAlignmentY(SwingConstants.BOTTOM);
		runTimeGraphComponent.getViewport().setBackground(
				new Color(1, 1, 1, 0f));
		// add graphComponent to panel
		runTimePanel.add(runTimeGraphComponent);

		this.add(designTimeScrollPane);
		this.add(Box.createRigidArea(new Dimension(5, 0)));
		this.add(runTimescrollPane);

		// test for scroll by dragging
		setAutoscrolls(true);

	}

	/**
	 * @return
	 */
	private JScrollPane hierarchyPanel(JPanel panel) {
		panel.setLayout(new BorderLayout());
		panel.setBackground(Color.decode(BACKGROUD_COLOR));
		panel.setBorder(new LineBorder(Color.decode(BORDER_COLOR)));
		JScrollPane runTimescrollPane = new JScrollPane(panel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		runTimescrollPane.setPreferredSize(new Dimension(900, 700));
		return runTimescrollPane;
	}

	public void clear() {
		mxGraph current = designTimeGraphComponent.getGraph();
		((mxGraphModel) current.getModel()).clear();
	}

	public void updateDesignTimeViewPanel(
			ArrayListMultimap<String, Map<String, List<String>>> softDependencies) {

		// define edge style
		Map<String, Object> style = designTimeGraph.getStylesheet()
				.getDefaultEdgeStyle();
		setGraphStyle(style);
		designTimeGraph.getModel().beginUpdate();

		try {
			Map<String, Object> vertices = new HashMap<String, Object>();
			for (String key : softDependencies.keys()) {
				Object v = designTimeGraph.insertVertex(designTimeParent, null,
						key, 1, 1, 80, 30, STYLE_VERTEX);
				designTimeGraph.updateCellSize(v);
				vertices.put(key, v);
			}

			for (String key : softDependencies.keys()) {
				Iterator<Map.Entry<String, List<String>>> it = ((Map<String, List<String>>) softDependencies
						.get(key).get(0)).entrySet().iterator();
				String keyMap = null;
				while (it.hasNext()) {
					Map.Entry<String, List<String>> pair = it.next();
					keyMap = pair.getKey();
				}
				for (String key1 : softDependencies.keys()) {
					Iterator<Map.Entry<String, List<String>>> i = ((Map<String, List<String>>) softDependencies
							.get(key1).get(0)).entrySet().iterator();
					List<String> value = null;
					while (i.hasNext()) {
						Map.Entry<String, List<String>> pairs = i.next();
						value = pairs.getValue();
					}

					for (int t = 0; t < value.size(); t++) {
						if (value.get(t).equals(keyMap)) {

							// System.out.println(key + " -> " + value.get(t)
							// + " -> " + key1);

							Iterator<Map.Entry<String, Object>> verticesIterator = ((Map<String, Object>) vertices)
									.entrySet().iterator();
							while (verticesIterator.hasNext()) {
								Map.Entry<String, Object> pair = verticesIterator
										.next();
								Iterator<Map.Entry<String, Object>> prova1 = ((Map<String, Object>) vertices)
										.entrySet().iterator();

								while (prova1.hasNext()) {
									Map.Entry<String, Object> pair1 = prova1
											.next();
									if (pair.getKey().equals(key)
											&& pair1.getKey().equals(key1)) {
										Object edge = designTimeGraph
												.insertEdge(designTimeParent,
														null, value.get(t),
														pair1.getValue(),
														pair.getValue(), null);

										((mxCell) edge).setStyle(style
												.toString());
										designTimeGraph
												.updateCellSize((mxCell) edge);
									}
								}
							}

						}
					}

				}
			}

		} finally {
			designTimeGraph.getModel().endUpdate();
		}

		double widthLayout = designTimeGraphComponent.getLayoutAreaSize()
				.getWidth();
		double heightLayout = designTimeGraphComponent.getLayoutAreaSize()
				.getHeight();
		designTimeGraph.getModel().setGeometry(
				designTimeGraph.getDefaultParent(),
				new mxGeometry(100, 100, widthLayout, heightLayout));

		// compactTree che funziona
		// mxCompactTreeLayout layout = new mxCompactTreeLayout(graph, false,
		// true);

		layout.setIntraCellSpacing(100);
		layout.setInterRankCellSpacing(100);
		layout.setInterHierarchySpacing(150);
		layout.execute(designTimeGraph.getDefaultParent());

		designTimeGraph.getModel().endUpdate();
		designTimeGraph.refresh();

		window.refreshWindow();
	}

	/**
	 * @param style
	 */
	private void setGraphStyle(Map<String, Object> style) {
		style.put(mxConstants.STYLE_STARTARROW, mxConstants.ARROW_CLASSIC);
		style.put(mxConstants.STYLE_ENDARROW, mxConstants.NONE);
		style.put(mxConstants.STYLE_STROKECOLOR, "#666666");
		style.put(mxConstants.STYLE_FONTCOLOR, "#000000");
		style.put(mxConstants.STYLE_STROKEWIDTH, "1");
		style.put(mxConstants.STYLE_FONTSIZE, "16");
		style.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE);
		style.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_LEFT);
		style.put(mxConstants.STYLE_ROUNDED, true);
		style.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ENTITY_RELATION);
		style.put(mxConstants.STYLE_DASHED, "true");
		style.put(mxConstants.STYLE_DASH_PATTERN, "10");
		style.put(mxConstants.STYLE_AUTOSIZE, "true");
		style.put(mxConstants.STYLE_STROKEWIDTH, "1");
	}

	public void updateRunTimeViewPanel(
			ArrayListMultimap<DomainObjectInstance, DomainObjectInstance> strongDependencies) {
		// define edge style
		Map<String, Object> style = runTimeGraph.getStylesheet()
				.getDefaultEdgeStyle();
		setGraphStyle(style);
		runTimeGraph.getModel().beginUpdate();

		// CONSTRUCT THE GRAPH

		runtimeLayout.setIntraCellSpacing(100);
		runtimeLayout.setInterRankCellSpacing(100);
		runtimeLayout.setInterHierarchySpacing(150);
		runtimeLayout.execute(runTimeGraph.getDefaultParent());

		runTimeGraph.getModel().endUpdate();
		runTimeGraph.refresh();

		window.refreshWindow();
	}
}
