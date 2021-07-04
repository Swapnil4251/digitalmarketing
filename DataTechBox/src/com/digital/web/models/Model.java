package com.digital.web.models;

import java.util.List;

import javax.faces.context.FacesContext;

import com.digital.web.core.Application;
import com.digital.web.core.ApplicationContextProvider;
import com.digital.web.core.DynamicObject;
import com.digital.web.datasource.DataSource;
import com.digital.web.datasource.EntityManagerDataSource;
import com.digital.web.utils.AppUtil;

/**
 * @author swapnilsarwade
 *
 */
public abstract class Model extends DynamicObject {

	DataSource dataSource;
	
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("rawtypes")
	DynamicObject data;
	
	@SuppressWarnings("rawtypes")
	public DynamicObject getData() {
		if (data == null) {
			data = new DynamicObject();
		}
		return data;
	}

	public DataSource getDataSource() {
		if (dataSource == null) {
			dataSource = Application.getStaticBean("entityManagerDataSource", EntityManagerDataSource.class);
		}
		return dataSource;
	}
	
	public String getTitle() {
		return "";
	}
	
	public String getViewId() {
		return FacesContext.getCurrentInstance().getViewRoot().getViewId();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<DynamicObject> getResults(String query) {
		return AppUtil.EMPTY_LIST;
	}
}
