package eu.domainobjects.presentation.main.action.listener;

import static eu.domainobjects.DemonstratorConstant.STEP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import eu.domainobjects.controller.MainController;
import eu.domainobjects.controller.events.StepEvent;

public class StepButtonActionListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case STEP:
			MainController.post(new StepEvent());
			break;
		}
	}

}
