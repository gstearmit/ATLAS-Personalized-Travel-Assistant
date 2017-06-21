package eu.domainobjects.presentation.main;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import eu.domainobjects.controller.MainController;

public class MonitorCellListRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = -2018744054171651337L;
	private MainWindow window;
	private int styleIndex = -1;

	public MonitorCellListRenderer(MainWindow window) {
		this.window = window;
		MainController.register(this);
	}

	public void setStyle(int i) {
		this.styleIndex = i;
	}

	public void resetStyle() {
		this.styleIndex = -1;
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		Component c = super.getListCellRendererComponent(list, value, index,
				isSelected, cellHasFocus);
		if (styleIndex == index) {
			setForeground(Color.RED);
		}
		return c;
	}

}
