package eu.domainobjects.presentation.main.action.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import eu.domainobjects.controller.MainController;
import eu.domainobjects.presentation.main.MainWindow;

public class SelectedComboEntityListener implements ActionListener {

	private MainWindow window;

	public SelectedComboEntityListener(MainWindow mainWindow) {
		this.window = mainWindow;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() != null && e.getSource() instanceof JComboBox<?>) {
			JComboBox<?> combobox = ((JComboBox<?>) e.getSource());
			String selected = (String) combobox.getSelectedItem();
			if (selected != null && !selected.isEmpty()) {
				// setCurrentProcessWithName(selected);
				MainController.post(new DomainObjectDefinitionSelectionByName(
						selected));

			}

		}
	}

	// public void setCurrentProcessWithName(String selectedValue) {
	// for (DoiBean db : window.getController().getProcessEngineFacade()
	// .getDomainObjectInstances()) {
	// if (db.getName().equals(selectedValue)) {
	// window.getController().updateInterface(db);
	// break;
	// }
	// }
	// }
}
