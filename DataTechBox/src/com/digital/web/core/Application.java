/**
 * 
 */
package com.digital.web.core;

import org.springframework.context.ApplicationContext;

/**
 * @author swapnilsarwade
 *
 */
public class Application {

	/**
	 * Returns Spring ApplicationContext.
	 * @return
	 */
	public static ApplicationContext getContext() {
		return ApplicationContextProvider.getApplicationContext();
	}
	
	/**
	 * Returns a bean registered in Spring ApplicationContext.
	 * @param id
	 * @param beanClass
	 * @return
	 */
	public static <T extends Object> T getStaticBean(String id, Class<T> beanClass) {
		return getContext().getBean(id, beanClass);
	}
	
}
