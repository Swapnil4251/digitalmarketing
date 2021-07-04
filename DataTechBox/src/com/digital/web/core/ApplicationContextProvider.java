/**
 * 
 */
package com.digital.web.core;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author swapnilsarwade
 *
 */
public class ApplicationContextProvider implements ApplicationContextAware {

	private static ApplicationContext context;
	
	public static ApplicationContext getApplicationContext() {
		return context;
	}
	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@SuppressWarnings("static-access")
	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}

}
