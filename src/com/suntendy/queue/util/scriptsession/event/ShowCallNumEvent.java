package com.suntendy.queue.util.scriptsession.event;

import org.springframework.context.ApplicationEvent;

public class ShowCallNumEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;
	
	public ShowCallNumEvent(Object source) {
		super(source);
	}
}