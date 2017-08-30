package eu.domainobjects.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.domainobjects.controller.MainController;
import eu.domainobjects.presentation.main.events.StoryboardLoadedEvent;
import eu.domainobjects.presentation.main.map.MapInfo;
import eu.domainobjects.presentation.main.map.MapInfoLoader;
import eu.domainobjects.presentation.main.map.Routes.Route;
import eu.domainobjects.presentation.main.map.Routes.Route.Leg;

public class ResourceLoader {

	private static final Logger logger = LogManager
			.getLogger(ResourceLoader.class);

	private static MapInfo mapInfo = null;

	private static Storyboard storyboard;

	private static File storyboardFile;

	// private static File routeFile;

	private static File scenarioFile;

	private static File coordinateFile;

	private static final String FILE_SEPARATOR = System
			.getProperty("file.separator");

	private static final String COORDINATE_FILENAME = "coordinates.properties";

	private static final String WORKING_DIR = "workingDir";

	private static Properties coordinates;

	private static List<eu.domainobjects.presentation.main.map.Routes.Route> routes;

	public static MapInfo load(String string) {
		if (mapInfo != null) {
			return mapInfo;
		}
		try {
			MapInfoLoader mil = new MapInfoLoader();
			mapInfo = mil.loadInfo("map/mapinfo.properties");
		} catch (IOException e) {
			logger.error("Error on loading map/mapinfo.properties", e);
			return null;
		}
		return mapInfo;
	}

	public static MapInfo getMapInfo() {
		return load("mapinfo.properties");
	}

	public static void loadStoryboard(File f) throws JAXBException {
		try {
			// read storyboard
			storyboard = null;
			JAXBContext context = JAXBContext.newInstance(Storyboard.class);
			storyboard = (Storyboard) context.createUnmarshaller().unmarshal(f);
			storyboardFile = f;
			// read routeFile
			// routeFile = new File(storyboardFile.getParent() + FILE_SEPARATOR
			// + storyboard.getRoutes());
			// if (!routeFile.exists()) {
			// logger.warn("File " + routeFile.getAbsolutePath()
			// + " does not exist");
			// return;
			// }
			// read scenarioFile
			scenarioFile = new File(storyboardFile.getParent() + FILE_SEPARATOR
					+ storyboard.getScenario());
			if (!scenarioFile.exists()) {
				logger.warn("File " + scenarioFile.getAbsolutePath()
						+ " does not exist");
				return;
			}

			logger.info("Storyboard loaded");
			// notify eventBus
			MainController.post(new StoryboardLoadedEvent());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Error on loading storyboard definition " + e.getMessage());
			logger.error(e.getMessage(), e);
		}
	}

	public static Storyboard getStoryboard() {
		return storyboard;
	}

	public static File getStoryboardFile() {
		return storyboardFile;
	}

	// public static File getRouteFile() {
	// return routeFile;
	// }

	// public static List<Route> getRoutes() {
	// if (routes != null) {
	// return routes;
	// }
	// try {
	// JAXBContext context = JAXBContext.newInstance(Routes.class);
	// routes = ((Routes) context.createUnmarshaller().unmarshal(
	// getRouteFile())).getRoute();
	// return routes;
	// } catch (Exception e) {
	// return new ArrayList<Routes.Route>();
	// }
	// }

	public static File getScenarioFile() {
		return scenarioFile;
	}

	private static File loadCoordinateFile() {
		if (coordinateFile == null) {
			coordinateFile = new File(storyboardFile.getParent()
					+ FILE_SEPARATOR + COORDINATE_FILENAME);
		}
		return coordinateFile;
	}

	public static Properties loadCoordinates() {
		if (coordinates == null) {
			coordinates = new Properties();
			try {
				coordinates.load(new FileInputStream(loadCoordinateFile()));
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				return new Properties();
			}
		}
		return coordinates;
	}

	public static BufferedImage getImageForTransportType(String transportType) {
		try {
			URL resource = ResourceLoader.class
					.getResource("/images/waypoint_white_" + transportType
							+ ".png");
			return ImageIO.read(resource);
		} catch (Exception ex) {
			try {
				URL resource = ResourceLoader.class
						.getResource("/images/waypoint_white_" + transportType
								+ ".png");
				return ImageIO.read(resource);
			} catch (IOException e) {
				logger.error("Impossible to load /images/waypoint_white_"
						+ transportType + ".png");
			}
		}
		return null;
	}

	public static Alternative convertFromRouteToAlternative(Route route) {
		double walkingDistance = 0;
		long travelTime = 0;
		double cost = 0;
		String modes = "";
		List<Leg> leg = route.getLeg();
		for (int j = 0; j < leg.size(); j++) {
			walkingDistance += leg.get(j).getWalkingDistance();
			travelTime += leg.get(j).getDuration();
			cost += leg.get(j).getCost();
			modes += leg.get(j).getTransportType().getType() + " ";
		}
		Alternative alternative = new Alternative(
				Integer.valueOf(route.getId()), route.getNoOfChanges(),
				walkingDistance, travelTime, cost, modes, leg, 0);
		return alternative;
	}

	public static List<Alternative> getAllAlternatives() {
		List<Alternative> alternatives = new ArrayList<Alternative>();
		for (Route route : routes) {
			alternatives.add(convertFromRouteToAlternative(route));
		}
		return alternatives;
	}

	public static Optional<Alternative> findAlternative(int id) {
		List<Alternative> alternatives = getAllAlternatives();
		return alternatives.stream().filter(a -> a.getId() == id).findFirst();
	}

	public static String getWorkingDir() {
		Properties prop = new Properties();
		try {
			prop.load(ResourceLoader.class.getClassLoader()
					.getResourceAsStream("demonstrator.properties"));
			return prop.getProperty(WORKING_DIR, "");
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
}
