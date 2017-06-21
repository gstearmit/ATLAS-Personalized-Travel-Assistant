package eu.domainobjects.presentation.main.action.listener;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import eu.domainobjects.presentation.main.MainWindow;

public class SelectPropertyListener implements ListSelectionListener {

	private MainWindow window;

	public SelectPropertyListener(MainWindow mainWindow) {
		this.window = mainWindow;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e != null && e.getSource() instanceof JList<?>) {
			if (e.getValueIsAdjusting()) {
				return;
			}
			JList<?> fsm = (JList<?>) e.getSource();
			for (int i = e.getFirstIndex(); i <= e.getLastIndex(); i++) {
				if (fsm.isSelectedIndex(i)) {
					setPropertyWithName(fsm.getSelectedValue());
					break;
				}
			}
		}
	}

	public void setPropertyWithName(Object selectedValue) {
		if (selectedValue instanceof String) {
			String propertyName = (String) selectedValue;
			window.getController().updatePropertyModelTab(propertyName);
		}
	}
}
