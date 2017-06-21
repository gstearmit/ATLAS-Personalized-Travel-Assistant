package eu.domainobjects.presentation.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Label;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.xml.bind.JAXBException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.eventbus.Subscribe;

import eu.domainobjects.controller.MainController;
import eu.domainobjects.controller.events.StepEvent;
import eu.domainobjects.presentation.main.events.SelectedAbstractActivityEvent;
import eu.domainobjects.presentation.main.process.ProcessModelPanel;
import eu.domainobjects.utils.ResourceLoader;
import eu.fbk.das.process.engine.api.AdaptationProblem;
import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.domain.AbstractActivity;
import eu.fbk.das.process.engine.api.domain.ObjectDiagram;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;
import eu.fbk.das.process.engine.api.domain.ServiceDiagram;
import eu.fbk.das.process.engine.api.jaxb.ClauseType.Point;
import eu.fbk.das.process.engine.api.jaxb.ClauseType.Point.DomainProperty;
import eu.fbk.das.process.engine.api.jaxb.GoalType;

public class ActivityWindow extends JPanel {

	private static final long serialVersionUID = -3065754932068313302L;

	private static final Logger logger = LogManager
			.getLogger(ActivityWindow.class);

	// private static JFrame frame;
	private MainController controller;
	private JFrame frame;

	ProcessDiagram current;

	private JPanel activityPanel;
	private JPanel problemPanel;
	private JPanel domainPanel;
	private JPanel resultPanel;
	private JPanel processPanel;
	private ProcessModelPanel graphActivityPanel;
	private ProcessModelPanel graphProcessPanel;

	private JLabel goalLabel;
	private Label goalProblemLabel;
	private Label activityLabel;
	private Label smvLabel;
	private Label dotLabel;
	private JTextArea logTextArea;

	private static final String BACKGROUD_COLOR = "#E0E0E0";

	/**
	 * Launch the application.
	 */
	public ActivityWindow(JFrame frame) throws JAXBException,
			URISyntaxException, IOException {
		setOpaque(true);
		setLayout(null);

		this.frame = frame;

		JPanel mainPanel = new JPanel();
		mainPanel.setLocation(0, 0);
		mainPanel.setSize(1000, 800);
		mainPanel.setLayout(null);
		mainPanel.setBackground(Color.decode(BACKGROUD_COLOR));
		add(mainPanel);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		// an example how to get current process
		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				// ChangeEvent consumed. Do nothing.
			}

		});
		tabbedPane.setBounds(0, 0, 950, 510);
		tabbedPane.setBackground(Color.decode(BACKGROUD_COLOR));

		mainPanel.add(tabbedPane);

		activityPanel = new JPanel();
		activityPanel.setBackground(Color.decode(BACKGROUD_COLOR));
		tabbedPane.addTab("Process/Fragment Activity", null, activityPanel,
				null);

		// create panel to show the activity to refine or adapt

		problemPanel = new JPanel();
		problemPanel.setLayout(null);
		problemPanel.setBackground(Color.decode(BACKGROUD_COLOR));

		problemPanel.setPreferredSize(new Dimension(400, 600));
		JScrollPane scrollFrame = new JScrollPane(problemPanel);
		problemPanel.setAutoscrolls(true);
		scrollFrame.setPreferredSize(new Dimension(600, 200));
		scrollFrame.setBackground(Color.decode(BACKGROUD_COLOR));

		tabbedPane.addTab("Activity Specialization Problem", null, scrollFrame,
				null);

		domainPanel = new JPanel();
		domainPanel.setBackground(Color.decode(BACKGROUD_COLOR));
		tabbedPane.addTab("Planning Domain", null, domainPanel, null);

		resultPanel = new JPanel();
		resultPanel.setBackground(Color.decode(BACKGROUD_COLOR));
		tabbedPane.addTab("Planning Result", null, resultPanel, null);

		processPanel = new JPanel();
		processPanel.setBackground(Color.decode(BACKGROUD_COLOR));
		JScrollPane scrollPane = new JScrollPane(processPanel);
		scrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setBackground(Color.decode(BACKGROUD_COLOR));
		tabbedPane.addTab("Activity Specialization Result", null, scrollPane,
				null);

		JLabel lblLog = new JLabel("Execution Log");
		lblLog.setBounds(10, 505, 223, 22);
		lblLog.setFont(new Font("Serif", Font.BOLD, 17));
		mainPanel.add(lblLog);

		// log inside a scrollpane
		logTextArea = new JTextArea("");
		logTextArea.setEditable(false);
		JScrollPane scrollPaneLog = new JScrollPane(logTextArea);
		scrollPaneLog.setBounds(10, 530, 940, 100);
		mainPanel.add(scrollPaneLog);

	}

	@Subscribe
	public void onStep(StepEvent e) {
		try {
			ProcessDiagram pd = controller.getCurrentProcess();
			controller.getProcessEngineFacade().getCompleteProcessDiagram(pd);
			this.addLog("ProcessEngine step completed");
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}

	public void setController(MainController controller) {
		this.controller = controller;
		controller.register(this);

		// ACTIVITY PANEL
		activityPanel.setLayout(null);
		activityLabel = new Label("Abstract Activity Goal");
		activityLabel.setBounds(24, 24, 550, 22);
		activityPanel.add(activityLabel);

		goalLabel = new JLabel("Abstract Activity Goal");
		goalLabel.setBounds(24, 45, 500, 50);
		goalLabel.setFont(new Font("Serif", Font.PLAIN, 17));
		activityPanel.add(goalLabel);

		graphActivityPanel = new ProcessModelPanel(
				controller.getProcessEngineFacade());
		graphActivityPanel.setBounds(24, 75, 200, 90);

		// PROBLEM PANEL

		goalProblemLabel = new Label("Abstract Activity Goal");
		goalProblemLabel.setBounds(24, 45, 500, 22);
		goalProblemLabel.setFont(new Font("Serif", Font.PLAIN, 17));

		// PLANNING PANEL
		smvLabel = new Label("SMV File");
		smvLabel.setBounds(24, 65, 50, 50);
		smvLabel.setFont(new Font("Serif", Font.BOLD, 17));

		// PLANNING RESULT PANEL
		dotLabel = new Label("DOT File");
		dotLabel.setBounds(24, 65, 50, 50);
		dotLabel.setFont(new Font("Serif", Font.BOLD, 17));

		// panel to show the adaptation (process) result in the ResultView
		graphProcessPanel = new ProcessModelPanel(
				controller.getProcessEngineFacade());
		graphProcessPanel.setBounds(60, 60, 800, 150);

	}

	@Subscribe
	public void HandleSelectedAbstractActivity(SelectedAbstractActivityEvent sa) {
		System.out.println("Activity Window");
		String label = sa.getLabel();
		if (label == null || label.contains("scope")) {

		}

		ProcessDiagram process = controller.getCurrentProcess();
		if (process == null) {
			return;
		}

		// reference to the do instance
		DomainObjectInstance doi = controller.getProcessEngineFacade()
				.getDomainObjectInstanceForProcess(process);
		if (doi == null) {
			return;
		}

		// add content the activity view
		showActivity(label, process, doi);
		// add content in the adaptation problem view
		showAdaptationProblem(label, process, doi);
		// add the smv file at the DomainView
		showSMVFile(label, process);
		// add dot file at the ResultView
		showDOTFile(label, process);

		// add adaptation process to ProcessView
		showProcessResult(label, process);

		frame.setVisible(true);
		refreshWindow();

	}

	public void refreshWindow() {
		frame.getContentPane().validate();
		frame.getContentPane().repaint();
	}

	public String retrieveGoalString(GoalType goal) {
		// String goalString = "<html>";
		String goalString = "";
		if (goal.getPoint().size() > 1) {
			// MULTIPLE POINT (OR of AND)
			for (int j = 0; j < goal.getPoint().size(); j++) {
				Point currentPoint = goal.getPoint().get(j);
				if (currentPoint.getDomainProperty().size() > 1) {
					// AND among different DP
					for (int k = 0; k < currentPoint.getDomainProperty().size(); k++) {
						DomainProperty currentDP = currentPoint
								.getDomainProperty().get(k);
						String dpName = currentDP.getDpName();
						String dpState = currentDP.getState().get(0);
						String first = dpName.concat(" = ");
						String second = first.concat(dpState);
						if (k == currentPoint.getDomainProperty().size() - 1) {
							// last DP
							goalString = goalString.concat(second);
						} else {
							// not last add AND
							String third = second.concat(" <b>AND</b> ");
							goalString = goalString.concat(third);
							goalString = goalString.concat("\n");

						}

					}
					// goalString = goalString.concat("</html>");

				} else {
					// single clause
					DomainProperty dp = currentPoint.getDomainProperty().get(0);
					String dpName = dp.getDpName();
					String dpState = dp.getState().get(0);
					String first = dpName.concat(" = ");
					String second = first.concat(dpState);
					goalString = goalString.concat(second);
					// goalString = goalString.concat("</html>");

				}
				if (j != goal.getPoint().size() - 1) {
					goalString = goalString.concat("\n\n");
					goalString = goalString.concat("OR");
					goalString = goalString.concat("\n\n");
				}

			}

		} else {
			// single POINT
			Point currentPoint = goal.getPoint().get(0);
			if (currentPoint.getDomainProperty().size() > 1) {
				// AND among different DP
				for (int j = 0; j < currentPoint.getDomainProperty().size(); j++) {
					DomainProperty currentDP = currentPoint.getDomainProperty()
							.get(j);
					String dpName = currentDP.getDpName();
					String dpState = currentDP.getState().get(0);
					String first = dpName.concat(" = ");
					String second = first.concat(dpState);
					if (j == currentPoint.getDomainProperty().size() - 1) {
						// last DP
						goalString = goalString.concat(second);
					} else {
						// not last add AND
						String third = second.concat(" AND ");
						goalString = goalString.concat(third);
						goalString = goalString.concat("\n");

					}

				}
				// goalString = goalString.concat("</html>");
			} else {
				// single clause
				DomainProperty dp = currentPoint.getDomainProperty().get(0);
				String dpName = dp.getDpName();
				String dpState = dp.getState().get(0);
				String first = dpName.concat(" = ");
				String second = first.concat(dpState);

				goalString = goalString.concat(second);
				// goalString = goalString.concat("</html>");
			}

		}
		return goalString;

	}

	// method to visualize the activity to refine or to adapt
	public void showActivity(String label, ProcessDiagram process,
			DomainObjectInstance doi) {

		// create and show the activity
		ProcessDiagram newProc = new ProcessDiagram();
		ProcessActivity activity = new ProcessActivity();
		activity.setAbstract(true);
		activity.setName(label);
		newProc.addActivity(activity);
		graphActivityPanel.clear();
		graphActivityPanel.updateProcess(newProc);
		activityPanel.add(graphActivityPanel);

		// retrieve the goal of the specific activity
		boolean exist = false;
		String goalString = "";
		for (int i = 0; i < process.getActivities().size(); i++) {
			ProcessActivity current = process.getActivities().get(i);
			if (current.getName() == label) {
				exist = true;
				GoalType goal = ((AbstractActivity) current).getGoal();

				goalString = retrieveGoalString(goal);
				break;

			}

		}

		if (!exist) {
			// activity not in the main process, should be in a refinement
			Map<String, ProcessDiagram> currentRefinements = controller
					.getProcessEngineFacade().getRefinements(process);
			for (Map.Entry<String, ProcessDiagram> entry : currentRefinements
					.entrySet()) {
				// String key = entry.getKey();
				ProcessDiagram proc = entry.getValue();
				for (int i = 0; i < proc.getActivities().size(); i++) {
					ProcessActivity currentActivity = proc.getActivities().get(
							i);
					if (currentActivity.getName() == label) {
						GoalType goal = ((AbstractActivity) currentActivity)
								.getGoal();
						goalString = retrieveGoalString(goal);
						break;
					}
				}

			}

		}

		// update goal string
		// goalLabel.setText(convertToMultiline(goalString));
		goalLabel.setText(goalString);

	}

	public void addFragmentsAndProperties(AdaptationProblem problem,
			ProcessDiagram process) {

		HashMap<String, ArrayList<String>> fragmentsOfCells = new HashMap<String, ArrayList<String>>();
		HashMap<String, ArrayList<String>> propertiesOfCells = new HashMap<String, ArrayList<String>>();

		Map<String, List<String>> fragments = problem.getRelevantServices();

		for (int j = 0; j < problem.getDomainObjectInstances().size(); j++) {
			DomainObjectInstance currentDoi = problem
					.getDomainObjectInstances().get(j);
			Iterator<Entry<String, List<String>>> itf = fragments.entrySet()
					.iterator();
			ArrayList<String> finalFragments = new ArrayList<String>();
			while (itf.hasNext()) {
				String currentFragment = itf.next().getKey();
				for (int k = 0; k < currentDoi.getFragments().size(); k++) {
					ServiceDiagram fr = currentDoi.getFragments().get(k);
					if (fr.getSid().equals(currentFragment)
							&& isCorrelated(currentDoi, process)) {
						// add fragment to the respective cell
						finalFragments.add(currentFragment);
						fragmentsOfCells
								.put(currentDoi.getId(), finalFragments);
					}
				}
			}
		}

		Iterator<String> iter = fragmentsOfCells.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			for (int j = 0; j < problem.getDomainObjectInstances().size(); j++) {
				DomainObjectInstance currentDoi = problem
						.getDomainObjectInstances().get(j);
				if (currentDoi != null && currentDoi.getId().equals(key)
						&& currentDoi.getExternalKnowledge() != null) {
					for (ObjectDiagram ek : currentDoi.getExternalKnowledge()) {
						ArrayList<String> values = new ArrayList<String>();
						values.add(ek.getCurrentState());
						propertiesOfCells.put(ek.getOid(), values);
					}
				}
			}

		}

		JTable tableFragments = new JTable(
				toTableModelFragments(fragmentsOfCells));

		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

			Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);

			@Override
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected,
						hasFocus, row, column);
				setBorder(BorderFactory.createCompoundBorder(getBorder(),
						padding));
				return this;
			}
		};
		tableFragments.setDefaultRenderer(Object.class, renderer);

		JPanel fragmentListPanel = new JPanel();
		fragmentListPanel.setBounds(24, 155, 500, 180);

		fragmentListPanel.setLayout(new BorderLayout());
		tableFragments.getTableHeader().setBorder(
				new LineBorder(new Color(0, 0, 0)));
		tableFragments.getTableHeader().setFont(
				new Font(tableFragments.getTableHeader().getFont().getName(),
						Font.BOLD, tableFragments.getTableHeader().getFont()
								.getSize()));
		fragmentListPanel.add(tableFragments.getTableHeader(),
				BorderLayout.PAGE_START);
		tableFragments.setRowHeight(30);
		fragmentListPanel.add(tableFragments, BorderLayout.CENTER);

		tableFragments.getColumnModel().getColumn(0).setPreferredWidth(80);
		tableFragments.getColumnModel().getColumn(1).setPreferredWidth(130);
		tableFragments.setBorder(new LineBorder(new Color(0, 0, 0)));

		JTable tableContext = new JTable(
				toTableModelProperties(propertiesOfCells));
		tableContext.setDefaultRenderer(Object.class, renderer);
		tableContext.setRowHeight(30);

		tableContext.setBorder(new LineBorder(new Color(0, 0, 0)));
		tableContext.getTableHeader().setBorder(
				new LineBorder(new Color(0, 0, 0)));
		tableContext.getTableHeader().setFont(
				new Font(tableFragments.getTableHeader().getFont().getName(),
						Font.BOLD, tableFragments.getTableHeader().getFont()
								.getSize()));

		JPanel contextListPanel = new JPanel();
		contextListPanel.setBounds(24, 400, 500, 180);

		contextListPanel.setLayout(new BorderLayout());
		contextListPanel.add(tableContext.getTableHeader(),
				BorderLayout.PAGE_START);
		contextListPanel.add(tableContext, BorderLayout.CENTER);

		tableContext.getColumnModel().getColumn(0).setPreferredWidth(80);
		tableContext.getColumnModel().getColumn(1).setPreferredWidth(130);
		problemPanel.add(contextListPanel);
		problemPanel.add(fragmentListPanel);

	}

	private boolean isCorrelated(DomainObjectInstance currentDoi,
			ProcessDiagram process) {
		DomainObjectInstance doi = controller.getProcessEngineFacade()
				.getDomainObjectInstanceForProcess(process);
		if (doi != null) {
			List<DomainObjectInstance> corrs = controller
					.getProcessEngineFacade().getCorrelations(doi);
			if (corrs != null) {
				Optional<DomainObjectInstance> t = corrs.stream()
						.filter(c -> c.getId().equals(currentDoi.getId()))
						.findFirst();
				if (t.isPresent()) {
					return true;
				}
			}
		}
		return false;
	}

	public void showAdaptationProblem(String label, ProcessDiagram process,
			DomainObjectInstance doi) {

		problemPanel.removeAll();
		Label goalLabel = new Label("Abstact Activity Goal");
		goalLabel.setBounds(24, 24, 550, 22);
		problemPanel.add(goalLabel);

		Label fragmentsLabel = new Label("Fragments");
		fragmentsLabel.setBounds(24, 125, 550, 22);
		problemPanel.add(fragmentsLabel);

		Label contextLabel = new Label("External Knowledge");
		contextLabel.setBounds(24, 370, 1000, 22);
		problemPanel.add(contextLabel);

		// add Grounded goal
		String goalString = "";
		boolean exist = false;
		for (int i = 0; i < process.getActivities().size(); i++) {
			ProcessActivity current = process.getActivities().get(i);
			if (current.getName() == label) {
				GoalType goal = ((AbstractActivity) current).getGoal();
				goalString = retrieveGoalString(goal);
				exist = true;
				break;
			}
		}
		if (!exist) {
			// activity not in the main process, should be in a refinement
			Map<String, ProcessDiagram> currentRefinements = controller
					.getProcessEngineFacade().getRefinements(process);
			for (Map.Entry<String, ProcessDiagram> entry : currentRefinements
					.entrySet()) {
				// String key = entry.getKey();
				ProcessDiagram proc = entry.getValue();
				for (int i = 0; i < proc.getActivities().size(); i++) {
					ProcessActivity currentActivity = proc.getActivities().get(
							i);
					if (currentActivity.getName() == label) {
						GoalType goal = ((AbstractActivity) currentActivity)
								.getGoal();
						goalString = retrieveGoalString(goal);
						break;
					}
				}

			}

		}
		// show goal string

		// goalProblemLabel.setText(convertToMultiline(goalString));
		goalProblemLabel.setText(goalString);
		problemPanel.add(goalProblemLabel);

		// add fragments and domain properties to the problem viewer
		exist = false;
		for (int i = 0; i < process.getActivities().size(); i++) {
			ProcessActivity current = process.getActivities().get(i);
			if (current.getName() == label) {

				exist = true;
				AdaptationProblem problem = ((AbstractActivity) current)
						.getProblem();
				addFragmentsAndProperties(problem, process);
				break;
			}

		}
		if (!exist) {
			// activity not in the main process, should be in a refinement
			Map<String, ProcessDiagram> currentRefinements = controller
					.getProcessEngineFacade().getRefinements(process);
			for (Map.Entry<String, ProcessDiagram> entry : currentRefinements
					.entrySet()) {
				// String key = entry.getKey();
				ProcessDiagram proc = entry.getValue();
				for (int i = 0; i < proc.getActivities().size(); i++) {
					ProcessActivity currentActivity = proc.getActivities().get(
							i);
					if (currentActivity.getName() == label) {
						AdaptationProblem problem = ((AbstractActivity) currentActivity)
								.getProblem();
						addFragmentsAndProperties(problem, proc);
						break;

					}
				}

			}

		}

	}

	public void showSMVFile(String label, ProcessDiagram process) {

		domainPanel.removeAll();
		String apid = null;
		for (int i = 0; i < process.getActivities().size(); i++) {
			ProcessActivity current = process.getActivities().get(i);
			if (current.getName() == label) {
				AdaptationProblem problem = ((AbstractActivity) current)
						.getProblem();
				apid = problem.getProblemId();
				break;
			}
		}
		if (apid == null) {
			// activity not in the main process, should be in a refinement
			Map<String, ProcessDiagram> currentRefinements = controller
					.getProcessEngineFacade().getRefinements(process);
			for (Map.Entry<String, ProcessDiagram> entry : currentRefinements
					.entrySet()) {
				// String key = entry.getKey();
				ProcessDiagram proc = entry.getValue();
				for (int i = 0; i < proc.getActivities().size(); i++) {
					ProcessActivity currentActivity = proc.getActivities().get(
							i);
					if (currentActivity.getName() == label) {
						AdaptationProblem problem = ((AbstractActivity) currentActivity)
								.getProblem();
						apid = problem.getProblemId();
						break;

					}
				}

			}

		}

		if (apid == null) {
			logger.warn("Adaptation problem id is null");
			return;
		}

		// read the smv file content

		String srcPath = ResourceLoader.getStoryboardFile().getParentFile()
				.getPath();
		String path = srcPath.concat("/Compositions/" + apid + "/" + apid
				+ ".smv");

		String output = null;
		try {
			output = new Scanner(new File(path)).useDelimiter("\\Z").next();
			System.out.println("" + output);
		} catch (Exception e) {
			logger.error("Error on readind SMV file ", e);
		}

		JTextArea codeArea = new JTextArea(50, 150);

		codeArea.setText(output);
		codeArea.setEditable(false);
		codeArea.setFont(Font.getFont(Font.SANS_SERIF));

		JScrollPane sp = new JScrollPane(codeArea);
		sp.setPreferredSize(new Dimension(600, 400));
		sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		domainPanel.add(sp);
		domainPanel.add(smvLabel);

	}

	public void showDOTFile(String label, ProcessDiagram process) {
		resultPanel.removeAll();
		String apid = null;
		for (int i = 0; i < process.getActivities().size(); i++) {
			ProcessActivity current = process.getActivities().get(i);
			if (current.getName() == label) {
				AdaptationProblem problem = ((AbstractActivity) current)
						.getProblem();
				apid = problem.getProblemId();
				break;

			}
		}
		if (apid == null) {
			Map<String, ProcessDiagram> currentRefinements = controller
					.getProcessEngineFacade().getRefinements(process);
			for (Map.Entry<String, ProcessDiagram> entry : currentRefinements
					.entrySet()) {
				String key = entry.getKey();
				ProcessDiagram proc = entry.getValue();
				for (int i = 0; i < proc.getActivities().size(); i++) {
					ProcessActivity currentActivity = proc.getActivities().get(
							i);
					if (currentActivity.getName() == label) {
						AdaptationProblem problem = ((AbstractActivity) currentActivity)
								.getProblem();
						apid = problem.getProblemId();
						break;
					}
				}

			}
		}

		String srcPath = ResourceLoader.getStoryboardFile().getParentFile()
				.getPath();
		String path = srcPath.concat("/Compositions/" + apid + "/" + apid
				+ ".dot");
		String output = null;

		try {
			output = new Scanner(new File(path)).useDelimiter("\\Z").next();

		} catch (Exception e) {
			logger.error("Error on readind SMV file ", e);
		}

		JTextArea codeArea = new JTextArea(50, 150);
		codeArea.setText(output);
		codeArea.setEditable(false);
		codeArea.setFont(Font.getFont(Font.SANS_SERIF));

		JScrollPane sp = new JScrollPane(codeArea);
		sp.setPreferredSize(new Dimension(600, 400));
		sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		resultPanel.add(sp);
		resultPanel.add(dotLabel);

	}

	public void showProcessResult(String label, ProcessDiagram process) {

		// retrieve adaptation result process

		boolean exist = false;
		for (int i = 0; i < process.getActivities().size(); i++) {
			ProcessActivity current = process.getActivities().get(i);
			if (current.getName() == label) {
				exist = true;
				try {
					Map<String, ProcessDiagram> currentRefinements = controller
							.getProcessEngineFacade().getRefinements(process);
					for (Map.Entry<String, ProcessDiagram> entry : currentRefinements
							.entrySet()) {
						String key = entry.getKey();
						ProcessDiagram proc = entry.getValue();
						if (key.contains(current.getName())) {

							// here I can retried the Adaptation Result as
							// process diagram and
							// show it in the viewer
							graphProcessPanel.clear();
							graphProcessPanel.updateProcess(proc);
							processPanel.add(graphProcessPanel);
						}

					}

				} catch (Exception e) {
					logger.error("Error on reading adaptation result ", e);
				}
				break;
			}
		}
		if (!exist) {
			try {
				Map<String, ProcessDiagram> currentRefinements = controller
						.getProcessEngineFacade().getRefinements(process);
				for (Map.Entry<String, ProcessDiagram> entry : currentRefinements
						.entrySet()) {
					ProcessDiagram currentProcess = entry.getValue();
					for (int i = 0; i < currentProcess.getActivities().size(); i++) {
						ProcessActivity current = currentProcess
								.getActivities().get(i);
						if (current.getName() == label) {
							exist = true;
							try {
								Map<String, ProcessDiagram> refinements = controller
										.getProcessEngineFacade()
										.getRefinements(process);
								for (Map.Entry<String, ProcessDiagram> ent : refinements
										.entrySet()) {
									String key = ent.getKey();
									ProcessDiagram proc = ent.getValue();
									if (key.contains(current.getName())) {

										// here I can retried the Adaptation
										// Result as
										// process diagram and
										// show it in the viewer
										graphProcessPanel.clear();
										graphProcessPanel.updateProcess(proc);
										processPanel.add(graphProcessPanel);
									}

								}

							} catch (Exception e) {
								logger.error(
										"Error on reading adaptation result ",
										e);
							}

							break;
						}
					}
				}
			} catch (Exception e) {
				logger.error("Error on reading adaptation result ", e);
			}

		}

	}

	public String convertToMultiline(String orig) {
		return orig.replaceAll("\n", "<br/>");
	}

	private static TableModel toTableModelFragments(Map map) {
		DefaultTableModel model = new DefaultTableModel(new Object[] {
				"Domain Objects", "Fragments" }, 0);
		for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			model.addRow(new Object[] { entry.getKey(), entry.getValue() });
		}
		return model;
	}

	public TableModel toTableModelProperties(Map map) {
		DefaultTableModel model = new DefaultTableModel(new Object[] {
				"Domain Property", "Current Status" }, 0);
		for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			model.addRow(new Object[] { entry.getKey(), entry.getValue() });
		}
		return model;
	}

	/**
	 * Add a line at the bottom of the log
	 * 
	 * @param line
	 */
	public void addLog(String line) {
		if (line == null) {
			logger.warn("addLog: line must be not null");
			return;
		}
		logTextArea.append(line + System.lineSeparator());
	}

}
