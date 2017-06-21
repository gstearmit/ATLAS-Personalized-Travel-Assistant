package eu.domainobjects.utils;

public class ExternalEvent {

	private String event;
	private String ensemble;

	public ExternalEvent(String event, String ensemble) {
		this.event = event;
		this.ensemble = ensemble;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getEnsemble() {
		return ensemble;
	}

	public void setEnsemble(String ensemble) {
		this.ensemble = ensemble;
	}

}
