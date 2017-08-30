package eu.domainobjects.presentation.main.action.listener;

import static eu.domainobjects.DemonstratorConstant.SCENARIO_CELL_SPECIALIZATION;
import static eu.domainobjects.DemonstratorConstant.STORYBOARD1_FOLDER;
import static eu.domainobjects.DemonstratorConstant.STORYBOARD1_MAIN_XML;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.domainobjects.DemonstratorConstant;
import eu.domainobjects.utils.GeneralResourceLoader;
import eu.domainobjects.utils.ResourceLoader;

public class OpenScenarioListener implements ActionListener {

	private static final Logger logger = LogManager
			.getLogger(OpenScenarioListener.class);
	private JFileChooser chooser;

	public OpenScenarioListener() {
		chooser = new JFileChooser() {

			private static final long serialVersionUID = 7489308134784417097L;

			@Override
			public void approveSelection() {
				File f = getSelectedFile();
				if (f != null && f.exists()) {
					logger.info("Selected scenario: " + f.getAbsolutePath());
					super.approveSelection();
					try {
						ResourceLoader.loadStoryboard(f);
					} catch (JAXBException e) {
						logger.error("Error on loading storyboard " + f, e);
						JOptionPane.showMessageDialog(this,
								"Scenario file not valid");
					}
				} else {
					logger.error("Selected scenario file null or not exist");
				}
			}

		};
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Allow Ensembles scenario (.xml)", "xml");
		chooser.setFileFilter(filter);
	}

	private void loadScenario(String folder, String mainXml) {
		File f = null;
		try {
			URL res = GeneralResourceLoader.getResource(ResourceLoader
					.getWorkingDir()
					+ DemonstratorConstant.FILE_SEPARATOR
					+ folder + DemonstratorConstant.FILE_SEPARATOR + mainXml);
			f = new File(res.toURI());
			ResourceLoader.loadStoryboard(f);
		} catch (JAXBException | URISyntaxException | IllegalArgumentException e) {
			logger.warn("Trying to load something inside jar");
			logger.error("Error loading default storyboard1 ", e);
			e.printStackTrace();
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case SCENARIO_CELL_SPECIALIZATION:
			loadScenario(STORYBOARD1_FOLDER, STORYBOARD1_MAIN_XML);
			break;
//		case SCENARIO_COLLECTIVE_ADAPTATION_1:
//			loadScenario(SCENARIO_COLLECTIVE_ADAPTATION_1_FOLDER,
//					SCENARIO_COLLECTIVE_ADAPTATION_1_MAIN_XML);
//			break;
//		case SCENARIO_COLLECTIVE_ADAPTATION_2:
//			loadScenario(SCENARIO_COLLECTIVE_ADAPTATION_2_FOLDER,
//					SCENARIO_COLLECTIVE_ADAPTATION_2_MAIN_XML);
//			break;

		default:
			chooser.showOpenDialog(null);
			break;
		}
	}
}
