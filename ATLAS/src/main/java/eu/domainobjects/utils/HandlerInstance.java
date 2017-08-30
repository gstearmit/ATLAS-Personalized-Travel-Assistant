package eu.domainobjects.utils;

import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.jaxb.EventHandlerType;


public class HandlerInstance {

	private EventHandlerType handler;
	private DomainObjectInstance doi;

	public HandlerInstance(EventHandlerType handler) {
		this.handler = handler;
	}

	public EventHandlerType getHandler() {
		return handler;
	}

	public void setHandler(EventHandlerType handler) {
		this.handler = handler;
	}

	public DomainObjectInstance getDoi() {
		return doi;
	}

	public void setDoi(DomainObjectInstance doi) {
		this.doi = doi;
	}

}
