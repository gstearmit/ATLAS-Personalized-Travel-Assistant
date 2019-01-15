package eu.domainobjects.presentation.main.action;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JViewport;
import javax.swing.SwingUtilities;

import eu.domainobjects.presentation.main.process.ServiceModelPanel;

public class DraggingMouseAdapterServices extends MouseAdapter {

	private Point origin;
	private ServiceModelPanel serviceModelPanel;

	public DraggingMouseAdapterServices(ServiceModelPanel serviceModelPanel) {
		this.serviceModelPanel = serviceModelPanel;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		origin = new Point(e.getPoint());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (origin != null) {
			JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(
					JViewport.class, serviceModelPanel);
			if (viewPort != null) {
				int deltaX = origin.x - e.getX();
				int deltaY = origin.y - e.getY();

				Rectangle view = viewPort.getViewRect();
				view.x += deltaX;
				view.y += deltaY;

				serviceModelPanel.scrollRectToVisible(view);
			}
		}
	}

}
