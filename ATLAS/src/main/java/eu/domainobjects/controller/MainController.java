package eu.domainobjects.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.generics.BotSession;
import org.w3c.dom.Element;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import eu.domainobjects.controller.events.DomainObjectInstanceSelection;
import eu.domainobjects.controller.events.StepEvent;
import eu.domainobjects.presentation.main.DomainObjectsModelsPanel;
import eu.domainobjects.presentation.main.MainWindow;
import eu.domainobjects.presentation.main.action.listener.DomainObjectDefinitionSelectionByName;
import eu.domainobjects.presentation.main.action.listener.StepButtonActionListener;
import eu.domainobjects.presentation.main.events.DomainObjectInstanceSelectionByName;
import eu.domainobjects.presentation.main.events.StoryboardLoadedEvent;
import eu.domainobjects.utils.DoiBean;
import eu.domainobjects.utils.ExternalEvent;
import eu.domainobjects.utils.PlayRunner;
import eu.domainobjects.utils.ResourceLoader;
import eu.domainobjects.utils.UserData;
import eu.fbk.das.composer.api.exceptions.InvalidServiceInitialStateException;
import eu.fbk.das.composer.api.exceptions.InvalidServiceTransitionException;
import eu.fbk.das.composer.api.exceptions.ServiceDuplicateActionException;
import eu.fbk.das.domainobject.executable.AskToUseCurrentLocationExecutable;
import eu.fbk.das.domainobject.executable.BBCServiceCallExecutable;
import eu.fbk.das.domainobject.executable.ChooseAlternativeExecutable;
import eu.fbk.das.domainobject.executable.DVMDefineDataPatternExecutable;
import eu.fbk.das.domainobject.executable.InsertDestinationExecutable;
import eu.fbk.das.domainobject.executable.InsertOptionalDataExecutable;
import eu.fbk.das.domainobject.executable.Rome2RioCallExecutable;
import eu.fbk.das.domainobject.executable.SelectPlanningModeExecutable;
import eu.fbk.das.domainobject.executable.ShowResultsExecutable;
import eu.fbk.das.domainobject.executable.StartChatbotExecutable;
import eu.fbk.das.domainobject.executable.TAHandleLegResultsExecutable;
import eu.fbk.das.domainobject.executable.TAProvideSelectedDestinationExecutable;
import eu.fbk.das.domainobject.executable.TAProvideSelectedSourceExecutable;
import eu.fbk.das.domainobject.executable.TARefineDestinationPointExecutable;
import eu.fbk.das.domainobject.executable.TARefineSourcePointExecutable;
import eu.fbk.das.domainobject.executable.TAShowLegResultsExecutable;
import eu.fbk.das.domainobject.executable.TAcheckLegSetExecutable;
import eu.fbk.das.domainobject.executable.TAdefineJourneyLegsExecutable;
import eu.fbk.das.domainobject.executable.TAidentifyLegExecutable;
import eu.fbk.das.domainobject.executable.UserInsertSourceLocationExecutable;
import eu.fbk.das.domainobject.executable.VTServiceCallExecutable;
import eu.fbk.das.domainobject.executable.utils.BotTelegram.TravelAssistantBot;
import eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Keyboards;
import eu.fbk.das.domainobject.executable.utils.Rome2Rio.TripAlternativeRome2Rio;
//import eu.fbk.das.domainobject.executable.Rome2RioCallExecutable;
import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.DomainObjectManagerInterface;
import eu.fbk.das.process.engine.api.domain.DomainObjectDefinition;
import eu.fbk.das.process.engine.api.domain.ObjectDiagram;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;
import eu.fbk.das.process.engine.api.domain.ServiceDiagram;
import eu.fbk.das.process.engine.api.jaxb.DomainObject;
import eu.fbk.das.process.engine.api.jaxb.VariableType;
import eu.fbk.das.process.engine.impl.ProcessEngineImpl;
import eu.fbk.das.process.engine.impl.util.Parser;

/**
 * Domain Objects Demonstrator's Main controller, use it to post/subscribe
 * events using google's EventBus
 * 
 * @see EventBus
 */
public class MainController {

	private static final Logger logger = LogManager
			.getLogger(ProcessEngineFacade.class);

	private static EventBus eventBus;
	private MainWindow window;
	private ProcessEngineFacade processEngineFacade;

	private Map<String, UserData> userData = new HashMap<String, UserData>();

	private Map<String, List<ServiceDiagram>> doServiceDiagrams = new HashMap<String, List<ServiceDiagram>>();

	private DoiBean current;

	private List<ExternalEvent> externalEvents = new ArrayList<ExternalEvent>();

	private Map<String, Integer> monitorSelectedMap = new HashMap<String, Integer>();

	private static Parser parser = new Parser();
	private String currentSelectedDomainObjectName = new String();
	private static final String WORKING_DIR = "workingDir";

	/**
	 * Construct controller for Demonstrator. Initialize {@link EventBus} with
	 * {@link MainController} instance
	 * 
	 * @param window
	 *            instance
	 */
	public MainController(MainWindow window) {
		eventBus = new EventBus();
		register(this);
		this.window = window;
	}

	/**
	 * Post an event on eventBus
	 * 
	 * @param event
	 *            - a generic object that represent an event, subscribed by a
	 *            method
	 * @see EventBus#post(Object)
	 * 
	 */
	public static void post(Object event) {
		if (eventBus == null) {
			logger.warn("EventBus is not initialized correctly, not possible to post event");
			return;
		}
		eventBus.post(event);
	}

	/**
	 * Register a subscriber to be notified by events
	 * 
	 * 
	 * @param subscriber
	 *            object
	 * @see EventBus#register(Object)
	 */
	public static void register(Object subscriber) {
		if (eventBus == null) {
			logger.warn("EventBus is not initialized correctly, not possible to register subscriber");
			return;
		}
		eventBus.register(subscriber);
	}

	@Subscribe
	public void onStoryboardLoaded(StoryboardLoadedEvent sle) {
		try {
			// init scenario: load domainObjectInstances
			processEngineFacade = new ProcessEngineFacade(ResourceLoader
					.getScenarioFile().getParent());
			processEngineFacade.loadScenario(ResourceLoader.getScenarioFile()
					.getName(), this);
			logger.debug("ProcessEngineFacade init complete");
			window.loadDomainObjectInstancesTable(processEngineFacade
					.getDomainObjectInstances());

			// update comboboxModels
			// updateComboboxEntities();
			updateListDomainObjectsEntities();

			// show main window
			window.showMainScrollPane(true);

			// register handler for executable activities
			registerHandlersForProcessEngine();

			// addLog("Storyboard loaded: "
			// + ResourceLoader.getScenarioFile().getAbsolutePath());
		} catch (Exception e) {
			logger.debug("Problem on loading the storyboard!");
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * Register handlers for executable activity. This is a bridge with actual
	 * implementation of a given activity; when processEngine process an
	 * activity with a given name, registered handler will be called and
	 * executed
	 */
	private void registerHandlersForProcessEngine() {

		ActionListener aListner = new StepButtonActionListener();
		ActionEvent event = new ActionEvent(
				processEngineFacade.getProcessEngine(),
				ActionEvent.ACTION_PERFORMED, "step");

		// Bot Creation
		ApiContextInitializer.init();
		Keyboards keyboards = new Keyboards();

		TelegramBotsApi api = new TelegramBotsApi();
		TravelAssistantBot bot = null;
		try {

			String botData = this.getBotParameters();
			String[] fields = botData.split(";");
			String name = fields[0];
			String token = fields[1];
			String[] nameValues = name.split("=");
			String[] tokenValues = token.split("=");

			String botName = nameValues[1];
			String botToken = tokenValues[1];

			bot = new TravelAssistantBot(botName, botToken, false, false,
					false, false, aListner, event);

			BotSession session = api.registerBot(bot);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}

		Long ChatId = bot.getCurrentID();

		ArrayList<TripAlternativeRome2Rio> alternatives = new ArrayList<TripAlternativeRome2Rio>();

		processEngineFacade.addExecutableHandler(
				"R2R_ServiceCall",
				new Rome2RioCallExecutable(processEngineFacade
						.getProcessEngine(), alternatives, bot));

		processEngineFacade.addExecutableHandler(
				"TA_StartChatbot",
				new StartChatbotExecutable(processEngineFacade
						.getProcessEngine(), bot, ChatId));

		processEngineFacade.addExecutableHandler(
				"TA_UseCurrentLocation",
				new AskToUseCurrentLocationExecutable(processEngineFacade
						.getProcessEngine(), bot));

		processEngineFacade.addExecutableHandler(
				"TA_InsertSource",
				new UserInsertSourceLocationExecutable(processEngineFacade
						.getProcessEngine(), bot));

		processEngineFacade.addExecutableHandler(
				"TA_InsertDestination",
				new InsertDestinationExecutable(processEngineFacade
						.getProcessEngine(), bot));

		processEngineFacade.addExecutableHandler(
				"TA_InsertOptionalData",
				new InsertOptionalDataExecutable(processEngineFacade
						.getProcessEngine(), bot));

		processEngineFacade.addExecutableHandler(
				"TA_SelectPlanningMode",
				new SelectPlanningModeExecutable(processEngineFacade
						.getProcessEngine(), bot));

		processEngineFacade.addExecutableHandler(
				"TA_ShowResults",
				new ShowResultsExecutable(processEngineFacade
						.getProcessEngine(), bot));

		processEngineFacade.addExecutableHandler(
				"TA_ChooseAlternative",
				new ChooseAlternativeExecutable(processEngineFacade
						.getProcessEngine(), bot));

		processEngineFacade.addExecutableHandler(
				"TA_DefineJourneyLegs",
				new TAdefineJourneyLegsExecutable(processEngineFacade
						.getProcessEngine()));

		processEngineFacade.addExecutableHandler(
				"TA_IdentifyLeg",
				new TAidentifyLegExecutable(processEngineFacade
						.getProcessEngine()));

		processEngineFacade.addExecutableHandler(
				"TA_CheckLegSet",
				new TAcheckLegSetExecutable(processEngineFacade
						.getProcessEngine()));

		processEngineFacade.addExecutableHandler(
				"DVM_DefineDataPattern",
				new DVMDefineDataPatternExecutable(processEngineFacade
						.getProcessEngine(), bot));

		processEngineFacade.addExecutableHandler(
				"BBC_ServiceCall",
				new BBCServiceCallExecutable(processEngineFacade
						.getProcessEngine(), bot));

		processEngineFacade.addExecutableHandler(
				"TA_ShowLegResults",
				new TAShowLegResultsExecutable(processEngineFacade
						.getProcessEngine(), bot));

		processEngineFacade.addExecutableHandler(
				"TA_HandleLegResults",
				new TAHandleLegResultsExecutable(processEngineFacade
						.getProcessEngine()));

		processEngineFacade.addExecutableHandler(
				"TA_RefineSourcePoint",
				new TARefineSourcePointExecutable(processEngineFacade
						.getProcessEngine(), bot));

		processEngineFacade.addExecutableHandler(
				"TA_RefineDestinationPoint",
				new TARefineDestinationPointExecutable(processEngineFacade
						.getProcessEngine(), bot));

		processEngineFacade.addExecutableHandler(
				"TA_ProvideSelectedSource",
				new TAProvideSelectedSourceExecutable(processEngineFacade
						.getProcessEngine(), bot));

		processEngineFacade.addExecutableHandler(
				"TA_ProvideSelectedDestination",
				new TAProvideSelectedDestinationExecutable(processEngineFacade
						.getProcessEngine(), bot));
		processEngineFacade.addExecutableHandler(
				"VT_ServiceCall",
				new VTServiceCallExecutable(processEngineFacade
						.getProcessEngine(), bot));

		// handler for hoaa for pre-phase
		// processEngineFacade.addExecutableHandler(
		// "TO_HOAAorganizeTrip",
		// new ToHoaaOrganizeTripExecutable(processEngineFacade
		// .getProcessEngine()));
		// handler for hoaa for execute phase
		// processEngineFacade
		// .addExecutableHandler(
		// "USER_ExecuteTrip",
		// new UserExecuteTripHoaa(processEngineFacade
		// .getProcessEngine()));
	}

	@Subscribe
	public void onDomainObjectInstanceSelection(DomainObjectInstanceSelection e) {
		DoiBean instance = findDoiBeanByName(e.getName());
		if (instance != null) {
			current = instance;
			updateInterface(current);
		}
	}

	private boolean isProcessEnded(String userName) {
		Optional<DomainObjectInstance> dr = processEngineFacade
				.getProcessEngine().getDomainObjectInstances().stream()
				.filter(d -> d.getId().equals(userName)).findFirst();
		if (dr.isPresent()) {
			if (dr.get().getProcess().isEnded()
					|| dr.get().getProcess().isTerminated()) {
				return true;
			}
		}
		return false;
	}

	@Subscribe
	public void onDomainObjectInstanceSelection(
			DomainObjectInstanceSelectionByName e) {
		try {
			if (e.name != null) {
				current = findDoiBeanByName(e.name);
				updateInterface(current);
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}

	@Subscribe
	public void onDomainObjectDefinition(DomainObjectDefinitionSelectionByName e) {
		// given model name, get first instance: if there is at least one,
		// update interface with it, otherwise search process model by type an
		// populate interface with info
		DoiBean instance = null;
		for (DoiBean db : processEngineFacade.getDomainObjectInstances()) {
			if (db.getName().startsWith(e.name)) {
				instance = db;
				break;
			}
		}
		if (instance != null) {
			current = instance;
			updateInterface(current);
			// reset all information not related directly to cell model
			window.resetCorrelatedCells();
			window.resetContextDetails();
			window.resetCellDetails();
			window.resetProcessExecution();
			window.resetProcessModel();
		} else {
			ProcessDiagram p = processEngineFacade.getModelByType(e.name);
			if (p != null) {
				// display model information
				resetInterface();
				window.displayProcess(p, true, false);
				// update fragments list
				List<String> f = processEngineFacade.getFragmentNames(e.name);
				window.updateSelectedEntityProvidedFragments(f);
			}
		}
	}

	private void resetInterface() {
		window.resetCellInstances();
		window.resetCorrelatedCells();
		window.resetCellDetails();
		window.resetContextDetails();
		window.resetProcessExecution();
		window.resetProcessModel();
	}

	/**
	 * Update window interface and refresh it
	 */
	public void updateInterface() {
		updateInterface(getCurrentDoiBean());
	}

	/**
	 * Update window interface and refresh it
	 */
	public void updateInterface(DoiBean db) {
		try {
			DomainObjectInstance doi = processEngineFacade.getManager()
					.findInstanceById(db.getName());
			if (doi == null) {
				return;
			}
			displayProcessExecution(db);
			displayProcessModel(db);
			updateSelectedEntityState(db);
			updateSelectedEntityCorrelations(db);
			updateSelectedEntityProvidedFragments(db);
			updateCellInstances(db);
			updateSelectedEntityKnowledge(db);
			// updateComboboxEntities();
			window.refreshWindow();
		} catch (Exception e) {
			logger.error("Error in interface update", e);
		}
	}

	private static final String PROCESSES_DIR = "/storyboard1/processes/";
	private static final String DOMAIN_OBJECTS_DIR = "/storyboard1/domainObjects/";
	private static final String DOMAIN_KNOWLEDGE_DIR = "/storyboard1/domainProperties/";
	private static final String FRAGMENTS_DIR = "/storyboard1/fragments/";

	/**
	 * Update domain objects models tab and refresh it
	 */
	public void updateInterfaceModelsTab(String modelName) {
		currentSelectedDomainObjectName = modelName;
		try {
			clearPanels();
			window.refreshWindow();
			updateFragmentsModels(modelName);
			updateDomainPropertisModel(modelName);
			updateCoreProcessModel(modelName);
			updateDefinitionModel(modelName);

		} catch (Exception e) {
			logger.error("Error in interface update", e);
		}
	}

	public void clearPanels() {
		((DomainObjectsModelsPanel) window.getModelPanel()).clearModelsPanel();
	}

	private void updateCoreProcessModel(String modelName) {
		String filePath = PROCESSES_DIR.concat("PROC_").concat(modelName)
				.concat(".xml");
		// process model diagram
		ProcessDiagram process = processEngineFacade.getModelByType(modelName);
		((DomainObjectsModelsPanel) window.getModelPanel())
				.updateCoreProcessPanel(filePath, process);
	}

	private void updateDefinitionModel(String modelName) {
		String filePath = DOMAIN_OBJECTS_DIR.concat(modelName).concat(".xml");

		((DomainObjectsModelsPanel) window.getModelPanel())
				.updateDefinitionPanel(filePath);
	}

	private void updateDomainPropertisModel(String modelName) {
		List<String> properties = new ArrayList<String>();

		DomainObject current = null;
		List<DomainObjectDefinition> dod = processEngineFacade
				.getDomainObjectDefinitions();
		for (DomainObjectDefinition definition : dod) {
			if (definition.getDomainObject().getName().equals(modelName)) {
				current = definition.getDomainObject();
				break;
			}
		}

		if (current.getDomainKnowledge() != null) {
			if (current.getDomainKnowledge().getInternalDomainProperty() != null
					&& !current.getDomainKnowledge()
							.getInternalDomainProperty().isEmpty()) {
				String internalProperty = "internal/"
						+ current.getDomainKnowledge()
								.getInternalDomainProperty().get(0).getName()
								.split("/")[1];
				properties.add(internalProperty);
			}

			if (current.getDomainKnowledge().getExternalDomainProperty() != null
					&& !current.getDomainKnowledge()
							.getExternalDomainProperty().isEmpty()) {
				for (int j = 0; j < current.getDomainKnowledge()
						.getExternalDomainProperty().size(); j++) {
					String externalProperty = "external/"
							+ current.getDomainKnowledge()
									.getExternalDomainProperty().get(j)
									.getName().split("/")[1];
					properties.add(externalProperty);
				}
			}
		}

		((DomainObjectsModelsPanel) window.getModelPanel())
				.updateDomainPropertiesList(properties);
	}

	private void updateFragmentsModels(String modelName)
			throws InvalidServiceInitialStateException,
			InvalidServiceTransitionException, ServiceDuplicateActionException {
		List<String> fragmentsList = new ArrayList<String>();

		DomainObject current = null;
		DomainObjectDefinition currentDod = null;
		List<DomainObjectDefinition> dod = processEngineFacade
				.getDomainObjectDefinitions();
		for (DomainObjectDefinition definition : dod) {
			if (definition.getDomainObject().getName().equals(modelName)) {
				current = definition.getDomainObject();
				currentDod = definition;
				break;
			}
		}

		if (current.getFragment() != null) {
			for (int j = 0; j < current.getFragment().size(); j++) {
				String fragmentName = current.getFragment().get(j).getName()
						.split("/")[1];
				fragmentsList.add(fragmentName);
			}
		}

		if (!doServiceDiagrams.containsKey(modelName)) {
			doServiceDiagrams.put(modelName,
					parser.convertToServiceDiagram(currentDod.getFragments()));
		}

		((DomainObjectsModelsPanel) window.getModelPanel())
				.updateFragmentsList(fragmentsList);
	}

	public void updateFragmentsModelsTab(String fragmentName) {
		List<ServiceDiagram> services = new ArrayList<ServiceDiagram>();
		String filePath = FRAGMENTS_DIR.concat(fragmentName).concat(".xml");
		((DomainObjectsModelsPanel) window.getModelPanel())
				.updateFragmentPanel(filePath);

		if (doServiceDiagrams.containsKey(currentSelectedDomainObjectName)) {
			services = doServiceDiagrams.get(currentSelectedDomainObjectName);
		}

		ServiceDiagram currentService;
		for (ServiceDiagram sd : services) {
			if (sd.getSid().equalsIgnoreCase(fragmentName)) {
				currentService = sd;
				break;
			}
		}

		// TODO: chiamata a metodo che mostra il frammento come processo. Vedi
		// come fa per process diagram
	}

	public void updatePropertyModelTab(String propertyName) {
		propertyName = propertyName.split("/")[1];
		String filePath = DOMAIN_KNOWLEDGE_DIR.concat(propertyName).concat(
				".xml");
		((DomainObjectsModelsPanel) window.getModelPanel())
				.updatePropertyPanel(filePath);
	}

	// private void updateComboboxEntities() {
	// List<String> response = new ArrayList<String>();
	// for (DomainObjectDefinition dod : processEngineFacade
	// .getDomainObjectDefinitions()) {
	// if (!response.contains(dod.getDomainObject().getName())) {
	// response.add(dod.getDomainObject().getName());
	// }
	// }
	// window.updateComboxEntities(response);
	//
	// }

	private void updateListDomainObjectsEntities() {
		List<String> response = new ArrayList<String>();
		for (DomainObjectDefinition dod : processEngineFacade
				.getDomainObjectDefinitions()) {
			if (!response.contains(dod.getDomainObject().getName())) {
				response.add(dod.getDomainObject().getName());
			}
		}
		((DomainObjectsModelsPanel) window.getModelPanel())
				.updateListDomainObjectsEntities(response);

	}

	private void updateSelectedEntityKnowledge(DoiBean db) {
		if (db == null) {
			return;
		}
		DomainObjectInstance doi = processEngineFacade
				.getDomainObjectInstanceForProcess(processEngineFacade
						.getProcessDiagram(db));
		if (doi != null) {
			List<String> response = new ArrayList<String>();
			response = getKnowledgeValues(response, doi.getInternalKnowledge());
			response = getKnowledgeValues(response, doi.getExternalKnowledge());
			window.updateEntityKnowledge(response);
		}
	}

	private List<String> getKnowledgeValues(List<String> response,
			List<ObjectDiagram> internal) {
		String v = "";
		if (internal != null) {
			for (ObjectDiagram in : internal) {
				v = in.getOid() + " = " + in.getCurrentState();
				if (!response.contains(v)) {
					response.add(v);
				}
			}
		}
		return response;
	}

	private void updateCellInstances(DoiBean db2) {
		if (db2 == null) {
			return;
		}
		List<String> toDisplay = new ArrayList<String>();
		List<DoiBean> instances = getProcessEngineFacade()
				.getDomainObjectInstances();
		if (instances == null) {
			logger.warn("domainObjectInstances null");
			return;
		}
		// put ino cellInstances list a list of all cell instances of same type
		// of current
		String type = getCurrentType(processEngineFacade.getProcessDiagram(db2));
		if (type.isEmpty()) {
			window.updateCellInstances(toDisplay);
			logger.debug("type of current domainObjectInstance not found");
			return;
		}
		// return all instances of same type (f.e. all UMS instances using
		// current type
		List<DoiBean> result = new ArrayList<DoiBean>();
		for (DoiBean db : instances) {
			ProcessDiagram p = processEngineFacade.getProcessDiagram(db);
			if (db.getName().startsWith(type) && p != null) { // && !p.isEnded()
				result.add(db);
			}
		}
		for (DoiBean db : result) {
			toDisplay.add(db.getName());
		}
		window.updateCellInstances(toDisplay);
	}

	/**
	 * @param processDiagram
	 * @return type of current process or an empty string if not found
	 * @see ProcessEngineImpl#buildRelevantServices(ProcessDiagram) for info how
	 *      this Id is generated
	 */
	private String getCurrentType(ProcessDiagram process) {
		if (process != null) {
			DomainObjectInstance currentDoi = processEngineFacade
					.getDomainObjectInstanceForProcess(process);
			String name = "";
			if (currentDoi == null) {
				name = process.getName();
			} else {
				if (currentDoi.getId() != null) {
					name = currentDoi.getId();
				}
			}
			int separatorIndex = name
					.indexOf(DomainObjectManagerInterface.ID_SEPARATOR);
			if (separatorIndex != -1) {
				return name.substring(0, separatorIndex);
			}

			return name;
		}
		return "";
	}

	private void displayProcessModel(DoiBean db) {
		if (db == null) {
			return;
		}
		ProcessDiagram model = processEngineFacade.getModel(db.getName());
		window.displayProcess(model, true, false);
	}

	private void displayProcessExecution(DoiBean db) {
		if (db == null) {
			return;
		}
		logger.debug("Display process execution model for db: " + db.getName());
		ProcessDiagram pd = processEngineFacade.getProcessDiagram(db);
		// window.resetProcessExecution();
		// window.refreshWindow();
		window.displayProcess(pd, false, true);
	}

	@Subscribe
	public void onStep(StepEvent se) {
		try {
			// one step for process engine
			processEngineFacade.step();
			// addLog("ProcessEngine step completed");
			// update domainObjectInstances list
			window.updateDomainObjectInstancesTable(processEngineFacade
					.getDomainObjectInstances());
			// update current selected domainObjectInstance
			if (getCurrentDoiBean() == null) {
				window.selectFirstEntityInTable();
				current = getCurrentDoiBean();
			} else if (getCurrentDoiBean().getId() == null
					&& window.getSelectedEntityInTable().isEmpty()
					&& !window.getSelectedCorrelatedEntity().isEmpty()) {
				window.selectFirstEntityInTable();
				current = getCurrentDoiBean();
			} else if (!window.getSelectedCorrelatedEntity().isEmpty()) {
				updateInterface(findDoiBeanByName(window
						.getSelectedCorrelatedEntity()));
				return;
			}
			// update selected entity details
			updateInterface(getCurrentDoiBean());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void updateSelectedEntityProvidedFragments(DoiBean db) {
		if (db == null) {
			return;
		}
		List<String> response = processEngineFacade.getFragmentsFromDoiBean(db
				.getName());
		window.updateSelectedEntityProvidedFragments(response);
	}

	private void updateSelectedEntityState(DoiBean db) {
		if (db == null) {
			return;
		}
		List<String> toDisplay = new ArrayList<String>();
		DomainObjectInstance doi = processEngineFacade
				.getDomainObjectInstanceForProcess(processEngineFacade
						.getProcessDiagram(db));
		if (doi != null) {
			if (doi.getState() != null) {
				List<VariableType> state = new ArrayList<VariableType>();
				state = doi.getState().getStateVariable();
				for (VariableType var : state) {
					Element e = (Element) (var.getContent());
					if (e.getFirstChild() != null) {
						String value = e.getFirstChild().getNodeValue();
						toDisplay.add(var.getName() + " = " + value);
					} else {
						toDisplay.add(var.getName() + " = " + "");
					}
				}
			}
			window.updateSelectedEntityState(toDisplay);
		}
	}

	/**
	 * Update in window correlation of current process identified by index
	 * 
	 * Correlated DomainObjectInstances are :</br> - refinement process
	 * correlated instances, for example User -> refinement1 -> UMS</br> - and
	 * vice versa, so UMS -> refinement 1 -> User
	 * 
	 * @param db
	 */
	private void updateSelectedEntityCorrelations(DoiBean db) {
		if (db == null) {
			return;
		}
		List<String> response = new ArrayList<String>();
		ProcessDiagram process = processEngineFacade.getProcessDiagram(db);
		if (process != null) {
			// using correlated process, get origin of this correlation. So for
			// example if User -> refinement1 -> Telegram, origin for Telegram
			// is User
			DomainObjectInstance doi = processEngineFacade
					.getDomainObjectInstanceForProcess(process);
			List<DomainObjectInstance> corrs = processEngineFacade
					.getCorrelations(doi);
			if (corrs != null) {
				for (DomainObjectInstance corr : corrs) {
					if (corr != null && !response.contains(corr.getId())
							&& !doi.getId().equals(corr.getId())) {
						response.add(corr.getId());
					}
				}
			}
			// using current process, using refinements information, get in
			// correlation processes, so for example if User-> refinement 1 ->
			// Telegram, result is Telegram for user, in recursive way
			window.updateSelectedEntityCorrelations(response);
		}
	}

	/**
	 * @return instance of {@link ProcessEngineFacade}
	 */
	public ProcessEngineFacade getProcessEngineFacade() {
		return processEngineFacade;
	}

	/**
	 * @return process currently selected and executed, null if not found. Be
	 *         aware: this is process get directly from processEngine
	 */
	public ProcessDiagram getCurrentProcess() {
		return processEngineFacade.getProcessDiagram(getCurrentDoiBean());
	}

	/**
	 * Using currently selected domain object, select into interface
	 */
	public void selectDomainObject() {
		updateInterface(getCurrentDoiBean());
	}

	public void setUserData(String name, UserData data) {
		userData.put(name, data);
	}

	/**
	 * @return {@link UserData} for given userName
	 */
	public UserData getUserData(String name) {
		if (userData.get(name) == null) {
			userData.put(name, new UserData());
		}
		return userData.get(name);
	}

	/**
	 * Returns a process diagram refined up to the point of current execution
	 * 
	 * @return requested process diagram or void process diagram (no activities)
	 *         if there is no current process
	 */
	public ProcessDiagram getCurrentRefinedProcessDiagram() {
		List<ProcessActivity> activityList = new ArrayList<ProcessActivity>();
		ProcessDiagram pd = getCurrentProcess();
		if (pd == null) {
			return new ProcessDiagram();
		}
		activityList.addAll(pd.getActivities());
		ProcessActivity currentActivity = pd.getCurrentActivity();
		while (processEngineFacade.getRefinement(pd) != null) {
			pd = processEngineFacade.getRefinement(pd);
			int index = activityList.indexOf(currentActivity);
			activityList.addAll(index, pd.getActivities());
			activityList.remove(index + pd.getActivities().size());
			currentActivity = pd.getCurrentActivity();
		}
		// Create process from a list of activities
		ProcessDiagram retPd = new ProcessDiagram(activityList);
		return retPd;
	}

	/**
	 * Returns the activity currently being executed at the bottom of the
	 * refinement tree.
	 * 
	 * @return
	 */
	public ProcessActivity getCurrentlyExecutingRefinedActivity() {
		ProcessDiagram pd = getCurrentProcess();
		while (processEngineFacade.getRefinement(pd) != null) {
			pd = processEngineFacade.getRefinement(pd);
		}
		return pd.getCurrentActivity();
	}

	/**
	 * Display journey alternative in window for given username
	 * 
	 * @param userName
	 */
	public void displayAlternativesFor(String userName) {
		if (userName == null) {
			logger.error("Not possible to show alternatives for a null username");
			return;
		}
		UserData ud = userData.get(userName);
		if (ud == null) {
			logger.warn("Not found any data for name " + userName);
			return;
		}
		window.displayAlternativesWindow(ud);
	}

	/**
	 * @return selected DoiBean using interface information
	 */
	public DoiBean getCurrentDoiBean() {
		String id = window.getSelectedEntityInTable();
		if (id != null && !id.isEmpty()) {
			for (DoiBean d : processEngineFacade.getDomainObjectInstances()) {
				if (d.getId().equals(id)) {
					return d;
				}
			}
		}
		return current;
	}

	private DoiBean findDoiBeanById(String id) {
		for (DoiBean d : processEngineFacade.getDomainObjectInstances()) {
			if (d.getId().equals(id)) {
				return d;
			}
		}
		return null;
	}

	public DoiBean findDoiBeanByName(String name) {
		for (DoiBean d : processEngineFacade.getDomainObjectInstances()) {
			if (d.getName().equals(name)) {
				return d;
			}
		}
		return null;
	}

	/**
	 * Add a line of log into window
	 * 
	 * @param line
	 */
	public void addLog(String line) {
		window.addLog(line);
	}

	public void play() {
		try {
			if (PlayRunner.getDefault().getController() == null) {
				PlayRunner.getDefault().setController(this);
			}
			if (!PlayRunner.getDefault().isRunning()) {
				// PlayRunner.getDefault().stop();
				PlayRunner.getDefault().start();
				setPlayButton(false);
			} else {
				PlayRunner.getDefault().stop();
				setPlayButton(true);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * Return current user name. <br>
	 * From current displayed process, search into {@link DomainObjectInstance}
	 * if is not of user Type, then search using correlation and refinements
	 * 
	 * @return current user
	 */
	public String getCurrentUser() {
		if (getCurrentDoiBean() == null) {
			return null;
		}
		if (getCurrentDoiBean().getModel() != null) {
			DomainObjectInstance doi = processEngineFacade
					.getDomainObjectInstanceForProcess(getCurrentDoiBean()
							.getModel());
			if (doi != null) {
				if (doi.getType().startsWith("User")) {
					return doi.getId();
				} else {
					// search into correlated
					List<DomainObjectInstance> corr = processEngineFacade
							.getCorrelations(doi);
					for (DomainObjectInstance c : corr) {
						if (c != null) {
							if (c.getType().startsWith("User")) {
								return c.getId();
							}
						}
					}
				}
			}
		}
		return current.getName();
	}

	/**
	 * @param proc
	 *            - {@link ProcessDiagram} to analyze
	 * @param type
	 *            - type of domainObjectInstance to find
	 * @return correlated instance type from a process. Note: all process in
	 *         demonstrator are related to at least one type (user,flexibus,
	 *         etc..). Return empty string if error or not found
	 */
	public String getTypeForProcess(ProcessDiagram proc, String type) {
		if (proc == null) {
			logger.warn("Impossible to find user for null process");
			return "";
		}
		DomainObjectInstance doi = processEngineFacade
				.getDomainObjectInstanceForProcess(proc);
		if (doi == null) {
			return "";
		}
		if (doi.getType().startsWith(type)) {
			return doi.getId();
		} else {
			// search into correlated
			List<DomainObjectInstance> corr = processEngineFacade
					.getCorrelations(doi);
			for (DomainObjectInstance c : corr) {
				if (c != null) {
					if (c.getType().startsWith(type)) {
						return c.getId();
					}
				}
			}

		}
		return "";
	}

	public void setPlayButton(boolean b) {
		if (b) {
			window.setPlayButton();
		} else {
			window.setPauseButton();
		}
	}

	public void addExternalEvent(ExternalEvent event) {
		this.externalEvents.add(event);
	}

	public void selectMonitorTab(String name, Integer index) {
		monitorSelectedMap.put(name, index);
	}

	public void enableStep(boolean b) {
		window.enableStep(b);
	}

	public static String getBotParameters() {
		BufferedReader reader;
		String result = "";

		try {
			reader = new BufferedReader(new FileReader("botProperties.txt"));
			result = reader.readLine();

		} catch (IOException e) {
			System.err.println("Error :" + e);
		}
		// System.out.println(result);
		return result;
	}

}
