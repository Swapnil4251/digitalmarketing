package com.digital.web.actions;
import com.digital.web.core.DynamicObject;

public abstract class Action<T> extends DynamicObject<T> implements Runnable {
	
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	private volatile boolean checkUsage;
	
	public Action() {
		super();
	}
	
	public Action(Object ctx) {
		super();
		this.setContext(ctx);
	}
	

	public boolean isCheckUsage() {
		return checkUsage;
	}

	public void setCheckUsage(boolean checkUsage) {
		this.checkUsage = checkUsage;
	}

	
	/**
	 * 
	 */
	public void beforeAction() {
		
	};
	
	/**
	 * @return
	 */
	public Object performAction() {
		
		beforeAction();
		
		Object result = execute(null);
		
		afterAction();
		
		return result;
	}
	
	/**
	 * @param params
	 * @return
	 */
	public abstract Object execute(T params);
	
	/**
	 * @param caller
	 * @throws Exception
	 */
	public Thread executeAsync(Object caller) throws Exception {
		try {
			Thread thread = new Thread(this);
			thread.start();
			return thread;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public Thread executeAsync(Object caller, boolean isDaemon) throws Exception {
		try {
			Thread thread = new Thread(this);
			thread.setDaemon(isDaemon);
			thread.start();
			return thread;
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public void run() {
		this.performAction();
	}
	
	/**
	 * 
	 */
	public void afterAction() {
		
	}
}
