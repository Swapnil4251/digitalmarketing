/**
 * 
 */
package com.digital.web.datasource;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.digital.web.core.DynamicObject;

/**
 * @author swapnilsarwade
 *
 */
@Service
public class SqlDataSource extends DataSource {

	@Override
	public <T extends DynamicObject> T findOne(T parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DynamicObject> find(String sql, Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends DynamicObject> T update(T parameters) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(String sql) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void insert(String sql) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
