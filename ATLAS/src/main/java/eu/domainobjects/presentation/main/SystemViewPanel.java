package eu.domainobjects.presentation.main;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.google.common.collect.ArrayListMultimap;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;

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
	mxGraph graph = new mxGraph();
	Object parent = graph.getDefaultParent();
	mxGraphComponent graphComponent = new mxGraphComponent(graph);
	mxHierarchicalLayout layout;

	public SystemViewPanel(MainWindow mainWindow) {

		this.window = mainWindow;
		graph = new mxGraph();
		parent = graph.getDefaultParent();
		graphComponent = new mxGraphComponent(graph);
		layout = new mxHierarchicalLayout(graph, SwingConstants.NORTH);

		// add graphComponent to panel
		graphComponent.setBorder(null);
		graphComponent.setEnabled(false);
		// graphComponent.setAlignmentY(SwingConstants.BOTTOM);
		graphComponent.getViewport().setBackground(new Color(1, 1, 1, 0f));
		this.setBackground(Color.decode(BACKGROUD_COLOR));
		this.add(graphComponent);

		// test for scroll by dragging
		setAutoscrolls(true);

	}

	public void clear() {
		mxGraph current = graphComponent.getGraph();
		((mxGraphModel) current.getModel()).clear();
	}

	public void updateViewPanel(
			ArrayListMultimap<String, Map<String, List<String>>> softDependencies) {

		// define edge style
		Map<String, Object> style = graph.getStylesheet().getDefaultEdgeStyle();

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

		graph.getModel().beginUpdate();

		try {
			Map<String, Object> vertices = new HashMap<String, Object>();
			for (String key : softDependencies.keys()) {
				Object v = graph.insertVertex(parent, null, key, 1, 1, 80, 30,
						STYLE_VERTEX);
				graph.updateCellSize(v);
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

							Iterator<Map.Entry<String, Object>> prova = ((Map<String, Object>) vertices)
									.entrySet().iterator();
							while (prova.hasNext()) {
								Map.Entry<String, Object> pair = prova.next();
								Iterator<Map.Entry<String, Object>> prova1 = ((Map<String, Object>) vertices)
										.entrySet().iterator();

								while (prova1.hasNext()) {
									Map.Entry<String, Object> pair1 = prova1
											.next();
									if (pair.getKey().equals(key)
											&& pair1.getKey().equals(key1)) {
										Object edge = graph.insertEdge(parent,
												null, value.get(t),
												pair1.getValue(),
												pair.getValue(), null);

										((mxCell) edge).setStyle(style
												.toString());
										graph.updateCellSize((mxCell) edge);
									}
								}
							}

						}
					}

				}
			}

		} finally {
			graph.getModel().endUpdate();
		}

		double widthLayout = graphComponent.getLayoutAreaSize().getWidth();
		double heightLayout = graphComponent.getLayoutAreaSize().getHeight();
		graph.getModel().setGeometry(graph.getDefaultParent(),
				new mxGeometry(100, 100, widthLayout, heightLayout));

		// compactTree che funziona
		// mxCompactTreeLayout layout = new mxCompactTreeLayout(graph, false,
		// true);

		layout.setIntraCellSpacing(100);
		layout.setInterRankCellSpacing(100);
		layout.setInterHierarchySpacing(150);
		layout.execute(graph.getDefaultParent());

		graph.getModel().endUpdate();
		graph.refresh();

		window.refreshWindow();
	}
}
