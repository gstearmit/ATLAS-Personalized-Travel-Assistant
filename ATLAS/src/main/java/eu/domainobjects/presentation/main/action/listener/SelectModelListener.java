package eu.domainobjects.presentation.main.action.listener;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import eu.domainobjects.presentation.main.MainWindow;

public class SelectModelListener implements ListSelectionListener {

	private MainWindow window;

	public SelectModelListener(MainWindow mainWindow) {
		this.window = mainWindow;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e != null && e.getSource() instanceof JList<?>) {
			if (e.getValueIsAdjusting()) {
				return;
			}
			JList<?> lsm = (JList<?>) e.getSource();
			for (int i = e.getFirstIndex(); i <= e.getLastIndex(); i++) {
				if (lsm.isSelectedIndex(i)) {
					setCurrentModelWithName(lsm.getSelectedValue());
					break;
				}
			}
		}
	}

	public void setCurrentModelWithName(Object selectedValue) {
		if (selectedValue instanceof String) {
			String modelName = (String) selectedValue;
			window.getController().updateInterfaceModelsTab(modelName);
		}
	}
}
