/**
 * 
 */
package com.digital.web.datasource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.digital.web.core.DynamicObject;

/**
 * @author swapnilsarwade
 *
 */
public abstract class DataSource extends DynamicObject<Object> {

	public static final String DB_HOST = "host";
	
	public static final String DB_PORT = "port";
	
	public static final String DB_PROVIDER = "databaseProvider";
	
	/**
	 * @return
	 */
	public String getHost() {
		return this.getString(DB_HOST);
	}
	
	/**
	 * @param host
	 */
	public void setHost(String host) {
		this.set(DB_HOST, host);
	}
	
	/**
	 * @return
	 */
	public String getPort() {
		return this.getString(DB_PORT);
	}
	
	/**
	 * @param port
	 */
	public void setPort(String port) {
		this.set(DB_PORT, port);
	}
	
	/**
	 * @return
	 */
	public String getDatabaseProvider() {
		if (this.isFieldNull(DB_PROVIDER)) {
			this.set(DB_PROVIDER, "mysql");
		}
		return this.getString(DB_PROVIDER);
	}
	
	/**
	 * @param host
	 */
	public void setDatabaseProvider(String db) {
		this.set(DB_PROVIDER, db);
	}
	

	public abstract <T extends DynamicObject> T findOne(T parameters);
	
	public abstract List<DynamicObject> find(String sql, Map<String, Object> params);
	
	public abstract <T extends DynamicObject> T update(T parameters) throws Exception;
	
	public abstract int update(String sql);
	
	public abstract void insert(String sql) throws Exception;
}
