package com.suntendy.queue.util.scriptsession.event;

import org.springframework.context.ApplicationEvent;

public class RzdbTs extends ApplicationEvent{
	private static final long serialVersionUID = 1L;

	public RzdbTs(Object source) {
		super(source);
	}
}
