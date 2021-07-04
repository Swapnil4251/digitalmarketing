package com.digital.web.core;

import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import com.digital.web.datasource.DataSource;
import com.digital.web.datasource.EntityManagerDataSource;
import com.digital.web.utils.AppUtil;

/**
 * @author swapnilsarwade
 *
 */
public class DynamicObject<T> extends HashMap<Object, T> {

	private ResourceBundle labels;
	
	Logger logger = Logger.getLogger(this.getClass());
	
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	Object context;
	
	public DynamicObject() {
		// TODO Auto-generated constructor stub
	}
	
	public DynamicObject(Object ctx) {
		this.context = ctx;
	}
	
	public Object getContext() {
		return context;
	}

	public void setContext(Object context) {
		this.context = context;
	}
	
	public ResourceBundle getLabels() {
		if (labels == null) {
			FacesContext context = FacesContext.getCurrentInstance();
		    labels = ResourceBundle.getBundle("resources.Application_en", Locale.ENGLISH);
		}
		return labels;
	}
	
	public Logger getLogger() {
		return logger;
	}

	public String getProperty(String key) {
		String value = getLabels().getString(key);
		if (AppUtil.isNullOrEmpty(value) && key != null) {
			value = key;
		}
		return value;
	}
	
	@SuppressWarnings("deprecation")
	public void logMessage(String msg) {
		getLogger().log(Priority.INFO, msg);
	}
	
	@Override
	public T get(Object key) {
		return super.get(key);
	}
	
	public T set(Object key, T value) {
		this.put(key, value);
		return value; 
	}
	
	public String getString(Object key) {
		return super.get(key) == null ? null : String.valueOf(super.get(key));
	}
	
	public int getInt(Object key)  {
		return Integer.parseInt(this.getString(key));
	}
	
	public boolean isFieldNull(String key) {
		return AppUtil.isNullOrEmpty(this.getString(key));
	}
	
	public boolean getBoolean(String key) {
		return Boolean.parseBoolean(key);
	}
}
