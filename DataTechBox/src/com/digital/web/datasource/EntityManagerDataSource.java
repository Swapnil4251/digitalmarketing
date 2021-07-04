/**
 * 
 */
package com.digital.web.datasource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.digital.web.core.Application;
import com.digital.web.core.DynamicObject;

/**
 * @author swapnilsarwade
 *
 */
public class EntityManagerDataSource extends DataSource {

	/**  */
	private static final long serialVersionUID = 1L;

	public JdbcTemplate getJdbcTemplate() {
		return Application.getStaticBean("jdbcTemplate", JdbcTemplate.class);
	}
	
	@SuppressWarnings("rawtypes")
	public <T extends DynamicObject> T findOne(T parameters) {
		//getJdbcTemplate().queryForMap()
		return null;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<DynamicObject> find(String sql, Map<String, Object> params) {
		
		try {
			List<Map<String, Object>> queryForList = getJdbcTemplate().queryForList(sql);
			List<DynamicObject> list = new ArrayList<DynamicObject>();
			for (Map<String, Object> map : queryForList) {
				DynamicObject obj = new DynamicObject(this);
				obj.putAll(map);
				list.add(obj);
			}
			
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public <T extends DynamicObject> T update(T parameters) throws Exception {
		try {
			
		} catch (Exception e) {
			throw e;
		}
		return parameters;
	}
	
	public int update(String sql) {
		return getJdbcTemplate().update(sql);
	}

	@Override
	public void insert(String sql) throws Exception {
		getJdbcTemplate().execute(sql);
	}
}
