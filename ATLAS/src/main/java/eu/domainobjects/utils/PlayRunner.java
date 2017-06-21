package eu.domainobjects.utils;

import eu.domainobjects.DemonstratorConstant;
import eu.domainobjects.controller.MainController;
import eu.domainobjects.controller.events.StepEvent;

public class PlayRunner implements Runnable {

	private static final PlayRunner DEFAULT_THREAD = new PlayRunner();

	protected Thread thread = null;

	private MainController controller;

	public static PlayRunner getDefault() {
		return DEFAULT_THREAD;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		while (true) {
			if (controller == null) {
				stop();
				break;
			}
			MainController.post(new StepEvent());
			try {
				Thread.sleep(DemonstratorConstant.getStepTime());
			} catch (InterruptedException e) {
				return;
			}
		}
	}

	public boolean isRunning() {
		return thread != null && thread.isAlive();
	}

	public void start() {
		stop();
		controller.setPlayButton(false);
		thread = new Thread(this);
		thread.setDaemon(true);
		thread.start();
	}

	public void stop() {
		if (thread != null && thread.isAlive()) {
			thread.interrupt();
		}
		if (controller != null) {
			controller.setPlayButton(true);
		}
		thread = null;
	}

	public void setController(MainController controller) {
		this.controller = controller;
	}

	public MainController getController() {
		return controller;
	}

}