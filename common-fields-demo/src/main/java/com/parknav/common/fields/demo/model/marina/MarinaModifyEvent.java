package com.parknav.common.fields.demo.model.marina;

/** Fired when marina is modified. Fields of enclosed object depends on initiator. */
public class MarinaModifyEvent extends MarinaEvent {

	public MarinaModifyEvent(Marina marina, Object source) {
		super(marina, source);
	}
	
	private static final long serialVersionUID = 1L;

}
