package eu.domainobjects.presentation.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window.Type;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.xml.bind.JAXBException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.domainobjects.DemonstratorConstant;
import eu.domainobjects.controller.MainController;
import eu.domainobjects.presentation.main.action.SelectedEntitiesButtonListener;
import eu.domainobjects.presentation.main.action.listener.CorrelateEntitiesListener;
import eu.domainobjects.presentation.main.action.listener.EntityDetailActionListener;
import eu.domainobjects.presentation.main.action.listener.EntityTableSelectionListener;
import eu.domainobjects.presentation.main.action.listener.MenuExitListener;
import eu.domainobjects.presentation.main.action.listener.OpenScenarioListener;
import eu.domainobjects.presentation.main.action.listener.SelectInstanceListener;
import eu.domainobjects.presentation.main.action.listener.StepButtonActionListener;
import eu.domainobjects.presentation.main.process.ProcessModelPanel;
import eu.domainobjects.utils.DoiBean;
import eu.domainobjects.utils.UserData;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;

public class MainWindow {

	private static final Logger logger = LogManager.getLogger(MainWindow.class);

	// interface colors
	private static final String BACKGROUD_COLOR = "#E0E0E0";
	private static final String BORDER_COLOR = "#9E9E9E";
	// main frame
	public JFrame frame;

	// other windows
	private JFrame newEntityFrame;
	private AddNewEntityWindow addNewEntityWindow;

	// main window components
	private JTable generalTable;
	private JScrollPane entitiesScrollPane;
	private Container mainPanel;
	private JLabel lblDomainObjectsDetails;
	private JLabel lblProvidedFragments;
	private JList<String> providedFragmentsList;
	private JScrollPane mainScrollPane;
	private JLabel lblCellInstances;
	private JList<String> cellInstancesList;
	private ProcessModelPanel processModelPanel;
	private JLabel lblProcessModel;
	private JLabel lblProcessExecution;
	private ProcessModelPanel processExecutionPanel;
	private JLabel lblCorrelatedEntities;
	private JList<String> correlatedEntitiesList;
	private JMenu mnHelp;
	private JMenuItem mntmAbout;

	// window builder
	// private MapViewerComponentBuilder mvcb = new MapViewerComponentBuilder();

	// private JScrollPane processExecutionScrollPane;
	private MainController controller;
	private JToolBar toolbar;
	private JButton btnNextEntity;
	private JButton btnPreviousEntity;
	private JButton btnStep;
	private JButton btnPlaypause;
	//private JButton btnAdd;
	private JList<String> stateVariablesList;
	private ActivityWindow refinementView;
	private JFrame refinementFrame;
	private JFrame PSFrame;
	private JScrollPane entityStateScrollPane;
	private JList<String> entityKnowledgeList;
	private JScrollPane entityKnowledgeScrollPane;
	private JComboBox<String> comboEntities;
	private SelectJourneyAlternativeWindow selectAlternativeWindow;
	private JFrame selectAlternativeFrame;
	private JTextArea logTextArea;
	private AboutDialog abtDialog;
	private JMenu mnEdit;
	private PreferencesDialog preferencesDialog;
	private JList<String> monitorList;
	private Icon playIcon;
	private ImageIcon pauseIcon;
	private ImageIcon addIcon;
	private Vector<String> columnNames;
	private JTabbedPane tabEntity;
	private JPanel middlePanel;
	private JPanel bottomPanel;
	private JPanel procModel;
	private JPanel procExec;
	private GridBagConstraints cMiddle;
	private JScrollPane processModelScrollPane;
	private JScrollPane processExecutionScrollPane;
	private Font lblFont;
	private JPanel modelPanel;

	/**
	 * Create the application.
	 */
	public MainWindow() {
		try {
			initialize();
		} catch (IOException e) {
			logger.error("Error in initialization", e);
		}
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws IOException
	 */
	private void initialize() throws IOException {
		lblFont = new Font("Verdana", Font.PLAIN, 17);
		Font tabFont = new Font("Verdana", Font.PLAIN, 18);
		frame = new JFrame("ATLAS");

		// Panel for the main TabbedPane
		JPanel framePanel = new JPanel();
		framePanel.setVisible(true);
		framePanel.setLayout(new GridBagLayout());
		framePanel.setBackground(Color.decode(BACKGROUD_COLOR));

		// Constraints for the frameTabbedPane on the main window
		GridBagConstraints cFrameTabbed = getGridBagConstraints(
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, 1, 1, 0, 0,
				5, 5, 5, 5);

		// main tabbedPane
		JTabbedPane frameTabbedPane = new JTabbedPane(JTabbedPane.TOP);

		framePanel.add(frameTabbedPane, cFrameTabbed);

		// Panel for the Runtime Execution Tab
		mainPanel = new JPanel();
		mainPanel.setVisible(true);
		// the mainPanel has a GridBagLayout organized in one column and 3 rows
		mainPanel.setLayout(new GridBagLayout());
		mainPanel.setBackground(Color.decode(BACKGROUD_COLOR));
		// by setting preferredSize for the main panel, the vertical scrollbar
		// will appear if needed
		// mainPanel.setPreferredSize(new Dimension(1024, 900));

		// Panel for the Domain Objects Models Tab
		modelPanel = new DomainObjectsModelsPanel(this);

		frameTabbedPane.setFont(tabFont);
		frameTabbedPane.addTab("Domain Objects Models", null, modelPanel, null);
		frameTabbedPane.addTab("Runtime Execution", null, mainPanel, null);
		frameTabbedPane.setBackground(Color.decode(BACKGROUD_COLOR));
		// frameTabbedPane.setBorder(new
		// LineBorder(Color.decode(BORDER_COLOR)));

		frameTabbedPane.setUI(new SpacedTabbedPaneUI());

		mainScrollPane = new JScrollPane(framePanel);

		// adding the mainPanel on a scrollPane
		mainScrollPane.getVerticalScrollBar().setUnitIncrement(20);
		mainScrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		mainScrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		frame.getContentPane().add(mainScrollPane);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// change here for final frame size
		frame.setSize(1024, 768);
		// frame.setSize(1200, 800);
		frame.setResizable(true);
		frame.setLocationRelativeTo(null); // *** this will center your app
		frame.setIconImage(ImageIO.read(getClass().getResource(
				"/images/logo_32x32.png")));

		/****************** Runtime Execution Tab - TOP PANEL **************************************/
		// Constraints for the top panel on the first row of the mainPanel
		// GridBagConstraints cTop = new GridBagConstraints();
		// cTop.anchor = GridBagConstraints.LINE_END;
		// cTop.weightx = 0;
		// cTop.weighty = 0;
		// cTop.gridx = 0; // column 0
		// cTop.gridy = 0; // row 0
		// cTop.insets.top = 15; // external 'top' padding
		// cTop.insets.bottom = 15;
		// cTop.insets.right = 30;
		//
		// JPanel topPanel = new JPanel();
		// topPanel.setLayout(new FlowLayout());
		// topPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		// topPanel.setBackground(Color.decode(BACKGROUD_COLOR));
		//
		// // label and comboBox for the domain objects models selection
		// JLabel lblComboEntities = new JLabel("Domain Objects Models");
		// lblComboEntities.setFont(lblFont);
		// topPanel.add(lblComboEntities);
		//
		// comboEntities = new JComboBox<String>();
		// comboEntities.setVisible(true);
		// comboEntities.setLightWeightPopupEnabled(false);
		// comboEntities.setBorder(new LineBorder(Color.decode(BORDER_COLOR)));
		// comboEntities.addActionListener(new
		// SelectedComboEntityListener(this));
		// topPanel.add(comboEntities);
		//
		// // adding the topPanel on the mainPanel
		// mainPanel.add(topPanel, cTop);

		/***************************************** Runtime Execution Tab - MIDDLE PANEL ***********************************/
		// Constraints for the middle panel on the second row of the mainPanel
		// GridBagLayout
		cMiddle = getGridBagConstraints(GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, 0.3, 0.3, 0, 2, 15, 15, 30, 30);

		middlePanel = new JPanel();
		BoxLayout middlePanelLayout = new BoxLayout(middlePanel,
				BoxLayout.LINE_AXIS);
		middlePanel.setLayout(middlePanelLayout);
		middlePanel.setBackground(Color.decode(BACKGROUD_COLOR));

		// domain objects instances
		lblCellInstances = new JLabel("Domain Object Instances");
		lblCellInstances.setFont(lblFont);

		cellInstancesList = new JList<String>();
		cellInstancesList.setPreferredSize(new Dimension(250, 200));
		cellInstancesList.setMaximumSize(new Dimension(250, 200));
		cellInstancesList.setMinimumSize(new Dimension(250, 200));
		cellInstancesList.addListSelectionListener(new SelectInstanceListener(
				this));
		cellInstancesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JPanel domainObjectsInstances = createBorderLayoutWithJList(
				lblCellInstances, cellInstancesList, 300, 200);
		domainObjectsInstances.setBackground(Color.decode(BACKGROUD_COLOR));

		// provided fragments
		lblProvidedFragments = new JLabel("Provided Fragments");
		lblProvidedFragments.setFont(lblFont);

		providedFragmentsList = new JList<String>();
		providedFragmentsList.setPreferredSize(new Dimension(250, 200));
		providedFragmentsList.setMaximumSize(new Dimension(250, 200));
		providedFragmentsList.setMinimumSize(new Dimension(250, 200));
		// providedFragmentsList
		// .addListSelectionListener(new SelectFragmentListener(this));
		providedFragmentsList
				.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JPanel providedFragments = createBorderLayoutWithJList(
				lblProvidedFragments, providedFragmentsList, 300, 200);
		providedFragments.setBackground(Color.decode(BACKGROUD_COLOR));

		// process model
		lblProcessModel = new JLabel("Process Model");
		lblProcessModel.setFont(lblFont);
		lblProcessModel.setPreferredSize(new Dimension(1020, 24));
		lblProcessModel.setHorizontalAlignment(JLabel.CENTER);

		procModel = new JPanel();
		procModel.setLayout(new BorderLayout());
		procModel.setBorder(new EmptyBorder(10, 10, 10, 10));
		procModel.setBackground(Color.decode(BACKGROUD_COLOR));
		procModel.add(lblProcessModel, BorderLayout.PAGE_START);

		// adding components on the middle panel
		middlePanel.add(domainObjectsInstances);
		middlePanel.add(procModel);
		middlePanel.add(providedFragments);

		// adding the middle panel on the main panel
		mainPanel.add(middlePanel, cMiddle);

		/***************************************** Runtime Execution Tab - BOTTOM PANEL ***********************************/
		// Constraints for the bottom panel on the third row of the mainPanel
		// GridBagLayout
		GridBagConstraints cBottom = getGridBagConstraints(
				GridBagConstraints.PAGE_END, GridBagConstraints.BOTH, 0.5, 0.5,
				0, 4, 15, 15, 30, 30);

		bottomPanel = new JPanel();
		BoxLayout bottomPanelLayout = new BoxLayout(bottomPanel,
				BoxLayout.LINE_AXIS);
		bottomPanel.setLayout(bottomPanelLayout);
		bottomPanel.setBackground(Color.decode(BACKGROUD_COLOR));

		// correlated DOs
		lblCorrelatedEntities = new JLabel("Correlated Domain Objects");
		lblCorrelatedEntities.setFont(lblFont);

		correlatedEntitiesList = new JList<String>();
		correlatedEntitiesList.setMaximumSize(new Dimension(250, 180));
		correlatedEntitiesList.setMinimumSize(new Dimension(250, 180));
		correlatedEntitiesList.setPreferredSize(new Dimension(250, 180));
		correlatedEntitiesList
				.addMouseListener(new CorrelateEntitiesListener());

		JPanel correlatedDomainObjects = createBorderLayoutWithJList(
				lblCorrelatedEntities, correlatedEntitiesList, 300, 180);
		correlatedDomainObjects.setBackground(Color.decode(BACKGROUD_COLOR));

		// Domain Objects Details
		lblDomainObjectsDetails = new JLabel("Domain Object details");
		lblDomainObjectsDetails.setFont(lblFont);

		tabEntity = new JTabbedPane(JTabbedPane.TOP);
		tabEntity.setPreferredSize(new Dimension(300, 220));
		tabEntity.setBackground(Color.decode(BACKGROUD_COLOR));
		// tabEntity.setBorder(new LineBorder(new Color(0, 0, 0)));

		// defining the panel for the tabbedPane
		JPanel domainObjectsDetails = new JPanel();
		domainObjectsDetails.setLayout(new BorderLayout());
		domainObjectsDetails.add(lblDomainObjectsDetails,
				BorderLayout.PAGE_START);
		domainObjectsDetails.add(tabEntity, BorderLayout.CENTER);
		domainObjectsDetails.setBorder(new EmptyBorder(10, 10, 10, 10));
		domainObjectsDetails.setBackground(Color.decode(BACKGROUD_COLOR));

		// creating the bottom left panel
		JPanel bottomLeftPanel = new JPanel();
		BoxLayout layoutLeft = new BoxLayout(bottomLeftPanel, BoxLayout.Y_AXIS);
		bottomLeftPanel.setLayout(layoutLeft);
		bottomLeftPanel.setBackground(Color.decode(BACKGROUD_COLOR));

		// adding components on the bottom left panel
		bottomLeftPanel.add(correlatedDomainObjects);
		bottomLeftPanel.add(domainObjectsDetails);

		// defining content for the tabbedPane
		stateVariablesList = new JList<String>();
		stateVariablesList
				.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		entityKnowledgeList = new JList<String>();
		entityKnowledgeList
				.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		entityKnowledgeScrollPane = createScrollPaneForTab(entityKnowledgeList);
		tabEntity.addTab("Domain Knowledge", null, entityKnowledgeScrollPane);

		entityStateScrollPane = createScrollPaneForTab(stateVariablesList);
		tabEntity.addTab("State Variables", null, entityStateScrollPane);

		tabEntity.setFont(lblFont);

		// process execution panel
		lblProcessExecution = new JLabel("Process Execution");
		lblProcessExecution.setFont(lblFont);
		lblProcessExecution.setHorizontalAlignment(JLabel.CENTER);
		lblProcessExecution.setPreferredSize(new Dimension(1400, 24));

		procExec = new JPanel();
		procExec.setLayout(new BorderLayout());
		procExec.setBorder(new EmptyBorder(10, 10, 14, 10));
		procExec.setBackground(Color.decode(BACKGROUD_COLOR));
		procExec.add(lblProcessExecution, BorderLayout.PAGE_START);

		// adding components on the bottom panel
		bottomPanel.add(bottomLeftPanel);
		bottomPanel.add(procExec);

		// adding the bottom panel on the main panel third row
		mainPanel.add(bottomPanel, cBottom);

		/***************************************************************************************/
		// JLabel lblLog = new JLabel("Log");
		// lblLog.setBounds(10, 800, 223, 22);
		// mainPanel.add(lblLog);

		// log inside a scrollpane
		// logTextArea = new JTextArea("");
		// logTextArea.setBounds(10, 810, 982, 90);
		// logTextArea.setEditable(false);
		// DefaultCaret caret = (DefaultCaret) logTextArea.getCaret();
		// caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		// JScrollPane logScrollPane = new JScrollPane(logTextArea,
		// ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
		// ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		// logScrollPane.setBounds(10, 850, 982, 90);
		// mainPanel.add(logScrollPane);

		// Entity preference window
		newEntityFrame = new JFrame("Domain Objects Demonstrator");
		newEntityFrame.setType(Type.UTILITY);
		newEntityFrame.setVisible(false);
		newEntityFrame.setResizable(false);
		newEntityFrame.setSize(530, 600);
		addNewEntityWindow = new AddNewEntityWindow(this);
		newEntityFrame.setContentPane(addNewEntityWindow);

		/************************* MAIN WINDOW MENU BAR *****************************************/
		JMenuBar menuBar = new JMenuBar();
		// menuBar.setBackground(Color.decode("#" + "9E9E9E"));
		frame.setJMenuBar(menuBar);

		JMenu mnScenario = new JMenu("File");
		menuBar.add(mnScenario);

	//	JMenuItem mntmOpen = new JMenuItem("Open");
	//	mntmOpen.addActionListener(new OpenScenarioListener());
	//	mnScenario.add(mntmOpen);

		JMenuItem mnExit = new JMenuItem("Exit");
		mnExit.addActionListener(new MenuExitListener());

		mnScenario.add(new JSeparator());
		JMenuItem scenario1 = new JMenuItem("Open scenario");
		scenario1
				.setActionCommand(DemonstratorConstant.SCENARIO_CELL_SPECIALIZATION);
		scenario1.addActionListener(new OpenScenarioListener());
		mnScenario.add(scenario1);

		mnScenario.add(new JSeparator());
		mnScenario.add(mnExit);

		mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);

		JMenuItem mnPreferences = new JMenuItem("Preferences");
		mnEdit.add(mnPreferences);

		preferencesDialog = new PreferencesDialog();
		preferencesDialog.setVisible(false);

		mnPreferences.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				preferencesDialog.setVisible(true);
			}
		});

		mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		abtDialog = new AboutDialog();
		abtDialog.setVisible(false);

		mntmAbout = new JMenuItem("About");
		mntmAbout.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				abtDialog.setVisible(true);
			}
		});
		mnHelp.add(mntmAbout);

		/*************************** ADD TOOLBAR *******************************************/
		toolbar = new JToolBar();
		toolbar.setFloatable(false);
		toolbar.setRollover(false);
		toolbar.setEnabled(false);
		BoxLayout toolbarLayout = new BoxLayout(toolbar, BoxLayout.LINE_AXIS);
		toolbar.setLayout(toolbarLayout);
		toolbar.setPreferredSize(new Dimension(100, 60));
		toolbar.addSeparator();

		// load play/pause icon
		playIcon = new ImageIcon(
				MainWindow.class.getResource("/images/ic_play.png"));
		pauseIcon = new ImageIcon(
				MainWindow.class.getResource("/images/ic_pause.png"));
		addIcon = new ImageIcon(
				MainWindow.class.getResource("/images/ic_add.png"));

		// init column names for general table
		columnNames = new Vector<String>();
		columnNames.add("Id");
		columnNames.add("Name");
		columnNames.add("Status");

		btnStep = new JButton("Step");
		btnStep.setActionCommand(DemonstratorConstant.STEP);
		btnStep.addActionListener(new StepButtonActionListener());
		btnStep.setIcon(new ImageIcon(MainWindow.class
				.getResource("/images/ic_walk.png")));

		btnPlaypause = new JButton("Play");
		btnPlaypause.setIcon(playIcon);
		btnPlaypause.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				controller.play();
			}

		});

		//btnAdd = new JButton("Add Domain Object");
		//btnAdd.setIcon(addIcon);

		JButton btnLogo = new JButton("");
		// btnLogo.setSize(30, 65);
		btnLogo.setIcon(new ImageIcon(MainWindow.class
				.getResource("/images/logo_demo_50.png")));

		toolbar.add(btnPlaypause);
		toolbar.add(btnStep);
		//toolbar.add(btnAdd);
		toolbar.add(Box.createHorizontalGlue());
		toolbar.add(btnLogo);

		// create move next/previous entities
		btnPreviousEntity = new JButton("Previous");
		btnPreviousEntity.setIcon(new ImageIcon(MainWindow.class
				.getResource("/images/knob_left.png")));
		btnPreviousEntity
				.setActionCommand(SelectedEntitiesButtonListener.PREVIOUS);
		btnPreviousEntity.addActionListener(new SelectedEntitiesButtonListener(
				this));
		// toolbar.add(btnPreviousEntity);

		btnNextEntity = new JButton("Next");
		btnNextEntity.setIcon(new ImageIcon(MainWindow.class
				.getResource("/images/knob_forward.png")));
		btnNextEntity.setActionCommand(SelectedEntitiesButtonListener.NEXT);
		btnNextEntity
				.addActionListener(new SelectedEntitiesButtonListener(this));

		// toolbar.add(btnNextEntity);
		toolbar.addSeparator();

		// add toolbar
		frame.getContentPane().add(toolbar, BorderLayout.NORTH);

		// add and hide activity view inside another frame
		try {
			refinementFrame = new JFrame("Activity Details");
			refinementFrame.setType(Type.UTILITY);
			refinementView = new ActivityWindow(refinementFrame);
			refinementFrame.setSize(1000, 700);
			refinementFrame.setLocationRelativeTo(null);
			refinementFrame.setContentPane(refinementView);
			showRefinementFrame(false);

		} catch (JAXBException | URISyntaxException e2) {
			logger.error(e2.getMessage(), e2);
		}

		try {
			selectAlternativeFrame = new JFrame(
					"Domain Objects - select alternative");
			selectAlternativeFrame.setType(Type.UTILITY);
			selectAlternativeFrame.setSize(675, 435);
			selectAlternativeWindow = new SelectJourneyAlternativeWindow(this,
					selectAlternativeFrame);
			selectAlternativeFrame.setContentPane(selectAlternativeWindow);
			selectAlternativeFrame.setVisible(false);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		// last init part: hide mainScrollPane
		showMainScrollPane(false);
		showToolbar(false);
	}

	/**
	 * @return
	 */
	private GridBagConstraints getGridBagConstraints(int anchor, int fill,
			double weightx, double weighty, int gridx, int gridy,
			int insetsTop, int insetsBottom, int insetsRight, int insetsLeft) {
		GridBagConstraints gbConstraints = new GridBagConstraints();
		gbConstraints.anchor = anchor;
		gbConstraints.fill = fill;
		gbConstraints.weightx = weightx;
		gbConstraints.weighty = weighty;
		gbConstraints.gridx = gridx; // column 0
		gbConstraints.gridy = gridy; // row 0
		gbConstraints.insets.top = insetsTop; // external 'top' padding
		gbConstraints.insets.bottom = insetsBottom;
		gbConstraints.insets.right = insetsRight;
		gbConstraints.insets.left = insetsLeft;
		return gbConstraints;
	}

	private JScrollPane createScrollPaneForTab(JList<String> listElements) {
		JScrollPane scrollPane = new JScrollPane(listElements);
		scrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		// scrollPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		scrollPane.setBorder(new LineBorder(Color.decode(BORDER_COLOR)));
		return scrollPane;
	}

	private JPanel createBorderLayoutWithJList(JLabel listName,
			JList<String> listElements, int width, int height) {
		JPanel result = new JPanel();
		result.setLayout(new BorderLayout());

		JScrollPane scrollPane = new JScrollPane(listElements);
		scrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		// scrollPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		scrollPane.setBorder(new LineBorder(Color.decode(BORDER_COLOR)));
		scrollPane.setPreferredSize(new Dimension(width, height));

		result.add(listName, BorderLayout.PAGE_START);
		result.add(scrollPane, BorderLayout.CENTER);
		result.setBorder(new EmptyBorder(10, 10, 10, 10));
		result.setBackground(Color.decode(BACKGROUD_COLOR));

		return result;
	}

	public void displayProcess(ProcessDiagram p, boolean model,
			boolean execution) {
		if (p == null) {
			logger.warn("ProcessDiagram must be not null");
			return;
		}
		// create processExecutionPanel or processModelPanel on need
		if (processModelPanel == null) {
			processModelPanel = new ProcessModelPanel(
					controller.getProcessEngineFacade());

			processModelPanel.setLayout(new FlowLayout());
			processModelScrollPane = createProcessPanelScrollPane(
					processModelPanel, 1000, 200);
			procModel.add(processModelScrollPane, BorderLayout.CENTER);
		}
		if (processExecutionPanel == null) {
			// update process execution
			processExecutionPanel = new ProcessModelPanel(
					controller.getProcessEngineFacade());

			processExecutionPanel.init(this);
			processExecutionPanel.setLayout(new FlowLayout());
			processExecutionScrollPane = createProcessPanelScrollPane(
					processExecutionPanel, 1400, 200);
			procExec.add(processExecutionScrollPane, BorderLayout.CENTER);

		}
		// display them
		if (model) {
			processModelPanel.updateProcess(p);
		}
		if (execution) {
			processExecutionPanel.updateProcess(p);
		}
		// then refresh window
		refreshWindow();
	}

	public JScrollPane createProcessPanelScrollPane(
			ProcessModelPanel processModelPanel, int width, int height) {
		JScrollPane scrollPane = new JScrollPane(processModelPanel);
		scrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		// scrollPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		scrollPane.setBorder(new LineBorder(Color.decode(BORDER_COLOR)));
		scrollPane.setPreferredSize(new Dimension(width, height));
		return scrollPane;
	}

	public void addButtonToTab(final JTabbedPane tabEntity) {
		JButton button = new JButton("...");
		button.addMouseListener(new EntityDetailActionListener(this));
		tabEntity.setTabComponentAt(tabEntity.getTabCount() - 1, button);

	}

	public void showMainScrollPane(boolean value) {
		mainScrollPane.setVisible(value);
		showToolbar(true);
		refreshWindow();
	}

	private void showToolbar(boolean visible) {
		toolbar.setVisible(visible);
		btnPlaypause.setVisible(visible);
		btnStep.setVisible(visible);
		//btnAdd.setVisible(visible);
		btnPreviousEntity.setVisible(visible);
		btnNextEntity.setVisible(visible);
	}

	public void loadDomainObjectInstancesTable(
			List<DoiBean> domainObjectInstances) {
		Vector<Vector<String>> data = convertAndFilterForJtable(domainObjectInstances);
		generalTable = new JTable(data, columnNames) {
			@Override
			public boolean isCellEditable(int r, int c) {
				return false; // Disallow the editing of any cell
			}
		};

		generalTable.getColumnModel().getColumn(0).setResizable(false);
		generalTable.getColumnModel().getColumn(1).setResizable(false);
		generalTable.getColumnModel().getColumn(2).setResizable(false);

		generalTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		generalTable.setBounds(10, 10, 200, 223);
		generalTable.setFillsViewportHeight(true);
		generalTable.getSelectionModel().addListSelectionListener(
				new EntityTableSelectionListener(generalTable));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 223, 321);
		// mainPanel.add(tabbedPane);

		entitiesScrollPane = new JScrollPane(generalTable);
		tabbedPane.addTab("Domain Object instances", null, entitiesScrollPane,
				null);
		tabbedPane.setEnabledAt(0, true);

		refreshWindow();
	}

	public void resetDomainObjectInstanceTable() {
		if (generalTable != null) {
			generalTable.setModel(new DefaultTableModel());
		}
	}

	/**
	 * Convert input and filter only with mappable domainObjectInstances
	 * (mappable means with lat and lon)
	 * 
	 * @param domainObjectInstances
	 * @return
	 */
	private Vector<Vector<String>> convertAndFilterForJtable(
			List<DoiBean> domainObjectInstances) {
		Vector<Vector<String>> response = new Vector<Vector<String>>();
		for (DoiBean d : domainObjectInstances) {
			if (d.getLat() != null && d.getLon() != null) {
				Vector<String> v = new Vector<String>();
				v.add(d.getId());
				v.add(d.getName());
				v.add(d.getStatus());
				response.add(v);
			}
		}
		return response;
	}

	public void refreshWindow() {
		frame.getContentPane().validate();
		frame.getContentPane().repaint();
	}

	public void setController(MainController controller) {
		this.controller = controller;
		addNewEntityWindow.setController(controller);
	}

	public void updateDomainObjectInstancesTable(
			List<DoiBean> domainObjectInstances) {
		Vector<Vector<String>> data = convertAndFilterForJtable(domainObjectInstances);

		generalTable.setModel(new DefaultTableModel(data, columnNames));
		generalTable.getColumnModel().getColumn(0).setResizable(false);
		generalTable.getColumnModel().getColumn(1).setResizable(false);
		generalTable.getColumnModel().getColumn(2).setResizable(false);

		refreshWindow();
	}

	public void selectFirstEntityInTable() {
		if (generalTable.getModel().getValueAt(0, 0) != null) {
			generalTable.setRowSelectionInterval(0, 0);
		}
	}

	public String getSelectedEntityInTable() {
		int sr = generalTable.getSelectedRow();
		if (sr == -1 || sr >= generalTable.getModel().getRowCount()) {
			return "";
		}
		return (String) generalTable.getModel().getValueAt(sr, 0);
	}

	public void updateSelectedEntityState(List<String> toDisplay) {
		String[] array = new String[toDisplay.size()];
		for (int i = 0; i < toDisplay.size(); i++) {
			array[i] = toDisplay.get(i);
		}
		stateVariablesList.setListData(array);
	}

	public void updateSelectedEntityCorrelations(List<String> pids) {
		String[] array = new String[pids.size()];
		for (int i = 0; i < pids.size(); i++) {
			array[i] = String.valueOf(pids.get(i));
		}
		correlatedEntitiesList.setListData(array);
	}

	public void updateSelectedEntityProvidedFragments(List<String> values) {
		String[] array = new String[values.size()];
		for (int i = 0; i < values.size(); i++) {
			// array[i] = values.get(i);
			array[i] = String.valueOf(values.get(i));
		}
		providedFragmentsList.setListData(array);
	}

	public void updateCellInstances(List<String> toDisplay) {
		String[] array = new String[toDisplay.size()];
		for (int i = 0; i < toDisplay.size(); i++) {
			array[i] = String.valueOf(toDisplay.get(i));
		}
		cellInstancesList.setListData(array);
	}

	public void showPSFrame(boolean visible) {
		PSFrame.setVisible(visible);
	}

	public void showRefinementFrame(boolean visible) {
		refinementFrame.setVisible(visible);
	}

	/**
	 * 
	 * @return the model of current process (not the executed one, see
	 *         {@link MainController} method getCurrentProcess for it )
	 */
	public ProcessDiagram getCurrentProcess() {
		if (generalTable.getRowCount() > 0) {
			DoiBean current = controller.getCurrentDoiBean();
			ProcessDiagram process = controller.getProcessEngineFacade()
					.getModel(current.getName());
			return process;
		}
		return null;
	}

	public MainController getController() {
		return controller;
	}

	public void updateEntityKnowledge(List<String> toDisplay) {
		String[] array = new String[toDisplay.size()];
		for (int i = 0; i < toDisplay.size(); i++) {
			array[i] = String.valueOf(toDisplay.get(i));
		}
		entityKnowledgeList.setListData(array);
	}

	public ActivityWindow getActivityWindow() {
		return refinementView;
	}

	// public void updateComboxEntities(List<String> input) {
	// comboEntities.removeAllItems();
	// Collections.sort(input);
	// input.add(0, "");
	// for (String s : input) {
	// comboEntities.addItem(s);
	// }
	// }

	// public void updateListDomainObjectsEntities(List<String> input) {
	// String[] array = new String[input.size()];
	// modelList.removeAll();
	// Collections.sort(input);
	// input.add(0, "");
	// modelList.setListData(input.toArray(array));
	// }

	public void resetCellInstances() {
		String[] temp = { "" };
		cellInstancesList.setListData(temp);
	}

	public void resetCorrelatedCells() {
		String[] temp = { "" };
		correlatedEntitiesList.setListData(temp);
	}

	public void resetCellDetails() {
		String[] temp = { "" };
		stateVariablesList.setListData(temp);
	}

	public void resetContextDetails() {
		String[] temp = { "" };
		entityKnowledgeList.setListData(temp);
	}

	public void resetProcessExecution() {
		if (processExecutionPanel != null) {
			processExecutionPanel.clear();
		}
	}

	public void resetProcessModel() {
		if (processModelPanel != null) {
			processModelPanel.clear();
		}
	}

	public void displayAlternativesWindow(UserData ud) {
		// set data and display window
		selectAlternativeWindow.setData(ud);
		selectAlternativeFrame.setVisible(true);
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

	public void selectCellInstances(int index) {
		cellInstancesList.setSelectedIndex(index);

	}

	public String getSelectedCorrelatedTable(int rowIndex) {
		return (String) generalTable.getModel().getValueAt(rowIndex, 0);
	}

	public String getSelectedCorrelatedEntity() {
		int sr = correlatedEntitiesList.getSelectedIndex();
		if (sr == -1 || sr >= correlatedEntitiesList.getModel().getSize()) {
			return "";
		}
		return correlatedEntitiesList.getModel().getElementAt(sr);
	}

	public void resetProcessDisplay() {
		processExecutionPanel = null;
		processModelPanel = null;
	}

	public void setPauseButton() {
		btnPlaypause.setIcon(pauseIcon);
		btnPlaypause.setText("Pause");
	}

	public void setPlayButton() {
		btnPlaypause.setIcon(playIcon);
		btnPlaypause.setText("Play");
	}

	public void enableStep(boolean b) {
		btnStep.setEnabled(b);
		btnPlaypause.setEnabled(b);
	}

	public JPanel getModelPanel() {
		return modelPanel;
	}

}
