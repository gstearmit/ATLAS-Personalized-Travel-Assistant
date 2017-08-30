/**
 * 
 */
package eu.domainobjects.presentation.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultCaret;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//import xmleditorkit.XMLDocument;
//import xmleditorkit.XMLEditorKit;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import eu.domainobjects.presentation.main.action.listener.SelectFragmentListener;
import eu.domainobjects.presentation.main.action.listener.SelectModelListener;
import eu.domainobjects.presentation.main.action.listener.SelectPropertyListener;
import eu.domainobjects.presentation.main.process.ObjectDiagramPanel;
import eu.domainobjects.presentation.main.process.ProcessModelPanel;
import eu.domainobjects.presentation.main.process.ServiceModelPanel;
import eu.fbk.das.process.engine.api.domain.ObjectDiagram;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;
import eu.fbk.das.process.engine.api.domain.ServiceDiagram;

/**
 * @author Martina
 *
 *         Panel for the domain objects models visualization using XMLEditorKit
 *
 */
public class DomainObjectsModelsPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LogManager
			.getLogger(DomainObjectsModelsPanel.class);

	private static final String BACKGROUD_COLOR = "#E0E0E0";
	private static final String BORDER_COLOR = "#9E9E9E";
	private static final Font LABEL_FONT = new Font("Verdana", Font.PLAIN, 17);
	private static final Font TAB_FONT = new Font("Verdana", Font.PLAIN, 18);

	private MainWindow window;
	private JList<String> propertiesList;
	private JList<String> fragmentsList;

	private JList<String> modelList;

	public DomainObjectsModelsPanel(MainWindow mainWindow) {

		this.window = mainWindow;

		// Panel for the Domain Objects Models Tab
		this.setVisible(true);
		this.setBackground(Color.decode(BACKGROUD_COLOR));

		BoxLayout modelsCompLayout = new BoxLayout(this, BoxLayout.LINE_AXIS);
		this.setLayout(modelsCompLayout);

		// domain objects scenario models
		JLabel lblModels = new JLabel("Domain Objects Models");
		lblModels.setFont(LABEL_FONT);

		modelList = new JList<String>();
		modelList.setPreferredSize(new Dimension(250, 200));
		modelList.setMaximumSize(new Dimension(250, 200));
		modelList.setMinimumSize(new Dimension(250, 200));
		modelList
				.addListSelectionListener(new SelectModelListener(this.window));
		modelList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JPanel domainObjectsModels = createBorderLayoutWithJList(lblModels,
				modelList, 300, 200);
		domainObjectsModels.setMinimumSize(new Dimension(300, 400));
		domainObjectsModels.setMaximumSize(new Dimension(300, 400));
		domainObjectsModels.setBorder(new EmptyBorder(15, 10, 10, 10));
		domainObjectsModels.setAlignmentY(Component.TOP_ALIGNMENT);

		// adding the models list to the panel
		this.add(domainObjectsModels);

		// defining the tabbedPane for the models of the different components of
		// a domain object (properties, fragments etc)
		JTabbedPane modelComponentsTabs = new JTabbedPane(JTabbedPane.BOTTOM);
		modelComponentsTabs.setUI(new SpacedTabbedPaneUI());
		modelComponentsTabs.setPreferredSize(new Dimension(1024, 750));
		modelComponentsTabs.setFont(TAB_FONT);
		modelComponentsTabs.setAlignmentY(Component.TOP_ALIGNMENT);
		modelComponentsTabs.addTab("Core Process", null,
				modelCoreProcessPanel(), null);
		modelComponentsTabs.addTab("Provided Fragments", null,
				modelFragmentsPanel(), null);
		modelComponentsTabs.addTab("Domain Knowledge", null,
				modelDomainKnowledgePanel(), null);
		modelComponentsTabs.addTab("Domain Object Definition", null,
				modelDomainObjectDefinitionPanel(), null);
		modelComponentsTabs.setBackground(Color.decode(BACKGROUD_COLOR));
		// modelComponentsTabs
		// .setBorder(new LineBorder(Color.decode(BORDER_COLOR)));

		this.add(modelComponentsTabs);

	}

	private JPanel createBorderLayoutWithJList(JLabel listName,
			JList<String> listElements, int width, int height) {
		JPanel result = new JPanel();
		result.setLayout(new BorderLayout());

		JScrollPane scrollPane = new JScrollPane(listElements);
		scrollPane.setBorder(new LineBorder(Color.decode(BORDER_COLOR)));
		scrollPane.setPreferredSize(new Dimension(width, height));
		scrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		result.add(listName, BorderLayout.PAGE_START);
		result.add(scrollPane, BorderLayout.CENTER);
		result.setBorder(new EmptyBorder(10, 10, 10, 10));
		result.setBackground(Color.decode(BACKGROUD_COLOR));

		return result;
	}

	private JTextPane coreProcessTextPane = new JTextPane();
	private JScrollPane processPane;
	private JPanel panel;
	private ProcessModelPanel processModelPanel;
	private JScrollPane scrollPane;

	// private JScrollPane scrollPane;

	// defines the content of the tab about the domain object core process
	private Component modelCoreProcessPanel() {
		// main panel
		JPanel procModel = new JPanel();
		procModel.setBorder(new EmptyBorder(10, 10, 10, 10));
		procModel.setBackground(Color.decode(BACKGROUD_COLOR));

		BoxLayout compLayout = new BoxLayout(procModel, BoxLayout.Y_AXIS);
		procModel.setLayout(compLayout);

		// scroll pane for the process model panel
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBackground(Color.decode(BACKGROUD_COLOR));
		panel.setBorder(new LineBorder(Color.decode(BORDER_COLOR)));

		scrollPane = defineXMLtextPane(coreProcessTextPane, 900, 600);

		procModel.add(panel);
		procModel.add(Box.createRigidArea(new Dimension(0, 5)));
		procModel.add(scrollPane);

		return procModel;
	}

	private JTextPane definitionTextPane = new JTextPane();

	// defines the content of the tab about the domain object root file
	private Component modelDomainObjectDefinitionPanel() {
		// main panel
		JPanel definitionPanel = new JPanel();
		BoxLayout compLayout = new BoxLayout(definitionPanel,
				BoxLayout.LINE_AXIS);
		definitionPanel.setLayout(compLayout);
		definitionPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		definitionPanel.setBackground(Color.decode(BACKGROUD_COLOR));

		// scrollPane for the textPane showing the xml formatted file
		JScrollPane scrollPane = defineXMLtextPane(definitionTextPane, 900, 600);

		definitionPanel.add(scrollPane);

		return definitionPanel;
	}

	private JTextPane fragmentTextPane = new JTextPane();
	private ServiceModelPanel serviceModelPanel;
	private JScrollPane servicePane;
	private JPanel servicePanel;
	private JScrollPane serviceScrollPane;

	private Component modelFragmentsPanel() {
		// main panel
		JPanel panel = new JPanel();
		panel.setBackground(Color.decode(BACKGROUD_COLOR));
		panel.setBorder(new EmptyBorder(0, 10, 10, 10));
		panel.setAlignmentY(Component.TOP_ALIGNMENT);

		BoxLayout compLayout = new BoxLayout(panel, BoxLayout.LINE_AXIS);
		panel.setLayout(compLayout);

		JPanel serviceModel = new JPanel();
		serviceModel.setBorder(new EmptyBorder(10, 10, 10, 10));
		serviceModel.setBackground(Color.decode(BACKGROUD_COLOR));

		BoxLayout compLayoutLeft = new BoxLayout(serviceModel, BoxLayout.Y_AXIS);
		serviceModel.setLayout(compLayoutLeft);
		serviceModel.setAlignmentY(Component.TOP_ALIGNMENT);

		// label and comboBox for the fragments models selection
		JPanel panelListFragments = new JPanel();
		panelListFragments.setLayout(new BoxLayout(panelListFragments,
				BoxLayout.Y_AXIS));
		panelListFragments.setBackground(Color.decode(BACKGROUD_COLOR));
		JLabel lblFragments = new JLabel("Provided Fragments");
		lblFragments.setFont(LABEL_FONT);

		fragmentsList = new JList<String>();
		fragmentsList.setVisible(true);
		// propertiesList.setBorder(new LineBorder(Color.decode(BORDER_COLOR)));
		fragmentsList.setPreferredSize(new Dimension(250, 180));
		fragmentsList.setMaximumSize(new Dimension(250, 180));
		fragmentsList.setMinimumSize(new Dimension(250, 180));
		fragmentsList.addListSelectionListener(new SelectFragmentListener(
				this.window));
		// //define its own listener
		fragmentsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JPanel fragmentsModels = createBorderLayoutWithJList(lblFragments,
				fragmentsList, 300, 180);
		fragmentsModels.setMinimumSize(new Dimension(300, 400));
		fragmentsModels.setMaximumSize(new Dimension(300, 400));
		panelListFragments.add(fragmentsModels);
		panelListFragments.setAlignmentY(Component.TOP_ALIGNMENT);

		// scrollPane for the textPane showing the xml model of a property
		serviceScrollPane = defineXMLtextPane(fragmentTextPane, 900, 600);
		serviceScrollPane.setAlignmentY(Component.TOP_ALIGNMENT);

		// scroll pane for the service model panel
		servicePanel = new JPanel();
		servicePanel.setLayout(new BorderLayout());
		servicePanel.setBackground(Color.decode(BACKGROUD_COLOR));
		servicePanel.setBorder(new LineBorder(Color.decode(BORDER_COLOR)));

		panel.add(panelListFragments);
		panel.add(Box.createRigidArea(new Dimension(0, 5)));
		panel.add(serviceModel);
		serviceModel.add(servicePanel);
		serviceModel.add(Box.createRigidArea(new Dimension(0, 5)));
		serviceModel.add(serviceScrollPane);

		return panel;
	}

	private JTextPane propertyTextPane = new JTextPane();
	private JScrollPane propertyScrollPane;
	private JPanel propertyPanel;
	private ObjectDiagramPanel propertyModelPanel;
	private JScrollPane propertyPane;

	private Component modelDomainKnowledgePanel() {
		// main panel
		JPanel panel = new JPanel();
		panel.setBackground(Color.decode(BACKGROUD_COLOR));
		panel.setBorder(new EmptyBorder(0, 10, 10, 10));
		panel.setAlignmentY(Component.TOP_ALIGNMENT);

		BoxLayout compLayout = new BoxLayout(panel, BoxLayout.LINE_AXIS);
		panel.setLayout(compLayout);

		JPanel propertyModel = new JPanel();
		propertyModel.setBorder(new EmptyBorder(10, 10, 10, 10));
		propertyModel.setBackground(Color.decode(BACKGROUD_COLOR));

		BoxLayout compLayoutLeft = new BoxLayout(propertyModel,
				BoxLayout.Y_AXIS);
		propertyModel.setLayout(compLayoutLeft);
		propertyModel.setAlignmentY(Component.TOP_ALIGNMENT);

		// label and comboBox for the domain objects models selection
		JPanel panelListProperties = new JPanel();
		panelListProperties.setLayout(new BoxLayout(panelListProperties,
				BoxLayout.Y_AXIS));
		panelListProperties.setBackground(Color.decode(BACKGROUD_COLOR));
		JLabel lblDomainProperties = new JLabel("Domain Properties");
		lblDomainProperties.setFont(LABEL_FONT);

		propertiesList = new JList<String>();
		propertiesList.setVisible(true);
		// propertiesList.setBorder(new LineBorder(Color.decode(BORDER_COLOR)));
		propertiesList.setPreferredSize(new Dimension(250, 180));
		propertiesList.setMaximumSize(new Dimension(250, 180));
		propertiesList.setMinimumSize(new Dimension(250, 180));
		propertiesList.addListSelectionListener(new SelectPropertyListener(
				this.window));
		// //define its own listener
		propertiesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JPanel propertiesModels = createBorderLayoutWithJList(
				lblDomainProperties, propertiesList, 300, 180);
		propertiesModels.setMinimumSize(new Dimension(300, 400));
		propertiesModels.setMaximumSize(new Dimension(300, 400));
		panelListProperties.add(propertiesModels);
		panelListProperties.setAlignmentY(Component.TOP_ALIGNMENT);

		// scrollPane for the textPane showing the xml model of a property
		propertyScrollPane = defineXMLtextPane(propertyTextPane, 900, 600);
		propertyScrollPane.setAlignmentY(Component.TOP_ALIGNMENT);

		// scroll pane for the property model panel
		propertyPanel = new JPanel();
		propertyPanel.setLayout(new BorderLayout());
		propertyPanel.setBackground(Color.decode(BACKGROUD_COLOR));
		propertyPanel.setBorder(new LineBorder(Color.decode(BORDER_COLOR)));

		panel.add(panelListProperties);
		panel.add(Box.createRigidArea(new Dimension(0, 5)));
		panel.add(propertyModel);
		propertyModel.add(propertyPanel);
		propertyModel.add(Box.createRigidArea(new Dimension(0, 5)));
		propertyModel.add(propertyScrollPane);

		return panel;
	}

	private static final int XML_TEXT_FONT_SIZE = 18;
	private static final String ATTRIBUTENAME_TAG_COLOR = "#C2185B";
	private static final String TAGNAME_COLOR = "#1B5E20";

	private JScrollPane defineXMLtextPane(JTextPane textPane, int width,
			int height) {
		// XMLEditorKit xmlEditorKit = new XMLEditorKit();
		// StyleConstants.setFontSize(XMLDocument.PLAIN_ATTRIBUTES,
		// XML_TEXT_FONT_SIZE);
		// StyleConstants.setFontSize(XMLDocument.ATTRIBUTENAME_ATTRIBUTES,
		// XML_TEXT_FONT_SIZE);
		// StyleConstants.setFontSize(XMLDocument.ATTRIBUTEVALUE_ATTRIBUTES,
		// XML_TEXT_FONT_SIZE);
		// StyleConstants.setFontSize(XMLDocument.BRACKET_ATTRIBUTES,
		// XML_TEXT_FONT_SIZE);
		// StyleConstants.setFontSize(XMLDocument.COMMENT_ATTRIBUTES,
		// XML_TEXT_FONT_SIZE);
		// StyleConstants.setFontSize(XMLDocument.TAGNAME_ATTRIBUTES,
		// XML_TEXT_FONT_SIZE);
		// StyleConstants.setForeground(XMLDocument.ATTRIBUTENAME_ATTRIBUTES,
		// Color.decode(ATTRIBUTENAME_TAG_COLOR));
		// StyleConstants.setForeground(XMLDocument.TAGNAME_ATTRIBUTES,
		// Color.decode(TAGNAME_COLOR));
		// StyleConstants.setBold(XMLDocument.ATTRIBUTENAME_ATTRIBUTES, false);
		// StyleConstants.setBold(XMLDocument.TAGNAME_ATTRIBUTES, false);
		//
		// textPane.setEditorKit(xmlEditorKit);

		textPane.setContentType("text/xml");

		DefaultCaret caret = (DefaultCaret) textPane.getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		JScrollPane scrollPane = new JScrollPane(textPane,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(width, height));
		scrollPane.setBorder(new LineBorder(Color.decode(BORDER_COLOR)));

		return scrollPane;
	}

	public void updateListDomainObjectsEntities(List<String> input) {
		String[] array = new String[input.size()];
		modelList.removeAll();
		Collections.sort(input);
		input.add(0, "");
		modelList.setListData(input.toArray(array));
	}

	public void updateDomainPropertiesList(List<String> input) {
		String[] array = new String[input.size()];
		propertiesList.removeAll();
		Collections.sort(input);
		input.add(0, "");
		propertiesList.setListData(input.toArray(array));
	}

	public void updateFragmentsList(List<String> input) {
		String[] array = new String[input.size()];
		fragmentsList.removeAll();
		Collections.sort(input);
		input.add(0, "");
		fragmentsList.setListData(input.toArray(array));
	}

	public void updateCoreProcessPanel(String filePath, ProcessDiagram process) {
		String text = parseXMLFile(filePath);
		// XMLDocument doc = (XMLDocument) coreProcessTextPane.getEditorKit()
		// .createDefaultDocument();
		// XMLEditorKit editorKit = (XMLEditorKit) coreProcessTextPane
		// .getEditorKit();
		// try {
		// doc.insertString(0, text, null);
		// } catch (BadLocationException e) {
		// e.printStackTrace();
		// }
		// coreProcessTextPane.setEditorKit(editorKit);
		// coreProcessTextPane.setDocument(doc);

		coreProcessTextPane.setText(text);

		// set the graphical process
		processModelPanel = new ProcessModelPanel(this.window.getController()
				.getProcessEngineFacade());
		processPane = window.createProcessPanelScrollPane(processModelPanel,
				1000, 200);
		processModelPanel.updateProcess(process);
		panel.removeAll();
		panel.add(processPane, BorderLayout.CENTER);
	}

	public void updateDefinitionPanel(String filePath) {
		String text = parseXMLFile(filePath);
		definitionTextPane.setText(text);
	}

	public void updateFragmentPanel(String filePath, ServiceDiagram service) {
		String text = parseXMLFile(filePath);

		fragmentTextPane.setText(text);

		// set the graphical process
		serviceModelPanel = new ServiceModelPanel(this.window.getController()
				.getProcessEngineFacade());

		serviceModelPanel.setLayout(new FlowLayout());
		servicePane = window.createServicePanelScrollPane(serviceModelPanel,
				1000, 200);
		serviceModelPanel.defineServiceDiagramProcess(service);
		servicePanel.removeAll();
		servicePanel.add(servicePane, BorderLayout.CENTER);

		window.refreshWindow();
	}

	public void updatePropertyPanel(String filePath, ObjectDiagram property) {
		String text = parseXMLFile(filePath);
		propertyTextPane.setText(text);

		// set the graphical property
		propertyModelPanel = new ObjectDiagramPanel(this.window.getController()
				.getProcessEngineFacade());
		propertyModelPanel.setLayout(new FlowLayout());
		propertyPane = window.createPropertyPanelScrollPane(propertyModelPanel,
				1000, 200);
		propertyModelPanel.defineObjectDiagramGraph(property);
		propertyPanel.removeAll();
		propertyPanel.add(propertyPane, BorderLayout.CENTER);

		window.refreshWindow();
	}

	private String parseXMLFile(String urlString) {
		String xmlString = "";
		if (urlString != null && !urlString.isEmpty()) {
			URL url = MainWindow.class.getResource(urlString);
			try {
				xmlString = Resources.toString(url, Charsets.UTF_8);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return xmlString;
	}

	public void clearModelsPanel() {
		propertyTextPane.setText("");
		fragmentTextPane.setText("");
		coreProcessTextPane.setText("");
		definitionTextPane.setText("");

		DefaultCaret caret = (DefaultCaret) definitionTextPane.getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

	}

}
